
package org.skyllias.alomatia.filter.rgb.grey;

import java.util.Arrays;

/** GreyLevelProvider that takes the intermediate value from all channels. */

public class MedianChannelGreyShadeProvider implements GreyShadeProvider
{
//==============================================================================

  @Override
  public int getShade(int red, int green, int blue)
  {
    int[] values = new int[] {red, green, blue};
    Arrays.sort(values);

    return values[1];
  }

//------------------------------------------------------------------------------

}
