
package org.skyllias.alomatia.filter.factory;

import java.awt.Color;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

import org.skyllias.alomatia.filter.NamedFilter;
import org.skyllias.alomatia.filter.affine.AffineFilterFactory;
import org.skyllias.alomatia.filter.buffered.diffusion.DiffusionFilterFactory;
import org.skyllias.alomatia.filter.buffered.distortion.DistortingFilterFactory;
import org.skyllias.alomatia.filter.buffered.hdr.naive.NaiveHdrFilterFactory;
import org.skyllias.alomatia.filter.buffered.map.AngularMap;
import org.skyllias.alomatia.filter.buffered.map.CrossedMap;
import org.skyllias.alomatia.filter.buffered.map.DiagonalMap;
import org.skyllias.alomatia.filter.buffered.map.RadialMap;
import org.skyllias.alomatia.filter.buffered.patch.AxeColoursFilterFactory;
import org.skyllias.alomatia.filter.buffered.simple.DyeFilterFactory;
import org.skyllias.alomatia.filter.buffered.simple.PixelizerFilterFactory;
import org.skyllias.alomatia.filter.buffered.spectrum.SpectrumFilterFactory;
import org.skyllias.alomatia.filter.buffered.surround.SurroundingFilterFactory;
import org.skyllias.alomatia.filter.buffered.vignette.VignetteFilterFactory;
import org.skyllias.alomatia.filter.convolve.BlurFilterFactory;
import org.skyllias.alomatia.filter.convolve.EdgeConvolvingComposedFilter;
import org.skyllias.alomatia.filter.convolve.EdgeDetectorFilterFactory;
import org.skyllias.alomatia.filter.convolve.LinearBlurKernelDataFactory;
import org.skyllias.alomatia.filter.convolve.NucelarWashKernelDataFactory;
import org.skyllias.alomatia.filter.convolve.SquareBlurLineProfile;
import org.skyllias.alomatia.filter.convolve.emboss.EmbossFilterFactory;
import org.skyllias.alomatia.filter.daltonism.LmsFilterFactory;
import org.skyllias.alomatia.filter.daltonism.XyzFilterFactory;
import org.skyllias.alomatia.filter.hsb.HsbFilterFactory;
import org.skyllias.alomatia.filter.hsb.function.CosineHueFunction;
import org.skyllias.alomatia.filter.hsb.function.FlatStepHueFunction;
import org.skyllias.alomatia.filter.hsb.function.MultiplyingHueFactor;
import org.skyllias.alomatia.filter.hsb.function.PitStepHueFunction;
import org.skyllias.alomatia.filter.hsb.function.PositiveFilteringHueFunction;
import org.skyllias.alomatia.filter.hsb.pole.DistantAttraction;
import org.skyllias.alomatia.filter.hsb.pole.LinearRepulsion;
import org.skyllias.alomatia.filter.rgb.RgbFilterFactory;
import org.skyllias.alomatia.filter.rgb.lookup.ChannelLookupFilterFactory;
import org.springframework.stereotype.Component;

/** FilterFactory with a hardcoded set of available filters. */

@Component
public class FixedFilterFactory implements FilterFactory
{
  private static final String NO_FILTER_KEY            = "filter.selector.none";
  private static final String LMSPROPIA_FILTER_KEY     = "filter.dalton.lms.protanopia";
  private static final String LMSDEUPIA_FILTER_KEY     = "filter.dalton.lms.deuteranopia";
  private static final String LMSTRIPIA_FILTER_KEY     = "filter.dalton.lms.tritanopia";
  private static final String XYZPROPIA_FILTER_KEY     = "filter.dalton.xyz.protanopia";
  private static final String XYZPROMALY_FILTER_KEY    = "filter.dalton.xyz.protanomaly";
  private static final String XYZDEUPIA_FILTER_KEY     = "filter.dalton.xyz.deuteranopia";
  private static final String XYZDEUMALY_FILTER_KEY    = "filter.dalton.xyz.deuteranomaly";
  private static final String XYZTRIPIA_FILTER_KEY     = "filter.dalton.xyz.tritanopia";
  private static final String XYZTRIMALY_FILTER_KEY    = "filter.dalton.xyz.tritanomaly";
  private static final String XYZACHSIA_FILTER_KEY     = "filter.dalton.xyz.achromatopsia";
  private static final String XYZACHMALY_FILTER_KEY    = "filter.dalton.xyz.achromatomaly";
  private static final String TRITONE_RGB_FILTER_KEY   = "filter.tritone.rgb";
  private static final String TRITONE_RBG_FILTER_KEY   = "filter.tritone.rbg";
  private static final String TRITONE_GBR_FILTER_KEY   = "filter.tritone.gbr";
  private static final String TRITONE_GRB_FILTER_KEY   = "filter.tritone.grb";
  private static final String TRITONE_BRG_FILTER_KEY   = "filter.tritone.brg";
  private static final String TRITONE_BGR_FILTER_KEY   = "filter.tritone.bgr";
  private static final String TRITONE_RGC_FILTER_KEY   = "filter.tritone.rgc";
  private static final String TRITONE_GGP_FILTER_KEY   = "filter.tritone.ggp";
  private static final String TRITONE_BGY_FILTER_KEY   = "filter.tritone.bgy";
  private static final String TRITONE_NRY_FILTER_KEY   = "filter.tritone.nry";
  private static final String TRITONE_NRP_FILTER_KEY   = "filter.tritone.nrp";
  private static final String TRITONE_NGC_FILTER_KEY   = "filter.tritone.ngc";
  private static final String TRITONE_NGY_FILTER_KEY   = "filter.tritone.ngy";
  private static final String TRITONE_NBP_FILTER_KEY   = "filter.tritone.nbp";
  private static final String TRITONE_NBC_FILTER_KEY   = "filter.tritone.nbc";
  private static final String TRITONE_NRW_FILTER_KEY   = "filter.tritone.nrw";
  private static final String TRITONE_NGW_FILTER_KEY   = "filter.tritone.ngw";
  private static final String TRITONE_NBW_FILTER_KEY   = "filter.tritone.nbw";
  private static final String SEPIA_S_FILTER_KEY       = "filter.sepia.s";
  private static final String SEPIA_M_FILTER_KEY       = "filter.sepia.m";
  private static final String SEPIA_L_FILTER_KEY       = "filter.sepia.l";
  private static final String SEPIA_LUNAPIC_FILTER_KEY = "filter.sepia.lunapic";
  private static final String SEPIA_TUXPI_FILTER_KEY   = "filter.sepia.tuxpi";
  private static final String SEPIA_PIXLIED_FILTER_KEY = "filter.sepia.pixelied";
  private static final String RGBR_FILTER_KEY          = "filter.rgb.gbr";
  private static final String BGRB_FILTER_KEY          = "filter.rgb.brg";
  private static final String SWAP_RG_FILTER_KEY       = "filter.rgb.swap.rg";
  private static final String SWAP_GB_FILTER_KEY       = "filter.rgb.swap.gb";
  private static final String SWAP_BR_FILTER_KEY       = "filter.rgb.swap.br";
  private static final String EQUAL_GREY_FILTER_KEY    = "filter.rgb.greys.equal";
  private static final String HUMAN_GREY_FILTER_KEY    = "filter.rgb.greys.human";
  private static final String MAX_CH_GREY_FILTER_KEY   = "filter.rgb.greys.channel.max";
  private static final String MED_CH_GREY_FILTER_KEY   = "filter.rgb.greys.channel.median";
  private static final String MIN_CH_GREY_FILTER_KEY   = "filter.rgb.greys.channel.min";
  private static final String REDONLY_FILTER_KEY       = "filter.rgb.redonly";
  private static final String GREENONLY_FILTER_KEY     = "filter.rgb.greenonly";
  private static final String BLUEONLY_FILTER_KEY      = "filter.rgb.blueonly";
  private static final String REDLESS_FILTER_KEY       = "filter.rgb.redless";
  private static final String GREENLESS_FILTER_KEY     = "filter.rgb.greenless";
  private static final String BLUELESS_FILTER_KEY      = "filter.rgb.blueless";
  private static final String YELLOW_EQ_FILTER_KEY     = "filter.rgb.blue+yellow";
  private static final String MAGENTA_EQ_FILTER_KEY    = "filter.rgb.green+magenta";
  private static final String CYAN_EQ_FILTER_KEY       = "filter.rgb.red+cyan";
  private static final String NEGATIVE_FILTER_KEY      = "filter.rgb.invert";
  private static final String DEC_SAT_XL_FILTER_KEY    = "filter.hsb.saturation-xl";
  private static final String DEC_SAT_L_FILTER_KEY     = "filter.hsb.saturation-l";
  private static final String DEC_SAT_M_FILTER_KEY     = "filter.hsb.saturation-m";
  private static final String DEC_SAT_S_FILTER_KEY     = "filter.hsb.saturation-s";
  private static final String DEC_SAT_XS_FILTER_KEY    = "filter.hsb.saturation-xs";
  private static final String INC_SAT_XL_FILTER_KEY    = "filter.hsb.saturation+xl";
  private static final String INC_SAT_L_FILTER_KEY     = "filter.hsb.saturation+l";
  private static final String INC_SAT_M_FILTER_KEY     = "filter.hsb.saturation+m";
  private static final String INC_SAT_S_FILTER_KEY     = "filter.hsb.saturation+s";
  private static final String INC_SAT_XS_FILTER_KEY    = "filter.hsb.saturation+xs";
  private static final String FIX_HUE_RED_FILTER_KEY   = "filter.hsb.hue.fixed.red";
  private static final String FIX_HUE_YELW_FILTER_KEY  = "filter.hsb.hue.fixed.yellow";
  private static final String FIX_HUE_GREEN_FILTER_KEY = "filter.hsb.hue.fixed.green";
  private static final String FIX_HUE_CYAN_FILTER_KEY  = "filter.hsb.hue.fixed.cyan";
  private static final String FIX_HUE_BLUE_FILTER_KEY  = "filter.hsb.hue.fixed.blue";
  private static final String FIX_HUE_PRPL_FILTER_KEY  = "filter.hsb.hue.fixed.purple";
  private static final String DEC_HUE_XL_FILTER_KEY    = "filter.hsb.hue-xl";
  private static final String DEC_HUE_L_FILTER_KEY     = "filter.hsb.hue-l";
  private static final String DEC_HUE_M_FILTER_KEY     = "filter.hsb.hue-m";
  private static final String DEC_HUE_S_FILTER_KEY     = "filter.hsb.hue-s";
  private static final String DEC_HUE_XS_FILTER_KEY    = "filter.hsb.hue-xs";
  private static final String INC_HUE_XL_FILTER_KEY    = "filter.hsb.hue+xl";
  private static final String INC_HUE_L_FILTER_KEY     = "filter.hsb.hue+l";
  private static final String INC_HUE_M_FILTER_KEY     = "filter.hsb.hue+m";
  private static final String INC_HUE_S_FILTER_KEY     = "filter.hsb.hue+s";
  private static final String INC_HUE_XS_FILTER_KEY    = "filter.hsb.hue+xs";
  private static final String DEC_BRI_XL_FILTER_KEY    = "filter.hsb.brightness-xl";
  private static final String DEC_BRI_L_FILTER_KEY     = "filter.hsb.brightness-l";
  private static final String DEC_BRI_M_FILTER_KEY     = "filter.hsb.brightness-m";
  private static final String DEC_BRI_S_FILTER_KEY     = "filter.hsb.brightness-s";
  private static final String DEC_BRI_XS_FILTER_KEY    = "filter.hsb.brightness-xs";
  private static final String INC_BRI_XL_FILTER_KEY    = "filter.hsb.brightness+xl";
  private static final String INC_BRI_L_FILTER_KEY     = "filter.hsb.brightness+l";
  private static final String INC_BRI_M_FILTER_KEY     = "filter.hsb.brightness+m";
  private static final String INC_BRI_S_FILTER_KEY     = "filter.hsb.brightness+s";
  private static final String INC_BRI_XS_FILTER_KEY    = "filter.hsb.brightness+xs";
  private static final String DEC_CTR_XL_FILTER_KEY    = "filter.hsb.lightcontrast-xl";
  private static final String DEC_CTR_L_FILTER_KEY     = "filter.hsb.lightcontrast-l";
  private static final String DEC_CTR_M_FILTER_KEY     = "filter.hsb.lightcontrast-m";
  private static final String DEC_CTR_S_FILTER_KEY     = "filter.hsb.lightcontrast-s";
  private static final String DEC_CTR_XS_FILTER_KEY    = "filter.hsb.lightcontrast-xs";
  private static final String INC_CTR_XL_FILTER_KEY    = "filter.hsb.lightcontrast+xl";
  private static final String INC_CTR_L_FILTER_KEY     = "filter.hsb.lightcontrast+l";
  private static final String INC_CTR_M_FILTER_KEY     = "filter.hsb.lightcontrast+m";
  private static final String INC_CTR_S_FILTER_KEY     = "filter.hsb.lightcontrast+s";
  private static final String INC_CTR_XS_FILTER_KEY    = "filter.hsb.lightcontrast+xs";
  private static final String INC_RED_BR_FILTER_KEY    = "filter.hsb.red.brightness.up";
  private static final String DEC_RED_BR_FILTER_KEY    = "filter.hsb.red.brightness.down";
  private static final String INC_GRN_BR_FILTER_KEY    = "filter.hsb.green.brightness.up";
  private static final String DEC_GRN_BR_FILTER_KEY    = "filter.hsb.green.brightness.down";
  private static final String INC_BLU_BR_FILTER_KEY    = "filter.hsb.blue.brightness.up";
  private static final String DEC_BLU_BR_FILTER_KEY    = "filter.hsb.blue.brightness.down";
  private static final String INC_RED_SA_FILTER_KEY    = "filter.hsb.red.saturation.up";
  private static final String DEC_RED_SA_FILTER_KEY    = "filter.hsb.red.saturation.down";
  private static final String INC_GRN_SA_FILTER_KEY    = "filter.hsb.green.saturation.up";
  private static final String DEC_GRN_SA_FILTER_KEY    = "filter.hsb.green.saturation.down";
  private static final String INC_BLU_SA_FILTER_KEY    = "filter.hsb.blue.saturation.up";
  private static final String DEC_BLU_SA_FILTER_KEY    = "filter.hsb.blue.saturation.down";
  private static final String REVERSE_ORANG_FILTER_KEY = "filter.hsb.hue.reverse.orange-azure";
  private static final String REVERSE_TEAL_FILTER_KEY  = "filter.hsb.hue.reverse.teal-crimson";
  private static final String REVERSE_VIOLE_FILTER_KEY = "filter.hsb.hue.reverse.violet-chartreuse";
  private static final String DEC_CCT_XL_FILTER_KEY    = "filter.rgb.colourcontrast-xl";
  private static final String DEC_CCT_L_FILTER_KEY     = "filter.rgb.colourcontrast-l";
  private static final String DEC_CCT_M_FILTER_KEY     = "filter.rgb.colourcontrast-m";
  private static final String DEC_CCT_S_FILTER_KEY     = "filter.rgb.colourcontrast-s";
  private static final String DEC_CCT_XS_FILTER_KEY    = "filter.rgb.colourcontrast-xs";
  private static final String INC_CCT_XL_FILTER_KEY    = "filter.rgb.colourcontrast+xl";
  private static final String INC_CCT_L_FILTER_KEY     = "filter.rgb.colourcontrast+l";
  private static final String INC_CCT_M_FILTER_KEY     = "filter.rgb.colourcontrast+m";
  private static final String INC_CCT_S_FILTER_KEY     = "filter.rgb.colourcontrast+s";
  private static final String INC_CCT_XS_FILTER_KEY    = "filter.rgb.colourcontrast+xs";
  private static final String NAIVE_HDR_FILTER_KEY     = "filter.hdr.naive.standard";
  private static final String NAIVE_HDR_B_FILTER_KEY   = "filter.hdr.naive.blurry";
  private static final String HUE_DIFFUSION_FILTER_KEY = "filter.hsb.diffusion.hue";
  private static final String BNW_PIXEL_FILTER_KEY     = "filter.b&w.pixel";
  private static final String BNW_BLOT_FILTER_KEY      = "filter.b&w.blot";
  private static final String BNW_LITE_L_FILTER_KEY    = "filter.b&w.light.l";
  private static final String BNW_LITE_M_FILTER_KEY    = "filter.b&w.light.m";
  private static final String BNW_LITE_S_FILTER_KEY    = "filter.b&w.light.s";
  private static final String BNW_DARK_S_FILTER_KEY    = "filter.b&w.dark.s";
  private static final String BNW_DARK_M_FILTER_KEY    = "filter.b&w.dark.m";
  private static final String BNW_DARK_L_FILTER_KEY    = "filter.b&w.dark.l";
  private static final String BNW_BLACK_RED_FILTER_KEY = "filter.b&w.black&red";
  private static final String BNW_PRPL_YLW_FILTER_KEY  = "filter.b&w.purple&yellow";
  private static final String BNW_BLUE_ORNG_FILTER_KEY = "filter.b&w.blue&orange";
  private static final String BNW_GREEN_FILTER_KEY     = "filter.b&w.green";
  private static final String BNW_RED_WHITE_FILTER_KEY = "filter.b&w.red&white";
  private static final String BNW_SCATT_FILTER_KEY     = "filter.b&w.scatter";
  private static final String BNW_SNOW_FILTER_KEY      = "filter.b&w.snow";
  private static final String BLUR_SMALL_FILTER_KEY    = "filter.blur.small";
  private static final String BLUR_MED_FILTER_KEY      = "filter.blur.medium";
  private static final String BLUR_BIG_FILTER_KEY      = "filter.blur.big";
  private static final String MOTION_S0_FILTER_KEY     = "filter.blur.motion.slow.horizontal";
  private static final String MOTION_S90_FILTER_KEY    = "filter.blur.motion.slow.vertical";
  private static final String MOTION_S45_FILTER_KEY    = "filter.blur.motion.slow.oblique";
  private static final String MOTION_L0_FILTER_KEY     = "filter.blur.motion.fast.horizontal";
  private static final String MOTION_L90_FILTER_KEY    = "filter.blur.motion.fast.vertical";
  private static final String MOTION_L45_FILTER_KEY    = "filter.blur.motion.fast.oblique";
  private static final String SHARPEN_FILTER_KEY       = "filter.blur.sharpen";
  private static final String EDGEDETECT_FILTER_KEY    = "filter.convolve.edgedetection.standard";
  private static final String THICKEDGES_FILTER_KEY    = "filter.convolve.edgedetection.thick.s";
  private static final String THICKEDGEM_FILTER_KEY    = "filter.convolve.edgedetection.thick.m";
  private static final String THICKEDGEL_FILTER_KEY    = "filter.convolve.edgedetection.thick.l";
  private static final String NUCELAR_FILTER_KEY       = "filter.convolve.nucelarwash";
  private static final String LAYEMBOSS_FILTER_KEY     = "filter.convolve.emboss.layered";
  private static final String SMTHEMBOSS_FILTER_KEY    = "filter.convolve.emboss.smooth";
  private static final String MEDIAN_XS_FILTER_KEY     = "filter.median.xs";
  private static final String MEDIAN_M_FILTER_KEY      = "filter.median.m";
  private static final String ORANGEPHIL_FILTER_KEY    = "filter.phile.orange";
  private static final String GREENPHIL_FILTER_KEY     = "filter.phile.green";
  private static final String PURPLEPHIL_FILTER_KEY    = "filter.phile.purple";
  private static final String REDPHOB_FILTER_KEY       = "filter.phobic.red";
  private static final String GREENPHOB_FILTER_KEY     = "filter.phobic.green";
  private static final String BLUEPHOB_FILTER_KEY      = "filter.phobic.blue";
  private static final String YELLOWPHOB_FILTER_KEY    = "filter.phobic.yellow";
  private static final String CYANPHOB_FILTER_KEY      = "filter.phobic.cyan";
  private static final String MAGENTPHOB_FILTER_KEY    = "filter.phobic.magenta";
  private static final String SEAM_RED_FILTER_KEY      = "filter.seamless.red";
  private static final String SEAM_GREEN_FILTER_KEY    = "filter.seamless.green";
  private static final String SEAM_BLUE_FILTER_KEY     = "filter.seamless.blue";
  private static final String SEAM_YELLOW_FILTER_KEY   = "filter.seamless.yellow";
  private static final String SEAM_CYAN_FILTER_KEY     = "filter.seamless.cyan";
  private static final String SEAM_MAGENTA_FILTER_KEY  = "filter.seamless.magenta";
  private static final String WHITEDYE_FILTER_KEY      = "filter.dye.white";
  private static final String BLACKDYE_FILTER_KEY      = "filter.dye.black";
  private static final String REDDYE_FILTER_KEY        = "filter.dye.red";
  private static final String GREENDYE_FILTER_KEY      = "filter.dye.green";
  private static final String BLUEDYE_FILTER_KEY       = "filter.dye.blue";
  private static final String YELLOWDYE_FILTER_KEY     = "filter.dye.yellow";
  private static final String CYANDYE_FILTER_KEY       = "filter.dye.cyan";
  private static final String MAGENTADYE_FILTER_KEY    = "filter.dye.magenta";
  private static final String DOMINANT_RED_FILTER_KEY  = "filter.spectrum.hue.dominant.red";
  private static final String DOMINANT_YLOW_FILTER_KEY = "filter.spectrum.hue.dominant.yellow";
  private static final String DOMINANT_GREN_FILTER_KEY = "filter.spectrum.hue.dominant.green";
  private static final String DOMINANT_CYAN_FILTER_KEY = "filter.spectrum.hue.dominant.cyan";
  private static final String DOMINANT_BLUE_FILTER_KEY = "filter.spectrum.hue.dominant.blue";
  private static final String DOMINANT_PRPL_FILTER_KEY = "filter.spectrum.hue.dominant.purple";
  private static final String HORIZONTAL_FILTER_KEY    = "filter.affine.horizontal";
  private static final String VERTICAL_FILTER_KEY      = "filter.affine.vertical";
  private static final String ROTATION_FILTER_KEY      = "filter.affine.rotation";
  private static final String POSTER_XL_FILTER_KEY     = "filter.rgb.posterize.xl";
  private static final String POSTER_L_FILTER_KEY      = "filter.rgb.posterize.l";
  private static final String POSTER_M_FILTER_KEY      = "filter.rgb.posterize.m";
  private static final String POSTER_S_FILTER_KEY      = "filter.rgb.posterize.s";
  private static final String POSTER_XS_FILTER_KEY     = "filter.rgb.posterize.xs";
  private static final String BPOSTER_XL_FILTER_KEY    = "filter.hsb.posterize.brightness.xl";
  private static final String BPOSTER_L_FILTER_KEY     = "filter.hsb.posterize.brightness.l";
  private static final String BPOSTER_M_FILTER_KEY     = "filter.hsb.posterize.brightness.m";
  private static final String BPOSTER_S_FILTER_KEY     = "filter.hsb.posterize.brightness.s";
  private static final String BPOSTER_XS_FILTER_KEY    = "filter.hsb.posterize.brightness.xs";
  private static final String SATPOSTER_FILTER_KEY     = "filter.hsb.posterize.saturation";
  private static final String HPOSTER_L0_FILTER_KEY    = "filter.hsb.posterize.hue.l0";
  private static final String HPOSTER_L1_FILTER_KEY    = "filter.hsb.posterize.hue.l1";
  private static final String HPOSTER_M0_FILTER_KEY    = "filter.hsb.posterize.hue.m0";
  private static final String HPOSTER_M1_FILTER_KEY    = "filter.hsb.posterize.hue.m1";
  private static final String HPOSTER_S0_FILTER_KEY    = "filter.hsb.posterize.hue.s0";
  private static final String HPOSTER_S1_FILTER_KEY    = "filter.hsb.posterize.hue.s1";
  private static final String MAXONLY_XL_FILTER_KEY    = "filter.rgb.maxonly.xl";
  private static final String MAXONLY_L_FILTER_KEY     = "filter.rgb.maxonly.l";
  private static final String MAXONLY_M_FILTER_KEY     = "filter.rgb.maxonly.m";
  private static final String MAXONLY_S_FILTER_KEY     = "filter.rgb.maxonly.s";
  private static final String MAXONLY_XS_FILTER_KEY    = "filter.rgb.maxonly.xs";
  private static final String PIXEL_XS_FILTER_KEY      = "filter.pixelize.xs";
  private static final String PIXEL_S_FILTER_KEY       = "filter.pixelize.s";
  private static final String PIXEL_M_FILTER_KEY       = "filter.pixelize.m";
  private static final String PIXEL_L_FILTER_KEY       = "filter.pixelize.l";
  private static final String PIXEL_XL_FILTER_KEY      = "filter.pixelize.xl";
  private static final String HIGHSRED_FILTER_KEY      = "filter.highlight.saturation.red";
  private static final String HIGHSYELOW_FILTER_KEY    = "filter.highlight.saturation.yellow";
  private static final String HIGHSGREEN_FILTER_KEY    = "filter.highlight.saturation.green";
  private static final String HIGHSBLUE_FILTER_KEY     = "filter.highlight.saturation.blue";
  private static final String HIGHSPURPL_FILTER_KEY    = "filter.highlight.saturation.purple";
  private static final String H_BY_B_B_R_FILTER_KEY    = "filter.hsb.hue-by-brightness.blue-red";
  private static final String H_BY_B_R_B_FILTER_KEY    = "filter.hsb.hue-by-brightness.red-blue";
  private static final String H_BY_B_G_B_FILTER_KEY    = "filter.hsb.hue-by-brightness.green-blue";
  private static final String H_BY_B_B_G_FILTER_KEY    = "filter.hsb.hue-by-brightness.blue-green";
  private static final String H_BY_B_R_G_FILTER_KEY    = "filter.hsb.hue-by-brightness.red-green";
  private static final String DARK_G_BRIT_B_FILTER_KEY = "filter.hsb.dark-bright.green-blue";
  private static final String DARK_B_BRIT_R_FILTER_KEY = "filter.hsb.dark-bright.blue-red";
  private static final String DARK_R_BRIT_G_FILTER_KEY = "filter.hsb.dark-bright.red-green";
  private static final String DARK_C_BRIT_M_FILTER_KEY = "filter.hsb.dark-bright.cyan-magenta";
  private static final String DARK_M_BRIT_Y_FILTER_KEY = "filter.hsb.dark-bright.magenta-yellow";
  private static final String DARK_Y_BRIT_C_FILTER_KEY = "filter.hsb.dark-bright.yellow-cyan";
  private static final String H_BY_B_G_R_FILTER_KEY    = "filter.hsb.hue-by-brightness.green-red";
  private static final String PSYSCHED_XS_FILTER_KEY   = "filter.psychedelic.xs";
  private static final String PSYSCHED_S_FILTER_KEY    = "filter.psychedelic.s";
  private static final String PSYSCHED_M_FILTER_KEY    = "filter.psychedelic.m";
  private static final String PSYSCHED_L_FILTER_KEY    = "filter.psychedelic.l";
  private static final String PSYSCHED_XL_FILTER_KEY   = "filter.psychedelic.xl";
  private static final String HIGHBRED_FILTER_KEY      = "filter.highlight.brightness.red";
  private static final String HIGHBYELOW_FILTER_KEY    = "filter.highlight.brightness.yellow";
  private static final String HIGHBGREEN_FILTER_KEY    = "filter.highlight.brightness.green";
  private static final String HIGHBBLUE_FILTER_KEY     = "filter.highlight.brightness.blue";
  private static final String HIGHBPURPL_FILTER_KEY    = "filter.highlight.brightness.purple";
  private static final String VIGNETTE_R_FILTER_KEY    = "filter.vignette.round";
  private static final String VIGNETTE_C_FILTER_KEY    = "filter.vignette.cross";
  private static final String VIGNETTE_E_FILTER_KEY    = "filter.vignette.edges";
  private static final String MINMAX_BLK_FILTER_KEY    = "filter.minnmax.black";
  private static final String MINMAX_RED_FILTER_KEY    = "filter.minnmax.red";
  private static final String MINMAX_GRN_FILTER_KEY    = "filter.minnmax.green";
  private static final String MINMAX_BLU_FILTER_KEY    = "filter.minnmax.blue";
  private static final String MINMAX_CYA_FILTER_KEY    = "filter.minnmax.cyan";
  private static final String MINMAX_MGT_FILTER_KEY    = "filter.minnmax.magenta";
  private static final String MINMAX_YLW_FILTER_KEY    = "filter.minnmax.yellow";
  private static final String MINMAX_WIT_FILTER_KEY    = "filter.minnmax.white";
  private static final String MAGNIFY_XS_FILTER_KEY    = "filter.distort.magnifier.xs";
  private static final String MAGNIFY_S_FILTER_KEY     = "filter.distort.magnifier.s";
  private static final String MAGNIFY_M_FILTER_KEY     = "filter.distort.magnifier.m";
  private static final String MAGNIFY_L_FILTER_KEY     = "filter.distort.magnifier.l";
  private static final String MAGNIFY_XL_FILTER_KEY    = "filter.distort.magnifier.xl";
  private static final String REDUCE_XS_FILTER_KEY     = "filter.distort.reductor.xs";
  private static final String REDUCE_S_FILTER_KEY      = "filter.distort.reductor.s";
  private static final String REDUCE_M_FILTER_KEY      = "filter.distort.reductor.m";
  private static final String REDUCE_L_FILTER_KEY      = "filter.distort.reductor.l";
  private static final String REDUCE_XL_FILTER_KEY     = "filter.distort.reductor.xl";
  private static final String WAVE_HLST_FILTER_KEY     = "filter.distort.wave.hor+long+slow+thin";
  private static final String WAVE_HLSW_FILTER_KEY     = "filter.distort.wave.hor+long+slow+wide";
  private static final String WAVE_HLFT_FILTER_KEY     = "filter.distort.wave.hor+long+fast+thin";
  private static final String WAVE_HLFW_FILTER_KEY     = "filter.distort.wave.hor+long+fast+wide";
  private static final String WAVE_HPST_FILTER_KEY     = "filter.distort.wave.hor+perp+slow+thin";
  private static final String WAVE_HPSW_FILTER_KEY     = "filter.distort.wave.hor+perp+slow+wide";
  private static final String WAVE_HPFT_FILTER_KEY     = "filter.distort.wave.hor+perp+fast+thin";
  private static final String WAVE_HPFW_FILTER_KEY     = "filter.distort.wave.hor+perp+fast+wide";
  private static final String WAVE_VLST_FILTER_KEY     = "filter.distort.wave.ver+long+slow+thin";
  private static final String WAVE_VLSW_FILTER_KEY     = "filter.distort.wave.ver+long+slow+wide";
  private static final String WAVE_VLFT_FILTER_KEY     = "filter.distort.wave.ver+long+fast+thin";
  private static final String WAVE_VLFW_FILTER_KEY     = "filter.distort.wave.ver+long+fast+wide";
  private static final String WAVE_VPST_FILTER_KEY     = "filter.distort.wave.ver+perp+slow+thin";
  private static final String WAVE_VPSW_FILTER_KEY     = "filter.distort.wave.ver+perp+slow+wide";
  private static final String WAVE_VPFT_FILTER_KEY     = "filter.distort.wave.ver+perp+fast+thin";
  private static final String WAVE_VPFW_FILTER_KEY     = "filter.distort.wave.ver+perp+fast+wide";
  private static final String WAVE_OBLO_FILTER_KEY     = "filter.distort.wave.obl+long";
  private static final String WAVE_OBPE_FILTER_KEY     = "filter.distort.wave.obl+perp";
  private static final String WAVE_ROUGH_FILTER_KEY    = "filter.distort.wave.rough";
  private static final String TILT_RIGHT_FILTER_KEY    = "filter.distort.tilt.right";
  private static final String TILT_LEFT_FILTER_KEY     = "filter.distort.tilt.left";
  private static final String TURNING_XS_FILTER_KEY    = "filter.distort.turning.xs";
  private static final String TURNING_S_FILTER_KEY     = "filter.distort.turning.s";
  private static final String TURNING_M_FILTER_KEY     = "filter.distort.turning.m";
  private static final String TURNING_L_FILTER_KEY     = "filter.distort.turning.l";
  private static final String TURNING_XL_FILTER_KEY    = "filter.distort.turning.xl";
  private static final String WHIRL_XS_FILTER_KEY      = "filter.distort.whirlpool.xs";
  private static final String WHIRL_S_FILTER_KEY       = "filter.distort.whirlpool.s";
  private static final String WHIRL_M_FILTER_KEY       = "filter.distort.whirlpool.m";
  private static final String WHIRL_L_FILTER_KEY       = "filter.distort.whirlpool.l";
  private static final String WHIRL_XL_FILTER_KEY      = "filter.distort.whirlpool.xl";
  private static final String AXE_S_S_FILTER_KEY       = "filter.axe.s+s";
  private static final String AXE_S_L_FILTER_KEY       = "filter.axe.s+l";
  private static final String AXE_L_S_FILTER_KEY       = "filter.axe.l+s";
  private static final String AXE_L_L_FILTER_KEY       = "filter.axe.l+l";
  private static final String INST_ALLOPO_FILTER_KEY   = "filter.lookup.allopo";
  private static final String INST_EARBY_FILTER_KEY    = "filter.lookup.earbylird";
  private static final String INST_THOGAM_FILTER_KEY   = "filter.lookup.thogam";
  private static final String INST_VASHNILL_FILTER_KEY = "filter.lookup.vashnille";
  private static final String INST_FEHE_FILTER_KEY     = "filter.lookup.fehe";
  private static final String INST_MOLO_FILTER_KEY     = "filter.lookup.molo";
  private static final String INST_KEVLIN_FILTER_KEY   = "filter.lookup.kevlin";
  private static final String INST_1976_FILTER_KEY     = "filter.lookup.1976";
  private static final String INST_SOATER_FILTER_KEY   = "filter.lookup.soater";
  private static final String INST_LAWDEN_FILTER_KEY   = "filter.lookup.lawden";
  private static final String INST_PROX_FILTER_KEY     = "filter.lookup.prox";

  private static Collection<NamedFilter> filters = new LinkedList<>();

//==============================================================================

  static
  {
    filters.add(new NamedFilter(null, NO_FILTER_KEY));

    filters.add(new NamedFilter(XyzFilterFactory.forProtanopia(),    XYZPROPIA_FILTER_KEY));
    filters.add(new NamedFilter(XyzFilterFactory.forProtanomaly(),   XYZPROMALY_FILTER_KEY));
    filters.add(new NamedFilter(XyzFilterFactory.forDeuteranopia(),  XYZDEUPIA_FILTER_KEY));
    filters.add(new NamedFilter(XyzFilterFactory.forDeuteranomaly(), XYZDEUMALY_FILTER_KEY));
    filters.add(new NamedFilter(XyzFilterFactory.forTritanopia(),    XYZTRIPIA_FILTER_KEY));
    filters.add(new NamedFilter(XyzFilterFactory.forTritanomaly(),   XYZTRIMALY_FILTER_KEY));
    filters.add(new NamedFilter(XyzFilterFactory.forAchromatopsia(), XYZACHSIA_FILTER_KEY));
    filters.add(new NamedFilter(XyzFilterFactory.forAchromatomaly(), XYZACHMALY_FILTER_KEY));

    filters.add(new NamedFilter(LmsFilterFactory.forProtanopia(),   LMSPROPIA_FILTER_KEY));
    filters.add(new NamedFilter(LmsFilterFactory.forDeuteranopia(), LMSDEUPIA_FILTER_KEY));
    filters.add(new NamedFilter(LmsFilterFactory.forTritanopia(),   LMSTRIPIA_FILTER_KEY));

    filters.add(new NamedFilter(ChannelLookupFilterFactory.forThogam(),    INST_THOGAM_FILTER_KEY));
    filters.add(new NamedFilter(ChannelLookupFilterFactory.forProx(),      INST_PROX_FILTER_KEY));
    filters.add(new NamedFilter(ChannelLookupFilterFactory.forLawden(),    INST_LAWDEN_FILTER_KEY));
    filters.add(new NamedFilter(ChannelLookupFilterFactory.forFehe(),      INST_FEHE_FILTER_KEY));
    filters.add(new NamedFilter(ChannelLookupFilterFactory.forMolo(),      INST_MOLO_FILTER_KEY));
    filters.add(new NamedFilter(ChannelLookupFilterFactory.forAllopo(),    INST_ALLOPO_FILTER_KEY));
    filters.add(new NamedFilter(ChannelLookupFilterFactory.forEarbyLird(), INST_EARBY_FILTER_KEY));
    filters.add(new NamedFilter(ChannelLookupFilterFactory.forSoater(),    INST_SOATER_FILTER_KEY));
    filters.add(new NamedFilter(ChannelLookupFilterFactory.forVashnille(), INST_VASHNILL_FILTER_KEY));
    filters.add(new NamedFilter(ChannelLookupFilterFactory.for1976(),      INST_1976_FILTER_KEY));
    filters.add(new NamedFilter(ChannelLookupFilterFactory.forKevlin(),    INST_KEVLIN_FILTER_KEY));

    filters.add(new NamedFilter(VignetteFilterFactory.forRound(), VIGNETTE_R_FILTER_KEY));
    filters.add(new NamedFilter(VignetteFilterFactory.forCross(), VIGNETTE_C_FILTER_KEY));
    filters.add(new NamedFilter(VignetteFilterFactory.forEdges(), VIGNETTE_E_FILTER_KEY));

    filters.add(new NamedFilter(RgbFilterFactory.forTritone(new Color(100, 0, 0), new Color(50, 150, 50), new Color(200, 200, 255)),   TRITONE_RGB_FILTER_KEY));
    filters.add(new NamedFilter(RgbFilterFactory.forTritone(new Color(100, 0, 0), new Color(50, 50, 150), new Color(200, 255, 200)),   TRITONE_RBG_FILTER_KEY));
    filters.add(new NamedFilter(RgbFilterFactory.forTritone(new Color(0, 100, 0), new Color(50, 50, 150), new Color(255, 200, 200)),   TRITONE_GBR_FILTER_KEY));
    filters.add(new NamedFilter(RgbFilterFactory.forTritone(new Color(0, 100, 0), new Color(150, 50, 50), new Color(200, 200, 255)),   TRITONE_GRB_FILTER_KEY));
    filters.add(new NamedFilter(RgbFilterFactory.forTritone(new Color(0, 0, 100), new Color(150, 50, 50), new Color(200, 255, 200)),   TRITONE_BRG_FILTER_KEY));
    filters.add(new NamedFilter(RgbFilterFactory.forTritone(new Color(0, 0, 100), new Color(50, 150, 50), new Color(255, 200, 200)),   TRITONE_BGR_FILTER_KEY));
    filters.add(new NamedFilter(RgbFilterFactory.forTritone(new Color(127, 0, 0), new Color(127, 127, 127), new Color(0, 255, 255)),   TRITONE_RGC_FILTER_KEY));
    filters.add(new NamedFilter(RgbFilterFactory.forTritone(new Color(0, 127, 0), new Color(127, 127, 127), new Color(255, 127, 255)), TRITONE_GGP_FILTER_KEY));
    filters.add(new NamedFilter(RgbFilterFactory.forTritone(new Color(0, 0, 127), new Color(127, 127, 127), new Color(255, 255, 0)),   TRITONE_BGY_FILTER_KEY));
    filters.add(new NamedFilter(RgbFilterFactory.forTritone(new Color(0, 0, 0), new Color(255, 0, 0), new Color(255, 255, 0)),         TRITONE_NRY_FILTER_KEY));
    filters.add(new NamedFilter(RgbFilterFactory.forTritone(new Color(0, 0, 0), new Color(255, 0, 0), new Color(255, 127, 255)),       TRITONE_NRP_FILTER_KEY));
    filters.add(new NamedFilter(RgbFilterFactory.forTritone(new Color(0, 0, 0), new Color(0, 255, 0), new Color(0, 255, 255)),         TRITONE_NGC_FILTER_KEY));
    filters.add(new NamedFilter(RgbFilterFactory.forTritone(new Color(0, 0, 0), new Color(0, 255, 0), new Color(255, 255, 0)),         TRITONE_NGY_FILTER_KEY));
    filters.add(new NamedFilter(RgbFilterFactory.forTritone(new Color(0, 0, 0), new Color(0, 0, 255), new Color(255, 127, 255)),       TRITONE_NBP_FILTER_KEY));
    filters.add(new NamedFilter(RgbFilterFactory.forTritone(new Color(0, 0, 0), new Color(0, 0, 255), new Color(0, 255, 255)),         TRITONE_NBC_FILTER_KEY));
    filters.add(new NamedFilter(RgbFilterFactory.forTritone(new Color(0, 0, 0), new Color(255, 0, 0), new Color(255, 255, 255)),       TRITONE_NRW_FILTER_KEY));
    filters.add(new NamedFilter(RgbFilterFactory.forTritone(new Color(0, 0, 0), new Color(0, 255, 0), new Color(255, 255, 255)),       TRITONE_NGW_FILTER_KEY));
    filters.add(new NamedFilter(RgbFilterFactory.forTritone(new Color(0, 0, 0), new Color(0, 0, 255), new Color(255, 255, 255)),       TRITONE_NBW_FILTER_KEY));

    filters.add(new NamedFilter(AxeColoursFilterFactory.forAxeColours(4, 4, new DiagonalMap()), AXE_S_S_FILTER_KEY));
    filters.add(new NamedFilter(AxeColoursFilterFactory.forAxeColours(4, 6, new CrossedMap()),  AXE_S_L_FILTER_KEY));
    filters.add(new NamedFilter(AxeColoursFilterFactory.forAxeColours(7, 4, new AngularMap()),  AXE_L_S_FILTER_KEY));
    filters.add(new NamedFilter(AxeColoursFilterFactory.forAxeColours(7, 6, new RadialMap()),   AXE_L_L_FILTER_KEY));

    filters.add(new NamedFilter(HsbFilterFactory.forFixedHue(0.0f),  FIX_HUE_RED_FILTER_KEY));
    filters.add(new NamedFilter(HsbFilterFactory.forFixedHue(0.17f), FIX_HUE_YELW_FILTER_KEY));
    filters.add(new NamedFilter(HsbFilterFactory.forFixedHue(0.33f), FIX_HUE_GREEN_FILTER_KEY));
    filters.add(new NamedFilter(HsbFilterFactory.forFixedHue(0.5f),  FIX_HUE_CYAN_FILTER_KEY));
    filters.add(new NamedFilter(HsbFilterFactory.forFixedHue(0.67f), FIX_HUE_BLUE_FILTER_KEY));
    filters.add(new NamedFilter(HsbFilterFactory.forFixedHue(0.83f), FIX_HUE_PRPL_FILTER_KEY));

    filters.add(new NamedFilter(HsbFilterFactory.forHueShift(-0.1f),  DEC_HUE_XL_FILTER_KEY));
    filters.add(new NamedFilter(HsbFilterFactory.forHueShift(-0.05f), DEC_HUE_L_FILTER_KEY));
    filters.add(new NamedFilter(HsbFilterFactory.forHueShift(-0.03f), DEC_HUE_M_FILTER_KEY));
    filters.add(new NamedFilter(HsbFilterFactory.forHueShift(-0.02f), DEC_HUE_S_FILTER_KEY));
    filters.add(new NamedFilter(HsbFilterFactory.forHueShift(-0.01f), DEC_HUE_XS_FILTER_KEY));
    filters.add(new NamedFilter(HsbFilterFactory.forHueShift(0.01f),  INC_HUE_XS_FILTER_KEY));
    filters.add(new NamedFilter(HsbFilterFactory.forHueShift(0.02f),  INC_HUE_S_FILTER_KEY));
    filters.add(new NamedFilter(HsbFilterFactory.forHueShift(0.03f),  INC_HUE_M_FILTER_KEY));
    filters.add(new NamedFilter(HsbFilterFactory.forHueShift(0.05f),  INC_HUE_L_FILTER_KEY));
    filters.add(new NamedFilter(HsbFilterFactory.forHueShift(0.1f),   INC_HUE_XL_FILTER_KEY));

    filters.add(new NamedFilter(HsbFilterFactory.forSaturation(-2),   DEC_SAT_XL_FILTER_KEY));
    filters.add(new NamedFilter(HsbFilterFactory.forSaturation(-1),   DEC_SAT_L_FILTER_KEY));
    filters.add(new NamedFilter(HsbFilterFactory.forSaturation(-0.6), DEC_SAT_M_FILTER_KEY));
    filters.add(new NamedFilter(HsbFilterFactory.forSaturation(-0.3), DEC_SAT_S_FILTER_KEY));
    filters.add(new NamedFilter(HsbFilterFactory.forSaturation(-0.1), DEC_SAT_XS_FILTER_KEY));
    filters.add(new NamedFilter(HsbFilterFactory.forSaturation(0.1),  INC_SAT_XS_FILTER_KEY));
    filters.add(new NamedFilter(HsbFilterFactory.forSaturation(0.3),  INC_SAT_S_FILTER_KEY));
    filters.add(new NamedFilter(HsbFilterFactory.forSaturation(0.6),  INC_SAT_M_FILTER_KEY));
    filters.add(new NamedFilter(HsbFilterFactory.forSaturation(1),    INC_SAT_L_FILTER_KEY));
    filters.add(new NamedFilter(HsbFilterFactory.forSaturation(2),    INC_SAT_XL_FILTER_KEY));

    filters.add(new NamedFilter(HsbFilterFactory.forBrightness(-1.5),  DEC_BRI_XL_FILTER_KEY));
    filters.add(new NamedFilter(HsbFilterFactory.forBrightness(-0.8),  DEC_BRI_L_FILTER_KEY));
    filters.add(new NamedFilter(HsbFilterFactory.forBrightness(-0.4),  DEC_BRI_M_FILTER_KEY));
    filters.add(new NamedFilter(HsbFilterFactory.forBrightness(-0.1),  DEC_BRI_S_FILTER_KEY));
    filters.add(new NamedFilter(HsbFilterFactory.forBrightness(-0.05), DEC_BRI_XS_FILTER_KEY));
    filters.add(new NamedFilter(HsbFilterFactory.forBrightness(0.05),  INC_BRI_XS_FILTER_KEY));
    filters.add(new NamedFilter(HsbFilterFactory.forBrightness(0.1),   INC_BRI_S_FILTER_KEY));
    filters.add(new NamedFilter(HsbFilterFactory.forBrightness(0.4),   INC_BRI_M_FILTER_KEY));
    filters.add(new NamedFilter(HsbFilterFactory.forBrightness(0.8),   INC_BRI_L_FILTER_KEY));
    filters.add(new NamedFilter(HsbFilterFactory.forBrightness(1.5),   INC_BRI_XL_FILTER_KEY));

    filters.add(new NamedFilter(HsbFilterFactory.forContrast(-2),   DEC_CTR_XL_FILTER_KEY));
    filters.add(new NamedFilter(HsbFilterFactory.forContrast(-1),   DEC_CTR_L_FILTER_KEY));
    filters.add(new NamedFilter(HsbFilterFactory.forContrast(-0.6), DEC_CTR_M_FILTER_KEY));
    filters.add(new NamedFilter(HsbFilterFactory.forContrast(-0.3), DEC_CTR_S_FILTER_KEY));
    filters.add(new NamedFilter(HsbFilterFactory.forContrast(-0.1), DEC_CTR_XS_FILTER_KEY));
    filters.add(new NamedFilter(HsbFilterFactory.forContrast(0.1),  INC_CTR_XS_FILTER_KEY));
    filters.add(new NamedFilter(HsbFilterFactory.forContrast(0.3),  INC_CTR_S_FILTER_KEY));
    filters.add(new NamedFilter(HsbFilterFactory.forContrast(0.6),  INC_CTR_M_FILTER_KEY));
    filters.add(new NamedFilter(HsbFilterFactory.forContrast(1),    INC_CTR_L_FILTER_KEY));
    filters.add(new NamedFilter(HsbFilterFactory.forContrast(2),    INC_CTR_XL_FILTER_KEY));

    filters.add(new NamedFilter(RgbFilterFactory.forColourContrast(-2),   DEC_CCT_XL_FILTER_KEY));
    filters.add(new NamedFilter(RgbFilterFactory.forColourContrast(-1),   DEC_CCT_L_FILTER_KEY));
    filters.add(new NamedFilter(RgbFilterFactory.forColourContrast(-0.6), DEC_CCT_M_FILTER_KEY));
    filters.add(new NamedFilter(RgbFilterFactory.forColourContrast(-0.3), DEC_CCT_S_FILTER_KEY));
    filters.add(new NamedFilter(RgbFilterFactory.forColourContrast(-0.1), DEC_CCT_XS_FILTER_KEY));
    filters.add(new NamedFilter(RgbFilterFactory.forColourContrast(0.1),  INC_CCT_XS_FILTER_KEY));
    filters.add(new NamedFilter(RgbFilterFactory.forColourContrast(0.3),  INC_CCT_S_FILTER_KEY));
    filters.add(new NamedFilter(RgbFilterFactory.forColourContrast(0.6),  INC_CCT_M_FILTER_KEY));
    filters.add(new NamedFilter(RgbFilterFactory.forColourContrast(1),    INC_CCT_L_FILTER_KEY));
    filters.add(new NamedFilter(RgbFilterFactory.forColourContrast(2),    INC_CCT_XL_FILTER_KEY));

    filters.add(new NamedFilter(NaiveHdrFilterFactory.forSmallBlur(3),  NAIVE_HDR_FILTER_KEY));
    filters.add(new NamedFilter(NaiveHdrFilterFactory.forSmallBlur(15), NAIVE_HDR_B_FILTER_KEY));

    filters.add(new NamedFilter(SurroundingFilterFactory.forMedian(1), MEDIAN_XS_FILTER_KEY));
    filters.add(new NamedFilter(SurroundingFilterFactory.forMedian(5), MEDIAN_M_FILTER_KEY));

    filters.add(new NamedFilter(BlurFilterFactory.forSharpening(),  SHARPEN_FILTER_KEY));
    filters.add(new NamedFilter(BlurFilterFactory.forParaboloid(5), BLUR_SMALL_FILTER_KEY));
    filters.add(new NamedFilter(BlurFilterFactory.forGaussian(15),  BLUR_MED_FILTER_KEY));
    filters.add(new NamedFilter(BlurFilterFactory.forGaussian(31),  BLUR_BIG_FILTER_KEY));

    filters.add(new NamedFilter(new EdgeConvolvingComposedFilter(new LinearBlurKernelDataFactory(20, 0)),                                        MOTION_S0_FILTER_KEY));
    filters.add(new NamedFilter(new EdgeConvolvingComposedFilter(new LinearBlurKernelDataFactory(20, -Math.PI / 4)),                             MOTION_S45_FILTER_KEY));
    filters.add(new NamedFilter(new EdgeConvolvingComposedFilter(new LinearBlurKernelDataFactory(20, Math.PI / 2)),                              MOTION_S90_FILTER_KEY));
    filters.add(new NamedFilter(new EdgeConvolvingComposedFilter(new LinearBlurKernelDataFactory(30, 0, new SquareBlurLineProfile())),           MOTION_L0_FILTER_KEY));
    filters.add(new NamedFilter(new EdgeConvolvingComposedFilter(new LinearBlurKernelDataFactory(30, Math.PI / 4, new SquareBlurLineProfile())), MOTION_L45_FILTER_KEY));
    filters.add(new NamedFilter(new EdgeConvolvingComposedFilter(new LinearBlurKernelDataFactory(30, Math.PI / 2, new SquareBlurLineProfile())), MOTION_L90_FILTER_KEY));

    filters.add(new NamedFilter(EdgeDetectorFilterFactory.forStandardEdgeDetection(),                 EDGEDETECT_FILTER_KEY));
    filters.add(new NamedFilter(EdgeDetectorFilterFactory.forDrawLikeEdgeDetection(0.5f),             THICKEDGES_FILTER_KEY));
    filters.add(new NamedFilter(EdgeDetectorFilterFactory.forDrawLikeEdgeDetection(1),                THICKEDGEM_FILTER_KEY));
    filters.add(new NamedFilter(EdgeDetectorFilterFactory.forDrawLikeEdgeDetection(2),                THICKEDGEL_FILTER_KEY));
    filters.add(new NamedFilter(new EdgeConvolvingComposedFilter(new NucelarWashKernelDataFactory()), NUCELAR_FILTER_KEY));

    filters.add(new NamedFilter(EmbossFilterFactory.forLayeredEmboss(), LAYEMBOSS_FILTER_KEY));
    filters.add(new NamedFilter(EmbossFilterFactory.forSmoothEmboss(),  SMTHEMBOSS_FILTER_KEY));

    filters.add(new NamedFilter(RgbFilterFactory.forNegative(), NEGATIVE_FILTER_KEY));

    filters.add(new NamedFilter(RgbFilterFactory.forTritone(new Color(33, 0, 0), new Color(112, 66, 20), new Color(255, 231, 145)),  SEPIA_L_FILTER_KEY));
    filters.add(new NamedFilter(RgbFilterFactory.forTritone(new Color(45, 12, 0), new Color(112, 66, 20), new Color(200, 174, 161)), SEPIA_M_FILTER_KEY));
    filters.add(new NamedFilter(RgbFilterFactory.forTritone(new Color(49, 5, 4), new Color(112, 66, 20), new Color(250, 225, 200)),  SEPIA_S_FILTER_KEY));
    filters.add(new NamedFilter(RgbFilterFactory.forTritone(new Color(4, 0, 0), new Color(182, 139, 74), new Color(255, 255, 219)),  SEPIA_LUNAPIC_FILTER_KEY));
    filters.add(new NamedFilter(RgbFilterFactory.forTritone(new Color(0, 0, 0), new Color(138, 123, 94), new Color(254, 227, 172)),  SEPIA_TUXPI_FILTER_KEY));
    filters.add(new NamedFilter(RgbFilterFactory.forTritone(new Color(0, 0, 0), new Color(173, 154, 120), new Color(255, 255, 239)), SEPIA_PIXLIED_FILTER_KEY));

    filters.add(new NamedFilter(RgbFilterFactory.forEqualGreyScale(),          EQUAL_GREY_FILTER_KEY));
    filters.add(new NamedFilter(RgbFilterFactory.forHumanSensitiveGreyScale(), HUMAN_GREY_FILTER_KEY));
    filters.add(new NamedFilter(RgbFilterFactory.forMaxChannelGreyScale(),     MAX_CH_GREY_FILTER_KEY));
    filters.add(new NamedFilter(RgbFilterFactory.forMedianChannelGreyScale(),  MED_CH_GREY_FILTER_KEY));
    filters.add(new NamedFilter(RgbFilterFactory.forMinChannelGreyScale(),     MIN_CH_GREY_FILTER_KEY));

    filters.add(new NamedFilter(DiffusionFilterFactory.forHueDiffusion(31), HUE_DIFFUSION_FILTER_KEY));

    filters.add(new NamedFilter(SurroundingFilterFactory.forStrictBlackOrWhite(0, Color.BLACK, Color.WHITE),                         BNW_PIXEL_FILTER_KEY));
    filters.add(new NamedFilter(SurroundingFilterFactory.forProbabilisticBlackOrWhite(1),                                            BNW_SCATT_FILTER_KEY));
    filters.add(new NamedFilter(SurroundingFilterFactory.forProbabilisticBlackOrWhite(3),                                            BNW_SNOW_FILTER_KEY));
    filters.add(new NamedFilter(SurroundingFilterFactory.forStrictBlackOrWhite(2, 0.5f, Color.BLACK, Color.WHITE),                   BNW_BLOT_FILTER_KEY));
    filters.add(new NamedFilter(SurroundingFilterFactory.forStrictBlackOrWhite(2, 0.2f, Color.BLACK, Color.WHITE),                   BNW_LITE_L_FILTER_KEY));
    filters.add(new NamedFilter(SurroundingFilterFactory.forStrictBlackOrWhite(2, 0.3f, Color.BLACK, Color.WHITE),                   BNW_LITE_M_FILTER_KEY));
    filters.add(new NamedFilter(SurroundingFilterFactory.forStrictBlackOrWhite(2, 0.4f, Color.BLACK, Color.WHITE),                   BNW_LITE_S_FILTER_KEY));
    filters.add(new NamedFilter(SurroundingFilterFactory.forStrictBlackOrWhite(2, 0.6f, Color.BLACK, Color.WHITE),                   BNW_DARK_S_FILTER_KEY));
    filters.add(new NamedFilter(SurroundingFilterFactory.forStrictBlackOrWhite(2, 0.7f, Color.BLACK, Color.WHITE),                   BNW_DARK_M_FILTER_KEY));
    filters.add(new NamedFilter(SurroundingFilterFactory.forStrictBlackOrWhite(2, 0.8f, Color.BLACK, Color.WHITE),                   BNW_DARK_L_FILTER_KEY));
    filters.add(new NamedFilter(SurroundingFilterFactory.forStrictBlackOrWhite(2, Color.BLACK, Color.RED),                           BNW_BLACK_RED_FILTER_KEY));
    filters.add(new NamedFilter(SurroundingFilterFactory.forStrictBlackOrWhite(2, new Color(100, 32, 145), new Color(255, 255, 50)), BNW_PRPL_YLW_FILTER_KEY));
    filters.add(new NamedFilter(SurroundingFilterFactory.forStrictBlackOrWhite(2, new Color(0, 0, 150), new Color(255, 111, 00)),    BNW_BLUE_ORNG_FILTER_KEY));
    filters.add(new NamedFilter(SurroundingFilterFactory.forStrictBlackOrWhite(2, new Color(0, 50, 0), new Color(200, 255, 200)),    BNW_GREEN_FILTER_KEY));
    filters.add(new NamedFilter(SurroundingFilterFactory.forStrictBlackOrWhite(2, Color.RED, Color.WHITE),                           BNW_RED_WHITE_FILTER_KEY));

    filters.add(new NamedFilter(HsbFilterFactory.forClosestPole(new DistantAttraction(0.2f), 0.083f), ORANGEPHIL_FILTER_KEY));
    filters.add(new NamedFilter(HsbFilterFactory.forClosestPole(new DistantAttraction(0.2f), 0.3f),   GREENPHIL_FILTER_KEY));
    filters.add(new NamedFilter(HsbFilterFactory.forClosestPole(new DistantAttraction(0.2f), 0.7f),   PURPLEPHIL_FILTER_KEY));

    filters.add(new NamedFilter(HsbFilterFactory.forCombinedPole(new LinearRepulsion(0.12f, 0.22f), 0),      REDPHOB_FILTER_KEY));
    filters.add(new NamedFilter(HsbFilterFactory.forCombinedPole(new LinearRepulsion(0.12f, 0.22f), 0.333f), GREENPHOB_FILTER_KEY));
    filters.add(new NamedFilter(HsbFilterFactory.forCombinedPole(new LinearRepulsion(0.12f, 0.22f), 0.667f), BLUEPHOB_FILTER_KEY));
    filters.add(new NamedFilter(HsbFilterFactory.forCombinedPole(new LinearRepulsion(0.12f, 0.22f), 0.167f), YELLOWPHOB_FILTER_KEY));
    filters.add(new NamedFilter(HsbFilterFactory.forCombinedPole(new LinearRepulsion(0.12f, 0.22f), 0.5f),   CYANPHOB_FILTER_KEY));
    filters.add(new NamedFilter(HsbFilterFactory.forCombinedPole(new LinearRepulsion(0.12f, 0.22f), 0.833f), MAGENTPHOB_FILTER_KEY));

    filters.add(new NamedFilter(HsbFilterFactory.forSeamlessRepulsion(0, 0.05f, 0.12f, 0.22f),      SEAM_RED_FILTER_KEY));
    filters.add(new NamedFilter(HsbFilterFactory.forSeamlessRepulsion(0.333f, 0.05f, 0.12f, 0.22f), SEAM_GREEN_FILTER_KEY));
    filters.add(new NamedFilter(HsbFilterFactory.forSeamlessRepulsion(0.667f, 0.05f, 0.12f, 0.22f), SEAM_BLUE_FILTER_KEY));
    filters.add(new NamedFilter(HsbFilterFactory.forSeamlessRepulsion(0.167f, 0.05f, 0.12f, 0.22f), SEAM_YELLOW_FILTER_KEY));
    filters.add(new NamedFilter(HsbFilterFactory.forSeamlessRepulsion(0.5f, 0.05f, 0.12f, 0.22f),   SEAM_CYAN_FILTER_KEY));
    filters.add(new NamedFilter(HsbFilterFactory.forSeamlessRepulsion(0.833f, 0.05f, 0.12f, 0.22f), SEAM_MAGENTA_FILTER_KEY));

    filters.add(new NamedFilter(RgbFilterFactory.forRtoGtoBtoR(),       RGBR_FILTER_KEY));
    filters.add(new NamedFilter(RgbFilterFactory.forBtoGtoRtoB(),       BGRB_FILTER_KEY));
    filters.add(new NamedFilter(RgbFilterFactory.forRedChannelOnly(),   REDONLY_FILTER_KEY));
    filters.add(new NamedFilter(RgbFilterFactory.forGreenChannelOnly(), GREENONLY_FILTER_KEY));
    filters.add(new NamedFilter(RgbFilterFactory.forBlueChannelOnly(),  BLUEONLY_FILTER_KEY));
    filters.add(new NamedFilter(RgbFilterFactory.forRedless(),          REDLESS_FILTER_KEY));
    filters.add(new NamedFilter(RgbFilterFactory.forGreenless(),        GREENLESS_FILTER_KEY));
    filters.add(new NamedFilter(RgbFilterFactory.forBlueless(),         BLUELESS_FILTER_KEY));
    filters.add(new NamedFilter(RgbFilterFactory.forRedAndGreenSwap(),  SWAP_RG_FILTER_KEY));
    filters.add(new NamedFilter(RgbFilterFactory.forGreenAndBlueSwap(), SWAP_GB_FILTER_KEY));
    filters.add(new NamedFilter(RgbFilterFactory.forBlueAndRedSwap(),   SWAP_BR_FILTER_KEY));

    filters.add(new NamedFilter(HsbFilterFactory.forHueReverser(0.083f), REVERSE_ORANG_FILTER_KEY));
    filters.add(new NamedFilter(HsbFilterFactory.forHueReverser(0.417f), REVERSE_TEAL_FILTER_KEY));
    filters.add(new NamedFilter(HsbFilterFactory.forHueReverser(0.750f), REVERSE_VIOLE_FILTER_KEY));

    filters.add(new NamedFilter(RgbFilterFactory.forYellowEqualizer(),  YELLOW_EQ_FILTER_KEY));
    filters.add(new NamedFilter(RgbFilterFactory.forMagentaEqualizer(), MAGENTA_EQ_FILTER_KEY));
    filters.add(new NamedFilter(RgbFilterFactory.forCyanEqualizer(),    CYAN_EQ_FILTER_KEY));

    filters.add(new NamedFilter(SurroundingFilterFactory.forMinMaxChannel(1, false, false, false), MINMAX_BLK_FILTER_KEY));
    filters.add(new NamedFilter(SurroundingFilterFactory.forMinMaxChannel(1, true, false, false),  MINMAX_RED_FILTER_KEY));
    filters.add(new NamedFilter(SurroundingFilterFactory.forMinMaxChannel(1, false, true, false),  MINMAX_GRN_FILTER_KEY));
    filters.add(new NamedFilter(SurroundingFilterFactory.forMinMaxChannel(1, false, false, true),  MINMAX_BLU_FILTER_KEY));
    filters.add(new NamedFilter(SurroundingFilterFactory.forMinMaxChannel(1, false, true, true),   MINMAX_CYA_FILTER_KEY));
    filters.add(new NamedFilter(SurroundingFilterFactory.forMinMaxChannel(1, true, false, true),   MINMAX_MGT_FILTER_KEY));
    filters.add(new NamedFilter(SurroundingFilterFactory.forMinMaxChannel(1, true, true, false),   MINMAX_YLW_FILTER_KEY));
    filters.add(new NamedFilter(SurroundingFilterFactory.forMinMaxChannel(1, true, true, true),    MINMAX_WIT_FILTER_KEY));

    filters.add(new NamedFilter(DyeFilterFactory.forDefaultAlpha(Color.WHITE),   WHITEDYE_FILTER_KEY));
    filters.add(new NamedFilter(DyeFilterFactory.forDefaultAlpha(Color.BLACK),   BLACKDYE_FILTER_KEY));
    filters.add(new NamedFilter(DyeFilterFactory.forDefaultAlpha(Color.RED),     REDDYE_FILTER_KEY));
    filters.add(new NamedFilter(DyeFilterFactory.forDefaultAlpha(Color.GREEN),   GREENDYE_FILTER_KEY));
    filters.add(new NamedFilter(DyeFilterFactory.forDefaultAlpha(Color.BLUE),    BLUEDYE_FILTER_KEY));
    filters.add(new NamedFilter(DyeFilterFactory.forDefaultAlpha(Color.YELLOW),  YELLOWDYE_FILTER_KEY));
    filters.add(new NamedFilter(DyeFilterFactory.forDefaultAlpha(Color.CYAN),    CYANDYE_FILTER_KEY));
    filters.add(new NamedFilter(DyeFilterFactory.forDefaultAlpha(Color.MAGENTA), MAGENTADYE_FILTER_KEY));

    filters.add(new NamedFilter(SpectrumFilterFactory.forDominantHue(0),     DOMINANT_RED_FILTER_KEY));
    filters.add(new NamedFilter(SpectrumFilterFactory.forDominantHue(0.17f), DOMINANT_YLOW_FILTER_KEY));
    filters.add(new NamedFilter(SpectrumFilterFactory.forDominantHue(0.33f), DOMINANT_GREN_FILTER_KEY));
    filters.add(new NamedFilter(SpectrumFilterFactory.forDominantHue(0.5f),  DOMINANT_CYAN_FILTER_KEY));
    filters.add(new NamedFilter(SpectrumFilterFactory.forDominantHue(0.67f), DOMINANT_BLUE_FILTER_KEY));
    filters.add(new NamedFilter(SpectrumFilterFactory.forDominantHue(0.83f), DOMINANT_PRPL_FILTER_KEY));

    filters.add(new NamedFilter(HsbFilterFactory.forHueDependingBrightness(new PositiveFilteringHueFunction(new CosineHueFunction(0, 1, -7))),                                    INC_RED_BR_FILTER_KEY));
    filters.add(new NamedFilter(HsbFilterFactory.forHueDependingBrightness(new MultiplyingHueFactor(new PositiveFilteringHueFunction(new CosineHueFunction(0, 1, -7)), -1)),      DEC_RED_BR_FILTER_KEY));
    filters.add(new NamedFilter(HsbFilterFactory.forHueDependingBrightness(new PositiveFilteringHueFunction(new CosineHueFunction(0.333f, 1, -7))),                               INC_GRN_BR_FILTER_KEY));
    filters.add(new NamedFilter(HsbFilterFactory.forHueDependingBrightness(new MultiplyingHueFactor(new PositiveFilteringHueFunction(new CosineHueFunction(0.333f, 1, -7)), -1)), DEC_GRN_BR_FILTER_KEY));
    filters.add(new NamedFilter(HsbFilterFactory.forHueDependingBrightness(new PositiveFilteringHueFunction(new CosineHueFunction(0.666f, 1, -7))),                               INC_BLU_BR_FILTER_KEY));
    filters.add(new NamedFilter(HsbFilterFactory.forHueDependingBrightness(new MultiplyingHueFactor(new PositiveFilteringHueFunction(new CosineHueFunction(0.666f, 1, -7)), -1)), DEC_BLU_BR_FILTER_KEY));

    filters.add(new NamedFilter(HsbFilterFactory.forHueDependingBrightness(new PitStepHueFunction(0.9f, 0.1f)), HIGHBRED_FILTER_KEY));
    filters.add(new NamedFilter(HsbFilterFactory.forHueDependingBrightness(new PitStepHueFunction(0.1f, 0.3f)), HIGHBYELOW_FILTER_KEY));
    filters.add(new NamedFilter(HsbFilterFactory.forHueDependingBrightness(new PitStepHueFunction(0.3f, 0.5f)), HIGHBGREEN_FILTER_KEY));
    filters.add(new NamedFilter(HsbFilterFactory.forHueDependingBrightness(new PitStepHueFunction(0.5f, 0.7f)), HIGHBBLUE_FILTER_KEY));
    filters.add(new NamedFilter(HsbFilterFactory.forHueDependingBrightness(new PitStepHueFunction(0.7f, 0.9f)), HIGHBPURPL_FILTER_KEY));

    filters.add(new NamedFilter(HsbFilterFactory.forHueDependingSaturation(new FlatStepHueFunction(0.5, 0.95f, 0.05f)),  INC_RED_SA_FILTER_KEY));
    filters.add(new NamedFilter(HsbFilterFactory.forHueDependingSaturation(new FlatStepHueFunction(-0.5, 0.9f, 0.1f)),   DEC_RED_SA_FILTER_KEY));
    filters.add(new NamedFilter(HsbFilterFactory.forHueDependingSaturation(new FlatStepHueFunction(0.5, 0.25f, 0.4f)),   INC_GRN_SA_FILTER_KEY));
    filters.add(new NamedFilter(HsbFilterFactory.forHueDependingSaturation(new FlatStepHueFunction(-0.5, 0.2f, 0.45f)),  DEC_GRN_SA_FILTER_KEY));
    filters.add(new NamedFilter(HsbFilterFactory.forHueDependingSaturation(new FlatStepHueFunction(0.5, 0.5f, 0.7f)),    INC_BLU_SA_FILTER_KEY));
    filters.add(new NamedFilter(HsbFilterFactory.forHueDependingSaturation(new FlatStepHueFunction(-0.5, 0.45f, 0.75f)), DEC_BLU_SA_FILTER_KEY));

    filters.add(new NamedFilter(HsbFilterFactory.forHueDependingSaturation(new PitStepHueFunction(0.9f, 0.1f)), HIGHSRED_FILTER_KEY));
    filters.add(new NamedFilter(HsbFilterFactory.forHueDependingSaturation(new PitStepHueFunction(0.1f, 0.3f)), HIGHSYELOW_FILTER_KEY));
    filters.add(new NamedFilter(HsbFilterFactory.forHueDependingSaturation(new PitStepHueFunction(0.3f, 0.5f)), HIGHSGREEN_FILTER_KEY));
    filters.add(new NamedFilter(HsbFilterFactory.forHueDependingSaturation(new PitStepHueFunction(0.5f, 0.7f)), HIGHSBLUE_FILTER_KEY));
    filters.add(new NamedFilter(HsbFilterFactory.forHueDependingSaturation(new PitStepHueFunction(0.7f, 0.9f)), HIGHSPURPL_FILTER_KEY));

    filters.add(new NamedFilter(HsbFilterFactory.forBrightnessDependingHue(0.33f, 0.66f, 2.5f),  DARK_G_BRIT_B_FILTER_KEY));
    filters.add(new NamedFilter(HsbFilterFactory.forBrightnessDependingHue(-0.33f, 0f, 2.5f),    DARK_B_BRIT_R_FILTER_KEY));
    filters.add(new NamedFilter(HsbFilterFactory.forBrightnessDependingHue(0f, 0.33f, 2.5f),     DARK_R_BRIT_G_FILTER_KEY));
    filters.add(new NamedFilter(HsbFilterFactory.forBrightnessDependingHue(0.5f, 0.83f, 2.5f),   DARK_C_BRIT_M_FILTER_KEY));
    filters.add(new NamedFilter(HsbFilterFactory.forBrightnessDependingHue(-0.17f, 0.16f, 2.5f), DARK_M_BRIT_Y_FILTER_KEY));
    filters.add(new NamedFilter(HsbFilterFactory.forBrightnessDependingHue(0.17f, 0.5f, 2.5f),   DARK_Y_BRIT_C_FILTER_KEY));

    filters.add(new NamedFilter(HsbFilterFactory.forBrightnessDependingHue(0.33f, 1f, 0f),     H_BY_B_B_R_FILTER_KEY));
    filters.add(new NamedFilter(HsbFilterFactory.forBrightnessDependingHue(0.33f, -0.33f, 0f), H_BY_B_R_B_FILTER_KEY));
    filters.add(new NamedFilter(HsbFilterFactory.forBrightnessDependingHue(0f, 0.66f, 0f),     H_BY_B_G_B_FILTER_KEY));
    filters.add(new NamedFilter(HsbFilterFactory.forBrightnessDependingHue(0f, -0.66f, 0f),    H_BY_B_B_G_FILTER_KEY));
    filters.add(new NamedFilter(HsbFilterFactory.forBrightnessDependingHue(0.66f, 1.33f, 0f),  H_BY_B_R_G_FILTER_KEY));
    filters.add(new NamedFilter(HsbFilterFactory.forBrightnessDependingHue(0.66f, 0f, 0f),     H_BY_B_G_R_FILTER_KEY));

    filters.add(new NamedFilter(HsbFilterFactory.forBrightnessDependingHue(-0.5f, 0.5f, 0), PSYSCHED_XS_FILTER_KEY));
    filters.add(new NamedFilter(HsbFilterFactory.forBrightnessDependingHue(-1f, 1f, 0),     PSYSCHED_S_FILTER_KEY));
    filters.add(new NamedFilter(HsbFilterFactory.forBrightnessDependingHue(-1.5f, 1.5f, 0), PSYSCHED_M_FILTER_KEY));
    filters.add(new NamedFilter(HsbFilterFactory.forBrightnessDependingHue(-2f, 2f, 0),     PSYSCHED_L_FILTER_KEY));
    filters.add(new NamedFilter(HsbFilterFactory.forBrightnessDependingHue(-2.5f, 2.5f, 0), PSYSCHED_XL_FILTER_KEY));

    filters.add(new NamedFilter(DistortingFilterFactory.forMagnifier(0.25f, 4f), MAGNIFY_XS_FILTER_KEY));
    filters.add(new NamedFilter(DistortingFilterFactory.forMagnifier(0.25f, 6f), MAGNIFY_S_FILTER_KEY));
    filters.add(new NamedFilter(DistortingFilterFactory.forMagnifier(0.75f, 2f), MAGNIFY_M_FILTER_KEY));
    filters.add(new NamedFilter(DistortingFilterFactory.forMagnifier(0.75f, 4f), MAGNIFY_L_FILTER_KEY));
    filters.add(new NamedFilter(DistortingFilterFactory.forMagnifier(0.75f, 6f), MAGNIFY_XL_FILTER_KEY));

    filters.add(new NamedFilter(DistortingFilterFactory.forReductor(0.5f, 6, true),  REDUCE_XS_FILTER_KEY));
    filters.add(new NamedFilter(DistortingFilterFactory.forReductor(0.5f, 6, false), REDUCE_S_FILTER_KEY));
    filters.add(new NamedFilter(DistortingFilterFactory.forReductor(1, 4, true),     REDUCE_M_FILTER_KEY));
    filters.add(new NamedFilter(DistortingFilterFactory.forReductor(2, 6, true),     REDUCE_L_FILTER_KEY));
    filters.add(new NamedFilter(DistortingFilterFactory.forReductor(4, 6, false),    REDUCE_XL_FILTER_KEY));

    filters.add(new NamedFilter(DistortingFilterFactory.forIsotropicWave(0, 0, 500, 30),             WAVE_HLST_FILTER_KEY));
    filters.add(new NamedFilter(DistortingFilterFactory.forIsotropicWave(0, 0, 500, 60),             WAVE_HLSW_FILTER_KEY));
    filters.add(new NamedFilter(DistortingFilterFactory.forIsotropicWave(0, 0, 300, 20),             WAVE_HLFT_FILTER_KEY));
    filters.add(new NamedFilter(DistortingFilterFactory.forIsotropicWave(0, 0, 300, 35),             WAVE_HLFW_FILTER_KEY));
    filters.add(new NamedFilter(DistortingFilterFactory.forIsotropicWave(0, 1.5708f, 750, 20),       WAVE_HPST_FILTER_KEY));
    filters.add(new NamedFilter(DistortingFilterFactory.forIsotropicWave(0, 1.5708f, 750, 60),       WAVE_HPSW_FILTER_KEY));
    filters.add(new NamedFilter(DistortingFilterFactory.forIsotropicWave(0, 1.5708f, 300, 10),       WAVE_HPFT_FILTER_KEY));
    filters.add(new NamedFilter(DistortingFilterFactory.forIsotropicWave(0, 1.5708f, 300, 35),       WAVE_HPFW_FILTER_KEY));
    filters.add(new NamedFilter(DistortingFilterFactory.forIsotropicWave(1.5708f, 0, 500, 30),       WAVE_VLST_FILTER_KEY));
    filters.add(new NamedFilter(DistortingFilterFactory.forIsotropicWave(1.5708f, 0, 500, 60),       WAVE_VLSW_FILTER_KEY));
    filters.add(new NamedFilter(DistortingFilterFactory.forIsotropicWave(1.5708f, 0, 300, 20),       WAVE_VLFT_FILTER_KEY));
    filters.add(new NamedFilter(DistortingFilterFactory.forIsotropicWave(1.5708f, 0, 300, 35),       WAVE_VLFW_FILTER_KEY));
    filters.add(new NamedFilter(DistortingFilterFactory.forIsotropicWave(1.5708f, 1.5708f, 750, 20), WAVE_VPST_FILTER_KEY));
    filters.add(new NamedFilter(DistortingFilterFactory.forIsotropicWave(1.5708f, 1.5708f, 750, 60), WAVE_VPSW_FILTER_KEY));
    filters.add(new NamedFilter(DistortingFilterFactory.forIsotropicWave(1.5708f, 1.5708f, 300, 10), WAVE_VPFT_FILTER_KEY));
    filters.add(new NamedFilter(DistortingFilterFactory.forIsotropicWave(1.5708f, 1.5708f, 300, 35), WAVE_VPFW_FILTER_KEY));
    filters.add(new NamedFilter(DistortingFilterFactory.forIsotropicWave(0.785f, 0, 450, 45),        WAVE_OBLO_FILTER_KEY));
    filters.add(new NamedFilter(DistortingFilterFactory.forIsotropicWave(-0.785f, 1.5708f, 500, 60), WAVE_OBPE_FILTER_KEY));

    filters.add(new NamedFilter(DistortingFilterFactory.forConstantRotation(-0.1f), TILT_RIGHT_FILTER_KEY));
    filters.add(new NamedFilter(DistortingFilterFactory.forConstantRotation(0.1f),  TILT_LEFT_FILTER_KEY));

    filters.add(new NamedFilter(DistortingFilterFactory.forWhirlpool(0.2f, true),  WHIRL_XS_FILTER_KEY));
    filters.add(new NamedFilter(DistortingFilterFactory.forWhirlpool(0.4f, true),  WHIRL_S_FILTER_KEY));
    filters.add(new NamedFilter(DistortingFilterFactory.forWhirlpool(0.6f, false), WHIRL_M_FILTER_KEY));
    filters.add(new NamedFilter(DistortingFilterFactory.forWhirlpool(1.0f, false), WHIRL_L_FILTER_KEY));
    filters.add(new NamedFilter(DistortingFilterFactory.forWhirlpool(1.5f, false), WHIRL_XL_FILTER_KEY));

    filters.add(new NamedFilter(DistortingFilterFactory.forIncreasingRotation(0.1f, false), TURNING_XS_FILTER_KEY));
    filters.add(new NamedFilter(DistortingFilterFactory.forIncreasingRotation(0.3f, false), TURNING_S_FILTER_KEY));
    filters.add(new NamedFilter(DistortingFilterFactory.forIncreasingRotation(0.4f, true),  TURNING_M_FILTER_KEY));
    filters.add(new NamedFilter(DistortingFilterFactory.forIncreasingRotation(0.7f, true),  TURNING_L_FILTER_KEY));
    filters.add(new NamedFilter(DistortingFilterFactory.forIncreasingRotation(1.2f, true),  TURNING_XL_FILTER_KEY));

    filters.add(new NamedFilter(DistortingFilterFactory.forRoughWaves(), WAVE_ROUGH_FILTER_KEY));

    filters.add(new NamedFilter(PixelizerFilterFactory.forPixelSize(3),  PIXEL_XS_FILTER_KEY));
    filters.add(new NamedFilter(PixelizerFilterFactory.forPixelSize(5),  PIXEL_S_FILTER_KEY));
    filters.add(new NamedFilter(PixelizerFilterFactory.forPixelSize(10), PIXEL_M_FILTER_KEY));
    filters.add(new NamedFilter(PixelizerFilterFactory.forPixelSize(20), PIXEL_L_FILTER_KEY));
    filters.add(new NamedFilter(PixelizerFilterFactory.forPixelSize(50), PIXEL_XL_FILTER_KEY));

    filters.add(new NamedFilter(RgbFilterFactory.forPosterizer(2),  POSTER_XL_FILTER_KEY));
    filters.add(new NamedFilter(RgbFilterFactory.forPosterizer(3),  POSTER_L_FILTER_KEY));
    filters.add(new NamedFilter(RgbFilterFactory.forPosterizer(6),  POSTER_M_FILTER_KEY));
    filters.add(new NamedFilter(RgbFilterFactory.forPosterizer(12), POSTER_S_FILTER_KEY));
    filters.add(new NamedFilter(RgbFilterFactory.forPosterizer(20), POSTER_XS_FILTER_KEY));

    filters.add(new NamedFilter(HsbFilterFactory.forBrightnessPosterizer(2, false),  BPOSTER_XL_FILTER_KEY));
    filters.add(new NamedFilter(HsbFilterFactory.forBrightnessPosterizer(3, false),  BPOSTER_L_FILTER_KEY));
    filters.add(new NamedFilter(HsbFilterFactory.forBrightnessPosterizer(6, false),  BPOSTER_M_FILTER_KEY));
    filters.add(new NamedFilter(HsbFilterFactory.forBrightnessPosterizer(10, false), BPOSTER_S_FILTER_KEY));
    filters.add(new NamedFilter(HsbFilterFactory.forBrightnessPosterizer(20, false), BPOSTER_XS_FILTER_KEY));

    filters.add(new NamedFilter(HsbFilterFactory.forHuePosterizer(3, 0),         HPOSTER_L0_FILTER_KEY));
    filters.add(new NamedFilter(HsbFilterFactory.forHuePosterizer(3, 0.16667f),  HPOSTER_L1_FILTER_KEY));
    filters.add(new NamedFilter(HsbFilterFactory.forHuePosterizer(6, 0),         HPOSTER_M0_FILTER_KEY));
    filters.add(new NamedFilter(HsbFilterFactory.forHuePosterizer(6, 0.083333f), HPOSTER_M1_FILTER_KEY));
    filters.add(new NamedFilter(HsbFilterFactory.forHuePosterizer(12, 0),        HPOSTER_S0_FILTER_KEY));
    filters.add(new NamedFilter(HsbFilterFactory.forHuePosterizer(12, 0.04167f), HPOSTER_S1_FILTER_KEY));

    filters.add(new NamedFilter(HsbFilterFactory.forSaturationPosterizer(2, false), SATPOSTER_FILTER_KEY));

    filters.add(new NamedFilter(RgbFilterFactory.forMaxOnly(2),   MAXONLY_XL_FILTER_KEY));
    filters.add(new NamedFilter(RgbFilterFactory.forMaxOnly(16),  MAXONLY_L_FILTER_KEY));
    filters.add(new NamedFilter(RgbFilterFactory.forMaxOnly(32),  MAXONLY_M_FILTER_KEY));
    filters.add(new NamedFilter(RgbFilterFactory.forMaxOnly(64),  MAXONLY_S_FILTER_KEY));
    filters.add(new NamedFilter(RgbFilterFactory.forMaxOnly(128), MAXONLY_XS_FILTER_KEY));

    filters.add(new NamedFilter(AffineFilterFactory.forHorizontalFlip(), HORIZONTAL_FILTER_KEY));
    filters.add(new NamedFilter(AffineFilterFactory.forVerticalFlip(),   VERTICAL_FILTER_KEY));
    filters.add(new NamedFilter(AffineFilterFactory.forRotation(),       ROTATION_FILTER_KEY));
  }

//==============================================================================

  @Override
  public Collection<NamedFilter> getAllAvailableFilters()
  {
    return Collections.unmodifiableCollection(filters);
  }

//------------------------------------------------------------------------------

}
