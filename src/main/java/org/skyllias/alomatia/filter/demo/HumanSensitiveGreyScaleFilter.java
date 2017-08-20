
package org.skyllias.alomatia.filter.demo;

/** Filter that turns all colours to greys considering that the human eye is
 *  more sensitive to green and less to blue (at least, according to
 *  http://www.jhlabs.com/ip/filters/GrayscaleFilter.html). */

public class HumanSensitiveGreyScaleFilter extends WheighedGreyScaleFilter
{
//==============================================================================

  @Override
  protected int getRedWheight() {return 77;}

  @Override
  protected int getGreenWheight() {return 151;}

  @Override
  protected int getBlueWheight() {return 28;}

//------------------------------------------------------------------------------

}
