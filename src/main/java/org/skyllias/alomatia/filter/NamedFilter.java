
package org.skyllias.alomatia.filter;

import java.awt.image.ImageFilter;

import org.skyllias.alomatia.i18n.LabelLocalizer;

/** Association of an {@link ImageFilter} with a name. */

public class NamedFilter
{
  private static final String NAME_KEY_FORMAT = "%s.name";

  private final ImageFilter imageFilter;
  private final String filterKey;

//==============================================================================

  /** Associates imageFilter to an identifying string (localizable by means of a
   *  {@link LabelLocalizer}) by appending a fixed suffix). */

  public NamedFilter(ImageFilter imageFilter, String filterKey)
  {
    this.imageFilter = imageFilter;
    this.filterKey   = filterKey;
  }

//==============================================================================

  /** Returns the filter to transform images with.
   *  It may be null. */

  public ImageFilter getFilter()
  {
    return imageFilter;
  }

//------------------------------------------------------------------------------

  /** Returns the key associated to the filter.
   *  It should never be null. */

  public String getFilterKey()
  {
    return filterKey;
  }

//------------------------------------------------------------------------------

  /** Returns the key associated to the name of the filter.
   *  It should never be null. */

  public String getNameKey()
  {
    return String.format(NAME_KEY_FORMAT, filterKey);
  }

//------------------------------------------------------------------------------

}
