
package org.skyllias.alomatia.filter.buffered.vignette;

/** {int, int, int, int} -> [0, 1] function that defines how the pixels in an
 *  image should be darkened to simulate a vignette effect. */

public interface VignetteProfile
{
  /** Returns the degree of "vignetteness" for the pixel located at (x, y) in
   *  an image with dimensions (width, height), with 0 meaning that the source
   *  pixel should not be altered at all and 1 that it should be fully opaqued.
   *  Usually, the following conditions will hold, although anyway it is up to
   *  each implementation to decide:
   *  - The function is simmetric for x around width/2 (there might be a
   *    one-pixel difference).
   *  - The function is simmetric for y around height/2 (there might be a
   *    one-pixel difference).
   *  - For (x, y) inside (width/2 - W, height/2 - H) and (width/2 + W, height/2 + H),
   *  where W and H are of the order of width/4 and height/4 respectively, the
   *  returned value will be 0. */

  float getVignetteEffect(int x, int y, int width, int height);
}
