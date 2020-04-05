
package org.skyllias.alomatia.filter.convolve;

/** {@link KernelDataFactory} that washes colours, leaving only the zones with more contrast. */

public class NucelarWashKernelDataFactory implements KernelDataFactory
{
//==============================================================================

  @Override
  public float[][] getKernelData()
  {
    return new float[][] {{-0.75f, -0.75f, -0.75f},
                          {-0.75f,  8.25f, -0.75f},
                          {-0.75f, -0.75f, -0.75f}};
  }

//------------------------------------------------------------------------------

}
