
package org.skyllias.alomatia.filter.rgb.grey;

/** Producer of grey pixels as light or dark as their blue channel. */

public class BlueToGreyShadeProvider implements GreyShadeProvider
{
//==============================================================================

  @Override
  public int getShade(int red, int green, int blue) {return blue;}

//------------------------------------------------------------------------------

}
