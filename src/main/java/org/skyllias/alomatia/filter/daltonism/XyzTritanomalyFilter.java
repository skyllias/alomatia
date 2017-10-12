
package org.skyllias.alomatia.filter.daltonism;

/** Filter that simulates tritanomaly working on the XYZ space. */

public class XyzTritanomalyFilter extends BasicDaltonizingFilter
{
  private static XyzProjectorFactory projectorFactory = new XyzProjectorFactory();

//==============================================================================

  @Override
  protected ColourProjector getProjector()
  {
    return projectorFactory.getTritanomalyProjector();
  }

//------------------------------------------------------------------------------

}
