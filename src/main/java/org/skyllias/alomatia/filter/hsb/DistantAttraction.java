
package org.skyllias.alomatia.filter.hsb;

/** {@link Attraction} that hardly modifies hue closest to the pole, but affects
 *  others farther away. */

public class DistantAttraction implements Attraction
{
  private float strength;
  private float range;

//==============================================================================

  /** @param strength Multiplicative factor for the result. A value of 1
   *  complies with d(attract(x)) / dx <= 1
   *  @param range The farthest distance where the attraction has any effect. */

  public DistantAttraction(float strength, float range)
  {
    this.strength = strength;
    this.range    = range;
  }

//==============================================================================

  /** The polynomial function (x³ - x⁹) is used, because it complies with being
   *  fairly flat for x < 0.3, 0 for |x| = 1, and its maximum derivative in the
   *  interval is 1. */

  @Override
  public float attract(float difference)
  {
    if (Math.abs(difference) >= range) return 0;

    float x        = difference / range;
    float polynomy = (float) (Math.pow(x, 3) - Math.pow(x, 9));
    return strength * range * polynomy;
  }

//------------------------------------------------------------------------------

}
