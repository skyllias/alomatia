
package org.skyllias.alomatia.filter.daltonism;

/** Filter that simulates deuteranopia. */

public class DeuteranopiaFilter extends BasicDaltonizingFilter
{
  private static LmsProjectorFactory projectorFactory = new LmsProjectorFactory();

//==============================================================================

  @Override
  protected LmsProjector getLmsProjector()
  {
    return projectorFactory.getDeuteranopiaProjector();
  }

//------------------------------------------------------------------------------

}
