package org.skyllias.alomatia.source.screen;

import java.awt.GraphicsDevice;
import java.awt.Point;

/** Equivalent to {@link java.awt.PointerInfo} with instantiation capabilities
 *  for testing. */

public class PointerData
{
  private final GraphicsDevice device;
  private final Point location;

//==============================================================================

  public PointerData(GraphicsDevice device, Point location)
  {
    this.device   = device;
    this.location = location;
  }
//==============================================================================

  public GraphicsDevice getDevice() {return device;}

//------------------------------------------------------------------------------

  public Point getLocation() {return location;}

//------------------------------------------------------------------------------


}
