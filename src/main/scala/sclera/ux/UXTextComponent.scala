package sclera.ux

import sclera.sclera.format.TextFormat
import swing.{Swing, Label}
import java.awt.{Color, GraphicsEnvironment, Font}

class UXTextComponent(
    override val text:String,
    var textFormat : Option[TextFormat] = Option.empty
)
  extends Label(text)
  with UXObjectComponent {

  font = new Font("Helvetica", Font.PLAIN, 13);
  foreground = new Color(84, 123, 151);
}