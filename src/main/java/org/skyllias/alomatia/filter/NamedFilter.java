
package org.skyllias.alomatia.filter;

import java.awt.image.*;

/** Association of an {@link ImageFilter} with a name. */

public class NamedFilter
{
  private ImageFilter filter;
  private String nameKey;

//==============================================================================

  /** Associates imageFilter to an identifying string (most probably localizable
   *  by means of a {@link LabelLocalizer}). */

  public NamedFilter(ImageFilter imageFilter, String key)
  {
    filter  = imageFilter;
    nameKey = key;
  }

//==============================================================================

  /** Returns the filter to transform images with.
   *  It may be null. */

  public ImageFilter getFilter()
  {
    return filter;
  }

//------------------------------------------------------------------------------

  /** Returns the key associated to the name of the filter.
   *  It should never be null. */

  public String getNameKey()
  {
    return nameKey;
  }

//------------------------------------------------------------------------------

}
