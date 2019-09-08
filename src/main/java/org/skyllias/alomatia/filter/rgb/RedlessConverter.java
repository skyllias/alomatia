
package org.skyllias.alomatia.filter.rgb;

import java.awt.Color;

import org.skyllias.alomatia.filter.ColorConverter;

/** Demo converter that removes the red component of each colour. */

public class RedlessConverter implements ColorConverter
{
//==============================================================================

  @Override
  public Color convertColor(Color original)
  {
    int green = original.getGreen();
    int blue  = original.getBlue();
    return new Color(0, green, blue);
  }

//------------------------------------------------------------------------------

}
