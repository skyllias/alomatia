
package org.skyllias.alomatia.filter.buffered.spectrum;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.skyllias.alomatia.test.matchers.AlomatiaMatchers.sameImage;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.ImageFilter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.skyllias.alomatia.filter.buffered.FilteredBufferedImageGenerator;
import org.skyllias.alomatia.test.ImageUtils;

@RunWith(MockitoJUnitRunner.class)
public class DominantHueShiftOpTest
{
  @Mock
  private MostRelevantHueCalculator mostRelevantHueCalculator;
  @Mock
  private FilteredBufferedImageGenerator filteredBufferedImageGenerator;
  @Mock
  private HueShiftFilterFactory hueShiftFilterFactory;

  @Test
  public void shouldShiftHueToThePositive()
  {
    when(mostRelevantHueCalculator.getMostRelevantHue(argThat(sameImage(buildInputImage()))))
    .thenReturn(0f);

    ImageFilter positiveHueShiftFilter = mock(ImageFilter.class);
    when(hueShiftFilterFactory.getHueShiftFilter(0.25f)).thenReturn(positiveHueShiftFilter);

    when(filteredBufferedImageGenerator.generate(argThat(sameImage(buildInputImage())), eq(positiveHueShiftFilter)))
    .thenReturn(buildOutputImage());


    DominantHueShiftOp imageOp = new DominantHueShiftOp(mostRelevantHueCalculator,
                                                        filteredBufferedImageGenerator,
                                                        hueShiftFilterFactory, 0.25f);
    BufferedImage result = imageOp.filter(buildInputImage(), null);

    assertTrue(ImageUtils.areEqual(result, buildOutputImage()));
  }

  @Test
  public void shouldShiftHueToTheNegative()
  {
    when(mostRelevantHueCalculator.getMostRelevantHue(argThat(sameImage(buildInputImage()))))
      .thenReturn(0.75f);

    ImageFilter positiveHueShiftFilter = mock(ImageFilter.class);
    when(hueShiftFilterFactory.getHueShiftFilter(-0.25f)).thenReturn(positiveHueShiftFilter);

    when(filteredBufferedImageGenerator.generate(argThat(sameImage(buildInputImage())), eq(positiveHueShiftFilter)))
      .thenReturn(buildOutputImage());


    DominantHueShiftOp imageOp = new DominantHueShiftOp(mostRelevantHueCalculator,
                                                        filteredBufferedImageGenerator,
                                                        hueShiftFilterFactory, 0.5f);
    BufferedImage result = imageOp.filter(buildInputImage(), null);

    assertTrue(ImageUtils.areEqual(result, buildOutputImage()));
  }


  private BufferedImage buildInputImage()
  {
    BufferedImage image = new BufferedImage(2, 2, BufferedImage.TYPE_INT_ARGB);
    image.setRGB(0, 0, Color.RED.getRGB());
    image.setRGB(0, 1, Color.GREEN.getRGB());
    image.setRGB(1, 0, Color.BLUE.getRGB());
    image.setRGB(1, 1, Color.YELLOW.getRGB());

    return image;
  }

  private BufferedImage buildOutputImage()
  {
    BufferedImage image = new BufferedImage(2, 2, BufferedImage.TYPE_INT_ARGB);
    image.setRGB(0, 0, Color.CYAN.getRGB());
    image.setRGB(0, 1, Color.GRAY.getRGB());
    image.setRGB(1, 0, Color.WHITE.getRGB());
    image.setRGB(1, 1, Color.BLACK.getRGB());

    return image;
  }

}
