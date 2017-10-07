
package org.skyllias.alomatia.filter.factor;

/** BasicExponentialFactor that behaves like {@link AntiBoostingFactor} when
 *  increasing, but always returns 1 when applied to 1, while keeping the
 *  function continuous.
 *  <p>
 *  This results in either a |- shape when increasing or a _| shape when decreasing,
 *  and hence the class name as it resembles an elastic string tied at (0, 0) and
 *  (1, 1) and pullable from the centre towards (1, 0) or (0, 1). */

public class ElasticFactor extends BasicExponentialFactor
{
//==============================================================================

  public ElasticFactor(double openFactor) {super(openFactor);}

//==============================================================================

  /** Applies the factor to the magnitude by:
   *  If the magnitude is "small", multiplying the slope by the magnitude.
   *  If the magnitude is "big", substracting from 1 the multiplication of the
   *  slope and the distance of magnitude from 1.
   *  "Small" and "big" are defined so that the function is continuous. */

  @Override
  public float apply(float magnitude)
  {
    double slope = getSlope();

    boolean isSmall = magnitude <= 1/slope;
    if (isSmall) return (float) (slope * magnitude);
    else         return (float) (1 - (1 - magnitude) / slope);
  }

//------------------------------------------------------------------------------

}
