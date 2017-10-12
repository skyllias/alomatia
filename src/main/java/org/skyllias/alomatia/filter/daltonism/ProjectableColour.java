
package org.skyllias.alomatia.filter.daltonism;

import java.awt.*;

/** Representation of a colour in some particular {@link ColourSpace} that admits
 *  projections by means of a {@link ColourProjector}.
 *  <p>
 *  Instances can be derived from a {@link Color} instance and then modified by
 *  applying a projection (matrix multiplication) with a projector.
 *  This class is not thread-safe. */

public class ProjectableColour
{
  private ColourSpace space;                                                    // the space where the coordinates are defined
  private double[] coords = new double[3];                                      // the coordinates in the corresponding space. The valid range is space-dependent

//==============================================================================

  /** Creates a representation of rgbColor in colourSpace. */

  public ProjectableColour(ColourSpace colourSpace, Color rgbColor)
  {
    space                       = colourSpace;
    double[][] conversionMatrix = space.getConversionMatrix();

    int r = rgbColor.getRed();
    int g = rgbColor.getGreen();
    int b = rgbColor.getBlue();

    coords[0] = conversionMatrix[0][0] * r + conversionMatrix[0][1] * g + conversionMatrix[0][2] * b;
    coords[1] = conversionMatrix[1][0] * r + conversionMatrix[1][1] * g + conversionMatrix[1][2] * b;
    coords[2] = conversionMatrix[2][0] * r + conversionMatrix[2][1] * g + conversionMatrix[2][2] * b;
  }

//------------------------------------------------------------------------------

  /** Returns the best Color represented by the current coordinates.
   *  If no projection has been applied, this should be equal to the Color passed
   *  to the constructor. */

  public Color getColor()
  {
    double[][] inverseMatrix = space.getInverseConversionMatrix();

    double r = inverseMatrix[0][0] * coords[0] + inverseMatrix[0][1] * coords[1] + inverseMatrix[0][2] * coords[2];
    double g = inverseMatrix[1][0] * coords[0] + inverseMatrix[1][1] * coords[1] + inverseMatrix[1][2] * coords[2];
    double b = inverseMatrix[2][0] * coords[0] + inverseMatrix[2][1] * coords[1] + inverseMatrix[2][2] * coords[2];

    return new Color(fix(r), fix(g), fix(b));
  }

//------------------------------------------------------------------------------

  /** If the projector works on the same space that this colour is represented in,
   *  modifies the components of the colour by linearly combining them with
   *  the factors from the projector.
   *  Otherwise, an exception is thrown. */

  public void project(ColourProjector projector)
  {
    if (!space.equals(projector.getSpace())) throw new IllegalArgumentException("Trying to apply a projector for a colour space different from the space the projectable colour lives in");

    double new1 = projector.getO1ToR1() * coords[0] + projector.getO2ToR1() * coords[1] + projector.getO3ToR1() * coords[2];
    double new2 = projector.getO1ToR2() * coords[0] + projector.getO2ToR2() * coords[1] + projector.getO3ToR2() * coords[2];
    double new3 = projector.getO1ToR3() * coords[0] + projector.getO2ToR3() * coords[1] + projector.getO3ToR3() * coords[2];

    coords[0] = new1;
    coords[1] = new2;
    coords[2] = new3;
  }

//------------------------------------------------------------------------------

  /* Converts component to an int, making sure it is not below 0 or above 255. */

  private int fix(double component)
  {
    final int MIN = 0, MAX = 255;

    int intValue = (int) component;
    if (intValue < MIN) intValue = MIN;
    if (intValue > MAX) intValue = MAX;

    return intValue;
  }

//------------------------------------------------------------------------------

}
