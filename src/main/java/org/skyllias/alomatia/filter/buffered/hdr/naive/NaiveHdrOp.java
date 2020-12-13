
package org.skyllias.alomatia.filter.buffered.hdr.naive;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ImageFilter;
import java.awt.image.RenderedImage;

import org.skyllias.alomatia.filter.FilteredImageGenerator;
import org.skyllias.alomatia.filter.buffered.BasicBufferedImageOp;

/** {@link BufferedImageOp} that applies some blurring filter and then modifies
 *  each of the channels of all pixels by multiplying the normalized values of
 *  the blurred and the original images.
 *  The real effect probably has more to do with colour contrast than with real
 *  HDR, but anyway it has been copied from:
 *  https://github.com/alhazmy13/ImageFilters/blob/master/library/src/main/jni/HDRFilter.cpp */

public class NaiveHdrOp extends BasicBufferedImageOp
{
  private final ImageFilter blurringFilter;
  private final FilteredImageGenerator filteredImageGenerator;

//==============================================================================

  public NaiveHdrOp(ImageFilter blurringFilter,
                    FilteredImageGenerator filteredImageGenerator)
  {
    this.blurringFilter         = blurringFilter;
    this.filteredImageGenerator = filteredImageGenerator;
  }

//==============================================================================

  @Override
  protected void doFilter(BufferedImage src, BufferedImage dest)
  {
    BufferedImage blurredImage = getBlurredImage(src);

    for (int x = 0; x < src.getWidth(); x++)
      for (int y = 0; y < src.getHeight(); y++)
        setColour(x, y, dest, src, blurredImage);
  }

//------------------------------------------------------------------------------

  /* Returns a BufferedImage obtained by the application of blurringFilter to image. */

  private BufferedImage getBlurredImage(BufferedImage image)
  {
    Image blurredImage = filteredImageGenerator.generate(image, blurringFilter);

    if (blurredImage instanceof RenderedImage) return (BufferedImage) blurredImage;   // perhaps it is already

    BufferedImage bufferedImage = new BufferedImage(blurredImage.getWidth(null),
                                                    blurredImage.getHeight(null),
                                                    BufferedImage.TYPE_INT_ARGB);
    Graphics2D graphics         = bufferedImage.createGraphics();
    graphics.drawImage(blurredImage, 0, 0, null);
    graphics.dispose();
    return bufferedImage;
  }

//------------------------------------------------------------------------------

  /* Sets the colour of the pixel at (x, y) in dest as a function of the colours
   * at the same location of src and blurredImage. */

  private void setColour(int x, int y, BufferedImage dest, BufferedImage src,
                         BufferedImage blurredImage)
  {
    Color sourceColor  = new Color(src.getRGB(x, y));
    Color blurredColor = new Color(blurredImage.getRGB(x, y));

    int red   = getMultipliedValue(sourceColor.getRed(),   blurredColor.getRed());
    int green = getMultipliedValue(sourceColor.getGreen(), blurredColor.getGreen());
    int blue  = getMultipliedValue(sourceColor.getBlue(),  blurredColor.getBlue());

    Color destinationColor = new Color(red, green, blue);
    dest.setRGB(x, y, destinationColor.getRGB());
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
