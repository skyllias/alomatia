
package org.skyllias.alomatia.filter.daltonism;

import java.awt.*;

import org.skyllias.alomatia.filter.*;

/** Superclass for the filters that reduce the visible colours simulating some
 *  anomaly in the eye receptors.
 *  <p>
 *  According to "Digital Video Colourmaps for Checking the Legibility of
 *  Displays by Dichromats" by Vienot, Brettel and Mollon, the empirical
 *  transformation algorithm for each colour is:
 *  <ol>
 *  <li> Move from RGB colour-space to LMS colour-space.
 *  <li> Apply some projection (different for each anomaly) in the LMS colour-space.
 *  <li> Move back to the RGB colour-space.
 *  <li> Fix colour components that go beyond the 0 or 255 RGB limits.
 *  </ol>
 *  Some compensations described in other papers ("Modifying Images for Color
 *  Blind Viewers" by William Woods), plus some improvements described in
 *  "Intelligent modification for the daltonization process of digitized paintings"
 *  for fine tuning, could be carried out to improve the view of the image by a
 *  colour blind person, but the objective here is to simulate the anomaly, not
 *  to mend it.
 *  <p>
 *  com.intellij.ide.ui.DaltonizationFilter and related classes by Sergey Malenkov
 *  do similar things, but finally apply the corrections mentioned above.
 *
 *  All in all, this filter does not enforce the LMS colour-space. This is so
 *  because the LMS projectors found here and there
 *  (https://stacks.stanford.edu/file/druid:yj296hj2790/Woods_Assisting_Color_Blind_Viewers.pdf)
 *  work reasonably well for protanopia and deuteranopia, but not convincingly at
 *  all for tritanopia. On the other hand, here and there can be found projections
 *  for protanomaly, deuteranomaly and tritanomaly in the XYZ space, but not in
 *  the LMS space. So, the filter is design so that it only cares about the
 *  projection to apply, and converting to and from a suitable {@link ProjectableColour},
 *  whatever the space they are defined for.
 */

public abstract class BasicDaltonizingFilter extends BasicColorFilter
{
//==============================================================================

  /** The colour component fixing mentioned in the class description must be
   *  cared of internally by {@link ProjectableColour}. */

  @Override
  protected Color filterColor(Color original)
  {
    ColourProjector projector           = getProjector();
    ProjectableColour projectableColour = new ProjectableColour(projector.getSpace(), original);
    projectableColour.project(projector);
    return projectableColour.getColor();
  }

//------------------------------------------------------------------------------

  /** Returns the ColourProjector to apply by this filter. */

  protected abstract ColourProjector getProjector();

//------------------------------------------------------------------------------

}
