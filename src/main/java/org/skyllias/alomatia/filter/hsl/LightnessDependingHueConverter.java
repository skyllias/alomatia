
package org.skyllias.alomatia.filter.hsl;

import org.skyllias.alomatia.filter.factor.ComposedUnitFactor;

/** Converter that assigns hue depending on the lightness.
*   Starting at a given hue for lowest lightness and ending at another given
*   hue for highest lightness, intermediate values are assigned by means of
*   a {@link ComposedUnitFactor}. */

public class LightnessDependingHueConverter implements HslConverter
{
  private final float lowestLightnessHue;
  private final float highestLightnessHue;
  private final ComposedUnitFactor composedUnitFactor;

//==============================================================================

  /** As long as they are not equal (as the filter would be equivalent then to
   *  a fixed hue filter), lowestLightnessHue and highestLightnessHue can be
   *  greater or smaller than the other. Values outside the [0, 1) interval are
   *  also valid, although differences higher than 1 will yield odd rainbows.
   *  Check {@link ComposedUnitFactor} for the recommended values for openFactor. */

  public LightnessDependingHueConverter(float lowestLightnessHue,
                                        float highestLightnessHue,
                                        ComposedUnitFactor composedUnitFactor)
  {
    this.lowestLightnessHue  = lowestLightnessHue;
    this.highestLightnessHue = highestLightnessHue;
    this.composedUnitFactor  = composedUnitFactor;
  }

//==============================================================================

  @Override
  public float getNewHue(float hue, float saturation, float lightness)
  {
    float lightnessFactor = composedUnitFactor.apply(lightness);
    return lowestLightnessHue + (highestLightnessHue - lowestLightnessHue) * lightnessFactor;
  }

//------------------------------------------------------------------------------

}
