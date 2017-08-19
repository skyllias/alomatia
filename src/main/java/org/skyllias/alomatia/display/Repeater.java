
package org.skyllias.alomatia.display;

import java.awt.*;
import java.util.*;

import org.skyllias.alomatia.*;

/** Display that only transfers the received images to other displays (receivers). */

public class Repeater implements ImageDisplay
{
  private Collection<ImageDisplay> receivers = new HashSet<>();                 // may be empty but never null
  private Image currentImage;

//==============================================================================

  /** Creates a new repeater without any receiver.
   *  <p>
   *  They can be added later on with {@link #addReceiver(ImageDisplay)}. */

  public Repeater() {}

//------------------------------------------------------------------------------

  /** Creates a new repeater without the passed displays receiving the images this one gets.
   *  <p>
   *  They can be removed later on with {@link #removeReceiver(ImageDisplay)}. */

  public Repeater(Collection<ImageDisplay> initialReceivers)
  {
    if (initialReceivers != null) receivers.addAll(initialReceivers);
  }

//==============================================================================

  /** Resends the image to all the receivers. */

  @Override
  public void setOriginalImage(Image image)
  {
    for (ImageDisplay imageDisplay : receivers)
    {
      imageDisplay.setOriginalImage(image);
    }
    currentImage = image;
  }

//------------------------------------------------------------------------------

  /** Adds the passed ImageDisplay so that it gets the last original image
   *  received (if any) and all others until it is removed. */

  public void addReceiver(ImageDisplay newReceiver)
  {
    if (currentImage != null) newReceiver.setOriginalImage(currentImage);
    receivers.add(newReceiver);
  }

//------------------------------------------------------------------------------

  /** If the passed ImageDisplay is currently receiving images, it is unregistered
   *  so that it does not receive any more unless it is readded in the future. */

  public void removeReceiver(ImageDisplay formerReceiver)
  {
    receivers.remove(formerReceiver);
  }

//------------------------------------------------------------------------------

}
