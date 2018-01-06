
package org.skyllias.alomatia.ui;

import static org.assertj.swing.fixture.Containers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.io.*;
import java.util.concurrent.*;
import java.util.prefs.*;

import javax.swing.*;

import org.assertj.swing.edt.*;
import org.assertj.swing.fixture.*;
import org.junit.*;
import org.mockito.*;
import org.skyllias.alomatia.i18n.*;

/* The destination dir changes cannot be tested because they involve a JFileChooser. */

public class SaveFilePanelComposerTest
{
  private FrameFixture frameFixture;

  @Mock
  private FileImageSaver imageSaver;
  @Mock
  private Preferences preferences;

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
    final SaveFilePanelComposer panelComposer = new SaveFilePanelComposer(new KeyLabelLocalizer(), imageSaver);
    panelComposer.setPreferences(preferences);

    JComponent savePanel = GuiActionRunner.execute(new Callable<JComponent>()
    {
      @Override
      public JComponent call() throws Exception
      {
        return panelComposer.getComponent();
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
