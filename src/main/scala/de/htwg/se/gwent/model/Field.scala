package scala.de.htwg.se.gwent.model

import scala.math.sqrt

case class Field(col:Int, row:Int) {
  val evaluator = Evaluation()
  val field = Array.fill(col,row)(Card("",0,0,0))
  val size = field.length
  val blocknum = sqrt(size).toInt
  def isEmpty(col:Int,row:Int):Boolean = field(col)(row).isEmpty
  def isNotFull(fromRow:Int, tillRow:Int): Boolean = {
    var notFull = 0
    for {
      row <- fromRow until tillRow
      col <- 0 until col
    } if (field(row)(col).isEmpty) {notFull += 1}
    notFull > 0
  }
  def setCard(col:Int, row:Int, card:Card):Card = {
    field(col)(row) = card
    card
  }
  override def toString: String = {
    val lineseparator = ("+-----" + ("-----" * blocknum)) * blocknum + "+\n"
    val line = ("|" + "x") * row + "|\n"
    var box = "\n" + (lineseparator + (line * blocknum)) * blocknum + lineseparator
    for {
      row <- 0 until row
      col <- 0 until col
    } {
      val s = StringBuilder.newBuilder
      s.append("(").append(row).append(",").append(col).append(")")
      box = box.replaceFirst("x", field(row)(col).toString.replace("    ", s.toString()))
    }
    box
  }

  def getCard(col:Int, row:Int): Card = field(col)(row)

  def clear(field: Field): Field = {
    for {
      row <- 0 until row
      col <- 0 until col
    } field.setCard(col,row,Card("",0,0,0))
    field
  }
}