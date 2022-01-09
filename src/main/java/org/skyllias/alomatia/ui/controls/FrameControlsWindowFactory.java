
package org.skyllias.alomatia.ui.controls;

import javax.swing.JFrame;

import org.skyllias.alomatia.dependency.Profiles;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/** ControlsWindowFactory implementation for display on the main JFrame. */

@Component
@Profile(Profiles.SEPARATE_WINDOWS)
public class FrameControlsWindowFactory implements ControlsWindowFactory
{
//==============================================================================

  @Override
  public ControlsWindow createControlsWindow(JFrame mainFrame)
  {
    return new FrameControlsWindow(mainFrame);
  }

//------------------------------------------------------------------------------

}
