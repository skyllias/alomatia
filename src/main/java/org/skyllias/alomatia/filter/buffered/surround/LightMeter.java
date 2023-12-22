
package org.skyllias.alomatia.filter.buffered.surround;

import java.awt.Color;
import java.util.Collection;

/** Calculator of the amount of light a collection of colours accumulate,
 *  measured as the average of each channel over its maximum.
 *  Notice that this is not the same as brightness in the HSB colour-space, as
 *  a pure red here is dark while there it has brightness 1. */

public class LightMeter
{
  private static final long MAX_LIGHT_PER_PIXEL = 3 * 0xFF;

//==============================================================================

  /** Returns the accumulated values of all channels of all colours divided by
   *  the maximum amount possible.
   *  If colours contain only whites, then the result will be 1. If they contain
   *  only blacks, the result will be 0. */

  public float getLight(Collection<Color> colours)
  {
    if (colours.isEmpty()) return 0.5f;

    long totalLight       = 0;
    long maxPossibleLight = 0;

    for (Color color : colours)
    {
      totalLight       += color.getRed() + color.getGreen() + color.getBlue();
      maxPossibleLight += MAX_LIGHT_PER_PIXEL;
    }

    return totalLight / (float) maxPossibleLight;
  }

//------------------------------------------------------------------------------

}
