
package org.skyllias.alomatia.app;

import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.skyllias.alomatia.dependency.BeanFactoryLoader;
import org.skyllias.alomatia.ui.controls.ControlsFrameManager;

import com.jtattoo.plaf.aero.AeroLookAndFeel;

/** Launcher of a visual application. */

public class SwingAlomatia
{
  private static final String LNF_LOGO_STRING_PROPERTY = "logoString";
  private static final String APP_NAME                 = "Alomatia";

//==============================================================================

  public static void main(String[] args)
  {
    Future<ControlsFrameManager> controlFrameController = Executors.newSingleThreadExecutor().submit(() ->
    {
      BeanFactoryLoader beanFactoryLoader = new BeanFactoryLoader();
      return beanFactoryLoader.getLoadedBean(ControlsFrameManager.class);
    });

    SwingUtilities.invokeLater(new Runnable()
    {
      @Override
      public void run()
      {
        try
        {
          Properties lnfProperties = new Properties();
          lnfProperties.put(LNF_LOGO_STRING_PROPERTY, APP_NAME);
          AeroLookAndFeel.setCurrentTheme(lnfProperties);
          UIManager.setLookAndFeel(new AeroLookAndFeel());                      // TODO make it optional

          controlFrameController.get().createControlFrame();
        }
        catch (Exception e) {e.printStackTrace();}                              // TODO log
      }
    });
  }

//------------------------------------------------------------------------------

}
