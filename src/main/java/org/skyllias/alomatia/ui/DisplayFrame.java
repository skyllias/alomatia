
package org.skyllias.alomatia.ui;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.text.*;
import java.util.*;

import javax.swing.*;

import org.skyllias.alomatia.display.*;
import org.skyllias.alomatia.filter.*;
import org.skyllias.alomatia.i18n.*;
import org.skyllias.alomatia.logo.*;
import org.skyllias.alomatia.ui.frame.*;

/** Provider of the logic for windows where the contents of the capture frame
 *  are drawn after filtering them. The real frame is managed by means of a
 *  {@link FrameAdaptorFactory}.
 *  <p>
 *  In an application there can be zero or more of these windows, all with the
 *  same original image but potentially with a different filter applied to each one.
 *  When closed, a DisplayFrameCloseListener is notified and all consumed
 *  resources are freed.
 *  <p>
 *  The display panel gets a listener added so that the options dialog is shown
 *  when clicked. */

@SuppressWarnings("serial")
public class DisplayFrame implements ClosingFrameListener, FilterableDisplay
{
  private static final String DEFAULT_TITLE = "display.window.title";
  private static final String TITLE_PATTERN = "display.window.title.filtered";
  private static final String PANEL_TOOLTIP = "display.panel.tooltip";

  private static final Dimension DEFAULT_SIZE = new Dimension(600, 400);

  private LabelLocalizer labelLocalizer;

  private DisplayPanel displayPanel;

  private Collection<DisplayFrameCloseListener> listeners = new HashSet<>();
  private FilterSelector filterSelector;                                        // the selector from the associated DisplayOptionsDialog, used to set the selected filter externally

  private FrameAdaptor frameAdaptor;                                            // the Swing component with the frame

//==============================================================================

  /** Creates a new window containing the passed display panel. */

  public DisplayFrame(LabelLocalizer localizer, FrameAdaptor adaptor,
                      DisplayPanel panel, FilterFactory filterFactory)
  {
    labelLocalizer = localizer;
    displayPanel   = panel;
    frameAdaptor   = adaptor;

    frameAdaptor.setTitle(DEFAULT_TITLE);
    frameAdaptor.setIcon(getDefaultLogo());

    displayPanel.setToolTipText(labelLocalizer.getString(PANEL_TOOLTIP));

    frameAdaptor.addClosingFrameListener(this);

    DisplayOptionsDialog optionsDialog = new DisplayOptionsDialog(labelLocalizer,
                                                                  this, filterFactory);
    panel.addMouseListener(new DisplayPanelClickListener(optionsDialog));

    filterSelector = optionsDialog.getFilterSelector();
    setUpFilterKeyListeners(filterSelector);

    frameAdaptor.setSize(DEFAULT_SIZE.width, DEFAULT_SIZE.height);              // some windows managers will use a 0 by 0 size if this is not forced
    frameAdaptor.setMaximized(false);
    frameAdaptor.setVisible(true);
  }

//==============================================================================

  /** Registers the listener so that it is notified when the window is closed. */

  public void addListener(DisplayFrameCloseListener listener)
  {
    if (listener != null) listeners.add(listener);
  }

//------------------------------------------------------------------------------

  /** The window title and icon are updated and the panel gets the ImageFilter. */

  @Override
  public void setImageFilter(NamedFilter namedFilter)
  {
    applyFilterToTitle(namedFilter);
    applyFilterToIcon(namedFilter.getFilter());

    displayPanel.setImageFilter(namedFilter.getFilter());
  }

//------------------------------------------------------------------------------

  /** Returns the panel contained in this window. */

  public DisplayPanel getDisplayPanel() {return displayPanel;}

//------------------------------------------------------------------------------

  /** Returns the Frame where this window is contained. */

  public Frame getOwnerFrame() {return frameAdaptor.getOwnerFrame();}

//------------------------------------------------------------------------------

  /** Changes the status of the frame. */

  public void setMaximized(boolean maximized) {frameAdaptor.setMaximized(maximized);}

//------------------------------------------------------------------------------

  /** Changes the size of the frame. */

  public void setSize(int width, int height) {frameAdaptor.setSize(width, height);}

//------------------------------------------------------------------------------

  /** Changes the location of the frame. */

  public void setLocation(int x, int y) {frameAdaptor.setLocation(x, y);}

//------------------------------------------------------------------------------

  /** Changes the selection to the named filter at the passed position, being 0
   *  the first one.
   *  If index is below zero or above the amount of filters, nothing happens. */

  public void applyFilterAt(int index) {filterSelector.selectFilterAt(index);}

//------------------------------------------------------------------------------

  /* Modifies the frame's input and action maps so that the selection of
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
    setUpNumberKeyListeners(filterSelector, 30, KeyEvent.SHIFT_DOWN_MASK | KeyEvent.ALT_DOWN_MASK);
  }

//------------------------------------------------------------------------------

  /* Modifies the frame's input and action maps so that the number keys,
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
      frameAdaptor.getInputMap().put(stroke, actionName);
      frameAdaptor.getActionMap().put(actionName, new AbstractAction()
      {
        @Override
        public void actionPerformed(ActionEvent event) {filterSelector.selectFilterAt(index);}  // same as applyFilterAt(index)
      });
    }
  }

//------------------------------------------------------------------------------

  /* Changes the window title by including the localized name of filter. */

  private void applyFilterToTitle(NamedFilter filter)
  {
    String filterName         = labelLocalizer.getString(filter.getNameKey());
    MessageFormat titleFormat = new MessageFormat(labelLocalizer.getString(TITLE_PATTERN));
    String title              = titleFormat.format(new Object[] {filterName});
    frameAdaptor.setTitle(title);
  }

//------------------------------------------------------------------------------

  /* Changes the window icon by applying filter to the default logo. */

  private void applyFilterToIcon(ImageFilter filter)
  {
    Image logo = getDefaultLogo();
    if (filter != null)
    {
      ImageProducer producer = new FilteredImageSource(logo.getSource(), filter);
      logo                   = Toolkit.getDefaultToolkit().createImage(producer);
    }
    frameAdaptor.setIcon(logo);
  }

//------------------------------------------------------------------------------

  /** Returns the logo used when there is no filter applied.
   *  Copied from BasicAlomatiaWindow. */

  private Image getDefaultLogo()
  {
    return new LogoProducer().createImage(ControlFrame.ICON_WIDTH,
                                          ControlFrame.ICON_HEIGHT);            // dynamically generated every time instead of reading it from file or even caching it: it is not such a big overhead
  }

//------------------------------------------------------------------------------
//                        WINDOW LISTENER
//------------------------------------------------------------------------------

  /** Notifies all the listeners and disposes the frame. */

  @Override
  public void onClosingFrame(FrameAdaptor adaptor)
  {
    for (DisplayFrameCloseListener currentListener : listeners) currentListener.onDisplayFrameClosed(this);

    adaptor.dispose();
  }

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
