
package org.skyllias.alomatia.filter.rgb;

import java.awt.Color;

import org.skyllias.alomatia.filter.ColorConverter;

/** Demo converter that leaves only the red component of each colour. */

public class RedChannelOnlyConverter implements ColorConverter
{
//==============================================================================

  @Override
  public Color convertColor(Color original)
  {
    int red = original.getRed();
    return new Color(red, 0, 0);
  }

//------------------------------------------------------------------------------

}
