
package org.skyllias.alomatia.filter.rgb.grey;

/** GreyLevelProvider that takes the lowest value from all channels. */

public class MinChannelGreyShadeProvider implements GreyShadeProvider
{
//==============================================================================

  @Override
  public int getShade(int red, int green, int blue)
  {
    return Math.min(Math.min(red, green), blue);
  }

//------------------------------------------------------------------------------

}
