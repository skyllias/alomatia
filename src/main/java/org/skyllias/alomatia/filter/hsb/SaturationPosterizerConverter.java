
package org.skyllias.alomatia.filter.hsb;

import org.skyllias.alomatia.filter.hsb.HsbConverter.HsbAdapter;

/** Converter that quantizes the saturation of each pixel. */

public class SaturationPosterizerConverter extends HsbAdapter
{
  private final UnitQuantizer quantizer;

//==============================================================================

  public SaturationPosterizerConverter(int amountOfBuckets, boolean centerThem)
  {
    quantizer = new UnitQuantizer(amountOfBuckets, centerThem);
  }

//==============================================================================

  @Override
  public float getNewSaturation(float hue, float saturation, float brightness)
  {
    return quantizer.getQuantized(saturation);
  }

//------------------------------------------------------------------------------

}
