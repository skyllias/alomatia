
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
 */

public abstract class BasicDaltonizingFilter extends BasicColorFilter
{
//==============================================================================

  /** See the class description. */

  @Override
  protected Color filterColor(Color original)
  {
    LmsColour lmsColour = new LmsColour(original);
    lmsColour.project(getLmsProjector());
    return lmsColour.getColor();
  }

//------------------------------------------------------------------------------

  /** Returns the LmsProjector to apply by this filter. */

  protected abstract LmsProjector getLmsProjector();

//------------------------------------------------------------------------------

}
