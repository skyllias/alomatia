
package org.skyllias.alomatia.filter.rgb;

import java.awt.Color;

import org.skyllias.alomatia.filter.ColorConverter;

/** Converter of any colour into grey shades. */

public class WeighedGreyScaleConverter implements ColorConverter
{
  private final ChannelWeights channelWeights;

//==============================================================================

  public WeighedGreyScaleConverter(ChannelWeights channelWeights)
  {
    this.channelWeights = channelWeights;
  }

//==============================================================================

  @Override
  public Color convertColor(Color original)
  {
    int red   = original.getRed();
    int green = original.getGreen();
    int blue  = original.getBlue();

    int redWheight   = channelWeights.getRedWeight();
    int greenWheight = channelWeights.getGreenWeight();
    int blueWheight  = channelWeights.getBlueWeight();
    int totalWheight = redWheight + greenWheight + blueWheight;

    int totalLight = red * redWheight +
                     green * greenWheight +
                     blue + blueWheight +
                     totalWheight / 2;                                          // the last term makes the average round instead of truncate
    int average    = totalLight / totalWheight;
    return new Color(average, average, average);
  }

//------------------------------------------------------------------------------

}
