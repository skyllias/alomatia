
package org.skyllias.alomatia.filter.buffered.surround;

import java.awt.Color;
import java.util.Collection;

/** Calculator of the amount of light of a collection of colours, translating it
 *  (by means of a BlackOrWhiteSelector) into either a black or a white colour. */

public class LightCalculator implements SurroundingColoursCalculator
{
  private final LightMeter lightMeter;
  private final BlackOrWhiteSelector blackOrWhiteSelector;

//==============================================================================

  public LightCalculator(LightMeter lightMeter,
                         BlackOrWhiteSelector blackOrWhiteSelector)
  {
    this.lightMeter           = lightMeter;
    this.blackOrWhiteSelector = blackOrWhiteSelector;
  }

//==============================================================================

  @Override
  public Color getColour(Collection<Color> surroundingColours)
  {
    float lightness = lightMeter.getLight(surroundingColours);
    return blackOrWhiteSelector.chooseBlackOrWhite(lightness);
  }

//------------------------------------------------------------------------------

}
