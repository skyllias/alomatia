
package org.skyllias.alomatia.ui.start;

import javax.swing.SwingUtilities;

import org.skyllias.alomatia.dependency.Profiles;
import org.skyllias.alomatia.ui.WindowControlPanelComposer;
import org.skyllias.alomatia.ui.controls.ControlsFrameManager;
import org.skyllias.alomatia.ui.controls.ControlsWindow;
import org.skyllias.alomatia.ui.frame.JInternalFrameAdaptorFactory;
import org.skyllias.alomatia.ui.frame.MainApplicationFrameSupplier;
import org.skyllias.alomatia.ui.menu.MenuBarComposer;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/** StartupWindowOpener that shows the main frame as the container of the
 *  display frames, with the menu bar to manage them.
 *
 *  The controls window is created but not shown, since the user opens it on
 *  demand. */

@Component
@Profile(Profiles.INTERNAL_WINDOWS)
public class InternalWindowsStartupWindowOpener implements StartupWindowOpener
{
  private final ControlsFrameManager controlsFrameManager;
  private final MainApplicationFrameSupplier mainApplicationFrameSupplier;
  private final JInternalFrameAdaptorFactory internalFrameAdaptorFactory;
  private final MenuBarComposer menuBarComposer;
  private final WindowControlPanelComposer windowControlPanelComposer;

//==============================================================================

  public InternalWindowsStartupWindowOpener(ControlsFrameManager controlsFrameManager,
                                            MainApplicationFrameSupplier mainApplicationFrameSupplier,
                                            JInternalFrameAdaptorFactory internalFrameAdaptorFactory,
                                            MenuBarComposer menuBarComposer,
                                            WindowControlPanelComposer windowControlPanelComposer)
  {
    this.controlsFrameManager         = controlsFrameManager;
    this.mainApplicationFrameSupplier = mainApplicationFrameSupplier;
    this.internalFrameAdaptorFactory  = internalFrameAdaptorFactory;
    this.menuBarComposer              = menuBarComposer;
    this.windowControlPanelComposer   = windowControlPanelComposer;
  }

//==============================================================================

  @Override
  public void openStartupWindows()
  {
    ControlsWindow controlsWindow = controlsFrameManager.getControlsWindow();

    mainApplicationFrameSupplier.getMainFrame()
                                .setJMenuBar(menuBarComposer.createComponentForInternalDisplays(controlsWindow));
    internalFrameAdaptorFactory.addDesktopPane();
    mainApplicationFrameSupplier.showMainFrame();

    openNewWindowLater();
  }

//------------------------------------------------------------------------------

  /* Opens the display frame, if any, after all the other processing in the
   * current thread, so that it is placed inside an already visible container. */

  private void openNewWindowLater()
  {
    SwingUtilities.invokeLater(new Runnable()
    {
      @Override
      public void run() {windowControlPanelComposer.openNewWindowIfRequired();}
    });
  }

//------------------------------------------------------------------------------

}
