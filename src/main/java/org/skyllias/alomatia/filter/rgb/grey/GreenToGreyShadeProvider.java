
package org.skyllias.alomatia.filter.rgb.grey;

/** Producer of grey pixels as light or dark as their green channel. */

public class GreenToGreyShadeProvider implements GreyShadeProvider
{
//==============================================================================

  @Override
  public int getShade(int red, int green, int blue) {return green;}

//------------------------------------------------------------------------------

}
