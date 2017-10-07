
package org.skyllias.alomatia.filter.factor;

/** Modifier of a magnitude defined in the [0, 1] interval that increases or
 *  decreases it without ebbing out. It is parametrized by a real number so that
 *  a 0 does not change the original magnitude, a negative decreases it and a
 *  positive increases it. */

public interface UnitFactor
{
  float apply(float magnitude);
}
