
package org.skyllias.alomatia.filter.daltonism;

/** Filter that simulates protanopia. */

public class ProtanopiaFilter extends BasicDaltonizingFilter
{
  private static LmsProjectorFactory projectorFactory = new LmsProjectorFactory();

//==============================================================================

  @Override
  protected LmsProjector getLmsProjector()
  {
    return projectorFactory.getProtanopiaProjector();
  }

//------------------------------------------------------------------------------

}
