
package org.skyllias.alomatia.filter.convolve;

import java.awt.image.Kernel;

/** Provider of {@link Kernel} data matrices, suitable to be passed to
 *  {@link ConvolutingFilter#ConvolutingFilter(KernelDataFactory)}. */

public interface KernelDataFactory
{
  /** Returns a new rectangular matrix with data to generate a convoluting kernel with. */

  float[][] getKernelData();
}
