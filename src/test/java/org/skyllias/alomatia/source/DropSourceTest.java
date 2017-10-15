
package org.skyllias.alomatia.source;

import static org.mockito.Mockito.*;

import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.dnd.*;
import java.awt.image.*;

import org.junit.*;

import org.skyllias.alomatia.*;

/** Events have to be mocked because their construction is too complex. */

public class DropSourceTest
{
  @Test
  public void shouldDoNothingWhenDropOnInactive()
  {
    DropTargetDropEvent event = mock(DropTargetDropEvent.class);
    ImageDisplay imageDisplay = mock(ImageDisplay.class);

    DropSource dropSource = new DropSource();
    dropSource.setActive(false);
    dropSource.setDisplay(imageDisplay);

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

    DropSource dropSource = new DropSource();
    dropSource.setActive(true);
    dropSource.setDisplay(display);

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

    DropSource dropSource = new DropSource();
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

    DropSource dropSource = new DropSource();
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

    DropSource dropSource = new DropSource();
    dropSource.setActive(true);

    dropSource.dragEnter(event);
    verify(event, never()).rejectDrag();
  }
}
