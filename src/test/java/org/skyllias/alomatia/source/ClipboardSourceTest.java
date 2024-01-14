
package org.skyllias.alomatia.source;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.awt.Image;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.FlavorEvent;
import java.awt.image.BufferedImage;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.skyllias.alomatia.ImageDisplay;

/** The system's clipboard cannot be used to generate the events because it is
 *  asynchronous. */

@RunWith(MockitoJUnitRunner.class)
public class ClipboardSourceTest
{
  @Mock
  private ImageDisplay imageDisplay;
  @Mock
  private Clipboard clipboard;

  private ClipboardSource source;

  @Before
  public void setUp()
  {
    source = new ClipboardSource(imageDisplay, clipboard);
  }

//------------------------------------------------------------------------------

  @Test
  public void shouldDoNothingWhenNothingCopiedInAutoMode()
  {
    source.setActive(true);                                                     // order matters between setActive and setAutoMode
    source.setAutoMode(true);

    FlavorEvent event = new FlavorEvent(clipboard);
    source.flavorsChanged(event);

    verify(imageDisplay, never()).setOriginalImage(any(Image.class));
  }

  @Test
  public void shouldDoNothingWhenNothingCopied()
  {
    source.setActive(true);
    source.setAutoMode(false);

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
    source.setActive(true);
    source.setAutoMode(true);

    Image copiedImage = getNewImage();
    when(clipboard.getData(DataFlavor.imageFlavor)).thenReturn(copiedImage);
    source.flavorsChanged(new FlavorEvent(clipboard));

    verify(imageDisplay).setOriginalImage(copiedImage);
  }

  @Test
  public void shouldNotDisplayImageWhenCopiedInNotAutoMode() throws Exception
  {
    source.setActive(true);
    source.setAutoMode(false);

    source.flavorsChanged(new FlavorEvent(clipboard));

    verify(clipboard, never()).getData(any());
    verify(imageDisplay, never()).setOriginalImage(any());
  }

  @Test
  public void shouldNotDisplayImageWhenCopiedInAutoMode() throws Exception
  {
    source.setActive(true);
    source.setAutoMode(true);

    source.readFromClipboard();

    verify(clipboard, never()).getData(any());
    verify(imageDisplay, never()).setOriginalImage(any());
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
    when(clipboard.getData(DataFlavor.imageFlavor)).thenReturn(firstImage, secondImage);

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
