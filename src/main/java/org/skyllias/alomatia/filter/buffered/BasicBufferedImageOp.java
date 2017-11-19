
package org.skyllias.alomatia.filter.buffered;

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;

/** Superclass for the {@link BufferedImageOp} that keep the image size.
 *  Subclasses must implement filter(BufferedImage, BufferedImage). */

public abstract class BasicBufferedImageOp implements BufferedImageOp
{
//==============================================================================

  /** Copied from java.awt.image.RescaleOp. */

  @Override
  public BufferedImage createCompatibleDestImage(BufferedImage src, ColorModel destCM)
  {
    BufferedImage image;
    if (destCM == null)
    {
      ColorModel cm = src.getColorModel();
      image = new BufferedImage(cm, src.getRaster().createCompatibleWritableRaster(),
                                cm.isAlphaPremultiplied(), null);
    }
    else
    {
      int w = src.getWidth();
      int h = src.getHeight();
      image = new BufferedImage (destCM, destCM.createCompatibleWritableRaster(w, h),
                                 destCM.isAlphaPremultiplied(), null);
    }
    return image;
  }

//------------------------------------------------------------------------------

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

}