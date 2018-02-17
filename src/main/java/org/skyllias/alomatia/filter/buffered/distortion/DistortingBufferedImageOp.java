
package org.skyllias.alomatia.filter.buffered.distortion;

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.util.concurrent.*;

import org.skyllias.alomatia.filter.buffered.*;
import org.skyllias.alomatia.filter.buffered.parallelization.*;

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
    DistortionProcessor processor = new DistortionProcessor(src, dest);
    RecursiveAction processAction = new RecursiveImageAction(processor);
    processAction.invoke();
  }

//------------------------------------------------------------------------------

//******************************************************************************

  private class DistortionProcessor implements ImageProcessor
  {
    private BufferedImage src;
    private BufferedImage dest;
    private Dimension2D imageBounds;

    public DistortionProcessor(BufferedImage src, BufferedImage dest)
    {
      this.src  = src;
      this.dest = dest;

      imageBounds = new Dimension(dest.getWidth(), dest.getHeight());
    }

    @Override
    public int getImageWidth() {return src.getWidth();}

    @Override
    public int getImageHeight() {return src.getHeight();}

    @Override
    public void processPixel(int x, int y)
    {
      Point2D.Float currentDestPixel = new Point2D.Float(x + PixelizationConstants.PIXEL_OFFSET,
                                                         y + PixelizationConstants.PIXEL_OFFSET);
      Point2D.Float sourcePoint      = distortion.getSourcePoint(currentDestPixel, imageBounds);
      Color pixelColour              = interpolator.getColourAt(src, sourcePoint);
      dest.setRGB(x, y, pixelColour.getRGB());
    }
  }
}
