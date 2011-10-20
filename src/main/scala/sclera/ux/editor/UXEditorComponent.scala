
package sclera.ux.editor

import javax.swing.text._
import collection.immutable.TreeMap
import sclera.ux.wrappers.TextPaneComponent
import sclera.format.color.SolarizedColorPalette
import java.awt.event.{FocusEvent, FocusListener}
import sclera.ux.{UXPadEntry, UX}
import java.io.StringWriter
import sclera.util.{Loggable, SwingKit}
import java.awt.{Dimension, Font, Graphics}
import swing.event.{UIElementResized, Key, KeyTyped}

class UXEditorComponent(
    val padEntry: UXPadEntry
)
  extends TextPaneComponent
  with Loggable
{

  peer.setMaximumSize(new Dimension(Integer.MAX_VALUE, 15))

  /**
   * extract the text content from the text pane component
   */
  def textContent: String = {
    val kit = editorKit
    val stringWriter = new StringWriter()
    kit.write(stringWriter, document, 0, document.getLength)
    return stringWriter.toString
  }

  SwingKit.executeLater {
    editorKit =  new ScalaEditorKit()
    contentType = "text/scala"
    font = new Font("Menlo", Font.PLAIN, 12)
    foreground = SolarizedColorPalette("base03")

    listenTo(keys)
    reactions += {
      // evaluation
      case KeyTyped(_, text, Key.Modifier.Shift, _) if text.toInt == 13 =>
        UX.Processor ! UX.Evaluate()

      case other =>
        logger.debug("SIZE => {}", peer.getMinimumSize)
        val minSizeHeight = peer.getMinimumSize
        val height = peer.getHeight
        logger.debug("MIN SIZE HEIGHT: {}; HEIGHT: {}", minSizeHeight, height)
        peer.setMaximumSize(new Dimension(Integer.MAX_VALUE, height))
//        peer.repaint()
    }

    listenTo(this)
    reactions += {
      case UIElementResized(element) =>
        val currentHeight = peer.getHeight
        val size = new Dimension(Integer.MAX_VALUE, currentHeight)

        logger.debug("MIN SIZE: {}", peer.getMinimumSize)
        logger.debug("MAX SIZE: {}", peer.getMaximumSize)

//        peer.setMinimumSize(size)
//        peer.setSize(size)
//        peer.setMaximumSize(peer.getMinimumSize)
    }

    peer.addFocusListener(new FocusListener {
      def focusGained(e: FocusEvent) {
        padEntry.pad.processor !? UX.ChangeEntryFocus(padEntry)
      }

      def focusLost(e: FocusEvent) {
        padEntry.pad.processor !? UX.LoseEntryFocus(padEntry)
      }
    })
  }
}

class ScalaEditorKit(
  val viewFactory: ScalaViewFactory = new ScalaViewFactory()
) extends StyledEditorKit {
  override def getContentType = "text/scala"
}

class ScalaViewFactory extends ViewFactory {
  override def create(element: Element) =
    new ScalaView(element)
}

class ScalaView(val element: Element)
  extends PlainView(element) {

  getDocument.putProperty(PlainDocument.tabSizeAttribute, 4)

  override protected
  def drawUnselectedText(graphics: Graphics, x: Int, y: Int, p0: Int, p1: Int): Int = {
    val doc = getDocument
    val text = doc.getText(p0, p1 - p0)

    val segment = getLineBuffer

    val annotatedSource = new AnnotatedSource(text)

    val matches = UXEditorScalaKeywords.keywordRegexp.findAllIn(text)
    matches.matchData.foreach({ textmatch =>
      annotatedSource.addAnnotation(textmatch.start(0), textmatch.end(0))
    })

    return x
  }
}

class AnnotatedSource(text: String) {
  val annotationMap = new TreeMap[Tuple2[Int, Int], String]()

  def addAnnotation(start:Int, end:Int) {
    
  }
}