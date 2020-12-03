package scala.de.htwg.se.gwent.controller

import scala.de.htwg.se.gwent.controller.GameStatus.{GameStatus, INPUTFAIL, PASSED, PLAYING}
import scala.de.htwg.se.gwent.model.{Card, Field, HandCard, Player}
import scala.de.htwg.se.gwent.util.Observable

class Controller(var field: Field, var playerTop: Player, var playerBot: Player) extends Observable {

  var gameState: GameStatus = PLAYING
  val logic = new GameLogic
  def createField:Unit = {
    field = Field(4,4)
    notifyObservers
  }

  def fieldToString: String = field.toString

  def evaluate(fieldPlay: Field, playerTop: Player, playerBot: Player): Unit = {
    val winner = fieldPlay.evaluator.eval(fieldPlay,playerTop,playerBot)
    gameState = PLAYING
    if (winner == 1) {
      updatePlayerWins(playerTop,0)
      return fieldPlay.clear(fieldPlay)
    }
    if (winner == 2) {
      updatePlayerWins(playerBot,1)
      return fieldPlay.clear(fieldPlay)
    }
    fieldPlay.clear(fieldPlay)
  }

  def clearField(fieldPlay: Field): Unit = {
    field = fieldPlay.clear(fieldPlay)
    gameState = PLAYING
    notifyObservers
  }

  def createPlayer(name:String,p:Int):Unit = {
    if (p == 1) {
      playerBot = Player(name, HandCard(Vector[Card]()).newDeck(),0,false)
      return notifyObservers
    }
    playerTop = Player(name, HandCard(Vector[Card]()).newDeck(),0,true)
    notifyObservers
  }

  def updatePlayerWins(player: Player,p:Int): Unit = {
    if (p == 1) {
      playerBot = playerBot.updateWins(playerBot)
      return notifyObservers
    }
    playerTop = playerTop.updateWins(playerTop)
    notifyObservers
  }

  def playerToString(player: Player): String = player.toString

  def playCardAt(fieldPlay: Field, row: Int, col:Int, player: Player, cardIndex: Int): Unit = {
    gameState = logic.applyLogic(fieldPlay,row, col, player, cardIndex)
    if (gameState.equals(INPUTFAIL)) return notifyObservers
    val tuple = player.handCard.playCard(cardIndex,fieldPlay,row,col)
    field = tuple._3
    val name = player.name
    if (player.whichPlayer) {
      playerTop = Player(name, tuple._2,player.wins,true)
      return notifyObservers
    }
    playerBot = Player(name,tuple._2, player.wins, false)
    notifyObservers
  }

  def passRound():Unit = {
    if (gameState.equals(PLAYING)) {
      gameState = PASSED
      return notifyObservers
    }
    evaluate(field,playerTop,playerBot)
  }



}
