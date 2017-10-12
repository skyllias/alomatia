
package org.skyllias.alomatia.filter.daltonism;

/** Three-component colour space to convert to and from RGB, where projections
 *  are defined.
 *  <p>
 *  Supported conversions are linear combinations of the three components, which
 *  are defined in the form of 3x3 matrices. */

public interface ColourSpace
{
  /** Returns a 3x3 that applied to an RGB colour produces the three components
   *  of the representation of the colour in this colour space. */

  double[][] getConversionMatrix();

  /** Returns a 3x3 that applied to the three components of a representation of
   *  a colour in this space produces the RGB components.
   *  <p>
   *  This MUST be the inverse of getConversionMatrix(). */

  double[][] getInverseConversionMatrix();
}
