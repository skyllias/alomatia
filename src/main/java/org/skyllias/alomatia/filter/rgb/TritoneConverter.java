
package org.skyllias.alomatia.filter.rgb;

import java.awt.Color;

import org.skyllias.alomatia.filter.ColourConverter;

/** Converter that interpolates among colours depending on the original brightness:
 *  One colour is used to replace darkest colours, one for brightest colours and
 *  one for colours in the middle. Intermediate colors are replaced by a
 *  combination of the darkest and the middle colours or by a combination of the
 *  brightest and the middle colours, depending on their proximity of their
 *  brightness to the extremes.
 *  As an HsbConverter it would be less efficient. */

public class TritoneConverter implements ColourConverter
{
  private static final float LOWEST_BRIGHTNESS  = 0;
  private static final float MID_BRIGHTNESS     = 0.5f;
  private static final float HIGHEST_BRIGHTNESS = 1;

  private final Color darkColour;
  private final Color midColour;
  private final Color brightColour;

//==============================================================================

  public TritoneConverter(Color darkColour, Color midColour, Color brightColour)
  {
    this.darkColour   = darkColour;
    this.midColour    = midColour;
    this.brightColour = brightColour;
  }

//==============================================================================

  @Override
  public Color convertColour(Color original)
  {
    float brightness = Color.RGBtoHSB(original.getRed(),
                                      original.getGreen(),
                                      original.getBlue(), null)[2];
    if (brightness < MID_BRIGHTNESS) return interpolateDark(brightness);
    else                             return interpolateBright(brightness);
  }

//------------------------------------------------------------------------------

  private Color interpolateDark(float brightness)
  {
    return interpolate(brightness, LOWEST_BRIGHTNESS, darkColour,
                       MID_BRIGHTNESS, midColour);
  }

//------------------------------------------------------------------------------

  private Color interpolateBright(float brightness)
  {
    return interpolate(brightness, MID_BRIGHTNESS, midColour,
                       HIGHEST_BRIGHTNESS, brightColour);
  }

//------------------------------------------------------------------------------

  private Color interpolate(float brightness, float darkLimit, Color dark,
                            float brightLimit, Color bright)
  {
    float distanceToDark   = brightness - darkLimit;
    float distanceToBright = brightLimit - brightness;
    return interpolate(distanceToDark, dark, distanceToBright, bright);
  }

//------------------------------------------------------------------------------

  private Color interpolate(float distanceToDark, Color dark,
                            float distanceToBright, Color bright)
  {
    int interpolatedRed   = interpolateChannel(distanceToDark, dark.getRed(), distanceToBright, bright.getRed());
    int interpolatedGreen = interpolateChannel(distanceToDark, dark.getGreen(), distanceToBright, bright.getGreen());
    int interpolatedBlue  = interpolateChannel(distanceToDark, dark.getBlue(), distanceToBright, bright.getBlue());

    return new Color(interpolatedRed, interpolatedGreen, interpolatedBlue);
  }

//------------------------------------------------------------------------------

  private int interpolateChannel(float distanceToDark, int darkChannel,
                                 float distanceToBright, int brightChannel)
  {
    float totalDistance   = distanceToDark + distanceToBright;
    float normalizedLevel = distanceToDark / totalDistance;
    return darkChannel + Math.round((brightChannel - darkChannel) * normalizedLevel);
  }

//------------------------------------------------------------------------------

}
