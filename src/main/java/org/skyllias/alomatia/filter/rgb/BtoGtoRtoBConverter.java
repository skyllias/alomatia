
package org.skyllias.alomatia.filter.rgb;

import java.awt.Color;

import org.skyllias.alomatia.filter.ColorConverter;

/** Demo converter that rotates the colour components: Blue is set as green,
 *  green is set as red and red is set as blue. */

public class BtoGtoRtoBConverter implements ColorConverter
{
//==============================================================================

  @Override
  public Color convertColor(Color original)
  {
    int red   = original.getRed();
    int green = original.getGreen();
    int blue  = original.getBlue();
    return new Color(green, blue, red);
  }

//------------------------------------------------------------------------------

}
