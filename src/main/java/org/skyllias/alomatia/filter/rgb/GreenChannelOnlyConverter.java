
package org.skyllias.alomatia.filter.rgb;

import java.awt.Color;

import org.skyllias.alomatia.filter.ColourConverter;

/** Demo converter that leaves only the green component of each colour. */

public class GreenChannelOnlyConverter implements ColourConverter
{
//==============================================================================

  @Override
  public Color convertColour(Color original)
  {
    int green = original.getGreen();
    return new Color(0, green, 0);
  }

//------------------------------------------------------------------------------

}
