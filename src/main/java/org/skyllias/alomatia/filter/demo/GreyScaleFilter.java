
package org.skyllias.alomatia.filter.demo;

import java.awt.*;

import org.skyllias.alomatia.filter.*;

/** Demo filter that converts all colours to grey tones. */

public class GreyScaleFilter extends BasicColorFilter
{
//==============================================================================

  @Override
  public Color filterColor(Color original)
  {
    int red   = original.getRed();
    int green = original.getGreen();
    int blue  = original.getBlue();

    int totalLight = 1 + red + green + blue;                                    // the extra 1 makes the division round instead of truncate
    int average    = totalLight / 3;
    return new Color(average, average, average);
  }

//------------------------------------------------------------------------------

}
