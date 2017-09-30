
package org.skyllias.alomatia.filter.daltonism;

/** Filter that simulates tritanopia. */

public class TritanopiaFilter extends BasicDaltonizingFilter
{
  private static LmsProjectorFactory projectorFactory = new LmsProjectorFactory();

//==============================================================================

  @Override
  protected LmsProjector getLmsProjector()
  {
    return projectorFactory.getTritanopiaProjector();
  }

//------------------------------------------------------------------------------

}
