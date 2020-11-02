
package org.skyllias.alomatia.filter.rgb;

import java.awt.Color;

import org.skyllias.alomatia.filter.ColourConverter;

/** Demo converter that leaves only the blue component of each colour. */

public class BlueChannelOnlyConverter implements ColourConverter
{
//==============================================================================

  @Override
  public Color convertColour(Color original)
  {
    int blue = original.getBlue();
    return new Color(0, 0, blue);
  }

//------------------------------------------------------------------------------

}
