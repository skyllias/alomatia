
package org.skyllias.alomatia.source;

import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.dnd.*;
import java.awt.image.*;
import java.io.*;
import java.util.List;

import javax.imageio.*;

/** Source that takes images dropped on some component (supposedly, the display
 * panel itself).
 * <p>
 * It accepts both images and files containing images.
 * <p>
 * Currently, drops from browsers are not supported because they transfer HTML
 * fragments rather images. Maybe TransferHandler should be used instead of
 * DropTargetListener. */

public class DropSource extends BasicSource
                        implements DropTargetListener
{
//==============================================================================

  /** Takes the dropped object. If it is an image, it is sent to the display.
   *  If a single file, it is opened and if it contains an image, it is sent to
   *  the display. Otherwise, nothing happens. */

  @Override
  @SuppressWarnings("unchecked")
  public void drop(DropTargetDropEvent dtde)
  {
    boolean completed = false;

    if (isActive())
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
            if (image != null) sendImageToDisplay(image);
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
    if (isActive())
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

}
