
package org.skyllias.alomatia.ui.filter;

/** Provider of instances of FilterSearchHistory. */

public class FilterSearchHistoryFactory
{
//==============================================================================

  public FilterSearchHistory newInstance()
  {
    return new LruFilterSearchHistory(new SortedHistoryRepository());
  }

//------------------------------------------------------------------------------

}
