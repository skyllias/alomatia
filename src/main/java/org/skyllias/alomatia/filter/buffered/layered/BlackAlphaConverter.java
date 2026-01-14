
package org.skyllias.alomatia.filter.buffered.layered;

import java.awt.Color;

import org.skyllias.alomatia.filter.ColourConverter;

/** {@link ColourConverter} that, assuming that original colours are shades of
 *  gray, makes everything but the darkest transparent.  */

public class BlackAlphaConverter implements ColourConverter
{
  private static final int MAX_CHANNEL_VALUE = 255;
  private static final int MAX_LIGHT_TO_KEEP = 50;
  private static final int TRANSPARENCY_SLOPE = 4;

//==============================================================================

  @Override
  public Color convertColour(Color original)
  {
    int light = original.getRed();
    int alpha = MAX_CHANNEL_VALUE - TRANSPARENCY_SLOPE * (light - MAX_LIGHT_TO_KEEP);

    return new Color(light, light, light, Math.min(MAX_CHANNEL_VALUE,
                                                   Math.max(0, alpha)));
  }

//------------------------------------------------------------------------------

}
