

import org.scalatest.{Matchers, WordSpec}
import main.model.{Evaluation, Field, HandCard, Player}

class Evaluationspecs extends WordSpec with Matchers {
  "Evaluation compares the Attack Values of both Players and prints out the Winner of the Round" when{
    "Evaluation" should {
      val field = Field(4,4)
      "have a draw" in {
        Evaluation().eval(field,Player("Stefan") ,Player("Adrian") ) should be (Player("Unentschieden"))
      }
      "have playerTop win" in {
        Player("Stefan").hand.playCard(0, field, 0, 0)
        Evaluation().eval(field, Player("Stefan"), Player("Adrian")) should be(Player("Stefan"))
      }
      "have playerBot win" in {
        Player("Adrian").hand.playCard(0, field, 2, 2)
        Evaluation().eval(field, Player("Stefan"), Player("Adrian")) should be(Player("Adrian"))
      }
    }
  }
}