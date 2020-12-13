
package org.skyllias.alomatia.filter.buffered.hdr.naive;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.ImageFilter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.skyllias.alomatia.filter.FilteredImageGenerator;
import org.skyllias.alomatia.test.ImageUtils;

@RunWith(MockitoJUnitRunner.class)
public class NaiveHdrOpTest
{
  @Mock
  private ImageFilter blurringFilter;
  @Mock
  private FilteredImageGenerator filteredImageGenerator;

  @InjectMocks
  private NaiveHdrOp imageOp;

  @Test
  public void test()
  {
    BufferedImage srcImage = buildInputImage();

    when(filteredImageGenerator.generate(srcImage, blurringFilter))
        .thenReturn(buildBlurredImage());

    BufferedImage destImage = imageOp.filter(srcImage, null);

    assertTrue(ImageUtils.areEqual(destImage, buildExpectedImage()));
  }


  private BufferedImage buildInputImage()
  {
    BufferedImage image = new BufferedImage(2, 2, BufferedImage.TYPE_INT_ARGB);
    image.setRGB(0, 0, Color.RED.getRGB());
    image.setRGB(0, 1, Color.GREEN.getRGB());
    image.setRGB(1, 0, Color.BLUE.getRGB());
    image.setRGB(1, 1, new Color(100, 200, 50).getRGB());

    return image;
  }

  private BufferedImage buildBlurredImage()
  {
    BufferedImage image = new BufferedImage(2, 2, BufferedImage.TYPE_INT_ARGB);
    image.setRGB(0, 0, Color.BLACK.getRGB());
    image.setRGB(0, 1, Color.LIGHT_GRAY.getRGB());
    image.setRGB(1, 0, Color.WHITE.getRGB());
    image.setRGB(1, 1, new Color(200, 50, 100).getRGB());

    return image;
  }

  private BufferedImage buildExpectedImage()
  {
    BufferedImage image = new BufferedImage(2, 2, BufferedImage.TYPE_INT_ARGB);
    image.setRGB(0, 0, Color.BLACK.getRGB());
    image.setRGB(0, 1, new Color(129, 255, 129).getRGB());
    image.setRGB(1, 0, Color.WHITE.getRGB());
    image.setRGB(1, 1, new Color(188, 78, 39).getRGB());

    return image;
  }
}
