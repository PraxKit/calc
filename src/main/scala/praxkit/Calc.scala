package praxkit

import org.scalajs.dom
import dom.html
import scalajs.js.annotation._
import scalatags.JsDom.all._


@JSExportTopLevel("Calc")
object HelloWorld {
  
  @JSExport
  def sayHello(): Unit = {
    println("Hello world!")
  }
// -- view
  @JSExport
  def main(target: html.Div): Unit =  {
    target.appendChild(
      div(
        h1("Hello Calc!"),
        p(
          """ 
          Wie viele Sitzungen bzw. Behandlungen 
          habe ich pro Woche oder pro Monat?
          """
        ),
          
      p("Im Monat: ",aprice._1.toString, br, 
        "Im Jahr: ", aprice._2.toString),
      div(box),
      div(output)
        
      ).render
    )
  }
  def aprice = price(1, 4, true)

  val box = input(
    `type`:="text",
    placeholder:="Anzahl Sitzungen"
  ).render

  val vaccation = input(
    `type`:="text",
    placeholder:="Wochen Ferien"
  ).render

  
  val output = span.render


  // -- controller
  def str2Int(input: String): Int ={
     input match {
       case i: String => ???
     }
  }

  box.onkeyup = (e: dom.Event) => {
    val p = price(box.value.toInt, 4, false)
  output.textContent = p._2.toString + " pro Monat, " +  p._1.toString + " pro Jahr."
}
  // -- model
  @JSExport
  def price(
    sessionsMonth: Int, 
    vacations: Int, 
    withCalendar: Boolean): (Double, Double) = {

    val unitprice = withCalendar match {
      case true => 1.85d
      case _ => 1.00d
    }

    val workingmonth = 12 - (vacations / 4 )

    val yearprice = sessionsMonth * unitprice * workingmonth 
    
    val monthaverageprice = yearprice / 12
    println ("Month: " + monthaverageprice + " " + round(monthaverageprice))
    println ("Year: " +yearprice + " " + round(yearprice))
    (round(yearprice), round(monthaverageprice))
  }

  def round (input: Double): Double = BigDecimal(input).setScale(2, BigDecimal.RoundingMode.HALF_UP).toDouble
}