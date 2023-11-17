
package org.skyllias.alomatia.filter.convolve;

/** Helper class to generate two {@link KernelDataFactory} instances, one with
 *  only horizontal component and one with only vertical component, which
 *  composed form a separable kernel. */

public class SeparatedKernelDataFactoryBuilder
{
//==============================================================================

  /** Returns two factories, one horizontal and one vertical,
   *  with the same profile and length. */

  public KernelDataFactory[] createSeparatedKernelDataFactories(BlurLineProfile profile, int length)
  {
    float[] lineProfile = profile.getProfile(length);
    return new KernelDataFactory[] {new HorizontalKernelDataFactory(lineProfile),
                                    new VerticalKernelDataFactory(lineProfile)};
  }

//------------------------------------------------------------------------------

  /** Returns two factories, one horizontal and one vertical,
   *  with the same Gaussian profile and length. */

  public KernelDataFactory[] createSeparatedGaussianKernelDataFactories(int length)
  {
    return createSeparatedKernelDataFactories(new CenteredGaussianBlurProfile(), length);
  }

//------------------------------------------------------------------------------

  /** Returns a factory for a horizontal Gaussian profile and length. */

  public KernelDataFactory createHorizontalGaussianKernelDataFactory(int length)
  {
    return new HorizontalKernelDataFactory(new CenteredGaussianBlurProfile().getProfile(length));
  }

//------------------------------------------------------------------------------

  /** Returns a factory for a vertical Gaussian profile and length. */

  public KernelDataFactory createVerticalGaussianKernelDataFactory(int length)
  {
    return new VerticalKernelDataFactory(new CenteredGaussianBlurProfile().getProfile(length));
  }

//------------------------------------------------------------------------------

//******************************************************************************

  private static class HorizontalKernelDataFactory implements KernelDataFactory
  {
    public final float[] lineProfile;

    public HorizontalKernelDataFactory(float[] lineProfile)
    {
      this.lineProfile = lineProfile;
    }

    @Override
    public float[][] getKernelData()
    {
      return new float[][] {lineProfile};
    }

  }

//******************************************************************************

  private static class VerticalKernelDataFactory implements KernelDataFactory
  {
    public final float[] lineProfile;

    public VerticalKernelDataFactory(float[] lineProfile)
    {
      this.lineProfile = lineProfile;
    }

    @Override
    public float[][] getKernelData()
    {
      float[][] result = new float[lineProfile.length][1];
      for (int i = 0; i < lineProfile.length; i++) result[i][0] = lineProfile[i];
      return result;
    }

  }

}
