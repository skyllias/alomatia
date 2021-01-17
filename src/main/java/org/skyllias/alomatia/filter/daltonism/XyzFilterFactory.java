
package org.skyllias.alomatia.filter.daltonism;

import java.awt.image.ImageFilter;

import org.skyllias.alomatia.filter.ColourFilter;

/** Provider of filter instances for the known anomalies in the XYZ colour-space. */

public class XyzFilterFactory
{
  private static final ProjectableColourFactory projectableColourFactory = new ProjectableColourFactory();
  private static final XyzProjectorFactory xyzProjectorFactory           = new XyzProjectorFactory();

//==============================================================================

  /** Returns the filter for protanopia. */

  public static ImageFilter forProtanopia()
  {
    return forProjector(xyzProjectorFactory.getProtanopiaProjector());
  }

//------------------------------------------------------------------------------

  /** Returns the filter for protanomaly. */

  public static ImageFilter forProtanomaly()
  {
    return forProjector(xyzProjectorFactory.getProtanomalyProjector());
  }

//------------------------------------------------------------------------------

  /** Returns the filter for deuteranopia. */

  public static ImageFilter forDeuteranopia()
  {
    return forProjector(xyzProjectorFactory.getDeuteranopiaProjector());
  }

//------------------------------------------------------------------------------

  /** Returns the filter for deuteranomaly. */

  public static ImageFilter forDeuteranomaly()
  {
    return forProjector(xyzProjectorFactory.getDeuteranomalyProjector());
  }

//------------------------------------------------------------------------------

  /** Returns the filter for tritanopia. */

  public static ImageFilter forTritanopia()
  {
    return forProjector(xyzProjectorFactory.getTritanopiaProjector());
  }

//------------------------------------------------------------------------------

  /** Returns the filter for tritanomaly. */

  public static ImageFilter forTritanomaly()
  {
    return forProjector(xyzProjectorFactory.getTritanomalyProjector());
  }

//------------------------------------------------------------------------------

  /** Returns the filter for achromatopsia. */

  public static ImageFilter forAchromatopsia()
  {
    return forProjector(xyzProjectorFactory.getAchromatopsiaProjector());
  }

//------------------------------------------------------------------------------

  /** Returns the filter for achromatomaly. */

  public static ImageFilter forAchromatomaly()
  {
    return forProjector(xyzProjectorFactory.getAchromatomalyProjector());
  }

//------------------------------------------------------------------------------

  private static ImageFilter forProjector(ColourProjector projector)
  {
    return new ColourFilter(new DaltonizingColourConverter(projectableColourFactory, projector));
  }

//------------------------------------------------------------------------------

}
