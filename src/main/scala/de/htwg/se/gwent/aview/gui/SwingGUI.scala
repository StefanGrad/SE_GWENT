package de.htwg.se.gwent.aview.gui
import de.htwg.se.gwent.controller.{CellChanged, PlayerChanged}
import de.htwg.se.gwent.model.playerComponent.{PlayerType, choosePlayer}

import scala.de.htwg.se.gwent.Gwent.controller
import scala.de.htwg.se.gwent.aview.gui.CellPanel
import scala.de.htwg.se.gwent.controller.Controller
import scala.swing._
import scala.swing.Swing.LineBorder
import scala.swing.event._
import scala.io.Source._
import PlayerType.{BOT, TOP}

class SwingGUI(c :Controller) extends Frame {
  listenTo(c)

  title = "Gwent"

  var cells = Array.ofDim[CellPanel](controller.field.size, controller.field.size)
  var topHand = Array.ofDim[CardPanel](c.playerTop.handCard.size)
  var botHand = Array.ofDim[CardPanel](c.playerBot.handCard.size)

  def gridPanel = new GridPanel(4, 4) {
    preferredSize = new Dimension(105*4, 65*4)
    background = java.awt.Color.BLACK
    border = LineBorder(java.awt.Color.BLACK, 2)
    for {
      row <- 0 until 4
      column <- 0 until 4
    } {
      val cellPanel = new CellPanel(row, column, controller)
      cells(row)(column) = cellPanel
      contents += cellPanel
      cellPanel.border = LineBorder(java.awt.Color.YELLOW,1)
      listenTo(cellPanel)
    }
  }
  val gameMessage = new TextField(controller.gameMessage, 20)

  def handcardPanel(playerType: PlayerType.Value) = {
    val handSize = choosePlayer.choice(playerType).player(c).handCard.size
    //new GridPanel(1,handSize) {
    new FlowPanel() {
      preferredSize = new Dimension(105*handSize, 65)
      border = LineBorder(java.awt.Color.BLACK, 2)
      background = java.awt.Color.BLACK
      for {
        index <- 0 until handSize
      } {
        val cardPanel = new CardPanel(playerType,index,controller)
        playerType match {
          case TOP => topHand(index) = cardPanel
          case BOT => botHand(index) = cardPanel
        }
        contents += cardPanel
        listenTo(cardPanel)
      }
    }
  }

  contents = new BorderPanel {
    add(handcardPanel(TOP), BorderPanel.Position.North)
    add(gridPanel, BorderPanel.Position.Center)
    add(handcardPanel(BOT), BorderPanel.Position.South)
    add(gameMessage, BorderPanel.Position.East)
  }

  menuBar = new MenuBar {
    contents += new Menu("File") {
      mnemonic = Key.F
      contents += new MenuItem(Action("Quit") {System.exit(0)})
    }
    contents += new Menu("Edit") {
      mnemonic = Key.E
      contents += new MenuItem(Action("Undo") { controller.undo })
      contents += new MenuItem(Action("Redo") { controller.redo })
    }
    contents += new Menu("End Round") {
      mnemonic = Key.P
      contents += new MenuItem(Action("Pass") { controller.passRound() })
    }
  }

  visible = true
  redraw

  reactions += {
    case event: CellChanged   => redraw
    case event: PlayerChanged => redraw
  }

  def redraw = {
    for {
      row <- 0 until 4
      column <- 0 until 4
    } cells(row)(column).redraw
    for (index <- 0 until c.playerTop.handCard.size) topHand(index).redraw
    for (index <- 0 until botHand.length) botHand(index).redraw
    gameMessage.text = controller.gameMessage
    repaint
  }
}