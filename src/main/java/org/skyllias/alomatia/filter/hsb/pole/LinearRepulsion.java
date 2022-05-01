
package org.skyllias.alomatia.filter.hsb.pole;

/** {@link Attraction} that does not comply with the requirement x * attract(x) > 0,
 *  therefore used to avoid certain hues.
 *  Repulsions with multiple poles should avoid overlapping of their ranges, or
 *  some hues may end up in "forbidden" values. */

public class LinearRepulsion implements Attraction
{
  private final float strength;
  private final float range;

//==============================================================================

  /** @param strength Shift suffered by the hue on the pole itself. The resulting
   *  image will lack hues in the range (pole - strength, pole + strength].
   *  @param range Furthest distance where shifts happen. It should be greater
   *  than strength in order to prevent hue cluttering. */

  public LinearRepulsion(float strength, float range)
  {
    this.strength = strength;
    this.range    = range;
  }

//==============================================================================

  @Override
  public float attract(float difference)
  {
    if (Math.abs(difference) >= range) return 0;

    if (difference >= 0) return strength * (difference / range - 1);
    else                 return strength * (difference / range + 1);
  }

//------------------------------------------------------------------------------

}
