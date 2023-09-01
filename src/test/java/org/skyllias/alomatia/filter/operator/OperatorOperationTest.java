
package org.skyllias.alomatia.filter.operator;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.ImageFilter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.skyllias.alomatia.filter.buffered.FilteredBufferedImageGenerator;
import org.skyllias.alomatia.test.ImageUtils;

@RunWith(MockitoJUnitRunner.class)
public class OperatorOperationTest
{
  @Mock
  private ImageFilter horizontalPositiveConvolvingFilter;
  @Mock
  private ImageFilter horizontalNegativeConvolvingFilter;
  @Mock
  private ImageFilter verticalPositiveConvolvingFilter;
  @Mock
  private ImageFilter verticalNegativeConvolvingFilter;
  @Mock
  private FilteredBufferedImageGenerator filteredImageGenerator;

  private OperatorOperation operation;

  /* Cannot rely on @InjectMocks because it does not respect the order of parameters. */

  @Before
  public void setUp()
  {
    operation = new OperatorOperation(filteredImageGenerator,
                                      horizontalPositiveConvolvingFilter,
                                      horizontalNegativeConvolvingFilter,
                                      verticalPositiveConvolvingFilter,
                                      verticalNegativeConvolvingFilter);
  }


  @Test
  public void test()
  {
    BufferedImage srcImage = buildInputImage();

    when(filteredImageGenerator.generate(srcImage, horizontalPositiveConvolvingFilter))
        .thenReturn(buildHorizontalPositiveConvolvedImage());
    when(filteredImageGenerator.generate(srcImage, horizontalNegativeConvolvingFilter))
        .thenReturn(buildHorizontalNegativeConvolvedImage());
    when(filteredImageGenerator.generate(srcImage, verticalPositiveConvolvingFilter))
        .thenReturn(buildVerticalPositiveConvolvedImage());
    when(filteredImageGenerator.generate(srcImage, verticalNegativeConvolvingFilter))
        .thenReturn(buildVerticalNegativeConvolvedImage());

    BufferedImage destImage = new BufferedImage(3, 3, BufferedImage.TYPE_INT_ARGB);
    operation.filter(srcImage, destImage);

    assertTrue(ImageUtils.areEqual(destImage, buildExpectedImage()));
  }


  private BufferedImage buildInputImage()
  {
    BufferedImage image = new BufferedImage(3, 3, BufferedImage.TYPE_INT_ARGB);
    image.setRGB(0, 0, Color.HSBtoRGB(0.1f, 0.2f, 0.3f));
    image.setRGB(0, 1, Color.HSBtoRGB(0.4f, 0.5f, 0.6f));
    image.setRGB(0, 2, Color.HSBtoRGB(0.4f, 0.5f, 0.6f));
    image.setRGB(1, 0, Color.HSBtoRGB(0.7f, 0.8f, 0.9f));
    image.setRGB(1, 1, Color.HSBtoRGB(0.35f, 0.55f, 0.75f));
    image.setRGB(1, 2, Color.HSBtoRGB(0.35f, 0.55f, 0.75f));
    image.setRGB(2, 0, Color.HSBtoRGB(0.7f, 0.8f, 0.9f));
    image.setRGB(2, 1, Color.HSBtoRGB(0.35f, 0.55f, 0.75f));
    image.setRGB(2, 2, Color.HSBtoRGB(0.35f, 0.55f, 0.75f));

    return image;
  }

  private BufferedImage buildHorizontalPositiveConvolvedImage()
  {
    BufferedImage image = new BufferedImage(3, 3, BufferedImage.TYPE_INT_ARGB);
    image.setRGB(0, 0, new Color(0, 0, 0).getRGB());
    image.setRGB(0, 1, new Color(100, 0, 0).getRGB());
    image.setRGB(0, 2, new Color(100, 200, 0).getRGB());
    image.setRGB(1, 0, new Color(0, 0, 0).getRGB());
    image.setRGB(1, 1, new Color(100, 100, 100).getRGB());
    image.setRGB(1, 2, new Color(0, 100, 0).getRGB());
    image.setRGB(2, 0, new Color(0, 0, 0).getRGB());
    image.setRGB(2, 1, new Color(0, 0, 3).getRGB());
    image.setRGB(2, 2, new Color(0, 0, 0).getRGB());

    return image;
  }

  private BufferedImage buildHorizontalNegativeConvolvedImage()
  {
    BufferedImage image = new BufferedImage(3, 3, BufferedImage.TYPE_INT_ARGB);
    image.setRGB(0, 0, new Color(0, 0, 0).getRGB());
    image.setRGB(0, 1, new Color(0, 50, 0).getRGB());
    image.setRGB(0, 2, new Color(0, 0, 0).getRGB());
    image.setRGB(1, 0, new Color(0, 0, 0).getRGB());
    image.setRGB(1, 1, new Color(0, 0, 0).getRGB());
    image.setRGB(1, 2, new Color(0, 0, 0).getRGB());
    image.setRGB(2, 0, new Color(5, 4, 0).getRGB());
    image.setRGB(2, 1, new Color(30, 3, 0).getRGB());
    image.setRGB(2, 2, new Color(0, 0, 100).getRGB());

    return image;
  }

  private BufferedImage buildVerticalPositiveConvolvedImage()
  {
    BufferedImage image = new BufferedImage(3, 3, BufferedImage.TYPE_INT_ARGB);
    image.setRGB(0, 0, new Color(255, 50, 0).getRGB());
    image.setRGB(0, 1, new Color(0, 150, 0).getRGB());
    image.setRGB(0, 2, new Color(200, 100, 0).getRGB());
    image.setRGB(1, 0, new Color(0, 0, 0).getRGB());
    image.setRGB(1, 1, new Color(0, 0, 0).getRGB());
    image.setRGB(1, 2, new Color(50, 50, 50).getRGB());
    image.setRGB(2, 0, new Color(10, 0, 0).getRGB());
    image.setRGB(2, 1, new Color(5, 5, 5).getRGB());
    image.setRGB(2, 2, new Color(0, 0, 0).getRGB());

    return image;
  }

  private BufferedImage buildVerticalNegativeConvolvedImage()
  {
    BufferedImage image = new BufferedImage(3, 3, BufferedImage.TYPE_INT_ARGB);
    image.setRGB(0, 0, new Color(0, 0, 100).getRGB());
    image.setRGB(0, 1, new Color(0, 0, 150).getRGB());
    image.setRGB(0, 2, new Color(0, 0, 255).getRGB());
    image.setRGB(1, 0, new Color(0, 0, 0).getRGB());
    image.setRGB(1, 1, new Color(0, 0, 0).getRGB());
    image.setRGB(1, 2, new Color(0, 0, 0).getRGB());
    image.setRGB(2, 0, new Color(0, 1, 1).getRGB());
    image.setRGB(2, 1, new Color(0, 0, 0).getRGB());
    image.setRGB(2, 2, new Color(0, 255, 0).getRGB());

    return image;
  }

  private BufferedImage buildExpectedImage()
  {
    BufferedImage image = new BufferedImage(3, 3, BufferedImage.TYPE_INT_ARGB);
    image.setRGB(0, 0, new Color(135, 135, 135).getRGB());
    image.setRGB(0, 1, new Color(112, 112, 112).getRGB());
    image.setRGB(0, 2, new Color(210, 210, 210).getRGB());
    image.setRGB(1, 0, new Color(0, 0, 0).getRGB());
    image.setRGB(1, 1, new Color(100, 100, 100).getRGB());
    image.setRGB(1, 2, new Color(60, 60, 60).getRGB());
    image.setRGB(2, 0, new Color(5, 5, 5).getRGB());
    image.setRGB(2, 1, new Color(13, 13, 13).getRGB());
    image.setRGB(2, 2, new Color(91, 91, 91).getRGB());

    return image;
  }

}
