
package org.skyllias.alomatia.filter.hsb;

import org.skyllias.alomatia.filter.hsb.HsbConverter.HsbAdapter;

/** Converter that quantizes the hue of each pixel. */

public class HuePosterizerConverter extends HsbAdapter
{
  private final UnitQuantizer quantizer;
  private final float startHue;

//==============================================================================

  /** Creates a converter that divides the hue spectrum in amountOfBuckets
   *  centered stripes, with one of the buckets beginning at startingHue. */

  public HuePosterizerConverter(int amountOfBuckets, float startingHue)
  {
    quantizer = new UnitQuantizer(amountOfBuckets, true);
    startHue  = startingHue;
  }

//==============================================================================

  @Override
  public float getNewHue(float hue, float saturation, float brightness)
  {
    float shiftedHue = hue - startHue;
    if (shiftedHue < 0) shiftedHue = shiftedHue + 1;
    return startHue + quantizer.getQuantized(shiftedHue);
  }

//------------------------------------------------------------------------------

}
