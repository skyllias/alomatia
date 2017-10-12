
package org.skyllias.alomatia.filter.daltonism;

/** Filter that simulates protanopia working on the LMS space. */

public class LmsProtanopiaFilter extends BasicDaltonizingFilter
{
  private static LmsProjectorFactory projectorFactory = new LmsProjectorFactory();

//==============================================================================

  @Override
  protected ColourProjector getProjector()
  {
    return projectorFactory.getProtanopiaProjector();
  }

//------------------------------------------------------------------------------

}
