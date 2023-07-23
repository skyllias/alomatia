
package org.skyllias.alomatia.filter.convolve.emboss;

import org.skyllias.alomatia.filter.convolve.EdgeDetectingKernelDataFactory;
import org.skyllias.alomatia.filter.convolve.KernelDataFactory;

/** {@link KernelDataFactory} that prepares an embossed surface illuminated from
 *  the top left corner.
 *  Mind that the same considerations in {@link EdgeDetectingKernelDataFactory}
 *  about the alpha channel apply here.
 *  By default, on average half the pixels convolved by this kernel will be black,
 *  because the kernel volume is zero and negative values in RGB are trimmed to
 *  0 by ConvolveOp. To prevent it, a volume can be provided. With it, on average
 *  dark pixels will still be black, but light pixels will be light.
 *  By default too, the theoretical range for each channel before trimming
 *  is (- 3 * 255, 3 * 255). If smaller or greater ranges are wanted (ie, lower
 *  or higher effects effects of contrast), a slope can be provided. */

public class EmbossKernelDataFactory implements KernelDataFactory
{
  private final float slope, volume;

//==============================================================================

  /** Creates a factory that produces a kernel with a total volume of kernelVolume
   *  and a slope of gradientSlope.
   *  @param gradientSlope 0 produces a flat kernel, while 1 is the default value. */

  public EmbossKernelDataFactory(float kernelVolume, float gradientSlope)
  {
    volume = kernelVolume;
    slope  = gradientSlope;
  }

//==============================================================================

  @Override
  public float[][] getKernelData()
  {
    return new float[][] {{-slope + volume/9, -slope + volume/9,     0 + volume/9},
                          {-slope + volume/9,      0 + volume/9, slope + volume/9},
                          {     0 + volume/9,  slope + volume/9, slope + volume/9}};
  }

//------------------------------------------------------------------------------

}
