
package org.skyllias.alomatia.filter.buffered.layered;

import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.awt.image.BufferedImage;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.skyllias.alomatia.test.ImageUtils;

@RunWith(MockitoJUnitRunner.class)
public class RadialAlphaOperationTest
{
  @Test
  public void shouldSetAlphaRespectingOthers()
  {
    BufferedImage srcImage = buildInputImage();

    BufferedImage destImage = new BufferedImage(4, 3, BufferedImage.TYPE_INT_ARGB);
    new RadialAlphaOperation(0.75f).filter(srcImage, destImage);

    assertTrue(ImageUtils.areEqual(destImage, buildExpectedImage()));
  }


  private BufferedImage buildInputImage()
  {
    BufferedImage image = new BufferedImage(4, 3, BufferedImage.TYPE_INT_ARGB);
    image.setRGB(0, 0, new Color(0, 0, 255).getRGB());
    image.setRGB(0, 1, new Color(0, 127, 127).getRGB());
    image.setRGB(0, 2, new Color(0, 255, 0).getRGB());
    image.setRGB(1, 0, new Color(85, 0, 170).getRGB());
    image.setRGB(1, 1, new Color(85, 127, 127).getRGB());
    image.setRGB(1, 2, new Color(85, 255, 85).getRGB());
    image.setRGB(2, 0, new Color(170, 0, 85).getRGB());
    image.setRGB(2, 1, new Color(170, 127, 127).getRGB());
    image.setRGB(2, 2, new Color(170, 255, 170).getRGB());
    image.setRGB(3, 0, new Color(255, 0, 0).getRGB());
    image.setRGB(3, 1, new Color(255, 127, 127).getRGB());
    image.setRGB(3, 2, new Color(255, 255, 255).getRGB());

    return image;
  }

  private BufferedImage buildExpectedImage()
  {
    BufferedImage image = new BufferedImage(4, 3, BufferedImage.TYPE_INT_ARGB);
    image.setRGB(0, 0, new Color(0, 0, 255, 252).getRGB());
    image.setRGB(0, 1, new Color(0, 127, 127, 247).getRGB());
    image.setRGB(0, 2, new Color(0, 255, 0, 252).getRGB());
    image.setRGB(1, 0, new Color(85, 0, 170, 211).getRGB());
    image.setRGB(1, 1, new Color(85, 127, 127, 150).getRGB());
    image.setRGB(1, 2, new Color(85, 255, 85, 211).getRGB());
    image.setRGB(2, 0, new Color(170, 0, 85, 150).getRGB());
    image.setRGB(2, 1, new Color(170, 127, 127, 0).getRGB());
    image.setRGB(2, 2, new Color(170, 255, 170, 150).getRGB());
    image.setRGB(3, 0, new Color(255, 0, 0, 211).getRGB());
    image.setRGB(3, 1, new Color(255, 127, 127, 150).getRGB());
    image.setRGB(3, 2, new Color(255, 255, 255, 211).getRGB());

    return image;
  }

}
