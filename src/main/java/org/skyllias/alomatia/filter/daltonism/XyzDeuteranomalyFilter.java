
package org.skyllias.alomatia.filter.daltonism;

/** Filter that simulates deuteranomaly working on the XYZ space. */

public class XyzDeuteranomalyFilter extends BasicDaltonizingFilter
{
  private static XyzProjectorFactory projectorFactory = new XyzProjectorFactory();

//==============================================================================

  @Override
  protected ColourProjector getProjector()
  {
    return projectorFactory.getDeuteranomalyProjector();
  }

//------------------------------------------------------------------------------

}
