
package org.skyllias.alomatia.filter.rgb;

import java.awt.Color;

import org.skyllias.alomatia.filter.ColourConverter;

/** Demo converter that averages the blue and red channels while keeping the
 *  original green channel. */

public class MagentaEqualizerConverter implements ColourConverter
{
//==============================================================================

  @Override
  public Color convertColour(Color original)
  {
    int red   = original.getRed();
    int green = original.getGreen();
    int blue  = original.getBlue();

    int averageMagenta = (blue + red) / 2;
    return new Color(averageMagenta, green, averageMagenta);
  }

//------------------------------------------------------------------------------

}
