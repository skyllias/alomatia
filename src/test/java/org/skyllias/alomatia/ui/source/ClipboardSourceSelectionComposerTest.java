package org.skyllias.alomatia.ui.source;

import static org.assertj.swing.fixture.Containers.showInFrame;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.concurrent.Callable;

import org.assertj.swing.edt.FailOnThreadViolationRepaintManager;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.fixture.JCheckBoxFixture;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.skyllias.alomatia.i18n.KeyLabelLocalizer;
import org.skyllias.alomatia.preferences.SourcePreferences;
import org.skyllias.alomatia.source.ClipboardSource;

@RunWith(MockitoJUnitRunner.class)
public class ClipboardSourceSelectionComposerTest
{
  @Mock
  private ClipboardSource clipboardSource;
  @Mock
  private SourcePreferences sourcePreferences;
  @Spy
  private KeyLabelLocalizer labelLocalizer;

  @InjectMocks
  private ClipboardSourceSelectionComposer clipboardSourceSelectionComposer;

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
        return clipboardSourceSelectionComposer.buildSelector();
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
  public void shouldStartDisabled()
  {
    when(sourcePreferences.isClipboardAutoMode()).thenReturn(false);
    setUpUi();

    getAutoModeCheckbox().requireDisabled();
    getAutoModeCheckbox().requireNotSelected();
  }

  @Test
  public void shouldStartCheckedAccordingToSource()
  {
    when(sourcePreferences.isClipboardAutoMode()).thenReturn(true);
    setUpUi();

    getAutoModeCheckbox().requireDisabled();
    getAutoModeCheckbox().requireSelected();
  }

  @Test
  public void shouldEnableCheckboxWhenSourceActivated()
  {
    SourceSelection sourceSelection = setUpUi();
    GuiActionRunner.execute(() -> sourceSelection.getSource().setActive(true));

    getAutoModeCheckbox().requireEnabled();
    verify(clipboardSource).setActive(true);
  }

  @Test
  public void shouldDisableCheckboxWhenSourceDeactivated()
  {
    SourceSelection sourceSelection = setUpUi();
    GuiActionRunner.execute(() -> sourceSelection.getSource().setActive(true));
    GuiActionRunner.execute(() -> sourceSelection.getSource().setActive(false));

    getAutoModeCheckbox().requireDisabled();
    verify(clipboardSource).setActive(false);
  }

  @Test
  public void shouldSetAutoModeWhenCheckboxSelected()
  {
    SourceSelection sourceSelection = setUpUi();
    GuiActionRunner.execute(() -> sourceSelection.getSource().setActive(true));

    getAutoModeCheckbox().check();

    verify(clipboardSource, atLeastOnce()).setAutoMode(true);
    verify(sourcePreferences, atLeastOnce()).setClipboardAutoMode(true);
  }

  @Test
  public void shouldSetAutoModeWhenCheckboxDeselected()
  {
    SourceSelection sourceSelection = setUpUi();
    GuiActionRunner.execute(() -> sourceSelection.getSource().setActive(true));

    getAutoModeCheckbox().uncheck();

    verify(clipboardSource, atLeastOnce()).setAutoMode(false);
    verify(sourcePreferences, atLeastOnce()).setClipboardAutoMode(false);
  }


  private JCheckBoxFixture getAutoModeCheckbox()
  {
    return frameFixture.checkBox(ClipboardSourceSelectionComposer.AUTOMODE_CHECKBOX_NAME);
  }

}
