
package org.skyllias.alomatia.filter.buffered.distortion;

import static org.junit.Assert.assertEquals;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

import org.junit.BeforeClass;
import org.junit.Test;

public class BilinearInterpolatorTest
{
  private static BufferedImage srcImage = new BufferedImage(3, 2, BufferedImage.TYPE_3BYTE_BGR);

  private BilinearInterpolator interpolator = new BilinearInterpolator();

  @BeforeClass
  public static void setUp()
  {
    srcImage.setRGB(0, 0, Color.RED.getRGB());
    srcImage.setRGB(1, 0, Color.GRAY.getRGB());
    srcImage.setRGB(2, 0, Color.BLUE.getRGB());
    srcImage.setRGB(0, 1, Color.BLACK.getRGB());
    srcImage.setRGB(1, 1, Color.GREEN.getRGB());
    srcImage.setRGB(2, 1, Color.WHITE.getRGB());
  }

  @Test
  public void shouldReturnExactColourWhenExactPixel()
  {
    Point2D.Float point = new Point2D.Float(PixelizationConstants.PIXEL_OFFSET,
                                            PixelizationConstants.PIXEL_OFFSET);
    assertEquals("Colour at an exact pixel should be the original colour",
                 Color.RED, interpolator.getColourAt(srcImage, point));
  }

  @Test
  public void shouldReturnMixedColourWhenInBetweenTwoPixels()
  {
    Point2D.Float point = new Point2D.Float(0.5f + PixelizationConstants.PIXEL_OFFSET,
                                            1 + PixelizationConstants.PIXEL_OFFSET);
    assertEquals("Colour between two pixels should be the mix",
                 new Color(0, 128, 0), interpolator.getColourAt(srcImage, point));
  }

  @Test
  public void shouldReturnMixedColourWhenInBetweenFourPixels()
  {
    Point2D.Float point = new Point2D.Float(1.5f + PixelizationConstants.PIXEL_OFFSET,
                                            0.5f + PixelizationConstants.PIXEL_OFFSET);
    assertEquals("Colour between four pixels should be the mix of border pixels",
                 new Color(96, 160, 160), interpolator.getColourAt(srcImage, point));
  }

  @Test
  public void shouldReturnColourWhenOutsidePixel()
  {
    Point2D.Float point = new Point2D.Float(1 + PixelizationConstants.PIXEL_OFFSET,
                                            25f);
    assertEquals("Colour on out of bounds pixel should be the border pixel",
                 Color.GREEN, interpolator.getColourAt(srcImage, point));
  }

  @Test
  public void shouldReturnMixedColourWhenOutsideBetweenPixels()
  {
    Point2D.Float point = new Point2D.Float(-1f,
                                            0.5f + PixelizationConstants.PIXEL_OFFSET);
    assertEquals("Colour between out of bounds pixels should be the mix",
                 new Color(128, 0, 0), interpolator.getColourAt(srcImage, point));
  }
}
