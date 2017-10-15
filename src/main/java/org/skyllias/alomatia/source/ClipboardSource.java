
package org.skyllias.alomatia.source;

import java.awt.*;
import java.awt.datatransfer.*;

/** Source that extracts images from the system's clipboard. If the clipboard
 *  does not contain an image, nothing is generated.
 *  <p>
 *  This listens for changes in the clipboard, generating a new image when it is
 *  copied to the clipboard without active polling. */

public class ClipboardSource extends BasicSource
                             implements FlavorListener
{
  private Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

//==============================================================================

  /* Adds or removes itself as clipboard listener. */

  @Override
  public void setActive(boolean active)
  {
    super.setActive(active);

    if (active)
    {
      takeImageFromClipboard(clipboard);
      clipboard.addFlavorListener(this);
    }
    else clipboard.removeFlavorListener(this);
  }

//------------------------------------------------------------------------------

  /** Reads the contents of the clipboard in search of an image.
   *  To be invoked when the clipboard contents change. */

  @Override
  public void flavorsChanged(FlavorEvent e)
  {
    takeImageFromClipboard((Clipboard) e.getSource());
  }

//------------------------------------------------------------------------------

  /** Modifies the clipboard used to take images from when the source is activated.
   *  This only needs to be called when testing. */

  protected void setClipboard(Clipboard newClipboard) {clipboard = newClipboard;}

//------------------------------------------------------------------------------

  /** If an image is available in the clipboard, it is sent to the display.
   *  Else, nothing happens.
   *  <p>
   *  When an image is found, the clipboard is cleared by copying an empty string
   *  to it. This provokes the loss of the image from the clipboard, but
   *  unfortunately a contents change listener does not exist, only the flavour
   *  change listener. By copying a string to the clipboard, the next time an
   *  image is copied it will be detected and displayed. Other alternatives could
   *  be polling or taking ownership of the clipboard, but that can lead to a
   *  war if any other application is also regaining ownership automatically. */

  private void takeImageFromClipboard(Clipboard clipboard)
  {
    final String EMPTY_TEXT = "";

    try
    {
      Image clipboardImage = (Image) clipboard.getData(DataFlavor.imageFlavor);
      sendImageToDisplay(clipboardImage);

      clipboard.setContents(new StringSelection(EMPTY_TEXT), null);
    }
    catch (Exception e) {}                                                      // the clipboard may not contain an image, so do nothing then
  }

//------------------------------------------------------------------------------

}
