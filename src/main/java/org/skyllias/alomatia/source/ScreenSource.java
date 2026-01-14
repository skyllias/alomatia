
package org.skyllias.alomatia.source;

import java.awt.Color;
import java.awt.GraphicsDevice;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import org.skyllias.alomatia.ImageDisplay;
import org.skyllias.alomatia.ImageSource;
import org.skyllias.alomatia.source.screen.MouseLocator;
import org.skyllias.alomatia.source.screen.PointerData;
import org.skyllias.alomatia.source.screen.ScreenCapturer;
import org.springframework.stereotype.Component;

/** Source of screenshots.
 *  <p>
 *  For it to produce images, the portion of screen to take them from must be
 *  provided first by means of {@link #setScreenBounds(ScreenRectangle)}.
 *
 *  The capture of each screenshot is driven externally by calls to capture(). */

@Component
public class ScreenSource implements ImageSource
{
  private static final boolean showPointer = true;                              // if true, a pointer is added to the image right over the position of the mouse on the source. Someday this could be externally set

  private final ImageDisplay imageDisplay;
  private final ScreenCapturer screenCapturer;
  private final MouseLocator mouseLocator;

  private final State state = new State();

//==============================================================================

  /** Creates a new instance ready to get the display and screen bundle set
   *  before producing images. */

  public ScreenSource(ImageDisplay imageDisplay, ScreenCapturer screenCapturer,
                      MouseLocator mouseLocator)
  {
    this.imageDisplay   = imageDisplay;
    this.screenCapturer = screenCapturer;
    this.mouseLocator   = mouseLocator;
  }

//==============================================================================

  @Override
  public void setActive(boolean active) {state.active = active;}

//------------------------------------------------------------------------------

  /** Sets the device from which captures are to be taken and the rectangle of
   *  it that are to be taken in each capture. */

  public void setScreenBounds(ScreenRectangle screenRectangle)
  {
    state.currentScreenRectangle = screenRectangle;
  }

//------------------------------------------------------------------------------

  /** If there is a display and a device set, a new screenshot of the current
   *  bounds is taken and passed to it. */

  public void capture()
  {
    if (state.active && state.currentScreenRectangle != null)
    {
      try
      {
        PointerData pointerInfo     = mouseLocator.getMouseInfo();              // this is taken before the capture because it is expected to be faster, but probably there would be no difference
        BufferedImage capturedImage = screenCapturer.capture(state.currentScreenRectangle);
        if (pointerInfo != null)
        {
          boolean sameDevice = pointerInfo.getDevice().equals(state.currentScreenRectangle.device);
          if (sameDevice) paintMousePointer(capturedImage, state.currentScreenRectangle.bounds,
                                            pointerInfo.getLocation());
        }

        imageDisplay.setOriginalImage(capturedImage);
      }
      catch (Exception e) {e.printStackTrace();}                                // screnshots are not allowed. TODO log it
    }
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

    public Rectangle getBounds() {return bounds;}
  }

//******************************************************************************

  private static class State
  {
    boolean active;
    ScreenRectangle currentScreenRectangle;
  }
}
