
package org.skyllias.alomatia.filter.demo;

import java.awt.*;

import org.skyllias.alomatia.filter.*;

/** Demo filter that rotates the colour components: Blue is set as green,
 *  green is set as red and red is set as blue. */

public class BtoGtoRtoBFilter extends BasicColorFilter
{
//==============================================================================

  @Override
  public Color filterColor(Color original)
  {
    int alpha = original.getAlpha();
    int red   = original.getRed();
    int green = original.getGreen();
    int blue  = original.getBlue();
    return new Color(green, blue, red, alpha);
  }

//------------------------------------------------------------------------------

}
