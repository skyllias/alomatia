
package org.skyllias.alomatia.app;

import java.util.Properties;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.skyllias.alomatia.dependency.BeanFactoryLoader;
import org.skyllias.alomatia.i18n.LabelLocalizer;
import org.skyllias.alomatia.ui.controls.ControlsFrameManager;

import com.jtattoo.plaf.aero.AeroLookAndFeel;

/** Launcher of a visual application. */

public class SwingAlomatia
{
  private static final String LNF_LOGO_STRING_PROPERTY = "logoString";
  private static final String APP_NAME                 = "app.name";

//==============================================================================

  public static void main(String[] args)
  {
    BeanFactoryLoader beanFactoryLoader = new BeanFactoryLoader();

    ControlsFrameManager controlFrameController = beanFactoryLoader.getLoadedBean(ControlsFrameManager.class);
    LabelLocalizer labelLocalizer               = beanFactoryLoader.getLoadedBean(LabelLocalizer.class);

    SwingUtilities.invokeLater(new Runnable()
    {
      @Override
      public void run()
      {
        try
        {
          Properties lnfProperties = new Properties();
          lnfProperties.put(LNF_LOGO_STRING_PROPERTY, labelLocalizer.getString(APP_NAME));
          AeroLookAndFeel.setCurrentTheme(lnfProperties);
          UIManager.setLookAndFeel(new AeroLookAndFeel());                      // TODO make it optional

          controlFrameController.createControlFrame();
        }
        catch (Exception e) {e.printStackTrace();}                              // TODO log
      }
    });
  }

//------------------------------------------------------------------------------

}
