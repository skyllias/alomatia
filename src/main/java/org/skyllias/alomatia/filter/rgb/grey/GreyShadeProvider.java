
package org.skyllias.alomatia.filter.rgb.grey;

/** Provider of which grey shade should result from each RGB colour.. */

public interface GreyShadeProvider
{
  /** Assuming all inputs are between 0 and 255, returns a value in the same range. */

  int getShade(int red, int green, int blue);
}
