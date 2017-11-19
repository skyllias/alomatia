
package org.skyllias.alomatia.filter.affine;

import java.awt.geom.*;
import java.awt.image.*;

/** {@link BufferedImageOp} that flips an image horizontally. */

public class HorizontalFlipTransformImageOp extends FixedSizeAffineTransformImageOp
{
//==============================================================================

  @Override
  protected AffineTransform getTransform(int width, int height)
  {
    return new AffineTransform(-1, 0, 0, 1, width, 0);
  }

//------------------------------------------------------------------------------

}