
package org.skyllias.alomatia.filter;

import java.util.*;

import org.skyllias.alomatia.filter.demo.*;
import org.skyllias.alomatia.filter.hsb.*;

/** FilterFactory with a hardcoded set of available filters. */

public class FixedFilterFactory implements FilterFactory
{
  private static final String NO_FILTER_NAME         = "filter.selector.none";
  private static final String LIGHTER_FILTER_NAME    = "filter.demo.brighter.name";
  private static final String DARKER_FILTER_NAME     = "filter.demo.darker.name";
  private static final String RGBR_FILTER_NAME       = "filter.demo.gbr.name";
  private static final String BGRB_FILTER_NAME       = "filter.demo.brg.name";
  private static final String GREY_FILTER_NAME       = "filter.demo.greys.name";
  private static final String REDONLY_FILTER_NAME    = "filter.demo.redonly.name";
  private static final String GREENONLY_FILTER_NAME  = "filter.demo.greenonly.name";
  private static final String BLUEONLY_FILTER_NAME   = "filter.demo.blueonly.name";
  private static final String REDLESS_FILTER_NAME    = "filter.demo.redless.name";
  private static final String GREENLESS_FILTER_NAME  = "filter.demo.greenless.name";
  private static final String BLUELESS_FILTER_NAME   = "filter.demo.blueless.name";
  private static final String INVERT_FILTER_NAME     = "filter.demo.invert.name";
  private static final String DEC_SAT_XL_FILTER_NAME = "filter.demo.saturation-xl.name";
  private static final String DEC_SAT_L_FILTER_NAME  = "filter.demo.saturation-l.name";
  private static final String DEC_SAT_M_FILTER_NAME  = "filter.demo.saturation-m.name";
  private static final String DEC_SAT_S_FILTER_NAME  = "filter.demo.saturation-s.name";
  private static final String DEC_SAT_XS_FILTER_NAME = "filter.demo.saturation-xs.name";
  private static final String INC_SAT_XL_FILTER_NAME = "filter.demo.saturation+xl.name";
  private static final String INC_SAT_L_FILTER_NAME  = "filter.demo.saturation+l.name";
  private static final String INC_SAT_M_FILTER_NAME  = "filter.demo.saturation+m.name";
  private static final String INC_SAT_S_FILTER_NAME  = "filter.demo.saturation+s.name";
  private static final String INC_SAT_XS_FILTER_NAME = "filter.demo.saturation+xs.name";
  private static final String DEC_HUE_XL_FILTER_NAME = "filter.demo.hue-xl.name";
  private static final String DEC_HUE_L_FILTER_NAME  = "filter.demo.hue-l.name";
  private static final String DEC_HUE_M_FILTER_NAME  = "filter.demo.hue-m.name";
  private static final String DEC_HUE_S_FILTER_NAME  = "filter.demo.hue-s.name";
  private static final String DEC_HUE_XS_FILTER_NAME = "filter.demo.hue-xs.name";
  private static final String INC_HUE_XL_FILTER_NAME = "filter.demo.hue+xl.name";
  private static final String INC_HUE_L_FILTER_NAME  = "filter.demo.hue+l.name";
  private static final String INC_HUE_M_FILTER_NAME  = "filter.demo.hue+m.name";
  private static final String INC_HUE_S_FILTER_NAME  = "filter.demo.hue+s.name";
  private static final String INC_HUE_XS_FILTER_NAME = "filter.demo.hue+xs.name";

//==============================================================================

  @Override
  public Collection<NamedFilter> getAllAvailableFilters()
  {
    Collection<NamedFilter> filters = new LinkedList<>();

    filters.add(new NamedFilter(null,                         NO_FILTER_NAME));
    filters.add(new NamedFilter(new RGBInvertFilter(),        INVERT_FILTER_NAME));
    filters.add(new NamedFilter(new BrighterFilter(),         LIGHTER_FILTER_NAME));
    filters.add(new NamedFilter(new DarkerFilter(),           DARKER_FILTER_NAME));
    filters.add(new NamedFilter(new RtoGtoBtoRFilter(),       RGBR_FILTER_NAME));
    filters.add(new NamedFilter(new BtoGtoRtoBFilter(),       BGRB_FILTER_NAME));
    filters.add(new NamedFilter(new GreyScaleFilter(),        GREY_FILTER_NAME));
    filters.add(new NamedFilter(new RedChannelOnlyFilter(),   REDONLY_FILTER_NAME));
    filters.add(new NamedFilter(new GreenChannelOnlyFilter(), GREENONLY_FILTER_NAME));
    filters.add(new NamedFilter(new BlueChannelOnlyFilter(),  BLUEONLY_FILTER_NAME));
    filters.add(new NamedFilter(new RedlessFilter(),          REDLESS_FILTER_NAME));
    filters.add(new NamedFilter(new GreenlessFilter(),        GREENLESS_FILTER_NAME));
    filters.add(new NamedFilter(new BluelessFilter(),         BLUELESS_FILTER_NAME));
    filters.add(new NamedFilter(new SaturationFilter(-2),     DEC_SAT_XL_FILTER_NAME));
    filters.add(new NamedFilter(new SaturationFilter(-1),     DEC_SAT_L_FILTER_NAME));
    filters.add(new NamedFilter(new SaturationFilter(-0.6),   DEC_SAT_M_FILTER_NAME));
    filters.add(new NamedFilter(new SaturationFilter(-0.3),   DEC_SAT_S_FILTER_NAME));
    filters.add(new NamedFilter(new SaturationFilter(-0.1),   DEC_SAT_XS_FILTER_NAME));
    filters.add(new NamedFilter(new SaturationFilter(0.1),    INC_SAT_XS_FILTER_NAME));
    filters.add(new NamedFilter(new SaturationFilter(0.3),    INC_SAT_S_FILTER_NAME));
    filters.add(new NamedFilter(new SaturationFilter(0.6),    INC_SAT_M_FILTER_NAME));
    filters.add(new NamedFilter(new SaturationFilter(1),      INC_SAT_L_FILTER_NAME));
    filters.add(new NamedFilter(new SaturationFilter(2),      INC_SAT_XL_FILTER_NAME));
    filters.add(new NamedFilter(new HueShiftFilter(-0.1f),    DEC_HUE_XL_FILTER_NAME));
    filters.add(new NamedFilter(new HueShiftFilter(-0.05f),   DEC_HUE_L_FILTER_NAME));
    filters.add(new NamedFilter(new HueShiftFilter(-0.03f),   DEC_HUE_M_FILTER_NAME));
    filters.add(new NamedFilter(new HueShiftFilter(-0.02f),   DEC_HUE_S_FILTER_NAME));
    filters.add(new NamedFilter(new HueShiftFilter(-0.01f),   DEC_HUE_XS_FILTER_NAME));
    filters.add(new NamedFilter(new HueShiftFilter(0.01f),    INC_HUE_XS_FILTER_NAME));
    filters.add(new NamedFilter(new HueShiftFilter(0.02f),    INC_HUE_S_FILTER_NAME));
    filters.add(new NamedFilter(new HueShiftFilter(0.03f),    INC_HUE_M_FILTER_NAME));
    filters.add(new NamedFilter(new HueShiftFilter(0.05f),    INC_HUE_L_FILTER_NAME));
    filters.add(new NamedFilter(new HueShiftFilter(0.1f),     INC_HUE_XL_FILTER_NAME));

    return filters;
  }

//------------------------------------------------------------------------------

}
