
package org.skyllias.alomatia.logo;

import java.awt.Color;

import org.springframework.stereotype.Component;

/** Calculator of the colour to apply to a pixel according to its location in the logo. */

@Component
public class ColourCoordinator
{
//==============================================================================

  /** Returns the {@link Color} for the pixel located in a system of coordinates
   *  at (x, y), with both values inside the open interval (0, 1).
   *  The pixels may be as close to the boundary as wanted, but the round values
   *  should be prevented because all pixels on the edges would have the same
   *  colour.
   *  The colour is assigned using the HSB model, with the x mainly determining
   *  the hue (the y is also involved to a lesser extent to avoid vertical bars)
   *  and the y the saturation and the brightness: for the lower half of values,
   *  the saturation grows proportionally and the brightness is the maximum; for
   *  the higher half, saturation is the maximum and brightness decreases
   *  proportionally. */

  public Color getColourAt(float x, float y)
  {
    final float MIN_COORDINATE  = 0;
    final float MAX_COORDINATE  = 1;
    final float WIDTH           = MAX_COORDINATE - MIN_COORDINATE;
    final float HALF_COORDINATE = MIN_COORDINATE + WIDTH / 2;

    if (x < MIN_COORDINATE) x = MIN_COORDINATE;
    if (x > MAX_COORDINATE) x = MAX_COORDINATE;
    if (y < MIN_COORDINATE) y = MIN_COORDINATE;
    if (y > MAX_COORDINATE) y = MAX_COORDINATE;

    float xHue       = (x - MIN_COORDINATE) / WIDTH;
    float yHue       = (y - MIN_COORDINATE) / WIDTH;
    float hue        = xHue + yHue / 2;                                         // Color.HSBtoRGB already dephases values greater than 1
    float saturation = 1;
    float brightness = 1;

    if (y - MIN_COORDINATE < HALF_COORDINATE) saturation = 2 * (y - MIN_COORDINATE) / WIDTH;
    if (y - MIN_COORDINATE > HALF_COORDINATE) brightness = 1 - 2 * (y - HALF_COORDINATE) / WIDTH;

    return new Color(Color.HSBtoRGB(hue, saturation, brightness));
  }

//------------------------------------------------------------------------------

}
