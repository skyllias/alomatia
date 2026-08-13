
package org.skyllias.alomatia.ui.controls;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import org.skyllias.alomatia.i18n.LabelLocalizer;
import org.skyllias.alomatia.ui.frame.MainApplicationFrameSupplier;
import org.springframework.stereotype.Component;

/** Logic for the window containing the controls of the application. */

@Component
public class ControlsFrameManager
{
  private static final String CONTROL_TITLE = "control.window.title";

  private final LabelLocalizer labelLocalizer;
  private final ControlsWindowFactory controlsWindowFactory;
  private final MainApplicationFrameSupplier mainApplicationFrameSupplier;
  private final ControlsPaneComposer controlsPaneComposer;

  private ControlsWindow controlsWindow;

//==============================================================================

  public ControlsFrameManager(LabelLocalizer labelLocalizer,
                              ControlsWindowFactory controlsWindowFactory,
                              MainApplicationFrameSupplier mainApplicationFrameSupplier,
                              ControlsPaneComposer controlsPaneComposer)
  {
    this.labelLocalizer               = labelLocalizer;
    this.mainApplicationFrameSupplier = mainApplicationFrameSupplier;
    this.controlsWindowFactory        = controlsWindowFactory;
    this.controlsPaneComposer         = controlsPaneComposer;
  }

//==============================================================================

  /** Returns the window with the controls, creating it if required.
   *  It is packed and not resizable, but not necessarily visible.
   *  It must be invoked from the event dispatch thread. */

  public ControlsWindow getControlsWindow()
  {
    if (controlsWindow == null) controlsWindow = buildNewControlsWindow();

    return controlsWindow;
  }

//------------------------------------------------------------------------------

  /* Returns a new invisible window, packed and non resizable, with all the controls. */

  private ControlsWindow buildNewControlsWindow()
  {
    JFrame mainFrame = mainApplicationFrameSupplier.getMainFrame();

    ControlsWindow newControlsWindow = controlsWindowFactory.createControlsWindow(mainFrame);
    newControlsWindow.setTitle(labelLocalizer.getString(CONTROL_TITLE));

    newControlsWindow.getContentPane().add(controlsPaneComposer.createComponent(),
                                           BorderLayout.CENTER);

    newControlsWindow.pack();
    newControlsWindow.setResizable(false);

    return newControlsWindow;
  }

//------------------------------------------------------------------------------

}
