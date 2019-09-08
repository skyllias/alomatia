
package org.skyllias.alomatia.filter.buffered.patch;

import java.awt.*;

/** Comparator of {@link Color}s.
 *  A colour must be similar to itself and if A is similar to B then B is
 *  similar to A, but if A and B are similar and B and C are too, A may or may
 *  not be similar to C. */

public interface ColourSimilarity
{
  boolean areSimilar(Color aColor, Color anotherColor);
}
