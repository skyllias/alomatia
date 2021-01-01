
package org.skyllias.alomatia.test.matchers;

import java.awt.image.BufferedImage;

import org.mockito.ArgumentMatcher;
import org.skyllias.alomatia.test.ImageUtils;

/** Factory of image matchers with static methods analogous to Mockito matchers. */

public class AlomatiaMatchers
{
//==============================================================================

  public static ArgumentMatcher<BufferedImage> sameImage(final BufferedImage expectedImage)
  {
    return new ArgumentMatcher<BufferedImage>()
    {
      @Override
      public boolean matches(BufferedImage argument)
      {
        if (argument == null) return false;

        return ImageUtils.areEqual(argument, expectedImage);
      }
    };
  }

//------------------------------------------------------------------------------

}
