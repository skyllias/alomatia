
package org.skyllias.alomatia.source;

import java.awt.Image;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

import javax.imageio.ImageIO;

import org.skyllias.alomatia.ImageDisplay;
import org.skyllias.alomatia.ImageSource;
import org.springframework.stereotype.Component;

/** Source that takes images dropped on some component (supposedly, the display
 * panel itself).
 * <p>
 * It accepts both images and files containing images.
 * <p>
 * Currently, drops from browsers are not supported because they transfer HTML
 * fragments rather images. Maybe TransferHandler should be used instead of
 * DropTargetListener. */

@Component
public class DropSource implements ImageSource, DropTargetListener
{
  private final ImageDisplay imageDisplay;
  private boolean active;

//==============================================================================

  public DropSource(ImageDisplay imageDisplay)
  {
    this.imageDisplay = imageDisplay;
  }

//==============================================================================

  @Override
  public void setActive(boolean active) {this.active = active;}

//------------------------------------------------------------------------------

  /** Takes the dropped object. If it is an image, it is sent to the display.
   *  If a single file, it is opened and if it contains an image, it is sent to
   *  the display. Otherwise, nothing happens. */

  @Override
  @SuppressWarnings("unchecked")
  public void drop(DropTargetDropEvent dtde)
  {
    boolean completed = false;

    if (active)
    {
      try
      {
        dtde.acceptDrop(DnDConstants.ACTION_COPY);
        Transferable transferable = dtde.getTransferable();
        if (transferable.isDataFlavorSupported(DataFlavor.imageFlavor))
        {
          Image image = (Image) transferable.getTransferData(java.awt.datatransfer.DataFlavor.imageFlavor);
          sendImageToDisplay(image);
          completed = true;
        }
        else if (transferable.isDataFlavorSupported(DataFlavor.javaFileListFlavor))
        {
          List<File> fileList = (List<File>) transferable.getTransferData(DataFlavor.javaFileListFlavor);
          if (fileList.size() == 1)
          {
            File file           = fileList.get(0);
            BufferedImage image = ImageIO.read(file);                           // this returns null if an image cannot be read from the file
            sendImageToDisplay(image);
            completed = true;
          }
        }
      }
      catch (Exception e) {}                                                    // not critical
    }
    dtde.dropComplete(completed);
  }

//------------------------------------------------------------------------------

  /** Rejects the drop if the source is not active or if the data flavour is not
   *  one of the expected by {@link #drop(DropTargetDropEvent)}. */

  @Override
  public void dragEnter(DropTargetDragEvent dtde)
  {
    if (active)
    {
      Transferable transferable = dtde.getTransferable();
      if (!transferable.isDataFlavorSupported(DataFlavor.imageFlavor) &&
          !transferable.isDataFlavorSupported(DataFlavor.javaFileListFlavor))
        dtde.rejectDrag();
    }
    else dtde.rejectDrag();
  }

//------------------------------------------------------------------------------

  /* Nothing to do. */

  @Override
  public void dropActionChanged(DropTargetDragEvent arg0) {}

  @Override
  public void dragExit(DropTargetEvent arg0) {}

  @Override
  public void dragOver(DropTargetDragEvent arg0) {}

//------------------------------------------------------------------------------

  /* If the image is not null, it is sent to the display.
   * Else, nothing happens. */

  private void sendImageToDisplay(Image image)
  {
    if (image != null) imageDisplay.setOriginalImage(image);
  }

//------------------------------------------------------------------------------

}
