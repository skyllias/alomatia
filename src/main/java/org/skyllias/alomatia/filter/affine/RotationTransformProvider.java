
package org.skyllias.alomatia.filter.affine;

import java.awt.geom.AffineTransform;

/** {@link AffineTransformProvider} that rotates the image 180º.
 *  Other angles would not generally produce images with the same size. */

public class RotationTransformProvider implements AffineTransformProvider
{
//==============================================================================

  @Override
  public AffineTransform getTransform(int width, int height)
  {
    return AffineTransform.getQuadrantRotateInstance(2, ((double) width) / 2,
                                                     ((double) height) / 2);
  }

//------------------------------------------------------------------------------

}
