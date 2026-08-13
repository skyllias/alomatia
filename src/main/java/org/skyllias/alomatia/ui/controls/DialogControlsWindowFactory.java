
package org.skyllias.alomatia.ui.controls;

import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;

import javax.swing.JDialog;
import javax.swing.JFrame;

import org.skyllias.alomatia.dependency.Profiles;
import org.skyllias.alomatia.ui.EventUtils;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/** ControlsWindowFactory implementation for display on a dialog that opens
 *  when Ctrl + P is pressed. */

@Component
@Profile(Profiles.INTERNAL_WINDOWS)
public class DialogControlsWindowFactory implements ControlsWindowFactory
{
//==============================================================================

  @Override
  public ControlsWindow createControlsWindow(JFrame mainFrame)
  {
    JDialog dialog = new JDialog(mainFrame);
    addPreferencesVisibleKeyListener(dialog);

    return new DialogControlsWindow(dialog);
  }

//------------------------------------------------------------------------------

  /* Adds a global listener that makes dialog visible when Ctrl + P is pressed. */

  private void addPreferencesVisibleKeyListener(final JDialog dialog)
  {
    KeyboardFocusManager keyboardManager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
    keyboardManager.addKeyEventDispatcher(new KeyEventDispatcher()
    {
      @Override
      public boolean dispatchKeyEvent(KeyEvent e)
      {
        if(e.getID() == KeyEvent.KEY_PRESSED)
        {
          if (e.getKeyCode() == KeyEvent.VK_P && EventUtils.isControlDown(e)) dialog.setVisible(true);
        }
        return false;                                                           // allow the event to be redispatched
      }
    });
  }

//------------------------------------------------------------------------------

}
