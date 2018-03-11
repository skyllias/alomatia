
package org.skyllias.alomatia.filter.buffered.distortion;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.*;
import java.awt.geom.*;

import org.junit.*;

public class DistortionChainTest
{
  @Test
  public void shouldChainDistortions()
  {
    Point2D.Float destination = new Point2D.Float(100, 100);
    Point2D.Float source1     = new Point2D.Float(150, 50);
    Point2D.Float source2     = new Point2D.Float(75, 25);
    Point2D.Float source3     = new Point2D.Float(750, 250);

    Distortion distortion1 = mock(Distortion.class);
    when(distortion1.getSourcePoint(eq(destination), any(Dimension.class))).thenReturn(source1);
    Distortion distortion2 = mock(Distortion.class);
    when(distortion2.getSourcePoint(eq(source1), any(Dimension.class))).thenReturn(source2);
    Distortion distortion3 = mock(Distortion.class);
    when(distortion3.getSourcePoint(eq(source2), any(Dimension.class))).thenReturn(source3);

    DistortionChain chain           = new DistortionChain(distortion1, distortion2, distortion3);
    Point2D.Float accumulatedSource = chain.getSourcePoint(destination, new Dimension());

    assertEquals(750f, accumulatedSource.getX(), 0.5f);
    assertEquals(250f, accumulatedSource.getY(), 0.5f);
  }
}
