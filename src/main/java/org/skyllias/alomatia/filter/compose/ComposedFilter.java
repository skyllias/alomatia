
package org.skyllias.alomatia.filter.compose;

import java.awt.image.*;
import java.util.*;

import org.skyllias.alomatia.display.*;

/** Filter that delegates the image modifications to other filters.
 *  It is meant to cancatenate in order two or more different filters (all
 *  complying the requirements described at {@link FilterableDisplay#setImageFilter(ImageFilter)}),
 *  producing aggregated effects. Two filters applied in different order may
 *  produce significantly different results.
 *  TODO unit test */

public class ComposedFilter extends ImageFilter
{
  private List<ImageFilter> filters;                                            // the real filters, with the first one to apply at 0

//==============================================================================

  /** Sets up a filter that will apply the items in delegateFilters in order.
   *  There may be any amount of filters, but obviously less than two are useless.
   *  The processing time will be roughly the sum of the separate processing times. */

  public ComposedFilter(ImageFilter... delegateFilters)
  {
    filters = Arrays.asList(delegateFilters);
  }

//==============================================================================

  /** Creates a chain of consumers between the delegate filters. */

  @Override
  public ImageFilter getFilterInstance(ImageConsumer imageConsumer)
  {
    ImageFilter consumerInstance = super.getFilterInstance(imageConsumer);
    if (!filters.isEmpty())
    {
      List<ImageFilter> reversedFilters = new LinkedList<>(filters);            // the filters are looped from last to first to linked them
      Collections.reverse(reversedFilters);

      ;
      for (ImageFilter currentFilter : reversedFilters)
      {
        consumerInstance = currentFilter.getFilterInstance(imageConsumer);
        imageConsumer    = consumerInstance;
      }
    }
    return consumerInstance;
  }

//------------------------------------------------------------------------------

}
