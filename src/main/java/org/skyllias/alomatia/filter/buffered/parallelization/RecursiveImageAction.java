
package org.skyllias.alomatia.filter.buffered.parallelization;

import java.util.concurrent.*;

/** Fork/join task that works on some buffered image being filtered by
 *  recursively dividing it into smaller fragments and processing them
 *  concurrently.
 *  Filters that want to parallelize their work have to instantiate this class
 *  with an {@link ImageProcessor} and call the method {@link #invoke()}. */

@SuppressWarnings("serial")
public class RecursiveImageAction extends RecursiveAction
{
  private static final int MAX_SIZE = 100;                                      // this could be flexibilized if needed

  private int xFrom, xTo, yFrom, yTo;                                           // *From inclusive, *To exclusive
  private ImageProcessor imageProcessor;

//==============================================================================

  public RecursiveImageAction(ImageProcessor imageProcessor)
  {
    this(imageProcessor, 0, imageProcessor.getImageWidth(),
         0, imageProcessor.getImageHeight());
  }

//------------------------------------------------------------------------------

  private RecursiveImageAction(ImageProcessor imageProcessor,
                               int xFrom, int xTo, int yFrom, int yTo)
  {
    this.imageProcessor = imageProcessor;
    this.xFrom          = xFrom;
    this.xTo            = xTo;
    this.yFrom          = yFrom;
    this.yTo            = yTo;
  }

//==============================================================================

  /** First, the image is split horizontally until stripes of 100 or less pixels
   *  are obtained; afterwards, it is split vertically with the same criteria.
   *  If the task's fragment is small enough, the real computation is carried out. */

  @Override
  protected void compute()
  {
    if (xTo - xFrom > MAX_SIZE)
    {
      final int halfWay = (xTo - xFrom) / 2;
      RecursiveImageAction leftAction  = new RecursiveImageAction(imageProcessor, xFrom, xFrom + halfWay, yFrom, yTo);
      RecursiveImageAction rightAction = new RecursiveImageAction(imageProcessor, xFrom + halfWay, xTo, yFrom, yTo);
      invokeAll(leftAction, rightAction);
    }
    else if (yTo - yFrom > MAX_SIZE)
    {
      final int halfWay = (yTo - yFrom) / 2;
      RecursiveImageAction topAction    = new RecursiveImageAction(imageProcessor, xFrom, xTo, yFrom, yFrom + halfWay);
      RecursiveImageAction bottomAction = new RecursiveImageAction(imageProcessor, xFrom, xTo, yFrom + halfWay, yTo);
      invokeAll(topAction, bottomAction);
    }
    else processPixels();
  }

//------------------------------------------------------------------------------

  /* Processes all the pixels inside the fragment by invoking processPixel in
   * the image context. */

  private void processPixels()
  {
    for (int i = xFrom; i < xTo; i++)
    {
      for (int j = yFrom; j < yTo; j++)
      {
        imageProcessor.processPixel(i, j);
      }
    }
  }

//------------------------------------------------------------------------------

}
