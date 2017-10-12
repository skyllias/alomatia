
package org.skyllias.alomatia.filter.daltonism;

/** Filter that simulates achromatopsia working on the XYZ space. */

public class XyzAchromatopsiaFilter extends BasicDaltonizingFilter
{
  private static XyzProjectorFactory projectorFactory = new XyzProjectorFactory();

//==============================================================================

  @Override
  protected ColourProjector getProjector()
  {
    return projectorFactory.getAchromatopsiaProjector();
  }

//------------------------------------------------------------------------------

}
