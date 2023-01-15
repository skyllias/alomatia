
package org.skyllias.alomatia.filter.rgb.grey;

/** Provider of how much each RGB channel should be contributing to some
 *  calculation, like for example the brightness of a grey scale.
 *  All values should be between 1 and 2 << 16, although when very high they
 *  can hardly offer any advantadge due to the limited precision in colours. */

public interface ChannelWeights
{
  int getRedWeight();
  int getGreenWeight();
  int getBlueWeight();
}
