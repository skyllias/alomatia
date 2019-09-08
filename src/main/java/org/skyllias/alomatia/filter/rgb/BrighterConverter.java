
package org.skyllias.alomatia.filter.rgb;

import java.awt.Color;

import org.skyllias.alomatia.filter.ColorConverter;

/** Demo converter that makes the image lighter according to the AWT implementation. */

public class BrighterConverter implements ColorConverter
{
//==============================================================================

  @Override
  public Color convertColor(Color original)
  {
    return original.brighter();
  }

//------------------------------------------------------------------------------

}
