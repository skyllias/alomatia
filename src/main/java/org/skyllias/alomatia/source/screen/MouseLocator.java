
package org.skyllias.alomatia.source.screen;

import java.awt.MouseInfo;
import java.awt.PointerInfo;

import org.skyllias.alomatia.source.ScreenSource;
import org.springframework.stereotype.Component;

/** Provider of {@link PointerInfo} instances about the system's mouse position.
 *  Extracted from {@link ScreenSource} for testability purposes. */

@Component
public class MouseLocator
{
//==============================================================================

  /** Returns the current location of the mouse. */

  public PointerInfo getMouseInfo()
  {
    return MouseInfo.getPointerInfo();
  }

//------------------------------------------------------------------------------

}
