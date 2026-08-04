
package org.skyllias.alomatia.ui.controls;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import org.skyllias.alomatia.dependency.Profiles;
import org.skyllias.alomatia.i18n.LabelLocalizer;
import org.skyllias.alomatia.preferences.WindowControlPreferences;
import org.skyllias.alomatia.ui.DisplayFrameManager;
import org.skyllias.alomatia.ui.WindowControlPanelComposer;
import org.skyllias.alomatia.ui.frame.MainApplicationFrameSupplier;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/** Composer of the menu bar with the most basic window management actions, to be
 *  displayed in the main application frame that contains the internal frames
 *  where filtered images are drawn. */

@Component
@Profile(Profiles.INTERNAL_WINDOWS)
public class DesktopMenuBarComposer
{
  private static final String WINDOW_MENU_LABEL      = "desktop.menu.window";
  private static final String NEW_ITEM_LABEL         = "desktop.menu.window.new";
  private static final String ARRANGE_ITEM_LABEL     = "desktop.menu.window.arrange";
  private static final String CONTROLS_ITEM_LABEL    = "desktop.menu.window.controls";
  private static final String APPLICATION_MENU_LABEL = "desktop.menu.application";
  private static final String EXIT_ITEM_LABEL        = "desktop.menu.application.exit";

  protected static final String WINDOW_MENU_NAME      = "menu.window";           // name for the components
  protected static final String NEW_ITEM_NAME         = "menuitem.new";
  protected static final String ARRANGE_ITEM_NAME     = "menuitem.arrange";
  protected static final String CONTROLS_ITEM_NAME    = "menuitem.controls";
  protected static final String APPLICATION_MENU_NAME = "menu.application";
  protected static final String EXIT_ITEM_NAME        = "menuitem.exit";

  private final LabelLocalizer labelLocalizer;
  private final WindowControlPanelComposer windowControlPanelComposer;
  private final DisplayFrameManager displayFrameManager;
  private final WindowControlPreferences windowControlPreferences;
  private final MainApplicationFrameSupplier mainApplicationFrameSupplier;

//==============================================================================

  public DesktopMenuBarComposer(LabelLocalizer labelLocalizer,
                                WindowControlPanelComposer windowControlPanelComposer,
                                DisplayFrameManager displayFrameManager,
                                WindowControlPreferences windowControlPreferences,
                                MainApplicationFrameSupplier mainApplicationFrameSupplier)
  {
    this.labelLocalizer               = labelLocalizer;
    this.windowControlPanelComposer   = windowControlPanelComposer;
    this.displayFrameManager          = displayFrameManager;
    this.windowControlPreferences     = windowControlPreferences;
    this.mainApplicationFrameSupplier = mainApplicationFrameSupplier;
  }

//==============================================================================

  /** Returns a new menu bar with actions over the display windows and over
   *  controlsWindow, which is shown when the corresponding item is selected. */

  public JMenuBar createComponent(ControlsWindow controlsWindow)
  {
    JMenuBar menuBar = new JMenuBar();
    menuBar.add(buildWindowMenu(controlsWindow));
    menuBar.add(buildApplicationMenu());

    return menuBar;
  }

//------------------------------------------------------------------------------

  private JMenu buildWindowMenu(final ControlsWindow controlsWindow)
  {
    JMenu windowMenu = buildMenu(WINDOW_MENU_LABEL, WINDOW_MENU_NAME);

    windowMenu.add(buildMenuItem(NEW_ITEM_LABEL, NEW_ITEM_NAME, new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e) {windowControlPanelComposer.createNewDisplayFrame();}
    }));
    windowMenu.add(buildMenuItem(ARRANGE_ITEM_LABEL, ARRANGE_ITEM_NAME, new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e) {arrangeWindowsAsPreferred();}
    }));
    windowMenu.add(buildMenuItem(CONTROLS_ITEM_LABEL, CONTROLS_ITEM_NAME, new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e) {controlsWindow.setVisible(true);}
    }));

    return windowMenu;
  }

//------------------------------------------------------------------------------

  private JMenu buildApplicationMenu()
  {
    JMenu applicationMenu = buildMenu(APPLICATION_MENU_LABEL, APPLICATION_MENU_NAME);

    applicationMenu.add(buildMenuItem(EXIT_ITEM_LABEL, EXIT_ITEM_NAME, new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e) {closeApplication();}
    }));

    return applicationMenu;
  }

//------------------------------------------------------------------------------

  private JMenu buildMenu(String labelKey, String componentName)
  {
    JMenu menu = new JMenu(labelLocalizer.getString(labelKey));
    menu.setName(componentName);

    return menu;
  }

//------------------------------------------------------------------------------

  private JMenuItem buildMenuItem(String labelKey, String componentName,
                                  ActionListener actionListener)
  {
    JMenuItem menuItem = new JMenuItem(labelLocalizer.getString(labelKey));
    menuItem.setName(componentName);
    menuItem.addActionListener(actionListener);

    return menuItem;
  }

//------------------------------------------------------------------------------

  private void arrangeWindowsAsPreferred()
  {
    displayFrameManager.rearrangeWindows(windowControlPreferences.getAmountOfLinesToArrangeIn(),
                                         windowControlPreferences.isHorizontallyArranged());
  }

//------------------------------------------------------------------------------

  private void closeApplication()
  {
    JFrame mainFrame = mainApplicationFrameSupplier.getMainFrame();
    mainFrame.dispatchEvent(new WindowEvent(mainFrame, WindowEvent.WINDOW_CLOSING));
  }

//------------------------------------------------------------------------------

}
