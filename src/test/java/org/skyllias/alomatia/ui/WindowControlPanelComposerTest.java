
package org.skyllias.alomatia.ui;

import static org.assertj.swing.fixture.Containers.showInFrame;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.awt.dnd.DropTargetListener;
import java.util.concurrent.Callable;
import java.util.prefs.Preferences;

import javax.swing.JComponent;

import org.assertj.swing.edt.FailOnThreadViolationRepaintManager;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.edt.GuiTask;
import org.assertj.swing.fixture.FrameFixture;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.skyllias.alomatia.display.Repeater;
import org.skyllias.alomatia.i18n.StartupLabelLocalizer;
import org.skyllias.alomatia.ui.DisplayFrameManager.DisplayAmountChangeListener;
import org.skyllias.alomatia.ui.frame.FramePolicy;

public class WindowControlPanelComposerTest
{
  private FrameFixture frameFixture;
  @Mock
  private Repeater repeater;
  @Mock
  private DropTargetListener dropTargetListener;
  @Mock
  private DisplayPanelController displayPanel;
  @Mock
  private DisplayFrameController displayFrame;
  @Mock
  private DisplayFrameManager displayFrameManager;
  @Mock
  private FramePolicy framePolicy;
  @Mock
  private Preferences preferences;
  @Captor
  private ArgumentCaptor<DisplayAmountChangeListener> listenerCaptor;

  private WindowControlPanelComposer windowControlPanelComposer;

  @BeforeClass
  public static void setUpOnce()
  {
    FailOnThreadViolationRepaintManager.install();
  }

  @Before
  public void setUp()
  {
    MockitoAnnotations.initMocks(this);

    when(displayFrame.getDisplayPanel()).thenReturn(displayPanel);
    when(displayFrameManager.getNewDisplayFrame(any(Boolean.class))).thenReturn(displayFrame);
  }

  /* Cannot be called inside setUp() if preferences are to be tuned up. */

  private void setUpUi()
  {
    windowControlPanelComposer = new WindowControlPanelComposer(new StartupLabelLocalizer(), repeater,
                                                                dropTargetListener, displayFrameManager, framePolicy); // KeyLabelLocalizer cannot be used because it does not provide any TextMessage pattern
    windowControlPanelComposer.setPreferences(preferences);

    JComponent controlPanel = GuiActionRunner.execute(new Callable<JComponent>()
    {
      @Override
      public JComponent call() throws Exception
      {
        return windowControlPanelComposer.getComponent();
      }
    });
    frameFixture = showInFrame(controlPanel);
  }

  @After
  public void tearDown()
  {
    frameFixture.cleanUp();
  }

//------------------------------------------------------------------------------

  @Test
  public void shouldNotAddDisplayPanelWhenStartingWithoutAutoopen()
  {
    when(preferences.getBoolean(eq(WindowControlPanelComposer.PREFKEY_AUTOOPEN),
                                any(Boolean.class))).thenReturn(false);
    setUpUi();

    windowControlPanelComposer.openNewWindowIfRequired();
    verify(repeater, never()).addReceiver(displayPanel);
  }

  @Test
  public void shouldAddDisplayPanelWhenStartingWithAutoopen()
  {
    when(preferences.getBoolean(eq(WindowControlPanelComposer.PREFKEY_AUTOOPEN),
                                any(Boolean.class))).thenReturn(true);
    setUpUi();

    windowControlPanelComposer.openNewWindowIfRequired();
    verify(repeater).addReceiver(displayPanel);
  }

  @Test
  public void shouldOpenNewWindowWithFilterWhenButtonClickedWithCheckbox()
  {
    when(preferences.getBoolean(eq(WindowControlPanelComposer.PREFKEY_APPLYFILTER),
                                any(Boolean.class))).thenReturn(true);
    setUpUi();

    frameFixture.checkBox(WindowControlPanelComposer.AUTOAPPLY_FILTER_NAME).check(true);
    frameFixture.button(WindowControlPanelComposer.ADD_BUTTON_NAME).click();

    verify(displayFrameManager, times(1)).getNewDisplayFrame(true);
    verify(displayFrameManager, times(0)).getNewDisplayFrame(false);
  }

  @Test
  public void shouldOpenNewWindowWithoutFilterWhenButtonClickedWithoutCheckbox()
  {
    when(preferences.getBoolean(eq(WindowControlPanelComposer.PREFKEY_APPLYFILTER),
                                any(Boolean.class))).thenReturn(false);
    setUpUi();

    frameFixture.checkBox(WindowControlPanelComposer.AUTOAPPLY_FILTER_NAME).check(false);
    frameFixture.button(WindowControlPanelComposer.ADD_BUTTON_NAME).click();

    verify(displayFrameManager, times(0)).getNewDisplayFrame(true);
    verify(displayFrameManager, times(1)).getNewDisplayFrame(false);
  }

  @Test
  public void shouldChangeLabelWhenAmountChanged()
  {
    setUpUi();

    String originalText = frameFixture.label(WindowControlPanelComposer.AMOUNT_LABEL_NAME).text();

    verify(displayFrameManager).addAmountChangeListener(listenerCaptor.capture());

    when(displayFrameManager.getAmountOfOpenDisplayFrames()).thenReturn(6666);
    GuiActionRunner.execute(new GuiTask()
    {
      @Override
      protected void executeInEDT() {listenerCaptor.getValue().onAmountChanged(displayFrameManager);}
    });
    String modifiedText = frameFixture.label(WindowControlPanelComposer.AMOUNT_LABEL_NAME).text();

    assertNotEquals("Label should change when amount of windows changed",
                    originalText, modifiedText);
  }

  @Test
  public void shouldUpdatePolicyWhenCheckboxChecked()
  {
    when(preferences.getBoolean(eq(WindowControlPanelComposer.INTERNALFRAMES_CHECKBOX_NAME),
                                any(Boolean.class))).thenReturn(false);
    setUpUi();

    frameFixture.checkBox(WindowControlPanelComposer.INTERNALFRAMES_CHECKBOX_NAME).check(true);

    verify(framePolicy, atLeastOnce()).setUsingInternalFramesNextTime(true);    // checkboxes also change state when hovered
  }

  @Test
  public void shouldRearrangeWindowsHorizontallyWhenColumnsSelected()
  {
    setUpUi();

    frameFixture.spinner(WindowControlPanelComposer.LINES_SPINNER_NAME).enterText("17");
    frameFixture.comboBox(WindowControlPanelComposer.COMBO_HORIZONTAL_NAME).selectItem(0);  // the API does not support setting a value, so it has to be based on the order of the options
    frameFixture.button(WindowControlPanelComposer.ARRANGE_BUTTON_NAME).click();
    verify(displayFrameManager).rearrangeWindows(eq(17), eq(true));
  }

  @Test
  public void shouldRearrangeWindowsVerticallyWhenRowsSelected()
  {
    setUpUi();

    frameFixture.spinner(WindowControlPanelComposer.LINES_SPINNER_NAME).enterText("19");
    frameFixture.comboBox(WindowControlPanelComposer.COMBO_HORIZONTAL_NAME).selectItem(1);  // the API does not support setting a value, so it has to be based on the order of the options
    frameFixture.button(WindowControlPanelComposer.ARRANGE_BUTTON_NAME).click();
    verify(displayFrameManager).rearrangeWindows(eq(19), eq(false));
  }

  @Test
  public void shouldReapplyFiltersWhenButtonClicked()
  {
    setUpUi();

    frameFixture.button(WindowControlPanelComposer.REFILTER_BUTTON_NAME).click();
    verify(displayFrameManager).applySequentialFilters();
  }
}
