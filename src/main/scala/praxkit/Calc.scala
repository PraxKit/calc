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
          Wie viele Termine habe ich pro Monat?
          """
        ),
      p("Die Berechnung der Kosten erfolgt auf Basis der Anzahl Termine (Behandlungen, Sitzungen oder Konsultationen) pro Monat."), 
      p("Bitte ausfüllen:"),  
      br,
      div(cls:="row")(
        div(cls:="col-sm-6 col-md5")(
          h5("Ich habe in der Regel"),
          div(cls:="input")(span(box, " Termine pro Monat")),
          div(cls:="input")(span(vaccation, " Wochen Ferien im Jahr")), br
          
        ),
        div(cls:="col-sm-6 col-md5")(
          h5("Kosten"),
          div(cls:="input")(span(option, " mit Kalenderfunktionen")),
          div(
            output.render, br
          )
        )
      ),
      beispiel.render
        
      ).render
    )
  }
  val ex = Price(16, 8, false)
  def aprice = price(ex)
  val beispiel = div(cls:="beispiel")(
     h5("Beispiel " ),
      p("Wenn ich durchschnittlich " + ex.sessionsMonth + " Termine pro Monat habe und " + ex.vacations + " Wochen Ferien plane, ",
      "dann bezahle ich ohne Kalenderfunktionen ", fmt(aprice._2), "pro Monat oder etwa ", 
      fmt(aprice._1), "pro Jahr. "
      ))
 

  val box = input(
    `type`:="text",
    placeholder:="Termine",
    value:="0",
    size:="3"
  ).render

  val vaccation = input(
    `type`:="text",
    placeholder:="Ferien",
    value:="0",
    size:="3"
  ).render

  val option = input(
    `type`:="checkbox"
  ).render
  
  val output = div(id:="empty")(
     ul(
       li(fmt(0), "pro Monat "),
       li("etwa ", fmt(0), "pro Jahr ")
     )
   ).render


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
       li("etwa ", fmt(p._1), "pro Jahr ")
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