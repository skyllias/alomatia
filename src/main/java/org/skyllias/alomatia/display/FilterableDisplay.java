
package org.skyllias.alomatia.display;

import java.awt.image.ImageFilter;

import org.skyllias.alomatia.filter.NamedFilter;

/** Display with the ability to apply a filter to some image. */

public interface FilterableDisplay
{
//==============================================================================

  /** Modifies the current filter applied to images before being drawn.
   *  <p>
   *  {@link ImageFilter}s inside the {@link NamedFilter}s need not restrict
   *  themselves to changing the colours, since they may apply affine
   *  transformations, blurs, or whatever modification with just one restriction:
   *  the size of the resulting image must be the same as the original's.
   *  <p>
   *  If null (the default), the original image is displayed without modification.
   *  <p>
   *  The displayed image is updated immediately by applying the filter to the
   *  original image. */

  void setImageFilter(NamedFilter imageFilter);

//------------------------------------------------------------------------------

}
