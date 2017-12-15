
package org.skyllias.alomatia.filter.hsb;

/** Filter that quantizes the hue of each pixel. */

public class HuePosterizerFilter extends BasicHSBFilter
{
  private UnitQuantizer quantizer;
  private float startHue;

//==============================================================================

  /** Creates a filter that divides the hue spectrum in amountOfBuckets centered
   *  stripes, with one of the buckets beginning at startingHue. */

  public HuePosterizerFilter(int amountOfBuckets, float startingHue)
  {
    quantizer = new UnitQuantizer(amountOfBuckets, true);
    startHue  = startingHue;
  }

//==============================================================================

  @Override
  protected float getNewHue(float hue, float saturation, float brightness)
  {
    float shiftedHue = hue - startHue;
    if (shiftedHue < 0) shiftedHue = shiftedHue + 1;
    return startHue + quantizer.getQuantized(shiftedHue);
  }

//------------------------------------------------------------------------------

}
