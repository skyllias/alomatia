package org.skyllias.alomatia.ui.filter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.prefs.Preferences;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SortedHistoryRepositoryTest
{
  @Mock
  private Preferences preferences;

  @InjectMocks
  private SortedHistoryRepository sortedHistoryRepository;

  @Before
  public void setUp()
  {
    when(preferences.get("sortedSearchHistory", "")).thenReturn("");
  }

//------------------------------------------------------------------------------

  @Test
  public void shouldGetEmptyListByDefault()
  {
    assertTrue(sortedHistoryRepository.get().isEmpty());
  }

  @Test
  public void shouldParseNonEmptyList()
  {
    when(preferences.get("sortedSearchHistory", "")).thenReturn("123///a.a.a///xyz");

    assertEquals(Arrays.asList("123", "a.a.a", "xyz"), sortedHistoryRepository.get());
  }

  @Test
  public void shouldStoreNonEmptyList()
  {
    sortedHistoryRepository.save(Arrays.asList("123", "a.a.a", "xyz"));

    verify(preferences).put("sortedSearchHistory", "123///a.a.a///xyz");
  }

}
