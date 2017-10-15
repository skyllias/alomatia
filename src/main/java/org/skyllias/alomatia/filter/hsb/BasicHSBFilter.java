
package org.skyllias.alomatia.filter.hsb;

import java.awt.*;

import org.skyllias.alomatia.filter.*;

/** Superclass for the filters that work in the hue-saturation-brightness
 *  colour-space rather than the most common RGB space. */

public abstract class BasicHSBFilter extends BasicColorFilter
{
//==============================================================================

  @Override
  protected Color filterColor(Color original)
  {
    int red   = original.getRed();
    int green = original.getGreen();
    int blue  = original.getBlue();

    float[] originalHsb      = Color.RGBtoHSB(red, green, blue, null);
    float originalHue        = originalHsb[0];
    float originalSaturation = originalHsb[1];
    float originalBrightness = originalHsb[2];

    float newHue        = getNewHue(originalHue, originalSaturation, originalBrightness);
    float newSaturation = getNewSaturation(originalHue, originalSaturation, originalBrightness);
    float newBrightness = getNewBrightness(originalHue, originalSaturation, originalBrightness);
    return Color.getHSBColor(newHue, newSaturation, newBrightness);
  }

//------------------------------------------------------------------------------

  /** Returns the transformed hue for a colour with the passed HSB values.
   *  By default te original hue is returned, but subclasses can override it. */

  protected float getNewHue(float hue, float saturation, float brightness) {return hue;}

//------------------------------------------------------------------------------

  /** Returns the transformed saturation for a colour with the passed HSB values.
   *  By default te original hue is returned, but subclasses can override it. */

  protected float getNewSaturation(float hue, float saturation, float brightness) {return saturation;}

//------------------------------------------------------------------------------

  /** Returns the transformed brightness for a colour with the passed HSB values.
   *  By default te original brightness is returned, but subclasses can override it. */

  protected float getNewBrightness(float hue, float saturation, float brightness) {return brightness;}

//------------------------------------------------------------------------------

}
