
package org.skyllias.alomatia.filter.daltonism;

/** Filter that simulates tritanopia working on the LMS space. */

public class LmsTritanopiaFilter extends BasicDaltonizingFilter
{
  private static LmsProjectorFactory projectorFactory = new LmsProjectorFactory();

//==============================================================================

  @Override
  protected ColourProjector getProjector()
  {
    return projectorFactory.getTritanopiaProjector();
  }

//------------------------------------------------------------------------------

}
