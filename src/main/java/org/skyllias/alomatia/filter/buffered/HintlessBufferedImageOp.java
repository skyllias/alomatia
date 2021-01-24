
package org.skyllias.alomatia.filter.buffered;

import java.awt.Dimension;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ColorModel;

/** Implementation of {@link BufferedImageOp} that does not manipulate rendering hints.
 *  The business logic is delegated to a {@link ResizableBufferedImageOperation}. */

public class HintlessBufferedImageOp implements BufferedImageOp
{
  private final ResizableBufferedImageOperation resizableBufferedImageOperation;

//==============================================================================

  public HintlessBufferedImageOp(ResizableBufferedImageOperation resizableBufferedImageOperation)
  {
    this.resizableBufferedImageOperation = resizableBufferedImageOperation;
  }

//------------------------------------------------------------------------------

  /** Sugar for this(new SameSizeBufferedImageOperation(bufferedImageOperation)) */

  public HintlessBufferedImageOp(BufferedImageOperation bufferedImageOperation)
  {
    this(new SameSizeBufferedImageOperation(bufferedImageOperation));
  }

//==============================================================================

  /** Copied from java.awt.image.RescaleOp. */

  @Override
  public Rectangle2D getBounds2D(BufferedImage src)
  {
    return src.getRaster().getBounds();
  }

//------------------------------------------------------------------------------

  /** Copied from java.awt.image.RescaleOp. */

  @Override
  public Point2D getPoint2D(Point2D srcPt, Point2D dstPt)
  {
    if (dstPt == null) dstPt = new Point2D.Float();

    dstPt.setLocation(srcPt.getX(), srcPt.getY());
    return dstPt;
  }

//------------------------------------------------------------------------------

  /** Always null. */

  @Override
  public RenderingHints getRenderingHints() {return null;}

//------------------------------------------------------------------------------

  /** Generates an image with a compatible colour model but with the dimensions
   *  provided by {@link #getDestImageDimensions(BufferedImage)}. */

  @Override
  public BufferedImage createCompatibleDestImage(BufferedImage src, ColorModel destCM)
  {
    Dimension destSize = resizableBufferedImageOperation.getOutputImageDimension(src);
    int width          = (int) destSize.getWidth();
    int height         = (int) destSize.getHeight();

    if (destCM == null) destCM = src.getColorModel();

    return new BufferedImage(destCM, destCM.createCompatibleWritableRaster(width, height),
                             destCM.isAlphaPremultiplied(), null);
  }

//------------------------------------------------------------------------------

  /** Delegates the filter operation to resizableBufferedImageOperation,
   *  taking care of null safety of dest. */

  @Override
  public BufferedImage filter(BufferedImage src, BufferedImage dest)
  {
    if (dest == null) dest = createCompatibleDestImage(src, null);

    resizableBufferedImageOperation.filter(src, dest);
    return dest;
  }

//------------------------------------------------------------------------------

}
