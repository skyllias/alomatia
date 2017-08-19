
package org.skyllias.alomatia.filter.demo;

import java.awt.*;

import org.skyllias.alomatia.filter.*;

/** Demo filter that rotates the colour components: Red is set as green,
 *  green is set as blue and blue is set as red. */

public class RtoGtoBtoRFilter extends BasicColorFilter
{
//==============================================================================

  @Override
  public Color filterColor(Color original)
  {
    int alpha = original.getAlpha();
    int red   = original.getRed();
    int green = original.getGreen();
    int blue  = original.getBlue();
    return new Color(blue, red, green, alpha);
  }

//------------------------------------------------------------------------------

}
