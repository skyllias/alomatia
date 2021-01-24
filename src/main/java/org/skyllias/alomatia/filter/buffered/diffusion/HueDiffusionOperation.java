
package org.skyllias.alomatia.filter.buffered.diffusion;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.ImageFilter;

import org.skyllias.alomatia.filter.buffered.BufferedImageOperation;
import org.skyllias.alomatia.filter.buffered.FilteredBufferedImageGenerator;

/** {@link BufferedImageOperation} that applies some blurring to hues,
 *  maintaining the original values of brightness and saturation in each pixel. */

public class HueDiffusionOperation implements BufferedImageOperation
{
  private final ImageFilter blurringFilter;
  private final FilteredBufferedImageGenerator filteredImageGenerator;

//==============================================================================

  public HueDiffusionOperation(ImageFilter blurringFilter,
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

  private void setColour(int x, int y, BufferedImage dest, BufferedImage src,
                         BufferedImage blurredImage)
  {
    Color sourceColour  = new Color(src.getRGB(x, y));
    Color blurredColour = new Color(blurredImage.getRGB(x, y));

    float[] sourceHsbComponents  = getHsbComponents(sourceColour);
    float[] blurredHsbComponents = getHsbComponents(blurredColour);

    float hue        = blurredHsbComponents[0];
    float saturation = sourceHsbComponents[1];
    float brightness = sourceHsbComponents[2];

    dest.setRGB(x, y, Color.HSBtoRGB(hue, saturation, brightness));
  }

//------------------------------------------------------------------------------

  private float[] getHsbComponents(Color colour)
  {
    int red   = colour.getRed();
    int green = colour.getGreen();
    int blue  = colour.getBlue();

    return Color.RGBtoHSB(red, green, blue, null);
  }

//------------------------------------------------------------------------------

}
