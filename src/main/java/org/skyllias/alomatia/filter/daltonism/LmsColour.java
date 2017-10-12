
package org.skyllias.alomatia.filter.daltonism;

import java.awt.*;

/** Representation of a colour in the LMS colour-space.
 *  <p>
 *  Instances can be derived from a {@link Color} instance and then modified by
 *  applying a projection (matrix multiplication) with an LmsProjector.
 *  See https://stacks.stanford.edu/file/druid:yj296hj2790/Woods_Assisting_Color_Blind_Viewers.pdf.
 *  This class is not thread-safe. */
@Deprecated
public class LmsColour
{
  private static final double[][] RGB_TO_LMS_CONVERSOR = new double[][] {{17.8824, 43.5161, 4.11935},
                                                                         {3.45565, 27.1554, 3.86714},
                                                                         {0.0299566, 0.184309, 1.46709}};
  private static final double[][] LMS_TO_RGB_CONVERSOR = new double[][] {{0.0809444479, -0.130504409, 0.116721066},
                                                                         {-0.0102485335, 0.0540193266, -0.113614708},
                                                                         {-0.000365296938, -0.00412161469, 0.693511405}};   // the inverse of RGB_TO_LMS_CONVERSOR

  private double l, m, s;                                                       // the coordinates in the LMS space, based on RGB coordinates between 0 and 255

//==============================================================================

  /** Creates a representation of rgbColor in the LMS colour-space. */

  public LmsColour(Color rgbColor)
  {
    int r = rgbColor.getRed();
    int g = rgbColor.getGreen();
    int b = rgbColor.getBlue();

    l = RGB_TO_LMS_CONVERSOR[0][0] * r + RGB_TO_LMS_CONVERSOR[0][1] * g + RGB_TO_LMS_CONVERSOR[0][2] * b;
    m = RGB_TO_LMS_CONVERSOR[1][0] * r + RGB_TO_LMS_CONVERSOR[1][1] * g + RGB_TO_LMS_CONVERSOR[1][2] * b;
    s = RGB_TO_LMS_CONVERSOR[2][0] * r + RGB_TO_LMS_CONVERSOR[2][1] * g + RGB_TO_LMS_CONVERSOR[2][2] * b;
  }

//==============================================================================

  /** Returns the best Color represented by the current LMS values.
   *  If no projection has been applied, this should be equal to the Color passed
   *  to the constructor. */

  public Color getColor()
  {
    double r = LMS_TO_RGB_CONVERSOR[0][0] * l + LMS_TO_RGB_CONVERSOR[0][1] * m + LMS_TO_RGB_CONVERSOR[0][2] * s;
    double g = LMS_TO_RGB_CONVERSOR[1][0] * l + LMS_TO_RGB_CONVERSOR[1][1] * m + LMS_TO_RGB_CONVERSOR[1][2] * s;
    double b = LMS_TO_RGB_CONVERSOR[2][0] * l + LMS_TO_RGB_CONVERSOR[2][1] * m + LMS_TO_RGB_CONVERSOR[2][2] * s;

    return new Color(fix(r), fix(g), fix(b));
  }

//------------------------------------------------------------------------------

  /** Modifies the LMS components of the colour by linearly combining them with
   *  the factors from the projector. */

  public void project(LmsProjector projector)
  {
    double newL = projector.getlToL() * l + projector.getmToL() * m + projector.getsToL() * s;
    double newM = projector.getlToM() * l + projector.getmToM() * m + projector.getsToM() * s;
    double newS = projector.getlToS() * l + projector.getmToS() * m + projector.getsToS() * s;

    l = newL;
    m = newM;
    s = newS;
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
