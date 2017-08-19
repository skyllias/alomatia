
package org.skyllias.alomatia.source;

import java.awt.*;

import org.skyllias.alomatia.*;

/** Superclass for most source implementations. */

public abstract class BasicSource implements ImageSource
{
  private ImageDisplay imageDisplay;
  private boolean isActive = false;

//==============================================================================

  @Override
  public void setDisplay(ImageDisplay display) {imageDisplay = display;}
  protected ImageDisplay getDisplay() {return imageDisplay;}

//------------------------------------------------------------------------------

  @Override
  public void setActive(boolean active) {isActive = active;}
  protected boolean isActive() {return isActive;}

//------------------------------------------------------------------------------

  /** If the display is not null, the passed image is sent to it.
   *  Else, nothing happens.
   *  <p>
   *  Helper method for subclasses, to avoid carrying repeated checks. */

  protected void sendImageToDisplay(Image image)
  {
    if (imageDisplay != null && image != null) imageDisplay.setOriginalImage(image);
  }

//------------------------------------------------------------------------------

}
