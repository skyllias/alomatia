
package org.skyllias.alomatia.filter.buffered;

import java.awt.image.*;

/** Superclass for the {@link BufferedImageOp} that keep the image size.
 *  Subclasses must implement doFilter(BufferedImage, BufferedImage). */

public abstract class BasicBufferedImageOp extends HintlessBufferedImageOp
{
//==============================================================================

  /** Just calls doFilter ensuring that the destination is not null. */

  @Override
  public BufferedImage filter(BufferedImage src, BufferedImage dest)
  {
    if (dest == null) dest = createCompatibleDestImage(src, null);

    doFilter(src, dest);
    return dest;
  }

//------------------------------------------------------------------------------

  /** Does the filtering from the original image src into the destination image dest
   *  without caring about the nullness of the latter. */

  protected abstract void doFilter(BufferedImage src, BufferedImage dest);

//------------------------------------------------------------------------------

}
