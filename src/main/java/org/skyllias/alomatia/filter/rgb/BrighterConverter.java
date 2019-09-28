
package org.skyllias.alomatia.filter.rgb;

import java.awt.Color;

import org.skyllias.alomatia.filter.ColourConverter;

/** Demo converter that makes the image lighter according to the AWT implementation. */

public class BrighterConverter implements ColourConverter
{
//==============================================================================

  @Override
  public Color convertColour(Color original)
  {
    return original.brighter();
  }

//------------------------------------------------------------------------------

}
