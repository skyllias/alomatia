
package org.skyllias.alomatia.filter.demo;

import java.awt.*;

import org.skyllias.alomatia.filter.*;

/** Superclass for the filters that convert all colours to grey shades. */

public abstract class WheighedGreyScaleFilter extends BasicColorFilter
{
//==============================================================================

  @Override
  protected Color filterColor(Color original)
  {
    int red   = original.getRed();
    int green = original.getGreen();
    int blue  = original.getBlue();

    int redWheight   = getRedWheight();
    int greenWheight = getGreenWheight();
    int blueWheight  = getBlueWheight();
    int totalWheight = redWheight + greenWheight + blueWheight;

    int totalLight = red * redWheight + green * greenWheight + blue + blueWheight + totalWheight / 2; // the last term makes the average round instead of truncate
    int average    = totalLight / totalWheight;
    return new Color(average, average, average);
  }

//------------------------------------------------------------------------------

  /** Returns a positive number that will determine how much the red channel
   *  contributes to brightness compared to {@link @getGreenWheight()} and
   *  {@link @getBlueWheight()}.
   *  It should be between 1 and 2 << 16. */

  protected abstract int getRedWheight();

//------------------------------------------------------------------------------

  /** Returns a positive number that will determine how much the green channel
   *  contributes to brightness compared to {@link @getRedWheight()} and
   *  {@link @getBlueWheight()}. */

  protected abstract int getGreenWheight();

//------------------------------------------------------------------------------

  /** Returns a positive number that will determine how much the blue channel
   *  contributes to brightness compared to {@link @getGreenWheight()} and
   *  {@link @getRedWheight()}. */

  protected abstract int getBlueWheight();

//------------------------------------------------------------------------------

}
