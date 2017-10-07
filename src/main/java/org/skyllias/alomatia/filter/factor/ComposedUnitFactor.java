
package org.skyllias.alomatia.filter.factor;

/** {@link UnitFactor} that uses {@link ElasticFactors} to generate a new
 *  function.
 *  If the graph of input and output magnitude is divided in four quadrants,
 *  this factor uses one UnitFactor in the lower-left and another in the
 *  upper-right, so that the (0, 0), (.5, 0.5) and (1, 1) points are fixed and
 *  the other bend upwards or downwards without breaking continuity. */

public class ComposedUnitFactor
{
  private UnitFactor directFactor;                                              // the factor applied to the half of magnitude values that have to increase with positive factors
  private UnitFactor inverseFactor;                                             // the factor applied to the half of magnitude values that have to increase with negative factors

//==============================================================================

  /** Creates a factor that modifies magnitudes according to the value of openFactor:
   *  <ul>
   *  <li> If it is 0, then the magnitude does not change.
   *  <li> If is is lower than 0, the slope of the highest and lowest values
   *       is increased and the slope of middle values is decreased (the graph
   *       takes the shape of a |-|).
   *  <li> If is is higher than 0, the slope of the highest and lowest values
   *       is decreased and the slope of middle values is increased (the graph
   *       takes the shape of _/-).
   *  <li> With large negative numbers (3 and above), everything becomes almost
   *       flattened at a height of 0.5.
   *  <li> With large positive numbers (3 and above), everything becomes almost 0 or 1.
   *  <li> The first noticeable differences occur with absolute values of the order of 0.1.
   *  </ul> */

  public ComposedUnitFactor(double openFactor)
  {
    directFactor  = new ElasticFactor(openFactor);
    inverseFactor = new ElasticFactor(-openFactor);
  }

//==============================================================================

  /** Applies the factor to the original brightness avoiding results outside [0, 1].
   *  If the brightness graph is divided in quadrants, the lowert left and the
   *  upper right get a difeerent {@link UnitFactor} applied. */

  public float apply(float magnitude)
  {
    final float MID_MAGNITUDE = 0.5f;

    boolean isHighMagnitude  = (magnitude > MID_MAGNITUDE);
    UnitFactor factorToApply = isHighMagnitude? directFactor: inverseFactor;

    if (isHighMagnitude) return MID_MAGNITUDE * (1 + factorToApply.apply(magnitude / MID_MAGNITUDE - 1));
    else                 return MID_MAGNITUDE * factorToApply.apply(magnitude / MID_MAGNITUDE);
  }

//------------------------------------------------------------------------------

}
