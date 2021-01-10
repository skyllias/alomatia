
package org.skyllias.alomatia.filter.daltonism;

import java.awt.Color;

import org.skyllias.alomatia.filter.ColourConverter;

/** Colour converter that reduces the visible colours simulating some anomaly in
 * the eye receptors.
 *
 *  According to "Digital Video Colourmaps for Checking the Legibility of
 *  Displays by Dichromats" by Vienot, Brettel and Mollon, the empirical
 *  transformation algorithm for each colour is:
 *  - Move from RGB colour-space to LMS colour-space.
 *  - Apply some projection (different for each anomaly) in the LMS colour-space.
 *  - Move back to the RGB colour-space.
 *  - Fix colour components that go beyond the 0 or 255 RGB limits.
 *
 *  Some compensations described in other papers ("Modifying Images for Color
 *  Blind Viewers" by William Woods), plus some improvements described in
 *  "Intelligent modification for the daltonization process of digitized
 *  paintings" for fine tuning, could be carried out to improve the view of the
 *  image by a colour blind person, but the objective here is to simulate the
 *  anomaly, not to mend it.
 *
 *  com.intellij.ide.ui.DaltonizationFilter and related classes by Sergey
 *  Malenkov do similar things, but finally apply the corrections mentioned
 *  above.
 *
 *  All in all, this filter does not enforce the LMS colour-space. This is so
 *  because the LMS projectors found here and there
 *  (https://stacks.stanford.edu/file/druid:yj296hj2790/Woods_Assisting_Color_Blind_Viewers.pdf)
 *  work reasonably well for protanopia and deuteranopia, but not convincingly
 *  at all for tritanopia. On the other hand, here and there can be found
 *  projections for protanomaly, deuteranomaly and tritanomaly in the XYZ space,
 *  but not in the LMS space. So, the filter is design so that it only cares
 *  about the projection to apply, and converting to and from a suitable {@link
 *  ProjectableColour}, whatever the space they are defined for.
 */

public class DaltonizingColourConverter implements ColourConverter
{
  private final ColourProjector projector;

//==============================================================================

  /** Creates a converter that uses the passed projector to transform to its
   *  colour-space, make the projection and then transform back to RGB. */

  public DaltonizingColourConverter(ColourProjector projector)
  {
    this.projector = projector;
  }

//==============================================================================

  /** Returns the colour resulting from transform original to some non-RGB
   *  colour-space, project it by projector, and transform back to RGB. */

  @Override
  public Color convertColour(Color original)
  {
    ProjectableColour projectableColour = new ProjectableColour(original);
    return projectableColour.project(projector);
  }

//------------------------------------------------------------------------------

}
