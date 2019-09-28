
package org.skyllias.alomatia.filter.rgb;

import java.awt.Color;

import org.skyllias.alomatia.filter.ColourConverter;

/** Demo converter that leaves only the red component of each colour. */

public class RedChannelOnlyConverter implements ColourConverter
{
//==============================================================================

  @Override
  public Color convertColour(Color original)
  {
    int red = original.getRed();
    return new Color(red, 0, 0);
  }

//------------------------------------------------------------------------------

}
