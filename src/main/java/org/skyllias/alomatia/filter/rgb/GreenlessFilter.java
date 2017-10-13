
package org.skyllias.alomatia.filter.rgb;

import java.awt.*;

import org.skyllias.alomatia.filter.*;

/** Demo filter that removes the green component of each colour. */

public class GreenlessFilter extends BasicColorFilter
{
//==============================================================================

  @Override
  public Color filterColor(Color original)
  {
    int red   = original.getRed();
    int blue  = original.getBlue();
    return new Color(red, 0, blue);
  }

//------------------------------------------------------------------------------

}