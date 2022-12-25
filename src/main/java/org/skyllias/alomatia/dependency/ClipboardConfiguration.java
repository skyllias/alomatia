
package org.skyllias.alomatia.dependency;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** Instantiator of a clipboard for sources and UI components that need one. */

@Configuration
public class ClipboardConfiguration
{
//==============================================================================

  @Bean
  public Clipboard clipboard() {return Toolkit.getDefaultToolkit().getSystemClipboard();}

//------------------------------------------------------------------------------

}
