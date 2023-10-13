
package org.skyllias.alomatia.ui;

import static org.assertj.swing.fixture.Containers.showInFrame;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.skyllias.alomatia.ui.LanguagePanelComposer.LANG_SELECTOR_NAME;

import java.util.Arrays;
import java.util.Locale;
import java.util.concurrent.Callable;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.assertj.swing.edt.FailOnThreadViolationRepaintManager;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.fixture.JComboBoxFixture;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.skyllias.alomatia.i18n.LabelLocalizer;
import org.skyllias.alomatia.ui.component.BarePanelComposer;

public class LanguagePanelComposerTest
{
  private FrameFixture frameFixture;
  @Mock
  private LabelLocalizer labelLocalizer;
  @Mock
  private BarePanelComposer bareControlPanelComposer;

  @BeforeClass
  public static void setUpOnce()
  {
    FailOnThreadViolationRepaintManager.install();
  }

  @Before
  public void setUp()
  {
    MockitoAnnotations.initMocks(this);

    when(labelLocalizer.getString(any(String.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));
  }

  @After
  public void tearDown()
  {
    frameFixture.cleanUp();
  }

  private void setUpFixture()
  {
    LanguagePanelComposer composer = new LanguagePanelComposer(labelLocalizer,
                                                               bareControlPanelComposer);

    JComponent languagePanel = GuiActionRunner.execute(new Callable<JComponent>()
    {
      @Override
      public JComponent call() throws Exception
      {
        when(bareControlPanelComposer.getPanel("language.selector.title"))
            .thenReturn(new JPanel());

        return composer.getComponent();
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

    JComboBoxFixture comboBox = frameFixture.comboBox(LANG_SELECTOR_NAME);
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

    JComboBoxFixture comboBox = frameFixture.comboBox(LANG_SELECTOR_NAME);
    comboBox.selectItem(3);                                                     // alphabetically in Catalan, this should be French
    verify(labelLocalizer, times(1)).setLocale(new Locale("fr"));
  }
}
