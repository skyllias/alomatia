
package org.skyllias.alomatia.ui;

import javax.swing.JRadioButton;

import org.skyllias.alomatia.i18n.LabelLocalizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SourceRadioSelectorConfiguration
{
//==============================================================================

  @Bean
  public SourceRadioSelector<JRadioButton> sourceRadioSelector(LabelLocalizer labelLocalizer)
  {
    return new SourceRadioSelector<>(JRadioButton.class, labelLocalizer);
  }

//------------------------------------------------------------------------------

}
