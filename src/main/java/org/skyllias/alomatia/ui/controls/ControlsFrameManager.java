
package org.skyllias.alomatia.ui.controls;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import org.skyllias.alomatia.i18n.LabelLocalizer;
import org.skyllias.alomatia.ui.frame.MainApplicationFrameSupplier;
import org.springframework.stereotype.Component;

/** Logic for the main windows of the application.
 *  <p>
 *  Only one instance is expected in a given application. */

@Component
public class ControlsFrameManager
{
  private static final String CONTROL_TITLE = "control.window.title";

  private final LabelLocalizer labelLocalizer;
  private final ControlsWindowFactory controlsWindowFactory;
  private final MainApplicationFrameSupplier mainApplicationFrameSupplier;
  private final ControlsPaneComposer controlsPaneComposer;

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

  public void createControlFrame()
  {
    JFrame mainFrame = mainApplicationFrameSupplier.getMainFrame();

    ControlsWindow controlsWindow = controlsWindowFactory.createControlsWindow(mainFrame);
    setUpControlsFrame(controlsWindow);
  }

//------------------------------------------------------------------------------

  /* Shows ownerContainer as non resizable and packed, with all the controls. */

  private void setUpControlsFrame(ControlsWindow ownerContainer)
  {
    ownerContainer.setTitle(labelLocalizer.getString(CONTROL_TITLE));

    ownerContainer.getContentPane().add(controlsPaneComposer.createComponent(),
                                        BorderLayout.CENTER);

    ownerContainer.pack();
    ownerContainer.setResizable(false);
    ownerContainer.setVisible(true);                                            // do this at the end, when the size has been fixed
  }

//------------------------------------------------------------------------------

}
