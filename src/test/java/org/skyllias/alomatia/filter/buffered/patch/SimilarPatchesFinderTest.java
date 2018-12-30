
package org.skyllias.alomatia.filter.buffered.patch;

import static org.junit.Assert.*;

import java.awt.image.*;
import java.io.*;
import java.util.*;

import javax.imageio.*;

import org.junit.*;

public class SimilarPatchesFinderTest
{
  private SimilarPatchesFinder finder = new SimilarPatchesFinder(new ColourEquality());

  @Test
  public void mono() throws Exception
  {
    Collection<Patch> result = getPatchesForImageByName("mono");

    assertEquals(1, result.size());
    assertEquals(16, result.iterator().next().getPixels().size());
  }

  @Test
  public void stripes() throws Exception
  {
    Collection<Patch> result = getPatchesForImageByName("stripes");

    assertEquals(4, result.size());
    for (Patch patch : result) assertEquals(4, patch.getPixels().size());
  }

  @Test
  public void checkers() throws Exception
  {
    Collection<Patch> result = getPatchesForImageByName("checkers");

    assertEquals(16, result.size());
    for (Patch patch : result) assertEquals(1, patch.getPixels().size());
  }

  @Test
  public void island() throws Exception
  {
    List<Patch> result = new Vector<>(getPatchesForImageByName("island"));
    Collections.sort(result, new PatchSizeComparator());

    assertEquals(2, result.size());
    assertEquals(1, result.get(0).getPixels().size());
    assertEquals(15, result.get(1).getPixels().size());
  }

  @Test
  public void winding() throws Exception
  {
    List<Patch> result = new Vector<>(getPatchesForImageByName("winding"));
    Collections.sort(result, new PatchSizeComparator());

    assertEquals(3, result.size());
    assertEquals(3, result.get(0).getPixels().size());
    assertEquals(3, result.get(1).getPixels().size());
    assertEquals(10, result.get(2).getPixels().size());
  }

  @Test
  public void multi() throws Exception
  {
    List<Patch> result = new Vector<>(getPatchesForImageByName("multi"));
    Collections.sort(result, new PatchSizeComparator());

    assertEquals(4, result.size());
    assertEquals(1, result.get(0).getPixels().size());
    assertEquals(3, result.get(1).getPixels().size());
    assertEquals(5, result.get(2).getPixels().size());
    assertEquals(7, result.get(3).getPixels().size());
  }

  private Collection<Patch> getPatchesForImageByName(String name) throws IOException
  {
    final String PATH_FORMAT = "/patch/%s.png";

    BufferedImage image = ImageIO.read(getClass().getResourceAsStream(String.format(PATH_FORMAT, name)));
    return finder.findPatches(image);
  }

  private static class PatchSizeComparator implements Comparator<Patch>
  {
    @Override
    public int compare(Patch p1, Patch p2)
    {
      return p1.getPixels().size() - p2.getPixels().size();
    }
  }
}
