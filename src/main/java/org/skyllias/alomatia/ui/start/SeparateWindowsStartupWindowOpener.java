
package org.skyllias.alomatia.ui.start;

import javax.swing.SwingUtilities;

import org.skyllias.alomatia.dependency.Profiles;
import org.skyllias.alomatia.ui.WindowControlPanelComposer;
import org.skyllias.alomatia.ui.controls.ControlsFrameManager;
import org.skyllias.alomatia.ui.frame.MainApplicationFrameSupplier;
import org.skyllias.alomatia.ui.menu.MenuBarComposer;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/** StartupWindowOpener that shows the main frame with the controls in it and
 *  the menu bar to manage the display frames, since they are independent
 *  windows in this policy. */

@Component
@Profile(Profiles.SEPARATE_WINDOWS)
public class SeparateWindowsStartupWindowOpener implements StartupWindowOpener
{
  private final ControlsFrameManager controlsFrameManager;
  private final MainApplicationFrameSupplier mainApplicationFrameSupplier;
  private final MenuBarComposer menuBarComposer;
  private final WindowControlPanelComposer windowControlPanelComposer;

//==============================================================================

  public SeparateWindowsStartupWindowOpener(ControlsFrameManager controlsFrameManager,
                                            MainApplicationFrameSupplier mainApplicationFrameSupplier,
                                            MenuBarComposer menuBarComposer,
                                            WindowControlPanelComposer windowControlPanelComposer)
  {
    this.controlsFrameManager         = controlsFrameManager;
    this.mainApplicationFrameSupplier = mainApplicationFrameSupplier;
    this.menuBarComposer              = menuBarComposer;
    this.windowControlPanelComposer   = windowControlPanelComposer;
  }

//==============================================================================

  @Override
  public void openStartupWindows()
  {
    mainApplicationFrameSupplier.getMainFrame()
                                .setJMenuBar(menuBarComposer.createComponentForSeparateWindows());
    controlsFrameManager.getControlsWindow();                                   // this puts the controls in the main frame
    mainApplicationFrameSupplier.showMainFrame();

    openNewWindowLater();
  }

//------------------------------------------------------------------------------

  /* Opens the display frame, if any, after all the other processing in the
   * current thread. Otherwise, it would appear before the controls window and
   * separated from the subsequent windows in the task bar. */

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
