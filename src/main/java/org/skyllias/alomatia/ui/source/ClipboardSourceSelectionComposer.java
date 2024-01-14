package org.skyllias.alomatia.ui.source;

import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.skyllias.alomatia.ImageSource;
import org.skyllias.alomatia.i18n.LabelLocalizer;
import org.skyllias.alomatia.preferences.SourcePreferences;
import org.skyllias.alomatia.source.ClipboardSource;
import org.skyllias.alomatia.ui.EventUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/** Composer for a selector of {@link ClipboardSource}. */

@Component
@Order(2)
public class ClipboardSourceSelectionComposer implements SourceSelectionComposer
{
  private static final String SOURCE_KEY = "clipboard";

  private static final String CLIPBOARD_AUTO_LABEL = "source.selector.clipboard.checkbox";

  protected static final String AUTOMODE_CHECKBOX_NAME = "checkbox.automode";

  private final ClipboardSource clipboardSource;
  private final SourcePreferences sourcePreferences;
  private final LabelLocalizer labelLocalizer;

//==============================================================================

  public ClipboardSourceSelectionComposer(ClipboardSource clipboardSource,
                                          SourcePreferences sourcePreferences,
                                          LabelLocalizer labelLocalizer)
  {
    this.clipboardSource   = clipboardSource;
    this.sourcePreferences = sourcePreferences;
    this.labelLocalizer    = labelLocalizer;

    addPasteKeyListener();
  }

//==============================================================================

  @Override
  public SourceSelection buildSelector()
  {
    return new ClipboardSourceSelection();
  }

//------------------------------------------------------------------------------

  @Override
  public String getSourceKey() {return SOURCE_KEY;}

//------------------------------------------------------------------------------

  /* Adds a global listener that makes clipboardSource show the contents of the
   * clipboard when the proper keys are pressed: Ctrl + V or Shift + Insert. */

  private void addPasteKeyListener()
  {
    KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
    manager.addKeyEventDispatcher(new KeyEventDispatcher()
    {
      @Override
      public boolean dispatchKeyEvent(KeyEvent e)
      {
        if(e.getID() == KeyEvent.KEY_PRESSED)
        {
          int pressedKeyCode    = e.getKeyCode();
          boolean isControlDown = EventUtils.isControlDown(e);
          boolean isShiftDown   = e.isShiftDown();
          boolean isPaste = (isControlDown && pressedKeyCode == KeyEvent.VK_V) ||
                            (isShiftDown && pressedKeyCode == KeyEvent.VK_INSERT);
          if (isPaste) clipboardSource.readFromClipboard();
        }
        return false;                                                           // allow the event to be redispatched
      }
    });
  }

//------------------------------------------------------------------------------

//******************************************************************************

  private class ClipboardSourceSelection implements SourceSelection
  {
    private final JCheckBox autoCheckbox;
    private final JPanel controlsPanel;

    public ClipboardSourceSelection()
    {
      boolean autoMode = sourcePreferences.isClipboardAutoMode();
      clipboardSource.setAutoMode(autoMode);

      autoCheckbox = buildAutoCheckbox(autoMode);

      controlsPanel = new JPanel();
      controlsPanel.setLayout(new BoxLayout(controlsPanel, BoxLayout.X_AXIS));

      controlsPanel.add(autoCheckbox);
      controlsPanel.add(Box.createHorizontalGlue());
    }

    @Override
    public ImageSource getSource()
    {
      return new ImageSource()
      {
        @Override
        public void setActive(boolean active)
        {
          clipboardSource.setActive(active);
          autoCheckbox.setEnabled(active);
        }
      };
    }

    @Override
    public JComponent getControls() {return controlsPanel;}

    private JCheckBox buildAutoCheckbox(boolean isAutoMode)
    {
      JCheckBox checkbox = new JCheckBox(labelLocalizer.getString(CLIPBOARD_AUTO_LABEL), isAutoMode);
      checkbox.setName(AUTOMODE_CHECKBOX_NAME);
      checkbox.setEnabled(false);
      checkbox.addChangeListener(new ChangeListener()
      {
        @Override
        public void stateChanged(ChangeEvent event)
        {
          boolean newAutoMode = checkbox.isSelected();
          clipboardSource.setAutoMode(newAutoMode);

          sourcePreferences.setClipboardAutoMode(newAutoMode);
        }
      });

      return checkbox;
    }
  }
}
