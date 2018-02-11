
package org.skyllias.alomatia.filter.buffered.distortion;

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;

/**Provider of colours from an image from positions that may not fall exactly on
 * a pixel.
 * This overlaps somewhat with the existing algorithms used fro example in
 * {@link AffineTransformOp}. */

public interface Interpolator
{
  /** Returns the {@link Color} that sourceImage has at point.
   *  If point is exactly on a pixel, the colour of the pixel should be returned.
   *  Otherwise, a combination of the surrounding pixels is returned.
   *  Pixels are considered to lie on half-units (n + 0.5, with n between 0 and
   *  width/height - 1).
   *  Implementations must be able to gently handle points outside the image bounds. */

  Color getColourAt(BufferedImage sourceImage, Point2D.Float point);
}
