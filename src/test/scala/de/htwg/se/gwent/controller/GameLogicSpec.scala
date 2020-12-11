package scala.de.htwg.se.gwent.controller

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

import scala.de.htwg.se.gwent.controller.GameStatus.{INPUTFAIL, PLAYING}
import scala.de.htwg.se.gwent.model.PlayerType.{TOP,BOT}
import scala.de.htwg.se.gwent.model.{Card, Field, HandCard, Player}

class GameLogicSpec extends AnyWordSpec with Matchers {
  "The GameLogic decides if the Player Aktion is with in the Rules" when {
    "A Logic is in place" should {
      val logic = new GameLogic
      val archer = Card("Archer", 0, 3, 1)
      val f = Field(Vector[Vector[Option[Card]]]()).clear
      val field = f.setCard(3,3,Some(archer))
      val playerTop = Player(TOP, "Top", HandCard(Vector[Card](archer,archer,archer)),0)
      val playerBot = Player(BOT, "Bot", HandCard(Vector[Card](archer,archer)),0)
      "when a card shall be played in the field" in {
        logic.applyTryLogic(field,1,1,playerTop,0)._1 should be(PLAYING)
        logic.applyLogic(field,1,1,playerTop,0) should be(PLAYING)
      }
      "when a Card is played wrongly" in {
        logic.applyTryLogic(field,1,1,playerBot,0)._1 should be(INPUTFAIL)
        logic.applyTryLogic(field,2,2,playerBot,11)._1 should be(INPUTFAIL)
        logic.applyTryLogic(field,4,1,playerBot,0)._1 should be(INPUTFAIL)
        logic.applyTryLogic(field,3,3,playerBot,0)._1 should be(INPUTFAIL)
        logic.applyLogic(field,1,1,playerBot,0) should be(INPUTFAIL)
      }
    }
  }
}
