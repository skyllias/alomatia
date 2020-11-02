
package org.skyllias.alomatia.filter.rgb;

import java.awt.Color;

import org.skyllias.alomatia.filter.ColourConverter;

/** Demo converter that removes the green component of each colour. */

public class GreenlessConverter implements ColourConverter
{
//==============================================================================

  @Override
  public Color convertColour(Color original)
  {
    int red   = original.getRed();
    int blue  = original.getBlue();
    return new Color(red, 0, blue);
  }

//------------------------------------------------------------------------------

}
