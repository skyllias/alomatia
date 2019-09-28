
package org.skyllias.alomatia.filter.rgb;

import java.awt.Color;

import org.skyllias.alomatia.filter.ColourConverter;

/** Demo converter that makes the image darker according to the AWT implementation. */

public class DarkerConverter implements ColourConverter
{
//==============================================================================

  @Override
  public Color convertColour(Color original)
  {
    return original.darker();
  }

//------------------------------------------------------------------------------

}
