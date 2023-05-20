
package org.skyllias.alomatia.app;

import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.skyllias.alomatia.dependency.BeanFactoryLoader;
import org.skyllias.alomatia.ui.controls.ControlsFrameManager;

import com.formdev.flatlaf.FlatIntelliJLaf;

/** Launcher of a visual application. */

public class SwingAlomatia
{
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
          JFrame.setDefaultLookAndFeelDecorated(true);
          JDialog.setDefaultLookAndFeelDecorated(true);
          UIManager.setLookAndFeel(new FlatIntelliJLaf());

          controlFrameController.get().createControlFrame();
        }
        catch (Exception e) {e.printStackTrace();}                              // TODO log
      }
    });
  }

//------------------------------------------------------------------------------

}
