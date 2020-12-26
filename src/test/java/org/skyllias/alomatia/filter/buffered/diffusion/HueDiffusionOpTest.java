
package org.skyllias.alomatia.filter.buffered.diffusion;

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
import org.skyllias.alomatia.filter.buffered.FilteredBufferedImageGenerator;
import org.skyllias.alomatia.test.ImageUtils;

@RunWith(MockitoJUnitRunner.class)
public class HueDiffusionOpTest
{
  @Mock
  private ImageFilter blurringFilter;
  @Mock
  private FilteredBufferedImageGenerator filteredImageGenerator;

  @InjectMocks
  private HueDiffusionOp imageOp;


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
    image.setRGB(0, 0, Color.HSBtoRGB(0.1f, 0.2f, 0.3f));
    image.setRGB(0, 1, Color.HSBtoRGB(0.4f, 0.5f, 0.6f));
    image.setRGB(1, 0, Color.HSBtoRGB(0.7f, 0.8f, 0.9f));
    image.setRGB(1, 1, Color.HSBtoRGB(0.35f, 0.55f, 0.75f));

    return image;
  }

  private BufferedImage buildBlurredImage()
  {
    BufferedImage image = new BufferedImage(2, 2, BufferedImage.TYPE_INT_ARGB);
    image.setRGB(0, 0, Color.HSBtoRGB(0.16f, 0.25f, 0.35f));
    image.setRGB(0, 1, Color.HSBtoRGB(0.45f, 0.55f, 0.65f));
    image.setRGB(1, 0, Color.HSBtoRGB(0.75f, 0.85f, 0.95f));
    image.setRGB(1, 1, Color.HSBtoRGB(0.3f, 0.5f, 0.7f));

    return image;
  }

  private BufferedImage buildExpectedImage()
  {
    BufferedImage image = new BufferedImage(2, 2, BufferedImage.TYPE_INT_ARGB);
    image.setRGB(0, 0, Color.HSBtoRGB(0.16f, 0.2f, 0.3f));
    image.setRGB(0, 1, Color.HSBtoRGB(0.45f, 0.5f, 0.6f));
    image.setRGB(1, 0, Color.HSBtoRGB(0.75f, 0.8f, 0.9f));
    image.setRGB(1, 1, Color.HSBtoRGB(0.3f, 0.55f, 0.75f));

    return image;
  }
}
