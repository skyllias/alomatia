
package org.skyllias.alomatia.filter.daltonism;

/** Filter that simulates deuteranopia working on the LMS space. */

public class LmsDeuteranopiaFilter extends BasicDaltonizingFilter
{
  private static LmsProjectorFactory projectorFactory = new LmsProjectorFactory();

//==============================================================================

  @Override
  protected ColourProjector getProjector()
  {
    return projectorFactory.getDeuteranopiaProjector();
  }

//------------------------------------------------------------------------------

}
