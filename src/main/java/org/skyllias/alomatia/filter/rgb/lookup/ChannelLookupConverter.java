
package org.skyllias.alomatia.filter.rgb.lookup;

import java.awt.Color;

import org.skyllias.alomatia.filter.ColourConverter;

/** Converter that maps each channel according to a {@link ColourLookup}. */

public class ChannelLookupConverter implements ColourConverter
{
  private final ColourLookup lookup;

//==============================================================================

  public ChannelLookupConverter(ColourLookup colourLookup) {lookup = colourLookup;}

//==============================================================================

  @Override
  public Color convertColour(Color original)
  {
    int red   = original.getRed();
    int green = original.getGreen();
    int blue  = original.getBlue();
    return new Color(getLookupValue(lookup.getRedArray(), red),
                     getLookupValue(lookup.getGreenArray(), green),
                     getLookupValue(lookup.getBlueArray(), blue));
  }

//------------------------------------------------------------------------------

  /* Returns lookupValues[index] taking care of max and min values. */

  private int getLookupValue(int[] lookupValues, int index)
  {
    final int MIN_VALUE = 0;
    final int MAX_VALUE = 255;

    if (lookupValues.length == 0) return index;

    int valueFromLookup;
    try {valueFromLookup = lookupValues[index];}
    catch (ArrayIndexOutOfBoundsException aioobe) {valueFromLookup = lookupValues[lookupValues.length-1];}

    if (valueFromLookup < MIN_VALUE) valueFromLookup = MIN_VALUE;
    if (valueFromLookup > MAX_VALUE) valueFromLookup = MAX_VALUE;
    return valueFromLookup;
  }

//------------------------------------------------------------------------------


}
