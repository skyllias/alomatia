
package org.skyllias.alomatia.filter.buffered.map;

/** Function that returns a hue for a point in an image. */

public interface HueMap
{
  /** Returns the hue (in an HSB colour-space) for a point at (x, y) in an image
   *  of dimensions (width, height).
   *  The result needs not be in the range [0, 1). */

  float getHue(int x, int y, int width, int height);
}
