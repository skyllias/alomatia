
package org.skyllias.alomatia.filter.rgb;

import java.awt.Color;

import org.skyllias.alomatia.filter.ColorConverter;

/** Demo converter that swaps the blue and red channels, leaving green untouched. */

public class SwapBlueAndRedConverter implements ColorConverter
{
//==============================================================================

  @Override
  public Color convertColor(Color original)
  {
    int red   = original.getRed();
    int green = original.getGreen();
    int blue  = original.getBlue();
    return new Color(blue, green, red);
  }

//------------------------------------------------------------------------------

}
