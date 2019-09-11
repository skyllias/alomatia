
package org.skyllias.alomatia.filter.buffered.patch;

import java.awt.Color;

/** ColourSimilarity that regards as similar only equal colours. */

public class ColourEquality implements ColourSimilarity
{
//==============================================================================

  @Override
  public boolean areSimilar(Color aColor, Color anotherColor)
  {
    return aColor.equals(anotherColor);
  }

//------------------------------------------------------------------------------

}
