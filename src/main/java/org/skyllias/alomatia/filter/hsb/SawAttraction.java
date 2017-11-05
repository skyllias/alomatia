
package org.skyllias.alomatia.filter.hsb;

/** {@link Attraction} that grows proportionally up to a point from which it
 *  begins to decrease.
 *  Therefore, it is similar to {@link LinearAttraction} but prevents acting
 *  over too distant hues. */

public class SawAttraction implements Attraction
{
  private float slope;
  private float range;

//==============================================================================

  /** @param strength The amount of attraction per unit of distance, which should
   *  be smaller than 1 to have a reasonable behaviour.
   *  @param limit The distance where the attraction is maximum, decreasing until
   *  2 * limit, where it becomes 0 forever. So, it should be smaller than 0'25. */

  public SawAttraction(float strength, float limit)
  {
    slope = strength;
    range = limit;
  }

//==============================================================================

  @Override
  public float attract(float difference)
  {
    if (Math.abs(difference) > 2 * range) return 0;

    if (Math.abs(difference) < range) return slope * difference;
    else if (difference > 0)          return slope * (2 * range - difference);
    else                              return -slope * (2 * range + difference);
  }

//------------------------------------------------------------------------------

}
