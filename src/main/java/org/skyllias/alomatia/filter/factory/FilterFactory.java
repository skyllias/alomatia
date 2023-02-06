
package org.skyllias.alomatia.filter.factory;

import java.awt.image.ImageFilter;
import java.util.Collection;

import org.skyllias.alomatia.display.FilterableDisplay;
import org.skyllias.alomatia.filter.NamedFilter;

/** Provider of instances of {@link NamedFilter} appliable to a {@link FilterableDisplay}.
 *  <p>
 *  All instances obtained must comply with the requirements in
 *  {@link FilterableDisplay#setImageFilter(ImageFilter)}. */

public interface FilterFactory
{
  /** Returns all the appliable filters without any categorization.
   *  Instances may or may not be reused in subsequent invocations. */

  Collection<NamedFilter> getAllAvailableFilters();
}
