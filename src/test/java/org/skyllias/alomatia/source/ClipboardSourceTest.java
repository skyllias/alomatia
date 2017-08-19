
package org.skyllias.alomatia.source;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.image.*;

import org.junit.*;
import org.mockito.*;
import org.skyllias.alomatia.*;

/** The system's clipboard cannot be used to generate the events because it is
 *  asynchronous. */

public class ClipboardSourceTest
{
  private ClipboardSource source = new ClipboardSource();
  @Mock
  private ImageDisplay imageDisplay;
  @Mock
  private Clipboard clipboard;

  @Before
  public void setUp()
  {
    MockitoAnnotations.initMocks(this);

    source.setActive(false);
    source.setClipboard(clipboard);
    source.setDisplay(imageDisplay);
  }

  @After
  public void tearDown()
  {
    source.setActive(false);
  }

//------------------------------------------------------------------------------

  @Test
  public void shouldDoNothingWhenNothingCopied()
  {
    source.setActive(true);

    FlavorEvent event = new FlavorEvent(clipboard);
    source.flavorsChanged(event);

    verify(imageDisplay, never()).setOriginalImage(any(Image.class));
  }

  @Test
  public void shouldReadClipboardContentsWhenActivated() throws Exception
  {
    source.setActive(true);
    verify(clipboard, atLeastOnce()).getData(DataFlavor.imageFlavor);
  }

  @Test
  public void shouldRegisterListenerWhenActivated()
  {
    source.setActive(true);
    verify(clipboard, atLeastOnce()).addFlavorListener(source);
  }

  @Test
  public void shouldUnregisterListenerWhenInactive() throws Exception
  {
    source.setActive(false);
    verify(clipboard, atLeastOnce()).removeFlavorListener(source);
  }

  @Test
  public void shouldDisplayImageWhenCopied() throws Exception
  {
    source.setActive(true);

    Image copiedImage = getNewImage();
    when(clipboard.getData(DataFlavor.imageFlavor)).thenReturn(copiedImage);
    source.flavorsChanged(new FlavorEvent(clipboard));

    verify(imageDisplay, times(1)).setOriginalImage(copiedImage);
  }

  @Test
  public void shouldDisplaySubsequentImages() throws Exception
  {
    source.setActive(true);

    Image firstImage  = getNewImage();
    Image secondImage = getNewImage();
    when(clipboard.getData(DataFlavor.imageFlavor)).thenReturn(firstImage);
    when(clipboard.getData(DataFlavor.imageFlavor)).thenReturn(secondImage);

    source.flavorsChanged(new FlavorEvent(clipboard));
    source.flavorsChanged(new FlavorEvent(clipboard));

    verify(imageDisplay, atLeastOnce()).setOriginalImage(secondImage);
  }

//------------------------------------------------------------------------------

  private Image getNewImage()
  {
    return new BufferedImage(1, 1, BufferedImage.TYPE_BYTE_GRAY);
  }
/*
  private Transferable getImageTransferable(Image image) throws IOException, UnsupportedFlavorException
  {
    Transferable transferable = mock(Transferable.class);
    when(transferable.isDataFlavorSupported(DataFlavor.imageFlavor)).thenReturn(true);
    when(transferable.getTransferData(DataFlavor.imageFlavor)).thenReturn(image);
    return transferable;
  }
*/
}
