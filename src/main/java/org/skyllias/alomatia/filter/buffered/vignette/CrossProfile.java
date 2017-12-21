
package org.skyllias.alomatia.filter.buffered.vignette;

/** Profile that multiplies the effects of a horizontal linear profile and a
 *  vertical linear profile. */

public class CrossProfile implements VignetteProfile
{
  private VignetteProfile delegate = new MultiplyingProfile(new LinearProfile(true),
                                                            new LinearProfile(false));

//==============================================================================

  @Override
  public float getVignetteEffect(int x, int y, int width, int height)
  {
    return delegate.getVignetteEffect(x, y, width, height);
  }

//------------------------------------------------------------------------------
}
