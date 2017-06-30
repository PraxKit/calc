package praxkit

import org.scalajs.dom
import dom.html
import scalajs.js.annotation._
import scalatags.JsDom.all._


@JSExportTopLevel("HelloWorld")
object HelloWorld {
  
  @JSExport
  def sayHello(): Unit = {
    println("Hello world!")
  }

  @JSExport
  def main(target: html.Div): Unit =  {
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