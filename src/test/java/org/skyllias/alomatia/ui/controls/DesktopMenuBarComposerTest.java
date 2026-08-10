
package org.skyllias.alomatia.ui.controls;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.awt.Dimension;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.concurrent.Callable;

import javax.swing.JFrame;

import org.assertj.swing.edt.FailOnThreadViolationRepaintManager;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.skyllias.alomatia.i18n.KeyLabelLocalizer;
import org.skyllias.alomatia.preferences.WindowControlPreferences;
import org.skyllias.alomatia.ui.DisplayFrameManager;
import org.skyllias.alomatia.ui.WindowControlPanelComposer;
import org.skyllias.alomatia.ui.frame.MainApplicationFrameSupplier;

@RunWith(MockitoJUnitRunner.class)
public class DesktopMenuBarComposerTest
{
  @Spy
  private KeyLabelLocalizer labelLocalizer;

  @Mock
  private WindowControlPanelComposer windowControlPanelComposer;

  @Mock
  private DisplayFrameManager displayFrameManager;

  @Mock
  private WindowControlPreferences windowControlPreferences;

  @Mock
  private MainApplicationFrameSupplier mainApplicationFrameSupplier;

  @Mock
  private ControlsWindow controlsWindow;

  @InjectMocks
  private DesktopMenuBarComposer desktopMenuBarComposer;

  private FrameFixture frameFixture;


  @BeforeClass
  public static void setUpOnce()
  {
    FailOnThreadViolationRepaintManager.install();
  }

  private void setUpUi()
  {
    JFrame frame = GuiActionRunner.execute(new Callable<JFrame>()
    {
      @Override
      public JFrame call() throws Exception
      {
        JFrame hostFrame = new JFrame();
        hostFrame.setJMenuBar(desktopMenuBarComposer.createComponent(controlsWindow));
        hostFrame.setPreferredSize(new Dimension(400, 200));

        return hostFrame;
      }
    });
    frameFixture = new FrameFixture(frame);
    frameFixture.show();
  }

  @After
  public void tearDown()
  {
    frameFixture.cleanUp();
  }

//------------------------------------------------------------------------------

  @Test
  public void shouldCreateDisplayFrameWhenNewWindowClicked()
  {
    setUpUi();

    frameFixture.menuItem(DesktopMenuBarComposer.NEW_ITEM_NAME).click();

    verify(windowControlPanelComposer).createNewDisplayFrame();
  }

  @Test
  public void shouldRearrangeWindowsWithPreferredValuesWhenArrangeClicked()
  {
    when(windowControlPreferences.getAmountOfLinesToArrangeIn()).thenReturn(3);
    when(windowControlPreferences.isHorizontallyArranged()).thenReturn(false);

    setUpUi();

    frameFixture.menuItem(DesktopMenuBarComposer.ARRANGE_ITEM_NAME).click();

    verify(displayFrameManager).rearrangeWindows(3, false);
  }

  @Test
  public void shouldShowControlsWindowWhenControlsClicked()
  {
    setUpUi();

    frameFixture.menuItem(DesktopMenuBarComposer.CONTROLS_ITEM_NAME).click();

    verify(controlsWindow).setVisible(true);
  }

  @Test
  public void shouldCloseMainFrameWhenExitClicked()
  {
    JFrame mainFrame = GuiActionRunner.execute(new Callable<JFrame>()
    {
      @Override
      public JFrame call() throws Exception {return new JFrame();}
    });
    WindowListener closeListener = mock(WindowListener.class);
    mainFrame.addWindowListener(closeListener);
    when(mainApplicationFrameSupplier.getMainFrame()).thenReturn(mainFrame);

    setUpUi();

    frameFixture.menuItem(DesktopMenuBarComposer.EXIT_ITEM_NAME).click();

    verify(closeListener).windowClosing(any(WindowEvent.class));
  }

  @Test
  public void shouldDoNothingWhenNoItemClicked()
  {
    setUpUi();

    frameFixture.menuItem(DesktopMenuBarComposer.WINDOW_MENU_NAME).click();

    verifyZeroInteractions(windowControlPanelComposer, displayFrameManager,
                           mainApplicationFrameSupplier, controlsWindow);
  }

}
