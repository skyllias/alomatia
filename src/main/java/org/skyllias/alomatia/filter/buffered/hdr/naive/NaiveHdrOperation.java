
package org.skyllias.alomatia.filter.buffered.hdr.naive;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.ImageFilter;

import org.skyllias.alomatia.filter.buffered.BufferedImageOperation;
import org.skyllias.alomatia.filter.buffered.FilteredBufferedImageGenerator;

/** {@link BufferedImageOperation} that applies some blurring filter and then
 *  modifies each of the channels of all pixels by multiplying the normalized
 *  values of the blurred and the original images.
 *  The real effect probably has more to do with colour contrast than with real
 *  HDR, but anyway it has been copied from:
 *  https://github.com/alhazmy13/ImageFilters/blob/master/library/src/main/jni/HDRFilter.cpp */

public class NaiveHdrOperation implements BufferedImageOperation
{
  private final ImageFilter blurringFilter;
  private final FilteredBufferedImageGenerator filteredImageGenerator;

//==============================================================================

  public NaiveHdrOperation(ImageFilter blurringFilter,
                           FilteredBufferedImageGenerator filteredImageGenerator)
  {
    this.blurringFilter         = blurringFilter;
    this.filteredImageGenerator = filteredImageGenerator;
  }

//==============================================================================

  @Override
  public void filter(BufferedImage inputImage, BufferedImage outputImage)
  {
    BufferedImage blurredImage = getBlurredImage(inputImage);

    for (int x = 0; x < inputImage.getWidth(); x++)
    {
      for (int y = 0; y < inputImage.getHeight(); y++)
      {
        setColour(x, y, outputImage, inputImage, blurredImage);
      }
    }
  }

//------------------------------------------------------------------------------

  /* Returns a BufferedImage obtained by the application of blurringFilter to image. */

  private BufferedImage getBlurredImage(BufferedImage image)
  {
    return filteredImageGenerator.generate(image, blurringFilter);
  }

//------------------------------------------------------------------------------

  /* Sets the colour of the pixel at (x, y) in dest as a function of the colours
   * at the same location of src and blurredImage. */

  private void setColour(int x, int y, BufferedImage outputImage,
                         BufferedImage inputImage, BufferedImage blurredImage)
  {
    Color sourceColour  = new Color(inputImage.getRGB(x, y));
    Color blurredColour = new Color(blurredImage.getRGB(x, y));

    int red   = getMultipliedValue(sourceColour.getRed(),   blurredColour.getRed());
    int green = getMultipliedValue(sourceColour.getGreen(), blurredColour.getGreen());
    int blue  = getMultipliedValue(sourceColour.getBlue(),  blurredColour.getBlue());

    Color destinationColour = new Color(red, green, blue);
    outputImage.setRGB(x, y, destinationColour.getRGB());
  }

//------------------------------------------------------------------------------

  /* Returns the channel component for a pixel that has sourceValue in the
   * source image and blurredValue in the blurred image. */

  private int getMultipliedValue(int sourceValue, int blurredValue)
  {
    final float MAX_VALUE      = 0xFF;
    final float HALF_THRESHOLD = 0.5f;

    float smoothedValue = blurredValue / MAX_VALUE;

    if (smoothedValue <= HALF_THRESHOLD) return (int) (2 * smoothedValue * sourceValue);
    else                                 return (int) (MAX_VALUE - 2 * (1 - smoothedValue) * (MAX_VALUE - sourceValue));
  }

//------------------------------------------------------------------------------

}
