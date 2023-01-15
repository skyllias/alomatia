
package org.skyllias.alomatia.ui.source;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.skyllias.alomatia.ImageSource;
import org.skyllias.alomatia.source.VoidSource;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/** Composer for a selector of {@link VoidSource}. */

@Component
@Order(0)
public class VoidSourceSelectionComposer implements SourceSelectionComposer
{
  private static final String SOURCE_KEY = "none";

  private final VoidSource voidSource;

//==============================================================================

  public VoidSourceSelectionComposer(VoidSource voidSource)
  {
    this.voidSource = voidSource;
  }

//==============================================================================

  @Override
  public SourceSelection buildSelector()
  {
    return new SourceSelection()
    {

      @Override
      public ImageSource getSource() {return voidSource;}

      @Override
      public JComponent getControls() {return new JPanel();}
    };
  }

//------------------------------------------------------------------------------

  @Override
  public String getSourceKey() {return SOURCE_KEY;}

//------------------------------------------------------------------------------

}
