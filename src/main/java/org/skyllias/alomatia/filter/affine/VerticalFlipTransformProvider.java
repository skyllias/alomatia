
package org.skyllias.alomatia.filter.affine;

import java.awt.geom.AffineTransform;

/** {@link AffineTransformProvider} that flips an image vertically. */

public class VerticalFlipTransformProvider implements AffineTransformProvider
{
//==============================================================================

  @Override
  public AffineTransform getTransform(int width, int height)
  {
    return new AffineTransform(1, 0, 0, -1, 0, height);
  }

//------------------------------------------------------------------------------

}
