
package org.skyllias.alomatia.source;

import java.awt.*;
import java.awt.datatransfer.*;

/** Source that extracts images from the system's clipboard. If the clipboard
 *  does not contain an image, nothing is generated.
 *  <p>
 *  There are two modes, auto and non-auto. The first is the default and listens
 *  for changes in the clipboard, generating a new image when it is copied to
 *  the clipboard without active polling. Due to the limitations of clipboard
 *  listeners, the clipboard is cleared. The second only reads the clipboard
 *  when the method {@link #readFromClipboard()} is invoked from outside. */

public class ClipboardSource extends BasicSource
                             implements FlavorListener
{
  private Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

  private boolean autoMode = true;                                              // see setAutoMode

//==============================================================================

  /** Apart from the usual things, adds or removes itself as clipboard listener. */

  @Override
  public void setActive(boolean active)
  {
    super.setActive(active);

    if (active)
    {
      if (autoMode) takeImageFromClipboard(clipboard);
      clipboard.addFlavorListener(this);
    }
    else clipboard.removeFlavorListener(this);
  }

//------------------------------------------------------------------------------

  /** If the mode is auto, reads the contents of the clipboard in search of an
   *  image. Else, nothing happens.
   *  To be invoked when the clipboard contents change. The clipboard used is
   *  the one in event. It sohlud be the same used internally to register as
   *  listener, but it is not enforced. */

  @Override
  public void flavorsChanged(FlavorEvent event)
  {
    if (autoMode) takeImageFromClipboard((Clipboard) event.getSource());
  }

//------------------------------------------------------------------------------

  /** If the mode is not auto, reads the contents of the clipboard in search of
   *  an image. Else, nothing happens.
   *  To be invoked when the user wants to paste the image from the clipboard. */

  public void readFromClipboard()
  {
    if (!autoMode) takeImageFromClipboard(clipboard);
  }

//------------------------------------------------------------------------------

  /** Modifies the mode of the source, switching auto on or off.
   *  If true, the clipboard is listened to for changes, and images are removed
   *  after copy; else, the clipboard is only read upon external invocation.
   *  This method can be called any time. */

  public void setAutoMode(boolean auto) {autoMode = auto;}

//------------------------------------------------------------------------------

  /** Modifies the clipboard used to take images from when the source is activated.
   *  This only needs to be called when testing. */

  protected void setClipboard(Clipboard newClipboard) {clipboard = newClipboard;}

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
      sendImageToDisplay(clipboardImage);

      if (autoMode) clipboard.setContents(new StringSelection(EMPTY_TEXT), null);
    }
    catch (Exception e) {}                                                      // the clipboard may not contain an image, so do nothing then
  }

//------------------------------------------------------------------------------

}
