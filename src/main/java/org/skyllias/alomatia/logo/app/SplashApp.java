
package org.skyllias.alomatia.logo.app;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.stream.FileImageOutputStream;

import org.skyllias.alomatia.logo.ColourCoordinator;
import org.skyllias.alomatia.logo.LogoProducer;

/** Application to generate the splash image as a file in the project. */

public class SplashApp
{
  private static final String FILE_PATH  = "src/main/resources/img/splash.gif";
  private static final int IMAGE_TYPE    = BufferedImage.TYPE_INT_RGB;

  private static final int TILE_WIDTH             = 45;
  private static final int TILE_HEIGHT            = 45;
  private static final int HORIZONTAL_REPETITIONS = 8;

  private static final int FRAME_DELAY_MS = 100;
  private static final int FRAME_STEP     = 3;

//==============================================================================

  public static void main(String[] args) throws Exception
  {
    new SplashApp().createSplash();
  }

//------------------------------------------------------------------------------

  public void createSplash() throws FileNotFoundException, IOException
  {
    Image tileImage = new LogoProducer(new ColourCoordinator(true)).createImage(TILE_WIDTH, TILE_HEIGHT);

    try (FileImageOutputStream imageOutputStream = new FileImageOutputStream(new File(FILE_PATH)))
    {
      GifSequenceWriter gifSequenceWriter = new GifSequenceWriter(imageOutputStream, IMAGE_TYPE, FRAME_DELAY_MS, true);
      for (int i = 0; i < TILE_WIDTH; i += FRAME_STEP)
      {
        BufferedImage currentFrameImage = tile(tileImage, i);
        gifSequenceWriter.writeToSequence(currentFrameImage);
      }
      gifSequenceWriter.close();
    }
  }

//------------------------------------------------------------------------------

  private BufferedImage tile(Image image, int shift)
  {
    int tiledImageWidth  = HORIZONTAL_REPETITIONS * image.getWidth(null);
    int tiledImageHeight = image.getHeight(null);

    BufferedImage bufferedImage = new BufferedImage(tiledImageWidth, tiledImageHeight, IMAGE_TYPE);
    Graphics2D imageGraphics    = bufferedImage.createGraphics();

    for (int i = -1; i < HORIZONTAL_REPETITIONS; i++)
    {
      imageGraphics.drawImage(image, i * TILE_WIDTH + shift, 0, null);
    }

    imageGraphics.dispose();
    return bufferedImage;
  }

//------------------------------------------------------------------------------

}
