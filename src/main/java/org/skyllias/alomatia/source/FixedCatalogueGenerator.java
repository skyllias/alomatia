
package org.skyllias.alomatia.source;

import org.skyllias.alomatia.ImageDisplay;
import org.skyllias.alomatia.ImageSource;

/** Provider of instances of {@link SourceCatalogue} with instances from some
 *  known {@link ImageSource}s. */

public class FixedCatalogueGenerator
{
//==============================================================================

  /** Returns a new catalogue with new instances with the passed display already set.
   *  Other particular source properties (eg the file in a SingleFileSource) are
   *  not informed. */

  public SourceCatalogue getNewCatalogue(ImageDisplay display)
  {
    SourceCatalogue catalogue = new SourceCatalogue();

    catalogue.add(VoidSource.class,            new VoidSource());
    catalogue.add(DropSource.class,            new DropSource());
    catalogue.add(ClipboardSource.class,       new ClipboardSource());
    catalogue.add(ScreenSource.class,          new ScreenSource());
    catalogue.add(AsynchronousUrlSource.class, new AsynchronousUrlSource());
    catalogue.add(SingleFileSource.class,      new SingleFileSource());
    catalogue.add(DirFileSource.class,         new DirFileSource());

    setDisplay(catalogue, display);
    return catalogue;
  }

//------------------------------------------------------------------------------

  /* Invokes setDisplay(display) on all the instances in the catalogue. */

  private void setDisplay(SourceCatalogue catalogue, ImageDisplay display)
  {
    for (ImageSource currentSource : catalogue.getAllInstances())
    {
      currentSource.setDisplay(display);
    }
  }

//------------------------------------------------------------------------------

}
