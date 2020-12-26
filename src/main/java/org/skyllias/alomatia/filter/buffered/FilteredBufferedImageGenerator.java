
package org.skyllias.alomatia.filter.buffered;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ImageFilter;
import java.awt.image.RenderedImage;

import org.skyllias.alomatia.filter.FilteredImageGenerator;

/** Generator of {@link BufferedImage}s obtained after applying an
 *  {@link ImageFilter} to an input image. */

public class FilteredBufferedImageGenerator
{
  private final FilteredImageGenerator filteredImageGenerator;

//==============================================================================

  public FilteredBufferedImageGenerator(FilteredImageGenerator filteredImageGenerator)
  {
    this.filteredImageGenerator = filteredImageGenerator;
  }

//==============================================================================

  /** Returns a new image resulting from applying imageFilter to inputImage,
   *  ensuring that it is buffered. */

  public BufferedImage generate(Image inputImage, ImageFilter imageFilter)
  {
    Image filteredImage = filteredImageGenerator.generate(inputImage, imageFilter);

    if (filteredImage instanceof RenderedImage) return (BufferedImage) filteredImage;   // perhaps it is already

    BufferedImage bufferedImage = new BufferedImage(filteredImage.getWidth(null),
                                                    filteredImage.getHeight(null),
                                                    BufferedImage.TYPE_INT_ARGB);
    Graphics2D graphics         = bufferedImage.createGraphics();
    graphics.drawImage(filteredImage, 0, 0, null);
    graphics.dispose();

    return bufferedImage;
  }

//------------------------------------------------------------------------------

}
