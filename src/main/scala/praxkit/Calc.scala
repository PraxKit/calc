package praxkit

import org.scalajs.dom
import dom.html
import scalajs.js.annotation.JSExportTopLevel
import scalajs.js.annotation.JSExport
import scalatags.JsDom.all._
import scala.scalajs.js.JSApp

@JSExportTopLevel("praxkit.HelloWorld1")
object HelloWorld1 {

  @JSExport
  def start(target: html.Div) = {
    val (animalA, animalB) = ("fox", "dog")
    target.appendChild(
      div(
        h1("Hello World!"),
        p(
          "The quick brown ", b(animalA),
          " jumps over the lazy ",
          i(animalB), "."
        )
      ).render
    )
  }
}