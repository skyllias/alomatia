
package org.skyllias.alomatia.logo;

import java.awt.Image;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Component;

/** Supplier of the image to be used as icon in frames and dialogs.
 *  One single instance is loaded from a resource and returned every time the
 *  icon is requested. */

@Component
public class IconSupplier
{
  public static final String RESOURE_PATH = "/img/alomatia.png";

  private final Image icon;

//==============================================================================

  public IconSupplier()
  {
    try
    {
      URL resourceUrl = getClass().getResource(RESOURE_PATH);
      icon            = ImageIO.read(resourceUrl);
    }
    catch (IOException ioe) {throw new RuntimeException(ioe);}
  }

//==============================================================================

  public Image getIcon() {return icon;}

//------------------------------------------------------------------------------

}
