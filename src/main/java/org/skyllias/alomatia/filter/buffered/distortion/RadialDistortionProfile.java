
package org.skyllias.alomatia.filter.buffered.distortion;

/** Function that tells how points along a radius displace in a radial distortion. */

public interface RadialDistortionProfile
{
  /** Returns the relative radial shift of the point to be moved to radius.
   *  0 means that the point is not shifted, negative values mean that pixels
   *  are moved from the center outwards, and positive values mean that pixels
   *  are moved towards the center.
   *  Values below -1 translate points from the antipodes, while functions f(r)
   *  which at some point do not comply with f + rf' > 1 print the same pixels
   *  in different regions.
   *  referenceRadius can be used to scale the function in an
   *  implementation-dependent way. */

  float getSourceRadiusShift(float radius, float referenceRadius);
}
