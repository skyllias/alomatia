
package org.skyllias.alomatia.ui;

import java.awt.Toolkit;
import java.awt.event.KeyEvent;

/** Helper methods to deal with Swing events. */

public class EventUtils
{
//==============================================================================

  /** Returns the key mask generally used by the system as the control key.
   *  This is usually the control key itself, but not in Mac, where it is the
   *  command key. */

  public static int getSystemControlModifier()
  {
    return Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();
  }

//------------------------------------------------------------------------------

  /** Returns true if e has the control key pressed.
   *  It is equivalent to e.isControlDown() but in a system-independent way,
   *  taking into account the menu shortcut from the toolkit. */

  public static boolean isControlDown(KeyEvent e)
  {
    return (e.getModifiers() & getSystemControlModifier()) != 0;
  }

//------------------------------------------------------------------------------

}
