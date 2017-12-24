
package org.skyllias.alomatia.ui;

import static org.assertj.swing.fixture.Containers.*;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.awt.dnd.*;
import java.util.concurrent.*;
import java.util.prefs.*;

import org.assertj.swing.edt.*;
import org.assertj.swing.fixture.*;
import org.junit.*;
import org.mockito.*;
import org.skyllias.alomatia.display.*;
import org.skyllias.alomatia.i18n.*;

public class WindowControlPanelTest
{
  private FrameFixture frameFixture;
  @Mock
  private Repeater repeater;
  @Mock
  private DropTargetListener dropTargetListener;
  @Mock
  private DisplayPanel displayPanel;
  @Mock
  private DisplayFrame displayFrame;
  @Mock
  private DisplayFrameManager displayFrameManager;
  @Mock
  private Preferences preferences;
  private WindowControlPanel windowControlPanel;

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

    windowControlPanel = GuiActionRunner.execute(new Callable<WindowControlPanel>()
    {
      @Override
      public WindowControlPanel call() throws Exception
      {
        return new WindowControlPanel(preferences, new StartupLabelLocalizer(), // KeyLabelLocalizer cannot be used because it does not provide any TextMessage pattern
                                      repeater, dropTargetListener, displayFrameManager);
      }
    });
    frameFixture = showInFrame(windowControlPanel);
  }

  @After
  public void tearDown()
  {
    frameFixture.cleanUp();
  }

//------------------------------------------------------------------------------

  @Test
  public void shouldNotAddDisplayPanelWhenStarting()
  {
    verify(repeater, never()).addReceiver(displayPanel);                        // this relies on assertFalse(preferences.getBoolean(s, false)). The counterpart when they return true should be tested too, but to reinitialize the framwFirxter is a big deal
  }

  @Test
  public void shouldOpenNewWindowWithFilterWhenButtonClickedWithCheckbox()
  {
    frameFixture.checkBox(WindowControlPanel.AUTOAPPLY_FILTER_NAME).check(true);
    frameFixture.button(WindowControlPanel.ADD_BUTTON_NAME).click();

    verify(displayFrameManager, times(1)).getNewDisplayFrame(true);
    verify(displayFrameManager, times(0)).getNewDisplayFrame(false);
  }

  @Test
  public void shouldOpenNewWindowWithoutFilterWhenButtonClickedWithoutCheckbox()
  {
    frameFixture.checkBox(WindowControlPanel.AUTOAPPLY_FILTER_NAME).check(false);
    frameFixture.button(WindowControlPanel.ADD_BUTTON_NAME).click();

    verify(displayFrameManager, times(0)).getNewDisplayFrame(true);
    verify(displayFrameManager, times(1)).getNewDisplayFrame(false);
  }

  @Test
  public void shouldChangeLabelWhenAmountChanged()
  {
    String originalText = frameFixture.label(WindowControlPanel.AMOUNT_LABEL_NAME).text();

    when(displayFrameManager.getAmountOfOpenDisplayFrames()).thenReturn(6666);
    GuiActionRunner.execute(new GuiTask()
    {
      @Override
      protected void executeInEDT() {windowControlPanel.onAmountChanged(displayFrameManager);}
    });
    String modifiedText = frameFixture.label(WindowControlPanel.AMOUNT_LABEL_NAME).text();

    assertNotEquals("Label should change when amount of windows changed",
                    originalText, modifiedText);
  }

  @Test
  public void shouldRearrangeWindowsHorizontallyWhenColumnsSelected()
  {
    frameFixture.spinner(WindowControlPanel.LINES_SPINNER_NAME).enterText("17");
    frameFixture.comboBox(WindowControlPanel.COMBO_HORIZONTAL_NAME).selectItem(0);  // the API does not support setting a value, so it has to be based on the order of the options
    frameFixture.button(WindowControlPanel.ARRANGE_BUTTON_NAME).click();
    verify(displayFrameManager).rearrangeWindows(eq(17), eq(true));
  }

  @Test
  public void shouldRearrangeWindowsVerticallyWhenRowsSelected()
  {
    frameFixture.spinner(WindowControlPanel.LINES_SPINNER_NAME).enterText("19");
    frameFixture.comboBox(WindowControlPanel.COMBO_HORIZONTAL_NAME).selectItem(1);  // the API does not support setting a value, so it has to be based on the order of the options
    frameFixture.button(WindowControlPanel.ARRANGE_BUTTON_NAME).click();
    verify(displayFrameManager).rearrangeWindows(eq(19), eq(false));
  }

  @Test
  public void shouldReapplyFiltersWhenButtonClicked()
  {
    frameFixture.button(WindowControlPanel.REFILTER_BUTTON_NAME).click();
    verify(displayFrameManager).applySequentialFilters();
  }
}
