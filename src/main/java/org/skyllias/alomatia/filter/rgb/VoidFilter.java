
package org.skyllias.alomatia.filter.rgb;

import java.awt.*;

import org.skyllias.alomatia.filter.*;

/** Demo filter that does not alter colours.
 *  However, as it extends BasicColorFilter, the alpha channel is lost. */

public class VoidFilter extends BasicColorFilter
{
//==============================================================================

  @Override
  public Color filterColor(Color original) {return original;}

//------------------------------------------------------------------------------

}
