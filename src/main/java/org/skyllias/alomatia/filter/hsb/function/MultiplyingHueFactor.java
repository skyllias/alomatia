
package org.skyllias.alomatia.filter.hsb.function;

/** {@link HueFunction} that delegates the call to another function and
 *  multiplies its results by some factor.
 *  <p>
 *  This can be used to get negative values for positive only functions. */

public class MultiplyingHueFactor implements HueFunction
{
  private HueFunction delegate;
  private double factor;

//==============================================================================

  /** Creates an instance that returns the values from delegateFunction
   *  multiplied by multiplier. */

  public MultiplyingHueFactor(HueFunction delegateFunction, double multiplier)
  {
    delegate = delegateFunction;
    factor   = multiplier;
  }

//==============================================================================

  @Override
  public double getValue(float hue)
  {
    double originalValue = delegate.getValue(hue);
    return factor * originalValue;
  }

//------------------------------------------------------------------------------

}
