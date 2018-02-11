
package org.skyllias.alomatia.filter.buffered.distortion;

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;

import org.skyllias.alomatia.filter.buffered.*;

/** {@link BasicBufferedImageOp} that distorts images by applying a
 *  {@link Distortion} to all pixels. */

public class DistortingBufferedImageOp extends BasicBufferedImageOp
{
  private Distortion distortion;
  private Interpolator interpolator;

//==============================================================================

  public DistortingBufferedImageOp(Distortion distortion, Interpolator interpolator)
  {
    this.distortion   = distortion;
    this.interpolator = interpolator;
  }

//==============================================================================

  @Override
  protected void doFilter(BufferedImage src, BufferedImage dest)
  {
    Dimension2D imageBounds = new Dimension(dest.getWidth(), dest.getHeight());

    for (int i = 0; i < dest.getWidth(); i++)
    {
      for (int j = 0; j < dest.getHeight(); j++)
      {
        Point2D.Float currentDestPixel = new Point2D.Float(i + PixelizationConstants.PIXEL_OFFSET,
                                                           j + PixelizationConstants.PIXEL_OFFSET);
        Point2D.Float sourcePoint      = distortion.getSourcePoint(currentDestPixel, imageBounds);
        Color pixelColour              = interpolator.getColourAt(src, sourcePoint);
        dest.setRGB(i, j, pixelColour.getRGB());
      }
    }
  }

//------------------------------------------------------------------------------

}
