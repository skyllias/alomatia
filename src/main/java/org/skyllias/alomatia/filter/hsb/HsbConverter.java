
package org.skyllias.alomatia.filter.hsb;

/** Converter of coordinates in the HSB colour-space. */

public interface HsbConverter
{
  /** Returns the transformed hue for a colour with the passed HSB values. */

  float getNewHue(float hue, float saturation, float brightness);

  /** Returns the transformed saturation for a colour with the passed HSB values. */

  float getNewSaturation(float hue, float saturation, float brightness);

  /** Returns the transformed brightness for a colour with the passed HSB values. */

  float getNewBrightness(float hue, float saturation, float brightness);

//******************************************************************************

  /** Dummy implementation of HsbConverter that returns the original values unaltered. */

  public class HsbAdapter implements HsbConverter
  {
//==============================================================================

    @Override
    public float getNewHue(float hue, float saturation, float brightness) {return hue;}

//------------------------------------------------------------------------------

    @Override
    public float getNewSaturation(float hue, float saturation, float brightness) {return saturation;}

//------------------------------------------------------------------------------

    @Override
    public float getNewBrightness(float hue, float saturation, float brightness) {return brightness;}

//------------------------------------------------------------------------------

  }

}
