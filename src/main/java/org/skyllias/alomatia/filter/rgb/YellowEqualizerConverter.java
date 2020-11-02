
package org.skyllias.alomatia.filter.rgb;

import java.awt.Color;

import org.skyllias.alomatia.filter.ColourConverter;

/** Demo converter that averages the red and green channels while keeping the
 *  original blue channel. */

public class YellowEqualizerConverter implements ColourConverter
{
//==============================================================================

  @Override
  public Color convertColour(Color original)
  {
    int red   = original.getRed();
    int green = original.getGreen();
    int blue  = original.getBlue();

    int averageYellow = (red + green) / 2;
    return new Color(averageYellow, averageYellow, blue);
  }

//------------------------------------------------------------------------------

}
