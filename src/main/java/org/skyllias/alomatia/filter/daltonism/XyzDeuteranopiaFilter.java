
package org.skyllias.alomatia.filter.daltonism;

/** Filter that simulates deuteranopia working on the XYZ space. */

public class XyzDeuteranopiaFilter extends BasicDaltonizingFilter
{
  private static XyzProjectorFactory projectorFactory = new XyzProjectorFactory();

//==============================================================================

  @Override
  protected ColourProjector getProjector()
  {
    return projectorFactory.getDeuteranopiaProjector();
  }

//------------------------------------------------------------------------------

}
