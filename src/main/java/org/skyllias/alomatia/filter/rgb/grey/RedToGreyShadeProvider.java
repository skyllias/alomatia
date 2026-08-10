
package org.skyllias.alomatia.filter.rgb.grey;

/** Producer of grey pixels as light or dark as their red channel. */

public class RedToGreyShadeProvider implements GreyShadeProvider
{
//==============================================================================

  @Override
  public int getShade(int red, int green, int blue) {return red;}

//------------------------------------------------------------------------------

}
