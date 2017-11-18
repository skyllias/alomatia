
package org.skyllias.alomatia.filter.hsb;

import org.skyllias.alomatia.filter.factor.*;

/** Filter that increases or decreases the brightness of the colours in an image
 *  depending on their hue. */

public class HueDependingBrightnessFilter extends BasicHSBFilter
{
  private HueFunction hueFunction;

//==============================================================================

  public HueDependingBrightnessFilter(HueFunction function) {hueFunction = function;}

//==============================================================================

  /** Applies a {@link SimpleFactor} to the result of the function with the
   *  original brightness, but only if saturation is not 0 (because in this case
   *  the hue is undefined). */

  @Override
  protected float getNewBrightness(float hue, float saturation, float brightness)
  {
    if (saturation == 0) return brightness;

    double functionFactor = hueFunction.getValue(hue);
    UnitFactor unitFactor = new SimpleFactor(functionFactor);
    return unitFactor.apply(brightness);
  }

//------------------------------------------------------------------------------

}
