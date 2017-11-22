
package org.skyllias.alomatia.filter.affine;

import java.awt.geom.*;
import java.awt.image.*;

import org.skyllias.alomatia.filter.buffered.*;

/** {@link BufferedImageOp} that applies an {@link AffineTransform} that keeps
 *  the original size of the source image.
 *  <p>
 *  It does not extend {@link AffineTransformOp} to avoid the evil of extension.
 *  The transformation is delegated to a real AffineTransformOp in order to
 *  take advantage of any potential hardware or native optimizations. */

public abstract class FixedSizeAffineTransformImageOp extends BasicBufferedImageOp
{
//==============================================================================

  @Override
  public BufferedImage filter(BufferedImage src, BufferedImage dst)
  {
    int width  = src.getWidth();
    int height = src.getHeight();

    AffineTransform transform    = getTransform(width, height);
    AffineTransformOp delegateOp = new AffineTransformOp(transform, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);   // at least initially, there is no perspective of transformations that require interpolation
    return delegateOp.filter(src, dst);
  }

//------------------------------------------------------------------------------

  /** Returns the AffineTransform to apply to an image with the passed size. */

  protected abstract AffineTransform getTransform(int width, int height);

//------------------------------------------------------------------------------

}
