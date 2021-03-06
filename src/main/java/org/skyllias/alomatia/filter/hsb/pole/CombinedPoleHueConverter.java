
package org.skyllias.alomatia.filter.hsb.pole;

import java.awt.Color;

import org.skyllias.alomatia.filter.hsb.HsbConverter.HsbAdapter;

/** Converter that shifts hue combining the attractions of multiple poles. */

public class CombinedPoleHueConverter extends HsbAdapter
{
  private final AttractionPoles attractionPoles;

//==============================================================================

  /** Creates a filter that modifies the hue of images by adding the passed
   *  attraction generated by the hues in colourPoles individually.
   *  A pole passed twice has double effect.
   *  A colour with saturation 0 may have unexpected effects over the inner
   *  calculations, since the hue is then indeterministically defined. */

  public CombinedPoleHueConverter(Attraction attraction, Color... colourPoles)
  {
    attractionPoles = new AttractionPoles(attraction, colourPoles);
  }

//==============================================================================

  /** Applies the attraction to all the poles. */

  @Override
  public float getNewHue(float hue, float saturation, float brightness)
  {
    float accumulatedShift = 0;
    for (float currentPole : attractionPoles.getAllPoles())
    {
      accumulatedShift += attractionPoles.getAttraction(currentPole, hue);
    }
    return hue - accumulatedShift;
  }

//------------------------------------------------------------------------------

}
