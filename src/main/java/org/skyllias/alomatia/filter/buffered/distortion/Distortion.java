
package org.skyllias.alomatia.filter.buffered.distortion;

import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;

/** Geometric mapping of bidimensional points that dislocate the pixels from
 *  an image to produce a distorted image.
 *  Against the probably most intuitive approach, this function translates
 *  points from the destination image into points in the source image.
 *  To allow for smoother results, the function is defined not over discrete
 *  pixels but over a floating-point precision continuum. */

public interface Distortion
{
  /** Returns the point from the original image to be transfered to destination.
   *  Although the result ideally should be inside bounds, implementations are
   *  not expected to constrain the returned pixels. Instead, bounds is just
   *  passed to allow for calculations to scale in those cases where the
   *  distortion must be proportional to the images size. */

  Point2D.Float getSourcePoint(Point2D.Float destination, Dimension2D bounds);
}
