
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
   *  original brightness, but only if saturation and brightness are not too low
   *  (because in these cases the hue is undefined or similar shades of black
   *  can lead to very different tones). */

  @Override
  protected float getNewBrightness(float hue, float saturation, float brightness)
  {
    double functionFactor = hueFunction.getValue(hue);
    UnitFactor unitFactor = new SimpleFactor(functionFactor * saturation * brightness);
    return unitFactor.apply(brightness);
  }

//------------------------------------------------------------------------------

}
