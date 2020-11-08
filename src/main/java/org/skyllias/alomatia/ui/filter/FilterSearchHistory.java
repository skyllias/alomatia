
package org.skyllias.alomatia.ui.filter;

import java.util.List;

/** Registry of strings used in the past to search filters.
 *  It is expected to take care of all the aspects of the history, from
 *  persistence (so that strings used in previous executions are available) to
 *  sorting criteria (to decide which strings appear first in the results. */

public interface FilterSearchHistory
{
  /** Returns all the strings that have been used in the past that match the
   *  passed partial input.
   *  "match" must not be interpreted as a regular expression, but as "begin
   *  with" or "contain".
   *  All known strings are returned. Some may have been forgotten and therefore
   *  are not returned, but this object does not impose any maximum to the
   *  amount of results. If only a few are to be offered to users while they
   *  type, they have to be capped by the UI.
   *  The order is expected to be respected, with items at the beginning being
   *  more relevant than items at the end. The criteria why items are more
   *  relevant (frequency, recency...) are implementation-dependent. */

  List<String> getPastSearchStringsMatching(String partialInput);

  /** Stores the passed string so that it can be returned in future calls to
   *  {@link #getPastSearchStringsMatching(String)}.
   *  The position in the result can depend on the criteria for relevance,
   *  but in general the following should hold:
   *  <pre>
   *    registerSearchString(search);
   *    assert getPastSearchStringsMatching(search).contains(search);
   *  </pre>
   *  However, if the second call heppens a while after the first, then the
   *  history may have forgotten the value according to its internal criteria.
   *  The search string cannot be empty. */

  void registerSearchString(String search);
}
