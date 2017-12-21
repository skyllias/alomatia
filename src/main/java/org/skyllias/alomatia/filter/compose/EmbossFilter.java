
package org.skyllias.alomatia.filter.compose;

import java.awt.image.*;

import org.skyllias.alomatia.filter.buffered.*;
import org.skyllias.alomatia.filter.convolve.*;
import org.skyllias.alomatia.filter.rgb.*;

/** Composition of filters that produce an embossing effect on pictures.
 *  First the original channels are shifted, then embossed and then
 *  grey-scaled. */

public class EmbossFilter extends ComposedFilter
{
  private static final float MAX_VALUE = 255;

//==============================================================================

  /** Composes the following filters:
   *  <ol>
   *  <li> A rescaling filter that multiplies the values of the channels of each
   *       pixel by scale and adds offset to each one. Reasonable values for scale
   *       are between 0.05 (below 0.005, the granularity becomes too coarse for
   *       any input) and 1 (above 5, most channel values will become either the
   *       minimum or the maximum). Reasonable values for offset are between 0 and 1.
   *  <li> A filter that resets the alpha channel to 1.0.
   *  <li> A convolving filter with an embossing kernel with the passed volume
   *       and slope (see {@link EmbossKernelDataFactory}.
   *  <li> A grey scaling filter.
   *  </ol> */

  public EmbossFilter(float scale, float offset, float volume, float slope)
  {
    super(new SingleFrameBufferedImageFilter(new RescaleOp(scale, offset, null)),
          new VoidFilter(),
          new EdgeConvolvingComposedFilter(new EmbossKernelDataFactory(volume, slope)),
          new GreyScaleFilter());
  }

//------------------------------------------------------------------------------

//==============================================================================

  /** Returns a new instance with a configuration that in some pictures produces
   *  the effect of a superposition of layers. */

  public static EmbossFilter forLayeredEmboss()
  {
    final float RESCALE_SCALE  = 0.1f;
    final float RESCALE_OFFSET = MAX_VALUE * (1 - RESCALE_SCALE);               // not centered at 128
    final float EMBOSS_VOLUME  = 0.5f;
    final float EMBOSS_SLOPE   = 5;

    return new EmbossFilter(RESCALE_SCALE, RESCALE_OFFSET, EMBOSS_VOLUME, EMBOSS_SLOPE);
  }

//------------------------------------------------------------------------------

  /** Returns a new instance with a configuration that in some pictures produces
   *  the effect of a smooth grinder. */

  public static EmbossFilter forSmoothEmboss()
  {
    final float RESCALE_SCALE  = 0.3f;
    final float RESCALE_OFFSET = MAX_VALUE * (1 - RESCALE_SCALE);               // not centered at 128
    final float EMBOSS_VOLUME  = 0.8f;
    final float EMBOSS_SLOPE   = 6;

    return new EmbossFilter(RESCALE_SCALE, RESCALE_OFFSET, EMBOSS_VOLUME, EMBOSS_SLOPE);
  }

//------------------------------------------------------------------------------

}
