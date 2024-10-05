
package org.skyllias.alomatia.source.screen;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.image.BufferedImage;

import org.skyllias.alomatia.source.ScreenSource;
import org.skyllias.alomatia.source.ScreenSource.ScreenRectangle;
import org.springframework.stereotype.Component;

/** Provider of screen captures by means of a {@link Robot}.
 *  Extracted from {@link ScreenSource} for testability purposes. */

@Component
public class ScreenCapturer
{
//==============================================================================

  /** Returns a screenshot containing the region screenRectangle.getBounds()
   *  from the screenRectangle.getDevice() device.
   *  This method takes the overhead of instantiating a {@link Robot}, but
   *  from experience this represents a very small amount compared to the
   *  capture itself. If it were not the case, then some opaque context should
   *  be generated in another method and then passed to this one.
   *  An exception could be thrown if the application does not have permissions
   *  to instantiate a {@link Robot}. */

  public BufferedImage capture(ScreenRectangle screenRectangle) throws AWTException
  {
    Robot robot = new Robot(screenRectangle.getDevice());
    return robot.createScreenCapture(screenRectangle.getBounds());
  }

//------------------------------------------------------------------------------

}
