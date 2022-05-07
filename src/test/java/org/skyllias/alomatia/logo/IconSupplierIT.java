package org.skyllias.alomatia.logo;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class IconSupplierIT
{
//==============================================================================

  @Test
  public void iconLoads()
  {
    assertTrue(new IconSupplier().getIcon().getWidth(null) > 0);
  }

//------------------------------------------------------------------------------

}
