
package org.skyllias.alomatia.filter.buffered.surround;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/** Calculator of the threshold that would turn (approximately) half the pixels
 *  of an image to one colour and half to another in a {@link BlackOrWhiteSelector}.
 *  It delegates the work to a {@link LightMeter} but the light of each row is
 *  calculated separately to reduce the amount of resources consumed. */

public class DynamicThresholdCalculator
{
  private final LightMeter lightMeter;

//==============================================================================

  public DynamicThresholdCalculator(LightMeter lightMeter)
  {
    this.lightMeter = lightMeter;
  }

//==============================================================================

  /** Returns the average light of all pixels in the image. */

  public float getLight(BufferedImage image)
  {
    return (float) IntStream.range(0, image.getHeight())
                            .mapToDouble(row -> getLightnessOfRow(image, row))
                            .average()
                            .orElse(0.5);
  }

//------------------------------------------------------------------------------

  /* Returns the average light of the pixels in the (0-based) row in the image. */

  private float getLightnessOfRow(BufferedImage image, int row)
  {
    int[] rowRgbs                = image.getRGB(0, row, image.getWidth(), 1,
                                                null, 0, image.getWidth());
    Collection<Color> rowColours = Arrays.stream(rowRgbs)
                                         .mapToObj(Color::new)
                                         .collect(Collectors.toList());
    return lightMeter.getLight(rowColours);
  }

//------------------------------------------------------------------------------

}
