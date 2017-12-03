
package org.skyllias.alomatia.filter.hsb;

import org.skyllias.alomatia.filter.rgb.*;

/** Function [0, 1] -> [0, 1] that divides the range in two or more equal-sized
 *  buckets and returns the same result to all the values in each bucket.
 *  Similar to what is done in {@link RgbPosterizer} but with floats. */

public class UnitQuantizer
{
  private int buckets;
  private float bucketSize;
  private boolean center;

//==============================================================================

  public UnitQuantizer(int amountOfBuckets)
  {
    this(amountOfBuckets, false);
  }

//------------------------------------------------------------------------------

  /** Creates a quantizer with amountOfBuckets divisions. The amount should be
   *  at least 2.
   *  If centerValue, then the resulting value in each bucket is the center.
   *  Else, it is moved depending on the position of the corresponding bucket:
   *  The values in the first bucket are moved to 0, the values in the last
   *  bucket are moved to 1, the values in the middle bucket are moved to 0.5,
   *  and so on. */

  public UnitQuantizer(int amountOfBuckets, boolean centerValue)
  {
    buckets = amountOfBuckets;
    center  = centerValue;

    bucketSize = 1f / amountOfBuckets;
  }

//==============================================================================

  /* Returns the quantization of the passed channel value (between 0 and 255)
   * according to the bucket it falls into. */

  public float getQuantized(float value)
  {
    int correspondingBucket = (int) (value / bucketSize);

    float bucketStart          = bucketSize * correspondingBucket;
    float positionInsideBucket = center? (bucketSize / 2):                                    // the middle
                                         (bucketSize * correspondingBucket) / (buckets - 1);  // moved according to the corresponding bucket
    return bucketStart + positionInsideBucket;
  }

//------------------------------------------------------------------------------

}
