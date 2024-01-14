
package org.skyllias.alomatia.source;

import java.awt.Image;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.FlavorEvent;
import java.awt.datatransfer.FlavorListener;
import java.awt.datatransfer.StringSelection;

import org.skyllias.alomatia.ImageDisplay;
import org.skyllias.alomatia.ImageSource;
import org.springframework.stereotype.Component;

/** Source that extracts images from the system's clipboard. If the clipboard
 *  does not contain an image, nothing is generated.
 *  <p>
 *  There are two modes, auto and non-auto. The first is the default and listens
 *  for changes in the clipboard, generating a new image when it is copied to
 *  the clipboard without active polling. Due to the limitations of clipboard
 *  listeners, the clipboard is cleared. The second only reads the clipboard
 *  when the method {@link #readFromClipboard()} is invoked from outside. */

@Component
public class ClipboardSource implements ImageSource, FlavorListener
{
  private final ImageDisplay imageDisplay;
  private final Clipboard clipboard;

  private final State state = new State();

//==============================================================================

  public ClipboardSource(ImageDisplay imageDisplay, Clipboard clipboard)
  {
    this.imageDisplay = imageDisplay;
    this.clipboard    = clipboard;
  }

//==============================================================================

  /** Apart from the usual things, adds or removes itself as clipboard listener. */

  @Override
  public void setActive(boolean active)
  {
    state.active = active;

    if (active)
    {
      if (state.autoMode) takeImageFromClipboard(clipboard);
      clipboard.addFlavorListener(this);
    }
    else clipboard.removeFlavorListener(this);
  }

//------------------------------------------------------------------------------

  /** If the mode is auto and the source is active, reads the contents of the
   *  clipboard in search of an image. Else, nothing happens.
   *  To be invoked when the clipboard contents change. The clipboard used is
   *  the one in event. It should be the same used internally to register as
   *  listener, but it is not enforced. */

  @Override
  public void flavorsChanged(FlavorEvent event)
  {
    if (state.autoMode && state.active) takeImageFromClipboard((Clipboard) event.getSource());
  }

//------------------------------------------------------------------------------

  /** If the mode is not auto and the source is active, reads the contents of
   *  the clipboard in search of an image. Else, nothing happens.
   *  To be invoked when the user wants to paste the image from the clipboard. */

  public void readFromClipboard()
  {
    if (!state.autoMode && state.active) takeImageFromClipboard(clipboard);
  }

//------------------------------------------------------------------------------

  /** Modifies the mode of the source, switching auto on or off.
   *  If true, the clipboard is listened to for changes, and images are removed
   *  after copy; else, the clipboard is only read upon external invocation.
   *  This method can be called any time. */

  public void setAutoMode(boolean auto) {state.autoMode = auto;}

//------------------------------------------------------------------------------

  /* If an image is available in the clipboard, it is sent to the display.
   * Else, nothing happens.
   * When an image is found and mode is auto, the clipboard is cleared by
   * copying an empty string to it. This provokes the loss of the image from the
   * clipboard, but unfortunately a contents change listener does not exist,
   * only the flavour change listener. By copying a string to the clipboard, the
   * next time an image is copied it will be detected and displayed. Other
   * alternatives could be polling or taking ownership of the clipboard, but
   * that can lead to a war if any other application is also regaining ownership
   * automatically. If mode is not auto, the clipboard is left untouched. */

  private void takeImageFromClipboard(Clipboard clipboard)
  {
    final String EMPTY_TEXT = "";

    try
    {
      Image clipboardImage = (Image) clipboard.getData(DataFlavor.imageFlavor);
      if (clipboardImage != null) imageDisplay.setOriginalImage(clipboardImage);

      if (state.autoMode) clipboard.setContents(new StringSelection(EMPTY_TEXT), null);
    }
    catch (Exception e) {}                                                      // the clipboard may not contain an image, so do nothing then
  }

//------------------------------------------------------------------------------

//******************************************************************************

  private static class State
  {
    boolean active;
    boolean autoMode;                                                           // see setAutoMode
  }
}
