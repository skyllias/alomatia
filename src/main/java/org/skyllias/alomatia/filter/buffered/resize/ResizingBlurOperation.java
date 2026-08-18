
package org.skyllias.alomatia.filter.buffered.resize;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import org.skyllias.alomatia.filter.buffered.BufferedImageOperation;

/** {@link BufferedImageOperation} that blurs images by reducing them to a
 *  smaller size and then stretching them back to their original size.
 *  The smoothing comes from the bilinear interpolation applied in both
 *  resizings, so the smaller the intermediate image, the blurrier the outcome.
 *
 *  Unlike convolutions, the cost does not grow with the strength of the blur. */

public class ResizingBlurOperation implements BufferedImageOperation
{
  private final ReducedSizeCalculator reducedSizeCalculator;

//==============================================================================

  public ResizingBlurOperation(ReducedSizeCalculator reducedSizeCalculator)
  {
    this.reducedSizeCalculator = reducedSizeCalculator;
  }

//==============================================================================

  /** Draws the input image on an intermediate image of a reduced size, and then
   *  that intermediate image on the output image. */

  @Override
  public void filter(BufferedImage inputImage, BufferedImage outputImage)
  {
    Dimension reducedSize = reducedSizeCalculator.getReducedSize(inputImage.getWidth(),
                                                                inputImage.getHeight());

    BufferedImage reducedImage = new BufferedImage(reducedSize.width, reducedSize.height,
                                                   BufferedImage.TYPE_INT_ARGB_PRE);
    drawScaled(inputImage, reducedImage);
    drawScaled(reducedImage, outputImage);
  }

//------------------------------------------------------------------------------

  /* Draws sourceImage over the full surface of targetImage, scaling it as
   * required. */

  private void drawScaled(BufferedImage sourceImage, BufferedImage targetImage)
  {
    Graphics2D graphics = targetImage.createGraphics();
    graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                              RenderingHints.VALUE_INTERPOLATION_BILINEAR);

    graphics.drawImage(sourceImage, 0, 0, targetImage.getWidth(), 
    		               targetImage.getHeight(), null);

    graphics.dispose();
  }

//------------------------------------------------------------------------------

}
