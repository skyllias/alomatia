
package org.skyllias.alomatia.ui.save;

import static org.assertj.swing.fixture.Containers.showInFrame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.util.concurrent.Callable;
import java.util.prefs.Preferences;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.assertj.swing.edt.FailOnThreadViolationRepaintManager;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.skyllias.alomatia.i18n.KeyLabelLocalizer;
import org.skyllias.alomatia.ui.BarePanelComposer;

/* The destination dir changes cannot be tested because they involve a JFileChooser. */

public class SaveFilePanelComposerTest
{
  private FrameFixture frameFixture;

  @Mock
  private FileImageSaver imageSaver;
  @Mock
  private Preferences preferences;
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

    when(preferences.get(eq(SaveFilePanelComposer.PREFKEY_DESTINATION),
                         any(String.class))).thenReturn("some/path");           // this is always required for the method not to throw a NPE
  }

  /* Cannot be called inside setUp() if preferences are to be tuned up. */

  private void setUpUi()
  {
    final SaveFilePanelComposer panelComposer = new SaveFilePanelComposer(new KeyLabelLocalizer(),
                                                                          bareControlPanelComposer);
    panelComposer.setPreferences(preferences);

    JComponent savePanel = GuiActionRunner.execute(new Callable<JComponent>()
    {
      @Override
      public JComponent call() throws Exception
      {
        when(bareControlPanelComposer.getPanel("save.control.title"))
            .thenReturn(new JPanel());

        return panelComposer.getComponent(imageSaver);
      }
    });
    frameFixture = showInFrame(savePanel);
  }

  @After
  public void tearDown()
  {
    frameFixture.cleanUp();
  }

//------------------------------------------------------------------------------

  @Test
  public void shouldSetInitialDestinationWhenInitiated()
  {
    setUpUi();

    verify(imageSaver).setDestinationDir(new File("some/path"));
  }

  @Test
  public void shouldSetPromptWhenInitiatedAsTrue()
  {
    doReturn(true).when(preferences).getBoolean(eq(SaveFilePanelComposer.PREFKEY_PROMPT),
                                                any(Boolean.class));

    setUpUi();

    verify(imageSaver, atLeastOnce()).setPrompt(true);
    frameFixture.checkBox(SaveFilePanelComposer.PROMPT_CHECKBOX_NAME).requireSelected(true);
  }

  @Test
  public void shouldSetPromptWhenInitiatedAsFalse()
  {
    doReturn(false).when(preferences).getBoolean(eq(SaveFilePanelComposer.PREFKEY_PROMPT),
                                                 any(Boolean.class));

    setUpUi();

    verify(imageSaver).setPrompt(false);
    frameFixture.checkBox(SaveFilePanelComposer.PROMPT_CHECKBOX_NAME).requireSelected(false);
  }

  @Test
  public void shouldChangePromptWhenCheckboxChecked()
  {
    doReturn(false).when(preferences).getBoolean(eq(SaveFilePanelComposer.PREFKEY_PROMPT),
                                                 any(Boolean.class));

    setUpUi();
    frameFixture.checkBox(SaveFilePanelComposer.PROMPT_CHECKBOX_NAME).check(true);

    verify(imageSaver, atLeastOnce()).setPrompt(true);
  }

  @Test
  public void shouldChangePromptWhenCheckboxUnchecked()
  {
    doReturn(true).when(preferences).getBoolean(eq(SaveFilePanelComposer.PREFKEY_PROMPT),
                                                any(Boolean.class));

    setUpUi();
    frameFixture.checkBox(SaveFilePanelComposer.PROMPT_CHECKBOX_NAME).check(false);

    verify(imageSaver, atLeastOnce()).setPrompt(false);
  }
}
