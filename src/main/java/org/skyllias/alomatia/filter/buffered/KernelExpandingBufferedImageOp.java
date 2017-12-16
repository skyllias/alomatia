
package org.skyllias.alomatia.filter.buffered;

import java.awt.*;
import java.awt.image.*;

/** {@link BufferedImageOp} that, given a {@link Kernel}, produces an image with
 *  dimensions equal to the original image's plus the kernel's (minus 1).
 *  The original image is drawn in the center, while the extra edges are filled
 *  with the reflection of the edges in the original image. */

public class KernelExpandingBufferedImageOp extends HintlessBufferedImageOp
{
  private Kernel kernel;

//==============================================================================

  public KernelExpandingBufferedImageOp(Kernel aKernel) {kernel = aKernel;}

//==============================================================================

  /** The kernel dimensions are taken into account. */

  @Override
  protected Dimension getDestImageDimensions(BufferedImage src)
  {
    int width  = src.getWidth()  + kernel.getWidth()  - 1;
    int height = src.getHeight() + kernel.getHeight() - 1;

    return new Dimension(width, height);
  }

//------------------------------------------------------------------------------

  /** Draws the src into dest, along with each of the eight extra edge fragments.
   *  In the uncommon event of a kernel bigger than the source image, then the
   *  result might not be as expected, but nothing should crash. */

  @Override
  public BufferedImage filter(BufferedImage src, BufferedImage dest)
  {
    if (dest == null) dest = createCompatibleDestImage(src, null);

    int horizontalEdgeLeft  = Math.min(kernel.getXOrigin(), src.getWidth() - 1);    // for some reason, when the width or height of the kernel is even, the convolution seems to handle wrong the X or Y origin and an extra pixel is regarded as edge. Anyway, that effect is not considered here because it is assumed that the ConvolveOp.EDGE_NO_OP condition is used and a one-pixel stripe of non-convolved image is not a big deal
    int horizontalEdgeRight = Math.min(kernel.getWidth() - kernel.getXOrigin() - 1,
                                       src.getWidth() - 1);
    int verticalEdgeTop     = Math.min(kernel.getYOrigin(), src.getHeight() - 1);
    int verticalEdgeBottom  = Math.min(kernel.getHeight() - kernel.getYOrigin() - 1,
                                       src.getHeight() - 1);
    int srcRight            = src.getWidth();
    int srcBottom           = src.getHeight();
    int destRight           = dest.getWidth();
    int destBottom          = dest.getHeight();
    int centralRightX       = horizontalEdgeLeft + srcRight;
    int centralBottomY      = verticalEdgeTop + srcBottom;

    Graphics2D graphics = dest.createGraphics();
    graphics.drawImage(src, horizontalEdgeLeft, verticalEdgeTop, null);         // center
    graphics.drawImage(src,
                       horizontalEdgeLeft, verticalEdgeTop,
                       0, 0,
                       0, 0,
                       horizontalEdgeLeft, verticalEdgeTop,
                       null);                                                   // left, top corner
    graphics.drawImage(src,
                       horizontalEdgeLeft, verticalEdgeTop,
                       0, centralBottomY,
                       0, 0,
                       horizontalEdgeLeft, srcBottom,
                       null);                                                   // left stripe
    graphics.drawImage(src,
                       horizontalEdgeLeft, destBottom,
                       0, centralBottomY,
                       0, srcBottom - verticalEdgeBottom,
                       horizontalEdgeLeft, srcBottom,
                       null);                                                   // left, bottom corner
    graphics.drawImage(src,
                       horizontalEdgeLeft, destBottom,
                       centralRightX, centralBottomY,
                       0, srcBottom - verticalEdgeBottom,
                       srcRight, srcBottom,
                       null);                                                   // bottom stripe
    graphics.drawImage(src,
                       destRight, destBottom,
                       centralRightX, centralBottomY,
                       srcRight - horizontalEdgeRight, srcBottom - verticalEdgeBottom,
                       srcRight, srcBottom,
                       null);                                                   // right, bottom corner
    graphics.drawImage(src,
                       destRight, verticalEdgeTop,
                       centralRightX, centralBottomY,
                       srcRight - horizontalEdgeRight, 0,
                       srcRight, srcBottom,
                       null);                                                   // right stripe
    graphics.drawImage(src,
                       destRight, verticalEdgeTop,
                       centralRightX, 0,
                       srcRight - horizontalEdgeRight, 0,
                       srcRight, verticalEdgeTop,
                       null);                                                   // right, top corner
    graphics.drawImage(src,
                       horizontalEdgeLeft, verticalEdgeTop,
                       centralRightX, 0,
                       0, 0,
                       srcRight, verticalEdgeTop,
                       null);                                                   // top stripe

    graphics.dispose();
    return dest;
  }

//------------------------------------------------------------------------------

}
