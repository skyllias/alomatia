
package org.skyllias.alomatia.filter.rgb;

import java.awt.*;

import org.skyllias.alomatia.filter.*;

/** Demo filter that leaves only the blue component of each colour. */

public class BlueChannelOnlyFilter extends BasicColorFilter
{
//==============================================================================

  @Override
  public Color filterColor(Color original)
  {
    int blue = original.getBlue();
    return new Color(0, 0, blue);
  }

//------------------------------------------------------------------------------

}
