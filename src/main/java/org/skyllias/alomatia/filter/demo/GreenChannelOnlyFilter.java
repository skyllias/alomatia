
package org.skyllias.alomatia.filter.demo;

import java.awt.*;

import org.skyllias.alomatia.filter.*;

/** Demo filter that leaves only the green component of each colour. */

public class GreenChannelOnlyFilter extends BasicColorFilter
{
//==============================================================================

  @Override
  public Color filterColor(Color original)
  {
    int alpha = original.getAlpha();
    int green = original.getGreen();
    return new Color(0, green, 0, alpha);
  }

//------------------------------------------------------------------------------

}
