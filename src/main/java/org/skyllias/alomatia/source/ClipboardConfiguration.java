package org.skyllias.alomatia.source;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** Instantiator of the clipboard for the {@link ClipboardSource}. */

@Configuration
public class ClipboardConfiguration
{
//==============================================================================

  @Bean
  public Clipboard clipboard() {return Toolkit.getDefaultToolkit().getSystemClipboard();}

//------------------------------------------------------------------------------

}
