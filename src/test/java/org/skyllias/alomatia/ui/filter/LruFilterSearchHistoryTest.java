
package org.skyllias.alomatia.ui.filter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class LruFilterSearchHistoryTest
{
  @Mock
  private SortedHistoryRepository sortedHistoryRepository;

  @Before
  public void setUp()
  {
    when(sortedHistoryRepository.get()).thenReturn(Collections.<String>emptyList());
  }

//------------------------------------------------------------------------------

  @Test
  public void shouldReturnStringJustRegistered()
  {
    LruFilterSearchHistory history = new LruFilterSearchHistory(sortedHistoryRepository);

    history.registerSearchString("abc");

    assertTrue(history.getPastSearchStringsMatching("abc").contains("abc"));
  }

  @Test
  public void shouldReturnPartialStringJustRegistered()
  {
    LruFilterSearchHistory history = new LruFilterSearchHistory(sortedHistoryRepository);

    history.registerSearchString("abc");

    assertTrue(history.getPastSearchStringsMatching("b").contains("abc"));
  }

  @Test
  public void shouldAvoidDuplicates()
  {
    when(sortedHistoryRepository.get()).thenReturn(Arrays.asList("abc"));
    LruFilterSearchHistory history = new LruFilterSearchHistory(sortedHistoryRepository);

    history.registerSearchString("abc");

    assertEquals(1, history.getPastSearchStringsMatching("abc").size());
  }

  @Test
  public void shouldReturnLastRegisteredFirst()
  {
    when(sortedHistoryRepository.get()).thenReturn(Arrays.asList("abc2", "abc1"));
    LruFilterSearchHistory history = new LruFilterSearchHistory(sortedHistoryRepository);

    history.registerSearchString("abc3");

    assertEquals(Arrays.asList("abc3", "abc2", "abc1"),
                 history.getPastSearchStringsMatching("abc"));
  }

  @Test
  public void shouldAvoidDuplicatesWithMultipleResults()
  {
    when(sortedHistoryRepository.get()).thenReturn(Arrays.asList("abc2", "abc1"));
    LruFilterSearchHistory history = new LruFilterSearchHistory(sortedHistoryRepository);

    history.registerSearchString("abc2");

    assertEquals(Arrays.asList("abc2", "abc1"),
                 history.getPastSearchStringsMatching("abc"));
  }

  @Test
  public void shouldDiscardAllNonMatchingResults()
  {
    when(sortedHistoryRepository.get()).thenReturn(Arrays.asList("abc1", "abc2", "abc3", "abc4"));
    LruFilterSearchHistory history = new LruFilterSearchHistory(sortedHistoryRepository);

    assertTrue(history.getPastSearchStringsMatching("xyz").isEmpty());
  }

  @Test
  public void shouldSortReinsertionsCorrectly()
  {
    when(sortedHistoryRepository.get()).thenReturn(Arrays.asList("abc3", "abc2", "abc1"));
    LruFilterSearchHistory history = new LruFilterSearchHistory(sortedHistoryRepository);

    history.registerSearchString("abc2");

    assertEquals(Arrays.asList("abc2", "abc3", "abc1"),
                 history.getPastSearchStringsMatching("abc"));
  }

  @Test
  public void shouldRetrieveHistoryFromRepositoryBeforeRegistering()
  {
    when(sortedHistoryRepository.get()).thenReturn(Arrays.asList("xyz1", "xyz2", "xyz3"));
    LruFilterSearchHistory history = new LruFilterSearchHistory(sortedHistoryRepository);
    when(sortedHistoryRepository.get()).thenReturn(Arrays.asList("abc2", "abc3"));

    history.registerSearchString("abc1");

    assertEquals(Arrays.asList("abc1", "abc2", "abc3"),
                 history.getPastSearchStringsMatching("abc"));
  }

  @Test
  public void shouldSaveToRepository()
  {
    when(sortedHistoryRepository.get()).thenReturn(Arrays.asList("abc2", "abc1"));
    LruFilterSearchHistory history = new LruFilterSearchHistory(sortedHistoryRepository);
    history.registerSearchString("abc3");

    verify(sortedHistoryRepository).save(Arrays.asList("abc3", "abc2", "abc1"));
  }

}
