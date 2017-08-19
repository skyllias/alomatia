
package org.skyllias.alomatia.filter.demo;

import java.awt.*;

import org.skyllias.alomatia.filter.*;

/** Demo filter that removes the red component of each colour. */

public class BluelessFilter extends BasicColorFilter
{
//==============================================================================

  @Override
  public Color filterColor(Color original)
  {
    int alpha = original.getAlpha();
    int red   = original.getRed();
    int green = original.getGreen();
    return new Color(red, green, 0, alpha);
  }

//------------------------------------------------------------------------------

}
