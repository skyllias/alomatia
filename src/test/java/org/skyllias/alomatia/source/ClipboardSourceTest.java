
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
  public void shouldDoNothingWhenNothingCopiedInAutoMode()
  {
    source.setAutoMode(true);
    source.setActive(true);

    FlavorEvent event = new FlavorEvent(clipboard);
    source.flavorsChanged(event);

    verify(imageDisplay, never()).setOriginalImage(any(Image.class));
  }

  @Test
  public void shouldDoNothingWhenNothingCopiedInNotAutoMode()
  {
    source.setAutoMode(false);
    source.setActive(true);

    source.readFromClipboard();

    verify(imageDisplay, never()).setOriginalImage(any(Image.class));
  }

  @Test
  public void shouldReadClipboardContentsWhenActivatedInAutoMode() throws Exception
  {
    source.setAutoMode(true);
    source.setActive(true);
    verify(clipboard).getData(DataFlavor.imageFlavor);
  }

  @Test
  public void shouldNotReadClipboardContentsWhenActivatedInNotAutoMode() throws Exception
  {
    source.setAutoMode(false);
    source.setActive(true);
    verify(clipboard, never()).getData(DataFlavor.imageFlavor);
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
    verify(clipboard).removeFlavorListener(source);
  }

  @Test
  public void shouldDisplayImageWhenCopiedInAutoMode() throws Exception
  {
    source.setAutoMode(true);
    source.setActive(true);

    Image copiedImage = getNewImage();
    when(clipboard.getData(DataFlavor.imageFlavor)).thenReturn(copiedImage);
    source.flavorsChanged(new FlavorEvent(clipboard));

    verify(imageDisplay).setOriginalImage(copiedImage);
  }

  @Test
  public void shouldNotDisplayImageWhenCopiedInNotAutoMode() throws Exception
  {
    source.setAutoMode(false);
    source.setActive(true);

    Image copiedImage = getNewImage();
    when(clipboard.getData(DataFlavor.imageFlavor)).thenReturn(copiedImage);
    source.flavorsChanged(new FlavorEvent(clipboard));

    verify(imageDisplay, never()).setOriginalImage(copiedImage);
  }

  @Test
  public void shouldNotDisplayImageWhenCopiedInAutoMode() throws Exception
  {
    source.setAutoMode(true);
    source.setActive(true);

    Image copiedImage = getNewImage();
    when(clipboard.getData(DataFlavor.imageFlavor)).thenReturn(copiedImage);
    source.readFromClipboard();

    verify(imageDisplay, never()).setOriginalImage(copiedImage);
  }

  @Test
  public void shouldDisplayImageWhenCopiedInNotAutoMode() throws Exception
  {
    source.setAutoMode(false);
    source.setActive(true);

    Image copiedImage = getNewImage();
    when(clipboard.getData(DataFlavor.imageFlavor)).thenReturn(copiedImage);
    source.readFromClipboard();

    verify(imageDisplay).setOriginalImage(copiedImage);
  }

  @Test
  public void shouldDisplaySubsequentImages() throws Exception
  {
    source.setAutoMode(true);
    source.setActive(true);

    Image firstImage  = getNewImage();
    Image secondImage = getAnotherImage();
    when(clipboard.getData(DataFlavor.imageFlavor)).thenReturn(firstImage).
                                                    thenReturn(secondImage);

    source.flavorsChanged(new FlavorEvent(clipboard));
    source.flavorsChanged(new FlavorEvent(clipboard));

    verify(imageDisplay).setOriginalImage(secondImage);
  }

//------------------------------------------------------------------------------

  private Image getNewImage()
  {
    return new BufferedImage(1, 1, BufferedImage.TYPE_BYTE_GRAY);
  }

  private Image getAnotherImage()
  {
    return new BufferedImage(2, 2, BufferedImage.TYPE_BYTE_GRAY);
  }

}
