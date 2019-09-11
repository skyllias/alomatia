
package org.skyllias.alomatia.ui;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.commons.lang.StringUtils;
import org.skyllias.alomatia.i18n.LabelLocalizer;

/** {@link ImageSaver} that stores images in files.
 *  The format is initially fixed to PNG.
 *  By default, the user's home is used as output directory, but the path can be
 *  modified externally. Whether the user is prompted when saving interactively
 *  a file can also be set externally. */

public class FileImageSaver implements ImageSaver
{
  private static final String USER_HOME_PROP = "user.home";
  private static final String IMAGE_FORMAT   = "PNG";

  private static final String ERROR_TITLE_LABEL = "save.error.title";
  private static final String ERROR_DESCR_LABEL = "save.error.description";

  private LabelLocalizer labelLocalizer;

  private File destinationDir   = new File(System.getProperty(USER_HOME_PROP));
  private boolean promptForFile = true;
  private int discriminator     = 0;                                            // sequential number used to avoid collisions when multiple images are saved at the same time

//==============================================================================

  public FileImageSaver(LabelLocalizer localizer) {labelLocalizer = localizer;}

//==============================================================================

  /** Modifies the directory where files are created by default. */

  public void setDestinationDir(File dir) {destinationDir = dir;}

//------------------------------------------------------------------------------

  /** Modifies the behaviour when interactive saves are requested. If true,
   *  a file chooser will apeear so that the user chooses where to save the
   *  image; if false, a file will be created automatically. */

  public void setPrompt(boolean prompt) {promptForFile = prompt;}

//------------------------------------------------------------------------------

  /** Files are overwritten.
   *  I/O errors are always notified by means of dialogs. */

  @Override
  public void save(Image image, String nameHint, boolean silently)
  {
    File destinationFile = getDestinationFile(nameHint, silently);
    if (destinationFile != null)
    {
      try
      {
        RenderedImage renderedImage = getRenderedImage(image);
        ImageIO.write(renderedImage, IMAGE_FORMAT, destinationFile);
      }
      catch (Exception e)                                                       // unfortunately, the IOException hierarchy does not allow for surefire distinction of causes, so a generic message has to be used
      {
        JOptionPane.showMessageDialog(null, labelLocalizer.getString(ERROR_DESCR_LABEL),
                                      labelLocalizer.getString(ERROR_TITLE_LABEL),
                                      JOptionPane.ERROR_MESSAGE);
      }                                                                         // TODO log it
    }
  }

//------------------------------------------------------------------------------

  /* Returns the file where the image is to be saved.
   * If null, the file should not be saved. */

  private File getDestinationFile(String nameHint, boolean silently)
  {
    final String EXTENSION = ".png";

    if (!silently && promptForFile) return getChosenFile(EXTENSION);
    else                            return getAutomaticFile(nameHint, EXTENSION);
  }

//------------------------------------------------------------------------------

  /* Returns a file in destinationDir containing the hint in its probably unique name. */

  private File getAutomaticFile(String nameHint, String extension)
  {
    final char[] FORBIDDEN_CHARS     = "/\\|\"<>:;?*".toCharArray();
    final String AUTO_NAME_PATTERN   = "{0}-{1}-{2}" + extension;               // date, discriminator, hint
    final String DATE_NAME_PATTERN   = "yyyyMMddHHmmss";
    final String NUMBER_NAME_PATTERN = "000";                                   // if there are more than 1000 files saved, extra chars will be added automatically

    for (char forbiddenChar : FORBIDDEN_CHARS) nameHint = StringUtils.remove(nameHint, forbiddenChar);    // not efficient at all, but this will hardly ever be critical

    String dateNameFragment          = new SimpleDateFormat(DATE_NAME_PATTERN).format(new Date());
    String discriminatorNameFragment = new DecimalFormat(NUMBER_NAME_PATTERN).format(discriminator++);
    String defaultFileName           = MessageFormat.format(AUTO_NAME_PATTERN, dateNameFragment,
                                                            discriminatorNameFragment, nameHint);
    return new File(destinationDir, defaultFileName);
  }

//------------------------------------------------------------------------------

  /* Opens a file chooser in destinationDir restricted to files with the passed
   * extension and returns the selected file, or null if the operation is cancelled.
   * The extension is added if the file does not exist and its name does not end
   * with it. */

  private File getChosenFile(String extension)
  {
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setCurrentDirectory(destinationDir);
    fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    fileChooser.setMultiSelectionEnabled(false);
    fileChooser.setFileFilter(new FileNameExtensionFilter(extension, IMAGE_FORMAT));
    if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION)        // this makes it completely modal
    {
      File selectedFile = fileChooser.getSelectedFile();
      if (selectedFile.exists()) return selectedFile;

      String fileName = selectedFile.getName();
      if (StringUtils.endsWith(fileName, extension)) return selectedFile;

      return new File(selectedFile.getParentFile(), fileName + extension);
    }
    else return null;
  }

//------------------------------------------------------------------------------

  /* Returns a RenderedImage with the contents of image. */

  private RenderedImage getRenderedImage(Image image)
  {
    if (image instanceof RenderedImage) return (RenderedImage) image;           // filtered images will most likely be already BufferedImages

    BufferedImage bufferedImage = new BufferedImage(image.getWidth(null),
                                                    image.getHeight(null),
                                                    BufferedImage.TYPE_INT_ARGB);
    Graphics2D graphics         = bufferedImage.createGraphics();
    graphics.drawImage(image, 0, 0, null);
    graphics.dispose();
    return bufferedImage;
  }

//------------------------------------------------------------------------------

}
