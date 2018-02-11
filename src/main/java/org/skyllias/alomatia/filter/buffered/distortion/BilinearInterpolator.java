
package org.skyllias.alomatia.filter.buffered.distortion;

import java.awt.*;
import java.awt.geom.Point2D.Float;
import java.awt.image.*;

/** {@link Interpolator} that uses at most four surronding pixels to pick a
 *  colour that is not exactly on a pixel. */

public class BilinearInterpolator implements Interpolator
{
//==============================================================================

  @Override
  public Color getColourAt(BufferedImage sourceImage, Float point)
  {
    float x = Math.min(Math.max(PixelizationConstants.PIXEL_OFFSET, (float) point.getX()),
                       sourceImage.getWidth() - PixelizationConstants.PIXEL_OFFSET);
    float y = Math.min(Math.max(PixelizationConstants.PIXEL_OFFSET, (float) point.getY()),
                       sourceImage.getHeight() - PixelizationConstants.PIXEL_OFFSET);

    int horizontalPosition = (int) Math.min(Math.floor(x - PixelizationConstants.PIXEL_OFFSET),
                                            sourceImage.getWidth() - 2);
    int verticalPosition   = (int) Math.min(Math.floor(y - PixelizationConstants.PIXEL_OFFSET),
                                            sourceImage.getHeight() - 2);
    float horizontalOffset = x - horizontalPosition - PixelizationConstants.PIXEL_OFFSET;
    float verticalOffset   = y - verticalPosition - PixelizationConstants.PIXEL_OFFSET;

    Color topLeftPixel     = new Color(sourceImage.getRGB(horizontalPosition, verticalPosition));
    Color topRightPixel    = new Color(sourceImage.getRGB(horizontalPosition + 1, verticalPosition));
    Color bottomLeftPixel  = new Color(sourceImage.getRGB(horizontalPosition, verticalPosition + 1));
    Color bottomRightPixel = new Color(sourceImage.getRGB(horizontalPosition + 1, verticalPosition + 1));
    return interpolate(horizontalOffset, verticalOffset, topLeftPixel, topRightPixel,
                       bottomLeftPixel, bottomRightPixel);
  }

//------------------------------------------------------------------------------

  /* Returns a linear combination of the colours, with more contribution
   * of topLeft and bottomLeft when distanceToLeft is smaller, and more of
   * topLeft and topRight when distanceToTop is smaller. */

  private Color interpolate(float distanceToLeft, float distanceToTop,
                            Color topLeft, Color topRight, Color bottomLeft, Color bottomRight)
  {
    Color topInterpolation    = interpolate(distanceToLeft, topLeft, topRight);
    Color bottomInterpolation = interpolate(distanceToLeft, bottomLeft, bottomRight);
    return interpolate(distanceToTop, topInterpolation, bottomInterpolation);
  }

//------------------------------------------------------------------------------

  /* Returns a linear combination of previous and next, with more contribution
   * of previous when distanceToPrevious is smaller. */

  private Color interpolate(float distanceToPrevious, Color previous, Color next)
  {
    int red   = interpolate(distanceToPrevious, previous.getRed(), next.getRed());
    int green = interpolate(distanceToPrevious, previous.getGreen(), next.getGreen());
    int blue  = interpolate(distanceToPrevious, previous.getBlue(), next.getBlue());
    return new Color(red, green, blue);
  }

//------------------------------------------------------------------------------

  /* Returns a linear combination of previous and next, with more contribution
   * of previous when distanceToPrevious is smaller. */

  private int interpolate(float distanceToPrevious, int previous, int next)
  {
    return Math.round((1 - distanceToPrevious) * previous + distanceToPrevious * next);
  }

//------------------------------------------------------------------------------

}
