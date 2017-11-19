
package org.skyllias.alomatia.filter.hsb;

/** {@link HueFunction} that delegates the call to another one and only returns
 *  values that were positive in the delegate, and zero for the others. */

public class PositiveFilteringHueFunction implements HueFunction
{
  private HueFunction delegate;

//==============================================================================

  /** Creates an instance that filters out the negative values from delegateFunction. */

  public PositiveFilteringHueFunction(HueFunction delegateFunction)
  {
    delegate = delegateFunction;
  }

//==============================================================================

  @Override
  public double getValue(float hue)
  {
    double originalValue = delegate.getValue(hue);
    if (originalValue > 0) return originalValue;
    else                   return 0;
  }

//------------------------------------------------------------------------------

}
