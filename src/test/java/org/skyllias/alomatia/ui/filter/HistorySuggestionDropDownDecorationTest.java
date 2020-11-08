
package org.skyllias.alomatia.ui.filter;

import static org.assertj.swing.fixture.Containers.showInFrame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.Callable;

import javax.swing.JComponent;
import javax.swing.JTextField;

import org.assertj.swing.edt.FailOnThreadViolationRepaintManager;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.fixture.JTextComponentFixture;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class HistorySuggestionDropDownDecorationTest
{
  @Mock
  private FilterSearchHistory filterSearchHistory;

  private FrameFixture frameFixture;

  @BeforeClass
  public static void setUpOnce()
  {
    FailOnThreadViolationRepaintManager.install();
  }

  @Before
  public void setUp()
  {
    when(filterSearchHistory.getPastSearchStringsMatching(any(String.class)))
      .thenReturn(Collections.<String>emptyList());
    when(filterSearchHistory.getPastSearchStringsMatching("a"))
      .thenReturn(Arrays.asList("abc", "aaaaa", "a1"));
    when(filterSearchHistory.getPastSearchStringsMatching("x"))
      .thenReturn(Arrays.asList("xxx", "xyz"));

    JComponent searchField = GuiActionRunner.execute(new Callable<JComponent>()
    {
      @Override
      public JComponent call() throws Exception
      {
        JTextField inputField = new JTextField();
        new HistorySuggestionDropDownDecoration(inputField, filterSearchHistory);
        return inputField;
      }
    });
    frameFixture = showInFrame(searchField);
  }

  @After
  public void tearDown()
  {
    frameFixture.cleanUp();
  }

//------------------------------------------------------------------------------

  @Test
  public void shoulNotShowAnyPopupWhenNoSuggestions()
  {
    JTextComponentFixture searchField = frameFixture.textBox();
    searchField.enterText("z");
    searchField.pressAndReleaseKeys(KeyEvent.VK_ENTER);

    searchField.requireText("z");
  }

  @Test
  public void shoulShowPopupWithSuggestionsAndSelectFirst()
  {
    JTextComponentFixture searchField = frameFixture.textBox();
    searchField.enterText("x");
    searchField.pressAndReleaseKeys(KeyEvent.VK_ENTER);

    searchField.requireText("xxx");
  }

  @Test
  public void shoulShowPopupWithSuggestionsAndSelectThird()
  {
    JTextComponentFixture searchField = frameFixture.textBox();
    searchField.enterText("a");
    searchField.pressAndReleaseKeys(KeyEvent.VK_DOWN, KeyEvent.VK_DOWN,
                                    KeyEvent.VK_DOWN, KeyEvent.VK_DOWN,
                                    KeyEvent.VK_ENTER);

    searchField.requireText("a1");
  }

  @Test
  public void shoulShowPopupWithSuggestionsAndCancel()
  {
    JTextComponentFixture searchField = frameFixture.textBox();
    searchField.enterText("a");
    searchField.pressAndReleaseKeys(KeyEvent.VK_DOWN, KeyEvent.VK_DOWN,
                                    KeyEvent.VK_DOWN, KeyEvent.VK_DOWN,
                                    KeyEvent.VK_ESCAPE);

    searchField.requireText("a");
  }

}
