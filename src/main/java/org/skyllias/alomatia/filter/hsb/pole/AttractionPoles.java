
package org.skyllias.alomatia.filter.hsb.pole;

import org.apache.commons.lang.ArrayUtils;

/** Composition of an {@link Attraction} and one or more hue values, which
 *  together specify how an original hue may be shifted depending on its
 *  distance to the different poles. */

public class AttractionPoles
{
  private final HueDistance distance = new HueDistance();

  private final Attraction attraction;
  private final float[] poles;

//==============================================================================

  protected AttractionPoles(Attraction attraction, float... huePoles)
  {
    this.attraction = attraction;
    this.poles = ArrayUtils.clone(huePoles);
  }

//==============================================================================

  /** Offers all the received poles. */

  public float[] getAllPoles() {return poles;}

//------------------------------------------------------------------------------

  /** Returns the distance between hue1 and hue2. */

  public float getDistance(float hue1, float hue2) {return distance.calculate(hue1, hue2);}

//------------------------------------------------------------------------------

  /** Returns the attraction exerted on hue by pole. */

  public float getAttraction(float pole, float hue)
  {
    float difference = distance.difference(pole, hue);
    return attraction.attract(difference);
  }

//------------------------------------------------------------------------------

}
