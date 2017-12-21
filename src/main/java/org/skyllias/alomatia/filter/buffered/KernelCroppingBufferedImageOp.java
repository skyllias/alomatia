
package org.skyllias.alomatia.filter.buffered;

import java.awt.*;
import java.awt.image.*;

/** {@link BufferedImageOp} that, given a {@link Kernel}, produces an image with
 *  dimensions equal to the original image's minus the kernel's (minus 1).
 *  The center of the original image is drawn in it, while the extra edges are
 *  discarded in the destination image. */

public class KernelCroppingBufferedImageOp extends HintlessBufferedImageOp
{
  private Kernel kernel;

//==============================================================================

  public KernelCroppingBufferedImageOp(Kernel aKernel) {kernel = aKernel;}

//==============================================================================

  /** The kernel dimensions are taken into account. */

  @Override
  protected Dimension getDestImageDimensions(BufferedImage src)
  {
    int width  = src.getWidth()  - (kernel.getWidth()  - 1);
    int height = src.getHeight() - (kernel.getHeight() - 1);

    return new Dimension(width, height);
  }

//------------------------------------------------------------------------------

  /** Draws the src into dest, removing the fragments that correspond to the kernel.
   *  The size of src MUST be bigger than the kernel's. */

  @Override
  public BufferedImage filter(BufferedImage src, BufferedImage dest)
  {
    if (dest == null) dest = createCompatibleDestImage(src, null);

    Graphics2D graphics = dest.createGraphics();
    Image centralImage  = src.getSubimage(kernel.getXOrigin(), kernel.getYOrigin(),
                                          dest.getWidth(), dest.getHeight());
    graphics.drawImage(centralImage, 0, 0, null);

    graphics.dispose();
    return dest;
  }

//------------------------------------------------------------------------------

}
