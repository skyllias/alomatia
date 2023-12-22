
package org.skyllias.alomatia.filter.buffered.dynamic;

import java.awt.image.BufferedImage;
import java.awt.image.ImageFilter;
import java.util.function.Function;

import org.skyllias.alomatia.filter.FilteredImageGenerator;
import org.skyllias.alomatia.filter.buffered.BufferedImageOperation;
import org.skyllias.alomatia.filter.buffered.FilteredBufferedImageGenerator;

/** {@link BufferedImageOperation} that applies a filter parameterized with
 *  a value dynamically calculated from the input image.
 *  For the sake of reusability, it is split in a function that produces the
 *  parameter from the input image and another that produces the filter from
 *  the parameter.
 *  As most filters are light-weighted, the values from the filter function are
 *  not cached at all. If the previous assumption does not apply to some case,
 *  it is the function's responsibility to keep the cache. */

public class DynamicFilterOperation<PARAMETER> implements BufferedImageOperation
{
  private static final FilteredBufferedImageGenerator filteredBufferedImageGenerator = new FilteredBufferedImageGenerator(new FilteredImageGenerator());

  private final Function<BufferedImage, PARAMETER> parameterFunction;
  private final Function<PARAMETER, ImageFilter> filterFunction;

//==============================================================================

  public DynamicFilterOperation(Function<BufferedImage, PARAMETER> parameterFunction,
                                Function<PARAMETER, ImageFilter> filterFunction)
  {
    this.parameterFunction = parameterFunction;
    this.filterFunction    = filterFunction;
  }

//==============================================================================

  /** Invokes the two functions to obtain a filter from the input image and then
   *  applies it to it to draw it to the output image. */

  @Override
  public void filter(BufferedImage inputImage, BufferedImage outputImage)
  {
    PARAMETER parameter = parameterFunction.apply(inputImage);
    ImageFilter filter  = filterFunction.apply(parameter);

    BufferedImage result = filteredBufferedImageGenerator.generate(inputImage, filter);
    outputImage.getGraphics().drawImage(result, 0, 0, null);
  }

//------------------------------------------------------------------------------

}
