
package org.skyllias.alomatia.filter;

import java.awt.Color;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

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
import org.skyllias.alomatia.filter.hsb.CosineHueFunction;
import org.skyllias.alomatia.filter.hsb.FlatStepHueFunction;
import org.skyllias.alomatia.filter.hsb.HsbFilterFactory;
import org.skyllias.alomatia.filter.hsb.MultiplyingHueFactor;
import org.skyllias.alomatia.filter.hsb.PitStepHueFunction;
import org.skyllias.alomatia.filter.hsb.PositiveFilteringHueFunction;
import org.skyllias.alomatia.filter.hsb.pole.DistantAttraction;
import org.skyllias.alomatia.filter.hsb.pole.LinearRepulsion;
import org.skyllias.alomatia.filter.rgb.RgbFilterFactory;
import org.skyllias.alomatia.filter.rgb.lookup.ChannelLookupFilterFactory;

/** FilterFactory with a hardcoded set of available filters. */

public class FixedFilterFactory implements FilterFactory
{
  private static final String NO_FILTER_NAME            = "filter.selector.none";
  private static final String LMSPROPIA_FILTER_NAME     = "filter.dalton.lms.protanopia.name";
  private static final String LMSDEUPIA_FILTER_NAME     = "filter.dalton.lms.deuteranopia.name";
  private static final String LMSTRIPIA_FILTER_NAME     = "filter.dalton.lms.tritanopia.name";
  private static final String XYZPROPIA_FILTER_NAME     = "filter.dalton.xyz.protanopia.name";
  private static final String XYZPROMALY_FILTER_NAME    = "filter.dalton.xyz.protanomaly.name";
  private static final String XYZDEUPIA_FILTER_NAME     = "filter.dalton.xyz.deuteranopia.name";
  private static final String XYZDEUMALY_FILTER_NAME    = "filter.dalton.xyz.deuteranomaly.name";
  private static final String XYZTRIPIA_FILTER_NAME     = "filter.dalton.xyz.tritanopia.name";
  private static final String XYZTRIMALY_FILTER_NAME    = "filter.dalton.xyz.tritanomaly.name";
  private static final String XYZACHSIA_FILTER_NAME     = "filter.dalton.xyz.achromatopsia.name";
  private static final String XYZACHMALY_FILTER_NAME    = "filter.dalton.xyz.achromatomaly.name";
  private static final String RGBR_FILTER_NAME          = "filter.rgb.gbr.name";
  private static final String BGRB_FILTER_NAME          = "filter.rgb.brg.name";
  private static final String SWAP_RG_FILTER_NAME       = "filter.rgb.swap.rg.name";
  private static final String SWAP_GB_FILTER_NAME       = "filter.rgb.swap.gb.name";
  private static final String SWAP_BR_FILTER_NAME       = "filter.rgb.swap.br.name";
  private static final String EQUAL_GREY_FILTER_NAME    = "filter.rgb.greys.equal.name";
  private static final String HUMAN_GREY_FILTER_NAME    = "filter.rgb.greys.human.name";
  private static final String REDONLY_FILTER_NAME       = "filter.rgb.redonly.name";
  private static final String GREENONLY_FILTER_NAME     = "filter.rgb.greenonly.name";
  private static final String BLUEONLY_FILTER_NAME      = "filter.rgb.blueonly.name";
  private static final String REDLESS_FILTER_NAME       = "filter.rgb.redless.name";
  private static final String GREENLESS_FILTER_NAME     = "filter.rgb.greenless.name";
  private static final String BLUELESS_FILTER_NAME      = "filter.rgb.blueless.name";
  private static final String YELLOW_EQ_FILTER_NAME     = "filter.rgb.blue+yellow.name";
  private static final String MAGENTA_EQ_FILTER_NAME    = "filter.rgb.green+magenta.name";
  private static final String CYAN_EQ_FILTER_NAME       = "filter.rgb.red+cyan.name";
  private static final String NEGATIVE_FILTER_NAME      = "filter.rgb.invert.name";
  private static final String DEC_SAT_XL_FILTER_NAME    = "filter.hsb.saturation-xl.name";
  private static final String DEC_SAT_L_FILTER_NAME     = "filter.hsb.saturation-l.name";
  private static final String DEC_SAT_M_FILTER_NAME     = "filter.hsb.saturation-m.name";
  private static final String DEC_SAT_S_FILTER_NAME     = "filter.hsb.saturation-s.name";
  private static final String DEC_SAT_XS_FILTER_NAME    = "filter.hsb.saturation-xs.name";
  private static final String INC_SAT_XL_FILTER_NAME    = "filter.hsb.saturation+xl.name";
  private static final String INC_SAT_L_FILTER_NAME     = "filter.hsb.saturation+l.name";
  private static final String INC_SAT_M_FILTER_NAME     = "filter.hsb.saturation+m.name";
  private static final String INC_SAT_S_FILTER_NAME     = "filter.hsb.saturation+s.name";
  private static final String INC_SAT_XS_FILTER_NAME    = "filter.hsb.saturation+xs.name";
  private static final String DEC_HUE_XL_FILTER_NAME    = "filter.hsb.hue-xl.name";
  private static final String DEC_HUE_L_FILTER_NAME     = "filter.hsb.hue-l.name";
  private static final String DEC_HUE_M_FILTER_NAME     = "filter.hsb.hue-m.name";
  private static final String DEC_HUE_S_FILTER_NAME     = "filter.hsb.hue-s.name";
  private static final String DEC_HUE_XS_FILTER_NAME    = "filter.hsb.hue-xs.name";
  private static final String INC_HUE_XL_FILTER_NAME    = "filter.hsb.hue+xl.name";
  private static final String INC_HUE_L_FILTER_NAME     = "filter.hsb.hue+l.name";
  private static final String INC_HUE_M_FILTER_NAME     = "filter.hsb.hue+m.name";
  private static final String INC_HUE_S_FILTER_NAME     = "filter.hsb.hue+s.name";
  private static final String INC_HUE_XS_FILTER_NAME    = "filter.hsb.hue+xs.name";
  private static final String DEC_BRI_XL_FILTER_NAME    = "filter.hsb.brightness-xl.name";
  private static final String DEC_BRI_L_FILTER_NAME     = "filter.hsb.brightness-l.name";
  private static final String DEC_BRI_M_FILTER_NAME     = "filter.hsb.brightness-m.name";
  private static final String DEC_BRI_S_FILTER_NAME     = "filter.hsb.brightness-s.name";
  private static final String DEC_BRI_XS_FILTER_NAME    = "filter.hsb.brightness-xs.name";
  private static final String INC_BRI_XL_FILTER_NAME    = "filter.hsb.brightness+xl.name";
  private static final String INC_BRI_L_FILTER_NAME     = "filter.hsb.brightness+l.name";
  private static final String INC_BRI_M_FILTER_NAME     = "filter.hsb.brightness+m.name";
  private static final String INC_BRI_S_FILTER_NAME     = "filter.hsb.brightness+s.name";
  private static final String INC_BRI_XS_FILTER_NAME    = "filter.hsb.brightness+xs.name";
  private static final String DEC_CTR_XL_FILTER_NAME    = "filter.hsb.lightcontrast-xl.name";
  private static final String DEC_CTR_L_FILTER_NAME     = "filter.hsb.lightcontrast-l.name";
  private static final String DEC_CTR_M_FILTER_NAME     = "filter.hsb.lightcontrast-m.name";
  private static final String DEC_CTR_S_FILTER_NAME     = "filter.hsb.lightcontrast-s.name";
  private static final String DEC_CTR_XS_FILTER_NAME    = "filter.hsb.lightcontrast-xs.name";
  private static final String INC_CTR_XL_FILTER_NAME    = "filter.hsb.lightcontrast+xl.name";
  private static final String INC_CTR_L_FILTER_NAME     = "filter.hsb.lightcontrast+l.name";
  private static final String INC_CTR_M_FILTER_NAME     = "filter.hsb.lightcontrast+m.name";
  private static final String INC_CTR_S_FILTER_NAME     = "filter.hsb.lightcontrast+s.name";
  private static final String INC_CTR_XS_FILTER_NAME    = "filter.hsb.lightcontrast+xs.name";
  private static final String INC_RED_BR_FILTER_NAME    = "filter.hsb.red.brightness.up.name";
  private static final String DEC_RED_BR_FILTER_NAME    = "filter.hsb.red.brightness.down.name";
  private static final String INC_GRN_BR_FILTER_NAME    = "filter.hsb.green.brightness.up.name";
  private static final String DEC_GRN_BR_FILTER_NAME    = "filter.hsb.green.brightness.down.name";
  private static final String INC_BLU_BR_FILTER_NAME    = "filter.hsb.blue.brightness.up.name";
  private static final String DEC_BLU_BR_FILTER_NAME    = "filter.hsb.blue.brightness.down.name";
  private static final String INC_RED_SA_FILTER_NAME    = "filter.hsb.red.saturation.up.name";
  private static final String DEC_RED_SA_FILTER_NAME    = "filter.hsb.red.saturation.down.name";
  private static final String INC_GRN_SA_FILTER_NAME    = "filter.hsb.green.saturation.up.name";
  private static final String DEC_GRN_SA_FILTER_NAME    = "filter.hsb.green.saturation.down.name";
  private static final String INC_BLU_SA_FILTER_NAME    = "filter.hsb.blue.saturation.up.name";
  private static final String DEC_BLU_SA_FILTER_NAME    = "filter.hsb.blue.saturation.down.name";
  private static final String REVERSE_ORANG_FILTER_NAME = "filter.hsb.hue.reverse.orange-azure.name";
  private static final String REVERSE_TEAL_FILTER_NAME  = "filter.hsb.hue.reverse.teal-crimson.name";
  private static final String REVERSE_VIOLE_FILTER_NAME = "filter.hsb.hue.reverse.violet-chartreuse.name";
  private static final String DEC_CCT_XL_FILTER_NAME    = "filter.rgb.colourcontrast-xl.name";
  private static final String DEC_CCT_L_FILTER_NAME     = "filter.rgb.colourcontrast-l.name";
  private static final String DEC_CCT_M_FILTER_NAME     = "filter.rgb.colourcontrast-m.name";
  private static final String DEC_CCT_S_FILTER_NAME     = "filter.rgb.colourcontrast-s.name";
  private static final String DEC_CCT_XS_FILTER_NAME    = "filter.rgb.colourcontrast-xs.name";
  private static final String INC_CCT_XL_FILTER_NAME    = "filter.rgb.colourcontrast+xl.name";
  private static final String INC_CCT_L_FILTER_NAME     = "filter.rgb.colourcontrast+l.name";
  private static final String INC_CCT_M_FILTER_NAME     = "filter.rgb.colourcontrast+m.name";
  private static final String INC_CCT_S_FILTER_NAME     = "filter.rgb.colourcontrast+s.name";
  private static final String INC_CCT_XS_FILTER_NAME    = "filter.rgb.colourcontrast+xs.name";
  private static final String NAIVE_HDR_FILTER_NAME     = "filter.hdr.naive.standard.name";
  private static final String NAIVE_HDR_B_FILTER_NAME   = "filter.hdr.naive.blurry.name";
  private static final String HUE_DIFFUSION_FILTER_NAME = "filter.hsb.diffusion.hue.name";
  private static final String BNW_PIXEL_FILTER_NAME     = "filter.b&w.pixel.name";
  private static final String BNW_BLOT_FILTER_NAME      = "filter.b&w.blot.name";
  private static final String BNW_SCATT_FILTER_NAME     = "filter.b&w.scatter.name";
  private static final String BNW_SNOW_FILTER_NAME      = "filter.b&w.snow.name";
  private static final String BLUR_SMALL_FILTER_NAME    = "filter.blur.small.name";
  private static final String BLUR_MED_FILTER_NAME      = "filter.blur.medium.name";
  private static final String BLUR_BIG_FILTER_NAME      = "filter.blur.big.name";
  private static final String MOTION_S0_FILTER_NAME     = "filter.blur.motion.slow.horizontal.name";
  private static final String MOTION_S90_FILTER_NAME    = "filter.blur.motion.slow.vertical.name";
  private static final String MOTION_S45_FILTER_NAME    = "filter.blur.motion.slow.oblique.name";
  private static final String MOTION_L0_FILTER_NAME     = "filter.blur.motion.fast.horizontal.name";
  private static final String MOTION_L90_FILTER_NAME    = "filter.blur.motion.fast.vertical.name";
  private static final String MOTION_L45_FILTER_NAME    = "filter.blur.motion.fast.oblique.name";
  private static final String SHARPEN_FILTER_NAME       = "filter.blur.sharpen.name";
  private static final String EDGEDETECT_FILTER_NAME    = "filter.convolve.edgedetection.standard.name";
  private static final String THICKEDGES_FILTER_NAME    = "filter.convolve.edgedetection.thick.s.name";
  private static final String THICKEDGEM_FILTER_NAME    = "filter.convolve.edgedetection.thick.m.name";
  private static final String THICKEDGEL_FILTER_NAME    = "filter.convolve.edgedetection.thick.l.name";
  private static final String NUCELAR_FILTER_NAME       = "filter.convolve.nucelarwash.name";
  private static final String LAYEMBOSS_FILTER_NAME     = "filter.convolve.emboss.layered.name";
  private static final String SMTHEMBOSS_FILTER_NAME    = "filter.convolve.emboss.smooth.name";
  private static final String MEDIAN_XS_FILTER_NAME     = "filter.median.xs.name";
  private static final String MEDIAN_M_FILTER_NAME      = "filter.median.m.name";
  private static final String ORANGEPHIL_FILTER_NAME    = "filter.phile.orange.name";
  private static final String GREENPHIL_FILTER_NAME     = "filter.phile.green.name";
  private static final String PURPLEPHIL_FILTER_NAME    = "filter.phile.purple.name";
  private static final String REDPHOB_FILTER_NAME       = "filter.phobic.red.name";
  private static final String GREENPHOB_FILTER_NAME     = "filter.phobic.green.name";
  private static final String BLUEPHOB_FILTER_NAME      = "filter.phobic.blue.name";
  private static final String YELLOWPHOB_FILTER_NAME    = "filter.phobic.yellow.name";
  private static final String CYANPHOB_FILTER_NAME      = "filter.phobic.cyan.name";
  private static final String MAGENTPHOB_FILTER_NAME    = "filter.phobic.magenta.name";
  private static final String WHITEDYE_FILTER_NAME      = "filter.dye.white.name";
  private static final String BLACKDYE_FILTER_NAME      = "filter.dye.black.name";
  private static final String REDDYE_FILTER_NAME        = "filter.dye.red.name";
  private static final String GREENDYE_FILTER_NAME      = "filter.dye.green.name";
  private static final String BLUEDYE_FILTER_NAME       = "filter.dye.blue.name";
  private static final String YELLOWDYE_FILTER_NAME     = "filter.dye.yellow.name";
  private static final String CYANDYE_FILTER_NAME       = "filter.dye.cyan.name";
  private static final String MAGENTADYE_FILTER_NAME    = "filter.dye.magenta.name";
  private static final String DOMINANT_RED_FILTER_NAME  = "filter.spectrum.hue.dominant.red.name";
  private static final String DOMINANT_YLOW_FILTER_NAME = "filter.spectrum.hue.dominant.yellow.name";
  private static final String DOMINANT_GREN_FILTER_NAME = "filter.spectrum.hue.dominant.green.name";
  private static final String DOMINANT_CYAN_FILTER_NAME = "filter.spectrum.hue.dominant.cyan.name";
  private static final String DOMINANT_BLUE_FILTER_NAME = "filter.spectrum.hue.dominant.blue.name";
  private static final String DOMINANT_PRPL_FILTER_NAME = "filter.spectrum.hue.dominant.purple.name";
  private static final String HORIZONTAL_FILTER_NAME    = "filter.affine.horizontal.name";
  private static final String VERTICAL_FILTER_NAME      = "filter.affine.vertical.name";
  private static final String ROTATION_FILTER_NAME      = "filter.affine.rotation.name";
  private static final String POSTER_XL_FILTER_NAME     = "filter.rgb.posterize.xl.name";
  private static final String POSTER_L_FILTER_NAME      = "filter.rgb.posterize.l.name";
  private static final String POSTER_M_FILTER_NAME      = "filter.rgb.posterize.m.name";
  private static final String POSTER_S_FILTER_NAME      = "filter.rgb.posterize.s.name";
  private static final String POSTER_XS_FILTER_NAME     = "filter.rgb.posterize.xs.name";
  private static final String BPOSTER_XL_FILTER_NAME    = "filter.hsb.posterize.brightness.xl.name";
  private static final String BPOSTER_L_FILTER_NAME     = "filter.hsb.posterize.brightness.l.name";
  private static final String BPOSTER_M_FILTER_NAME     = "filter.hsb.posterize.brightness.m.name";
  private static final String BPOSTER_S_FILTER_NAME     = "filter.hsb.posterize.brightness.s.name";
  private static final String BPOSTER_XS_FILTER_NAME    = "filter.hsb.posterize.brightness.xs.name";
  private static final String SATPOSTER_FILTER_NAME     = "filter.hsb.posterize.saturation.name";
  private static final String HPOSTER_L0_FILTER_NAME    = "filter.hsb.posterize.hue.l0.name";
  private static final String HPOSTER_L1_FILTER_NAME    = "filter.hsb.posterize.hue.l1.name";
  private static final String HPOSTER_M0_FILTER_NAME    = "filter.hsb.posterize.hue.m0.name";
  private static final String HPOSTER_M1_FILTER_NAME    = "filter.hsb.posterize.hue.m1.name";
  private static final String HPOSTER_S0_FILTER_NAME    = "filter.hsb.posterize.hue.s0.name";
  private static final String HPOSTER_S1_FILTER_NAME    = "filter.hsb.posterize.hue.s1.name";
  private static final String MAXONLY_XL_FILTER_NAME    = "filter.rgb.maxonly.xl.name";
  private static final String MAXONLY_L_FILTER_NAME     = "filter.rgb.maxonly.l.name";
  private static final String MAXONLY_M_FILTER_NAME     = "filter.rgb.maxonly.m.name";
  private static final String MAXONLY_S_FILTER_NAME     = "filter.rgb.maxonly.s.name";
  private static final String MAXONLY_XS_FILTER_NAME    = "filter.rgb.maxonly.xs.name";
  private static final String PIXEL_XS_FILTER_NAME      = "filter.pixelize.xs.name";
  private static final String PIXEL_S_FILTER_NAME       = "filter.pixelize.s.name";
  private static final String PIXEL_M_FILTER_NAME       = "filter.pixelize.m.name";
  private static final String PIXEL_L_FILTER_NAME       = "filter.pixelize.l.name";
  private static final String PIXEL_XL_FILTER_NAME      = "filter.pixelize.xl.name";
  private static final String HIGHSRED_FILTER_NAME      = "filter.highlight.saturation.red.name";
  private static final String HIGHSYELOW_FILTER_NAME    = "filter.highlight.saturation.yellow.name";
  private static final String HIGHSGREEN_FILTER_NAME    = "filter.highlight.saturation.green.name";
  private static final String HIGHSBLUE_FILTER_NAME     = "filter.highlight.saturation.blue.name";
  private static final String HIGHSPURPL_FILTER_NAME    = "filter.highlight.saturation.purple.name";
  private static final String HIGHBRED_FILTER_NAME      = "filter.highlight.brightness.red.name";
  private static final String HIGHBYELOW_FILTER_NAME    = "filter.highlight.brightness.yellow.name";
  private static final String HIGHBGREEN_FILTER_NAME    = "filter.highlight.brightness.green.name";
  private static final String HIGHBBLUE_FILTER_NAME     = "filter.highlight.brightness.blue.name";
  private static final String HIGHBPURPL_FILTER_NAME    = "filter.highlight.brightness.purple.name";
  private static final String VIGNETTE_R_FILTER_NAME    = "filter.vignette.round.name";
  private static final String VIGNETTE_C_FILTER_NAME    = "filter.vignette.cross.name";
  private static final String VIGNETTE_E_FILTER_NAME    = "filter.vignette.edges.name";
  private static final String MINMAX_BLK_FILTER_NAME    = "filter.minnmax.black.name";
  private static final String MINMAX_RED_FILTER_NAME    = "filter.minnmax.red.name";
  private static final String MINMAX_GRN_FILTER_NAME    = "filter.minnmax.green.name";
  private static final String MINMAX_BLU_FILTER_NAME    = "filter.minnmax.blue.name";
  private static final String MINMAX_CYA_FILTER_NAME    = "filter.minnmax.cyan.name";
  private static final String MINMAX_MGT_FILTER_NAME    = "filter.minnmax.magenta.name";
  private static final String MINMAX_YLW_FILTER_NAME    = "filter.minnmax.yellow.name";
  private static final String MINMAX_WIT_FILTER_NAME    = "filter.minnmax.white.name";
  private static final String MAGNIFY_XS_FILTER_NAME    = "filter.distort.magnifier.xs.name";
  private static final String MAGNIFY_S_FILTER_NAME     = "filter.distort.magnifier.s.name";
  private static final String MAGNIFY_M_FILTER_NAME     = "filter.distort.magnifier.m.name";
  private static final String MAGNIFY_L_FILTER_NAME     = "filter.distort.magnifier.l.name";
  private static final String MAGNIFY_XL_FILTER_NAME    = "filter.distort.magnifier.xl.name";
  private static final String REDUCE_XS_FILTER_NAME     = "filter.distort.reductor.xs.name";
  private static final String REDUCE_S_FILTER_NAME      = "filter.distort.reductor.s.name";
  private static final String REDUCE_M_FILTER_NAME      = "filter.distort.reductor.m.name";
  private static final String REDUCE_L_FILTER_NAME      = "filter.distort.reductor.l.name";
  private static final String REDUCE_XL_FILTER_NAME     = "filter.distort.reductor.xl.name";
  private static final String WAVE_HLST_FILTER_NAME     = "filter.distort.wave.hor+long+slow+thin.name";
  private static final String WAVE_HLSW_FILTER_NAME     = "filter.distort.wave.hor+long+slow+wide.name";
  private static final String WAVE_HLFT_FILTER_NAME     = "filter.distort.wave.hor+long+fast+thin.name";
  private static final String WAVE_HLFW_FILTER_NAME     = "filter.distort.wave.hor+long+fast+wide.name";
  private static final String WAVE_HPST_FILTER_NAME     = "filter.distort.wave.hor+perp+slow+thin.name";
  private static final String WAVE_HPSW_FILTER_NAME     = "filter.distort.wave.hor+perp+slow+wide.name";
  private static final String WAVE_HPFT_FILTER_NAME     = "filter.distort.wave.hor+perp+fast+thin.name";
  private static final String WAVE_HPFW_FILTER_NAME     = "filter.distort.wave.hor+perp+fast+wide.name";
  private static final String WAVE_VLST_FILTER_NAME     = "filter.distort.wave.ver+long+slow+thin.name";
  private static final String WAVE_VLSW_FILTER_NAME     = "filter.distort.wave.ver+long+slow+wide.name";
  private static final String WAVE_VLFT_FILTER_NAME     = "filter.distort.wave.ver+long+fast+thin.name";
  private static final String WAVE_VLFW_FILTER_NAME     = "filter.distort.wave.ver+long+fast+wide.name";
  private static final String WAVE_VPST_FILTER_NAME     = "filter.distort.wave.ver+perp+slow+thin.name";
  private static final String WAVE_VPSW_FILTER_NAME     = "filter.distort.wave.ver+perp+slow+wide.name";
  private static final String WAVE_VPFT_FILTER_NAME     = "filter.distort.wave.ver+perp+fast+thin.name";
  private static final String WAVE_VPFW_FILTER_NAME     = "filter.distort.wave.ver+perp+fast+wide.name";
  private static final String WAVE_OBLO_FILTER_NAME     = "filter.distort.wave.obl+long.name";
  private static final String WAVE_OBPE_FILTER_NAME     = "filter.distort.wave.obl+perp.name";
  private static final String WAVE_ROUGH_FILTER_NAME    = "filter.distort.wave.rough.name";
  private static final String TILT_RIGHT_FILTER_NAME    = "filter.distort.tilt.right.name";
  private static final String TILT_LEFT_FILTER_NAME     = "filter.distort.tilt.left.name";
  private static final String TURNING_XS_FILTER_NAME    = "filter.distort.turning.xs.name";
  private static final String TURNING_S_FILTER_NAME     = "filter.distort.turning.s.name";
  private static final String TURNING_M_FILTER_NAME     = "filter.distort.turning.m.name";
  private static final String TURNING_L_FILTER_NAME     = "filter.distort.turning.l.name";
  private static final String TURNING_XL_FILTER_NAME    = "filter.distort.turning.xl.name";
  private static final String WHIRL_XS_FILTER_NAME      = "filter.distort.whirlpool.xs.name";
  private static final String WHIRL_S_FILTER_NAME       = "filter.distort.whirlpool.s.name";
  private static final String WHIRL_M_FILTER_NAME       = "filter.distort.whirlpool.m.name";
  private static final String WHIRL_L_FILTER_NAME       = "filter.distort.whirlpool.l.name";
  private static final String WHIRL_XL_FILTER_NAME      = "filter.distort.whirlpool.xl.name";
  private static final String AXE_S_S_FILTER_NAME       = "filter.axe.s+s.name";
  private static final String AXE_S_L_FILTER_NAME       = "filter.axe.s+l.name";
  private static final String AXE_L_S_FILTER_NAME       = "filter.axe.l+s.name";
  private static final String AXE_L_L_FILTER_NAME       = "filter.axe.l+l.name";
  private static final String INST_ALLOPO_FILTER_NAME   = "filter.lookup.allopo.name";
  private static final String INST_EARBY_FILTER_NAME    = "filter.lookup.earbylird.name";
  private static final String INST_THOGAM_FILTER_NAME   = "filter.lookup.thogam.name";
  private static final String INST_VASHNILL_FILTER_NAME = "filter.lookup.vashnille.name";
  private static final String INST_FEHE_FILTER_NAME     = "filter.lookup.fehe.name";
  private static final String INST_MOLO_FILTER_NAME     = "filter.lookup.molo.name";
  private static final String INST_KEVLIN_FILTER_NAME   = "filter.lookup.kevlin.name";
  private static final String INST_1976_FILTER_NAME     = "filter.lookup.1976.name";
  private static final String INST_SOATER_FILTER_NAME   = "filter.lookup.soater.name";
  private static final String INST_LAWDEN_FILTER_NAME   = "filter.lookup.lawden.name";
  private static final String INST_PROX_FILTER_NAME     = "filter.lookup.prox.name";

  private static Collection<NamedFilter> filters = new LinkedList<>();

//==============================================================================

  static
  {
    filters.add(new NamedFilter(null, NO_FILTER_NAME));

    filters.add(new NamedFilter(XyzFilterFactory.forProtanopia(),    XYZPROPIA_FILTER_NAME));
    filters.add(new NamedFilter(XyzFilterFactory.forProtanomaly(),   XYZPROMALY_FILTER_NAME));
    filters.add(new NamedFilter(XyzFilterFactory.forDeuteranopia(),  XYZDEUPIA_FILTER_NAME));
    filters.add(new NamedFilter(XyzFilterFactory.forDeuteranomaly(), XYZDEUMALY_FILTER_NAME));
    filters.add(new NamedFilter(XyzFilterFactory.forTritanopia(),    XYZTRIPIA_FILTER_NAME));
    filters.add(new NamedFilter(XyzFilterFactory.forTritanomaly(),   XYZTRIMALY_FILTER_NAME));
    filters.add(new NamedFilter(XyzFilterFactory.forAchromatopsia(), XYZACHSIA_FILTER_NAME));
    filters.add(new NamedFilter(XyzFilterFactory.forAchromatomaly(), XYZACHMALY_FILTER_NAME));

    filters.add(new NamedFilter(LmsFilterFactory.forProtanopia(),   LMSPROPIA_FILTER_NAME));
    filters.add(new NamedFilter(LmsFilterFactory.forDeuteranopia(), LMSDEUPIA_FILTER_NAME));
    filters.add(new NamedFilter(LmsFilterFactory.forTritanopia(),   LMSTRIPIA_FILTER_NAME));

    filters.add(new NamedFilter(ChannelLookupFilterFactory.forThogam(),    INST_THOGAM_FILTER_NAME));
    filters.add(new NamedFilter(ChannelLookupFilterFactory.forProx(),      INST_PROX_FILTER_NAME));
    filters.add(new NamedFilter(ChannelLookupFilterFactory.forLawden(),    INST_LAWDEN_FILTER_NAME));
    filters.add(new NamedFilter(ChannelLookupFilterFactory.forFehe(),      INST_FEHE_FILTER_NAME));
    filters.add(new NamedFilter(ChannelLookupFilterFactory.forMolo(),      INST_MOLO_FILTER_NAME));
    filters.add(new NamedFilter(ChannelLookupFilterFactory.forAllopo(),    INST_ALLOPO_FILTER_NAME));
    filters.add(new NamedFilter(ChannelLookupFilterFactory.forEarbyLird(), INST_EARBY_FILTER_NAME));
    filters.add(new NamedFilter(ChannelLookupFilterFactory.forSoater(),    INST_SOATER_FILTER_NAME));
    filters.add(new NamedFilter(ChannelLookupFilterFactory.forVashnille(), INST_VASHNILL_FILTER_NAME));
    filters.add(new NamedFilter(ChannelLookupFilterFactory.for1976(),      INST_1976_FILTER_NAME));
    filters.add(new NamedFilter(ChannelLookupFilterFactory.forKevlin(),    INST_KEVLIN_FILTER_NAME));

    filters.add(new NamedFilter(VignetteFilterFactory.forRound(), VIGNETTE_R_FILTER_NAME));
    filters.add(new NamedFilter(VignetteFilterFactory.forCross(), VIGNETTE_C_FILTER_NAME));
    filters.add(new NamedFilter(VignetteFilterFactory.forEdges(), VIGNETTE_E_FILTER_NAME));

    filters.add(new NamedFilter(AxeColoursFilterFactory.forAxeColours(4, 4, new DiagonalMap()), AXE_S_S_FILTER_NAME));
    filters.add(new NamedFilter(AxeColoursFilterFactory.forAxeColours(4, 6, new CrossedMap()),  AXE_S_L_FILTER_NAME));
    filters.add(new NamedFilter(AxeColoursFilterFactory.forAxeColours(7, 4, new AngularMap()),  AXE_L_S_FILTER_NAME));
    filters.add(new NamedFilter(AxeColoursFilterFactory.forAxeColours(7, 6, new RadialMap()),   AXE_L_L_FILTER_NAME));

    filters.add(new NamedFilter(HsbFilterFactory.forHueShift(-0.1f),  DEC_HUE_XL_FILTER_NAME));
    filters.add(new NamedFilter(HsbFilterFactory.forHueShift(-0.05f), DEC_HUE_L_FILTER_NAME));
    filters.add(new NamedFilter(HsbFilterFactory.forHueShift(-0.03f), DEC_HUE_M_FILTER_NAME));
    filters.add(new NamedFilter(HsbFilterFactory.forHueShift(-0.02f), DEC_HUE_S_FILTER_NAME));
    filters.add(new NamedFilter(HsbFilterFactory.forHueShift(-0.01f), DEC_HUE_XS_FILTER_NAME));
    filters.add(new NamedFilter(HsbFilterFactory.forHueShift(0.01f),  INC_HUE_XS_FILTER_NAME));
    filters.add(new NamedFilter(HsbFilterFactory.forHueShift(0.02f),  INC_HUE_S_FILTER_NAME));
    filters.add(new NamedFilter(HsbFilterFactory.forHueShift(0.03f),  INC_HUE_M_FILTER_NAME));
    filters.add(new NamedFilter(HsbFilterFactory.forHueShift(0.05f),  INC_HUE_L_FILTER_NAME));
    filters.add(new NamedFilter(HsbFilterFactory.forHueShift(0.1f),   INC_HUE_XL_FILTER_NAME));

    filters.add(new NamedFilter(HsbFilterFactory.forSaturation(-2),   DEC_SAT_XL_FILTER_NAME));
    filters.add(new NamedFilter(HsbFilterFactory.forSaturation(-1),   DEC_SAT_L_FILTER_NAME));
    filters.add(new NamedFilter(HsbFilterFactory.forSaturation(-0.6), DEC_SAT_M_FILTER_NAME));
    filters.add(new NamedFilter(HsbFilterFactory.forSaturation(-0.3), DEC_SAT_S_FILTER_NAME));
    filters.add(new NamedFilter(HsbFilterFactory.forSaturation(-0.1), DEC_SAT_XS_FILTER_NAME));
    filters.add(new NamedFilter(HsbFilterFactory.forSaturation(0.1),  INC_SAT_XS_FILTER_NAME));
    filters.add(new NamedFilter(HsbFilterFactory.forSaturation(0.3),  INC_SAT_S_FILTER_NAME));
    filters.add(new NamedFilter(HsbFilterFactory.forSaturation(0.6),  INC_SAT_M_FILTER_NAME));
    filters.add(new NamedFilter(HsbFilterFactory.forSaturation(1),    INC_SAT_L_FILTER_NAME));
    filters.add(new NamedFilter(HsbFilterFactory.forSaturation(2),    INC_SAT_XL_FILTER_NAME));

    filters.add(new NamedFilter(HsbFilterFactory.forBrightness(-1.5),  DEC_BRI_XL_FILTER_NAME));
    filters.add(new NamedFilter(HsbFilterFactory.forBrightness(-0.8),  DEC_BRI_L_FILTER_NAME));
    filters.add(new NamedFilter(HsbFilterFactory.forBrightness(-0.4),  DEC_BRI_M_FILTER_NAME));
    filters.add(new NamedFilter(HsbFilterFactory.forBrightness(-0.1),  DEC_BRI_S_FILTER_NAME));
    filters.add(new NamedFilter(HsbFilterFactory.forBrightness(-0.05), DEC_BRI_XS_FILTER_NAME));
    filters.add(new NamedFilter(HsbFilterFactory.forBrightness(0.05),  INC_BRI_XS_FILTER_NAME));
    filters.add(new NamedFilter(HsbFilterFactory.forBrightness(0.1),   INC_BRI_S_FILTER_NAME));
    filters.add(new NamedFilter(HsbFilterFactory.forBrightness(0.4),   INC_BRI_M_FILTER_NAME));
    filters.add(new NamedFilter(HsbFilterFactory.forBrightness(0.8),   INC_BRI_L_FILTER_NAME));
    filters.add(new NamedFilter(HsbFilterFactory.forBrightness(1.5),   INC_BRI_XL_FILTER_NAME));

    filters.add(new NamedFilter(HsbFilterFactory.forContrast(-2),   DEC_CTR_XL_FILTER_NAME));
    filters.add(new NamedFilter(HsbFilterFactory.forContrast(-1),   DEC_CTR_L_FILTER_NAME));
    filters.add(new NamedFilter(HsbFilterFactory.forContrast(-0.6), DEC_CTR_M_FILTER_NAME));
    filters.add(new NamedFilter(HsbFilterFactory.forContrast(-0.3), DEC_CTR_S_FILTER_NAME));
    filters.add(new NamedFilter(HsbFilterFactory.forContrast(-0.1), DEC_CTR_XS_FILTER_NAME));
    filters.add(new NamedFilter(HsbFilterFactory.forContrast(0.1),  INC_CTR_XS_FILTER_NAME));
    filters.add(new NamedFilter(HsbFilterFactory.forContrast(0.3),  INC_CTR_S_FILTER_NAME));
    filters.add(new NamedFilter(HsbFilterFactory.forContrast(0.6),  INC_CTR_M_FILTER_NAME));
    filters.add(new NamedFilter(HsbFilterFactory.forContrast(1),    INC_CTR_L_FILTER_NAME));
    filters.add(new NamedFilter(HsbFilterFactory.forContrast(2),    INC_CTR_XL_FILTER_NAME));

    filters.add(new NamedFilter(RgbFilterFactory.forColourContrast(-2),   DEC_CCT_XL_FILTER_NAME));
    filters.add(new NamedFilter(RgbFilterFactory.forColourContrast(-1),   DEC_CCT_L_FILTER_NAME));
    filters.add(new NamedFilter(RgbFilterFactory.forColourContrast(-0.6), DEC_CCT_M_FILTER_NAME));
    filters.add(new NamedFilter(RgbFilterFactory.forColourContrast(-0.3), DEC_CCT_S_FILTER_NAME));
    filters.add(new NamedFilter(RgbFilterFactory.forColourContrast(-0.1), DEC_CCT_XS_FILTER_NAME));
    filters.add(new NamedFilter(RgbFilterFactory.forColourContrast(0.1),  INC_CCT_XS_FILTER_NAME));
    filters.add(new NamedFilter(RgbFilterFactory.forColourContrast(0.3),  INC_CCT_S_FILTER_NAME));
    filters.add(new NamedFilter(RgbFilterFactory.forColourContrast(0.6),  INC_CCT_M_FILTER_NAME));
    filters.add(new NamedFilter(RgbFilterFactory.forColourContrast(1),    INC_CCT_L_FILTER_NAME));
    filters.add(new NamedFilter(RgbFilterFactory.forColourContrast(2),    INC_CCT_XL_FILTER_NAME));

    filters.add(new NamedFilter(NaiveHdrFilterFactory.forSmallBlur(3),  NAIVE_HDR_FILTER_NAME));
    filters.add(new NamedFilter(NaiveHdrFilterFactory.forSmallBlur(15), NAIVE_HDR_B_FILTER_NAME));

    filters.add(new NamedFilter(SurroundingFilterFactory.forMedian(1), MEDIAN_XS_FILTER_NAME));
    filters.add(new NamedFilter(SurroundingFilterFactory.forMedian(5), MEDIAN_M_FILTER_NAME));

    filters.add(new NamedFilter(BlurFilterFactory.forSharpening(),  SHARPEN_FILTER_NAME));
    filters.add(new NamedFilter(BlurFilterFactory.forParaboloid(5), BLUR_SMALL_FILTER_NAME));
    filters.add(new NamedFilter(BlurFilterFactory.forGaussian(15),  BLUR_MED_FILTER_NAME));
    filters.add(new NamedFilter(BlurFilterFactory.forGaussian(31),  BLUR_BIG_FILTER_NAME));

    filters.add(new NamedFilter(new EdgeConvolvingComposedFilter(new LinearBlurKernelDataFactory(20, 0)),                                        MOTION_S0_FILTER_NAME));
    filters.add(new NamedFilter(new EdgeConvolvingComposedFilter(new LinearBlurKernelDataFactory(20, -Math.PI / 4)),                             MOTION_S45_FILTER_NAME));
    filters.add(new NamedFilter(new EdgeConvolvingComposedFilter(new LinearBlurKernelDataFactory(20, Math.PI / 2)),                              MOTION_S90_FILTER_NAME));
    filters.add(new NamedFilter(new EdgeConvolvingComposedFilter(new LinearBlurKernelDataFactory(30, 0, new SquareBlurLineProfile())),           MOTION_L0_FILTER_NAME));
    filters.add(new NamedFilter(new EdgeConvolvingComposedFilter(new LinearBlurKernelDataFactory(30, Math.PI / 4, new SquareBlurLineProfile())), MOTION_L45_FILTER_NAME));
    filters.add(new NamedFilter(new EdgeConvolvingComposedFilter(new LinearBlurKernelDataFactory(30, Math.PI / 2, new SquareBlurLineProfile())), MOTION_L90_FILTER_NAME));

    filters.add(new NamedFilter(EdgeDetectorFilterFactory.forStandardEdgeDetection(),                 EDGEDETECT_FILTER_NAME));
    filters.add(new NamedFilter(EdgeDetectorFilterFactory.forDrawLikeEdgeDetection(0.5f),             THICKEDGES_FILTER_NAME));
    filters.add(new NamedFilter(EdgeDetectorFilterFactory.forDrawLikeEdgeDetection(1),                THICKEDGEM_FILTER_NAME));
    filters.add(new NamedFilter(EdgeDetectorFilterFactory.forDrawLikeEdgeDetection(2),                THICKEDGEL_FILTER_NAME));
    filters.add(new NamedFilter(new EdgeConvolvingComposedFilter(new NucelarWashKernelDataFactory()), NUCELAR_FILTER_NAME));

    filters.add(new NamedFilter(EmbossFilterFactory.forLayeredEmboss(), LAYEMBOSS_FILTER_NAME));
    filters.add(new NamedFilter(EmbossFilterFactory.forSmoothEmboss(),  SMTHEMBOSS_FILTER_NAME));

    filters.add(new NamedFilter(RgbFilterFactory.forNegative(), NEGATIVE_FILTER_NAME));

    filters.add(new NamedFilter(RgbFilterFactory.forEqualGreyScale(),          EQUAL_GREY_FILTER_NAME));
    filters.add(new NamedFilter(RgbFilterFactory.forHumanSensitiveGreyScale(), HUMAN_GREY_FILTER_NAME));

    filters.add(new NamedFilter(DiffusionFilterFactory.forHueDiffusion(31), HUE_DIFFUSION_FILTER_NAME));

    filters.add(new NamedFilter(SurroundingFilterFactory.forStrictBlackOrWhite(0),        BNW_PIXEL_FILTER_NAME));
    filters.add(new NamedFilter(SurroundingFilterFactory.forStrictBlackOrWhite(2),        BNW_BLOT_FILTER_NAME));
    filters.add(new NamedFilter(SurroundingFilterFactory.forProbabilisticBlackOrWhite(1), BNW_SCATT_FILTER_NAME));
    filters.add(new NamedFilter(SurroundingFilterFactory.forProbabilisticBlackOrWhite(3), BNW_SNOW_FILTER_NAME));

    filters.add(new NamedFilter(HsbFilterFactory.forClosestPole(new DistantAttraction(0.2f), new Color(255, 128, 0)), ORANGEPHIL_FILTER_NAME));
    filters.add(new NamedFilter(HsbFilterFactory.forClosestPole(new DistantAttraction(0.2f), new Color(43, 255, 0)),  GREENPHIL_FILTER_NAME));
    filters.add(new NamedFilter(HsbFilterFactory.forClosestPole(new DistantAttraction(0.2f), new Color(149, 0, 255)), PURPLEPHIL_FILTER_NAME));

    filters.add(new NamedFilter(HsbFilterFactory.forCombinedPole(new LinearRepulsion(0.12f, 0.22f), new Color(255, 0, 0)),   REDPHOB_FILTER_NAME));
    filters.add(new NamedFilter(HsbFilterFactory.forCombinedPole(new LinearRepulsion(0.12f, 0.22f), new Color(0, 255, 0)),   GREENPHOB_FILTER_NAME));
    filters.add(new NamedFilter(HsbFilterFactory.forCombinedPole(new LinearRepulsion(0.12f, 0.22f), new Color(0, 0, 255)),   BLUEPHOB_FILTER_NAME));
    filters.add(new NamedFilter(HsbFilterFactory.forCombinedPole(new LinearRepulsion(0.12f, 0.22f), new Color(255, 255, 0)), YELLOWPHOB_FILTER_NAME));
    filters.add(new NamedFilter(HsbFilterFactory.forCombinedPole(new LinearRepulsion(0.12f, 0.22f), new Color(0, 255, 255)), CYANPHOB_FILTER_NAME));
    filters.add(new NamedFilter(HsbFilterFactory.forCombinedPole(new LinearRepulsion(0.12f, 0.22f), new Color(255, 0, 255)), MAGENTPHOB_FILTER_NAME));

    filters.add(new NamedFilter(RgbFilterFactory.forRtoGtoBtoR(),       RGBR_FILTER_NAME));
    filters.add(new NamedFilter(RgbFilterFactory.forBtoGtoRtoB(),       BGRB_FILTER_NAME));
    filters.add(new NamedFilter(RgbFilterFactory.forRedChannelOnly(),   REDONLY_FILTER_NAME));
    filters.add(new NamedFilter(RgbFilterFactory.forGreenChannelOnly(), GREENONLY_FILTER_NAME));
    filters.add(new NamedFilter(RgbFilterFactory.forBlueChannelOnly(),  BLUEONLY_FILTER_NAME));
    filters.add(new NamedFilter(RgbFilterFactory.forRedless(),          REDLESS_FILTER_NAME));
    filters.add(new NamedFilter(RgbFilterFactory.forGreenless(),        GREENLESS_FILTER_NAME));
    filters.add(new NamedFilter(RgbFilterFactory.forBlueless(),         BLUELESS_FILTER_NAME));
    filters.add(new NamedFilter(RgbFilterFactory.forRedAndGreenSwap(),  SWAP_RG_FILTER_NAME));
    filters.add(new NamedFilter(RgbFilterFactory.forGreenAndBlueSwap(), SWAP_GB_FILTER_NAME));
    filters.add(new NamedFilter(RgbFilterFactory.forBlueAndRedSwap(),   SWAP_BR_FILTER_NAME));

    filters.add(new NamedFilter(HsbFilterFactory.forHueReverser(0.083f), REVERSE_ORANG_FILTER_NAME));
    filters.add(new NamedFilter(HsbFilterFactory.forHueReverser(0.417f), REVERSE_TEAL_FILTER_NAME));
    filters.add(new NamedFilter(HsbFilterFactory.forHueReverser(0.750f), REVERSE_VIOLE_FILTER_NAME));

    filters.add(new NamedFilter(RgbFilterFactory.forYellowEqualizer(),  YELLOW_EQ_FILTER_NAME));
    filters.add(new NamedFilter(RgbFilterFactory.forMagentaEqualizer(), MAGENTA_EQ_FILTER_NAME));
    filters.add(new NamedFilter(RgbFilterFactory.forCyanEqualizer(),    CYAN_EQ_FILTER_NAME));

    filters.add(new NamedFilter(SurroundingFilterFactory.forMinMaxChannel(1, false, false, false), MINMAX_BLK_FILTER_NAME));
    filters.add(new NamedFilter(SurroundingFilterFactory.forMinMaxChannel(1, true, false, false),  MINMAX_RED_FILTER_NAME));
    filters.add(new NamedFilter(SurroundingFilterFactory.forMinMaxChannel(1, false, true, false),  MINMAX_GRN_FILTER_NAME));
    filters.add(new NamedFilter(SurroundingFilterFactory.forMinMaxChannel(1, false, false, true),  MINMAX_BLU_FILTER_NAME));
    filters.add(new NamedFilter(SurroundingFilterFactory.forMinMaxChannel(1, false, true, true),   MINMAX_CYA_FILTER_NAME));
    filters.add(new NamedFilter(SurroundingFilterFactory.forMinMaxChannel(1, true, false, true),   MINMAX_MGT_FILTER_NAME));
    filters.add(new NamedFilter(SurroundingFilterFactory.forMinMaxChannel(1, true, true, false),   MINMAX_YLW_FILTER_NAME));
    filters.add(new NamedFilter(SurroundingFilterFactory.forMinMaxChannel(1, true, true, true),    MINMAX_WIT_FILTER_NAME));

    filters.add(new NamedFilter(DyeFilterFactory.forDefaultAlpha(Color.WHITE),   WHITEDYE_FILTER_NAME));
    filters.add(new NamedFilter(DyeFilterFactory.forDefaultAlpha(Color.BLACK),   BLACKDYE_FILTER_NAME));
    filters.add(new NamedFilter(DyeFilterFactory.forDefaultAlpha(Color.RED),     REDDYE_FILTER_NAME));
    filters.add(new NamedFilter(DyeFilterFactory.forDefaultAlpha(Color.GREEN),   GREENDYE_FILTER_NAME));
    filters.add(new NamedFilter(DyeFilterFactory.forDefaultAlpha(Color.BLUE),    BLUEDYE_FILTER_NAME));
    filters.add(new NamedFilter(DyeFilterFactory.forDefaultAlpha(Color.YELLOW),  YELLOWDYE_FILTER_NAME));
    filters.add(new NamedFilter(DyeFilterFactory.forDefaultAlpha(Color.CYAN),    CYANDYE_FILTER_NAME));
    filters.add(new NamedFilter(DyeFilterFactory.forDefaultAlpha(Color.MAGENTA), MAGENTADYE_FILTER_NAME));

    filters.add(new NamedFilter(SpectrumFilterFactory.forDominantHue(0),     DOMINANT_RED_FILTER_NAME));
    filters.add(new NamedFilter(SpectrumFilterFactory.forDominantHue(0.17f), DOMINANT_YLOW_FILTER_NAME));
    filters.add(new NamedFilter(SpectrumFilterFactory.forDominantHue(0.33f), DOMINANT_GREN_FILTER_NAME));
    filters.add(new NamedFilter(SpectrumFilterFactory.forDominantHue(0.5f),  DOMINANT_CYAN_FILTER_NAME));
    filters.add(new NamedFilter(SpectrumFilterFactory.forDominantHue(0.67f), DOMINANT_BLUE_FILTER_NAME));
    filters.add(new NamedFilter(SpectrumFilterFactory.forDominantHue(0.83f), DOMINANT_PRPL_FILTER_NAME));

    filters.add(new NamedFilter(HsbFilterFactory.forHueDependingBrightness(new PositiveFilteringHueFunction(new CosineHueFunction(0, 1, -7))),                                    INC_RED_BR_FILTER_NAME));
    filters.add(new NamedFilter(HsbFilterFactory.forHueDependingBrightness(new MultiplyingHueFactor(new PositiveFilteringHueFunction(new CosineHueFunction(0, 1, -7)), -1)),      DEC_RED_BR_FILTER_NAME));
    filters.add(new NamedFilter(HsbFilterFactory.forHueDependingBrightness(new PositiveFilteringHueFunction(new CosineHueFunction(0.333f, 1, -7))),                               INC_GRN_BR_FILTER_NAME));
    filters.add(new NamedFilter(HsbFilterFactory.forHueDependingBrightness(new MultiplyingHueFactor(new PositiveFilteringHueFunction(new CosineHueFunction(0.333f, 1, -7)), -1)), DEC_GRN_BR_FILTER_NAME));
    filters.add(new NamedFilter(HsbFilterFactory.forHueDependingBrightness(new PositiveFilteringHueFunction(new CosineHueFunction(0.666f, 1, -7))),                               INC_BLU_BR_FILTER_NAME));
    filters.add(new NamedFilter(HsbFilterFactory.forHueDependingBrightness(new MultiplyingHueFactor(new PositiveFilteringHueFunction(new CosineHueFunction(0.666f, 1, -7)), -1)), DEC_BLU_BR_FILTER_NAME));

    filters.add(new NamedFilter(HsbFilterFactory.forHueDependingBrightness(new PitStepHueFunction(0.9f, 0.1f)), HIGHBRED_FILTER_NAME));
    filters.add(new NamedFilter(HsbFilterFactory.forHueDependingBrightness(new PitStepHueFunction(0.1f, 0.3f)), HIGHBYELOW_FILTER_NAME));
    filters.add(new NamedFilter(HsbFilterFactory.forHueDependingBrightness(new PitStepHueFunction(0.3f, 0.5f)), HIGHBGREEN_FILTER_NAME));
    filters.add(new NamedFilter(HsbFilterFactory.forHueDependingBrightness(new PitStepHueFunction(0.5f, 0.7f)), HIGHBBLUE_FILTER_NAME));
    filters.add(new NamedFilter(HsbFilterFactory.forHueDependingBrightness(new PitStepHueFunction(0.7f, 0.9f)), HIGHBPURPL_FILTER_NAME));

    filters.add(new NamedFilter(HsbFilterFactory.forHueDependingSaturation(new FlatStepHueFunction(0.5, 0.95f, 0.05f)),  INC_RED_SA_FILTER_NAME));
    filters.add(new NamedFilter(HsbFilterFactory.forHueDependingSaturation(new FlatStepHueFunction(-0.5, 0.9f, 0.1f)),   DEC_RED_SA_FILTER_NAME));
    filters.add(new NamedFilter(HsbFilterFactory.forHueDependingSaturation(new FlatStepHueFunction(0.5, 0.25f, 0.4f)),   INC_GRN_SA_FILTER_NAME));
    filters.add(new NamedFilter(HsbFilterFactory.forHueDependingSaturation(new FlatStepHueFunction(-0.5, 0.2f, 0.45f)),  DEC_GRN_SA_FILTER_NAME));
    filters.add(new NamedFilter(HsbFilterFactory.forHueDependingSaturation(new FlatStepHueFunction(0.5, 0.5f, 0.7f)),    INC_BLU_SA_FILTER_NAME));
    filters.add(new NamedFilter(HsbFilterFactory.forHueDependingSaturation(new FlatStepHueFunction(-0.5, 0.45f, 0.75f)), DEC_BLU_SA_FILTER_NAME));

    filters.add(new NamedFilter(HsbFilterFactory.forHueDependingSaturation(new PitStepHueFunction(0.9f, 0.1f)), HIGHSRED_FILTER_NAME));
    filters.add(new NamedFilter(HsbFilterFactory.forHueDependingSaturation(new PitStepHueFunction(0.1f, 0.3f)), HIGHSYELOW_FILTER_NAME));
    filters.add(new NamedFilter(HsbFilterFactory.forHueDependingSaturation(new PitStepHueFunction(0.3f, 0.5f)), HIGHSGREEN_FILTER_NAME));
    filters.add(new NamedFilter(HsbFilterFactory.forHueDependingSaturation(new PitStepHueFunction(0.5f, 0.7f)), HIGHSBLUE_FILTER_NAME));
    filters.add(new NamedFilter(HsbFilterFactory.forHueDependingSaturation(new PitStepHueFunction(0.7f, 0.9f)), HIGHSPURPL_FILTER_NAME));

    filters.add(new NamedFilter(DistortingFilterFactory.forMagnifier(0.25f, 4f), MAGNIFY_XS_FILTER_NAME));
    filters.add(new NamedFilter(DistortingFilterFactory.forMagnifier(0.25f, 6f), MAGNIFY_S_FILTER_NAME));
    filters.add(new NamedFilter(DistortingFilterFactory.forMagnifier(0.75f, 2f), MAGNIFY_M_FILTER_NAME));
    filters.add(new NamedFilter(DistortingFilterFactory.forMagnifier(0.75f, 4f), MAGNIFY_L_FILTER_NAME));
    filters.add(new NamedFilter(DistortingFilterFactory.forMagnifier(0.75f, 6f), MAGNIFY_XL_FILTER_NAME));

    filters.add(new NamedFilter(DistortingFilterFactory.forReductor(0.5f, 6, true),  REDUCE_XS_FILTER_NAME));
    filters.add(new NamedFilter(DistortingFilterFactory.forReductor(0.5f, 6, false), REDUCE_S_FILTER_NAME));
    filters.add(new NamedFilter(DistortingFilterFactory.forReductor(1, 4, true),     REDUCE_M_FILTER_NAME));
    filters.add(new NamedFilter(DistortingFilterFactory.forReductor(2, 6, true),     REDUCE_L_FILTER_NAME));
    filters.add(new NamedFilter(DistortingFilterFactory.forReductor(4, 6, false),    REDUCE_XL_FILTER_NAME));

    filters.add(new NamedFilter(DistortingFilterFactory.forIsotropicWave(0, 0, 500, 30),             WAVE_HLST_FILTER_NAME));
    filters.add(new NamedFilter(DistortingFilterFactory.forIsotropicWave(0, 0, 500, 60),             WAVE_HLSW_FILTER_NAME));
    filters.add(new NamedFilter(DistortingFilterFactory.forIsotropicWave(0, 0, 300, 20),             WAVE_HLFT_FILTER_NAME));
    filters.add(new NamedFilter(DistortingFilterFactory.forIsotropicWave(0, 0, 300, 35),             WAVE_HLFW_FILTER_NAME));
    filters.add(new NamedFilter(DistortingFilterFactory.forIsotropicWave(0, 1.5708f, 750, 20),       WAVE_HPST_FILTER_NAME));
    filters.add(new NamedFilter(DistortingFilterFactory.forIsotropicWave(0, 1.5708f, 750, 60),       WAVE_HPSW_FILTER_NAME));
    filters.add(new NamedFilter(DistortingFilterFactory.forIsotropicWave(0, 1.5708f, 300, 10),       WAVE_HPFT_FILTER_NAME));
    filters.add(new NamedFilter(DistortingFilterFactory.forIsotropicWave(0, 1.5708f, 300, 35),       WAVE_HPFW_FILTER_NAME));
    filters.add(new NamedFilter(DistortingFilterFactory.forIsotropicWave(1.5708f, 0, 500, 30),       WAVE_VLST_FILTER_NAME));
    filters.add(new NamedFilter(DistortingFilterFactory.forIsotropicWave(1.5708f, 0, 500, 60),       WAVE_VLSW_FILTER_NAME));
    filters.add(new NamedFilter(DistortingFilterFactory.forIsotropicWave(1.5708f, 0, 300, 20),       WAVE_VLFT_FILTER_NAME));
    filters.add(new NamedFilter(DistortingFilterFactory.forIsotropicWave(1.5708f, 0, 300, 35),       WAVE_VLFW_FILTER_NAME));
    filters.add(new NamedFilter(DistortingFilterFactory.forIsotropicWave(1.5708f, 1.5708f, 750, 20), WAVE_VPST_FILTER_NAME));
    filters.add(new NamedFilter(DistortingFilterFactory.forIsotropicWave(1.5708f, 1.5708f, 750, 60), WAVE_VPSW_FILTER_NAME));
    filters.add(new NamedFilter(DistortingFilterFactory.forIsotropicWave(1.5708f, 1.5708f, 300, 10), WAVE_VPFT_FILTER_NAME));
    filters.add(new NamedFilter(DistortingFilterFactory.forIsotropicWave(1.5708f, 1.5708f, 300, 35), WAVE_VPFW_FILTER_NAME));
    filters.add(new NamedFilter(DistortingFilterFactory.forIsotropicWave(0.785f, 0, 450, 45),        WAVE_OBLO_FILTER_NAME));
    filters.add(new NamedFilter(DistortingFilterFactory.forIsotropicWave(-0.785f, 1.5708f, 500, 60), WAVE_OBPE_FILTER_NAME));

    filters.add(new NamedFilter(DistortingFilterFactory.forConstantRotation(-0.1f), TILT_RIGHT_FILTER_NAME));
    filters.add(new NamedFilter(DistortingFilterFactory.forConstantRotation(0.1f),  TILT_LEFT_FILTER_NAME));

    filters.add(new NamedFilter(DistortingFilterFactory.forWhirlpool(0.2f, true),  WHIRL_XS_FILTER_NAME));
    filters.add(new NamedFilter(DistortingFilterFactory.forWhirlpool(0.4f, true),  WHIRL_S_FILTER_NAME));
    filters.add(new NamedFilter(DistortingFilterFactory.forWhirlpool(0.6f, false), WHIRL_M_FILTER_NAME));
    filters.add(new NamedFilter(DistortingFilterFactory.forWhirlpool(1.0f, false), WHIRL_L_FILTER_NAME));
    filters.add(new NamedFilter(DistortingFilterFactory.forWhirlpool(1.5f, false), WHIRL_XL_FILTER_NAME));

    filters.add(new NamedFilter(DistortingFilterFactory.forIncreasingRotation(0.1f, false), TURNING_XS_FILTER_NAME));
    filters.add(new NamedFilter(DistortingFilterFactory.forIncreasingRotation(0.3f, false), TURNING_S_FILTER_NAME));
    filters.add(new NamedFilter(DistortingFilterFactory.forIncreasingRotation(0.4f, true),  TURNING_M_FILTER_NAME));
    filters.add(new NamedFilter(DistortingFilterFactory.forIncreasingRotation(0.7f, true),  TURNING_L_FILTER_NAME));
    filters.add(new NamedFilter(DistortingFilterFactory.forIncreasingRotation(1.2f, true),  TURNING_XL_FILTER_NAME));

    filters.add(new NamedFilter(DistortingFilterFactory.forRoughWaves(), WAVE_ROUGH_FILTER_NAME));

    filters.add(new NamedFilter(PixelizerFilterFactory.forPixelSize(3),  PIXEL_XS_FILTER_NAME));
    filters.add(new NamedFilter(PixelizerFilterFactory.forPixelSize(5),  PIXEL_S_FILTER_NAME));
    filters.add(new NamedFilter(PixelizerFilterFactory.forPixelSize(10), PIXEL_M_FILTER_NAME));
    filters.add(new NamedFilter(PixelizerFilterFactory.forPixelSize(20), PIXEL_L_FILTER_NAME));
    filters.add(new NamedFilter(PixelizerFilterFactory.forPixelSize(50), PIXEL_XL_FILTER_NAME));

    filters.add(new NamedFilter(RgbFilterFactory.forPosterizer(2),  POSTER_XL_FILTER_NAME));
    filters.add(new NamedFilter(RgbFilterFactory.forPosterizer(3),  POSTER_L_FILTER_NAME));
    filters.add(new NamedFilter(RgbFilterFactory.forPosterizer(6),  POSTER_M_FILTER_NAME));
    filters.add(new NamedFilter(RgbFilterFactory.forPosterizer(12), POSTER_S_FILTER_NAME));
    filters.add(new NamedFilter(RgbFilterFactory.forPosterizer(20), POSTER_XS_FILTER_NAME));

    filters.add(new NamedFilter(HsbFilterFactory.forBrightnessPosterizer(2, false),  BPOSTER_XL_FILTER_NAME));
    filters.add(new NamedFilter(HsbFilterFactory.forBrightnessPosterizer(3, false),  BPOSTER_L_FILTER_NAME));
    filters.add(new NamedFilter(HsbFilterFactory.forBrightnessPosterizer(6, false),  BPOSTER_M_FILTER_NAME));
    filters.add(new NamedFilter(HsbFilterFactory.forBrightnessPosterizer(10, false), BPOSTER_S_FILTER_NAME));
    filters.add(new NamedFilter(HsbFilterFactory.forBrightnessPosterizer(20, false), BPOSTER_XS_FILTER_NAME));

    filters.add(new NamedFilter(HsbFilterFactory.forHuePosterizer(3, 0),         HPOSTER_L0_FILTER_NAME));
    filters.add(new NamedFilter(HsbFilterFactory.forHuePosterizer(3, 0.16667f),  HPOSTER_L1_FILTER_NAME));
    filters.add(new NamedFilter(HsbFilterFactory.forHuePosterizer(6, 0),         HPOSTER_M0_FILTER_NAME));
    filters.add(new NamedFilter(HsbFilterFactory.forHuePosterizer(6, 0.083333f), HPOSTER_M1_FILTER_NAME));
    filters.add(new NamedFilter(HsbFilterFactory.forHuePosterizer(12, 0),        HPOSTER_S0_FILTER_NAME));
    filters.add(new NamedFilter(HsbFilterFactory.forHuePosterizer(12, 0.04167f), HPOSTER_S1_FILTER_NAME));

    filters.add(new NamedFilter(HsbFilterFactory.forSaturationPosterizer(2, false), SATPOSTER_FILTER_NAME));

    filters.add(new NamedFilter(RgbFilterFactory.forMaxOnly(2),   MAXONLY_XL_FILTER_NAME));
    filters.add(new NamedFilter(RgbFilterFactory.forMaxOnly(16),  MAXONLY_L_FILTER_NAME));
    filters.add(new NamedFilter(RgbFilterFactory.forMaxOnly(32),  MAXONLY_M_FILTER_NAME));
    filters.add(new NamedFilter(RgbFilterFactory.forMaxOnly(64),  MAXONLY_S_FILTER_NAME));
    filters.add(new NamedFilter(RgbFilterFactory.forMaxOnly(128), MAXONLY_XS_FILTER_NAME));

    filters.add(new NamedFilter(AffineFilterFactory.forHorizontalFlip(), HORIZONTAL_FILTER_NAME));
    filters.add(new NamedFilter(AffineFilterFactory.forVerticalFlip(),   VERTICAL_FILTER_NAME));
    filters.add(new NamedFilter(AffineFilterFactory.forRotation(),       ROTATION_FILTER_NAME));
  }

//==============================================================================

  @Override
  public Collection<NamedFilter> getAllAvailableFilters()
  {
    return Collections.unmodifiableCollection(filters);
  }

//------------------------------------------------------------------------------

}
