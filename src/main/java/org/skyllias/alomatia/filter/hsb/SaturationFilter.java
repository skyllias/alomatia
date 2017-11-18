
package org.skyllias.alomatia.filter.hsb;

import org.skyllias.alomatia.filter.factor.*;

/** Filter that increases or decreases the saturation of the colours in an image
 *  by a non-linear factor.
 *  <p>
 *  This could be a particular case of {@link HueDependingSaturationFactorFilter}. */

public class SaturationFilter extends BasicHSBFilter
{
  private UnitFactor unitFactor;                                                // favouring composition over inheritance

//==============================================================================

  /** Creates a filter that modifies the saturation of images according to the
   *  value of saturationFactor:
   *  - If it is 0, then the saturation does not change.
   *  - If is is lower than 0, it is reduced.
   *  - If is is higher than 0, it is increased.
   *  - With large negative numbers (3 and above), the saturation is nearly removed.
   *  - With large positive numbers (3 and above), the saturation is nearly saturated.
   *  - The first noticeable differences occur with absolute values of the order of 0.1. */

  public SaturationFilter(double saturationFactor)
  {
    unitFactor = new AntiBoostingFactor(saturationFactor);
  }

//==============================================================================

  /** Applies an {@link AntiBoostingFactor} to the original saturation avoiding
   *  results outside [0, 1]. */

  @Override
  protected float getNewSaturation(float hue, float saturation, float brightness)
  {
    return unitFactor.apply(saturation);
  }

//------------------------------------------------------------------------------

}
