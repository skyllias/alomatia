
package org.skyllias.alomatia.filter.rgb;

import java.awt.Color;

import org.skyllias.alomatia.filter.ColourConverter;

/** Demo converter that averages the green and blue channels while keeping the
 *  original red channel. */

public class CyanEqualizerConverter implements ColourConverter
{
//==============================================================================

  @Override
  public Color convertColour(Color original)
  {
    int red   = original.getRed();
    int green = original.getGreen();
    int blue  = original.getBlue();

    int averageCyan = (green + blue) / 2;
    return new Color(red, averageCyan, averageCyan);
  }

//------------------------------------------------------------------------------

}
