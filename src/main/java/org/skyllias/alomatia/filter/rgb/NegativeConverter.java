
package org.skyllias.alomatia.filter.rgb;

import java.awt.Color;

import org.skyllias.alomatia.filter.ColourConverter;

/** Demo converter that changes every colour by its invert. */

public class NegativeConverter implements ColourConverter
{
//==============================================================================

  @Override
  public Color convertColour(Color original)
  {
    final int MAX = 0xFF;

    int red   = original.getRed();
    int green = original.getGreen();
    int blue  = original.getBlue();
    return new Color(MAX - red, MAX - green, MAX - blue);
  }

//------------------------------------------------------------------------------

}
