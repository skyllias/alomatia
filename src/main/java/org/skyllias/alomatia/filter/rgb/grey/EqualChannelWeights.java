
package org.skyllias.alomatia.filter.rgb.grey;

/** ChannelWights where all channels contribute the same. */

public class EqualChannelWeights implements ChannelWeights
{
//==============================================================================

  @Override
  public int getRedWeight() {return 1;}

  @Override
  public int getGreenWeight() {return 1;}

  @Override
  public int getBlueWeight() {return 1;}

//------------------------------------------------------------------------------

}
