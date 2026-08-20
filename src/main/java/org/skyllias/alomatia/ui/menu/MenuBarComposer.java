
package org.skyllias.alomatia.ui.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import org.skyllias.alomatia.i18n.LabelLocalizer;
import org.skyllias.alomatia.preferences.WindowControlPreferences;
import org.skyllias.alomatia.ui.DisplayFrameManager;
import org.skyllias.alomatia.ui.WindowControlPanelComposer;
import org.skyllias.alomatia.ui.controls.ControlsWindow;
import org.skyllias.alomatia.ui.frame.MainApplicationFrameSupplier;
import org.springframework.stereotype.Component;

/** Composer of the menu bar with the most basic window management actions, to
 *  be displayed in the main application frame.
 *
 *  The items to arrange the display frames and to show the controls window only
 *  make sense when the main frame contains the display frames and the controls
 *  are displayed apart from it. */

@Component
public class MenuBarComposer
{
  private static final String WINDOW_MENU_LABEL      = "desktop.menu.window";
  private static final String NEW_ITEM_LABEL         = "desktop.menu.window.new";
  private static final String ARRANGE_ITEM_LABEL     = "desktop.menu.window.arrange";
  private static final String CONTROLS_ITEM_LABEL    = "desktop.menu.window.controls";
  private static final String APPLICATION_MENU_LABEL = "desktop.menu.application";
  private static final String EXIT_ITEM_LABEL        = "desktop.menu.application.exit";

  protected static final String WINDOW_MENU_NAME      = "menu.window";          // name for the components
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

  public MenuBarComposer(LabelLocalizer labelLocalizer,
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

  /** Returns a new menu bar for the frame that contains the controls, with the
   *  display frames being independent windows. */

  public JMenuBar createComponentForSeparateWindows()
  {
    return buildMenuBar(buildWindowMenu());
  }

//------------------------------------------------------------------------------

  /** Returns a new menu bar for the frame that contains the display frames, with
   *  additional items to arrange them and to show controlsWindow. */

  public JMenuBar createComponentForInternalDisplays(ControlsWindow controlsWindow)
  {
    JMenu windowMenu = buildWindowMenu();
    windowMenu.add(buildArrangeItem());
    windowMenu.add(buildControlsItem(controlsWindow));

    return buildMenuBar(windowMenu);
  }

//------------------------------------------------------------------------------

  private JMenuBar buildMenuBar(JMenu windowMenu)
  {
    JMenuBar menuBar = new JMenuBar();
    menuBar.add(windowMenu);
    menuBar.add(buildApplicationMenu());

    return menuBar;
  }

//------------------------------------------------------------------------------

  private JMenu buildWindowMenu()
  {
    JMenu windowMenu = buildMenu(WINDOW_MENU_LABEL, WINDOW_MENU_NAME);

    windowMenu.add(buildMenuItem(NEW_ITEM_LABEL, NEW_ITEM_NAME, new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e) {windowControlPanelComposer.createNewDisplayFrame();}
    }));

    return windowMenu;
  }

//------------------------------------------------------------------------------

  private JMenuItem buildArrangeItem()
  {
    return buildMenuItem(ARRANGE_ITEM_LABEL, ARRANGE_ITEM_NAME, new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e) {arrangeWindowsAsPreferred();}
    });
  }

//------------------------------------------------------------------------------

  private JMenuItem buildControlsItem(final ControlsWindow controlsWindow)
  {
    return buildMenuItem(CONTROLS_ITEM_LABEL, CONTROLS_ITEM_NAME, new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e) {controlsWindow.setVisible(true);}
    });
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
