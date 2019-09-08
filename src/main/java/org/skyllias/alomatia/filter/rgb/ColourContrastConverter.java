
package org.skyllias.alomatia.filter.rgb;

import java.awt.Color;

import org.skyllias.alomatia.filter.ColorConverter;
import org.skyllias.alomatia.filter.factor.ComposedUnitFactor;
import org.skyllias.alomatia.filter.hsb.ContrastFilter;

/** Converter that separately increases the differences of the three RGB
 *  components by means of a {@link ComposedUnitFactor}.
 *  Its results may be similar to those by {@link ContrastFilter}, although this
 *  should increase the differences between colours with separate contributions
 *  in each channel. The other one does not change the hue. */

public class ColourContrastConverter implements ColorConverter
{
  private ComposedUnitFactor factor;

//==============================================================================

  /** Creates a converter that will modify each component in the RGB colour space
   *  with a ComposedUnitFactor based on contrastFactor, so that negative values
   *  turn most colours greyish, and positive values project colours towards the
   *  {black, cyan, purple, yellow, red, geen, blue, white} set. */

  public ColourContrastConverter(double contrastFactor)
  {
    factor = new ComposedUnitFactor(contrastFactor);
  }

//==============================================================================

  @Override
  public Color convertColor(Color original)
  {
    int red   = original.getRed();
    int green = original.getGreen();
    int blue  = original.getBlue();

    int newRed   = getModifiedValue(red);
    int newGreen = getModifiedValue(green);
    int newBlue  = getModifiedValue(blue);
    return new Color(newRed, newGreen, newBlue);
  }

//------------------------------------------------------------------------------

  /* Returns the integer resulting from the application of the factor over original,
   * considering it defined in the interval [0, 255]. */

  private int getModifiedValue(int original)
  {
    float MAX_CHANNEL = 256f;

    return (int) Math.floor(MAX_CHANNEL * factor.apply(original / MAX_CHANNEL));
  }

//------------------------------------------------------------------------------

}
