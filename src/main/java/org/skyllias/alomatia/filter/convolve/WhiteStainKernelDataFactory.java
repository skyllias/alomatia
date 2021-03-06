
package org.skyllias.alomatia.filter.convolve;

/** {@link KernelDataFactory} that turns a pixel white if it or any of its
 *  neighbours is not close to black.
 *  The real colour should not be white, as each channel is maximized separately,
 *  but in images generated by a simple edge detector filter, which are those
 *  where this kernel is expected to be composed, seldom all the nearby pixels
 *  will have only one or two channels. */

public class WhiteStainKernelDataFactory implements KernelDataFactory
{
  private final float intensity;

//==============================================================================

  /** Creates a kernel data factory with the passed intensity.
   *  Values above 1 make the image whiter and thicker edges, while values
   *  below 1 make if darker and with fainter edges. */

  public WhiteStainKernelDataFactory(float intensity)
  {
    this.intensity = intensity;
  }

//==============================================================================

  @Override
  public float[][] getKernelData()
  {
    return new float[][] {{intensity * 0.5f, intensity * 1, intensity * 0.5f},
                          {intensity * 1,    intensity * 1, intensity * 1},
                          {intensity * 0.5f, intensity * 1, intensity * 0.5f}};
  }

//------------------------------------------------------------------------------

}
