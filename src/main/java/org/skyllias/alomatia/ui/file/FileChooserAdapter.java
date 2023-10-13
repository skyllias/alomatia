
package org.skyllias.alomatia.ui.file;

import java.awt.Component;
import java.io.File;
import java.util.function.Supplier;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.skyllias.alomatia.i18n.LabelLocalizer;

/** Wrapper around a JFileChooser that is not instantiated until needed.
 *  Its purpose is that JFileChooser instances can be generated inside the AWT
 *  event dispatching thread and not during Spring bean factorization.
 *  Only the required methods are exposed. */

public class FileChooserAdapter
{
  private static final String IMAGE_FILES_FILTER_LABEL = "source.selector.file.filter";

  private final Supplier<JFileChooser> instanceSupplier;

  private JFileChooser instance;

//==============================================================================

  private FileChooserAdapter(Supplier<JFileChooser> instanceSupplier)
  {
    this.instanceSupplier = instanceSupplier;
  }

//==============================================================================

  /** Factory method for a single image file chooser. */

  public static FileChooserAdapter forSingleImage(LabelLocalizer labelLocalizer)
  {
    return new FileChooserAdapter(() ->
    {
      JFileChooser chooser   = new JFileChooser();
      FileFilter imageFilter = new FileNameExtensionFilter(labelLocalizer.getString(IMAGE_FILES_FILTER_LABEL),
                                                           ImageIO.getReaderFileSuffixes());
      chooser.setAcceptAllFileFilterUsed(false);
      chooser.addChoosableFileFilter(imageFilter);

      return chooser;
    });
  }

//------------------------------------------------------------------------------

  /** Factory method for a dir chooser. */

  public static FileChooserAdapter forDir(LabelLocalizer labelLocalizer)
  {
    return new FileChooserAdapter(() ->
    {
      JFileChooser chooser = new JFileChooser();
      chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

      return chooser;
    });
  }

//------------------------------------------------------------------------------

  public void setSelectedFile(File file) {getInstance().setSelectedFile(file);}

//------------------------------------------------------------------------------

  public int showOpenDialog(Component parent) {return getInstance().showOpenDialog(null);}

//------------------------------------------------------------------------------

  public File getSelectedFile() {return getInstance().getSelectedFile();}

//------------------------------------------------------------------------------

  private JFileChooser getInstance()
  {
    if (instance == null) instance = instanceSupplier.get();

    return instance;
  }

//------------------------------------------------------------------------------

}
