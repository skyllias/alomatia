
package org.skyllias.alomatia.ui.source;

import static org.assertj.swing.fixture.Containers.showInFrame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.util.Optional;
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
import org.skyllias.alomatia.source.DirFileSource;
import org.skyllias.alomatia.ui.file.FileChooserAdapter;

/** TODO find a way to test navigation with keys. */

@RunWith(MockitoJUnitRunner.class)
public class DirFileSourceSelectionComposerTest
{
  @Mock
  private DirFileSource dirFileSource;
  @Mock
  private FileChooserAdapter fileChooserAdapter;
  @Spy
  private KeyLabelLocalizer labelLocalizer;

  @InjectMocks
  private DirFileSourceSelectionComposer dirFileSourceSelectionComposer;

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
        return dirFileSourceSelectionComposer.buildSelector();
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
  public void shouldNotCrashIfNoDirSelectedPreviously()
  {
    when(dirFileSource.getCurrentDir()).thenReturn(Optional.empty());
    setUpUi();

    getPathField().requireNotEditable();
    getPathField().requireEmpty();
    getSelectButton().requireDisabled();

    verify(fileChooserAdapter, never()).setSelectedFile(any());
  }

  @Test
  public void shouldUsePathIfDirSelectedPreviously()
  {
    when(dirFileSource.getCurrentDir()).thenReturn(Optional.of(new File("/full/path")));
    setUpUi();

    getPathField().requireNotEditable();
    getPathField().requireText("/full/path");
    getSelectButton().requireDisabled();

    verify(fileChooserAdapter).setSelectedFile(new File("/full/path"));
  }

  @Test
  public void shouldEnableButtonIfSourceActivated()
  {
    when(dirFileSource.getCurrentDir()).thenReturn(Optional.empty());
    SourceSelection sourceSelection = setUpUi();

    GuiActionRunner.execute(() -> sourceSelection.getSource().setActive(true));

    getPathField().requireNotEditable();
    getSelectButton().requireEnabled();
  }

  @Test
  public void shouldDoNothingWhenNoDirSelected()
  {
    when(dirFileSource.getCurrentDir()).thenReturn(Optional.empty());
    when(fileChooserAdapter.showOpenDialog(null)).thenReturn(JFileChooser.CANCEL_OPTION);
    SourceSelection sourceSelection = setUpUi();

    GuiActionRunner.execute(() -> sourceSelection.getSource().setActive(true));
    getSelectButton().click();

    getPathField().requireNotEditable();
    getPathField().requireEmpty();
    getSelectButton().requireEnabled();

    verify(dirFileSource, never()).setFileSource(any());
  }

  @Test
  public void shouldSetFileWhenFileSelected()
  {
    when(dirFileSource.getCurrentDir()).thenReturn(Optional.empty());
    when(fileChooserAdapter.showOpenDialog(null)).thenReturn(JFileChooser.APPROVE_OPTION);
    when(fileChooserAdapter.getSelectedFile()).thenReturn(new File("/full/path"));
    SourceSelection sourceSelection = setUpUi();

    GuiActionRunner.execute(() -> sourceSelection.getSource().setActive(true));
    getSelectButton().click();

    getPathField().requireNotEditable();
    getPathField().requireText("/full/path");
    getSelectButton().requireEnabled();

    verify(dirFileSource).setFileSource(new File("/full/path"));
  }


  private JTextComponentFixture getPathField()
  {
    return frameFixture.textBox(DirFileSourceSelectionComposer.PATH_FIELD_NAME);
  }

  private JButtonFixture getSelectButton()
  {
    return frameFixture.button(DirFileSourceSelectionComposer.BUTTON_NAME);
  }

}
