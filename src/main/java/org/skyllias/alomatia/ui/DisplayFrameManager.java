
package org.skyllias.alomatia.ui;

import java.awt.*;
import java.util.List;
import java.util.*;

import org.skyllias.alomatia.filter.*;
import org.skyllias.alomatia.i18n.*;

/** Factory for {@link DisplayFrame} instances that keeps track of them. */

public class DisplayFrameManager
{
  private List<DisplayFrame> existingFrames = Collections.synchronizedList(new LinkedList<DisplayFrame>());  // every new window is added here in order of creation, and removed when closed

  private LabelLocalizer localizer;
  private FilterFactory filterFactory;

  private Collection<DisplayAmountChangeListener> listeners = new LinkedList<>(); // probably there will be just one, but just in case

//==============================================================================

  public DisplayFrameManager(LabelLocalizer labelLocalizer, FilterFactory factory)
  {
    localizer     = labelLocalizer;
    filterFactory = factory;
  }

//------------------------------------------------------------------------------

  /** Just for testing purposes, this should NOT be used in real code. */

  protected DisplayFrameManager(LabelLocalizer labelLocalizer, FilterFactory factory,
                                List<DisplayFrame> displayFrames)
  {
    this(labelLocalizer, factory);

    existingFrames.addAll(displayFrames);
  }

//==============================================================================

  /** Creates a new window and returns it, so that it can be used for example to
   * register other DisplayFrameCloseListener. */

  public DisplayFrame getNewDisplayFrame()
  {
    DisplayPanel displayPanel = new DisplayPanel();
    DisplayFrame frame        = new DisplayFrame(localizer, displayPanel, filterFactory);
    frame.addListener(new DisplayFrameCloseListener());

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
   *  as much of screenBounds without overlapping. They are all supposed to exist
   *  in the same graphical device, which will be true (since they have been
   *  instantiated by this object) unless somehow moved externally.
   *  <p>
   *  They are arranged in order along a bidimensional matrix with same-sized
   *  cells and amounfOfLines columns (rows if horizontally is false). If the
   *  amount of windows is not a multiple of amounfOfLines, the last rows (no
   *  matter the value of horizontally) may be incomplete. */

  public void rearrangeWindows(int amountOfLines, Rectangle screenBounds, boolean horizontally)
  {
    if (!existingFrames.isEmpty() && amountOfLines > 0)
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
        for (DisplayFrame currentWindow : existingFrames)
        {
          currentWindow.setExtendedState(Frame.NORMAL);
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

  private class DisplayFrameCloseListener implements DisplayFrame.DisplayFrameCloseListener
  {
    @Override
    public void onDisplayFrameClosed(DisplayFrame displayFrame)
    {
      existingFrames.remove(displayFrame);
      notifyListeners();
    }
  }
}
