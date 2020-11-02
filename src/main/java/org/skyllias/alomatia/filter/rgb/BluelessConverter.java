
package org.skyllias.alomatia.filter.rgb;

import java.awt.Color;

import org.skyllias.alomatia.filter.ColourConverter;

/** Demo converter that removes the blue component of each colour. */

public class BluelessConverter implements ColourConverter
{
//==============================================================================

  @Override
  public Color convertColour(Color original)
  {
    int red   = original.getRed();
    int green = original.getGreen();
    return new Color(red, green, 0);
  }

//------------------------------------------------------------------------------

}
