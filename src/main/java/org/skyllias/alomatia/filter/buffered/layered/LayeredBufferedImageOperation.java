
package org.skyllias.alomatia.filter.buffered.layered;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.skyllias.alomatia.filter.buffered.BufferedImageOperation;
import org.skyllias.alomatia.filter.buffered.HintlessBufferedImageOp;

/** {@link BufferedImageOperation} that delegates the image modifications to
 *  other operations and then stacks the resulting images.
 *  It is meant to receive two or more different operations, with the first one
 *  generating an opaque image and all the others with some degree of
 *  transparency. Otherwise, all the operations below the last opaque one
 *  would be useless. */

public class LayeredBufferedImageOperation implements BufferedImageOperation
{
  private final List<HintlessBufferedImageOp> bufferedImageOps;                 // lowest layer at 0

//==============================================================================

  /** Sets up an operation that will apply the items in layerOperations in order,
   *  with the first one at the bottom and the last one at the top.
   *  There may be any amount of operations, but obviously less than two are
   *  useless. */

  public LayeredBufferedImageOperation(BufferedImageOperation... layerOperations)
  {
    bufferedImageOps = Stream.of(layerOperations)
                             .map(HintlessBufferedImageOp::new)
                             .collect(Collectors.toList());
  }

//==============================================================================

  /** Applies all the operations sequentially and stacks the results. */

  @Override
  public void filter(BufferedImage inputImage, BufferedImage outputImage)
  {
    Graphics2D graphics = outputImage.createGraphics();

    bufferedImageOps.stream()
                    .map(operation -> operation.filter(inputImage, null))
                    .forEach(layer -> graphics.drawImage(layer, 0, 0, null));

    graphics.dispose();
  }

//------------------------------------------------------------------------------

}
