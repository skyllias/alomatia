
package org.skyllias.alomatia.filter;

import java.awt.Color;
import java.awt.image.RGBImageFilter;

/** Superclass for the filters that just alter the colours of an image's palette.
 *  The alpha channel is not properly handled due to the limitations of {@link RGBImageFilter}.
 *  @deprecated Use {@link ColorFilter} and {@link ColorConverter} to avoid
 *  long class hierarchies. */

@Deprecated
public abstract class BasicColorFilter extends RGBImageFilter
{
//==============================================================================

  public BasicColorFilter() {super.canFilterIndexColorModel = true;}

//==============================================================================

  /** Returns a new int representation of the passed colour without caring about
   *  the pixel coordinates. */

  @Override
  public int filterRGB(int x, int y, int rgb)
  {
    Color originalColor = new Color(rgb);
    Color modifiedColor = filterColor(originalColor);
    return modifiedColor.getRGB();
  }

//------------------------------------------------------------------------------

  /** Returns a colour that must substitute the original. */

  protected abstract Color filterColor(Color original);

//------------------------------------------------------------------------------

}
