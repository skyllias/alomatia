
package org.skyllias.alomatia.filter;

import java.awt.image.*;
import java.util.*;

import org.skyllias.alomatia.display.*;

/** Provider of instances of {@link NamedFilter} appliable to an {@link FilterableDisplay}.
 *  <p>
 *  All instances obtained must comply with the requirements in
 *  {@link FilterableDisplay#setImageFilter(ImageFilter)}. */

public interface FilterFactory
{
  /** Returns all the appliable filters without any categorization.
   *  Instances may or may not be reused in subsequent invocations. */

  Collection<NamedFilter> getAllAvailableFilters();
}
