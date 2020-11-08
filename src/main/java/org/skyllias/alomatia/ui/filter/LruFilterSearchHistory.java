
package org.skyllias.alomatia.ui.filter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/** Naive implementatation of {@link FilterSearchHistory} that keeps track of
 *  all searched strings and returns them based on the inverse order with which
 *  they were last used. Frequency or age are not taken into account. */

public class LruFilterSearchHistory implements FilterSearchHistory
{
  private final SortedHistoryRepository sortedHistoryRepository;

  private List<String> pastSearches;                                            // maintained in memory for searches

//==============================================================================

  public LruFilterSearchHistory(SortedHistoryRepository sortedHistoryRepository)
  {
    this.sortedHistoryRepository = sortedHistoryRepository;

    loadPastSearchesFromRepository();
  }

//==============================================================================

  @Override
  public List<String> getPastSearchStringsMatching(String partialInput)
  {
    List<String> matchedStrings = new LinkedList<>();

    for (String currentPastSearch : pastSearches)
    {
      boolean match = currentPastSearch.contains(partialInput);
      if (match) matchedStrings.add(currentPastSearch);
    }

    return matchedStrings;
  }

//------------------------------------------------------------------------------

  @Override
  public void registerSearchString(String search)
  {
    loadPastSearchesFromRepository();

    pastSearches.remove(search);
    pastSearches.add(0, search);

    sortedHistoryRepository.save(new ArrayList<>(pastSearches));
  }

//------------------------------------------------------------------------------

  /* Discards the contents of pastSearches, if any, and populates it with the
   * strings in the repository. */

  private void loadPastSearchesFromRepository()
  {
    pastSearches = new LinkedList<>();                                          // a LinkedHashSet is not a better alternative as it keeps insertion order when reinserting
    pastSearches.addAll(sortedHistoryRepository.get());
  }

//------------------------------------------------------------------------------

}
