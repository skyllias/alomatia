
package org.skyllias.alomatia.filter.convolve;

import java.awt.image.ImageFilter;

/** Instantiator of filters that blur. */

public class BlurFilterFactory
{
//==============================================================================

  public static ImageFilter forParaboloid(int length) {return new EdgeConvolvingComposedFilter(new ParaboloidBlurKernelDataFactory(length));}

//------------------------------------------------------------------------------

  public static ImageFilter forGaussian(int length) {return new EdgeConvolvingComposedFilter(new SeparatedKernelDataFactoryBuilder().createSeparatedGaussianKernelDataFactories(length));}

//------------------------------------------------------------------------------

  public static ImageFilter forSharpening() {return new EdgeConvolvingComposedFilter(new NeighbourSharpKernelDataFactory());}

//------------------------------------------------------------------------------

}
