
package org.skyllias.alomatia.filter.buffered.vignette;

/** Profile that multiplies the effects of other {@link VignetteProfile}s.
 *  This profile provides cross-like results when the original profiles act
 *  independently on each dimension and produce clear centers and dark edges. */

public class MultiplyingProfile implements VignetteProfile
{
  private final VignetteProfile[] profiles;

//==============================================================================

  public MultiplyingProfile(VignetteProfile... factorProfiles) {profiles = factorProfiles;}

//==============================================================================

  /** As long as all the profiles return values in the [0, 1] interval, this will too. */

  @Override
  public float getVignetteEffect(int x, int y, int width, int height)
  {
    float total = 1;
    for (VignetteProfile currentProfile : profiles)
    {
      total *= currentProfile.getVignetteEffect(x, y, width, height);
    }
    return total;
  }

//------------------------------------------------------------------------------

}
