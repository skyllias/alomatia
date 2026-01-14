
package org.skyllias.alomatia.filter.buffered.vignette;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;

import org.skyllias.alomatia.filter.buffered.BufferedImageOperation;
import org.skyllias.alomatia.filter.buffered.simple.DyeOperation;

/** {@link BufferedImageOperation} that paints a partially opaque, black
 *  rectangle in front of the source image. In opposition to {@link DyeOperation},
 *  the transparency is different in each pixel, with darker edges. The profile
 *  of transparency is defined by a {@link VignetteProfile}.
 *  The "Resizing" in the name refers to the fact that one single, squared
 *  profile is obtained only once, and then it is reshaped to match the
 *  destination image size. This means that the consuming operation of
 *  generating the profile is carried out only once, at the expense of always
 *  darkening the same areas. Other VignetteOps may deal with profiles in a
 *  different way. */

public class ResizingVignetteOperation implements BufferedImageOperation
{
  private static final ThreadFactory lowerPriorityThreadFactory;
  private final ExecutorService executorService = Executors.newSingleThreadExecutor(lowerPriorityThreadFactory);

  private final Future<BufferedImage> futureVignetteImage;                      // the precalculated, semi-transparent image generated from a profile and a colour that is drawn in front of the source image; needs a lot of processing but usually is not immediately required

//==============================================================================

  /* threadFactory could be non-static and initialized in the constructor, or
   * even inline, but this way it is cleaner and less forgettable.
   * It uses the instances from the default Executors.defaultThreadFactory() but
   * lowering their priority. */

  static
  {
    lowerPriorityThreadFactory = new ThreadFactory()
    {
      @Override
      public Thread newThread(Runnable runnable)
      {
        Thread thread = Executors.defaultThreadFactory().newThread(runnable);
        thread.setPriority(Thread.MIN_PRIORITY);
        return thread;
      }
    };
  }

//==============================================================================

  /** Creates a BufferedImageOp that applies a black vignetteProfile. */

  public ResizingVignetteOperation(VignetteProfile vignetteProfile) {this(vignetteProfile, Color.BLACK);}

//------------------------------------------------------------------------------

  /** Creates a BufferedImageOp that applies vignetteProfile with vignetteColour. */

  public ResizingVignetteOperation(VignetteProfile vignetteProfile, Color vignetteColour)
  {
    futureVignetteImage = getFutureVignette(vignetteProfile, vignetteColour);
  }

//==============================================================================

  /** Copies the input image and draws the precalculated vignette image on it
   *  when available. */

  @Override
  public void filter(BufferedImage inputImage, BufferedImage outputImage)
  {
    Graphics2D graphics = outputImage.createGraphics();
    graphics.drawImage(inputImage, 0, 0, null);
    try {graphics.drawImage(futureVignetteImage.get(), 0, 0,
                            outputImage.getWidth(), outputImage.getHeight(), null);}
    catch (Exception e) {}                                                      // TODO log exceptions

    graphics.dispose();
  }

//------------------------------------------------------------------------------

  /* Returns a Future for the result of getVignetteImage(profile, colour) */

  private Future<BufferedImage> getFutureVignette(final VignetteProfile profile, final Color colour)
  {
    return executorService.submit(new Callable<BufferedImage>()
    {
      @Override
      public BufferedImage call() throws Exception
      {
        return getVignetteImage(profile, colour);
      }
    });
  }

//------------------------------------------------------------------------------

  /* Returns a new image some fixed size with all the pixels painted with colour
   * and each one with an alpha proportional to profile. */

  private BufferedImage getVignetteImage(VignetteProfile profile, Color colour)
  {
    final int WIDTH  = 1000;                                                    // this size requires high initialization times, but smaller images result in pixelization
    final int HEIGHT = 1000;

    float[] rgbComponents = colour.getRGBColorComponents(null);
    float red             = rgbComponents[0];
    float green           = rgbComponents[1];
    float blue            = rgbComponents[2];

    BufferedImage result = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_4BYTE_ABGR);

    for (int x = 0; x < WIDTH; x++)
    {
      for (int y = 0; y < HEIGHT; y++)
      {
        float alpha = profile.getVignetteEffect(x, y, WIDTH, HEIGHT);
        result.setRGB(x, y, new Color(red, green, blue, alpha).getRGB());
      }
    }

    return result;
  }

//------------------------------------------------------------------------------

}
