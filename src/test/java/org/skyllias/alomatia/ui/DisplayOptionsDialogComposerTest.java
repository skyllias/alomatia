package org.skyllias.alomatia.ui;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.awt.Frame;
import java.awt.event.KeyEvent;
import java.util.concurrent.Callable;

import javax.swing.JDialog;
import javax.swing.JPanel;

import org.assertj.swing.core.KeyPressInfo;
import org.assertj.swing.edt.FailOnThreadViolationRepaintManager;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.DialogFixture;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.skyllias.alomatia.i18n.KeyLabelLocalizer;
import org.skyllias.alomatia.preferences.DisplayOptionsPreferences;
import org.skyllias.alomatia.ui.filter.FilterSelector;

@RunWith(MockitoJUnitRunner.class)
public class DisplayOptionsDialogComposerTest
{
  @Spy
  private KeyLabelLocalizer labelLocalizer;

  @Mock
  private ZoomSelectorComposer zoomSelectorComposer;

  @Mock
  private DisplayOptionsPreferences displayOptionsPreferences;

  @InjectMocks
  private DisplayOptionsDialogComposer displayOptionsDialogComposer;

  private DialogFixture dialogFixture;


  @BeforeClass
  public static void setUpOnce()
  {
    FailOnThreadViolationRepaintManager.install();
  }

  private JDialog setUpUi()
  {
    DisplayFrameController ownerDisplayFrame      = mock(DisplayFrameController.class);
    FilterSelector filterSelector                 = mock(FilterSelector.class);
    DisplayPanelController displayPanelController = mock(DisplayPanelController.class);

    JDialog optionsDialog = GuiActionRunner.execute(new Callable<JDialog>()
    {
      @Override
      public JDialog call() throws Exception
      {
        when(ownerDisplayFrame.getOwnerFrame()).thenReturn(new Frame());
        when(ownerDisplayFrame.getDisplayPanel()).thenReturn(displayPanelController);
        when(zoomSelectorComposer.getComponent(displayPanelController)).thenReturn(new JPanel());
        when(filterSelector.getComponent()).thenReturn(new JPanel());

        return displayOptionsDialogComposer.getDialog(ownerDisplayFrame, filterSelector);
      }
    });
    dialogFixture = new DialogFixture(optionsDialog);

    return optionsDialog;
  }

  @After
  public void tearDown()
  {
    dialogFixture.cleanUp();
  }

//------------------------------------------------------------------------------

  @Test
  public void shouldInitializeCheckboxWhenShowImmediatelyIsOn()
  {
    when(displayOptionsPreferences.isDialogShownImmediately()).thenReturn(true);

    JDialog dialog = setUpUi();

    assertTrue(dialog.isVisible());
    dialogFixture.dialog().checkBox().requireSelected();
  }

  @Test
  public void shouldNotShowWhenShowImmediatelyIsOff()
  {
    when(displayOptionsPreferences.isDialogShownImmediately()).thenReturn(false);

    JDialog dialog = setUpUi();

    assertFalse(dialog.isVisible());

    dialog.setVisible(true);
    dialogFixture.dialog().checkBox().requireNotSelected();
  }

  @Test
  @Ignore("For some reason, incmopatible with other tests")
  public void shouldCloseWhenEscapeIsPressed()
  {
    JDialog dialog = setUpUi();

    dialog.setVisible(true);
    dialogFixture.dialog().pressAndReleaseKey(KeyPressInfo.keyCode(KeyEvent.VK_ESCAPE));
    assertFalse(dialog.isVisible());
  }

  @Test
  public void shouldCloseWhenButtonIsClicked()
  {
    JDialog dialog = setUpUi();

    dialog.setVisible(true);
    dialogFixture.dialog().button(DisplayOptionsDialogComposer.CLOSE_LABEL).click();
    assertFalse(dialog.isVisible());
  }

  @Test
  public void shouldSavePreferenceWhenOn()
  {
    JDialog dialog = setUpUi();

    dialog.setVisible(true);
    dialogFixture.dialog().checkBox().check();
    verify(displayOptionsPreferences, atLeastOnce()).setDialogShownImmediately(true);
  }

  @Test
  public void shouldSavePreferenceWhenOff()
  {
    when(displayOptionsPreferences.isDialogShownImmediately()).thenReturn(true);

    setUpUi();

    dialogFixture.dialog().checkBox().check(false);
    verify(displayOptionsPreferences, atLeastOnce()).setDialogShownImmediately(false);
  }


}
