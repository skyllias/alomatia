
package org.skyllias.alomatia.filter.convolve;

/** {@link KernelDataFactory} that uses an almost circular-paraboloid shaped matrix to blur.
 *  No kernel value is negative, and the centers of the edges get an almost zero value.
 *  Given the same matrix size, this produces neater blurs than {@link BoxBlurKernelDataFactory}. */

public class ParaboloidBlurKernelDataFactory implements KernelDataFactory
{
  private final int sideSize;

//==============================================================================

  /** width must be bigger than 1. Odd values produce more symmetric results
   *  than even, but both are accepted. */

  public ParaboloidBlurKernelDataFactory(int width) {sideSize = width;}

//==============================================================================

  @Override
  public float[][] getKernelData()
  {
    float[][] data = new float[sideSize][sideSize];

    float center = (sideSize - 1f) / 2f;
    float height = (center + 1) * (center + 1);                                 // this is the height of the paraboloid before normalization at the center, calculated so that the first pixels outside the kernel would get a zero value
    float volume = 0;
    for (int i = 0; i < sideSize; i++)
    {
      for (int j = 0; j < sideSize; j++)
      {
        float horizontalSquare = (center - i) * (center - i);
        float verticalSquare   = (center - j) * (center - j);
        float value            = Math.max(0, height - horizontalSquare - verticalSquare);

        data[i][j] = value;
        volume    += value;
      }
    }

    for (int i = 0; i < sideSize; i++)                                          // normalize so that the total volume is 1
    {
      for (int j = 0; j < sideSize; j++) data[i][j] = data[i][j] / volume;
    }
    return data;
  }

//------------------------------------------------------------------------------

}
