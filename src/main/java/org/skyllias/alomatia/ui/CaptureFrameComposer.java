
package org.skyllias.alomatia.ui;

import java.awt.Button;
import java.awt.GraphicsDevice;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

import org.skyllias.alomatia.i18n.LabelLocalizer;
import org.skyllias.alomatia.logo.IconSupplier;
import org.skyllias.alomatia.preferences.SourcePreferences;
import org.skyllias.alomatia.source.ScreenSource.ScreenRectangle;
import org.springframework.stereotype.Component;

/** Opener of frames used to select a region to capture images from. */

@Component
public class CaptureFrameComposer
{
  private static final String TITLE        = "capture.window.title";
  private static final String BUTTON_LABEL = "capture.button.text";

  private final SourcePreferences sourcePreferences;
  private final LabelLocalizer labelLocalizer;
  private final IconSupplier iconSupplier;

//==============================================================================

  public CaptureFrameComposer(SourcePreferences sourcePreferences,
                              LabelLocalizer localizer,
                              IconSupplier iconSupplier)
  {
    this.sourcePreferences = sourcePreferences;
    labelLocalizer         = localizer;
    this.iconSupplier      = iconSupplier;
  }

//==============================================================================

  /** Returns a new visible frame that will notify listener when the user
   *  chooses the region to capture from.
   *  The returned frame has already been set up (icon, size, etc.) and
   *  therefore most times it is not expected to be used by the caller. */

  public JFrame openNewFrame(CaptureBoundsListener listener)
  {
    JFrame frame = new JFrame(labelLocalizer.getString(TITLE));

    frame.setLocationRelativeTo(null);

    Rectangle initialRectangle = sourcePreferences.getScreenRectangle();
    frame.setLocation(initialRectangle.x, initialRectangle.y);
    frame.setSize(initialRectangle.width, initialRectangle.height);

    frame.setIconImage(iconSupplier.getIcon());

    Button captureButton = new Button(labelLocalizer.getString(BUTTON_LABEL));
    captureButton.addActionListener(new CaptureButtonListener(frame, listener));
    frame.add(captureButton);
    frame.setVisible(true);

    return frame;
  }

//------------------------------------------------------------------------------

//******************************************************************************

  /** Listener to be invoked when the user finishes the region selection,
   *  receiving the capture bounds. */

  public static interface CaptureBoundsListener
  {
    void boundsSelected(ScreenRectangle bounds);
  }

//******************************************************************************

  /* Listener to be invoked when the button is clicked, notifying the listener
   * and disposing the frame. */

  private class CaptureButtonListener implements ActionListener
  {
    private JFrame frame;
    private CaptureBoundsListener boundsListener;

    public CaptureButtonListener(JFrame jFrame, CaptureBoundsListener listener)
    {
      frame          = jFrame;
      boundsListener = listener;
    }

    @Override
    public void actionPerformed(ActionEvent event)
    {
      ScreenRectangle screenRectangle = getCaptureBounds();
      boundsListener.boundsSelected(screenRectangle);
      frame.dispose();                                                          // close the window and release its system resources

      sourcePreferences.setScreenRectangle(screenRectangle.getBounds());
    }

    /* Returns the screen rectangle from which captures should take place. */

    private ScreenRectangle getCaptureBounds()
    {
      GraphicsDevice device   = frame.getGraphicsConfiguration().getDevice();
      Rectangle currentBounds = frame.getBounds();
      return new ScreenRectangle(device, currentBounds);
    }

  }

}
