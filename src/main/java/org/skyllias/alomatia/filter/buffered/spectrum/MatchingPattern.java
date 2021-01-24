
package org.skyllias.alomatia.filter.buffered.spectrum;

/** Provider of profiles that describe how the buckets of a spectrum have to
 *  be counted when searching for the most relevant hue in the spectrum.
 *  If buckets are small, the most frequent hue is not necessarily that with the
 *  highest value in the distribution, as surrounding buckets could be similar
 *  enough for them to be taken into account as well. An isolated peak can be
 *  less representative than a group of near by buckets. */

public interface MatchingPattern
{
  /** Returns an array (always the same) that describes how much surrounding
   *  buckets have to be taken into account when calculating the most relevant
   *  hue in a spectrum.
   *  Each position in the array corresponds to a pair of buckets: position 0
   *  corresponds to the first buckets immediately left and right, and each
   *  other position to the buckets one step further from the central than the
   *  previous one.
   *  An empty array means that each bucket contributes alone.
   *  The values of the pattern are relative to the contribution of a bucket to
   *  its own relevance. Therefore, the expected profile is that elements in the
   *  array are in the range (0, 1] and do not increase.
   *  The length of the array is expected to be much shorter than the amount of
   *  buckets in a spectrum. */

  float[] getPattern();
}
