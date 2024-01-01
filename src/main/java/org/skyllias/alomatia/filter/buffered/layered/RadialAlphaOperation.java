
package org.skyllias.alomatia.filter.buffered.layered;

import java.awt.Color;
import java.awt.image.BufferedImage;

import org.skyllias.alomatia.filter.buffered.BufferedImageOperation;

/** {@link BufferedImageOperation} that makes the center of an image transparent,
 *  gradually becoming more opaque as distance increases. */

public class RadialAlphaOperation implements BufferedImageOperation
{
  private final float relativeRadius;

//==============================================================================

  /** Creates an operation that will apply an alpha with the following pseudo-formula:
   *  <pre> 1 - exp(distance² / 2) </pre>
   *  Where distance is Euclidian but interpreted depending on the location of
   *  the pixel compared to the smallest of the image dimensions.
   *  For relativeRadius = 1, the distance of the pixels at the middle of the
   *  longest sides of the image is 1; for relativeRadius = 0.5, it is 2;
   *  for relativeRadius = 4, it is 0.25; and so on. relativeRadius must be
   *  strictly positive. */

  public RadialAlphaOperation(float relativeRadius)
  {
    this.relativeRadius = relativeRadius;
  }

//==============================================================================

  @Override
  public void filter(BufferedImage inputImage, BufferedImage outputImage)
  {
    final float squaredRelativeRadius = relativeRadius * relativeRadius;

    int width  = inputImage.getWidth();
    int height = inputImage.getHeight();

    int halfWidth       = width / 2;
    int halfHeight      = height / 2;
    int smallestSquared = Math.min(halfWidth * halfWidth, halfHeight * halfHeight);

    for (int x = 0; x < width; x++)
    {
      for (int y = 0; y < height; y++)
      {
        int xFromCenter     = (halfWidth - x);
        int yFromCenter     = (halfHeight - y);
        int squaredDistance = xFromCenter * xFromCenter + yFromCenter * yFromCenter;

        float relativeDistance = squaredDistance / (squaredRelativeRadius * smallestSquared);
        float alpha            = (float) (1 - Math.exp(-relativeDistance / 2));

        Color originalColour    = new Color(inputImage.getRGB(x, y));
        Color transparentColour = new Color(originalColour.getColorSpace(),
                                            originalColour.getColorComponents(null),
                                            alpha);
        outputImage.setRGB(x, y, transparentColour.getRGB());
      }
    }
  }

//------------------------------------------------------------------------------

}
