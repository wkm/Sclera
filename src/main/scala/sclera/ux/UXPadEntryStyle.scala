package sclera.ux

import java.awt.Color

/**
 * sclera.ux.UXPadEntryStyle
 * wiktor - 2011
 */

abstract class UXPadEntryStyle
  extends TextStyle
  with FrameStyle
  with MarginStyle

/**
 * encapsulates various text formatting information (family,
 * weight, color, etc.)
 */
trait TextStyle {
  var fontFamily: String
  var fontSize: Int
}

/**
 * encapsulates various framing information (thickness, color,
 * background, etc.)
 */
trait FrameStyle {
  var frameColor: Option[Color]
  var frameThickness: Option[BoxSideValues[Int]]
  var framePadding: Option[BoxSideValues[Int]]
}

/**
 * encapsulates margin information (width, etc.)
 */
trait MarginStyle {
  var marginWidth: BoxSideValues[Int]
}

case class BoxSideValues[A] (
    val top: A,
    val right: A,
    val bottom: A,
    val left: A
) {
  def equalSided =
    top == right &&
    right == bottom &&
    bottom == left

  def equalVerticals =
    top == bottom

  def equalHorizontals =
    left == right
}

object BoxSideValues
{
  def apply[A](value: A) : BoxSideValues[A] =
    apply[A](value, value)

  def apply[A](vertical: A, horizontal: A): BoxSideValues[A] =
    BoxSideValues[A](vertical, horizontal, vertical, horizontal)
}