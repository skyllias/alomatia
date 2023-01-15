
package org.skyllias.alomatia.source;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.skyllias.alomatia.ImageDisplay;
import org.skyllias.alomatia.preferences.SourcePreferences;

/** The system's clipboard cannot be used to generate the events because it is
 *  asynchronous. */

@RunWith(MockitoJUnitRunner.class)
public class ClipboardSourceTest
{
  @Mock
  private ImageDisplay imageDisplay;
  @Mock
  private Clipboard clipboard;
  @Mock
  private SourcePreferences sourcePreferences;

  private ClipboardSource source;

  private void setUp()
  {
    source = new ClipboardSource(imageDisplay, clipboard, sourcePreferences);
  }

//------------------------------------------------------------------------------

  @Test
  public void shouldHaveAutoModeOffAccordingToPreferences()
  {
    when(sourcePreferences.isClipboardAutoMode()).thenReturn(false);
    setUp();

    assertFalse(source.isAutoMode());
  }

  @Test
  public void shouldHaveAutoModeOnAccordingToPreferences()
  {
    when(sourcePreferences.isClipboardAutoMode()).thenReturn(true);
    setUp();

    assertTrue(source.isAutoMode());
  }

  @Test
  public void shouldSavePreferencesWhenAutoModeSet()
  {
    setUp();

    source.setAutoMode(true);
    verify(sourcePreferences).setClipboardAutoMode(true);
  }

  @Test
  public void shouldDoNothingWhenNothingCopiedInAutoMode()
  {
    setUp();

    source.setActive(true);                                                     // order matters between setActive and setAutoMode
    source.setAutoMode(true);

    FlavorEvent event = new FlavorEvent(clipboard);
    source.flavorsChanged(event);

    verify(imageDisplay, never()).setOriginalImage(any(Image.class));
  }

  @Test
  public void shouldDoNothingWhenNothingCopied()
  {
    setUp();

    source.setActive(true);
    source.setAutoMode(false);

    source.readFromClipboard();

    verify(imageDisplay, never()).setOriginalImage(any(Image.class));
  }

  @Test
  public void shouldReadClipboardContentsWhenActivatedInAutoMode() throws Exception
  {
    setUp();

    source.setAutoMode(true);
    source.setActive(true);
    verify(clipboard).getData(DataFlavor.imageFlavor);
  }

  @Test
  public void shouldNotReadClipboardContentsWhenActivatedInNotAutoMode() throws Exception
  {
    setUp();

    source.setAutoMode(false);
    source.setActive(true);
    verify(clipboard, never()).getData(DataFlavor.imageFlavor);
  }

  @Test
  public void shouldRegisterListenerWhenActivated()
  {
    setUp();

    source.setActive(true);
    verify(clipboard, atLeastOnce()).addFlavorListener(source);
  }

  @Test
  public void shouldUnregisterListenerWhenInactive() throws Exception
  {
    setUp();

    source.setActive(false);
    verify(clipboard).removeFlavorListener(source);
  }

  @Test
  public void shouldDisplayImageWhenCopiedInAutoMode() throws Exception
  {
    setUp();

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
    setUp();

    source.setActive(true);
    source.setAutoMode(false);

    source.flavorsChanged(new FlavorEvent(clipboard));

    verify(clipboard, never()).getData(any());
    verify(imageDisplay, never()).setOriginalImage(any());
  }

  @Test
  public void shouldNotDisplayImageWhenCopiedInAutoMode() throws Exception
  {
    setUp();

    source.setActive(true);
    source.setAutoMode(true);

    source.readFromClipboard();

    verify(clipboard, never()).getData(any());
    verify(imageDisplay, never()).setOriginalImage(any());
  }

  @Test
  public void shouldDisplayImageWhenCopiedInNotAutoMode() throws Exception
  {
    setUp();

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
    setUp();

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
