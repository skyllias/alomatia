
package org.skyllias.alomatia.filter.rgb;

import java.awt.*;

import org.skyllias.alomatia.filter.*;

/** Demo filter that removes the red component of each colour. */

public class RedlessFilter extends BasicColorFilter
{
//==============================================================================

  @Override
  public Color filterColor(Color original)
  {
    int green = original.getGreen();
    int blue  = original.getBlue();
    return new Color(0, green, blue);
  }

//------------------------------------------------------------------------------

}
