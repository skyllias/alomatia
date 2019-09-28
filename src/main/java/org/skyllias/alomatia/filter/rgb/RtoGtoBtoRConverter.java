
package org.skyllias.alomatia.filter.rgb;

import java.awt.Color;

import org.skyllias.alomatia.filter.ColourConverter;

/** Demo converter that rotates the colour components: Red is set as green,
 *  green is set as blue and blue is set as red. */

public class RtoGtoBtoRConverter implements ColourConverter
{
//==============================================================================

  @Override
  public Color convertColour(Color original)
  {
    int red   = original.getRed();
    int green = original.getGreen();
    int blue  = original.getBlue();
    return new Color(blue, red, green);
  }

//------------------------------------------------------------------------------

}
