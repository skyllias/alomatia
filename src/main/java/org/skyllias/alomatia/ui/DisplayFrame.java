
package org.skyllias.alomatia.ui;

import java.awt.*;
import java.awt.datatransfer.*;
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

  private LabelLocalizer labelLocalizer;

  private DisplayPanel displayPanel;

  private Collection<DisplayFrameCloseListener> listeners = new HashSet<>();
  private FilterSelectorComposer filterSelector;                                // the selector from the associated DisplayOptionsDialog, used to set the selected filter externally

  private FrameAdaptor frameAdaptor;                                            // the Swing component with the frame

  private Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
  private ImageSaver imageSaver;
  private KeyEventDispatcher saveAllEventDispatcher;                            // listener of the key combinations used to save all images, to be removed when the frame is disposed
  private String currentFilterName = "";

//==============================================================================

  /** Creates a new window containing the passed display panel. */

  public DisplayFrame(LabelLocalizer localizer, FrameAdaptor adaptor,
                      DisplayPanel panel, FilterFactory filterFactory, ImageSaver saver)
  {
    labelLocalizer = localizer;
    displayPanel   = panel;
    frameAdaptor   = adaptor;
    imageSaver     = saver;

    frameAdaptor.setTitle(labelLocalizer.getString(DEFAULT_TITLE));
    frameAdaptor.setIcon(getDefaultLogo());

    displayPanel.getComponent().setToolTipText(labelLocalizer.getString(PANEL_TOOLTIP));

    frameAdaptor.addClosingFrameListener(this);

    filterSelector = new FilterSelectorComposer(labelLocalizer, this, filterFactory);
    setUpFilterKeyListeners(filterSelector);
    setOutputKeyListeners();

    frameAdaptor.setMaximized(false);
    frameAdaptor.setVisible(true);

    DisplayOptionsDialogComposer dialogComposer = new DisplayOptionsDialogComposer(labelLocalizer,
                                                                                   this, filterSelector);
    JDialog optionsDialog                       = dialogComposer.getDialog();
    displayPanel.getComponent().addMouseListener(new DisplayPanelClickListener(optionsDialog));
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

  /** Sets the clipboard where filtered images are to be copied.
   *  This method should only be called in tests. */

  protected void setClipboard(Clipboard clip) {clipboard = clip;}

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

  private void setUpFilterKeyListeners(FilterSelectorComposer filterSelector)
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

  private void setUpNumberKeyListeners(final FilterSelectorComposer filterSelector,
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
        if (filteredImage != null) imageSaver.save(filteredImage, currentFilterName, false);
      }
    });
  }

//------------------------------------------------------------------------------

  /* Sets up the key event handlet to save the filtered image from the display panel.
   * Ctrl + Shift + S is used to silently save all the frames' images.
   * The listener should be removed when the frame is disposed. */

  private void setSaveAllKeyListeners()
  {
    saveAllEventDispatcher = new KeyEventDispatcher()
    {
      @Override
      public boolean dispatchKeyEvent(KeyEvent e)
      {
        if(e.getID() == KeyEvent.KEY_PRESSED && EventUtils.isControlDown(e) && e.isShiftDown())
        {
          if (e.getKeyCode() == KeyEvent.VK_S)
          {
            Image filteredImage = displayPanel.getFilteredImage();
            if (filteredImage != null) imageSaver.save(filteredImage, currentFilterName, true);
          }
        }
        return false;                                                           // allow the event to be redispatched
      }
    };

    getKeyboardFocusManager().addKeyEventDispatcher(saveAllEventDispatcher);
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
    currentFilterName         = labelLocalizer.getString(filter.getNameKey());
    MessageFormat titleFormat = new MessageFormat(labelLocalizer.getString(TITLE_PATTERN));
    String title              = titleFormat.format(new Object[] {currentFilterName});
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

    getKeyboardFocusManager().removeKeyEventDispatcher(saveAllEventDispatcher);
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
