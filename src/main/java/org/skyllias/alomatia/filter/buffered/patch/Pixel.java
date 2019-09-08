
package org.skyllias.alomatia.filter.buffered.patch;

import java.awt.*;

/** Pixel from an image, identified by its coordinates and characterized by its colour.
 *  Two pixels are considered equal if they have the same coordinates, regardless
 *  of their colour. */

public class Pixel
{
  private final Point coordinates;
  private final Color colour;

//==============================================================================

  public Pixel(Point coordinates, Color colour)
  {
    this.coordinates = coordinates;
    this.colour      = colour;
  }

//==============================================================================

  public Point getCoordinates() {return coordinates;}

  public Color getColour() {return colour;}

//------------------------------------------------------------------------------

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((coordinates == null) ? 0 : coordinates.hashCode());
    return result;
  }

//------------------------------------------------------------------------------

  @Override
  public boolean equals(Object obj)
  {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    Pixel other = (Pixel) obj;
    if (coordinates == null)
    {
      if (other.coordinates != null) return false;
    }
    else if (!coordinates.equals(other.coordinates)) return false;
    return true;
  }

//------------------------------------------------------------------------------

  @Override
  public String toString()
  {
    return "Pixel [coordinates=" + coordinates + ", colour=" + colour + "]";
  }

//------------------------------------------------------------------------------

}
