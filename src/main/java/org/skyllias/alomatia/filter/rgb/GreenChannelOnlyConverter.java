
package org.skyllias.alomatia.filter.rgb;

import java.awt.Color;

import org.skyllias.alomatia.filter.ColorConverter;

/** Demo converter that leaves only the green component of each colour. */

public class GreenChannelOnlyConverter implements ColorConverter
{
//==============================================================================

  @Override
  public Color convertColor(Color original)
  {
    int green = original.getGreen();
    return new Color(0, green, 0);
  }

//------------------------------------------------------------------------------

}
