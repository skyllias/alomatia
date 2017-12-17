
package org.skyllias.alomatia.filter;

import java.awt.*;
import java.util.*;

import org.skyllias.alomatia.filter.affine.*;
import org.skyllias.alomatia.filter.buffered.*;
import org.skyllias.alomatia.filter.buffered.vignette.*;
import org.skyllias.alomatia.filter.compose.*;
import org.skyllias.alomatia.filter.convolve.*;
import org.skyllias.alomatia.filter.daltonism.*;
import org.skyllias.alomatia.filter.hsb.*;
import org.skyllias.alomatia.filter.rgb.*;
import org.skyllias.alomatia.filter.rgb.lookup.*;

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
  private static final String RGBR_FILTER_NAME       = "filter.rgb.gbr.name";
  private static final String BGRB_FILTER_NAME       = "filter.rgb.brg.name";
  private static final String GREY_FILTER_NAME       = "filter.rgb.greys.name";
  private static final String REDONLY_FILTER_NAME    = "filter.rgb.redonly.name";
  private static final String GREENONLY_FILTER_NAME  = "filter.rgb.greenonly.name";
  private static final String BLUEONLY_FILTER_NAME   = "filter.rgb.blueonly.name";
  private static final String REDLESS_FILTER_NAME    = "filter.rgb.redless.name";
  private static final String GREENLESS_FILTER_NAME  = "filter.rgb.greenless.name";
  private static final String BLUELESS_FILTER_NAME   = "filter.rgb.blueless.name";
  private static final String NEGATIVE_FILTER_NAME   = "filter.rgb.invert.name";
  private static final String DEC_SAT_XL_FILTER_NAME = "filter.hsb.saturation-xl.name";
  private static final String DEC_SAT_L_FILTER_NAME  = "filter.hsb.saturation-l.name";
  private static final String DEC_SAT_M_FILTER_NAME  = "filter.hsb.saturation-m.name";
  private static final String DEC_SAT_S_FILTER_NAME  = "filter.hsb.saturation-s.name";
  private static final String DEC_SAT_XS_FILTER_NAME = "filter.hsb.saturation-xs.name";
  private static final String INC_SAT_XL_FILTER_NAME = "filter.hsb.saturation+xl.name";
  private static final String INC_SAT_L_FILTER_NAME  = "filter.hsb.saturation+l.name";
  private static final String INC_SAT_M_FILTER_NAME  = "filter.hsb.saturation+m.name";
  private static final String INC_SAT_S_FILTER_NAME  = "filter.hsb.saturation+s.name";
  private static final String INC_SAT_XS_FILTER_NAME = "filter.hsb.saturation+xs.name";
  private static final String DEC_HUE_XL_FILTER_NAME = "filter.hsb.hue-xl.name";
  private static final String DEC_HUE_L_FILTER_NAME  = "filter.hsb.hue-l.name";
  private static final String DEC_HUE_M_FILTER_NAME  = "filter.hsb.hue-m.name";
  private static final String DEC_HUE_S_FILTER_NAME  = "filter.hsb.hue-s.name";
  private static final String DEC_HUE_XS_FILTER_NAME = "filter.hsb.hue-xs.name";
  private static final String INC_HUE_XL_FILTER_NAME = "filter.hsb.hue+xl.name";
  private static final String INC_HUE_L_FILTER_NAME  = "filter.hsb.hue+l.name";
  private static final String INC_HUE_M_FILTER_NAME  = "filter.hsb.hue+m.name";
  private static final String INC_HUE_S_FILTER_NAME  = "filter.hsb.hue+s.name";
  private static final String INC_HUE_XS_FILTER_NAME = "filter.hsb.hue+xs.name";
  private static final String DEC_BRI_XL_FILTER_NAME = "filter.hsb.brightness-xl.name";
  private static final String DEC_BRI_L_FILTER_NAME  = "filter.hsb.brightness-l.name";
  private static final String DEC_BRI_M_FILTER_NAME  = "filter.hsb.brightness-m.name";
  private static final String DEC_BRI_S_FILTER_NAME  = "filter.hsb.brightness-s.name";
  private static final String DEC_BRI_XS_FILTER_NAME = "filter.hsb.brightness-xs.name";
  private static final String INC_BRI_XL_FILTER_NAME = "filter.hsb.brightness+xl.name";
  private static final String INC_BRI_L_FILTER_NAME  = "filter.hsb.brightness+l.name";
  private static final String INC_BRI_M_FILTER_NAME  = "filter.hsb.brightness+m.name";
  private static final String INC_BRI_S_FILTER_NAME  = "filter.hsb.brightness+s.name";
  private static final String INC_BRI_XS_FILTER_NAME = "filter.hsb.brightness+xs.name";
  private static final String DEC_CTR_XL_FILTER_NAME = "filter.hsb.lightcontrast-xl.name";
  private static final String DEC_CTR_L_FILTER_NAME  = "filter.hsb.lightcontrast-l.name";
  private static final String DEC_CTR_M_FILTER_NAME  = "filter.hsb.lightcontrast-m.name";
  private static final String DEC_CTR_S_FILTER_NAME  = "filter.hsb.lightcontrast-s.name";
  private static final String DEC_CTR_XS_FILTER_NAME = "filter.hsb.lightcontrast-xs.name";
  private static final String INC_CTR_XL_FILTER_NAME = "filter.hsb.lightcontrast+xl.name";
  private static final String INC_CTR_L_FILTER_NAME  = "filter.hsb.lightcontrast+l.name";
  private static final String INC_CTR_M_FILTER_NAME  = "filter.hsb.lightcontrast+m.name";
  private static final String INC_CTR_S_FILTER_NAME  = "filter.hsb.lightcontrast+s.name";
  private static final String INC_CTR_XS_FILTER_NAME = "filter.hsb.lightcontrast+xs.name";
  private static final String INC_RED_BR_FILTER_NAME = "filter.hsb.red.brightness.up.name";
  private static final String DEC_RED_BR_FILTER_NAME = "filter.hsb.red.brightness.down.name";
  private static final String INC_GRN_BR_FILTER_NAME = "filter.hsb.green.brightness.up.name";
  private static final String DEC_GRN_BR_FILTER_NAME = "filter.hsb.green.brightness.down.name";
  private static final String INC_BLU_BR_FILTER_NAME = "filter.hsb.blue.brightness.up.name";
  private static final String DEC_BLU_BR_FILTER_NAME = "filter.hsb.blue.brightness.down.name";
  private static final String INC_RED_SA_FILTER_NAME = "filter.hsb.red.saturation.up.name";
  private static final String DEC_RED_SA_FILTER_NAME = "filter.hsb.red.saturation.down.name";
  private static final String INC_GRN_SA_FILTER_NAME = "filter.hsb.green.saturation.up.name";
  private static final String DEC_GRN_SA_FILTER_NAME = "filter.hsb.green.saturation.down.name";
  private static final String INC_BLU_SA_FILTER_NAME = "filter.hsb.blue.saturation.up.name";
  private static final String DEC_BLU_SA_FILTER_NAME = "filter.hsb.blue.saturation.down.name";
  private static final String DEC_CCT_XL_FILTER_NAME = "filter.rgb.colourcontrast-xl.name";
  private static final String DEC_CCT_L_FILTER_NAME  = "filter.rgb.colourcontrast-l.name";
  private static final String DEC_CCT_M_FILTER_NAME  = "filter.rgb.colourcontrast-m.name";
  private static final String DEC_CCT_S_FILTER_NAME  = "filter.rgb.colourcontrast-s.name";
  private static final String DEC_CCT_XS_FILTER_NAME = "filter.rgb.colourcontrast-xs.name";
  private static final String INC_CCT_XL_FILTER_NAME = "filter.rgb.colourcontrast+xl.name";
  private static final String INC_CCT_L_FILTER_NAME  = "filter.rgb.colourcontrast+l.name";
  private static final String INC_CCT_M_FILTER_NAME  = "filter.rgb.colourcontrast+m.name";
  private static final String INC_CCT_S_FILTER_NAME  = "filter.rgb.colourcontrast+s.name";
  private static final String INC_CCT_XS_FILTER_NAME = "filter.rgb.colourcontrast+xs.name";
  private static final String BLUR_SMALL_FILTER_NAME = "filter.blur.small.name";
  private static final String BLUR_BIG_FILTER_NAME   = "filter.blur.big.name";
  private static final String MOTION_S0_FILTER_NAME  = "filter.blur.motion.slow.horizontal.name";
  private static final String MOTION_S90_FILTER_NAME = "filter.blur.motion.slow.vertical.name";
  private static final String MOTION_S45_FILTER_NAME = "filter.blur.motion.slow.oblique.name";
  private static final String MOTION_L0_FILTER_NAME  = "filter.blur.motion.fast.horizontal.name";
  private static final String MOTION_L90_FILTER_NAME = "filter.blur.motion.fast.vertical.name";
  private static final String MOTION_L45_FILTER_NAME = "filter.blur.motion.fast.oblique.name";
  private static final String SHARPEN_FILTER_NAME    = "filter.blur.sharpen.name";
  private static final String EDGEDETECT_FILTER_NAME = "filter.convolve.edgedetection.name";
  private static final String LAYEMBOSS_FILTER_NAME  = "filter.convolve.emboss.layered.name";
  private static final String SMTHEMBOSS_FILTER_NAME = "filter.convolve.emboss.smooth.name";
  private static final String MEDIAN_XS_FILTER_NAME  = "filter.median.xs.name";
  private static final String MEDIAN_M_FILTER_NAME   = "filter.median.m.name";
  private static final String ORANGEPHIL_FILTER_NAME = "filter.phile.orange.name";
  private static final String GREENPHIL_FILTER_NAME  = "filter.phile.green.name";
  private static final String PURPLEPHIL_FILTER_NAME = "filter.phile.purple.name";
  private static final String REDPHOB_FILTER_NAME    = "filter.phobic.red.name";
  private static final String GREENPHOB_FILTER_NAME  = "filter.phobic.green.name";
  private static final String BLUEPHOB_FILTER_NAME   = "filter.phobic.blue.name";
  private static final String YELLOWPHOB_FILTER_NAME = "filter.phobic.yellow.name";
  private static final String CYANPHOB_FILTER_NAME   = "filter.phobic.cyan.name";
  private static final String MAGENTPHOB_FILTER_NAME = "filter.phobic.magenta.name";
  private static final String WHITEDYE_FILTER_NAME   = "filter.dye.white.name";
  private static final String BLACKDYE_FILTER_NAME   = "filter.dye.black.name";
  private static final String REDDYE_FILTER_NAME     = "filter.dye.red.name";
  private static final String GREENDYE_FILTER_NAME   = "filter.dye.green.name";
  private static final String BLUEDYE_FILTER_NAME    = "filter.dye.blue.name";
  private static final String YELLOWDYE_FILTER_NAME  = "filter.dye.yellow.name";
  private static final String CYANDYE_FILTER_NAME    = "filter.dye.cyan.name";
  private static final String MAGENTADYE_FILTER_NAME = "filter.dye.magenta.name";
  private static final String HORIZONTAL_FILTER_NAME = "filter.affine.horizontal.name";
  private static final String VERTICAL_FILTER_NAME   = "filter.affine.vertical.name";
  private static final String ROTATION_FILTER_NAME   = "filter.affine.rotation.name";
  private static final String POSTER_XL_FILTER_NAME  = "filter.rgb.posterize.xl.name";
  private static final String POSTER_L_FILTER_NAME   = "filter.rgb.posterize.l.name";
  private static final String POSTER_M_FILTER_NAME   = "filter.rgb.posterize.m.name";
  private static final String POSTER_S_FILTER_NAME   = "filter.rgb.posterize.s.name";
  private static final String POSTER_XS_FILTER_NAME  = "filter.rgb.posterize.xs.name";
  private static final String BPOSTER_XL_FILTER_NAME = "filter.hsb.posterize.brightness.xl.name";
  private static final String BPOSTER_L_FILTER_NAME  = "filter.hsb.posterize.brightness.l.name";
  private static final String BPOSTER_M_FILTER_NAME  = "filter.hsb.posterize.brightness.m.name";
  private static final String BPOSTER_S_FILTER_NAME  = "filter.hsb.posterize.brightness.s.name";
  private static final String BPOSTER_XS_FILTER_NAME = "filter.hsb.posterize.brightness.xs.name";
  private static final String SATPOSTER_FILTER_NAME  = "filter.hsb.posterize.saturation.name";
  private static final String HPOSTER_L0_FILTER_NAME = "filter.hsb.posterize.hue.l0.name";
  private static final String HPOSTER_L1_FILTER_NAME = "filter.hsb.posterize.hue.l1.name";
  private static final String HPOSTER_M0_FILTER_NAME = "filter.hsb.posterize.hue.m0.name";
  private static final String HPOSTER_M1_FILTER_NAME = "filter.hsb.posterize.hue.m1.name";
  private static final String HPOSTER_S0_FILTER_NAME = "filter.hsb.posterize.hue.s0.name";
  private static final String HPOSTER_S1_FILTER_NAME = "filter.hsb.posterize.hue.s1.name";
  private static final String PIXEL_XS_FILTER_NAME   = "filter.pixelize.xs.name";
  private static final String PIXEL_S_FILTER_NAME    = "filter.pixelize.s.name";
  private static final String PIXEL_M_FILTER_NAME    = "filter.pixelize.m.name";
  private static final String PIXEL_L_FILTER_NAME    = "filter.pixelize.l.name";
  private static final String PIXEL_XL_FILTER_NAME   = "filter.pixelize.xl.name";
  private static final String HIGHSRED_FILTER_NAME   = "filter.highlight.saturation.red.name";
  private static final String HIGHSYELOW_FILTER_NAME = "filter.highlight.saturation.yellow.name";
  private static final String HIGHSGREEN_FILTER_NAME = "filter.highlight.saturation.green.name";
  private static final String HIGHSBLUE_FILTER_NAME  = "filter.highlight.saturation.blue.name";
  private static final String HIGHSPURPL_FILTER_NAME = "filter.highlight.saturation.purple.name";
  private static final String HIGHBRED_FILTER_NAME   = "filter.highlight.brightness.red.name";
  private static final String HIGHBYELOW_FILTER_NAME = "filter.highlight.brightness.yellow.name";
  private static final String HIGHBGREEN_FILTER_NAME = "filter.highlight.brightness.green.name";
  private static final String HIGHBBLUE_FILTER_NAME  = "filter.highlight.brightness.blue.name";
  private static final String HIGHBPURPL_FILTER_NAME = "filter.highlight.brightness.purple.name";
  private static final String VIGNETTE_R_FILTER_NAME = "filter.vignette.round.name";
  private static final String VIGNETTE_C_FILTER_NAME = "filter.vignette.cross.name";
  private static final String VIGNETTE_E_FILTER_NAME = "filter.vignette.edges.name";
  private static final String INST_ALOPO_FILTER_NAME = "Allopo";                // these are proper names and needn't i18n
  private static final String INST_EARBY_FILTER_NAME = "Earby lird";
  private static final String INST_TOGAM_FILTER_NAME = "Thogam";
  private static final String INST_VASHN_FILTER_NAME = "Vashnille";
  private static final String INST_FEHE_FILTER_NAME  = "Fehe";
  private static final String INST_MOLO_FILTER_NAME  = "Molo";
  private static final String INST_KEVLN_FILTER_NAME = "Kevlin";
  private static final String INST_1976_FILTER_NAME  = "1976";
  private static final String INST_SOATR_FILTER_NAME = "Soater";
  private static final String INST_LAWDN_FILTER_NAME = "Lawden";
  private static final String INST_PROX_FILTER_NAME  = "Prox";

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

    filters.add(new NamedFilter(ChannelLookupFilterFactory.forThogam(),    INST_TOGAM_FILTER_NAME));
    filters.add(new NamedFilter(ChannelLookupFilterFactory.forProx(),      INST_PROX_FILTER_NAME));
    filters.add(new NamedFilter(ChannelLookupFilterFactory.forLawden(),    INST_LAWDN_FILTER_NAME));
    filters.add(new NamedFilter(ChannelLookupFilterFactory.forFehe(),      INST_FEHE_FILTER_NAME));
    filters.add(new NamedFilter(ChannelLookupFilterFactory.forMolo(),      INST_MOLO_FILTER_NAME));
    filters.add(new NamedFilter(ChannelLookupFilterFactory.forAllopo(),    INST_ALOPO_FILTER_NAME));
    filters.add(new NamedFilter(ChannelLookupFilterFactory.forEarbyLird(), INST_EARBY_FILTER_NAME));
    filters.add(new NamedFilter(ChannelLookupFilterFactory.forSoater(),    INST_SOATR_FILTER_NAME));
    filters.add(new NamedFilter(ChannelLookupFilterFactory.forVashnille(), INST_VASHN_FILTER_NAME));
    filters.add(new NamedFilter(ChannelLookupFilterFactory.for1976(),      INST_1976_FILTER_NAME));
    filters.add(new NamedFilter(ChannelLookupFilterFactory.forKevlin(),    INST_KEVLN_FILTER_NAME));

    filters.add(new NamedFilter(VignetteFilterFactory.forRound(), VIGNETTE_R_FILTER_NAME));
    filters.add(new NamedFilter(VignetteFilterFactory.forCross(), VIGNETTE_C_FILTER_NAME));
    filters.add(new NamedFilter(VignetteFilterFactory.forEdges(), VIGNETTE_E_FILTER_NAME));

    filters.add(new NamedFilter(new HueShiftFilter(-0.1f),  DEC_HUE_XL_FILTER_NAME));
    filters.add(new NamedFilter(new HueShiftFilter(-0.05f), DEC_HUE_L_FILTER_NAME));
    filters.add(new NamedFilter(new HueShiftFilter(-0.03f), DEC_HUE_M_FILTER_NAME));
    filters.add(new NamedFilter(new HueShiftFilter(-0.02f), DEC_HUE_S_FILTER_NAME));
    filters.add(new NamedFilter(new HueShiftFilter(-0.01f), DEC_HUE_XS_FILTER_NAME));
    filters.add(new NamedFilter(new HueShiftFilter(0.01f),  INC_HUE_XS_FILTER_NAME));
    filters.add(new NamedFilter(new HueShiftFilter(0.02f),  INC_HUE_S_FILTER_NAME));
    filters.add(new NamedFilter(new HueShiftFilter(0.03f),  INC_HUE_M_FILTER_NAME));
    filters.add(new NamedFilter(new HueShiftFilter(0.05f),  INC_HUE_L_FILTER_NAME));
    filters.add(new NamedFilter(new HueShiftFilter(0.1f),   INC_HUE_XL_FILTER_NAME));

    filters.add(new NamedFilter(new SaturationFilter(-2),   DEC_SAT_XL_FILTER_NAME));
    filters.add(new NamedFilter(new SaturationFilter(-1),   DEC_SAT_L_FILTER_NAME));
    filters.add(new NamedFilter(new SaturationFilter(-0.6), DEC_SAT_M_FILTER_NAME));
    filters.add(new NamedFilter(new SaturationFilter(-0.3), DEC_SAT_S_FILTER_NAME));
    filters.add(new NamedFilter(new SaturationFilter(-0.1), DEC_SAT_XS_FILTER_NAME));
    filters.add(new NamedFilter(new SaturationFilter(0.1),  INC_SAT_XS_FILTER_NAME));
    filters.add(new NamedFilter(new SaturationFilter(0.3),  INC_SAT_S_FILTER_NAME));
    filters.add(new NamedFilter(new SaturationFilter(0.6),  INC_SAT_M_FILTER_NAME));
    filters.add(new NamedFilter(new SaturationFilter(1),    INC_SAT_L_FILTER_NAME));
    filters.add(new NamedFilter(new SaturationFilter(2),    INC_SAT_XL_FILTER_NAME));

    filters.add(new NamedFilter(new BrightnessFilter(-1.5),  DEC_BRI_XL_FILTER_NAME));
    filters.add(new NamedFilter(new BrightnessFilter(-0.8),  DEC_BRI_L_FILTER_NAME));
    filters.add(new NamedFilter(new BrightnessFilter(-0.4),  DEC_BRI_M_FILTER_NAME));
    filters.add(new NamedFilter(new BrightnessFilter(-0.1),  DEC_BRI_S_FILTER_NAME));
    filters.add(new NamedFilter(new BrightnessFilter(-0.05), DEC_BRI_XS_FILTER_NAME));
    filters.add(new NamedFilter(new BrightnessFilter(0.05),  INC_BRI_XS_FILTER_NAME));
    filters.add(new NamedFilter(new BrightnessFilter(0.1),   INC_BRI_S_FILTER_NAME));
    filters.add(new NamedFilter(new BrightnessFilter(0.4),   INC_BRI_M_FILTER_NAME));
    filters.add(new NamedFilter(new BrightnessFilter(0.8),   INC_BRI_L_FILTER_NAME));
    filters.add(new NamedFilter(new BrightnessFilter(1.5),   INC_BRI_XL_FILTER_NAME));

    filters.add(new NamedFilter(new ContrastFilter(-2),   DEC_CTR_XL_FILTER_NAME));
    filters.add(new NamedFilter(new ContrastFilter(-1),   DEC_CTR_L_FILTER_NAME));
    filters.add(new NamedFilter(new ContrastFilter(-0.6), DEC_CTR_M_FILTER_NAME));
    filters.add(new NamedFilter(new ContrastFilter(-0.3), DEC_CTR_S_FILTER_NAME));
    filters.add(new NamedFilter(new ContrastFilter(-0.1), DEC_CTR_XS_FILTER_NAME));
    filters.add(new NamedFilter(new ContrastFilter(0.1),  INC_CTR_XS_FILTER_NAME));
    filters.add(new NamedFilter(new ContrastFilter(0.3),  INC_CTR_S_FILTER_NAME));
    filters.add(new NamedFilter(new ContrastFilter(0.6),  INC_CTR_M_FILTER_NAME));
    filters.add(new NamedFilter(new ContrastFilter(1),    INC_CTR_L_FILTER_NAME));
    filters.add(new NamedFilter(new ContrastFilter(2),    INC_CTR_XL_FILTER_NAME));

    filters.add(new NamedFilter(new ColourContrastFilter(-2),   DEC_CCT_XL_FILTER_NAME));
    filters.add(new NamedFilter(new ColourContrastFilter(-1),   DEC_CCT_L_FILTER_NAME));
    filters.add(new NamedFilter(new ColourContrastFilter(-0.6), DEC_CCT_M_FILTER_NAME));
    filters.add(new NamedFilter(new ColourContrastFilter(-0.3), DEC_CCT_S_FILTER_NAME));
    filters.add(new NamedFilter(new ColourContrastFilter(-0.1), DEC_CCT_XS_FILTER_NAME));
    filters.add(new NamedFilter(new ColourContrastFilter(0.1),  INC_CCT_XS_FILTER_NAME));
    filters.add(new NamedFilter(new ColourContrastFilter(0.3),  INC_CCT_S_FILTER_NAME));
    filters.add(new NamedFilter(new ColourContrastFilter(0.6),  INC_CCT_M_FILTER_NAME));
    filters.add(new NamedFilter(new ColourContrastFilter(1),    INC_CCT_L_FILTER_NAME));
    filters.add(new NamedFilter(new ColourContrastFilter(2),    INC_CCT_XL_FILTER_NAME));

    filters.add(new NamedFilter(new SingleFrameBufferedImageFilter(new SurroundingColoursOp(1, new MedianChannelCalculator())), MEDIAN_XS_FILTER_NAME));
    filters.add(new NamedFilter(new SingleFrameBufferedImageFilter(new SurroundingColoursOp(5, new MedianChannelCalculator())), MEDIAN_M_FILTER_NAME));

    filters.add(new NamedFilter(new EdgeConvolvingComposedFilter(new ParaboloidBlurKernelDataFactory(5)),                                        BLUR_SMALL_FILTER_NAME));
    filters.add(new NamedFilter(new EdgeConvolvingComposedFilter(new ParaboloidBlurKernelDataFactory(11)),                                       BLUR_BIG_FILTER_NAME));
    filters.add(new NamedFilter(new EdgeConvolvingComposedFilter(new LinearBlurKernelDataFactory(20, 0)),                                        MOTION_S0_FILTER_NAME));
    filters.add(new NamedFilter(new EdgeConvolvingComposedFilter(new LinearBlurKernelDataFactory(20, -Math.PI / 4)),                             MOTION_S45_FILTER_NAME));
    filters.add(new NamedFilter(new EdgeConvolvingComposedFilter(new LinearBlurKernelDataFactory(20, Math.PI / 2)),                              MOTION_S90_FILTER_NAME));
    filters.add(new NamedFilter(new EdgeConvolvingComposedFilter(new LinearBlurKernelDataFactory(30, 0, new SquareBlurLineProfile())),           MOTION_L0_FILTER_NAME));
    filters.add(new NamedFilter(new EdgeConvolvingComposedFilter(new LinearBlurKernelDataFactory(30, Math.PI / 4, new SquareBlurLineProfile())), MOTION_L45_FILTER_NAME));
    filters.add(new NamedFilter(new EdgeConvolvingComposedFilter(new LinearBlurKernelDataFactory(30, Math.PI / 2, new SquareBlurLineProfile())), MOTION_L90_FILTER_NAME));
    filters.add(new NamedFilter(new EdgeConvolvingComposedFilter(new NeighbourSharpKernelDataFactory()),                                         SHARPEN_FILTER_NAME));

    filters.add(new NamedFilter(new EdgeConvolvingComposedFilter(new EdgeDetectingKernelDataFactory()), EDGEDETECT_FILTER_NAME));

    filters.add(new NamedFilter(EmbossFilter.forLayeredEmboss(), LAYEMBOSS_FILTER_NAME));
    filters.add(new NamedFilter(EmbossFilter.forSmoothEmboss(),  SMTHEMBOSS_FILTER_NAME));

    filters.add(new NamedFilter(new NegativeFilter(), NEGATIVE_FILTER_NAME));

    filters.add(new NamedFilter(new GreyScaleFilter(), GREY_FILTER_NAME));

    filters.add(new NamedFilter(new ClosestPoleHueFilter(new DistantAttraction(0.2f), new Color(255, 128, 0)), ORANGEPHIL_FILTER_NAME));
    filters.add(new NamedFilter(new ClosestPoleHueFilter(new DistantAttraction(0.2f), new Color(43, 255, 0)),  GREENPHIL_FILTER_NAME));
    filters.add(new NamedFilter(new ClosestPoleHueFilter(new DistantAttraction(0.2f), new Color(149, 0, 255)), PURPLEPHIL_FILTER_NAME));

    filters.add(new NamedFilter(new CombinedPoleHueFilter(new LinearRepulsion(0.12f, 0.22f), new Color(255, 0, 0)),   REDPHOB_FILTER_NAME));
    filters.add(new NamedFilter(new CombinedPoleHueFilter(new LinearRepulsion(0.12f, 0.22f), new Color(0, 255, 0)),   GREENPHOB_FILTER_NAME));
    filters.add(new NamedFilter(new CombinedPoleHueFilter(new LinearRepulsion(0.12f, 0.22f), new Color(0, 0, 255)),   BLUEPHOB_FILTER_NAME));
    filters.add(new NamedFilter(new CombinedPoleHueFilter(new LinearRepulsion(0.12f, 0.22f), new Color(255, 255, 0)), YELLOWPHOB_FILTER_NAME));
    filters.add(new NamedFilter(new CombinedPoleHueFilter(new LinearRepulsion(0.12f, 0.22f), new Color(0, 255, 255)), CYANPHOB_FILTER_NAME));
    filters.add(new NamedFilter(new CombinedPoleHueFilter(new LinearRepulsion(0.12f, 0.22f), new Color(255, 0, 255)), MAGENTPHOB_FILTER_NAME));

    filters.add(new NamedFilter(new RtoGtoBtoRFilter(),       RGBR_FILTER_NAME));
    filters.add(new NamedFilter(new BtoGtoRtoBFilter(),       BGRB_FILTER_NAME));
    filters.add(new NamedFilter(new RedChannelOnlyFilter(),   REDONLY_FILTER_NAME));
    filters.add(new NamedFilter(new GreenChannelOnlyFilter(), GREENONLY_FILTER_NAME));
    filters.add(new NamedFilter(new BlueChannelOnlyFilter(),  BLUEONLY_FILTER_NAME));
    filters.add(new NamedFilter(new RedlessFilter(),          REDLESS_FILTER_NAME));
    filters.add(new NamedFilter(new GreenlessFilter(),        GREENLESS_FILTER_NAME));
    filters.add(new NamedFilter(new BluelessFilter(),         BLUELESS_FILTER_NAME));

    filters.add(new NamedFilter(new SingleFrameBufferedImageFilter(new DyeOp(Color.WHITE, 0.2f)),   WHITEDYE_FILTER_NAME));
    filters.add(new NamedFilter(new SingleFrameBufferedImageFilter(new DyeOp(Color.BLACK, 0.2f)),   BLACKDYE_FILTER_NAME));
    filters.add(new NamedFilter(new SingleFrameBufferedImageFilter(new DyeOp(Color.RED, 0.2f)),     REDDYE_FILTER_NAME));
    filters.add(new NamedFilter(new SingleFrameBufferedImageFilter(new DyeOp(Color.GREEN, 0.2f)),   GREENDYE_FILTER_NAME));
    filters.add(new NamedFilter(new SingleFrameBufferedImageFilter(new DyeOp(Color.BLUE, 0.2f)),    BLUEDYE_FILTER_NAME));
    filters.add(new NamedFilter(new SingleFrameBufferedImageFilter(new DyeOp(Color.YELLOW, 0.2f)),  YELLOWDYE_FILTER_NAME));
    filters.add(new NamedFilter(new SingleFrameBufferedImageFilter(new DyeOp(Color.CYAN, 0.2f)),    CYANDYE_FILTER_NAME));
    filters.add(new NamedFilter(new SingleFrameBufferedImageFilter(new DyeOp(Color.MAGENTA, 0.2f)), MAGENTADYE_FILTER_NAME));

    filters.add(new NamedFilter(new HueDependingBrightnessFilter(new PositiveFilteringHueFunction(new CosineHueFunction(0, 1, -7))),                                    INC_RED_BR_FILTER_NAME));
    filters.add(new NamedFilter(new HueDependingBrightnessFilter(new MultiplyingHueFactor(new PositiveFilteringHueFunction(new CosineHueFunction(0, 1, -7)), -1)),      DEC_RED_BR_FILTER_NAME));
    filters.add(new NamedFilter(new HueDependingBrightnessFilter(new PositiveFilteringHueFunction(new CosineHueFunction(0.333f, 1, -7))),                               INC_GRN_BR_FILTER_NAME));
    filters.add(new NamedFilter(new HueDependingBrightnessFilter(new MultiplyingHueFactor(new PositiveFilteringHueFunction(new CosineHueFunction(0.333f, 1, -7)), -1)), DEC_GRN_BR_FILTER_NAME));
    filters.add(new NamedFilter(new HueDependingBrightnessFilter(new PositiveFilteringHueFunction(new CosineHueFunction(0.666f, 1, -7))),                               INC_BLU_BR_FILTER_NAME));
    filters.add(new NamedFilter(new HueDependingBrightnessFilter(new MultiplyingHueFactor(new PositiveFilteringHueFunction(new CosineHueFunction(0.666f, 1, -7)), -1)), DEC_BLU_BR_FILTER_NAME));

    filters.add(new NamedFilter(new HueDependingBrightnessFilter(new PitStepHueFunction(0.9f, 0.1f)), HIGHBRED_FILTER_NAME));
    filters.add(new NamedFilter(new HueDependingBrightnessFilter(new PitStepHueFunction(0.1f, 0.3f)), HIGHBYELOW_FILTER_NAME));
    filters.add(new NamedFilter(new HueDependingBrightnessFilter(new PitStepHueFunction(0.3f, 0.5f)), HIGHBGREEN_FILTER_NAME));
    filters.add(new NamedFilter(new HueDependingBrightnessFilter(new PitStepHueFunction(0.5f, 0.7f)), HIGHBBLUE_FILTER_NAME));
    filters.add(new NamedFilter(new HueDependingBrightnessFilter(new PitStepHueFunction(0.7f, 0.9f)), HIGHBPURPL_FILTER_NAME));

    filters.add(new NamedFilter(new HueDependingSaturationFactorFilter(new FlatStepHueFunction(0.5, 0.95f, 0.05f)),  INC_RED_SA_FILTER_NAME));
    filters.add(new NamedFilter(new HueDependingSaturationFactorFilter(new FlatStepHueFunction(-0.5, 0.9f, 0.1f)),   DEC_RED_SA_FILTER_NAME));
    filters.add(new NamedFilter(new HueDependingSaturationFactorFilter(new FlatStepHueFunction(0.5, 0.25f, 0.4f)),   INC_GRN_SA_FILTER_NAME));
    filters.add(new NamedFilter(new HueDependingSaturationFactorFilter(new FlatStepHueFunction(-0.5, 0.2f, 0.45f)),  DEC_GRN_SA_FILTER_NAME));
    filters.add(new NamedFilter(new HueDependingSaturationFactorFilter(new FlatStepHueFunction(0.5, 0.5f, 0.7f)),    INC_BLU_SA_FILTER_NAME));
    filters.add(new NamedFilter(new HueDependingSaturationFactorFilter(new FlatStepHueFunction(-0.5, 0.45f, 0.75f)), DEC_BLU_SA_FILTER_NAME));

    filters.add(new NamedFilter(new HueDependingSaturationFactorFilter(new PitStepHueFunction(0.9f, 0.1f)), HIGHSRED_FILTER_NAME));
    filters.add(new NamedFilter(new HueDependingSaturationFactorFilter(new PitStepHueFunction(0.1f, 0.3f)), HIGHSYELOW_FILTER_NAME));
    filters.add(new NamedFilter(new HueDependingSaturationFactorFilter(new PitStepHueFunction(0.3f, 0.5f)), HIGHSGREEN_FILTER_NAME));
    filters.add(new NamedFilter(new HueDependingSaturationFactorFilter(new PitStepHueFunction(0.5f, 0.7f)), HIGHSBLUE_FILTER_NAME));
    filters.add(new NamedFilter(new HueDependingSaturationFactorFilter(new PitStepHueFunction(0.7f, 0.9f)), HIGHSPURPL_FILTER_NAME));

    filters.add(new NamedFilter(new SingleFrameBufferedImageFilter(new PixelizerOp(3)),  PIXEL_XS_FILTER_NAME));
    filters.add(new NamedFilter(new SingleFrameBufferedImageFilter(new PixelizerOp(5)),  PIXEL_S_FILTER_NAME));
    filters.add(new NamedFilter(new SingleFrameBufferedImageFilter(new PixelizerOp(10)), PIXEL_M_FILTER_NAME));
    filters.add(new NamedFilter(new SingleFrameBufferedImageFilter(new PixelizerOp(20)), PIXEL_L_FILTER_NAME));
    filters.add(new NamedFilter(new SingleFrameBufferedImageFilter(new PixelizerOp(50)), PIXEL_XL_FILTER_NAME));

    filters.add(new NamedFilter(new RgbPosterizer(2),  POSTER_XL_FILTER_NAME));
    filters.add(new NamedFilter(new RgbPosterizer(3),  POSTER_L_FILTER_NAME));
    filters.add(new NamedFilter(new RgbPosterizer(6),  POSTER_M_FILTER_NAME));
    filters.add(new NamedFilter(new RgbPosterizer(12), POSTER_S_FILTER_NAME));
    filters.add(new NamedFilter(new RgbPosterizer(20), POSTER_XS_FILTER_NAME));

    filters.add(new NamedFilter(new BrightnessPosterizerFilter(2, false),  BPOSTER_XL_FILTER_NAME));
    filters.add(new NamedFilter(new BrightnessPosterizerFilter(3, false),  BPOSTER_L_FILTER_NAME));
    filters.add(new NamedFilter(new BrightnessPosterizerFilter(6, false),  BPOSTER_M_FILTER_NAME));
    filters.add(new NamedFilter(new BrightnessPosterizerFilter(10, false), BPOSTER_S_FILTER_NAME));
    filters.add(new NamedFilter(new BrightnessPosterizerFilter(20, false), BPOSTER_XS_FILTER_NAME));

    filters.add(new NamedFilter(new HuePosterizerFilter(3, 0),         HPOSTER_L0_FILTER_NAME));
    filters.add(new NamedFilter(new HuePosterizerFilter(3, 0.16667f),  HPOSTER_L1_FILTER_NAME));
    filters.add(new NamedFilter(new HuePosterizerFilter(6, 0),         HPOSTER_M0_FILTER_NAME));
    filters.add(new NamedFilter(new HuePosterizerFilter(6, 0.083333f), HPOSTER_M1_FILTER_NAME));
    filters.add(new NamedFilter(new HuePosterizerFilter(12, 0),        HPOSTER_S0_FILTER_NAME));
    filters.add(new NamedFilter(new HuePosterizerFilter(12, 0.04167f), HPOSTER_S1_FILTER_NAME));

    filters.add(new NamedFilter(new SaturationPosterizerFilter(2, false), SATPOSTER_FILTER_NAME));

    filters.add(new NamedFilter(new SingleFrameBufferedImageFilter(new HorizontalFlipTransformImageOp()), HORIZONTAL_FILTER_NAME));
    filters.add(new NamedFilter(new SingleFrameBufferedImageFilter(new VerticalFlipTransformImageOp()),   VERTICAL_FILTER_NAME));
    filters.add(new NamedFilter(new SingleFrameBufferedImageFilter(new RotationTransformImageOp()),       ROTATION_FILTER_NAME));

    return filters;
  }

//------------------------------------------------------------------------------

}
