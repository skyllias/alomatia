
package org.skyllias.alomatia.test;

import java.awt.Color;
import java.awt.image.BufferedImage;

/** Helper class with methods to tell whether two images are equal. */

public class ImageUtils
{
//==============================================================================

  public static boolean areEqual(BufferedImage image1, BufferedImage image2)
  {
    int width  = image1.getWidth();
    int height = image1.getHeight();

    if (width  != image2.getWidth())
    {
System.err.println("Widths do not match: " + width + " vs. " + image2.getWidth());
      return false;
    }
    if (height != image2.getHeight())
    {
System.err.println("Heights do not match: " + height + " vs. " + image2.getHeight());
      return false;
    }

    for (int i = 0; i < width; i++)
    {
      for (int j = 0; j < height; j++)
      {
        int rgb1 = image1.getRGB(i, j);
        int rgb2 = image2.getRGB(i, j);
        if (rgb1 != rgb2)
        {
System.err.println("Colours at (" + i + ", " + j + ") do not match: " + new Color(rgb1) + " vs. " + new Color(rgb2));
          return false;
        }
      }
    }

    return true;
  }

//------------------------------------------------------------------------------

}
