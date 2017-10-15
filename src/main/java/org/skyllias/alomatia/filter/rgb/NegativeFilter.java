
package org.skyllias.alomatia.filter.rgb;

import java.awt.*;

import org.skyllias.alomatia.filter.*;

/** Demo filter that changes every colour by its invert. */

public class NegativeFilter extends BasicColorFilter
{
//==============================================================================

  @Override
  public Color filterColor(Color original)
  {
    final int MAX = 0xFF;

    int red   = original.getRed();
    int green = original.getGreen();
    int blue  = original.getBlue();
    return new Color(MAX - red, MAX - green, MAX - blue);
  }

//------------------------------------------------------------------------------

}
