
package org.skyllias.alomatia.filter.convolve;

import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;

import org.skyllias.alomatia.filter.buffered.SingleFrameBufferedImageFilter;

/** Filter that applies a {@link Kernel}-based convolution.
 *  Mind that convolutions can be very slow with large kernel data.
 *
 *  @deprecated Since Java's implementation only offers two edge handlings
 *  (leave them unchanged or zero them -ie, turn them to black), an
 *  {@link EdgeConvolvingComposedFilter} is recommended instead. */

@Deprecated
public class ConvolutingFilter extends SingleFrameBufferedImageFilter
{
//==============================================================================

  public ConvolutingFilter(Kernel kernel) {super(new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null));}

//------------------------------------------------------------------------------

  /** Creates a new filter that uses a {@link Kernel} derived from the kernel
   *  data provided by dataFactory. */

  public ConvolutingFilter(KernelDataFactory dataFactory) {this(getKernel(dataFactory.getKernelData()));}

//==============================================================================

  /* Returns a Kernel with the values from matrixData.
   * If it is a jagged or empty array, an {@link IndexOutOfBoundsException}
   * may be thrown. */

  private static Kernel getKernel(float[][] matrixData)
  {
    int width          = matrixData[0].length;
    int height         = matrixData.length;
    int total          = width * height;
    float[] linearData = new float[total];
    for (int i = 0; i < height; i++)
    {
      for (int j = 0; j < width; j++) linearData[i * width + j] = matrixData[i][j];
      }
    return new Kernel(width, height, linearData);
  }

//------------------------------------------------------------------------------

}
