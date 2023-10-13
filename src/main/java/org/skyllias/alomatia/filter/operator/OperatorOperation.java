
package org.skyllias.alomatia.filter.operator;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.ImageFilter;

import org.skyllias.alomatia.filter.buffered.BufferedImageOperation;
import org.skyllias.alomatia.filter.buffered.FilteredBufferedImageGenerator;

/** Operation that calculates edges by calculing the magnitude of the gradient
 *  of colour in a pixel from the gradient in the horizontal and vertical
 *  directions.
 *
 *  Depending on the filters received, the final image will detect some edges
 *  better or worse.
 *
 *  A set of four convolving filters is required, despite two would suffice
 *  if convolutions produced values outside the [0, 255] range. An ad-hoc
 *  implementation of convolutions, or the use of a native library, could
 *  save from the "negative" filter required to obtain the equivalent to
 *  values below 0 in each direction. For the time being, though, this operation
 *  expects two filters per direction, assuming that the negative and the
 *  positive kernels only differ in the sign of each component.
 *
 *  Another potential performance boost could come from parallel execution, both
 *  of filters and the arithmetics for magnitude calculation.
 *
 *  Although the original algorithm is defined for grey-scaled images, this
 *  implementations handles each RGB channel separately and then averages the
 *  result. Other possibilities would be to take the maximum, minimum or median
 *  magnitude as the edge intensity, apart from converting images to greys
 *  before the convolutions. */

public class OperatorOperation implements BufferedImageOperation
{
  private final FilteredBufferedImageGenerator filteredImageGenerator;

  private final ImageFilter horizontalPositiveConvolvingFilter;
  private final ImageFilter horizontalNegativeConvolvingFilter;
  private final ImageFilter verticalPositiveConvolvingFilter;
  private final ImageFilter verticalNegativeConvolvingFilter;

//==============================================================================

  public OperatorOperation(FilteredBufferedImageGenerator filteredImageGenerator,
                         ImageFilter horizontalPositiveConvolvingFilter,
                         ImageFilter horizontalNegativeConvolvingFilter,
                         ImageFilter verticalPositiveConvolvingFilter,
                         ImageFilter verticalNegativeConvolvingFilter)
  {
    this.filteredImageGenerator             = filteredImageGenerator;
    this.horizontalPositiveConvolvingFilter = horizontalPositiveConvolvingFilter;
    this.horizontalNegativeConvolvingFilter = horizontalNegativeConvolvingFilter;
    this.verticalPositiveConvolvingFilter   = verticalPositiveConvolvingFilter;
    this.verticalNegativeConvolvingFilter   = verticalNegativeConvolvingFilter;
  }

//==============================================================================

  /** Applies the four convolutions  */

  @Override
  public void filter(BufferedImage inputImage, BufferedImage outputImage)
  {
    BufferedImage horizontalPositiveConvolution = filteredImageGenerator.generate(inputImage, horizontalPositiveConvolvingFilter);
    BufferedImage horizontalNegativeConvolution = filteredImageGenerator.generate(inputImage, horizontalNegativeConvolvingFilter);
    BufferedImage verticalPositiveConvolution   = filteredImageGenerator.generate(inputImage, verticalPositiveConvolvingFilter);
    BufferedImage verticalNegativeConvolution   = filteredImageGenerator.generate(inputImage, verticalNegativeConvolvingFilter);

    for (int x = 0; x < outputImage.getWidth(); x++)
    {
      for (int y = 0; y < outputImage.getHeight(); y++)
      {
        int horizontalGradientMagnitude = getPixelValue(horizontalPositiveConvolution, horizontalNegativeConvolution, x, y);
        int verticalGradientMagnitude   = getPixelValue(verticalPositiveConvolution, verticalNegativeConvolution, x, y);
        int gradientMagnitude           = combineMagnitude(horizontalGradientMagnitude, verticalGradientMagnitude);

        int pixelGreyColour = new Color(gradientMagnitude, gradientMagnitude, gradientMagnitude).getRGB();
        outputImage.setRGB(x, y, pixelGreyColour);
      }
    }
  }

//------------------------------------------------------------------------------

  /* Assuming that the value of the (x, y) pixel in each channel is zero in one
   * of the images and the gradient in the other image, the average of the
   * gradients over the three channels is returned. */

  private int getPixelValue(BufferedImage positiveConvolution,
                            BufferedImage negativeConvolution, int x, int y)
  {
    Color colour1 = new Color(positiveConvolution.getRGB(x, y));
    Color colour2 = new Color(negativeConvolution.getRGB(x, y));

    int sumOfMaximums = Math.max(colour1.getRed(), colour2.getRed()) +
                        Math.max(colour1.getGreen(), colour2.getGreen()) +
                        Math.max(colour1.getBlue(), colour2.getBlue());
    return sumOfMaximums / 3;
  }

//------------------------------------------------------------------------------

  /* Assuming that each gradient magnitude is in the range [0, 255], their
   * magnitudes are combined as the length of a two-dimensional vector. */

  private int combineMagnitude(int horizontalGradientMagnitude,
                               int verticalGradientMagnitude)
  {
    return (int) Math.min(255,
                          Math.round(Math.sqrt(horizontalGradientMagnitude * horizontalGradientMagnitude +
                                               verticalGradientMagnitude * verticalGradientMagnitude)));
  }

//------------------------------------------------------------------------------

}
