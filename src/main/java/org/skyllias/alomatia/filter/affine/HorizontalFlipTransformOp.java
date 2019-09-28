
package org.skyllias.alomatia.filter.affine;

import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImageOp;

/** {@link BufferedImageOp} that flips an image horizontally. */

public class HorizontalFlipTransformOp extends FixedSizeAffineTransformOp
{
//==============================================================================

  @Override
  protected AffineTransform getTransform(int width, int height)
  {
    return new AffineTransform(-1, 0, 0, 1, width, 0);
  }

//------------------------------------------------------------------------------

}
