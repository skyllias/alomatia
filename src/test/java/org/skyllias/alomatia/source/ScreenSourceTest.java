
package org.skyllias.alomatia.source;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.skyllias.alomatia.test.matchers.AlomatiaMatchers.sameImage;

import java.awt.GraphicsDevice;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.skyllias.alomatia.ImageDisplay;
import org.skyllias.alomatia.source.ScreenSource.ScreenRectangle;
import org.skyllias.alomatia.source.screen.MouseLocator;
import org.skyllias.alomatia.source.screen.PointerData;
import org.skyllias.alomatia.source.screen.ScreenCapturer;

@RunWith(MockitoJUnitRunner.class)
public class ScreenSourceTest
{
  @Mock
  private ImageDisplay imageDisplay;
  @Mock
  private ScreenCapturer screenCapturer;
  @Mock
  private MouseLocator mouseLocator;

  @InjectMocks
  private ScreenSource screenSource;

//------------------------------------------------------------------------------

  @Test
  public void shouldDoNothingIfInactive() throws Exception
  {
    screenSource.setActive(false);
    screenSource.setScreenBounds(new ScreenRectangle(buildGraphicsDevice(),
                                                     buildRectangle()));

    screenSource.capture();

    verify(screenCapturer, never()).capture(any());
    verify(mouseLocator, never()).getMouseInfo();
    verify(imageDisplay, never()).setOriginalImage(any());
  }

  @Test
  public void shouldDoNothingIfNoBoundsSet() throws Exception
  {
    screenSource.setActive(true);

    screenSource.capture();

    verify(screenCapturer, never()).capture(any());
    verify(mouseLocator, never()).getMouseInfo();
    verify(imageDisplay, never()).setOriginalImage(any());
  }

  @Test
  public void shouldDisplayCapturedImageIfMouseInDifferentDevice() throws Exception
  {
    ScreenRectangle screenRectangle = new ScreenRectangle(buildGraphicsDevice(),
                                                          buildRectangle());
    PointerData pointerInfo = new PointerData(buildGraphicsDevice(), new Point(75, 200));
    when(screenCapturer.capture(screenRectangle)).thenReturn(buildInputImage());
    when(mouseLocator.getMouseInfo()).thenReturn(pointerInfo);

    screenSource.setActive(true);
    screenSource.setScreenBounds(screenRectangle);

    screenSource.capture();

    verify(imageDisplay).setOriginalImage(argThat(sameImage(buildInputImage())));
  }

  @Test
  public void shouldDisplayCapturedImageIfMouseOutsideBounds() throws Exception
  {
    GraphicsDevice graphicsDevice = buildGraphicsDevice();
    ScreenRectangle screenRectangle = new ScreenRectangle(graphicsDevice,
                                                          buildRectangle());
    PointerData pointerInfo = new PointerData(graphicsDevice, new Point(25, 200));
    when(screenCapturer.capture(screenRectangle)).thenReturn(buildInputImage());
    when(mouseLocator.getMouseInfo()).thenReturn(pointerInfo);

    screenSource.setActive(true);
    screenSource.setScreenBounds(screenRectangle);

    screenSource.capture();

    verify(imageDisplay).setOriginalImage(argThat(sameImage(buildInputImage())));
  }

  @Test
  public void shouldDisplayCapturedImageWithPointer() throws Exception
  {
    GraphicsDevice graphicsDevice = buildGraphicsDevice();
    ScreenRectangle screenRectangle = new ScreenRectangle(graphicsDevice,
                                                          buildRectangle());
    PointerData pointerInfo = new PointerData(graphicsDevice, new Point(75, 200));
    when(screenCapturer.capture(screenRectangle)).thenReturn(buildInputImage());
    when(mouseLocator.getMouseInfo()).thenReturn(pointerInfo);

    screenSource.setActive(true);
    screenSource.setScreenBounds(screenRectangle);

    screenSource.capture();

    verify(imageDisplay).setOriginalImage(argThat(sameImage(buildImageWithCursor())));
  }

  private BufferedImage buildInputImage()
  {
    final String IMAGE_PATH = "/sources/streaks.png";

    return buildImage(IMAGE_PATH);
  }

  private GraphicsDevice buildGraphicsDevice()
  {
    return mock(GraphicsDevice.class);
  }

  private Rectangle buildRectangle()
  {
    return new Rectangle(50, 100, 150, 200);
  }

  private BufferedImage buildImageWithCursor()
  {
    final String IMAGE_PATH = "/sources/streaks-with-cursor.png";

    return buildImage(IMAGE_PATH);
  }

  private BufferedImage buildImage(String imagePath)
  {
    try (InputStream imageStream = getClass().getResourceAsStream(imagePath))
    {
      return ImageIO.read(imageStream);
    }
    catch (IOException e) {throw new RuntimeException(e);}
  }

}
