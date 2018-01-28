
package org.skyllias.alomatia.ui;

import static org.assertj.swing.fixture.Containers.*;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.*;
import java.util.concurrent.*;

import org.assertj.swing.edt.*;
import org.assertj.swing.fixture.*;
import org.junit.*;
import org.mockito.*;
import org.skyllias.alomatia.i18n.*;

public class LanguagePanelTest
{
  private FrameFixture frameFixture;
  @Mock
  private LabelLocalizer labelLocalizer;

  @BeforeClass
  public static void setUpOnce()
  {
    FailOnThreadViolationRepaintManager.install();
  }

  @Before
  public void setUp()
  {
    MockitoAnnotations.initMocks(this);

    when(labelLocalizer.getString(any(String.class))).thenReturn("");           // avoid NPE
  }

  @After
  public void tearDown()
  {
    frameFixture.cleanUp();
  }

  private void setUpFixture()
  {
    LanguagePanel languagePanel = GuiActionRunner.execute(new Callable<LanguagePanel>()
    {
      @Override
      public LanguagePanel call() throws Exception
      {
        return new LanguagePanel(labelLocalizer);
      }
    });
    frameFixture = showInFrame(languagePanel);
  }

//------------------------------------------------------------------------------

  @Test
  public void shouldInitializeComboWithCurrentLocale()
  {
    when(labelLocalizer.getAvailableLocales()).thenReturn(Arrays.asList(new Locale("es"),
                                                                        new Locale("ca"),
                                                                        new Locale("en"),
                                                                        new Locale("fr")));
    when(labelLocalizer.getCurrentLocale()).thenReturn(new Locale("ca"));

    setUpFixture();

    JComboBoxFixture comboBox = frameFixture.comboBox(LanguagePanel.LANG_SELECTOR_NAME);
    comboBox.requireItemCount(4);
    assertTrue("Selected language name should contain Catalan", comboBox.selectedItem().contains("català"));
  }

  @Test
  public void shouldChangeLocaleWhenOptionSelected()
  {
    when(labelLocalizer.getAvailableLocales()).thenReturn(Arrays.asList(new Locale("es"),
                                                                        new Locale("ca"),
                                                                        new Locale("en"),
                                                                        new Locale("fr")));
    when(labelLocalizer.getCurrentLocale()).thenReturn(new Locale("ca"));

    setUpFixture();

    JComboBoxFixture comboBox = frameFixture.comboBox(LanguagePanel.LANG_SELECTOR_NAME);
    comboBox.selectItem(3);                                                     // alphabetically in Catalan, this should be French
    verify(labelLocalizer, times(1)).setLocale(new Locale("fr"));
  }
}
