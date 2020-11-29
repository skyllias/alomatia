
package org.skyllias.alomatia.test.matchers;

import java.awt.Image;
import java.awt.image.BufferedImage;

import org.mockito.ArgumentMatcher;
import org.skyllias.alomatia.test.ImageUtils;

/** Factory of image matchers with static methods analogous to Mockito matchers. */

public class AlomatiaMatchers
{
//==============================================================================

  public static ArgumentMatcher<Image> sameImage(final BufferedImage expectedImage)
  {
    return new ArgumentMatcher<Image>()
    {
      @Override
      public boolean matches(Image argument)
      {
        if (argument == null) return false;
        BufferedImage argumentImage = (BufferedImage) argument;

        return ImageUtils.areEqual(argumentImage, expectedImage);
      }
    };
  }

//------------------------------------------------------------------------------

}
