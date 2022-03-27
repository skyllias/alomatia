
package org.skyllias.alomatia.filter.hsb;

/** Converter that flips the hue around a certain central value.
 *
 *  If the central value is aligned with red, green or blue (or their
 *  complementaries) then results from a filter that uses this converter are the
 *  same as the results from a filter that uses the RGB SwapXAndYConverter.
 *  Performance of this converter is worse, though, so for those three cases it
 *  is advisable to use the RGB version. */

public class HueReverserConverter implements HsbConverter
{
  private final float centralHue;

//==============================================================================

  /** Creates a converter that turns a hue centralHue + x into centralHue - x,
   *  for any real value of x.
   *  Due to the cyclic nature of hue in the HSB space, passing
   *  centralHue + 0.5 * n has the same effect as passing centralHue, for any
   *  integer value of n. */

  public HueReverserConverter(float centralHue)
  {
    this.centralHue = centralHue;
  }

//==============================================================================

  @Override
  public float getNewHue(float hue, float saturation, float brightness)
  {
    float difference = centralHue - hue;
    return centralHue + difference;
  }

//------------------------------------------------------------------------------

}
