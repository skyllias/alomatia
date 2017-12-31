
package org.skyllias.alomatia.filter.buffered;

import java.awt.*;
import java.util.*;

/** Calculator of the minimum or maximum of each separate channel of a collection of colours. */

public class MinMaxChannelCalculator implements SurroundingColoursCalculator
{
  private boolean useMaxRed, useMaxGreen, useMaxBlue;                           // if true, the maximum is to be used; if false, the minimum

//==============================================================================

  public MinMaxChannelCalculator(boolean maximumRed, boolean maximumGreen, boolean maximumBlue)
  {
    useMaxRed   = maximumRed;
    useMaxGreen = maximumGreen;
    useMaxBlue  = maximumBlue;
  }

//==============================================================================

  @Override
  public Color getColour(Collection<Color> surroundingColours)
  {
    final int MIN = 0;
    final int MAX = 255;

    int minRed   = MAX, maxRed   = MIN;
    int minGreen = MAX, maxGreen = MIN;
    int minBlue  = MAX, maxBlue  = MIN;

    for (Color currentColour : surroundingColours)
    {
      int red = currentColour.getRed();
      if (red < minRed) minRed = red;
      if (red > maxRed) maxRed = red;

      int green = currentColour.getGreen();
      if (green < minGreen) minGreen = green;
      if (green > maxGreen) maxGreen = green;

      int blue = currentColour.getBlue();
      if (blue < minBlue) minBlue = blue;
      if (blue > maxBlue) maxBlue = blue;
    }

    return new Color(useMaxRed? maxRed: minRed,
                     useMaxGreen? maxGreen: minGreen,
                     useMaxBlue? maxBlue: minBlue);
  }

//------------------------------------------------------------------------------

}
