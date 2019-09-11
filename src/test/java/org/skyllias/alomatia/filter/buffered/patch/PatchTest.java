
package org.skyllias.alomatia.filter.buffered.patch;

import static org.junit.Assert.assertEquals;

import java.awt.Color;
import java.awt.Point;

import org.junit.Test;

public class PatchTest
{

  @Test
  public void getCentralPoint()
  {
    Patch patch = new Patch();
    patch.add(createPixel(0, 0));
    patch.add(createPixel(0, 1));
    patch.add(createPixel(0, 2));
    patch.add(createPixel(1, 0));
    patch.add(createPixel(1, 2));
    patch.add(createPixel(2, 0));
    patch.add(createPixel(2, 1));
    patch.add(createPixel(2, 2));

    Point center = patch.getCentralPoint();

    assertEquals(1, center.x);
    assertEquals(1, center.y);
  }

  private Pixel createPixel(int x, int y)
  {
    return new Pixel(new Point(x, y), Color.WHITE);
  }
}
