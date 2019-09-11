
package org.skyllias.alomatia.filter.buffered.vignette;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;

import org.skyllias.alomatia.filter.buffered.BasicBufferedImageOp;
import org.skyllias.alomatia.filter.buffered.DyeOp;

/** {@link BufferedImageOp} that paints a partially opaque, black rectangle in
 *  front of the source image. In opposition to {@link DyeOp}, the transparency
 *  is different in each pixel, with darker edges. The profile of transparency
 *  is defined by a {@link VignetteProfile}.
 *  The "Resizing" in the name refers to the fact that one single, squared
 *  profile is obtained only once, and then it is reshaped to match the
 *  destination image size. This means that the consuming operation of
 *  generating the profile is carried out only once, at the expense of always
 *  darkening the same areas. Other VignetteOps may deal with profiles in a
 *  different way. */

public class ResizingVignetteOp extends BasicBufferedImageOp
{
  private static ThreadFactory lowerPriorityThreadFactory;
  private ExecutorService executorService = Executors.newSingleThreadExecutor(lowerPriorityThreadFactory);
  private Future<BufferedImage> futureVignetteImage;                            // the precalculated, semi-transparent image generated from a profile and a colour that is drawn in front of the source image, needs a lot of processing but usually is not immediately required

//==============================================================================

  /* threadFactory could be non-static and initialized in the constructor, or
   * even inline, but this way it is cleaner and less forgetable.
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

  public ResizingVignetteOp(VignetteProfile vignetteProfile) {this(vignetteProfile, Color.BLACK);}

//------------------------------------------------------------------------------

  /** Creates a BufferedImageOp that applies vignetteProfile with vignetteColour. */

  public ResizingVignetteOp(VignetteProfile vignetteProfile, Color vignetteColour)
  {
    futureVignetteImage = getFutureVignette(vignetteProfile, vignetteColour);
  }

//==============================================================================

  /** Copies src and draws the precalculated vignette image when available. */

  @Override
  public void doFilter(BufferedImage src, BufferedImage dest)
  {
    Graphics2D graphics = dest.createGraphics();
    graphics.drawImage(src, 0, 0, null);
    try {graphics.drawImage(futureVignetteImage.get(), 0, 0, dest.getWidth(), dest.getHeight(), null);}
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
    Graphics2D graphics  = result.createGraphics();

    for (int x = 0; x < WIDTH; x++)
    {
      for (int y = 0; y < HEIGHT; y++)
      {
        float alpha = profile.getVignetteEffect(x, y, WIDTH, HEIGHT);
        graphics.setColor(new Color(red, green, blue, alpha));
        graphics.fillRect(x, y, 1, 1);                                          // isn't there a paintPixel(x, y)?
      }
    }
    graphics.dispose();
    return result;
  }

//------------------------------------------------------------------------------

}
