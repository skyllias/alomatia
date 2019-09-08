
package org.skyllias.alomatia.filter.rgb;

import java.awt.Color;

import org.skyllias.alomatia.filter.ColorConverter;

/** Demo converter that only removes the alpha channel without altering the others. */

public class VoidConverter implements ColorConverter
{
//==============================================================================

  @Override
  public Color convertColor(Color original)
  {
    return new Color(original.getRed(), original.getGreen(), original.getBlue());
  }

//------------------------------------------------------------------------------

}
