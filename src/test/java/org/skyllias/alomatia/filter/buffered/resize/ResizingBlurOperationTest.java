
package org.skyllias.alomatia.filter.buffered.resize;

import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.awt.image.BufferedImage;

import org.junit.Test;
import org.skyllias.alomatia.test.ImageUtils;

public class ResizingBlurOperationTest
{
  @Test
  public void shouldNotChangeUniformImages()
  {
    BufferedImage srcImage  = buildUniformImage(16, 16, new Color(10, 200, 30));
    BufferedImage destImage = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);

    buildOperation(2).filter(srcImage, destImage);

    assertTrue(ImageUtils.areEqual(destImage, buildUniformImage(16, 16, new Color(10, 200, 30))));
  }

  @Test
  public void shouldNotChangeUniformTranslucentImages()
  {
    BufferedImage srcImage  = buildUniformImage(16, 16, new Color(255, 255, 255, 128));
    BufferedImage destImage = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);

    buildOperation(2).filter(srcImage, destImage);

    assertTrue(ImageUtils.areEqual(destImage, buildUniformImage(16, 16, new Color(255, 255, 255, 128))));
  }

  @Test
  public void shouldNotModifyInputImage()
  {
    BufferedImage srcImage  = buildGradientImage();
    BufferedImage destImage = new BufferedImage(8, 6, BufferedImage.TYPE_INT_ARGB);

    buildOperation(4).filter(srcImage, destImage);

    assertTrue(ImageUtils.areEqual(srcImage, buildGradientImage()));
  }

  @Test
  public void shouldNotFailWithImagesShorterThanTheRequestedLength()
  {
    BufferedImage srcImage  = buildUniformImage(3, 2, Color.BLUE);
    BufferedImage destImage = new BufferedImage(3, 2, BufferedImage.TYPE_INT_ARGB);

    buildOperation(50).filter(srcImage, destImage);

    assertTrue(ImageUtils.areEqual(destImage, buildUniformImage(3, 2, Color.BLUE)));
  }


  private ResizingBlurOperation buildOperation(int shortestLength)
  {
    return new ResizingBlurOperation(new ReducedSizeCalculator(shortestLength));
  }

  private BufferedImage buildUniformImage(int width, int height, Color colour)
  {
    BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    for (int x = 0; x < width; x++)
    {
      for (int y = 0; y < height; y++) image.setRGB(x, y, colour.getRGB());
    }

    return image;
  }

  private BufferedImage buildGradientImage()
  {
    BufferedImage image = new BufferedImage(8, 6, BufferedImage.TYPE_INT_ARGB);
    for (int x = 0; x < 8; x++)
    {
      for (int y = 0; y < 6; y++) image.setRGB(x, y, new Color(32 * x, 42 * y, 0).getRGB());
    }

    return image;
  }
}
