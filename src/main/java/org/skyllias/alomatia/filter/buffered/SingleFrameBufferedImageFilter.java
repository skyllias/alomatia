
package org.skyllias.alomatia.filter.buffered;

import java.awt.image.BufferedImageFilter;
import java.awt.image.BufferedImageOp;
import java.awt.image.ImageConsumer;

/** {@link BufferedImageFilter} that does not process the
 *  imageComplete(ImageConsumer.STATICIMAGEDONE) notification, as it assumes that
 *  the imageComplete(ImageConsumer.SINGLEFRAMEDONE) must have been called
 *  previously without any difference.
 *  This class exists only because, in Java 8, sun.awt.image.OffScreenImageSource
 *  invokes upon completion imageComplete consecutively, first with
 *  {@link ImageConsumer.SINGLEFRAMEDONE} and then with
 *  {@link ImageConsumer.STATICIMAGEDONE}. BufferedImageFilter implementation
 *  does the same in both calls, which means the {@link BufferedImageOp} is
 *  invoked redundantly (therefore, the filter is applied twice). In previous
 *  versions of Java, only one call was carried out with
 *  ImageConsumer.SINGLEFRAMEDONE. Let's hope that in future versions the
 *  behaviour is not worsened.
 *
 *  This class is expected to be used everywhere instead of its superclass. */

public class SingleFrameBufferedImageFilter extends BufferedImageFilter
{
//==============================================================================

  public SingleFrameBufferedImageFilter(BufferedImageOp op) {super(op);}

//==============================================================================

  /** Calls the superclass' implementation as long as status != STATICIMAGEDONE.
   *  The consumer is notified anyway because otherwise it blocks indefinitely. */

  @Override
  public void imageComplete(int status)
  {
    if (status != STATICIMAGEDONE) super.imageComplete(status);
    else                           consumer.imageComplete(status);
  }

//------------------------------------------------------------------------------

}
