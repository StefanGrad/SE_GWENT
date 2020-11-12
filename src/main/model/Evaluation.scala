package main.model

class Evaluation {
  def eval(field: Field, playerTop: Player, playerBot: Player):Player = {
    var top = 0
    for {
      r <- 0 until (field.row - 2)
      c <- 0 until field.col
    } top += field.get(c,r).strength
    var bot = 0
    for {
      r <- 2 until field.row
      c <- 0 until field.col
    } top += field.get(c,r).strength
    var winner = Player("Unentschieden")
    if (top - bot > 0) {
      winner = playerTop
    }
    if(top - bot < 0){
      winner = playerBot
    }
    println(winner.toString)
    winner
  }

}
