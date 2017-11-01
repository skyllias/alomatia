
package org.skyllias.alomatia.filter.hsb;

import java.awt.*;

/** Filter that shifts hue towards the closest of one or more poles. */

public class ClosestPoleHueFilter extends BasicHSBFilter
{
  private HueDistance distance = new HueDistance();

  private Attraction attraction;
  private float[] poles;

//==============================================================================

  /** Creates a filter that modifies the hue of images by finding the closest
   *  from the hues of poles and then applying the passed attraction.
   *  A pole passed twice makes no difference.
   *  A colour with saturation 0 may have unexpected effects over the inner
   *  calculations, since the hue is then indeterministically defined. However,
   *  the resulting hue is irrelevant because the saturation is 0 and the
   *  corresponding colour will be the same shade of grey. */

  public ClosestPoleHueFilter(Attraction attraction, Color... colourPoles)
  {
    this.attraction = attraction;

    this.poles = new float[colourPoles.length];
    for (int i = 0; i < poles.length; i++)
    {
      Color currentColour = colourPoles[i];
      poles[i]            = Color.RGBtoHSB(currentColour.getRed(),
                                           currentColour.getGreen(),
                                           currentColour.getBlue(), null)[0];
    }
  }

//==============================================================================

  /** Applies the attraction to the closest pole. */

  @Override
  protected float getNewHue(float hue, float saturation, float brightness)
  {
    float closestPole = findClosestPole(hue);
    float difference  = distance.difference(closestPole, hue);
    return hue - attraction.attract(difference);
  }

//------------------------------------------------------------------------------

  /* Returns the value from poles whose distance is smallest to hue.
   * If there are two equidistant to hue, the first one is returned. */

  private float findClosestPole(float hue)
  {
    float closestPole      = 0;
    float smallestDistance = 1;
    for (float currentPole : poles)
    {
      float currentDistance = distance.calculate(currentPole, hue);
      if (currentDistance < smallestDistance)
      {
        closestPole      = currentPole;
        smallestDistance = currentDistance;
      }
    }
    return closestPole;
  }

//------------------------------------------------------------------------------

}
