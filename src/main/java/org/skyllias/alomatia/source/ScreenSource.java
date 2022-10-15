
package org.skyllias.alomatia.source;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.GraphicsDevice;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.Timer;

import org.skyllias.alomatia.ImageDisplay;
import org.skyllias.alomatia.ImageSource;
import org.springframework.stereotype.Component;

/** Source of periodic screenshots.
 *  <p>
 *  For it to produce images, the portion of screen to take them from must be
 *  provided first by means of {@link #setScreenBounds(ScreenRectangle)}.
 *  The speed at which they are produced can be tuned externally too by means
 *  of {@link #setFrequency(int)}. */

@Component
public class ScreenSource implements ImageSource, ActionListener
{
  private static final int DEFAULT_PERIOD_MS = 40;                              // slow enough for old machines to work, fast enough for an active refreshment

  private static final boolean showPointer = true;                              // if true, a pointer is added to the image right over the position of the mouse on the source. Someday this could be externally set

  private final ImageDisplay imageDisplay;
  private final Timer captureTimer;

  private Rectangle sourceRectangle = new Rectangle(0, 0, 450, 700);
  private GraphicsDevice graphDevice;                                           // the graphics device from which captures must be taken
  private Robot captureRobot;

//==============================================================================

  /** Creates a new instance ready to get the display and screen bundle set
   *  before producing images. */

  public ScreenSource(ImageDisplay imageDisplay)
  {
    this.imageDisplay = imageDisplay;

    captureTimer = new Timer(DEFAULT_PERIOD_MS, this);
  }

//==============================================================================

  /** Starts or stops the capture timer. */

  @Override
  public void setActive(boolean active)
  {
    if (active) captureTimer.start();
    else        captureTimer.stop();
  }

//------------------------------------------------------------------------------

  @Override
  public void setDisplay(ImageDisplay display) {}

//------------------------------------------------------------------------------

  /** Sets the amount of milliseconds between screenshots.
   *  <p>
   *  The amount should be greater than 0.
   *  <p>
   *  The name is a little misleading because the frequency is the inverse of
   *  the period, but it is a more usual word. */

  public void setFrequency(int millis) {captureTimer.setDelay(millis);}

//------------------------------------------------------------------------------

  /** Sets the device from which captures are to be taken and the rectangle of
   *  it that are to be taken in each capture.
   *  <p>
   *  If the device is null, the default screen device is used.
   * @throws AWTException seldom, since it should have been thrown in the constructor. */

  public void setScreenBounds(ScreenRectangle screenRectangle) throws AWTException
  {
    setScreenBounds(screenRectangle.bounds);
    setDevice(screenRectangle.device);
  }

//------------------------------------------------------------------------------

  /** If there is a display and a device set, a new screenshot of the current
   *  bounds is taken and passed to it.
   *  Expected to be invoked by a timer. */

  @Override
  public void actionPerformed(ActionEvent event)
  {
    if (graphDevice != null)                                                    // this also ensures that captureRobot != null
    {
      PointerInfo pointerInfo     = MouseInfo.getPointerInfo();                 // this is taken before the capture because it is expected to be faster, but probably there would be no difference
      BufferedImage capturedImage = captureRobot.createScreenCapture(sourceRectangle);
      if (pointerInfo != null)
      {
        boolean sameDevice = pointerInfo.getDevice().equals(graphDevice);
        if (sameDevice) paintMousePointer(capturedImage, sourceRectangle,
                                          pointerInfo.getLocation());
      }

      imageDisplay.setOriginalImage(capturedImage);
    }
  }

//------------------------------------------------------------------------------

  /* Sets the bounds of screen that are to be taken in each capture, keeping
   * the same graphics device. */

  private void setScreenBounds(Rectangle rectangle) {sourceRectangle = rectangle;}

//------------------------------------------------------------------------------

  /* Sets the passed device and a new robot instance for it. */

  private void setDevice(GraphicsDevice device) throws AWTException
  {
    graphDevice = device;

    captureRobot = new Robot(graphDevice);
  }

//------------------------------------------------------------------------------

  /* If the pointer is to be shown and the mouse is over the image, a mark is
   * added to the passed image on the corresponding point.
   * The bounds do not refer the image but the point, since a translation must be
   * carried out to know where the point really is.
   * The system's pointer is not used to avoid confusion. */

  private void paintMousePointer(BufferedImage image, Rectangle captureBounds, Point point)
  {
    if (showPointer)
    {
      if (captureBounds.contains(point))                                        // the mouse is on the captured region
      {
        point.translate(-captureBounds.x, -captureBounds.y);
        drawMousePointer(image, point);
      }
    }
  }

//------------------------------------------------------------------------------

  /* Draws a pointer on the passed point of the image.
   * Instead of using the system's pointer, a few pixels are modified with the
   * basic colours.
   * When the pointer is next to the edge, the pixels that would fall outside
   * the image bounds are controlled to prevent ArrayIndexOutOfBoundsExceptions. */

  private void drawMousePointer(BufferedImage image, Point point)
  {
    try {image.setRGB(point.x + 0, point.y - 1, Color.WHITE.getRGB());} catch (ArrayIndexOutOfBoundsException aioobe) {}
    try {image.setRGB(point.x + 0, point.y - 2, Color.WHITE.getRGB());} catch (ArrayIndexOutOfBoundsException aioobe) {}
    try {image.setRGB(point.x + 1, point.y - 1, Color.WHITE.getRGB());} catch (ArrayIndexOutOfBoundsException aioobe) {}
    try {image.setRGB(point.x + 1, point.y - 2, Color.WHITE.getRGB());} catch (ArrayIndexOutOfBoundsException aioobe) {}
    try {image.setRGB(point.x - 1, point.y + 0, Color.RED.getRGB());}   catch (ArrayIndexOutOfBoundsException aioobe) {}
    try {image.setRGB(point.x - 1, point.y + 1, Color.RED.getRGB());}   catch (ArrayIndexOutOfBoundsException aioobe) {}
    try {image.setRGB(point.x - 2, point.y + 0, Color.RED.getRGB());}   catch (ArrayIndexOutOfBoundsException aioobe) {}
    try {image.setRGB(point.x - 2, point.y + 1, Color.RED.getRGB());}   catch (ArrayIndexOutOfBoundsException aioobe) {}
    try {image.setRGB(point.x + 0, point.y + 0, Color.GREEN.getRGB());} catch (ArrayIndexOutOfBoundsException aioobe) {}
    try {image.setRGB(point.x + 0, point.y + 1, Color.GREEN.getRGB());} catch (ArrayIndexOutOfBoundsException aioobe) {}
    try {image.setRGB(point.x + 1, point.y + 0, Color.GREEN.getRGB());} catch (ArrayIndexOutOfBoundsException aioobe) {}
    try {image.setRGB(point.x + 1, point.y + 1, Color.GREEN.getRGB());} catch (ArrayIndexOutOfBoundsException aioobe) {}
    try {image.setRGB(point.x + 2, point.y + 0, Color.BLUE.getRGB());}  catch (ArrayIndexOutOfBoundsException aioobe) {}
    try {image.setRGB(point.x + 2, point.y + 1, Color.BLUE.getRGB());}  catch (ArrayIndexOutOfBoundsException aioobe) {}
    try {image.setRGB(point.x + 3, point.y + 0, Color.BLUE.getRGB());}  catch (ArrayIndexOutOfBoundsException aioobe) {}
    try {image.setRGB(point.x + 3, point.y + 1, Color.BLUE.getRGB());}  catch (ArrayIndexOutOfBoundsException aioobe) {}
    try {image.setRGB(point.x + 0, point.y + 2, Color.BLACK.getRGB());} catch (ArrayIndexOutOfBoundsException aioobe) {}
    try {image.setRGB(point.x + 0, point.y + 3, Color.BLACK.getRGB());} catch (ArrayIndexOutOfBoundsException aioobe) {}
    try {image.setRGB(point.x + 1, point.y + 2, Color.BLACK.getRGB());} catch (ArrayIndexOutOfBoundsException aioobe) {}
    try {image.setRGB(point.x + 1, point.y + 3, Color.BLACK.getRGB());} catch (ArrayIndexOutOfBoundsException aioobe) {}
  }

//------------------------------------------------------------------------------

//******************************************************************************

  /** Holder of a graphics device and a rectangle representing a region from the
   *  screen that must be captured. */

  public static class ScreenRectangle
  {
    private GraphicsDevice device;
    private Rectangle bounds;

    public ScreenRectangle(GraphicsDevice screenDevice, Rectangle screenBounds)
    {
      device = screenDevice;
      bounds = screenBounds;
    }

    public GraphicsDevice getDevice() {return device;}

    public Rectangle getBonuds() {return bounds;}
  }
}
