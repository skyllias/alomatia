
package org.skyllias.alomatia.filter.hsb;

import org.skyllias.alomatia.filter.factor.*;

/** Function that tells how a magnitude like brightness or saturation should
 *  vary depending on hue. Its result is not intended to be applied directly
 *  but through a {@link UnitFactor}. */

public interface HueFunction
{
  /** Assuming an input in the domain [0, 1), returns a number in the range
   *  (-infinite, +infinite). A result of 0 yields no change in the magnitude,
   *  while greater values increase it and lower decrease it. */

  double getValue(float hue);
}
