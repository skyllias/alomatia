
package org.skyllias.alomatia.ui;

import java.awt.*;
import java.awt.event.*;

import org.skyllias.alomatia.i18n.*;
import org.skyllias.alomatia.source.ScreenSource.*;

/** Transparent window where input images are taken from.
 *  <p>
 *  Support for translucency is assumed. It should be checked previously by
 *  means of GraphicsEnvironment.getLocalGraphicsEnvironment().
 *  <p>
 *  When the screen region to capture is selected and the button is clicked,
 *  the CaptureBoundsListener received by the constructor gets the bounds and
 *  the windows is closed.
 *  @deprecated Use CaptureFrameComposer instead. */

@Deprecated
@SuppressWarnings("serial")
public class CaptureFrame extends BasicAlomatiaWindow
{
  private static final String TITLE        = "capture.window.title";
  private static final String BUTTON_LABEL = "capture.button.text";

//  private static final float OPACITY = 0.3f;                                    // TODO this should be 0, but Lubuntu does not seem to support transparency, so the window will have to be opaque to set its boundaries and then hide when beginning to capture

//==============================================================================

  public CaptureFrame(LabelLocalizer localizer, CaptureBoundsListener listener)
  {
    super(localizer, TITLE);

    setLocationRelativeTo(null);

    setSize(400, 300);

//    setUndecorated(true);                                                       // required for setOpacity to work
//    setOpacity(OPACITY);

    Button captureButton = new Button(getLabelLocalizer().getString(BUTTON_LABEL));
    captureButton.addActionListener(new CaptureButtonListener(listener));
    add(captureButton);
    pack();
    setVisible(true);
  }

//==============================================================================

  /** Returns the screen rectangle from which captures should take place. */

  private ScreenRectangle getCaptureBounds()
  {
    GraphicsDevice device   = getGraphicsConfiguration().getDevice();
    Rectangle currentBounds = getBounds();
    return new ScreenRectangle(device, currentBounds);
  }

//------------------------------------------------------------------------------

//******************************************************************************

  /** Listener to be invoked when the button is clicked, receiving the capture bounds. */

  public static interface CaptureBoundsListener
  {
    void boundsSelected(ScreenRectangle bounds);
  }

//******************************************************************************

  /* Listener to be invoked when the button is clicked, receiving the capture bounds. */

  private class CaptureButtonListener implements ActionListener
  {
    private CaptureBoundsListener boundsListener;

    public CaptureButtonListener(CaptureBoundsListener listener) {boundsListener = listener;}

    @Override
    public void actionPerformed(ActionEvent event)
    {
      ScreenRectangle screenRectangle = getCaptureBounds();
      boundsListener.boundsSelected(screenRectangle);
      dispose();                                                                // close the window and release its system resources
    }
  }

}
