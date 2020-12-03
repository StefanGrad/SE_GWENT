package scala.de.htwg.se.gwent.aview

import scala.de.htwg.se.gwent.util.Observer
import scala.de.htwg.se.gwent.controller.Controller
import scala.de.htwg.se.gwent.controller.GameStatus.INPUTFAIL
import scala.de.htwg.se.gwent.model.Player

class Tui(controller: Controller) extends Observer{

  controller.add(this)


  def processInputLine(input: String, boolean: Boolean):Unit = {
    input match {
      case "q" =>
      case "c" =>
        controller.passRound()
      case _ => {
        input.toList.filter(c => c != ' ').map(c => c.toString.toInt) match {
          case row :: column :: cardIndex :: Nil => controller.playCardAt(controller.field, row, column, boolean, cardIndex)
          case _ => controller.gameState = INPUTFAIL
        }
      }
    }
  }

   def update: Boolean =  {
    println(controller.fieldToString)
    true
  }

}
