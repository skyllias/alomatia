
package org.skyllias.alomatia.filter.rgb;

import java.awt.*;

import org.skyllias.alomatia.filter.*;
import org.skyllias.alomatia.filter.factor.*;

/** Filter that separately increases the differences of the three RGB components
 *  by means of a {@link ComposedUnitFactor}.
 *  Its results may be similar to those by {@link ContrastFilter}, although this
 *  should increase the differences between colours with separate contributions
 *  in each channel. The other one does not change the hue. */

public class ColourContrastFilter extends BasicColorFilter
{
  private ComposedUnitFactor factor;

//==============================================================================

  /** Creates a filter that will modify each component in the RGB colour space
   *  with a ComposedUnitFactor based on contrastFactor, so that negative values
   *  turn most colours greyish, and positive values project colours towards the
   *  {black, cyan, purple, yellow, red, geen, blue, white} set. */

  public ColourContrastFilter(double contrastFactor)
  {
    factor = new ComposedUnitFactor(contrastFactor);
  }

//==============================================================================

  @Override
  public Color filterColor(Color original)
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
    float MAX_CHANNEL = 255f;

    return Math.round(MAX_CHANNEL * factor.apply(original / MAX_CHANNEL));
  }

//------------------------------------------------------------------------------

}
