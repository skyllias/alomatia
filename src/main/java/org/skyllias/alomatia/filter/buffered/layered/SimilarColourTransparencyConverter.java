
package org.skyllias.alomatia.filter.buffered.layered;

import java.awt.Color;

import org.skyllias.alomatia.filter.ColourConverter;

/** {@link ColourConverter} that adds more transparency to colours similar to some target.
 *  The relationship between two colours and the transparency could be
 *  externalized but for the time being it is limited to a single factor that
 *  controls how fast distant colours are considered different. */

public class SimilarColourTransparencyConverter implements ColourConverter
{
  private final Color target;
  private final int similarityFactor;

//==============================================================================

  /** Creates a converter that will make target invisible and distant colours visible.
   *
   *  similarityFactor controls how transparency is decreased as colours are
   *  more different. It can be approximately assimilated to the square of the
   *  Euclidean distance from target in the RGB space over which colours are
   *  retained, with 1 making most colours visible. Non-positive values are
   *  invalid. */

  public SimilarColourTransparencyConverter(Color target, int similarityFactor)
  {
    this.target           = target;
    this.similarityFactor = similarityFactor;
  }

//==============================================================================

  @Override
  public Color convertColour(Color original)
  {
    return new Color(original.getRed(), original.getGreen(), original.getBlue(),
                     getAlpha(original));
  }

//------------------------------------------------------------------------------

  /* Returns the alpha level (between 0 and 255) that original colour will have. */

  private int getAlpha(Color original)
  {
    int redDifference   = target.getRed() - original.getRed();
    int greenDifference = target.getGreen() - original.getGreen();
    int blueDifference  = target.getBlue() - original.getBlue();

    int accumulatedDifference = redDifference * redDifference +
                                greenDifference * greenDifference +
                                blueDifference * blueDifference;
    return Math.min(255, accumulatedDifference / similarityFactor);
  }

//------------------------------------------------------------------------------

}
