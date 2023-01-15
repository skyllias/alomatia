
package org.skyllias.alomatia.source;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.awt.Image;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.image.BufferedImage;

import org.junit.Test;
import org.skyllias.alomatia.ImageDisplay;

/** Events have to be mocked because their construction is too complex. */

public class DropSourceTest
{
  @Test
  public void shouldDoNothingWhenDropOnInactive()
  {
    DropTargetDropEvent event = mock(DropTargetDropEvent.class);
    ImageDisplay imageDisplay = mock(ImageDisplay.class);

    DropSource dropSource = new DropSource(imageDisplay);
    dropSource.setActive(false);

    dropSource.drop(event);
    verify(imageDisplay, never()).setOriginalImage(any(Image.class));
  }

  @Test
  public void shouldDisplayImageWhenDroppedOnActive() throws Exception
  {
    Image image               = new BufferedImage(1, 1, BufferedImage.TYPE_BYTE_GRAY);
    Transferable transferable = mock(Transferable.class);
    when(transferable.isDataFlavorSupported(DataFlavor.imageFlavor)).thenReturn(true);
    when(transferable.getTransferData(DataFlavor.imageFlavor)).thenReturn(image);
    DropTargetDropEvent event = mock(DropTargetDropEvent.class);
    when(event.getTransferable()).thenReturn(transferable);

    ImageDisplay display = mock(ImageDisplay.class);

    DropSource dropSource = new DropSource(display);
    dropSource.setActive(true);

    dropSource.drop(event);
    verify(display, times(1)).setOriginalImage(image);
  }

  // TODO test drop file, which needs a real file with an image
  public void dropFileOnActive() throws Exception
  {
  }

  @Test
  public void shouldRejectWhenDragEnterInactive()
  {
    DropTargetDragEvent event = mock(DropTargetDragEvent.class);

    ImageDisplay display = mock(ImageDisplay.class);

    DropSource dropSource = new DropSource(display);
    dropSource.setActive(false);

    dropSource.dragEnter(event);
    verify(event).rejectDrag();
  }

  @Test
  public void shouldRejectWhenDragEnterUnsupportedFlavour()
  {
    Transferable transferable = mock(Transferable.class);
    when(transferable.isDataFlavorSupported(any(DataFlavor.class))).thenReturn(false);
    DropTargetDragEvent event = mock(DropTargetDragEvent.class);
    when(event.getTransferable()).thenReturn(transferable);

    ImageDisplay display = mock(ImageDisplay.class);

    DropSource dropSource = new DropSource(display);
    dropSource.setActive(true);

    dropSource.dragEnter(event);
    verify(event).rejectDrag();
  }

  @Test
  public void shouldDoNothingWhenJustDragEnterSupportedFlavour()
  {
    Transferable transferable = mock(Transferable.class);
    when(transferable.isDataFlavorSupported(any(DataFlavor.class))).thenReturn(true);
    DropTargetDragEvent event = mock(DropTargetDragEvent.class);
    when(event.getTransferable()).thenReturn(transferable);

    ImageDisplay display = mock(ImageDisplay.class);

    DropSource dropSource = new DropSource(display);
    dropSource.setActive(true);

    dropSource.dragEnter(event);
    verify(event, never()).rejectDrag();
  }
}
