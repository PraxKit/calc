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
        h3(
          """ 
          Wie viele Termine 
          habe ich pro Monat?
          """
        ),
          
      h4("Beispiel: ", br, ex.toString ),
      p("Ich habe durchschnittlich " + ex.sessionsMonth + " Termine pro Monat und plane " + ex.vacations + " Wochen Ferien.", br, 
      "Ohne Kalenderfunktionen kostet mich PraxKit: "),
      ul( 
        li("Im Monat: ",aprice._2.toString), 
        li("Im Jahr: ", aprice._1.toString)
      ),
      h4("Meine Berechnung"),
      div(span(box, " Anzahl Sitzungen pro Monat")),
      div(span(vaccation, " Wochen Ferien im Jahr")),
      p(span(option, " mit Kalenderfunktionen")),
      div(
        output.render
      )
        
      ).render
    )
  }

  val ex = Price(12, 8, false)
  def aprice = price(ex)

  val box = input(
    `type`:="text",
    placeholder:="Anzahl Sitzungen",
    value:="0",
    size:="4"
  ).render

  val vaccation = input(
    `type`:="text",
    placeholder:="Wochen Ferien",
    value:="0",
    size:="4"
  ).render

  val option = input(
    `type`:="checkbox"
  ).render
  
  val output = div.render


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

  option.onchange = (e: dom.Event) => {
    val choice = option.checked
    println("option: " + choice)
     myPrice.withCalendar = choice
    val p = price(myPrice)
    renderResult(p)
  }

 def renderResult(p: (Double,Double)): Unit = {
   //output.textContent = p._2.toString + " pro Monat, " +  p._1.toString + " pro Jahr."
   val resultId = "resultId"
   println(myPrice)


   val result =div(id:=resultId)(
     ul(
       li("Im Monat: ", p._2.toString),
       li("Im Jahr: ", p._1.toString)
     )
   ).render
   
   val n = output.appendChild(div(id:= resultId).render)
   println (n)
   output.replaceChild(result, n)
   println (output.childNodes(0))

 }
  // -- model
  
  case class Price(
    var sessionsMonth: Int, 
    var vacations: Int, 
    var withCalendar: Boolean)

  @JSExportTopLevel("myPrice")
  var myPrice = Price(0, 0, false)

  @JSExport
  def price(p: Price): (Double, Double) = {

    val unitprice = p.withCalendar match {
      case true => 1.85d
      case _ => 1.00d
    }

    def workingmonth(vacs: Int): Double = {
      val month = 12d

      val wm = month - (vacs.toDouble / 52 * month )
      println("workingmonth " +wm)
      wm
    }

    val monthprice = p.sessionsMonth  * unitprice
    val yearprice = monthprice * workingmonth(p.vacations) 

    println ("Month: " + monthprice + " " + round(monthprice))
    println ("Year: " + yearprice + " " + round(yearprice))
    (round(yearprice), round(monthprice))
  }

  def round (input: Double): Double = BigDecimal(input).setScale(2, BigDecimal.RoundingMode.HALF_UP).toDouble
}