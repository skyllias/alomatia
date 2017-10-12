
package org.skyllias.alomatia.filter;

import java.util.*;

import org.skyllias.alomatia.filter.convolve.*;
import org.skyllias.alomatia.filter.daltonism.*;
import org.skyllias.alomatia.filter.demo.*;
import org.skyllias.alomatia.filter.hsb.*;

/** FilterFactory with a hardcoded set of available filters. */

public class FixedFilterFactory implements FilterFactory
{
  private static final String NO_FILTER_NAME         = "filter.selector.none";
  private static final String LMSPROPIA_FILTER_NAME  = "filter.dalton.lms.protanopia.name";
  private static final String LMSDEUPIA_FILTER_NAME  = "filter.dalton.lms.deuteranopia.name";
  private static final String LMSTRIPIA_FILTER_NAME  = "filter.dalton.lms.tritanopia.name";
  private static final String XYZPROPIA_FILTER_NAME  = "filter.dalton.xyz.protanopia.name";
  private static final String XYZPROMALY_FILTER_NAME = "filter.dalton.xyz.protanomaly.name";
  private static final String XYZDEUPIA_FILTER_NAME  = "filter.dalton.xyz.deuteranopia.name";
  private static final String XYZDEUMALY_FILTER_NAME = "filter.dalton.xyz.deuteranomaly.name";
  private static final String XYZTRIPIA_FILTER_NAME  = "filter.dalton.xyz.tritanopia.name";
  private static final String XYZTRIMALY_FILTER_NAME = "filter.dalton.xyz.tritanomaly.name";
  private static final String XYZACHSIA_FILTER_NAME  = "filter.dalton.xyz.achromatopsia.name";
  private static final String XYZACHMALY_FILTER_NAME = "filter.dalton.xyz.achromatomaly.name";
  private static final String RGBR_FILTER_NAME       = "filter.demo.gbr.name";
  private static final String BGRB_FILTER_NAME       = "filter.demo.brg.name";
  private static final String GREY_FILTER_NAME       = "filter.demo.greys.name";
  private static final String REDONLY_FILTER_NAME    = "filter.demo.redonly.name";
  private static final String GREENONLY_FILTER_NAME  = "filter.demo.greenonly.name";
  private static final String BLUEONLY_FILTER_NAME   = "filter.demo.blueonly.name";
  private static final String REDLESS_FILTER_NAME    = "filter.demo.redless.name";
  private static final String GREENLESS_FILTER_NAME  = "filter.demo.greenless.name";
  private static final String BLUELESS_FILTER_NAME   = "filter.demo.blueless.name";
  private static final String NEGATIVE_FILTER_NAME   = "filter.demo.invert.name";
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
  private static final String DEC_BRI_XL_FILTER_NAME = "filter.demo.brightness-xl.name";
  private static final String DEC_BRI_L_FILTER_NAME  = "filter.demo.brightness-l.name";
  private static final String DEC_BRI_M_FILTER_NAME  = "filter.demo.brightness-m.name";
  private static final String DEC_BRI_S_FILTER_NAME  = "filter.demo.brightness-s.name";
  private static final String DEC_BRI_XS_FILTER_NAME = "filter.demo.brightness-xs.name";
  private static final String INC_BRI_XL_FILTER_NAME = "filter.demo.brightness+xl.name";
  private static final String INC_BRI_L_FILTER_NAME  = "filter.demo.brightness+l.name";
  private static final String INC_BRI_M_FILTER_NAME  = "filter.demo.brightness+m.name";
  private static final String INC_BRI_S_FILTER_NAME  = "filter.demo.brightness+s.name";
  private static final String INC_BRI_XS_FILTER_NAME = "filter.demo.brightness+xs.name";
  private static final String BLUR_SMALL_FILTER_NAME = "filter.demo.blur.small.name";
  private static final String BLUR_BIG_FILTER_NAME   = "filter.demo.blur.big.name";
  private static final String SHARPEN_FILTER_NAME    = "filter.demo.sharpen.name";

//==============================================================================

  @Override
  public Collection<NamedFilter> getAllAvailableFilters()
  {
    Collection<NamedFilter> filters = new LinkedList<>();

    filters.add(new NamedFilter(null, NO_FILTER_NAME));

    filters.add(new NamedFilter(new XyzProtanopiaFilter(),    XYZPROPIA_FILTER_NAME));
    filters.add(new NamedFilter(new XyzProtanomalyFilter(),   XYZPROMALY_FILTER_NAME));
    filters.add(new NamedFilter(new XyzDeuteranopiaFilter(),  XYZDEUPIA_FILTER_NAME));
    filters.add(new NamedFilter(new XyzDeuteranomalyFilter(), XYZDEUMALY_FILTER_NAME));
    filters.add(new NamedFilter(new XyzTritanopiaFilter(),    XYZTRIPIA_FILTER_NAME));
    filters.add(new NamedFilter(new XyzTritanomalyFilter(),   XYZTRIMALY_FILTER_NAME));
    filters.add(new NamedFilter(new XyzAchromatopsiaFilter(), XYZACHSIA_FILTER_NAME));
    filters.add(new NamedFilter(new XyzAchromatomalyFilter(), XYZACHMALY_FILTER_NAME));

    filters.add(new NamedFilter(new LmsProtanopiaFilter(),   LMSPROPIA_FILTER_NAME));
    filters.add(new NamedFilter(new LmsDeuteranopiaFilter(), LMSDEUPIA_FILTER_NAME));
    filters.add(new NamedFilter(new LmsTritanopiaFilter(),   LMSTRIPIA_FILTER_NAME));


    filters.add(new NamedFilter(new NegativeFilter(),         NEGATIVE_FILTER_NAME));

    filters.add(new NamedFilter(new GreyScaleFilter(),        GREY_FILTER_NAME));

    filters.add(new NamedFilter(new RtoGtoBtoRFilter(),       RGBR_FILTER_NAME));
    filters.add(new NamedFilter(new BtoGtoRtoBFilter(),       BGRB_FILTER_NAME));
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

    filters.add(new NamedFilter(new BrightnessFilter(-1.5),   DEC_BRI_XL_FILTER_NAME));
    filters.add(new NamedFilter(new BrightnessFilter(-0.8),   DEC_BRI_L_FILTER_NAME));
    filters.add(new NamedFilter(new BrightnessFilter(-0.4),   DEC_BRI_M_FILTER_NAME));
    filters.add(new NamedFilter(new BrightnessFilter(-0.1),   DEC_BRI_S_FILTER_NAME));
    filters.add(new NamedFilter(new BrightnessFilter(-0.05),  DEC_BRI_XS_FILTER_NAME));
    filters.add(new NamedFilter(new BrightnessFilter(0.05),   INC_BRI_XS_FILTER_NAME));
    filters.add(new NamedFilter(new BrightnessFilter(0.1),    INC_BRI_S_FILTER_NAME));
    filters.add(new NamedFilter(new BrightnessFilter(0.4),    INC_BRI_M_FILTER_NAME));
    filters.add(new NamedFilter(new BrightnessFilter(0.8),    INC_BRI_L_FILTER_NAME));
    filters.add(new NamedFilter(new BrightnessFilter(1.5),    INC_BRI_XL_FILTER_NAME));

    filters.add(new NamedFilter(new ConvolutingFilter(new ParaboloidBlurKernelDataFactory(5)),  BLUR_SMALL_FILTER_NAME));
    filters.add(new NamedFilter(new ConvolutingFilter(new ParaboloidBlurKernelDataFactory(11)), BLUR_BIG_FILTER_NAME));
    filters.add(new NamedFilter(new ConvolutingFilter(new NeighbourSharpKernelDataFactory()),   SHARPEN_FILTER_NAME));

    return filters;
  }

//------------------------------------------------------------------------------

}
