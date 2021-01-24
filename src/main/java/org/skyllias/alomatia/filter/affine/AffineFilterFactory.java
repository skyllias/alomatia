
package org.skyllias.alomatia.filter.affine;

import java.awt.image.ImageFilter;

import org.skyllias.alomatia.filter.buffered.HintlessBufferedImageOp;
import org.skyllias.alomatia.filter.buffered.SingleFrameBufferedImageFilter;

/** Instantiator of filters that apply affine transformations. */

public class AffineFilterFactory
{
//==============================================================================

  public static ImageFilter forHorizontalFlip()
  {
    return forTransformProvider(new HorizontalFlipTransformProvider());
  }

//------------------------------------------------------------------------------

  public static ImageFilter forVerticalFlip()
  {
    return forTransformProvider(new VerticalFlipTransformProvider());
  }

//------------------------------------------------------------------------------

  public static ImageFilter forRotation()
  {
    return forTransformProvider(new RotationTransformProvider());
  }

//------------------------------------------------------------------------------

  private static ImageFilter forTransformProvider(AffineTransformProvider affineTransformProvider)
  {
    return new SingleFrameBufferedImageFilter(new HintlessBufferedImageOp(new FixedSizeAffineTransformOperation(affineTransformProvider)));
  }

//------------------------------------------------------------------------------

}
