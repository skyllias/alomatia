
package org.skyllias.alomatia.logo;

import java.awt.*;
import java.awt.image.*;

/** Generator of images whose pixels depend on their location according to a ColourCoordinator. */

public class LogoProducer
{
  private static final int MARGIN_SATURATION = 2;                               // amount of pixels to keep away from the uttermost unsaturated colours
  private static final int MARGIN_BRIGHTNESS = 2;                               // amount of pixels to keep away from the uttermost darkened colours

  private ColourCoordinator coordinator = new ColourCoordinator();

//==============================================================================

  /** Returns a new image with the passed dimensions. */

  public Image createImage(int width, int height)
  {
    BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

    for (int i = 0; i < width; i++)
    {
      for (int j = 0; j < height; j++)
      {
        float x = ((float) i) / (float) width;
        float y = ((float) j + MARGIN_SATURATION) /
                  ((float) height  + MARGIN_SATURATION + MARGIN_BRIGHTNESS);    // avoid 0 and 1

        Color pixelColour = coordinator.getColourAt(x, y);
        image.setRGB(i, j, pixelColour.getRGB());
      }
    }
    return image;
  }

//------------------------------------------------------------------------------

}
