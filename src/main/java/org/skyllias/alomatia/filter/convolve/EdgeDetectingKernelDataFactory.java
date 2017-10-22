
package org.skyllias.alomatia.filter.convolve;

import java.awt.image.*;

/** {@link KernelDataFactory} that turns black all pixels similar to their neighbours.
 * Mind that {@link ConvolveOp} affects all the channels, including the alpha,
 * and that kernels provided by this factory have a total volume of 0, which
 * means that resulting images are fully transparent. */

public class EdgeDetectingKernelDataFactory implements KernelDataFactory
{
//==============================================================================

  @Override
  public float[][] getKernelData()
  {
    return new float[][] {{-1, -1, -1},
                          {-1,  8, -1},
                          {-1, -1, -1}};
  }

//------------------------------------------------------------------------------

}
