
package org.skyllias.alomatia.filter.buffered;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.Kernel;

/** {@link ResizableBufferedImageOperation} that, given a {@link Kernel},
 *  produces an image with dimensions equal to the original image's plus the
 *  kernel's (minus 1).
 *  The original image is drawn in the center, while the extra edges are filled
 *  with the reflection of the edges in the original image. */

public class KernelExpandingBufferedImageOperation implements ResizableBufferedImageOperation
{
  private final Kernel kernel;

//==============================================================================

  public KernelExpandingBufferedImageOperation(Kernel aKernel) {kernel = aKernel;}

//==============================================================================

  /** The kernel dimensions are taken into account. */

  @Override
  public Dimension getOutputImageDimension(BufferedImage src)
  {
    int width  = src.getWidth()  + kernel.getWidth()  - 1;
    int height = src.getHeight() + kernel.getHeight() - 1;

    return new Dimension(width, height);
  }

//------------------------------------------------------------------------------

  /** Draws the input image to the outputimage, along with each of the eight
   *  extra edge fragments.
   *  In the uncommon event of a kernel bigger than the source image, then the
   *  result might not be as expected, but nothing should crash. */

  @Override
  public void filter(BufferedImage inputImage, BufferedImage outputImage)
  {
    int horizontalEdgeLeft  = Math.min(kernel.getXOrigin(), inputImage.getWidth() - 1);    // for some reason, when the width or height of the kernel is even, the convolution seems to handle wrong the X or Y origin and an extra pixel is regarded as edge. Anyway, that effect is not considered here because it is assumed that the ConvolveOp.EDGE_NO_OP condition is used and a one-pixel stripe of non-convolved image is not a big deal
    int horizontalEdgeRight = Math.min(kernel.getWidth() - kernel.getXOrigin() - 1,
                                       inputImage.getWidth() - 1);
    int verticalEdgeTop     = Math.min(kernel.getYOrigin(), inputImage.getHeight() - 1);
    int verticalEdgeBottom  = Math.min(kernel.getHeight() - kernel.getYOrigin() - 1,
                                       inputImage.getHeight() - 1);
    int srcRight            = inputImage.getWidth();
    int srcBottom           = inputImage.getHeight();
    int destRight           = outputImage.getWidth();
    int destBottom          = outputImage.getHeight();
    int centralRightX       = horizontalEdgeLeft + srcRight;
    int centralBottomY      = verticalEdgeTop + srcBottom;

    Graphics2D graphics = outputImage.createGraphics();
    graphics.drawImage(inputImage, horizontalEdgeLeft, verticalEdgeTop, null);  // center
    graphics.drawImage(inputImage,
                       horizontalEdgeLeft, verticalEdgeTop,
                       0, 0,
                       0, 0,
                       horizontalEdgeLeft, verticalEdgeTop,
                       null);                                                   // left, top corner
    graphics.drawImage(inputImage,
                       horizontalEdgeLeft, verticalEdgeTop,
                       0, centralBottomY,
                       0, 0,
                       horizontalEdgeLeft, srcBottom,
                       null);                                                   // left stripe
    graphics.drawImage(inputImage,
                       horizontalEdgeLeft, destBottom,
                       0, centralBottomY,
                       0, srcBottom - verticalEdgeBottom,
                       horizontalEdgeLeft, srcBottom,
                       null);                                                   // left, bottom corner
    graphics.drawImage(inputImage,
                       horizontalEdgeLeft, destBottom,
                       centralRightX, centralBottomY,
                       0, srcBottom - verticalEdgeBottom,
                       srcRight, srcBottom,
                       null);                                                   // bottom stripe
    graphics.drawImage(inputImage,
                       destRight, destBottom,
                       centralRightX, centralBottomY,
                       srcRight - horizontalEdgeRight, srcBottom - verticalEdgeBottom,
                       srcRight, srcBottom,
                       null);                                                   // right, bottom corner
    graphics.drawImage(inputImage,
                       destRight, verticalEdgeTop,
                       centralRightX, centralBottomY,
                       srcRight - horizontalEdgeRight, 0,
                       srcRight, srcBottom,
                       null);                                                   // right stripe
    graphics.drawImage(inputImage,
                       destRight, verticalEdgeTop,
                       centralRightX, 0,
                       srcRight - horizontalEdgeRight, 0,
                       srcRight, verticalEdgeTop,
                       null);                                                   // right, top corner
    graphics.drawImage(inputImage,
                       horizontalEdgeLeft, verticalEdgeTop,
                       centralRightX, 0,
                       0, 0,
                       srcRight, verticalEdgeTop,
                       null);                                                   // top stripe

    graphics.dispose();
  }

//------------------------------------------------------------------------------

}
