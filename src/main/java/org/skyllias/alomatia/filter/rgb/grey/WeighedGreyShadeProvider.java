
package org.skyllias.alomatia.filter.rgb.grey;

/** Mixer of all channels according to some weights. */

public class WeighedGreyShadeProvider implements GreyShadeProvider
{
  private final ChannelWeights channelWeights;

//==============================================================================

  public WeighedGreyShadeProvider(ChannelWeights channelWeights)
  {
    this.channelWeights = channelWeights;
  }

//==============================================================================

  @Override
  public int getShade(int red, int green, int blue)
  {
    int redWheight   = channelWeights.getRedWeight();
    int greenWheight = channelWeights.getGreenWeight();
    int blueWheight  = channelWeights.getBlueWeight();
    int totalWheight = redWheight + greenWheight + blueWheight;

    int totalLight = red * redWheight +
                     green * greenWheight +
                     blue * blueWheight +
                     totalWheight / 2;                                          // the last term makes the average round instead of truncate

    return totalLight / totalWheight;
  }

//------------------------------------------------------------------------------

}
