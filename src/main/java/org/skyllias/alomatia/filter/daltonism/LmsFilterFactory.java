
package org.skyllias.alomatia.filter.daltonism;

import java.awt.image.ImageFilter;

import org.skyllias.alomatia.filter.ColourFilter;

/** Provider of filter instances for the known anomalies in the LMS colour-space. */

public class LmsFilterFactory
{
  private static final LmsProjectorFactory lmsProjectorFactory = new LmsProjectorFactory();

//==============================================================================

  /** Returns the filter for protanopia. */

  public static ImageFilter forProtanopia()
  {
    return forProjector(lmsProjectorFactory.getProtanopiaProjector());
  }

//------------------------------------------------------------------------------

  /** Returns the filter for deuteranopia. */

  public static ImageFilter forDeuteranopia()
  {
    return forProjector(lmsProjectorFactory.getDeuteranopiaProjector());
  }

//------------------------------------------------------------------------------

  /** Returns the filter for tritanopia. */

  public static ImageFilter forTritanopia()
  {
    return forProjector(lmsProjectorFactory.getTritanopiaProjector());
  }

//------------------------------------------------------------------------------

  private static ImageFilter forProjector(ColourProjector projector)
  {
    return new ColourFilter(new DaltonizingColourConverter(projector));
  }

//------------------------------------------------------------------------------

}
