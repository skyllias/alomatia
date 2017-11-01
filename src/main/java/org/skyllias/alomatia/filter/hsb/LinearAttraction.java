
package org.skyllias.alomatia.filter.hsb;

/** {@link Attraction} proportional to difference (never decreasing). */

public class LinearAttraction implements Attraction
{
  private float slope;

//==============================================================================

  /** @param strength The amount of attraction per unit of distance, which should
   *  be smaller than 1 to have a reasonable behaviour. */

  public LinearAttraction(float strength) {this.slope = strength;}

//==============================================================================

  @Override
  public float attract(float difference)
  {
    return slope * difference;
  }

//------------------------------------------------------------------------------

}
