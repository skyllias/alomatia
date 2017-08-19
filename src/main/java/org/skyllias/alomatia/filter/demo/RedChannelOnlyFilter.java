
package org.skyllias.alomatia.filter.demo;

import java.awt.*;

import org.skyllias.alomatia.filter.*;

/** Demo filter that leaves only the red component of each colour. */

public class RedChannelOnlyFilter extends BasicColorFilter
{
//==============================================================================

  @Override
  public Color filterColor(Color original)
  {
    int alpha = original.getAlpha();
    int red   = original.getRed();
    return new Color(red, 0, 0, alpha);
  }

//------------------------------------------------------------------------------

}
