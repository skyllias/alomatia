
package org.skyllias.alomatia.filter.buffered.spectrum;

import java.util.Arrays;

/** Distribution of hue across all the pixels of an image.
 *
 *  Like in a histogram, the values are organized in equally sized buckets.
 *  However, the height of each bar is not necessarily related to the frequency
 *  of items within its range. For example, the brightness and saturation of
 *  each pixel could be taken into account to calculate its contribution.
 *
 *  Spectra are mutable through {@link #addPixelContribution(float, float)}, but
 *  the size and amount of buckets cannot be changed.
 *  Bucket intervals are closed-open. They are characterized by the central
 *  value of the interval. */

public class Spectrum
{
  private static final float RANGE_START = 0;
  private static final float RANGE_END   = 1;

  private final float[] distribution;
  private final int amountOfBuckets;                                            // convenient field equal to distribution.length

//==============================================================================

  public Spectrum(int amountOfBuckets)
  {
    assert amountOfBuckets > 0;

    this.amountOfBuckets = amountOfBuckets;
    distribution         = new float[amountOfBuckets];
  }

//------------------------------------------------------------------------------

  /** Just for testing purposes. */

  protected Spectrum(float[] distribution)
  {
    this.distribution    = distribution;
    this.amountOfBuckets = distribution.length;
  }

//==============================================================================

  /** Modifies the bucket of the distribution where hue belongs by adding amount.
   *  All other buckets remain the same. */

  public void addPixelContribution(float hue, float amount)
  {
    assert RANGE_START <= hue && hue < RANGE_END;
    assert amount >= 0;

    int destinationBucket            = (int) (amountOfBuckets * hue);
    distribution[destinationBucket] += amount;
  }

//------------------------------------------------------------------------------

  /** Returns a copy of the distribution. */

  public float[] getDistribution() {return Arrays.copyOf(distribution, amountOfBuckets);}

//------------------------------------------------------------------------------

  /** Returns the characteristic hue of the most relevant bucket according to
   *  the matching pattern. */

  public float getMostRelevantHue(MatchingPattern matchingPattern)
  {
    float[] patternProfile = matchingPattern.getPattern();
    assert patternProfile.length < amountOfBuckets / 2;

    int mostRelevantBucket = 0;
    float mostRelevance    = 0;

    for (int currentBucket = 0; currentBucket < amountOfBuckets; currentBucket++)
    {
      float currentRelevance = calculateRelevance(currentBucket, patternProfile);

      if (currentRelevance > mostRelevance)
      {
        mostRelevantBucket = currentBucket;
        mostRelevance      = currentRelevance;
      }
    }

    return getCharacteristicHue(mostRelevantBucket);
  }

//------------------------------------------------------------------------------

  /* Returns the sum of the value of bucket and the product of its surrounding
   * buckets with the pattern profile. */

  private float calculateRelevance(int bucket, float[] patternProfile)
  {
    float relevance = distribution[bucket];

    for (int i = 0; i < patternProfile.length; i++)
    {
      relevance += patternProfile[i] * distribution[getBucketToTheLeft(bucket, i + 1)];
      relevance += patternProfile[i] * distribution[getBucketToTheRight(bucket, i + 1)];
    }

    return relevance;
  }

//------------------------------------------------------------------------------

  /* Returns the index of the distribution that is at distance to the left from
   * centralBucket, considering that the distribution is cyclic and the last
   * bucket is immediately to the left of the first one.
   * A distance of 1 means the bucket immediately to the left. */

  private int getBucketToTheLeft(int centralBucket, int distance)
  {
    int naiveBucket = centralBucket - distance;

    if (naiveBucket >= 0) return naiveBucket;
    else                  return amountOfBuckets + naiveBucket;
  }

//------------------------------------------------------------------------------

  /* Returns the index of the distribution that is at distance to the right from
   * centralBucket, considering that the distribution is cyclic and the first
   * bucket is immediately to the right of the last one.
   * A distance of 1 means the bucket immediately to the right. */

  private int getBucketToTheRight(int centralBucket, int distance)
  {
    int naiveBucket = centralBucket + distance;

    if (naiveBucket < amountOfBuckets) return naiveBucket;
    else                               return naiveBucket - amountOfBuckets;
  }

//------------------------------------------------------------------------------

  /* Returns the central value of the bucket. */

  private float getCharacteristicHue(int bucket)
  {
    float bucketWidth = (RANGE_END - RANGE_START) / amountOfBuckets;

    return bucketWidth * bucket + bucketWidth / 2;
  }

//------------------------------------------------------------------------------

}
