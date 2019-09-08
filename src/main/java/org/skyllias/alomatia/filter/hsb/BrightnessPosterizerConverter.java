
package org.skyllias.alomatia.filter.hsb;

import org.skyllias.alomatia.filter.hsb.HsbConverter.HsbAdapter;

/** Converter that quantizes the brightness of each pixel. */

public class BrightnessPosterizerConverter extends HsbAdapter
{
  private final UnitQuantizer quantizer;

//==============================================================================

  public BrightnessPosterizerConverter(int amountOfBuckets, boolean centerThem)
  {
    quantizer = new UnitQuantizer(amountOfBuckets, centerThem);
  }

//==============================================================================

  @Override
  public float getNewBrightness(float hue, float saturation, float brightness)
  {
    return quantizer.getQuantized(brightness);
  }

//------------------------------------------------------------------------------

}
