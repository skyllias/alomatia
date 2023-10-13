package org.skyllias.alomatia.filter.convolve;

import java.util.Arrays;

import org.apache.commons.lang.ArrayUtils;

/** KernelDataFactory that returns kernels from another factory multiplied by -1. */

public class NegativeKernelDataFactory implements KernelDataFactory
{
  private final KernelDataFactory originalKernelDataFactory;

//==============================================================================

  public NegativeKernelDataFactory(KernelDataFactory originalKernelDataFactory)
  {
    this.originalKernelDataFactory = originalKernelDataFactory;
  }

//==============================================================================

  @Override
  public float[][] getKernelData()
  {
    return Arrays.stream(originalKernelDataFactory.getKernelData())
          .map(row -> Arrays.stream(ArrayUtils.toObject(row))
               .map(cell -> -cell)
               .toArray(Float[]::new))
          .map(ArrayUtils::toPrimitive)
          .toArray(float[][]::new);
  }

//------------------------------------------------------------------------------

}
