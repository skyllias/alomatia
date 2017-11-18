
package org.skyllias.alomatia.filter.hsb;

import org.skyllias.alomatia.filter.factor.*;

/** Filter that increases or decreases the saturation of the colours in an image
 *  depending on their hue. */

public class HueDependingSaturationFactorFilter extends BasicHSBFilter
{
  private HueFunction hueFunction;

//==============================================================================

  public HueDependingSaturationFactorFilter(HueFunction function) {hueFunction = function;}

//==============================================================================

  /** Applies an {@link AntiBoostingFactor} to the result of the function with the
   *  original saturation, but only if saturation is not 0 (because in this case
   *  the hue is undefined). */

  @Override
  protected float getNewSaturation(float hue, float saturation, float brightness)
  {
    if (saturation == 0) return saturation;

    double functionFactor = hueFunction.getValue(hue);
    UnitFactor unitFactor = new AntiBoostingFactor(functionFactor);
    return unitFactor.apply(saturation);
  }

//------------------------------------------------------------------------------

}
