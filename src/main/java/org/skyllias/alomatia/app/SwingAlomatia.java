
package org.skyllias.alomatia.app;

import java.util.*;

import javax.swing.*;

import org.skyllias.alomatia.display.*;
import org.skyllias.alomatia.filter.*;
import org.skyllias.alomatia.i18n.*;
import org.skyllias.alomatia.source.*;
import org.skyllias.alomatia.ui.*;
import org.skyllias.alomatia.ui.frame.*;

import com.jtattoo.plaf.aero.*;

/** Launcher of a visual application. */

public class SwingAlomatia
{
  private static final String LNF_LOGO_STRING_PROPERTY = "logoString";
  private static final String APP_NAME                 = "app.name";

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
          LabelLocalizer labelLocalizer = new StartupLabelLocalizer();

          Properties lnfProperties = new Properties();
          lnfProperties.put(LNF_LOGO_STRING_PROPERTY, labelLocalizer.getString(APP_NAME));
          AeroLookAndFeel.setCurrentTheme(lnfProperties);
          UIManager.setLookAndFeel(new AeroLookAndFeel());                      // TODO make it optional

          FixedCatalogueGenerator catalogueGenerator = new FixedCatalogueGenerator();
          Repeater repeater                          = new Repeater();
          FramePolicy framePolicy                    = new FramePolicy();
          new ControlFrame(labelLocalizer, catalogueGenerator.getNewCatalogue(repeater),
                           repeater, new FixedFilterFactory(), framePolicy);    // TODO instead of passing these instances everywhere use an injection framework like Spring
        }
        catch (Exception e) {e.printStackTrace();}                              // TODO log
      }
    });
  }

//------------------------------------------------------------------------------

}
