
package org.skyllias.alomatia.ui.filter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.springframework.stereotype.Component;

/** Naive implementatation of {@link FilterSearchHistory} that keeps track of
 *  all searched strings and returns them based on the inverse order with which
 *  they were last used. Frequency or age are not taken into account. */

@Component
public class LruFilterSearchHistory implements FilterSearchHistory
{
  private final SortedHistoryRepository sortedHistoryRepository;

  private final List<String> pastSearches = new LinkedList<>();                 // maintained in memory for searches. A LinkedHashSet is not a better alternative as it keeps insertion order when reinserting

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
    pastSearches.clear();
    pastSearches.addAll(sortedHistoryRepository.get());
  }

//------------------------------------------------------------------------------

}
