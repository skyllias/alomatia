
package org.skyllias.alomatia.filter.buffered;

import java.awt.Color;
import java.util.Collection;

/** Calculator of the average of each separate channel of a collection of colours.
 *
 *  Not really meant to be used by {@link SurroundingColoursOp} because the
 *  result would be the same as a ConvolveOp with equally weighed pixels in the
 *  kernel, and a convolution is much faster than what SurroundingColoursOp does.
 *  Anyway, available as a function voer some colours. */

public class AverageChannelCalculator implements SurroundingColoursCalculator
{
//==============================================================================

  /** With Java 8's streams this could be more compact. */

  @Override
  public Color getColour(Collection<Color> surroundingColours)
  {
    int redSum   = 0;
    int greenSum = 0;
    int blueSum  = 0;

    for (Color currentColour : surroundingColours)
    {
      redSum   += currentColour.getRed();
      greenSum += currentColour.getGreen();
      blueSum  += currentColour.getBlue();
    }

    int amountOfColours = surroundingColours.size();
    return new Color(redSum / amountOfColours,
                     greenSum / amountOfColours,
                     blueSum / amountOfColours);
  }

//------------------------------------------------------------------------------

}
