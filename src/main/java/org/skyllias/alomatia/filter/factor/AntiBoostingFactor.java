
package org.skyllias.alomatia.filter.factor;

/** BasicExponentialFactor that behaves like {@link SimpleFactor} when decreasing,
 *  but keeps a linear factor with low magnitudes when increasing, so that 0 is
 *  returned when applied to a magnitude of 0 and there is no continuity break in
 *  the function.
 *  <p>
 *  This prevents sharp colour changes when for example the saturation is increased.
 *  Whitish colours are shown with a very noticeable, unnatural hue if SimpleFactor
 *  is used. */

public class AntiBoostingFactor extends BasicExponentialFactor
{
//==============================================================================

  public AntiBoostingFactor(double openFactor) {super(openFactor);}

//==============================================================================

  /** Applies the factor to the magnitude by:
   *  If the open factor was below 0, multiplying its exponential by the magnitude.
   *  If the open factor was above 1, "small" magnitudes  are linearly
   *  multiplied by the exponential of the open factor, while "big"
   *  magnitudes are handled like when SimpleFactor. "Small" and "big"
   *  are defined so that the function is continuous.
   *  This avoids problems when magnitude represents for example saturation:
   *  a perfect gray shade cannot be turned deterministically into any hue. */

  @Override
  public float apply(float magnitude)
  {
    double slope      = getSlope();
    boolean useLinear = (slope <= 1) || (magnitude <= 1/slope);
    if (useLinear) return (float) (slope * magnitude);
    else           return (float) (1 - (1 - magnitude) / slope);
  }

//------------------------------------------------------------------------------

}
