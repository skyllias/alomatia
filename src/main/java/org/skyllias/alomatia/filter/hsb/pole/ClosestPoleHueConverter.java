
package org.skyllias.alomatia.filter.hsb.pole;

import org.skyllias.alomatia.filter.hsb.HsbConverter;

/** Converter that shifts hue towards the closest of one or more poles. */

public class ClosestPoleHueConverter implements HsbConverter
{
  private final AttractionPoles attractionPoles;

//==============================================================================

  /** Creates a filter that modifies the hue of images by finding the closest
   *  from the hues of poles and then applying the passed attraction.
   *  A pole passed twice makes no difference. */

  public ClosestPoleHueConverter(Attraction attraction, float... huePoles)
  {
    attractionPoles = new AttractionPoles(attraction, huePoles);
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
