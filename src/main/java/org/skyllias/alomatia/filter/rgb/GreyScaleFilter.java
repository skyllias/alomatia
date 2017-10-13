
package org.skyllias.alomatia.filter.rgb;

/** Demo filter that converts all colours to grey tones, with all channels
 *  contributing equally. */

public class GreyScaleFilter extends WheighedGreyScaleFilter
{
//==============================================================================

  @Override
  protected int getRedWheight() {return 1;}

  @Override
  protected int getGreenWheight() {return 1;}

  @Override
  protected int getBlueWheight() {return 1;}

//------------------------------------------------------------------------------

}
