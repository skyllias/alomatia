
package org.skyllias.alomatia.filter.rgb;

import java.awt.*;

import org.skyllias.alomatia.filter.*;

/** Filter that divides each RGB channel in two or more buckets and quantifies
 *  all the values inside each bucket into one single value. */

public class RgbPosterizer extends BasicColorFilter
{
  private int buckets;
  private float bucketSize;
  private boolean center;

//==============================================================================

  /** Same as RgbPosterizer(amountOfBuckets, false); */

  public RgbPosterizer(int amountOfBuckets)
  {
    this(amountOfBuckets, false);
  }

//------------------------------------------------------------------------------

  /** Creates a posterizer with amountOfBuckets divisions in each channel. The
   *  amount should be at least 2 (producing at most 8 different colours) and
   *  smaller than 255 (producing exactly the same input colours), with the most
   *  acceptable results around 10.
   *  If centerValue, then the resulting value in each bucket is the center.
   *  Else, it is moved depending on the position of the corresponding bucket:
   *  The values in the first bucket are moved to 0, the values in the last
   *  bucket are moved to 255, the values in the middle bucket are moved to 127,
   *  and so on. */

  public RgbPosterizer(int amountOfBuckets, boolean centerValue)
  {
    final float FULL_RANGE = 256f;                                              // considering the interval [0, 256). See getQuantized

    buckets = amountOfBuckets;
    center  = centerValue;

    bucketSize = FULL_RANGE / amountOfBuckets;
  }

//==============================================================================

  @Override
  public Color filterColor(Color original)
  {
    int red   = getQuantized(original.getRed());
    int green = getQuantized(original.getGreen());
    int blue  = getQuantized(original.getBlue());
    return new Color(red, green, blue);
  }

//------------------------------------------------------------------------------

  /* Returns the quantization of the passed channel value (between 0 and 255)
   * according to the bucket it falls into. */

  private int getQuantized(int value)
  {
    final int MAX_VALUE = 255;                                                  // because the interval above was [0, 256)

    int correspondingBucket = (int) (value / bucketSize);

    float bucketStart          = bucketSize * correspondingBucket;
    float positionInsideBucket = center? (bucketSize / 2):                                    // the middle
                                         (bucketSize * correspondingBucket) / (buckets - 1);  // moved according to the corresponding bucket
    return Math.min(MAX_VALUE, Math.round(bucketStart + positionInsideBucket));
  }

//------------------------------------------------------------------------------

}
