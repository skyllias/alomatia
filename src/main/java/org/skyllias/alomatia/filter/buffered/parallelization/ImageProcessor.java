
package org.skyllias.alomatia.filter.buffered.parallelization;

/** Applier of some concurrent transformation from a source image to a
 *  destination image of the same size.
 *  Processing is carried out pixel by pixel, but this does not prevent
 *  implementations from using multiple pixels around, nor forces them to get
 *  or set them individually. */

public interface ImageProcessor
{
  /** Returns the width of the image to process. */

  int getImageWidth();

  /** Returns the height of the image to process. */

  int getImageHeight();

  /** Applies the transformation to the pixel located at (x, y).
   *  Whether the coordinates are from the source or the destination is up to
   *  the implementation. */

  void processPixel(int x, int y);
}
