
package org.skyllias.alomatia.filter.affine;

import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImageOp;

/** {@link BufferedImageOp} that rotates the image 180º.
 *  Other angles would not generally produce images with the same size. */

public class RotationTransformOp extends FixedSizeAffineTransformOp
{
//==============================================================================

  @Override
  protected AffineTransform getTransform(int width, int height)
  {
    return AffineTransform.getQuadrantRotateInstance(2, ((double) width) / 2, ((double) height) / 2);
  }

//------------------------------------------------------------------------------

}
