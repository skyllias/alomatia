
package org.skyllias.alomatia.filter.buffered.distortion.rotational;

/** Function that tells how points are rotated according to their distance to
 *  the center. */

public interface RotationalDistortionProfile
{
  /** Returns the amount of radians the source of a point located at radius
   *  pixels is to be found.
   *  In order to get anticlockwise image rotations, this must return a negative
   *  number.
   *  referenceRadius can be used to scale the function in an
   *  implementation-dependent way. */

  float getSourceAngularShift(float radius, float referenceRadius);
}
