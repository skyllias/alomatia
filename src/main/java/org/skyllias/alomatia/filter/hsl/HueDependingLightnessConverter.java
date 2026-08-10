
package org.skyllias.alomatia.filter.hsl;

import org.skyllias.alomatia.filter.factor.SimpleFactor;
import org.skyllias.alomatia.filter.factor.UnitFactor;
import org.skyllias.alomatia.filter.hsb.function.HueFunction;

/** Converter that increases or decreases the lightness of the colours in an
 *  image depending on their hue. */

public class HueDependingLightnessConverter implements HslConverter
{
  private final HueFunction hueFunction;

//==============================================================================

  public HueDependingLightnessConverter(HueFunction function) {hueFunction = function;}

//==============================================================================

  /** Applies a {@link SimpleFactor} to the result of the function with the
   *  original lightness, damped by the chroma so that colours with an undefined
   *  or unreliable hue (the greys, and the shades close to black and to white)
   *  are barely modified. */

  @Override
  public float getNewLightness(float hue, float saturation, float lightness)
  {
    double functionFactor = hueFunction.getValue(hue);
    UnitFactor unitFactor = new SimpleFactor(functionFactor * calculateChroma(saturation, lightness));
    return unitFactor.apply(lightness);
  }

//------------------------------------------------------------------------------

  private float calculateChroma(float saturation, float lightness)
  {
    return saturation * (1 - Math.abs(2 * lightness - 1));
  }

//------------------------------------------------------------------------------

}
