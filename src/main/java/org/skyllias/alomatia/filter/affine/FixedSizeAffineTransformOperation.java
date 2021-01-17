
package org.skyllias.alomatia.filter.affine;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

import org.skyllias.alomatia.filter.buffered.BufferedImageOperation;

/** {@link BufferedImageOperation} that applies an {@link AffineTransform} that
 *  keeps the original size of the source image.
 *  It does not extend {@link AffineTransformOp} to avoid the evil of extension.
 *  The transformation is delegated to a real AffineTransformOp in order to
 *  take advantage of any potential hardware or native optimizations. */

public class FixedSizeAffineTransformOperation implements BufferedImageOperation
{
  private final AffineTransformProvider affineTransformProvider;

//==============================================================================

  public FixedSizeAffineTransformOperation(AffineTransformProvider affineTransformProvider)
  {
    this.affineTransformProvider = affineTransformProvider;
  }

//==============================================================================

  @Override
  public void filter(BufferedImage inputImage, BufferedImage outputImage)
  {
    int width  = inputImage.getWidth();
    int height = inputImage.getHeight();

    AffineTransform transform    = affineTransformProvider.getTransform(width, height);
    AffineTransformOp delegateOp = new AffineTransformOp(transform, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);   // at least initially, there is no perspective of transformations that require interpolation
    delegateOp.filter(inputImage, outputImage);
  }

//------------------------------------------------------------------------------

}
