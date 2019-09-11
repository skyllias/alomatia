
package org.skyllias.alomatia.filter.buffered.distortion.wave;

import static org.junit.Assert.assertEquals;

import java.awt.Dimension;
import java.awt.geom.Point2D;

import org.junit.Test;

public class IsotropicWaveDistortionTest
{
  @Test
  public void shouldNotMovePixelsAtPeriodInHorizontalLongitudinalWaves()
  {
    IsotropicWaveDistortion wave = new IsotropicWaveDistortion(0, 0, 100, 50);
    Point2D.Float source         = wave.getSourcePoint(new Point2D.Float(200, 110), new Dimension());

    assertEquals(200f, source.getX(), 0.5f);
    assertEquals(110f, source.getY(), 0.5f);
  }

  @Test
  public void shouldNotMovePixelsAtHalfPeriodInHorizontalLongitudinalWaves()
  {
    IsotropicWaveDistortion wave = new IsotropicWaveDistortion(0, 0, 100, 50);
    Point2D.Float source         = wave.getSourcePoint(new Point2D.Float(150, 110), new Dimension());

    assertEquals(150f, source.getX(), 0.5f);
    assertEquals(110f, source.getY(), 0.5f);
  }

  @Test
  public void shouldMovePixelsAtQuarterPeriodInHorizontalLongitudinalWaves()
  {
    IsotropicWaveDistortion wave = new IsotropicWaveDistortion(0, 0, 100, 50);
    Point2D.Float source         = wave.getSourcePoint(new Point2D.Float(125, 110), new Dimension());

    assertEquals(175f, source.getX(), 0.5f);
    assertEquals(110f, source.getY(), 0.5f);
  }

  @Test
  public void shouldNotMovePixelsAtPeriodInHorizontalPerpendicularWaves()
  {
    IsotropicWaveDistortion wave = new IsotropicWaveDistortion(0, (float) (Math.PI / 2), 100, 50);
    Point2D.Float source         = wave.getSourcePoint(new Point2D.Float(200, 110), new Dimension());

    assertEquals(200f, source.getX(), 0.5f);
    assertEquals(110f, source.getY(), 0.5f);
  }

  @Test
  public void shouldNotMovePixelsAtHalfPeriodInHorizontalPerpendicularWaves()
  {
    IsotropicWaveDistortion wave = new IsotropicWaveDistortion(0, (float) (Math.PI / 2), 100, 50);
    Point2D.Float source         = wave.getSourcePoint(new Point2D.Float(150, 110), new Dimension());

    assertEquals(150f, source.getX(), 0.5f);
    assertEquals(110f, source.getY(), 0.5f);
  }

  @Test
  public void shouldMovePixelsAtQuarterPeriodInHorizontalPerpendicularWaves()
  {
    IsotropicWaveDistortion wave = new IsotropicWaveDistortion(0, (float) (Math.PI / 2), 100, 50);
    Point2D.Float source         = wave.getSourcePoint(new Point2D.Float(125, 110), new Dimension());

    assertEquals(125f, source.getX(), 0.5f);
    assertEquals(60f, source.getY(), 0.5f);
  }
}
