
package org.skyllias.alomatia.filter.buffered;

import java.awt.Dimension;
import java.awt.image.BufferedImage;

/** {@link ResizableBufferedImageOperation} that keeps the image size.
 *  The filter operation is delegated to a BufferedImageOperation. */

public class SameSizeBufferedImageOperation implements ResizableBufferedImageOperation
{
  private final BufferedImageOperation bufferedImageOperation;

//==============================================================================

  public SameSizeBufferedImageOperation(BufferedImageOperation bufferedImageOperation)
  {
    this.bufferedImageOperation = bufferedImageOperation;
  }

//==============================================================================

  /** Just calls doFilter ensuring that the destination is not null. */

  @Override
  public Dimension getOutputImageDimension(BufferedImage src)
  {
    return new Dimension(src.getWidth(), src.getHeight());
  }

//------------------------------------------------------------------------------

  /** Delegates the filter to bufferedImageOperaton. */

  @Override
  public void filter(BufferedImage inputImage, BufferedImage outputImage)
  {
    bufferedImageOperation.filter(inputImage, outputImage);
  }

//------------------------------------------------------------------------------

}
