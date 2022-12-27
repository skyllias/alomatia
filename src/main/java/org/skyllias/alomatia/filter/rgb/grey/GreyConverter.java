
package org.skyllias.alomatia.filter.rgb.grey;

import java.awt.Color;

import org.skyllias.alomatia.filter.ColourConverter;

/** Converter of any colour into a shade of grey.
 *  The way channels are to be combined is decided by a {@link GreyShadeProvider}. */

public class GreyConverter implements ColourConverter
{
  private final GreyShadeProvider greyLevelProvider;

//==============================================================================

  public GreyConverter(GreyShadeProvider greyLevelProvider)
  {
    this.greyLevelProvider = greyLevelProvider;
  }

//==============================================================================

  @Override
  public Color convertColour(Color original)
  {
    int red   = original.getRed();
    int green = original.getGreen();
    int blue  = original.getBlue();

    int level = greyLevelProvider.getShade(red, green, blue);

    return new Color(level, level, level);
  }

//------------------------------------------------------------------------------

}
