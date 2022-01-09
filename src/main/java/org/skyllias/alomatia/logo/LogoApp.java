
package org.skyllias.alomatia.logo;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

/** Application to generate the logo as a file in the project. */

public class LogoApp
{
  private static final String FILE_PATH  = "src/main/resources/img/alomatia.png";
  private static final String IMG_FORMAT = "png";
  private static final int WIDTH         = 32;
  private static final int HEIGHT        = 32;

//==============================================================================

  public static void main(String[] args) throws Exception
  {
    Image logo = new LogoProducer(new ColourCoordinator()).createImage(WIDTH, HEIGHT);

    BufferedImage bufferedImage = new BufferedImage(logo.getWidth(null), logo.getHeight(null), BufferedImage.TYPE_INT_RGB);
    Graphics2D imageGraphics    = bufferedImage.createGraphics();
    imageGraphics.drawImage(logo, null, null);
    imageGraphics.dispose();

    File outputFile = new File(FILE_PATH);
    ImageIO.write(bufferedImage, IMG_FORMAT, outputFile);
  }

//------------------------------------------------------------------------------

}
