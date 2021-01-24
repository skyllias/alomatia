
package org.skyllias.alomatia.filter.affine;

import java.awt.geom.AffineTransform;

/** {@link AffineTransformProvider} that flips an image horizontally. */

public class HorizontalFlipTransformProvider implements AffineTransformProvider
{
//==============================================================================

  @Override
  public AffineTransform getTransform(int width, int height)
  {
    return new AffineTransform(-1, 0, 0, 1, width, 0);
  }

//------------------------------------------------------------------------------

}
