
package org.skyllias.alomatia.filter.buffered.distortion;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.concurrent.RecursiveAction;

import org.skyllias.alomatia.filter.buffered.BufferedImageOperation;
import org.skyllias.alomatia.filter.buffered.parallelization.ImageProcessor;
import org.skyllias.alomatia.filter.buffered.parallelization.RecursiveImageAction;

/** {@link BufferedImageOperation} that distorts images by applying a
 *  {@link Distortion} to all pixels. */

public class DistortingBufferedImageOperation implements BufferedImageOperation
{
  private final Distortion distortion;
  private final Interpolator interpolator;

//==============================================================================

  public DistortingBufferedImageOperation(Distortion distortion, Interpolator interpolator)
  {
    this.distortion   = distortion;
    this.interpolator = interpolator;
  }

//==============================================================================

  @Override
  public void filter(BufferedImage inputImage, BufferedImage outputImage)
  {
    DistortionProcessor processor = new DistortionProcessor(inputImage, outputImage);
    RecursiveAction processAction = new RecursiveImageAction(processor);
    processAction.invoke();
  }

//------------------------------------------------------------------------------

//******************************************************************************

  private class DistortionProcessor implements ImageProcessor
  {
    private BufferedImage inputImage;
    private BufferedImage outputImage;
    private Dimension2D imageBounds;

    public DistortionProcessor(BufferedImage inputImage, BufferedImage outputImage)
    {
      this.inputImage  = inputImage;
      this.outputImage = outputImage;

      imageBounds = new Dimension(outputImage.getWidth(), outputImage.getHeight());
    }

    @Override
    public int getImageWidth() {return inputImage.getWidth();}

    @Override
    public int getImageHeight() {return inputImage.getHeight();}

    @Override
    public void processPixel(int x, int y)
    {
      Point2D.Float currentDestPixel = new Point2D.Float(x + PixelizationConstants.PIXEL_OFFSET,
                                                         y + PixelizationConstants.PIXEL_OFFSET);
      Point2D.Float sourcePoint      = distortion.getSourcePoint(currentDestPixel, imageBounds);
      Color pixelColour              = interpolator.getColourAt(inputImage, sourcePoint);
      outputImage.setRGB(x, y, pixelColour.getRGB());
    }
  }
}
