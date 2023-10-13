
package org.skyllias.alomatia.filter.operator;

import java.awt.image.ImageFilter;

import org.skyllias.alomatia.filter.FilteredImageGenerator;
import org.skyllias.alomatia.filter.buffered.FilteredBufferedImageGenerator;
import org.skyllias.alomatia.filter.buffered.HintlessBufferedImageOp;
import org.skyllias.alomatia.filter.buffered.SingleFrameBufferedImageFilter;
import org.skyllias.alomatia.filter.convolve.EdgeConvolvingComposedFilter;
import org.skyllias.alomatia.filter.convolve.KernelDataFactory;
import org.skyllias.alomatia.filter.convolve.NegativeKernelDataFactory;

/** Although most kernels are separable, they are too small for the separation
 *  to have better performance due to the overhead of enlarging and cropping
 *  and building an intermediate image. */

public class OperatorFilterFactory
{
  private static final FilteredBufferedImageGenerator filteredImageGenerator = new FilteredBufferedImageGenerator(new FilteredImageGenerator());

//==============================================================================

  public static ImageFilter forSobel()
  {
    return forKernels(
      new float[][]
      {
        {1, 0, -1},
        {2, 0, -2},
        {1, 0, -1},
      },
      new float[][]
      {
        {1, 2, 1},
        {0, 0, 0},
        {-1, -2, -1},
      });
  }

//------------------------------------------------------------------------------

  public static ImageFilter forScharr()
  {
    return forKernels(
      new float[][]
      {
        {0.47f, 0, -0.47f},
        {1.62f, 0, -1.62f},
        {0.47f, 0, -0.47f},
      },
      new float[][]
      {
        {0.47f, 1.62f, 0.47f},
        {0, 0, 0},
        {-0.47f, -1.62f, -0.47f},
      });
  }

//------------------------------------------------------------------------------

  public static ImageFilter forPrewitt()
  {
    return forKernels(
      new float[][]
      {
        {1, 0, -1},
        {1, 0, -1},
        {1, 0, -1},
      },
      new float[][]
      {
        {1, 1, 1},
        {0, 0, 0},
        {-1, -1, -1},
      });
  }

//------------------------------------------------------------------------------

  public static ImageFilter forRobertsCross()
  {
    return forKernels(
      new float[][]
      {
        {1, 0},
        {0, -1},
      },
      new float[][]
      {
        {0, 1},
        {-1, 0},
      });
  }

//------------------------------------------------------------------------------

  private static ImageFilter forKernels(float[][] horizontalKernel, float[][] verticalKernel)
  {
    KernelDataFactory horizontalPositiveKernelDataFactory = new KernelDataFactory()
    {
      @Override
      public float[][] getKernelData() {return horizontalKernel;}
    };
    KernelDataFactory horizontalNegativeKernelDataFactory = new NegativeKernelDataFactory(horizontalPositiveKernelDataFactory);

    KernelDataFactory verticalPositiveKernelDataFactory = new KernelDataFactory()
    {
      @Override
      public float[][] getKernelData() {return verticalKernel;}
    };
    KernelDataFactory verticalNegativeKernelDataFactory = new NegativeKernelDataFactory(verticalPositiveKernelDataFactory);

    OperatorOperation operatorOperation = new OperatorOperation(filteredImageGenerator,
                                                                new EdgeConvolvingComposedFilter(horizontalPositiveKernelDataFactory),
                                                                new EdgeConvolvingComposedFilter(horizontalNegativeKernelDataFactory),
                                                                new EdgeConvolvingComposedFilter(verticalPositiveKernelDataFactory),
                                                                new EdgeConvolvingComposedFilter(verticalNegativeKernelDataFactory));

    return new SingleFrameBufferedImageFilter(new HintlessBufferedImageOp(operatorOperation));
  }

//------------------------------------------------------------------------------

}
