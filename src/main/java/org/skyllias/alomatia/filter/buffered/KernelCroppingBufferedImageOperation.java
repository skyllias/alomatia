
package org.skyllias.alomatia.filter.buffered;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.Kernel;

/** {@link ResizableBufferedImageOperation} that, given a {@link Kernel},
 *  produces an image with dimensions equal to the original image's minus the
 *  kernel's (minus 1).
 *  The center of the original image is drawn in it, while the extra edges are
 *  discarded in the destination image. */

public class KernelCroppingBufferedImageOperation implements ResizableBufferedImageOperation
{
  private final Kernel kernel;

//==============================================================================

  public KernelCroppingBufferedImageOperation(Kernel aKernel) {kernel = aKernel;}

//==============================================================================

  /** The kernel dimensions are taken into account. */

  @Override
  public Dimension getOutputImageDimension(BufferedImage src)
  {
    int width  = src.getWidth()  - (kernel.getWidth()  - 1);
    int height = src.getHeight() - (kernel.getHeight() - 1);

    return new Dimension(width, height);
  }

//------------------------------------------------------------------------------

  /** Draws the input image to the output image, removing the fragments that
   *  correspond to the kernel.
   *  The size of src MUST be bigger than the kernel's. */

  @Override
  public void filter(BufferedImage inputImage, BufferedImage outputImage)
  {
    Graphics2D graphics = outputImage.createGraphics();
    Image centralImage  = inputImage.getSubimage(kernel.getXOrigin(), kernel.getYOrigin(),
                                                 outputImage.getWidth(), outputImage.getHeight());
    graphics.drawImage(centralImage, 0, 0, null);

    graphics.dispose();
  }

//------------------------------------------------------------------------------

}
