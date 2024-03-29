
package org.skyllias.alomatia.ui;

import java.awt.Frame;
import java.awt.Image;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.ImageFilter;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JDialog;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import org.skyllias.alomatia.display.FilterableDisplay;
import org.skyllias.alomatia.filter.FilteredImageGenerator;
import org.skyllias.alomatia.filter.NamedFilter;
import org.skyllias.alomatia.i18n.LabelLocalizer;
import org.skyllias.alomatia.logo.IconSupplier;
import org.skyllias.alomatia.ui.filter.FilterSelector;
import org.skyllias.alomatia.ui.filter.FilterSelectorComposer;
import org.skyllias.alomatia.ui.frame.FrameAdaptor;
import org.skyllias.alomatia.ui.frame.FrameAdaptorFactory;
import org.skyllias.alomatia.ui.save.ImageSaver;

/** Provider of the logic for windows where the filtered images are drawn. The
 *  real frame is managed by means of a {@link FrameAdaptorFactory}.
 *  <p>
 *  There is one instance of controller per window/frame.
 *  <p>
 *  In an application there can be zero or more of these windows, all with the
 *  same original image but potentially with a different filter applied to each
 *  one. When closed, a {@link DisplayFrameCloseListener} is notified and all
 *  consumed resources are freed.
 *  <p>
 *  The display panel gets a listener added so that the options dialog is shown
 *  when clicked. */

@SuppressWarnings("serial")
public class DisplayFrameController implements FrameAdaptor.ClosingFrameListener, FilterableDisplay
{
  private static final String DEFAULT_TITLE = "display.window.title";
  private static final String TITLE_PATTERN = "display.window.title.filtered";
  private static final String PANEL_TOOLTIP = "display.panel.tooltip";

  private final LabelLocalizer labelLocalizer;
  private final IconSupplier iconSupplier;

  private final FrameAdaptor frameAdaptor;                                      // the Swing component with the frame
  private final DisplayPanelController displayPanel;
  private final FilterSelector filterSelector;                                  // the selector from the associated DisplayOptionsDialog, used to set the selected filter externally
  private final FilteredImageGenerator filteredImageGenerator;
  private final ImageSaver imageSaver;
  private final Clipboard clipboard;

  private final State state = new State();

//==============================================================================

  /** Creates a new window containing the passed display panel. */

  public DisplayFrameController(LabelLocalizer localizer, IconSupplier iconSupplier, FrameAdaptor adaptor,
                                DisplayPanelController panel, FilterSelectorComposer filterSelectorComposer,
                                FilteredImageGenerator filteredImageGenerator, ImageSaver saver,
                                DisplayOptionsDialogComposer dialogComposer, Clipboard clipboard)
  {
    labelLocalizer              = localizer;
    this.iconSupplier           = iconSupplier;
    displayPanel                = panel;
    frameAdaptor                = adaptor;
    this.filteredImageGenerator = filteredImageGenerator;
    imageSaver                  = saver;
    this.clipboard              = clipboard;

    frameAdaptor.setTitle(labelLocalizer.getString(DEFAULT_TITLE));
    frameAdaptor.setIcon(iconSupplier.getIcon());

    displayPanel.getComponent().setToolTipText(labelLocalizer.getString(PANEL_TOOLTIP));

    frameAdaptor.addClosingFrameListener(this);

    filterSelector = filterSelectorComposer.createFilterSelector(this);
    setUpFilterKeyListeners(filterSelector);
    setOutputKeyListeners();

    frameAdaptor.setMaximized(false);
    frameAdaptor.setVisible(true);

    JDialog optionsDialog = dialogComposer.getDialog(frameAdaptor.getOwnerFrame(),
                                                     displayPanel, filterSelector);
    displayPanel.getComponent().addMouseListener(new DisplayPanelClickListener(optionsDialog));
  }

//==============================================================================

  /** Registers the listener so that it is notified when the window is closed. */

  public void addListener(DisplayFrameCloseListener listener)
  {
    if (listener != null) state.listeners.add(listener);
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

  public DisplayPanelController getDisplayPanel() {return displayPanel;}

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
//                            KEY LISTENERS
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

    setUpArrowKeyListeners(filterSelector);
    setUpArrowKeyAllListeners(filterSelector);
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

  /* Modifies the frame's input and action maps so that the arrow keys can be
   * used to change the selected filter. */

  private void setUpArrowKeyListeners(final FilterSelector filterSelector)
  {
    setUpArrowKeyListener(filterSelector, "previousFilterSelector",
                          getKeyEventWithControl(KeyEvent.VK_LEFT), -1);
    setUpArrowKeyListener(filterSelector, "previousManyFilterSelector",
                          getKeyEventWithControl(KeyEvent.VK_UP), -10);
    setUpArrowKeyListener(filterSelector, "nextFilterSelector",
                          getKeyEventWithControl(KeyEvent.VK_RIGHT), 1);
    setUpArrowKeyListener(filterSelector, "nextManyFilterSelector",
                          getKeyEventWithControl(KeyEvent.VK_DOWN), 10);
  }

//------------------------------------------------------------------------------

  /* Returns the key stroke corresponding to the passed virtual key with the Ctrl modifier. */

  private KeyStroke getKeyEventWithControl(int virtualKey)
  {
    return KeyStroke.getKeyStroke(virtualKey,
                                  EventUtils.getSystemControlModifier());
  }

//------------------------------------------------------------------------------

  /* Modifies the frame's input and action maps so that the passed key can be
   * used to change the selected filter by increment. */

  private void setUpArrowKeyListener(final FilterSelector filterSelector,
                                     String actionName, KeyStroke stroke,
                                     int increment)
  {
    frameAdaptor.getInputMap().put(stroke, actionName);
    frameAdaptor.getActionMap().put(actionName, new AbstractAction()
    {
      @Override
      public void actionPerformed(ActionEvent event) {filterSelector.incrementSelectedIndex(increment);}
    });
  }

//------------------------------------------------------------------------------

  /* Sets up the key event handler to navigate over the filter list, no matter where the focus is.
   * The procedure is different from setUpArrowKeyListeners because that one only
   * applies when the frame has the focus. */

  private void setUpArrowKeyAllListeners(final FilterSelector filterSelector)
  {
    setUpArrowKeyAllListener(filterSelector, KeyEvent.VK_LEFT, -1);
    setUpArrowKeyAllListener(filterSelector, KeyEvent.VK_UP, -10);
    setUpArrowKeyAllListener(filterSelector, KeyEvent.VK_RIGHT, 1);
    setUpArrowKeyAllListener(filterSelector, KeyEvent.VK_DOWN, 10);
  }

//------------------------------------------------------------------------------

  /* Sets up the key event handler to navigate over the filter list, no matter where the focus is.
   * The procedure is different from setUpArrowKeyListener because that one only
   * applies when the frame has the focus.
   * The listener should be removed when the frame is disposed (onClosingFrame). */

  private void setUpArrowKeyAllListener(final FilterSelector filterSelector,
                                        int virtualKey, int increment)
  {
    KeyEventDispatcher keyEventDispatcher = new KeyEventDispatcher()
    {
      @Override
      public boolean dispatchKeyEvent(KeyEvent e)
      {
        if(e.getID() == KeyEvent.KEY_PRESSED && EventUtils.isControlDown(e) && e.isShiftDown())
        {
          if (e.getKeyCode() == virtualKey)
          {
            filterSelector.incrementSelectedIndex(increment);
          }
        }
        return false;                                                           // allow the event to be redispatched
      }
    };

    state.eventDispatchers.add(keyEventDispatcher);
    getKeyboardFocusManager().addKeyEventDispatcher(keyEventDispatcher);
  }

//------------------------------------------------------------------------------

  /* Sets up key listeners to extract (copy, save...) the filtered image from
   * the display panel. */

  private void setOutputKeyListeners()
  {
    setCopyKeyListeners();
    setSaveSingleKeyListeners();
    setSaveAllKeyListeners();
  }

//------------------------------------------------------------------------------

  /* Sets up key listeners (for Ctrl + C) to copy the filtered image from the
   * display panel. */

  private void setCopyKeyListeners()
  {
    final String COPY_ACTION = "copyImage";

    KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_C,
                                              EventUtils.getSystemControlModifier());
    frameAdaptor.getInputMap().put(stroke, COPY_ACTION);
    frameAdaptor.getActionMap().put(COPY_ACTION, new AbstractAction()
    {
      @Override
      public void actionPerformed(ActionEvent event)
      {
        Image filteredImage = displayPanel.getFilteredImage();
        if (filteredImage != null) clipboard.setContents(new ImageSelection(filteredImage), null);
      }
    });
  }

//------------------------------------------------------------------------------

  /* Sets up key listener to save the filtered image from the display panel.
   * Ctrl + S is used to save the current image with possible user interaction. */

  private void setSaveSingleKeyListeners()
  {
    final String SAVE_ACTION = "saveImage";

    KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_S,
                                              EventUtils.getSystemControlModifier());
    frameAdaptor.getInputMap().put(stroke, SAVE_ACTION);
    frameAdaptor.getActionMap().put(SAVE_ACTION, new AbstractAction()
    {
      @Override
      public void actionPerformed(ActionEvent event)
      {
        Image filteredImage = displayPanel.getFilteredImage();
        if (filteredImage != null) imageSaver.save(filteredImage, state.currentFilterName, false);
      }
    });
  }

//------------------------------------------------------------------------------

  /* Sets up the key event handler to save the filtered image from the display panel.
   * Ctrl + Shift + S is used to silently save all the frames' images.
   * The procedure is different from setSaveSingleKeyListeners because "save all"
   * has to be performed no matter where the focus is, while the "save single"
   * only applies when the focus is in the current frame.
   * The listener should be removed when the frame is disposed (onClosingFrame). */

  private void setSaveAllKeyListeners()
  {
    KeyEventDispatcher keyEventDispatcher = new KeyEventDispatcher()
    {
      @Override
      public boolean dispatchKeyEvent(KeyEvent e)
      {
        if(e.getID() == KeyEvent.KEY_PRESSED && EventUtils.isControlDown(e) && e.isShiftDown())
        {
          if (e.getKeyCode() == KeyEvent.VK_S)
          {
            Image filteredImage = displayPanel.getFilteredImage();
            if (filteredImage != null) imageSaver.save(filteredImage, state.currentFilterName, true);
          }
        }
        return false;                                                           // allow the event to be redispatched
      }
    };

    state.eventDispatchers.add(keyEventDispatcher);
    getKeyboardFocusManager().addKeyEventDispatcher(keyEventDispatcher);
  }

//------------------------------------------------------------------------------

  /* Returns a keyboard manager where global key event dispatchers can be added
   * upon frame creation and removed upon disposal. */

  private KeyboardFocusManager getKeyboardFocusManager()
  {
    return KeyboardFocusManager.getCurrentKeyboardFocusManager();
  }

//------------------------------------------------------------------------------
//                          FILTER CHANGE
//------------------------------------------------------------------------------

  /* Changes the window title by including the localized name of filter. */

  private void applyFilterToTitle(NamedFilter filter)
  {
    state.currentFilterName   = labelLocalizer.getString(filter.getNameKey());
    MessageFormat titleFormat = new MessageFormat(labelLocalizer.getString(TITLE_PATTERN));
    String title              = titleFormat.format(new Object[] {state.currentFilterName});
    frameAdaptor.setTitle(title);
  }

//------------------------------------------------------------------------------

  /* Changes the window icon by applying filter to the default logo. */

  private void applyFilterToIcon(ImageFilter filter)
  {
    Image logo = iconSupplier.getIcon();
    if (filter != null) logo = filteredImageGenerator.generate(logo, filter);

    frameAdaptor.setIcon(logo);
  }

//------------------------------------------------------------------------------
//                        WINDOW LISTENER
//------------------------------------------------------------------------------

  /** Notifies all the listeners and disposes the frame. */

  @Override
  public void onClosingFrame(FrameAdaptor adaptor)
  {
    for (DisplayFrameCloseListener currentListener : state.listeners) currentListener.onDisplayFrameClosed(this);

    state.eventDispatchers.forEach(getKeyboardFocusManager()::removeKeyEventDispatcher);

    adaptor.dispose();
  }

//------------------------------------------------------------------------------

//******************************************************************************

  private static class State
  {
    final Collection<DisplayFrameCloseListener> listeners = new HashSet<>();

    List<KeyEventDispatcher> eventDispatchers = new LinkedList<>();             // listener of the key combinations used to perform actions on the frame wherever the focus is, to be removed when the frame is disposed
    String currentFilterName = "";
  }

//******************************************************************************

  /** Listener of the window close event, so that any clean up operations other
   *  than the disposal performed internally can be carried out. */

  public static interface DisplayFrameCloseListener
  {
    /** Invoked right before displayFrame is disposed.
     *  <p>
     *  It is not worth the effort to create events for this. */

    void onDisplayFrameClosed(DisplayFrameController displayFrame);
  }

//******************************************************************************

  /* When a double click or a right click is received, the options dialog is opened. */

  private class DisplayPanelClickListener extends MouseAdapter
  {
    private JDialog dialog;

    public DisplayPanelClickListener(JDialog optionsDialog) {dialog = optionsDialog;}

    @Override
    public void mouseClicked(MouseEvent e)
    {
      boolean showDialog = e.getClickCount() > 1 || !SwingUtilities.isLeftMouseButton(e);
      if (showDialog) dialog.setVisible(true);
    }
  }

//******************************************************************************

  /* Transferable to put an image in a clipboard. */

  class ImageSelection implements Transferable
  {
    private Image image;

    public ImageSelection(Image image) {this.image = image;}

    @Override
    public DataFlavor[] getTransferDataFlavors() {return new DataFlavor[] {DataFlavor.imageFlavor};}

    @Override
    public boolean isDataFlavorSupported(DataFlavor flavor) {return DataFlavor.imageFlavor.equals(flavor);}

    @Override
    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException
    {
      if (!DataFlavor.imageFlavor.equals(flavor)) throw new UnsupportedFlavorException(flavor);

      return image;
    }
  }
}
