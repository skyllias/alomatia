
package org.skyllias.alomatia.filter.rgb;

import java.awt.*;

import org.skyllias.alomatia.filter.*;

/** Demo filter that makes the image darker according to the AWT implementation. */

public class DarkerFilter extends BasicColorFilter
{
//==============================================================================

  @Override
  public Color filterColor(Color original)
  {
    return original.darker();
  }

//------------------------------------------------------------------------------

}
