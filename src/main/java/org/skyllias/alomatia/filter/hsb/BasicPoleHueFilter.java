
package org.skyllias.alomatia.filter.hsb;

import java.awt.*;

/** Superclass for the filters that shift hues depending on the attraction from
 *  one or more poles. */

public abstract class BasicPoleHueFilter extends BasicHSBFilter
{
  private HueDistance distance = new HueDistance();

  private Attraction attraction;
  private float[] poles;

//==============================================================================

  protected BasicPoleHueFilter(Attraction attraction, Color... colourPoles)
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

  /** Offers all the received poles to subclasses. */

  protected float[] getAllPoles() {return poles;}

//------------------------------------------------------------------------------

  /** Returns the distance between hue1 and hue2. */

  protected float getDistance(float hue1, float hue2) {return distance.calculate(hue1, hue2);}

//------------------------------------------------------------------------------

  /** Returns the attraction exerted on hue by pole. */

  protected float getAttraction(float pole, float hue)
  {
    float difference = distance.difference(pole, hue);
    return attraction.attract(difference);
  }

//------------------------------------------------------------------------------

}
