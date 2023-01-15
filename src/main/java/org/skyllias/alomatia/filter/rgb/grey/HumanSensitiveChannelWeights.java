
package org.skyllias.alomatia.filter.rgb.grey;

/** ChannelWeights that consider that the human eye is more sensitive to green 
 *  and less to blue (at least, according to 
 *  http://www.jhlabs.com/ip/filters/GrayscaleFilter.html). */

public class HumanSensitiveChannelWeights implements ChannelWeights
{
//==============================================================================

  @Override
  public int getRedWeight() {return 77;}

  @Override
  public int getGreenWeight() {return 151;}

  @Override
  public int getBlueWeight() {return 28;}

//------------------------------------------------------------------------------

}
