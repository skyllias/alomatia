
package org.skyllias.alomatia.display;

import java.awt.Image;
import java.util.Collection;
import java.util.HashSet;

import org.skyllias.alomatia.ImageDisplay;
import org.springframework.stereotype.Component;

/** Stateful display that simply transfers the received images to other displays (receivers). */

@Component
public class Repeater implements ImageDisplay
{
  private final State state = new State();

//==============================================================================

  /** Resends the image to all the receivers. */

  @Override
  public void setOriginalImage(Image image)
  {
    for (ImageDisplay imageDisplay : state.receivers)
    {
      imageDisplay.setOriginalImage(image);
    }
    state.currentImage = image;
  }

//------------------------------------------------------------------------------

  /** Adds the passed ImageDisplay so that it gets the last original image
   *  received (if any) and all others until it is removed. */

  public void addReceiver(ImageDisplay newReceiver)
  {
    if (state.currentImage != null) newReceiver.setOriginalImage(state.currentImage);
    state.receivers.add(newReceiver);
  }

//------------------------------------------------------------------------------

  /** If the passed ImageDisplay is currently receiving images, it is unregistered
   *  so that it does not receive any more unless it is readded in the future. */

  public void removeReceiver(ImageDisplay formerReceiver)
  {
    state.receivers.remove(formerReceiver);
  }

//------------------------------------------------------------------------------

  private static class State
  {
    final Collection<ImageDisplay> receivers = new HashSet<>();                 // may be empty
    Image currentImage;                                                         // kept to be able to pass it to new receivers
  }
}
