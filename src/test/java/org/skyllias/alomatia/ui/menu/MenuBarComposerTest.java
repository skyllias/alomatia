
package org.skyllias.alomatia.ui.menu;

import static org.junit.Assert.assertThrows;
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
import javax.swing.JMenuBar;

import org.assertj.swing.edt.FailOnThreadViolationRepaintManager;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.exception.ComponentLookupException;
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
import org.skyllias.alomatia.ui.controls.ControlsWindow;
import org.skyllias.alomatia.ui.frame.MainApplicationFrameSupplier;

@RunWith(MockitoJUnitRunner.class)
public class MenuBarComposerTest
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
  private MenuBarComposer menuBarComposer;

  private FrameFixture frameFixture;


  @BeforeClass
  public static void setUpOnce()
  {
    FailOnThreadViolationRepaintManager.install();
  }

  private void setUpInternalDisplaysUi()
  {
    setUpUi(new Callable<JMenuBar>()
    {
      @Override
      public JMenuBar call() throws Exception
      {
        return menuBarComposer.createComponentForInternalDisplays(controlsWindow);
      }
    });
  }

  private void setUpSeparateWindowsUi()
  {
    setUpUi(new Callable<JMenuBar>()
    {
      @Override
      public JMenuBar call() throws Exception
      {
        return menuBarComposer.createComponentForSeparateWindows();
      }
    });
  }

  private void setUpUi(final Callable<JMenuBar> menuBarSupplier)
  {
    JFrame frame = GuiActionRunner.execute(new Callable<JFrame>()
    {
      @Override
      public JFrame call() throws Exception
      {
        JFrame hostFrame = new JFrame();
        hostFrame.setJMenuBar(menuBarSupplier.call());
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
    setUpInternalDisplaysUi();

    frameFixture.menuItem(MenuBarComposer.NEW_ITEM_NAME).click();

    verify(windowControlPanelComposer).createNewDisplayFrame();
  }

  @Test
  public void shouldRearrangeWindowsWithPreferredValuesWhenArrangeClicked()
  {
    when(windowControlPreferences.getAmountOfLinesToArrangeIn()).thenReturn(3);
    when(windowControlPreferences.isHorizontallyArranged()).thenReturn(false);

    setUpInternalDisplaysUi();

    frameFixture.menuItem(MenuBarComposer.ARRANGE_ITEM_NAME).click();

    verify(displayFrameManager).rearrangeWindows(3, false);
  }

  @Test
  public void shouldShowControlsWindowWhenControlsClicked()
  {
    setUpInternalDisplaysUi();

    frameFixture.menuItem(MenuBarComposer.CONTROLS_ITEM_NAME).click();

    verify(controlsWindow).setVisible(true);
  }

  @Test
  public void shouldCloseMainFrameWhenExitClicked()
  {
    WindowListener closeListener = setUpMainFrameCloseListener();

    setUpInternalDisplaysUi();

    frameFixture.menuItem(MenuBarComposer.EXIT_ITEM_NAME).click();

    verify(closeListener).windowClosing(any(WindowEvent.class));
  }

  @Test
  public void shouldDoNothingWhenNoItemClicked()
  {
    setUpInternalDisplaysUi();

    frameFixture.menuItem(MenuBarComposer.WINDOW_MENU_NAME).click();

    verifyZeroInteractions(windowControlPanelComposer, displayFrameManager,
                           mainApplicationFrameSupplier, controlsWindow);
  }

  @Test
  public void shouldCreateDisplayFrameWhenNewWindowClickedWithSeparateWindows()
  {
    setUpSeparateWindowsUi();

    frameFixture.menuItem(MenuBarComposer.NEW_ITEM_NAME).click();

    verify(windowControlPanelComposer).createNewDisplayFrame();
  }

  @Test
  public void shouldCloseMainFrameWhenExitClickedWithSeparateWindows()
  {
    WindowListener closeListener = setUpMainFrameCloseListener();

    setUpSeparateWindowsUi();

    frameFixture.menuItem(MenuBarComposer.EXIT_ITEM_NAME).click();

    verify(closeListener).windowClosing(any(WindowEvent.class));
  }

  @Test
  public void shouldNotOfferArrangeNorControlsWithSeparateWindows()
  {
    setUpSeparateWindowsUi();

    assertThrows(ComponentLookupException.class,
                 () -> frameFixture.menuItem(MenuBarComposer.ARRANGE_ITEM_NAME));
    assertThrows(ComponentLookupException.class,
                 () -> frameFixture.menuItem(MenuBarComposer.CONTROLS_ITEM_NAME));
  }

  @Test
  public void shouldDoNothingWhenNoItemClickedWithSeparateWindows()
  {
    setUpSeparateWindowsUi();

    frameFixture.menuItem(MenuBarComposer.WINDOW_MENU_NAME).click();

    verifyZeroInteractions(windowControlPanelComposer, displayFrameManager,
                           mainApplicationFrameSupplier, controlsWindow);
  }

//------------------------------------------------------------------------------

  private WindowListener setUpMainFrameCloseListener()
  {
    JFrame mainFrame = GuiActionRunner.execute(new Callable<JFrame>()
    {
      @Override
      public JFrame call() throws Exception {return new JFrame();}
    });
    WindowListener closeListener = mock(WindowListener.class);
    mainFrame.addWindowListener(closeListener);
    when(mainApplicationFrameSupplier.getMainFrame()).thenReturn(mainFrame);

    return closeListener;
  }

}
