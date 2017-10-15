
package org.skyllias.alomatia.filter.convolve;

/** {@link KernelDataFactory} that can sharpen images by substracting part of
 *  the immediate neighbours of evey pixel. */

public class NeighbourSharpKernelDataFactory implements KernelDataFactory
{
  private static final float DEFAULT_NEIGHBOUR_WEIGHT = 0.2f;

  private float neighbourWeight;

//==============================================================================

  /** Uses a default weight. */

  public NeighbourSharpKernelDataFactory() {this(DEFAULT_NEIGHBOUR_WEIGHT);}

//------------------------------------------------------------------------------

  /** weight is the amount to substract from neighbouring pixels and should be
   *  between 0 and 1. */

  public NeighbourSharpKernelDataFactory(float weight) {neighbourWeight = weight;}

//==============================================================================

  @Override
  public float[][] getKernelData()
  {
    final float VOLUME = 1f;

    return new float[][] {{0, -neighbourWeight, 0},
                          {-neighbourWeight, VOLUME + 4 * neighbourWeight, -neighbourWeight},
                          {0, -neighbourWeight, 0}};
  }

//------------------------------------------------------------------------------

}
