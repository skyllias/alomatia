
package org.skyllias.alomatia.filter.daltonism;

/** Filter that simulates achromatomaly working on the XYZ space. */

public class XyzAchromatomalyFilter extends BasicDaltonizingFilter
{
  private static XyzProjectorFactory projectorFactory = new XyzProjectorFactory();

//==============================================================================

  @Override
  protected ColourProjector getProjector()
  {
    return projectorFactory.getAchromatomalyProjector();
  }

//------------------------------------------------------------------------------

}
