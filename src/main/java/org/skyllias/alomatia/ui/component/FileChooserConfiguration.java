
package org.skyllias.alomatia.ui.component;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.skyllias.alomatia.i18n.LabelLocalizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** Provider of {@link JFileChooser} instances to make its users testable. */

@Configuration
public class FileChooserConfiguration
{
  private static final String IMAGE_FILES_FILTER_LABEL = "source.selector.file.filter";

//==============================================================================

  @Bean("singleFileChooser")
  public JFileChooser singleFileChooser(LabelLocalizer labelLocalizer)
  {
    JFileChooser chooser   = new JFileChooser();
    FileFilter imageFilter = new FileNameExtensionFilter(labelLocalizer.getString(IMAGE_FILES_FILTER_LABEL),
                                                         ImageIO.getReaderFileSuffixes());
    chooser.setAcceptAllFileFilterUsed(false);
    chooser.addChoosableFileFilter(imageFilter);

    return chooser;
  }

//------------------------------------------------------------------------------

  @Bean("dirFileChooser")
  public JFileChooser dirFileChooser(LabelLocalizer labelLocalizer)
  {
    JFileChooser chooser = new JFileChooser();
    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

    return chooser;
  }

//------------------------------------------------------------------------------

}
