
package org.skyllias.alomatia.ui.controls;

import javax.swing.JFrame;

/** Factory of ControlsWindow instances associated to an owner main JFrame. */

public interface ControlsWindowFactory
{
  ControlsWindow createControlsWindow(JFrame mainFrame);
}
