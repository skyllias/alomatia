
package org.skyllias.alomatia.filter;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;

/** Generator of {@link Image}s obtained after applying an {@link ImageFilter}
 *  to an input image. */

public class FilteredImageGenerator
{
//==============================================================================

  /** Returns a new image resulting from applying imageFilter to inputImage. */

  public Image generate(Image inputImage, ImageFilter imageFilter)
  {
    ImageProducer producer = new FilteredImageSource(inputImage.getSource(), imageFilter);
    return Toolkit.getDefaultToolkit().createImage(producer);
  }

//------------------------------------------------------------------------------

}
