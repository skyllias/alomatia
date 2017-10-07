
package org.skyllias.alomatia.filter.factor;

/** Superclass for the UnitFactors that are generated from an real open factor
 *  that can take negative or positive values, with the former decreasing
 *  the magnitude when applied, the latter increasing it, and 0 leaving it the same.
 *  <p>
 *  The factor function is mostly of constant slope, but the exponential in the
 *  name refers to the relationship between the generating open factor and the
 *  resulting slope. */

public abstract class BasicExponentialFactor implements UnitFactor
{
  private double slope;                                                         // this goes from 0 to infinite, with 1 producing no change, lower values reducing the magnitude and higher values increasing it

//==============================================================================

  /** Creates a factor that modifies magnitudes according to the value of openFactor:
   *  <ul>
   *  <li> If it is 0, then the magnitude does not change.
   *  <li> If is is lower than 0, it is reduced, with 0 always being 0.
   *  <li> If is is higher than 0, it is increased, with 1 always being 1.
   *  <li> With large negative numbers (3 and above), the magnitude becomes almost 0 for most original values.
   *  <li> With large positive numbers (3 and above), the magnitude becomes almost 1 for most original values.
   *  <li> The first noticeable differences occur with absolute values of the order of 0.1.
   *  </ul>  */

  public BasicExponentialFactor(double openFactor)
  {
    slope = Math.exp(openFactor);                                               // this pre-calculation avoids a lot of redundant exponentials later on
  }

//==============================================================================

  /** Offers the already calculated factor to be used as the main slope. */

  protected double getSlope() {return slope;}

//------------------------------------------------------------------------------

}
