
package org.skyllias.alomatia.filter.daltonism;

/** Filter that simulates protanomaly working on the XYZ space. */

public class XyzProtanomalyFilter extends BasicDaltonizingFilter
{
  private static XyzProjectorFactory projectorFactory = new XyzProjectorFactory();

//==============================================================================

  @Override
  protected ColourProjector getProjector()
  {
    return projectorFactory.getProtanomalyProjector();
  }

//------------------------------------------------------------------------------

}
