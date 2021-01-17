
package org.skyllias.alomatia.filter.convolve;

import java.awt.image.ConvolveOp;
import java.awt.image.ImageConsumer;
import java.awt.image.ImageFilter;
import java.awt.image.Kernel;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.skyllias.alomatia.filter.buffered.HintlessBufferedImageOp;
import org.skyllias.alomatia.filter.buffered.KernelCroppingBufferedImageOperation;
import org.skyllias.alomatia.filter.buffered.KernelExpandingBufferedImageOperation;
import org.skyllias.alomatia.filter.buffered.ResizableBufferedImageOperation;
import org.skyllias.alomatia.filter.buffered.SingleFrameBufferedImageFilter;
import org.skyllias.alomatia.filter.compose.ComposedFilter;
import org.skyllias.alomatia.filter.rgb.RgbFilterFactory;

/** {@link ImageFilter} that delegates the image modifications to three composed
 *  filters: one to add an extra margin to the image, one to apply a convolution,
 *  and one to remove the extra margin. This way, the edge limitations in
 *  {@link ConvolveOp} can be overcome. Other strategies for the image expansion
 *  could be selected, but since all have advantages and disadvantages, none
 *  would be absolutely better than the one picked.
 *  The alpha channel is maintained, but its information is lost to prevent
 *  problems derived from convolutions with zero-volume kernels, that produce
 *  images with alpha equal to 0 (and therefore invisible). */

public class EdgeConvolvingComposedFilter extends ImageFilter
{
  private ComposedFilter composedFilter;

//==============================================================================

  /** Sets up a filter that will apply the kernels from the factory to images.
   *  A filter is set up for each kernel factory separately and they are
   *  composed in the same order.
   *  Kernels can have different sizes, but if they don't the edge is maximized
   *  and convolutions will be less efficient as some will be applied to
   *  fragments of image that will be fully cropped. */

  public EdgeConvolvingComposedFilter(KernelDataFactory... kernelFactories)
  {
    List<Kernel> kernels = new LinkedList<>();
    for (KernelDataFactory kernelDataFactory : kernelFactories)
    {
      kernels.add(getKernel(kernelDataFactory));
    }

    composedFilter = composeFilters(kernels);
  }

//==============================================================================

  /** Delegates the composition to a {@link ComposedFilter} instance. */

  @Override
  public ImageFilter getFilterInstance(ImageConsumer imageConsumer)
  {
    return composedFilter.getFilterInstance(imageConsumer);
  }

//------------------------------------------------------------------------------

  /* Returns a composed filter with a kernel expanding filter, the convolution
   * filters associated to the passed kernels, a channel to restore the alpha
   * channel and a kernel cropping filter.
   * The order is important and any change may lead to fully black images. */

  private ComposedFilter composeFilters(List<Kernel> kernels)
  {
    Kernel maximalKernel = getMaximalKernel(kernels);

    List<ImageFilter> filtersToCompose = new LinkedList<>();

    ImageFilter expandingFilter = getFilter(new KernelExpandingBufferedImageOperation(maximalKernel));
    filtersToCompose.add(expandingFilter);

    for (Kernel kernel : kernels)
    {
      ConvolveOp convolveOp        = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);    // EDGE_NO_OP has to be used to prevent a one-pixel black stripe if an even size is used for the kernel width or height
      ImageFilter convolvingFilter = new SingleFrameBufferedImageFilter(convolveOp);
      filtersToCompose.add(convolvingFilter);
    }

    ImageFilter alphalessFitler = RgbFilterFactory.forVoid();
    filtersToCompose.add(alphalessFitler);
    ImageFilter croppingFilter = getFilter(new KernelCroppingBufferedImageOperation(maximalKernel));
    filtersToCompose.add(croppingFilter);

    return new ComposedFilter(filtersToCompose.toArray(new ImageFilter[0]));
  }

//------------------------------------------------------------------------------

  /* Returns a Kernel with the values from matrixData.
   * If it is a jagged or empty array, an {@link IndexOutOfBoundsException}
   * may be thrown.
   * Copied from the deprecated class ConvolutingFilter. */

  private Kernel getKernel(KernelDataFactory kernelFactory)
  {
    float[][] matrixData = kernelFactory.getKernelData();
    int width            = matrixData[0].length;
    int height           = matrixData.length;
    int total            = width * height;
    float[] linearData   = new float[total];
    for (int i = 0; i < height; i++)
    {
      for (int j = 0; j < width; j++) linearData[i * width + j] = matrixData[i][j];
    }
    return new Kernel(width, height, linearData);
  }

//------------------------------------------------------------------------------

  /* Returns a Kernel instance whose width is the maximum width from kernels,
   * whose height is the maximum height of kernels, and wose data is all zeros.
   * This should only be used as a wrapper of some dimensions. */

  private Kernel getMaximalKernel(Collection<Kernel> kernels)
  {
    int maxWidth  = 0;
    int maxHeight = 0;

    for (Kernel currentKernel : kernels)
    {
      int currentWidth = currentKernel.getWidth();
      if (currentWidth > maxWidth) maxWidth = currentWidth;

      int currentHeight = currentKernel.getHeight();
      if (currentHeight > maxHeight) maxHeight = currentHeight;
    }

    int totalSize = maxWidth * maxHeight;
    return new Kernel(maxWidth, maxHeight, new float[totalSize]);
  }

//------------------------------------------------------------------------------

  private ImageFilter getFilter(ResizableBufferedImageOperation resizableBufferedImageOperation)
  {
    return new SingleFrameBufferedImageFilter(new HintlessBufferedImageOp(resizableBufferedImageOperation));
  }

//------------------------------------------------------------------------------

}
