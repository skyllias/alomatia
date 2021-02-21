
package org.skyllias.alomatia.filter.buffered.surround;

import java.awt.Color;
import java.util.Collection;
import java.util.function.ToIntFunction;

/** Calculator of the average of each separate channel of a collection of colours.
 *
 *  Not really meant to be used by {@link SurroundingColoursOperation} because
 *  the result would be the same as a ConvolveOp with equally weighed pixels in
 *  the kernel, and a convolution is much faster than what SurroundingColoursOp
 *  does. Anyway, available as a function for some colours. */

public class AverageChannelCalculator implements SurroundingColoursCalculator
{
//==============================================================================

  @Override
  public Color getColour(Collection<Color> surroundingColours)
  {
    int redAverage   = getChannelAverage(surroundingColours, Color::getRed);
    int greenAverage = getChannelAverage(surroundingColours, Color::getGreen);
    int blueAverage  = getChannelAverage(surroundingColours, Color::getBlue);

    return new Color(redAverage, greenAverage, blueAverage);
  }

//------------------------------------------------------------------------------

  /* Returns the floor of the average of the selected channel over all colours. */

  private int getChannelAverage(Collection<Color> colours,
                                ToIntFunction<Color> channelSelector)
  {
    return (int) colours.stream()
                        .mapToInt(channelSelector)
                        .average()
                        .orElse(0);
  }

//------------------------------------------------------------------------------

}
