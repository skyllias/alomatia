
package org.skyllias.alomatia.filter.hsb;

/** Function over {@link HueDistance#difference(float, float)} that
 *  determines how a hue should be shifted towards a pole.
 *  <p>
 *  The following guidelines are expected but not enforced:
 *  <ul>
 *  <li> attract(0) = 0 (a pole is not modified)
 *  <li> attract(x) = -attract(-x) (the function is antisymmetric, which implies
 *       the condition above)
 *  <li> attract(x) * x >= 0 (poles attract)
 *  <li> attract(x) <= x (attract is moderate, preventing the huw from trespassing
 *       the pole)
 *  <li> attract(x + y) - attract(x) <= 0 for y > 0 and x > some value (attraction
 *       intensity decreases for distant hues)
 *  </ul> */

public interface Attraction
{
  /** Returns how much a hue distant in difference from a pole is to be shifted. */

  float attract(float difference);
}
