
package org.skyllias.alomatia.filter.rgb;

import java.awt.Color;

import org.skyllias.alomatia.filter.ColorConverter;

/** Demo converter that leaves only the blue component of each colour. */

public class BlueChannelOnlyConverter implements ColorConverter
{
//==============================================================================

  @Override
  public Color convertColor(Color original)
  {
    int blue = original.getBlue();
    return new Color(0, 0, blue);
  }

//------------------------------------------------------------------------------

}
