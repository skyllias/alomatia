
package org.skyllias.alomatia.filter.rgb;

import java.awt.Color;

import org.skyllias.alomatia.filter.ColorConverter;

/** Demo converter that makes the image darker according to the AWT implementation. */

public class DarkerConverter implements ColorConverter
{
//==============================================================================

  @Override
  public Color convertColor(Color original)
  {
    return original.darker();
  }

//------------------------------------------------------------------------------

}
