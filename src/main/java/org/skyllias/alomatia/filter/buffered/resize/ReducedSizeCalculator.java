
package org.skyllias.alomatia.filter.buffered.resize;

import java.awt.Dimension;

/** Calculator of the size that images are reduced to before being stretched
 *  back to their original size.
 *  The shortest dimension of the reduced size is a fixed amount of pixels, and
 *  the longest one keeps the original aspect ratio. Sizes are never smaller
 *  than a minimum amount of pixels, and images are never enlarged. */

public class ReducedSizeCalculator
{
  private static final int MIN_LENGTH = 2;

  private final int shortestLength;

//==============================================================================

  /** Creates a calculator of sizes whose shortest dimension is shortestLength
   *  pixels. It should be greater than 1: the smaller, the blurrier the outcome. */

  public ReducedSizeCalculator(int shortestLength) {this.shortestLength = shortestLength;}

//==============================================================================

  /** Returns the size that an image of the passed dimensions should be reduced
   *  to, with both dimensions positive and, whenever the original size allows
   *  it, not smaller than MIN_LENGTH. */

  public Dimension getReducedSize(int originalWidth, int originalHeight)
  {
    float effectiveRatio = getEffectiveRatio(originalWidth, originalHeight);

    return new Dimension(getReducedLength(originalWidth, effectiveRatio),
                         getReducedLength(originalHeight, effectiveRatio));
  }

//------------------------------------------------------------------------------

  /* Returns the ratio to apply to both dimensions so that the shortest one
   * becomes the requested amount of pixels, unless the image is so small that
   * it would have to be enlarged. */

  private float getEffectiveRatio(int originalWidth, int originalHeight)
  {
    int shortestOriginalLength = Math.min(originalWidth, originalHeight);
    float requestedRatio       = ((float) getBoundShortestLength()) / shortestOriginalLength;

    return Math.min(1, requestedRatio);
  }

//------------------------------------------------------------------------------

  /* Returns the requested shortest length, never below MIN_LENGTH. */

  private int getBoundShortestLength() {return Math.max(MIN_LENGTH, shortestLength);}

//------------------------------------------------------------------------------

  /* Returns originalLength multiplied by ratio, never below one pixel. */

  private int getReducedLength(int originalLength, float ratio)
  {
    return Math.max(1, Math.round(originalLength * ratio));
  }

//------------------------------------------------------------------------------

}
