
package org.skyllias.alomatia.filter.convolve;

/** {@link KernelDataFactory} that uses a flat square to blur. */

public class BoxBlurKernelDataFactory implements KernelDataFactory
{
  private final int boxSize;

//==============================================================================

  /** boxWidth must be bigger than 1. Odd values produce more symmetric results
   *  than even, but both are accepted. */

  public BoxBlurKernelDataFactory(int boxWidth) {boxSize = boxWidth;}

//==============================================================================

  @Override
  public float[][] getKernelData()
  {
    float[][] data = new float[boxSize][boxSize];
    float value    = 1f / (boxSize * boxSize);

    for (int i = 0; i < boxSize; i++)
    {
      for (int j = 0; j < boxSize; j++) data[i][j] = value;
    }
    return data;
  }

//------------------------------------------------------------------------------

}
