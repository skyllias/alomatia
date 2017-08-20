
package org.skyllias.alomatia.ui;

import java.awt.*;
import java.awt.event.*;
import java.text.*;
import java.util.*;

import javax.swing.*;

import org.skyllias.alomatia.display.*;
import org.skyllias.alomatia.filter.*;
import org.skyllias.alomatia.i18n.*;

/** Window where the contents of the capture frame are drawn after filtering them.
 *  <p>
 *  In an application there can be one or more of these windows, all with the
 *  same original image but potentially with a different filter applied to each one.
 *  When closed, a DisplayFrameCloseListener is notified and all consumed
 *  resources are freed.
 *  <p>
 *  The display panel gets a listener added so that the options dialog is shown
 *  when clicked.
 *  TODO Apply composition over inheritance in order to work indistinctively with JFrames or JInternalFrames. */

@SuppressWarnings("serial")
public class DisplayFrame extends BasicAlomatiaWindow
                          implements WindowListener, FilterableDisplay
{
  private static final String DEFAULT_TITLE = "display.window.title";
  private static final String TITLE_PATTERN = "display.window.title.filtered";
  private static final String PANEL_TOOLTIP = "display.panel.tooltip";

  private DisplayPanel displayPanel;

  private Collection<DisplayFrameCloseListener> listeners = new HashSet<>();

//==============================================================================

  /** Creates a new window containing the passed display panel. */

  public DisplayFrame(LabelLocalizer localizer, DisplayPanel panel, FilterFactory filterFactory)
  {
    super(localizer, DEFAULT_TITLE);

    displayPanel = panel;
    getContentPane().add(displayPanel, BorderLayout.CENTER);
    displayPanel.setToolTipText(getLabelLocalizer().getString(PANEL_TOOLTIP));

    setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);              // the window will be closed, but TODO only if it is not the last one standing
    addWindowListener(this);
    setVisible(true);

    DisplayOptionsDialog optionsDialog = new DisplayOptionsDialog(getLabelLocalizer(),
                                                                  this, filterFactory);
    panel.addMouseListener(new DisplayPanelClickListener(optionsDialog));

    setUpFilterKeyListeners(optionsDialog.getFilterSelector());
  }

//==============================================================================

  /** Registers the listener so that it is notified when the window is closed. */

  public void addListener(DisplayFrameCloseListener listener)
  {
    if (listener != null) listeners.add(listener);
  }

//------------------------------------------------------------------------------

  /** The window title is updated and the panel gets the ImageFilter.
   *  The icon could also be updated in the future. */

  @Override
  public void setImageFilter(NamedFilter namedFilter)
  {
    LabelLocalizer localizer  = getLabelLocalizer();
    String filterName         = localizer.getString(namedFilter.getNameKey());
    MessageFormat titleFormat = new MessageFormat(localizer.getString(TITLE_PATTERN));
    String title              = titleFormat.format(new Object[] {filterName});
    setTitle(title);

    displayPanel.setImageFilter(namedFilter.getFilter());
  }

//------------------------------------------------------------------------------

  /** Returns the panel contained in this window. */

  public DisplayPanel getDisplayPanel() {return displayPanel;}

//------------------------------------------------------------------------------

  /* Modifies the root pane's input and action maps so that the selection of
   * filterSelector is modified when some key strokes take place.
   * The selected keys try to avoid to violate the principle of consistency. So
   * although the Fx keys are 12, they are not used (moreover, some desktops
   * may use them for other functionalities). So, from 0 to 9, the number keys
   * are used to select the first 10 filters, and then the same with the shift
   * key pressed for the next 10 filters, and then the same with the alt key
   * for the nect 10 filters. The control modifier is avoided because it is
   * currently used to choose zoom. */

  private void setUpFilterKeyListeners(FilterSelector filterSelector)
  {
    setUpNumberKeyListeners(filterSelector, 0, 0);
    setUpNumberKeyListeners(filterSelector, 10, KeyEvent.SHIFT_DOWN_MASK);
    setUpNumberKeyListeners(filterSelector, 20, KeyEvent.ALT_DOWN_MASK);
  }

//------------------------------------------------------------------------------

  /* Modifies the root pane's input and action maps so that the number keys,
   * when pressed with the passed modifiers, selects the (offset + i)th filter
   * in filterSelector. */

  private void setUpNumberKeyListeners(final FilterSelector filterSelector,
                                       int offset, int modifiers)
  {
    final String ACTION_NAME_PREFIX = "filterSelector";
    for (int i = 0; i < 10; i++)
    {
      final int index   = offset + i;
      String actionName = i + ACTION_NAME_PREFIX + modifiers;
      KeyStroke stroke  = KeyStroke.getKeyStroke(KeyEvent.VK_0 + i, modifiers);
      getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(stroke, actionName);
      getRootPane().getActionMap().put(actionName, new AbstractAction()
      {
        @Override
        public void actionPerformed(ActionEvent event) {filterSelector.selectFilterAt(index);}
      });
    }
  }

//------------------------------------------------------------------------------
//                        WINDOW LISTENER
//------------------------------------------------------------------------------

  /** Notifies all the listeners and disposes itself. */

  @Override
  public void windowClosing(WindowEvent e)
  {
    for (DisplayFrameCloseListener currentListener : listeners) currentListener.onDisplayFrameClosed(this);

    dispose();
  }

//------------------------------------------------------------------------------

  @Override
  public void windowActivated(WindowEvent arg0) {}

  @Override
  public void windowClosed(WindowEvent arg0) {}

  @Override
  public void windowDeactivated(WindowEvent e) {}

  @Override
  public void windowDeiconified(WindowEvent e) {}

  @Override
  public void windowIconified(WindowEvent e) {}

  @Override
  public void windowOpened(WindowEvent e) {}

//------------------------------------------------------------------------------

//******************************************************************************

  /** Listener of the window close event, so that any clean up operations other
   *  than the disposal performed internally can be carried out. */

  public static interface DisplayFrameCloseListener
  {
    /** Invoked right before displayFrame is disposed.
     *  <p>
     *  It is not worth the effort to create events for this. */

    void onDisplayFrameClosed(DisplayFrame displayFrame);
  }

//******************************************************************************

  /* When a double click or a right click is received, the options dialog is opened. */

  private class DisplayPanelClickListener extends MouseAdapter
  {
    private DisplayOptionsDialog dialog;

    public DisplayPanelClickListener(DisplayOptionsDialog optionsDialog) {dialog = optionsDialog;}

    @Override
    public void mouseClicked(MouseEvent e)
    {
      boolean showDialog = e.getClickCount() > 1 || !SwingUtilities.isLeftMouseButton(e);
      if (showDialog) dialog.setVisible(true);
    }
  }
}
