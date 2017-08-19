
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
    when(displayFrameManager.getNewDisplayFrame()).thenReturn(displayFrame);

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
    verify(repeater, times(1)).addReceiver(displayPanel);
  }

  @Test
  public void shouldOpenNewWindowWhenButtonClicked()
  {
    verify(displayFrameManager, times(1)).getNewDisplayFrame();                 // the one opened when starting

    frameFixture.button(WindowControlPanel.ADD_BUTTON_NAME).click();
    verify(displayFrameManager, times(2)).getNewDisplayFrame();
  }

  @Test
  public void shouldChangeLabelWhenAmountChanged()
  {
    String originalText = frameFixture.label(WindowControlPanel.AMOUNT_LABEL_NAME).text();

    when(displayFrameManager.getAmountOfOpenDisplayFrames()).thenReturn(6);
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
    frameFixture.spinner(WindowControlPanel.LINES_SPINNER_NAME).enterText("6");
    frameFixture.comboBox(WindowControlPanel.COMBO_HORIZONTAL_NAME).selectItem(0);  // the API does not support setting a value, so it has to be based on the order of the options
    frameFixture.button(WindowControlPanel.ARRANGE_BUTTON_NAME).click();
    verify(displayFrameManager, times(1)).rearrangeWindows(eq(6), any(Rectangle.class), eq(true));
  }

  @Test
  public void shouldRearrangeWindowsVerticallyWhenRowsSelected()
  {
    frameFixture.spinner(WindowControlPanel.LINES_SPINNER_NAME).enterText("5");
    frameFixture.comboBox(WindowControlPanel.COMBO_HORIZONTAL_NAME).selectItem(1);  // the API does not support setting a value, so it has to be based on the order of the options
    frameFixture.button(WindowControlPanel.ARRANGE_BUTTON_NAME).click();
    verify(displayFrameManager, times(1)).rearrangeWindows(eq(5), any(Rectangle.class), eq(false));
  }
}
