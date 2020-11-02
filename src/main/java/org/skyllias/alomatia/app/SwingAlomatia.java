
package org.skyllias.alomatia.app;

import java.util.Properties;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.skyllias.alomatia.display.Repeater;
import org.skyllias.alomatia.filter.FixedFilterFactory;
import org.skyllias.alomatia.i18n.LabelLocalizer;
import org.skyllias.alomatia.i18n.StartupLabelLocalizer;
import org.skyllias.alomatia.source.FixedCatalogueGenerator;
import org.skyllias.alomatia.ui.ControlFrameManager;
import org.skyllias.alomatia.ui.frame.FramePolicyAtStartUp;

import com.jtattoo.plaf.aero.AeroLookAndFeel;

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
          FramePolicyAtStartUp framePolicy           = new FramePolicyAtStartUp();

          ControlFrameManager controlFrameController = new ControlFrameManager();
          controlFrameController.createControlFrame(labelLocalizer,
                                                    catalogueGenerator.getNewCatalogue(repeater),
                                                    repeater, new FixedFilterFactory(), framePolicy);    // TODO instead of passing these instances everywhere use an injection framework like Spring
        }
        catch (Exception e) {e.printStackTrace();}                              // TODO log
      }
    });
  }

//------------------------------------------------------------------------------

}
