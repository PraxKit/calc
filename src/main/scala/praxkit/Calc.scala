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
      p("Die Berechnung der Kosten erfolgt auf Basis der ", em("Anzahl "), "Termine pro Monat."),   
      h4("Beispiel: " ),
      p("Ich habe durchschnittlich " + ex.sessionsMonth + " Termine pro Monat und plane " + ex.vacations + " Wochen Ferien.", br, 
      "Ohne Kalenderfunktionen bezahle ich: "),
      ul( 
        li( fmt(aprice._2), "pro Monat. "), 
        li( fmt(aprice._1), "pro Jahr. ")
      ),
      h4("Meine persönliche Kostenrechner"),
      div(span(box, " Anzahl Termine pro Monat")),
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
    placeholder:="Termine",
    value:="0",
    size:="4"
  ).render

  val vaccation = input(
    `type`:="text",
    placeholder:="Ferien",
    value:="0",
    size:="4"
  ).render

  val option = input(
    `type`:="checkbox"
  ).render
  
  val output = div.render


  // -- controller
  

  box.onkeyup = (e: dom.Event) => {
    myPrice.sessionsMonth = toInt(box.value).getOrElse(0)
    val p = price(myPrice)
    renderResult(p)
  }

  vaccation.onkeyup = (e: dom.Event) => {
    myPrice.vacations = toInt(vaccation.value).getOrElse(0) % 52
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


   val result = div(id:=resultId)(
     ul(
       li(fmt(p._2), "pro Monat "),
       li(fmt(p._1), "pro Jahr ")
     )
   ).render
   
   if (output.hasChildNodes) {  
     output.replaceChild(result, output.firstChild) 
     //println("hasChildNodes")
     }
   else {
     //println("no ChildNodes")
     output.appendChild(div(id:= resultId).render)
     output.replaceChild(result, output.firstChild)
   }
   

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
      wm
    }

    val monthprice = p.sessionsMonth  * unitprice
    val yearprice = monthprice * workingmonth(p.vacations) 

    (round(yearprice), round(monthprice))
  }

  def round (input: Double): Double = BigDecimal(input).setScale(2, BigDecimal.RoundingMode.HALF_UP).toDouble

  def fmt(d: Double): String = f"CHF $d%10.2f "

  def toInt(s: String): Option[Int] = {
  try {
    Some(s.toInt)
  } catch {
    case e: Exception => None
  }
}
}