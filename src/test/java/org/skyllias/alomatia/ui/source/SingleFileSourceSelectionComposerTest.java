
package org.skyllias.alomatia.ui.source;

import static org.assertj.swing.fixture.Containers.showInFrame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.util.concurrent.Callable;

import javax.swing.JFileChooser;

import org.assertj.swing.edt.FailOnThreadViolationRepaintManager;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.fixture.JButtonFixture;
import org.assertj.swing.fixture.JTextComponentFixture;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.skyllias.alomatia.i18n.KeyLabelLocalizer;
import org.skyllias.alomatia.preferences.SourceSingleFilePreferences;
import org.skyllias.alomatia.source.SingleFileSource;
import org.skyllias.alomatia.ui.file.FileChooserAdapter;

@RunWith(MockitoJUnitRunner.class)
public class SingleFileSourceSelectionComposerTest
{
  @Mock
  private SingleFileSource singleFileSource;
  @Mock
  private FileChooserAdapter fileChooserAdapter;
  @Mock
  private SourceSingleFilePreferences sourcePreferences;
  @Spy
  private KeyLabelLocalizer labelLocalizer;

  @InjectMocks
  private SingleFileSourceSelectionComposer singleFileSourceSelectionComposer;

  private FrameFixture frameFixture;

  @BeforeClass
  public static void setUpOnce()
  {
    FailOnThreadViolationRepaintManager.install();
  }

  private SourceSelection setUpUi()
  {
    SourceSelection sourceSelection = GuiActionRunner.execute(new Callable<SourceSelection>()
    {
      @Override
      public SourceSelection call() throws Exception
      {
        return singleFileSourceSelectionComposer.buildSelector();
      }
    });
    frameFixture = showInFrame(sourceSelection.getControls());

    return sourceSelection;
  }

  @After
  public void tearDown()
  {
    frameFixture.cleanUp();
  }

//------------------------------------------------------------------------------

  @Test
  public void shouldNotCrashIfNoFileSelectedPreviously()
  {
    when(sourcePreferences.getDefaultFilePath()).thenReturn(null);
    setUpUi();

    getPathField().requireNotEditable();
    getPathField().requireEmpty();
    getSelectButton().requireDisabled();

    verify(fileChooserAdapter, never()).setSelectedFile(any());
    verify(singleFileSource, never()).setFileSource(any());
  }

  @Test
  public void shouldUsePathIfFileSelectedPreviously()
  {
    when(sourcePreferences.getDefaultFilePath()).thenReturn("/full/path/file.png");
    setUpUi();

    getPathField().requireNotEditable();
    getPathField().requireText("/full/path/file.png");
    getSelectButton().requireDisabled();

    verify(fileChooserAdapter).setSelectedFile(new File("/full/path/file.png"));
    verify(singleFileSource).setFileSource(new File("/full/path/file.png"));
  }

  @Test
  public void shouldEnableButtonIfSourceActivated()
  {
    when(sourcePreferences.getDefaultFilePath()).thenReturn(null);
    SourceSelection sourceSelection = setUpUi();

    GuiActionRunner.execute(() -> sourceSelection.getSource().setActive(true));

    getPathField().requireNotEditable();
    getSelectButton().requireEnabled();
  }

  @Test
  public void shouldDoNothingWhenNoFileSelected()
  {
    when(sourcePreferences.getDefaultFilePath()).thenReturn(null);
    when(fileChooserAdapter.showOpenDialog(null)).thenReturn(JFileChooser.CANCEL_OPTION);
    SourceSelection sourceSelection = setUpUi();

    GuiActionRunner.execute(() -> sourceSelection.getSource().setActive(true));
    getSelectButton().click();

    getPathField().requireNotEditable();
    getPathField().requireEmpty();
    getSelectButton().requireEnabled();

    verify(singleFileSource, never()).setFileSource(any());
    verify(sourcePreferences, never()).setDefaultFilePath(any());
  }

  @Test
  public void shouldSetFileWhenFileSelected()
  {
    when(sourcePreferences.getDefaultFilePath()).thenReturn(null);
    when(fileChooserAdapter.showOpenDialog(null)).thenReturn(JFileChooser.APPROVE_OPTION);
    when(fileChooserAdapter.getSelectedFile()).thenReturn(new File("/full/path/file.png"));
    SourceSelection sourceSelection = setUpUi();

    GuiActionRunner.execute(() -> sourceSelection.getSource().setActive(true));
    getSelectButton().click();

    getPathField().requireNotEditable();
    getPathField().requireText("/full/path/file.png");
    getSelectButton().requireEnabled();

    verify(singleFileSource).setFileSource(new File("/full/path/file.png"));
    verify(sourcePreferences).setDefaultFilePath("/full/path/file.png");
  }


  private JTextComponentFixture getPathField()
  {
    return frameFixture.textBox(SingleFileSourceSelectionComposer.PATH_FIELD_NAME);
  }

  private JButtonFixture getSelectButton()
  {
    return frameFixture.button(SingleFileSourceSelectionComposer.BUTTON_NAME);
  }

}
