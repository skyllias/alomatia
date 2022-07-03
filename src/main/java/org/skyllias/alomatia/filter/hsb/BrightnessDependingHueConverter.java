
package org.skyllias.alomatia.filter.hsb;

import org.skyllias.alomatia.filter.factor.ComposedUnitFactor;

/** Converter that assigns hue depending on the brightness.
*   Starting at a given hue for lowest brightness and ending at another given
*   hue for highest brightness, intermediate values are assigned by means of
*   a {@link ComposedUnitFactor}. */

public class BrightnessDependingHueConverter implements HsbConverter
{
  private final float lowestBrightnessHue;
  private final float highestBrightnessHue;
  private final ComposedUnitFactor composedUnitFactor;

//==============================================================================

  /** As long as they are not equal (as the filter would be equiavlent then to
   *  a fixed hue filter), lowestBrightnessHue and highestBrightnessHue can be
   *  greater or smaller than the other. Values outside the [0, 1) interval are
   *  also valid, although differences higher than 1 will yield odd rainbows.
   *  Check {@link ComposedUnitFactor} for the recommended values for openFactor. */

  public BrightnessDependingHueConverter(float lowestBrightnessHue,
                                         float highestBrightnessHue,
                                         ComposedUnitFactor composedUnitFactor)
  {
    this.lowestBrightnessHue  = lowestBrightnessHue;
    this.highestBrightnessHue = highestBrightnessHue;
    this.composedUnitFactor   = composedUnitFactor;
  }

//==============================================================================

  @Override
  public float getNewHue(float hue, float saturation, float brightness)
  {
    float brightnessFactor = composedUnitFactor.apply(brightness);
    return lowestBrightnessHue + (highestBrightnessHue - lowestBrightnessHue) * brightnessFactor;
  }

//------------------------------------------------------------------------------

}
