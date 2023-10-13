
package org.skyllias.alomatia.ui.file;

import org.skyllias.alomatia.i18n.LabelLocalizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** Provider of {@link FileChooserAdapter} instances to make the classes that
 *  use a JFileChooser testable. */

@Configuration
public class FileChooserConfiguration
{
//==============================================================================

  @Bean("singleFileChooser")
  public FileChooserAdapter singleFileChooser(LabelLocalizer labelLocalizer)
  {
    return FileChooserAdapter.forSingleImage(labelLocalizer);
  }

//------------------------------------------------------------------------------

  @Bean("dirFileChooser")
  public FileChooserAdapter dirFileChooser(LabelLocalizer labelLocalizer)
  {
    return FileChooserAdapter.forDir(labelLocalizer);
  }

//------------------------------------------------------------------------------

}
