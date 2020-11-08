
package org.skyllias.alomatia.ui.filter;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.prefs.Preferences;

import org.apache.commons.lang.StringUtils;

/** Repository to save and retrieve a list of strings.
 *  The storage is based on {@link Preferences}. */

public class SortedHistoryRepository
{
  private static final String PREFKEY_SORTED_SEARCH_HISTORY = "sortedSearchHistory";
  private static final String ENTRY_SEPARATOR               = "///";

  private Preferences preferences = Preferences.userNodeForPackage(getClass());

//==============================================================================

  public SortedHistoryRepository() {}

//------------------------------------------------------------------------------

  /** Meant only for testing purposes. */

  protected SortedHistoryRepository(Preferences preferences)
  {
    this.preferences = preferences;
  }

//==============================================================================

  public List<String> get()
  {
    String formattedList = preferences.get(PREFKEY_SORTED_SEARCH_HISTORY, "");
    if (StringUtils.isBlank(formattedList)) return new LinkedList<>();

    return Arrays.asList(formattedList.split(ENTRY_SEPARATOR));
  }

//------------------------------------------------------------------------------

  public void save(List<String> history)
  {
    String formattedList = StringUtils.join(history, ENTRY_SEPARATOR);
    preferences.put(PREFKEY_SORTED_SEARCH_HISTORY, formattedList);
  }

//------------------------------------------------------------------------------

}
