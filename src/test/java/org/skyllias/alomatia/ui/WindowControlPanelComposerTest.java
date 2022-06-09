
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

import java.util.concurrent.Callable;

import javax.swing.JComponent;
import javax.swing.JPanel;

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
import org.skyllias.alomatia.i18n.LabelLocalizer;
import org.skyllias.alomatia.preferences.FramePolicyPreferences;
import org.skyllias.alomatia.preferences.WindowControlPreferences;
import org.skyllias.alomatia.ui.DisplayFrameManager.DisplayAmountChangeListener;

public class WindowControlPanelComposerTest
{
  private FrameFixture frameFixture;
  @Mock
  private LabelLocalizer labelLocalizer;
  @Mock
  private Repeater repeater;
  @Mock
  private DropTargetListenerSupplier dropTargetListenerSupplier;
  @Mock
  private DisplayPanelController displayPanel;
  @Mock
  private DisplayFrameController displayFrame;
  @Mock
  private DisplayFrameManager displayFrameManager;
  @Mock
  private FramePolicyPreferences framePolicyPreferences;
  @Mock
  private BarePanelComposer bareControlPanelComposer;
  @Mock
  private WindowControlPreferences windowControlPreferences;
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
    when(displayFrameManager.createDisplayFrame(any(Boolean.class))).thenReturn(displayFrame);

    when(labelLocalizer.getString(any())).thenAnswer(invocation ->              // KeyLabelLocalizer cannot be used because it does not provide any TextMessage pattern and does not allow to test changes in the label with the amount of windows
    {
      String key = invocation.getArgument(0, String.class);
      if ("frame.control.amount".equals(key)) return "{0}";
      else                                    return key;
    });
  }

  /* Cannot be called inside setUp() if preferences are to be tuned up. */

  private void setUpUi()
  {
    windowControlPanelComposer = new WindowControlPanelComposer(labelLocalizer, repeater,
                                                                dropTargetListenerSupplier, displayFrameManager,
                                                                framePolicyPreferences, bareControlPanelComposer,
                                                                windowControlPreferences);

    JComponent controlPanel = GuiActionRunner.execute(new Callable<JComponent>()
    {
      @Override
      public JComponent call() throws Exception
      {
        when(bareControlPanelComposer.getPanel("frame.control.title"))
            .thenReturn(new JPanel());

        return windowControlPanelComposer.createComponent();
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
    when(windowControlPreferences.isAutoOpenWindowOnStartup()).thenReturn(false);
    setUpUi();

    windowControlPanelComposer.openNewWindowIfRequired();
    verify(repeater, never()).addReceiver(displayPanel);
  }

  @Test
  public void shouldAddDisplayPanelWhenStartingWithAutoopen()
  {
    when(windowControlPreferences.isAutoOpenWindowOnStartup()).thenReturn(true);
    setUpUi();

    windowControlPanelComposer.openNewWindowIfRequired();
    verify(repeater).addReceiver(displayPanel);
  }

  @Test
  public void shouldOpenNewWindowWithFilterWhenButtonClickedWithCheckbox()
  {
    when(windowControlPreferences.isSequentialFilterApplied()).thenReturn(true);
    setUpUi();

    frameFixture.checkBox(WindowControlPanelComposer.AUTOAPPLY_FILTER_NAME).check(true);
    frameFixture.button(WindowControlPanelComposer.ADD_BUTTON_NAME).click();

    verify(displayFrameManager, times(1)).createDisplayFrame(true);
    verify(displayFrameManager, times(0)).createDisplayFrame(false);
  }

  @Test
  public void shouldOpenNewWindowWithoutFilterWhenButtonClickedWithoutCheckbox()
  {
    when(windowControlPreferences.isSequentialFilterApplied()).thenReturn(false);
    setUpUi();

    frameFixture.checkBox(WindowControlPanelComposer.AUTOAPPLY_FILTER_NAME).check(false);
    frameFixture.button(WindowControlPanelComposer.ADD_BUTTON_NAME).click();

    verify(displayFrameManager, times(0)).createDisplayFrame(true);
    verify(displayFrameManager, times(1)).createDisplayFrame(false);
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
    setUpUi();

    frameFixture.checkBox(WindowControlPanelComposer.INTERNALFRAMES_CHECKBOX_NAME).check(true);

    verify(framePolicyPreferences, atLeastOnce()).setUsingInternalFramesNextTime(true);    // checkboxes also change state when hovered
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
