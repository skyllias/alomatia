
package org.skyllias.alomatia.filter.rgb;

import java.awt.Color;

import org.skyllias.alomatia.filter.ColorConverter;

/** Demo converter that changes every colour by its invert. */

public class NegativeConverter implements ColorConverter
{
//==============================================================================

  @Override
  public Color convertColor(Color original)
  {
    final int MAX = 0xFF;

    int red   = original.getRed();
    int green = original.getGreen();
    int blue  = original.getBlue();
    return new Color(MAX - red, MAX - green, MAX - blue);
  }

//------------------------------------------------------------------------------

}
