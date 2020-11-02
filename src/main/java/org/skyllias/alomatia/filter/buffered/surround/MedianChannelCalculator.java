
package org.skyllias.alomatia.filter.buffered.surround;

import java.awt.Color;
import java.util.Arrays;
import java.util.Collection;

/** Calculator of the median of each separate channel of a collection of colours. */

public class MedianChannelCalculator implements SurroundingColoursCalculator
{
//==============================================================================

  /** If surroundingColours contains an even amount of items, the median is taken
   *  as the value immediately below the middle. */

  @Override
  public Color getColour(Collection<Color> surroundingColours)
  {
    int amountOfColours = surroundingColours.size();
    int[] reds          = new int[amountOfColours];
    int[] greens        = new int[amountOfColours];
    int[] blues         = new int[amountOfColours];

    int currentIndex = 0;
    for (Color currentColour : surroundingColours)
    {
      reds[currentIndex]   = currentColour.getRed();
      greens[currentIndex] = currentColour.getGreen();
      blues[currentIndex]  = currentColour.getBlue();
      currentIndex++;
    }

    Arrays.sort(reds);
    Arrays.sort(greens);
    Arrays.sort(blues);
    int middle = amountOfColours / 2;
    return new Color(reds[middle], greens[middle], blues[middle]);
  }

//------------------------------------------------------------------------------

}
