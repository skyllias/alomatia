
package org.skyllias.alomatia.filter.rgb.grey;

/** GreyLevelProvider that takes the highest value from all channels. */

public class MaxChannelGreyShadeProvider implements GreyShadeProvider
{
//==============================================================================

  @Override
  public int getShade(int red, int green, int blue)
  {
    return Math.max(Math.max(red, green), blue);
  }

//------------------------------------------------------------------------------

}
