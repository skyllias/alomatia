
package org.skyllias.alomatia.filter.convolve;

/** {@link KernelDataFactory} that mixes the pixles in a straight line, creating
 *  the effect of a motion blur.
 *  The volume of the kernel is always 1.
 *  It allows setting the length and the angle. Due to the convolving algorithm,
 *  both have effects on the filter speed, because the bigger the length and the
 *  closet to the diagonal, the bigger the kernel matrix.
 *  Since accuracy is not really too important when blurring, the drawn on the
 *  kernel is not antialiased. */

public class LinearBlurKernelDataFactory implements KernelDataFactory
{
  private float length;
  private double angle;

//==============================================================================

  /** blurLength should be greater than 2.
   *  motionAngle must be in radians. The result of angle + {@link Math.PI} is
   *  the same as angle.  */

  public LinearBlurKernelDataFactory(float blurLength, double motionAngle)
  {
    length = blurLength;
    angle  = motionAngle;
  }

//==============================================================================

  /* Creates a matrix with at least one item per dimension and a diagonal of
   * approximately length. The diagonal is filled with a value that makes the
   * total sum of filled values 1. */

  @Override
  public float[][] getKernelData()
  {
    boolean invertSlope = Math.tan(angle) > 0;                                  // in the vertical axis, coordinates grow downwards, not upwards

    double horizontality       = Math.abs(Math.cos(angle));
    double verticality         = Math.abs(Math.sin(angle));
    boolean isMainlyHorizontal = horizontality > verticality;
    double biggestCathetus     = Math.max(horizontality, verticality);
    double smallestRatio       = isMainlyHorizontal? verticality / horizontality:
                                                     horizontality / verticality; // always between 0 and 1

    int longestSide  = Math.max(1, (int) Math.round(biggestCathetus * length));
    int shortestSide = 1 + (int) (smallestRatio * (longestSide - 1));           // this funny way to calculate it prevents getting a row or column full of zeros
    int width        = isMainlyHorizontal? longestSide: shortestSide;
    int height       = isMainlyHorizontal? shortestSide: longestSide;

    float contributionPerPixel = 1f / longestSide;
    float[][] kernel           = new float[height][width];
    for (int mainCoordinate = 0; mainCoordinate < longestSide; mainCoordinate++)
    {
      int oppositeCoordinate = (int) (smallestRatio * mainCoordinate);
      int x, y;
      if (isMainlyHorizontal)   // TODO consider negative angles
      {
        x = mainCoordinate;
        y = oppositeCoordinate;
      }
      else
      {
        x = oppositeCoordinate;
        y = mainCoordinate;
      }
      if (invertSlope) y = height - y - 1;
      kernel[y][x] = contributionPerPixel;
    }
    return kernel;
  }

//------------------------------------------------------------------------------

}
