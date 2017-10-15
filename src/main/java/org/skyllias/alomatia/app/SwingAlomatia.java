
package org.skyllias.alomatia.app;

import javax.swing.*;

import org.skyllias.alomatia.display.*;
import org.skyllias.alomatia.filter.*;
import org.skyllias.alomatia.i18n.*;
import org.skyllias.alomatia.source.*;
import org.skyllias.alomatia.ui.*;

import com.jtattoo.plaf.aero.*;

/** Launcher of a visual application. */

public class SwingAlomatia
{
//==============================================================================

  public static void main(String[] args)
  {
    SwingUtilities.invokeLater(new Runnable()
    {
      @Override
      public void run()
      {
        try
        {
          UIManager.setLookAndFeel(new AeroLookAndFeel());                      // TODO make it optional

          FixedCatalogueGenerator catalogueGenerator = new FixedCatalogueGenerator();
          Repeater repeater                          = new Repeater();
          new ControlFrame(new StartupLabelLocalizer(), catalogueGenerator.getNewCatalogue(repeater),
                           repeater, new FixedFilterFactory());                 // TODO instead of passing these instances everywhere use an injection framework like Spring
        }
        catch (Exception e) {e.printStackTrace();}                              // TODO log
      }
    });
  }

//------------------------------------------------------------------------------

}
