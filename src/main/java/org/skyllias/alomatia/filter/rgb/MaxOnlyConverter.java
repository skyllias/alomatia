
package org.skyllias.alomatia.filter.rgb;

import java.awt.Color;

import org.skyllias.alomatia.filter.ColourConverter;

/** Converter that only leaves the channel with the maximum value and those that
 *  are close enough from the remaining channels, zeroing those that are distant
 *  from the maximum value.
 *  The threshold for "close" and "distant" can be chosen at instantiation time. */

public class MaxOnlyConverter implements ColourConverter
{
  private final int threshold;

//==============================================================================

  /** Creates a converter that only keeps the channels whose value is strictly
   *  above <pre>maximum - threshold</pre>, where <pre>maximum</pre> is the
   *  highest value of all channels. */

  public MaxOnlyConverter(int threshold)
  {
    this.threshold = threshold;
  }

//==============================================================================

  @Override
  public Color convertColour(Color original)
  {
    int red   = original.getRed();
    int green = original.getGreen();
    int blue  = original.getBlue();
    int max   = Math.max(red, Math.max(green, blue));

    int minRequired = max - threshold;

    int filteredRed   = (red > minRequired)? red: 0;
    int filteredGreen = (green > minRequired)? green: 0;
    int filteredBlue  = (blue > minRequired)? blue: 0;
    return new Color(filteredRed, filteredGreen, filteredBlue);
  }

//------------------------------------------------------------------------------

}
