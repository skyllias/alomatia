
package org.skyllias.alomatia.ui.save;

import java.awt.Image;
import java.io.File;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.commons.lang.StringUtils;
import org.skyllias.alomatia.i18n.LabelLocalizer;
import org.skyllias.alomatia.save.FileSaver;
import org.springframework.stereotype.Component;

/** {@link ImageSaver} that stores images in files.
 *  The format is initially fixed to PNG.
 *  By default, the user's home is used as output directory, but the path can be
 *  modified externally. Whether the user is prompted when saving interactively
 *  a file can also be set externally.
 *  This class is stateful. */

@Component
public class FileImageSaver implements ImageSaver
{
  private static final String USER_HOME_PROP = "user.home";
  private static final String IMAGE_FORMAT   = "PNG";

  private static final String ERROR_TITLE_LABEL = "save.error.title";
  private static final String ERROR_DESCR_LABEL = "save.error.description";

  private final LabelLocalizer labelLocalizer;
  private final FileSaver fileSaver;

  private final State state = new State();

//==============================================================================

  public FileImageSaver(LabelLocalizer localizer, FileSaver fileSaver)
  {
    this.labelLocalizer = localizer;
    this.fileSaver      = fileSaver;
  }

//==============================================================================

  /** Modifies the directory where files are created by default. */

  public void setDestinationDir(File dir) {state.destinationDir = dir;}

//------------------------------------------------------------------------------

  /** Modifies the behaviour when interactive saves are requested. If true,
   *  a file chooser will appear so that the user chooses where to save the
   *  image; if false, a file will be created automatically. */

  public void setPrompt(boolean prompt) {state.promptForFile = prompt;}

//------------------------------------------------------------------------------

  /** Files are overwritten.
   *  I/O errors are always notified by means of dialogs. */

  @Override
  public void save(Image image, String nameHint, boolean silently)
  {
    File destinationFile = getDestinationFile(nameHint, silently);
    if (destinationFile != null)
    {
      try {fileSaver.save(image, destinationFile);}
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

    boolean prompt = !silently && state.promptForFile;
    if (prompt) return getChosenFile(EXTENSION);
    else        return getAutomaticFile(nameHint, EXTENSION);
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
    String discriminatorNameFragment = new DecimalFormat(NUMBER_NAME_PATTERN).format(state.discriminator++);
    String defaultFileName           = MessageFormat.format(AUTO_NAME_PATTERN, dateNameFragment,
                                                            discriminatorNameFragment, nameHint);
    return new File(state.destinationDir, defaultFileName);
  }

//------------------------------------------------------------------------------

  /* Opens a file chooser in destinationDir restricted to files with the passed
   * extension and returns the selected file, or null if the operation is cancelled.
   * The extension is added if the file does not exist and its name does not end
   * with it. */

  private File getChosenFile(String extension)
  {
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setCurrentDirectory(state.destinationDir);
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

//******************************************************************************

  private static class State
  {
    File destinationDir   = new File(System.getProperty(USER_HOME_PROP));
    boolean promptForFile = true;
    int discriminator     = 0;                                                  // sequential number used to avoid collisions when multiple images are saved at the same time
  }
}
