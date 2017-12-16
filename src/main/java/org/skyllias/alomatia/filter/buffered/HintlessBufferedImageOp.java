
package org.skyllias.alomatia.filter.buffered;

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;

/** Superclass for the {@link BufferedImageOp}s that do not manipulate rendering hints. */

public abstract class HintlessBufferedImageOp implements BufferedImageOp
{
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
    Dimension destSize = getDestImageDimensions(src);
    int width          = (int) destSize.getWidth();
    int height         = (int) destSize.getHeight();

    if (destCM == null) destCM = src.getColorModel();

    return new BufferedImage(destCM, destCM.createCompatibleWritableRaster(width, height),
                             destCM.isAlphaPremultiplied(), null);
  }

//------------------------------------------------------------------------------

  /** Returns the size of the resulting image for an input image src.
   *  By default, returns the imput image's dimensions, but subclasses may
   *  override it. */

  protected Dimension getDestImageDimensions(BufferedImage src)
  {
    return new Dimension(src.getWidth(), src.getHeight());
  }

//------------------------------------------------------------------------------

}
