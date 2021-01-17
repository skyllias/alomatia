
package org.skyllias.alomatia.filter.buffered;

import java.awt.Dimension;
import java.awt.image.BufferedImage;

/** Filter operation to be applied by a {@link HintlessBufferedImageOp}.
 *  In addition to the pixel-oriented filtering itself, it allows to modify the
 *  size of the destination image. */

public interface ResizableBufferedImageOperation
{
  /** Returns the size of the output image for inputImage. */

  Dimension getOutputImageDimension(BufferedImage inputImage);

  /** Assuming that none of the images is null, draws to outputImage the outcome
   *  of the application of the filtering operation to inputImage.
   *  inputImage is left untouched. */

  void filter(BufferedImage inputImage, BufferedImage outputImage);
}
