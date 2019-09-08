
package org.skyllias.alomatia.filter.hsb.pole;

import java.awt.Color;

import org.skyllias.alomatia.filter.hsb.HsbConverter.HsbAdapter;

/** Converter that shifts hue towards the closest of one or more poles. */

public class ClosestPoleHueConverter extends HsbAdapter
{
  private final AttractionPoles attractionPoles;

//==============================================================================

  /** Creates a filter that modifies the hue of images by finding the closest
   *  from the hues of poles and then applying the passed attraction.
   *  A pole passed twice makes no difference.
   *  A colour with saturation 0 may have unexpected effects over the inner
   *  calculations, since the hue is then indeterministically defined. */

  public ClosestPoleHueConverter(Attraction attraction, Color... colourPoles)
  {
    attractionPoles = new AttractionPoles(attraction, colourPoles);
  }

//==============================================================================

  /** Applies the attraction to the closest pole. */

  @Override
  public float getNewHue(float hue, float saturation, float brightness)
  {
    float closestPole = findClosestPole(hue);
    return hue - attractionPoles.getAttraction(closestPole, hue);
  }

//------------------------------------------------------------------------------

  /* Returns the value from poles whose distance is smallest to hue.
   * If there are two equidistant to hue, the first one is returned. */

  private float findClosestPole(float hue)
  {
    float closestPole      = 0;
    float smallestDistance = 1;
    for (float currentPole : attractionPoles.getAllPoles())
    {
      float currentDistance = attractionPoles.getDistance(currentPole, hue);
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
