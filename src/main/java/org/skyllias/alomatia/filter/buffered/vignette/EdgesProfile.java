
package org.skyllias.alomatia.filter.buffered.vignette;

/** Profile that sums the effects of a horizontal power profile and a
 *  vertical power profile. */

public class EdgesProfile implements VignetteProfile
{
  private VignetteProfile delegate = new SummingProfile(new PowerProfile(true),
                                                        new PowerProfile(false));

//==============================================================================

  @Override
  public float getVignetteEffect(int x, int y, int width, int height)
  {
    return delegate.getVignetteEffect(x, y, width, height);
  }

//------------------------------------------------------------------------------

}
