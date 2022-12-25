
package org.skyllias.alomatia.filter.buffered.vignette;

/** Profile that sums the effects of other {@link VignetteProfile}s.
 *  With typical profiles, this results in dark edges. */

public class SummingProfile implements VignetteProfile
{
  private final VignetteProfile[] profiles;

//==============================================================================

  public SummingProfile(VignetteProfile... factorProfiles) {profiles = factorProfiles;}

//==============================================================================

  /** This takes care of overflows, limiting the result to 1. */

  @Override
  public float getVignetteEffect(int x, int y, int width, int height)
  {
    float total = 0;
    for (VignetteProfile currentProfile : profiles)
    {
      total += currentProfile.getVignetteEffect(x, y, width, height);
    }
    return Math.min(1, total);
  }

//------------------------------------------------------------------------------

}
