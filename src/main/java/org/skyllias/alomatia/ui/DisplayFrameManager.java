
package org.skyllias.alomatia.ui;

import java.awt.Rectangle;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.skyllias.alomatia.filter.FilterFactory;
import org.skyllias.alomatia.i18n.LabelLocalizer;
import org.skyllias.alomatia.ui.frame.FrameAdaptor;
import org.skyllias.alomatia.ui.frame.FrameAdaptorFactory;

/** Factory for {@link DisplayFrameController} instances that keeps track of them. */

public class DisplayFrameManager
{
  private FrameAdaptorFactory adaptorFactory;

  private List<DisplayFrameController> existingFrames = Collections.synchronizedList(new LinkedList<DisplayFrameController>());  // every new window is added here in order of creation, and removed when closed. The order is used to apply filters sequentially

  private LabelLocalizer localizer;
  private FilterFactory filterFactory;
  private ImageSaver imageSaver;

  private Collection<DisplayAmountChangeListener> listeners = new LinkedList<>(); // probably there will be just one, but just in case

//==============================================================================

  /** frameFactory is used both when getting DisplayFrame instances and when
   *  rearranging them. */

  public DisplayFrameManager(LabelLocalizer labelLocalizer, FilterFactory aFilterFactory,
                             FrameAdaptorFactory frameFactory, ImageSaver saver)
  {
    localizer      = labelLocalizer;
    filterFactory  = aFilterFactory;
    adaptorFactory = frameFactory;
    imageSaver     = saver;
  }

//------------------------------------------------------------------------------

  /** Just for testing purposes, this should NOT be used in production code. */

  protected DisplayFrameManager(LabelLocalizer labelLocalizer, FilterFactory filterFactory,
                                FrameAdaptorFactory frameFactory, ImageSaver saver,
                                List<DisplayFrameController> displayFrames)
  {
    this(labelLocalizer, filterFactory, frameFactory, saver);

    existingFrames.addAll(displayFrames);
  }

//==============================================================================

  /** Creates a new window and returns it, so that it can be used for example to
   *  register other DisplayFrameCloseListener.
   *  If applySequentialFilter, the nth filter is automatically selected in the
   *  new instance, with n being the amount of DisplayFrames that already existed. */

  public DisplayFrameController createDisplayFrame(boolean applySequentialFilter)
  {
    DisplayPanelController displayPanel = new DisplayPanelController();
    FrameAdaptor frameAdaptor           = adaptorFactory.getNewFrame(displayPanel.getComponent());
    DisplayFrameController frame        = new DisplayFrameController(localizer, frameAdaptor, displayPanel,
                                                                     filterFactory, imageSaver);
    frame.addListener(new DisplayFrameCloseListener());

    if (applySequentialFilter) frame.applyFilterAt(existingFrames.size());

    existingFrames.add(frame);
    notifyListeners();

    return frame;
  }

//------------------------------------------------------------------------------

  /** Returns how many frames instantiated by this manager have not been closed yet. */

  public int getAmountOfOpenDisplayFrames()
  {
    return existingFrames.size();
  }

//------------------------------------------------------------------------------

  /** Registers listener so that it is notified from now on about changes. */

  public void addAmountChangeListener(DisplayAmountChangeListener listener)
  {
    listeners.add(listener);
  }

//------------------------------------------------------------------------------

  /** Resizes and relocates the currently open DisplayFrames so that they cover
   *  as much of the rearrangement bounds offered by the adaptor factory without
   *  overlapping. They are all supposed to exist in the same graphical device,
   *  which will be true (since they have been instantiated by this object)
   *  unless somehow moved externally.
   *  <p>
   *  They are arranged in order along a bidimensional matrix with same-sized
   *  cells and amounfOfLines columns (rows if horizontally is false). If the
   *  amount of windows is not a multiple of amounfOfLines, the last rows (no
   *  matter the value of horizontally) may be incomplete. */

  public void rearrangeWindows(int amountOfLines, boolean horizontally)
  {
    Rectangle screenBounds = adaptorFactory.getRearrengementBounds();

    if (!existingFrames.isEmpty() && amountOfLines > 0 && screenBounds != null)
    {
      int amountOfWindows      = existingFrames.size();
      int amountOfCounterLines = 1 + (amountOfWindows - 1) / amountOfLines;
      int amountOfColumns      = horizontally? amountOfLines: amountOfCounterLines;
      int amountOfRows         = horizontally? amountOfCounterLines: amountOfLines;

      if (screenBounds != null)
      {
        int windowWidth  = screenBounds.width / amountOfColumns;                // some pixels may be lost with roundings
        int windowHeight = screenBounds.height / amountOfRows;
        int currentIndex = 0;
        for (DisplayFrameController currentWindow : existingFrames)
        {
          currentWindow.setMaximized(false);
          currentWindow.setSize(windowWidth, windowHeight);

          int column = currentIndex % amountOfColumns;
          int row    = currentIndex / amountOfColumns;
          int x      = screenBounds.x + column * windowWidth;
          int y      = screenBounds.y + row * windowHeight;
          currentWindow.setLocation(x, y);

          currentIndex++;
        }
      }
    }
  }

//------------------------------------------------------------------------------

  /** Changes the selected filter in each of the currently open DisplayFrames so
   *  that the first window gets the first filter and so on.
   *  <p>
   *  If there are more windows than filters, the last ones may not be changed.
   *  However, that is something outside this manager's control. */

  public void applySequentialFilters()
  {
    int currentIndex = 0;
    for (DisplayFrameController currentWindow : existingFrames) currentWindow.applyFilterAt(currentIndex++);
  }

//------------------------------------------------------------------------------

  /* Invokes onAmountChanged on all registered listeners.  */

  private void notifyListeners()
  {
    for (DisplayAmountChangeListener currentListener : listeners)
    {
      currentListener.onAmountChanged(this);
    }
  }

//------------------------------------------------------------------------------

//******************************************************************************

  /** Listener of changes of the amount of display frames open, either due to
   *  creation or closing. */

  public static interface DisplayAmountChangeListener
  {
    /** Invoked when the amount of open display frames is modified in the passed manager. */

    void onAmountChanged(DisplayFrameManager manager);
  }

//******************************************************************************

  /** DisplayFrameCloseListener that removes a window from existingFrames when it is closed. */

  private class DisplayFrameCloseListener implements DisplayFrameController.DisplayFrameCloseListener
  {
    @Override
    public void onDisplayFrameClosed(DisplayFrameController displayFrame)
    {
      existingFrames.remove(displayFrame);
      notifyListeners();
    }
  }
}
