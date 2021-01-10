
package org.skyllias.alomatia.filter.daltonism;

import static org.junit.Assert.assertEquals;

import java.awt.Color;
import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class ProjectableColourTest
{
  private final Color originalColor;
  private final ColourProjector projector;
  private final Color expectedResult;

  public ProjectableColourTest(Color originalColor, ColourProjector projector,
                               Color expectedResult)
  {
    this.originalColor  = originalColor;
    this.projector      = projector;
    this.expectedResult = expectedResult;
  }

  @Parameterized.Parameters
  public static Collection<Object[]> primeNumbers()
  {
    return Arrays.asList(new Object[][]
    {
      {new Color(240, 2, 21), getLmsProtanopiaProjector(), new Color(28, 28, 21)},
      {new Color(10, 219, 8), getLmsProtanopiaProjector(), new Color(195, 195, 7)},
      {new Color(8, 23, 234), getLmsProtanopiaProjector(), new Color(21, 21, 233)},
      {new Color(190, 200, 40), getLmsProtanopiaProjector(), new Color(198, 198, 39)},
      {new Color(40, 190, 200), getLmsProtanopiaProjector(), new Color(173, 173, 199)},
      {new Color(200, 40, 190), getLmsProtanopiaProjector(), new Color(57, 57, 190)},

      {new Color(240, 2, 21), getLmsDeuteranopiaProjector(), new Color(71, 71, 15)},
      {new Color(10, 219, 8), getLmsDeuteranopiaProjector(), new Color(157, 157, 12)},
      {new Color(8, 23, 234), getLmsDeuteranopiaProjector(), new Color(18, 18, 234)},
      {new Color(190, 200, 40), getLmsDeuteranopiaProjector(), new Color(197, 197, 40)},
      {new Color(40, 190, 200), getLmsDeuteranopiaProjector(), new Color(146, 146, 203)},
      {new Color(200, 40, 190), getLmsDeuteranopiaProjector(), new Color(86, 86, 186)},

      {new Color(240, 2, 21), getXyzProtanopiaProjector(), new Color(136, 134, 16)},
      {new Color(10, 219, 8), getXyzProtanopiaProjector(), new Color(100, 102, 58)},
      {new Color(8, 23, 234), getXyzProtanopiaProjector(), new Color(14, 14, 183)},
      {new Color(190, 200, 40), getXyzProtanopiaProjector(), new Color(194, 194, 78)},
      {new Color(40, 190, 200), getXyzProtanopiaProjector(), new Color(104, 106, 197)},
      {new Color(200, 40, 190), getXyzProtanopiaProjector(), new Color(130, 129, 153)},

      {new Color(240, 2, 21), getXyzDeuteranopiaProjector(), new Color(150, 168, 15)},
      {new Color(10, 219, 8), getXyzDeuteranopiaProjector(), new Color(88, 72, 71)},
      {new Color(8, 23, 234), getXyzDeuteranopiaProjector(), new Color(13, 12, 170)},
      {new Color(190, 200, 40), getXyzDeuteranopiaProjector(), new Color(193, 193, 88)},
      {new Color(40, 190, 200), getXyzDeuteranopiaProjector(), new Color(96, 85, 197)},
      {new Color(200, 40, 190), getXyzDeuteranopiaProjector(), new Color(140, 152, 145)},
    });
  }

  @Test
  public void shouldProject()
  {
    ProjectableColour projectableColour = new ProjectableColour(originalColor);
    Color result                        = projectableColour.project(projector);

    assertEquals(expectedResult, result);
  }


  private static ColourProjector getLmsProtanopiaProjector()
  {
    return new ColourProjector(getLmsColourSpace(), 0, 2.02344, -2.52581, 0, 1, 0, 0, 0, 1);
  }

  private static ColourProjector getLmsDeuteranopiaProjector()
  {
    return new ColourProjector(getLmsColourSpace(), 1, 0, 0, 0.49421, 0, 1.24827, 0, 0, 1);
  }

  private static ColourProjector getXyzProtanopiaProjector()
  {
    return new ColourProjector(getXyzColourSpace(), 0.56667, 0.43333, 0, 0.55833, 0.44167, 0, 0, 0.24167, 0.75833);
  }

  private static ColourProjector getXyzDeuteranopiaProjector()
  {
    return new ColourProjector(getXyzColourSpace(), 0.625, 0.375, 0, 0.7, 0.3, 0, 0, 0.3, 0.7);
  }

  private static ColourSpace getLmsColourSpace()
  {
    return new ColourSpace()
    {
      @Override
      public double[][] getConversionMatrix()
      {
        return new double[][] {{17.8824, 43.5161, 4.11935},
                               {3.45565, 27.1554, 3.86714},
                               {0.0299566, 0.184309, 1.46709}};
      }
      @Override
      public double[][] getInverseConversionMatrix()
      {
        return new double[][] {{0.0809444479, -0.130504409, 0.116721066},
                               {-0.0102485335, 0.0540193266, -0.113614708},
                               {-0.000365296938, -0.00412161469, 0.693511405}};
      }
    };
  }

  private static ColourSpace getXyzColourSpace()
  {
    return new ColourSpace()
    {
      @Override
      public double[][] getConversionMatrix()
      {
        return new double[][] {{1, 0, 0},
                               {0, 1, 0},
                               {0, 0, 1}};
      }
      @Override
      public double[][] getInverseConversionMatrix()
      {
        return new double[][] {{1, 0, 0},
                               {0, 1, 0},
                               {0, 0, 1}};
      }
    };
  }
}
