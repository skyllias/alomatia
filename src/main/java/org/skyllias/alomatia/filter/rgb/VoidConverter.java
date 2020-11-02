
package org.skyllias.alomatia.filter.rgb;

import java.awt.Color;

import org.skyllias.alomatia.filter.ColourConverter;

/** Demo converter that only removes the alpha channel without altering the others. */

public class VoidConverter implements ColourConverter
{
//==============================================================================

  @Override
  public Color convertColour(Color original)
  {
    return new Color(original.getRed(), original.getGreen(), original.getBlue());
  }

//------------------------------------------------------------------------------

}
