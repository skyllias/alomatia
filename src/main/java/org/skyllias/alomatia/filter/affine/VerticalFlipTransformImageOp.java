
package org.skyllias.alomatia.filter.affine;

import java.awt.geom.*;
import java.awt.image.*;

/** {@link BufferedImageOp} that flips an image vertically. */

public class VerticalFlipTransformImageOp extends FixedSizeAffineTransformImageOp
{
//==============================================================================

  @Override
  protected AffineTransform getTransform(int width, int height)
  {
    return new AffineTransform(1, 0, 0, -1, 0, height);
  }

//------------------------------------------------------------------------------

}
