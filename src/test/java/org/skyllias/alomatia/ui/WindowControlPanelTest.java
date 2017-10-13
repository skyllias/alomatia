
package org.skyllias.alomatia.ui;

import static org.assertj.swing.fixture.Containers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.*;
import java.awt.dnd.*;
import java.util.concurrent.*;

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
        return new WindowControlPanel(new StartupLabelLocalizer(), repeater, dropTargetListener, displayFrameManager);
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
  public void shouldAddDisplayPanelWhenStarting()
  {
    verify(repeater).addReceiver(displayPanel);
  }

  @Test
  public void shouldOpenNewWindowWithFilterWhenButtonClickedWithCheckbox()
  {
    boolean initiallyWithFilters   = windowControlPanel.isAutomaticallyApplyingFilters();
    int expectedTimesWithFilter    = 0;
    int expectedTimesWithoutFilter = 0;
    if (initiallyWithFilters) expectedTimesWithFilter++;
    else                      expectedTimesWithoutFilter++;
    verify(displayFrameManager).getNewDisplayFrame(initiallyWithFilters);       // the one opened when starting

    frameFixture.checkBox(WindowControlPanel.AUTOAPPLY_FILTER_NAME).check(true);
    expectedTimesWithFilter++;
    frameFixture.button(WindowControlPanel.ADD_BUTTON_NAME).click();

    verify(displayFrameManager, times(expectedTimesWithFilter)).getNewDisplayFrame(true);
    verify(displayFrameManager, times(expectedTimesWithoutFilter)).getNewDisplayFrame(false);
  }

  @Test
  public void shouldOpenNewWindowWithoutFilterWhenButtonClickedWithoutCheckbox()
  {
    boolean initiallyWithFilters   = windowControlPanel.isAutomaticallyApplyingFilters();
    int expectedTimesWithFilter    = 0;
    int expectedTimesWithoutFilter = 0;
    if (initiallyWithFilters) expectedTimesWithFilter++;
    else                      expectedTimesWithoutFilter++;
    verify(displayFrameManager).getNewDisplayFrame(initiallyWithFilters);       // the one opened when starting

    frameFixture.checkBox(WindowControlPanel.AUTOAPPLY_FILTER_NAME).check(false);
    expectedTimesWithoutFilter++;
    frameFixture.button(WindowControlPanel.ADD_BUTTON_NAME).click();

    verify(displayFrameManager, times(expectedTimesWithFilter)).getNewDisplayFrame(true);
    verify(displayFrameManager, times(expectedTimesWithoutFilter)).getNewDisplayFrame(false);
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

    assertNotEquals("Label should change when amount of windows changed", originalText, modifiedText);
  }

  @Test
  public void shouldRearrangeWindowsHorizontallyWhenColumnsSelected()
  {
    String previousContents = frameFixture.spinner(WindowControlPanel.LINES_SPINNER_NAME).text();
    frameFixture.spinner(WindowControlPanel.LINES_SPINNER_NAME).enterText("17");
    frameFixture.comboBox(WindowControlPanel.COMBO_HORIZONTAL_NAME).selectItem(0);  // the API does not support setting a value, so it has to be based on the order of the options
    frameFixture.button(WindowControlPanel.ARRANGE_BUTTON_NAME).click();
    verify(displayFrameManager).rearrangeWindows(eq(17), any(Rectangle.class), eq(true));

    frameFixture.spinner(WindowControlPanel.LINES_SPINNER_NAME).enterText(previousContents);  // could be moved to tearDown
    frameFixture.button(WindowControlPanel.ARRANGE_BUTTON_NAME).click();
  }

  @Test
  public void shouldRearrangeWindowsVerticallyWhenRowsSelected()
  {
    String previousContents = frameFixture.spinner(WindowControlPanel.LINES_SPINNER_NAME).text();
    frameFixture.spinner(WindowControlPanel.LINES_SPINNER_NAME).enterText("19");
    frameFixture.comboBox(WindowControlPanel.COMBO_HORIZONTAL_NAME).selectItem(1);  // the API does not support setting a value, so it has to be based on the order of the options
    frameFixture.button(WindowControlPanel.ARRANGE_BUTTON_NAME).click();
    verify(displayFrameManager).rearrangeWindows(eq(19), any(Rectangle.class), eq(false));

    frameFixture.spinner(WindowControlPanel.LINES_SPINNER_NAME).enterText(previousContents);  // could be moved to tearDown
    frameFixture.button(WindowControlPanel.ARRANGE_BUTTON_NAME).click();
  }

  @Test
  public void shouldReapplyFiltersWhenButtonClicked()
  {
    frameFixture.button(WindowControlPanel.REFILTER_BUTTON_NAME).click();
    verify(displayFrameManager).applySequentialFilters();
  }
}
