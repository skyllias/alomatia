
package org.skyllias.alomatia.filter.affine;

import java.awt.geom.AffineTransform;

/** Provider of instances of {@link AffineTransform} that keep the size. */

public interface AffineTransformProvider
{
  /** Returns the AffineTransform to apply to an image with the passed size. */

  AffineTransform getTransform(int width, int height);
}
