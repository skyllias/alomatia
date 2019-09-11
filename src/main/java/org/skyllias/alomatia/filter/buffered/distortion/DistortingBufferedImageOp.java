
package org.skyllias.alomatia.filter.buffered.distortion;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.concurrent.RecursiveAction;

import org.skyllias.alomatia.filter.buffered.BasicBufferedImageOp;
import org.skyllias.alomatia.filter.buffered.parallelization.ImageProcessor;
import org.skyllias.alomatia.filter.buffered.parallelization.RecursiveImageAction;

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
