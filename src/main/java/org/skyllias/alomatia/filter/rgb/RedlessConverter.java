
package org.skyllias.alomatia.filter.rgb;

import java.awt.Color;

import org.skyllias.alomatia.filter.ColourConverter;

/** Demo converter that removes the red component of each colour. */

public class RedlessConverter implements ColourConverter
{
//==============================================================================

  @Override
  public Color convertColour(Color original)
  {
    int green = original.getGreen();
    int blue  = original.getBlue();
    return new Color(0, green, blue);
  }

//------------------------------------------------------------------------------

}
