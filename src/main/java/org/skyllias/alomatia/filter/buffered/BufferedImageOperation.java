
package org.skyllias.alomatia.filter.buffered;

import java.awt.image.BufferedImage;

/** Filter operation to be applied by a {@link SameSizeBufferedImageOperation}. */

public interface BufferedImageOperation
{

  /** Assuming that none of the images is null, draws to outputImage the outcome
   *  of the application of the filtering operation to inputImage.
   *  inputImage is left untouched. */

  void filter(BufferedImage inputImage, BufferedImage outputImage);
}
