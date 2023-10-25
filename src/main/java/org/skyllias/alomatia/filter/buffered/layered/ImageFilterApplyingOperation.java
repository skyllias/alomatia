
package org.skyllias.alomatia.filter.buffered.layered;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ImageFilter;

import org.skyllias.alomatia.filter.FilteredImageGenerator;
import org.skyllias.alomatia.filter.buffered.BufferedImageOperation;

/** {@link BufferedImageOperation} that applies an {@link ImageFilter} to the input.
 *  Useful to reuse existing filters for the lowest layer image. */

public class ImageFilterApplyingOperation implements BufferedImageOperation
{
  private final FilteredImageGenerator filteredImageGenerator;
  private final ImageFilter filter;

//==============================================================================

  public ImageFilterApplyingOperation(FilteredImageGenerator filteredImageGenerator,
                                      ImageFilter filter)
  {
    this.filteredImageGenerator = filteredImageGenerator;
    this.filter                 = filter;
  }

//==============================================================================

  @Override
  public void filter(BufferedImage inputImage, BufferedImage outputImage)
  {
    Graphics2D graphics = outputImage.createGraphics();
    Image filteredImage = filteredImageGenerator.generate(inputImage, filter);
    graphics.drawImage(filteredImage, 0, 0, null);
    graphics.dispose();
  }

//------------------------------------------------------------------------------

}
