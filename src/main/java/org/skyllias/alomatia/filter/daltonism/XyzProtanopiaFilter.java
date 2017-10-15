
package org.skyllias.alomatia.filter.daltonism;

/** Filter that simulates protanopia working on the XYZ space. */

public class XyzProtanopiaFilter extends BasicDaltonizingFilter
{
  private static XyzProjectorFactory projectorFactory = new XyzProjectorFactory();

//==============================================================================

  @Override
  protected ColourProjector getProjector()
  {
    return projectorFactory.getProtanopiaProjector();
  }

//------------------------------------------------------------------------------

}
