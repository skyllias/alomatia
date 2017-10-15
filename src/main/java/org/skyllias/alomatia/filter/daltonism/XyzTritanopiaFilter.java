
package org.skyllias.alomatia.filter.daltonism;

/** Filter that simulates tritanopia working on the XYZ space. */

public class XyzTritanopiaFilter extends BasicDaltonizingFilter
{
  private static XyzProjectorFactory projectorFactory = new XyzProjectorFactory();

//==============================================================================

  @Override
  protected ColourProjector getProjector()
  {
    return projectorFactory.getTritanopiaProjector();
  }

//------------------------------------------------------------------------------

}
