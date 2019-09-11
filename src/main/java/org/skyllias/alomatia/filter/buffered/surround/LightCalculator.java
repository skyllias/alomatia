
package org.skyllias.alomatia.filter.buffered.surround;

import java.awt.Color;
import java.util.Collection;

/** Calculator of the amount of light of a collection of colours, translating it
 *  (by means of a BlackOrWhiteSelector) into either a black or a white colour.
 *  Notice that this is not the same as brightness in the HSB colour-space, as
 *  a pure red here is dark while there it has brightness 1. */

public class LightCalculator implements SurroundingColoursCalculator
{
  private final BlackOrWhiteSelector blackOrWhiteSelector;

//==============================================================================

  public LightCalculator(BlackOrWhiteSelector blackOrWhiteSelector)
  {
    this.blackOrWhiteSelector = blackOrWhiteSelector;
  }

//==============================================================================

  @Override
  public Color getColour(Collection<Color> surroundingColours)
  {
    final float MAX_LIGHT_PER_PIXEL = 3 * 0xFF;

    float totalLight       = 0;
    float maxPossibleLight = 0;

    for (Color color : surroundingColours)
    {
      totalLight       += color.getRed() + color.getGreen() + color.getBlue();
      maxPossibleLight += MAX_LIGHT_PER_PIXEL;
    }

    float lightness = totalLight / maxPossibleLight;
    return blackOrWhiteSelector.chooseBlackOrWhite(lightness);
  }

//------------------------------------------------------------------------------

}
