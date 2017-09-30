
package org.skyllias.alomatia.filter.hsb;

/** Modifier of a magnitude defined in the [0, 1] interval that increases or
 *  decreases it without ebbing out. It is parametrized by a real number so that
 *  a 0 does not change the original magnitude, a negative decreases it and a
 *  positive increases it.
 *  <p>
 *  Initially only an implementation is considered with a fixed algorithm, but
 *  if others where to appear an interface would be created. */

public class UnitFactor
{
  private double innerFactor;                                                   // this goes from 0 to infinite, with 1 producing no change, lower values reducing the brightness and higher values increasing it

//==============================================================================

  /** Creates a factor that modifies magnitudes according to the value of openFactor:
   *  <ul>
   *  <li> If it is 0, then the saturation does not change.
   *  <li> If is is lower than 0, it is reduced.
   *  <li> If is is higher than 0, it is increased.
   *  <li> With large negative numbers (3 and above), the magnitude becomes almost 0.
   *  <li> With large positive numbers (3 and above), the magnitude becmoes almost 1.
   *  <li> The first noticeable differences occur with absolute values of the order of 0.1.
   *  </ul>  */

  public UnitFactor(double openFactor)
  {
    innerFactor = Math.exp(openFactor);                                         // this pre-calculation avoids a lot of redundant exponentials later on
  }

//==============================================================================

  /** Same as apply(false, magnitude);. */

  public float apply(float magnitude)
  {
    return apply(false, magnitude);
  }

//------------------------------------------------------------------------------

  /** Applies the factor to the magnitude (which must be inside the [0, 1]
   *  interval) avoiding results outside [0, 1] by:
   *  If the open factor was below 0, multiplying its exponential by the magnitude.
   *  If the open factor was above 1 and avoidZeroBoost is false, multiplying the
   *  inverse of its exponential by the "lack of magnitude" (ie by (1 - magnitude)
   *  and substracting that from 1).
   *  If the open factor was above 1 and avoidZeroBoost is true, "small" magnitudes
   *  are linearly multiplied by the exponential of the open factor, while "big"
   *  magnitudes are handled like when avoidZeroBoost is false. "Small" and "big"
   *  are defined so that the function is continuous.
   *  This avoids problems when magnitude represents for example saturation:
   *  a perfect gray shade cannot be turned deterministically into any hue. */

  public float apply(boolean avoidZeroBoost, float magnitude)
  {
    boolean useLinear = (innerFactor <= 1) ||
                        (avoidZeroBoost && magnitude <= 1 / innerFactor);
    if (useLinear) return (float) (innerFactor * magnitude);
    else           return (float) (1 - (1 - magnitude) / innerFactor);
  }

//------------------------------------------------------------------------------

}
