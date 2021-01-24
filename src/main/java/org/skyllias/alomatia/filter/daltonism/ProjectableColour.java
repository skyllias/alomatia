
package org.skyllias.alomatia.filter.daltonism;

import java.awt.Color;

/** Colour that can be converted into another one by transformation to
 *  {@link ColourSpace} and projections by means of a {@link ColourProjector}. */

public class ProjectableColour
{
  private final Color originalColour;

//==============================================================================

  public ProjectableColour(Color rgbColor)
  {
    originalColour = rgbColor;
  }

//==============================================================================

  /** Transforms the colour to the space of the projector, performs the
   * projection and transforms back to RGB space, making sure that the
   * components are in the valid range. */

  public Color project(ColourProjector projector)
  {
    double[] transformedCoords = getCoordinatesIn(projector.getSpace());
    double[] projectedCoords   = project(projector, transformedCoords);
    return getRgbColour(projectedCoords, projector.getSpace());
  }

//------------------------------------------------------------------------------

  private double[] getCoordinatesIn(ColourSpace space)
  {
    double[][] conversionMatrix = space.getConversionMatrix();

    int r = originalColour.getRed();
    int g = originalColour.getGreen();
    int b = originalColour.getBlue();

    double coords1 = conversionMatrix[0][0] * r + conversionMatrix[0][1] * g + conversionMatrix[0][2] * b;
    double coords2 = conversionMatrix[1][0] * r + conversionMatrix[1][1] * g + conversionMatrix[1][2] * b;
    double coords3 = conversionMatrix[2][0] * r + conversionMatrix[2][1] * g + conversionMatrix[2][2] * b;

    return new double[] {coords1, coords2, coords3};
  }

//------------------------------------------------------------------------------

  /* Makes the projection of coords with projector. */

  private double[] project(ColourProjector projector, double[] coords)
  {
    double projection1 = projector.getO1ToR1() * coords[0] + projector.getO2ToR1() * coords[1] + projector.getO3ToR1() * coords[2];
    double projection2 = projector.getO1ToR2() * coords[0] + projector.getO2ToR2() * coords[1] + projector.getO3ToR2() * coords[2];
    double projection3 = projector.getO1ToR3() * coords[0] + projector.getO2ToR3() * coords[1] + projector.getO3ToR3() * coords[2];

    return new double[] {projection1, projection2, projection3};
  }

//------------------------------------------------------------------------------

  /** Returns the best Color represented by coords. */

  private Color getRgbColour(double[] coords, ColourSpace space)
  {
    double[][] inverseMatrix = space.getInverseConversionMatrix();

    double r = inverseMatrix[0][0] * coords[0] + inverseMatrix[0][1] * coords[1] + inverseMatrix[0][2] * coords[2];
    double g = inverseMatrix[1][0] * coords[0] + inverseMatrix[1][1] * coords[1] + inverseMatrix[1][2] * coords[2];
    double b = inverseMatrix[2][0] * coords[0] + inverseMatrix[2][1] * coords[1] + inverseMatrix[2][2] * coords[2];

    return new Color(fix(r), fix(g), fix(b));
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
