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
          
      p("Beispiel: Im Monat: ",aprice._1.toString, br, 
        "Im Jahr: ", aprice._2.toString),
      div(span(vaccation, " Wochen Ferien im Jahr")),
      div(span(box, " Anzahl Sitzungen pro Monat")),
      div(output)
        
      ).render
    )
  }
  def aprice = price(Price(1, 4, true))

  val box = input(
    `type`:="text",
    placeholder:="Anzahl Sitzungen",
    value:="0",
    size:="4"
  ).render

  val vaccation = input(
    `type`:="text",
    placeholder:="Wochen Ferien",
    value:="4",
    size:="2"
  ).render

  
  val output = span.render


  // -- controller
  

  box.onkeyup = (e: dom.Event) => {
    myPrice.sessionsMonth = box.value.toInt
    val p = price(myPrice)
    renderResult(p)
 }

   vaccation.onkeyup = (e: dom.Event) => {
     myPrice.vacations = vaccation.value.toInt
    val p = price(myPrice)
    renderResult(p)
 }

 def renderResult(p: (Double,Double)): Unit = {
   output.textContent = p._2.toString + " pro Monat, " +  p._1.toString + " pro Jahr."
   println(myPrice)
 }
  // -- model
  
  case class Price(
    var sessionsMonth: Int, 
    var vacations: Int, 
    var withCalendar: Boolean)

  @JSExportTopLevel("myPrice")
  var myPrice = Price(24, 4, false)

  @JSExport
  def price(p: Price): (Double, Double) = {

    val unitprice = p.withCalendar match {
      case true => 1.85d
      case _ => 1.00d
    }

    val workingmonth: Double = 12d - (p.vacations.toDouble / 4 )

    val yearprice = p.sessionsMonth * unitprice * workingmonth 
    
    val monthaverageprice = yearprice / 12
    println ("Month: " + monthaverageprice + " " + round(monthaverageprice))
    println ("Year: " +yearprice + " " + round(yearprice))
    (round(yearprice), round(monthaverageprice))
  }

  def round (input: Double): Double = BigDecimal(input).setScale(2, BigDecimal.RoundingMode.HALF_UP).toDouble
}