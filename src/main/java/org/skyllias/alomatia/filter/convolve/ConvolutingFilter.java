
package org.skyllias.alomatia.filter.convolve;

import java.awt.image.*;

/** Filter that applies a {@link Kernel}-based convolution.
 *  Mind that convolutions can be very slow with large kernel data. */

public class ConvolutingFilter extends BufferedImageFilter
{
//==============================================================================

  public ConvolutingFilter(Kernel kernel) {super(new ConvolveOp(kernel));}

//------------------------------------------------------------------------------

  /** Creates a new filter that uses a {@link Kernel} derived from the kernel
   *  data provided by dataFactory. */

  public ConvolutingFilter(KernelDataFactory dataFactory) {super(new ConvolveOp(getKernel(dataFactory.getKernelData())));}

//==============================================================================

  /* Returns a Kernel with the values from matrixData.
   * If it is a jagged or empty array, an {@link IndexOutOfBoundsException}
   * may be thrown. */

  private static Kernel getKernel(float[][] matrixData)
  {
    int width          = matrixData.length;
    int height         = matrixData[0].length;
    int total          = width * height;
    float[] linearData = new float[total];

    for (int i = 0; i < width; i++)
    {
      for (int j = 0; j < height; j++) linearData[i * width + j] = matrixData[i][j];
    }
    return new Kernel(width, height, linearData);
  }

//------------------------------------------------------------------------------

}