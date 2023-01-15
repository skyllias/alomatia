package org.skyllias.alomatia.ui.source;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.skyllias.alomatia.ImageSource;
import org.skyllias.alomatia.source.DropSource;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/** Composer for a selector of {@link DropSource}. */

@Component
@Order(1)
public class DropSourceSelectionComposer implements SourceSelectionComposer
{
  private static final String SOURCE_KEY = "dnd";

  private final DropSource dropSource;

//==============================================================================

  public DropSourceSelectionComposer(DropSource dropSource)
  {
    this.dropSource = dropSource;
  }

//==============================================================================

  @Override
  public SourceSelection buildSelector()
  {
    return new SourceSelection()
    {

      @Override
      public ImageSource getSource() {return dropSource;}

      @Override
      public JComponent getControls() {return new JPanel();}
    };
  }

//------------------------------------------------------------------------------

  @Override
  public String getSourceKey() {return SOURCE_KEY;}

//------------------------------------------------------------------------------

}
