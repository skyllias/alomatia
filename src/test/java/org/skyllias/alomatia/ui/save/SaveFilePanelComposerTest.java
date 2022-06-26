
package org.skyllias.alomatia.ui.save;

import static org.assertj.swing.fixture.Containers.showInFrame;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.util.concurrent.Callable;

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
import org.skyllias.alomatia.preferences.SavePreferences;
import org.skyllias.alomatia.ui.BarePanelComposer;

/* The destination dir changes cannot be tested because they involve a JFileChooser. */

public class SaveFilePanelComposerTest
{
  private FrameFixture frameFixture;

  @Mock
  private FileImageSaver imageSaver;
  @Mock
  private SavePreferences savePreferences;
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

    when(savePreferences.getDestinationPath()).thenReturn("some/path");
  }

  /* Cannot be called inside setUp() if preferences are to be tuned up. */

  private void setUpUi()
  {
    final SaveFilePanelComposer panelComposer = new SaveFilePanelComposer(new KeyLabelLocalizer(),
                                                                          bareControlPanelComposer,
                                                                          savePreferences);

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
    when(savePreferences.isPromptOn()).thenReturn(true);

    setUpUi();

    verify(imageSaver, atLeastOnce()).setPrompt(true);
    frameFixture.checkBox(SaveFilePanelComposer.PROMPT_CHECKBOX_NAME).requireSelected(true);
  }

  @Test
  public void shouldSetPromptWhenInitiatedAsFalse()
  {
    when(savePreferences.isPromptOn()).thenReturn(false);

    setUpUi();

    verify(imageSaver).setPrompt(false);
    frameFixture.checkBox(SaveFilePanelComposer.PROMPT_CHECKBOX_NAME).requireSelected(false);
  }

  @Test
  public void shouldChangePromptWhenCheckboxChecked()
  {
    when(savePreferences.isPromptOn()).thenReturn(false);

    setUpUi();
    frameFixture.checkBox(SaveFilePanelComposer.PROMPT_CHECKBOX_NAME).check(true);

    verify(imageSaver, atLeastOnce()).setPrompt(true);
  }

  @Test
  public void shouldChangePromptWhenCheckboxUnchecked()
  {
    when(savePreferences.isPromptOn()).thenReturn(true);

    setUpUi();
    frameFixture.checkBox(SaveFilePanelComposer.PROMPT_CHECKBOX_NAME).check(false);

    verify(imageSaver, atLeastOnce()).setPrompt(false);
  }
}
