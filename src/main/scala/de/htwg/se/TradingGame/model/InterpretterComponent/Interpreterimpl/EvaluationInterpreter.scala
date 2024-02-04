package de.htwg.se.TradingGame.model.InterpretterComponent.Interpreterimpl

import com.google.inject.Guice
import com.google.inject.Inject
import com.google.inject.Injector
import com.google.inject.name.Names
import de.htwg.se.TradingGame.TradingGameModule
import de.htwg.se.TradingGame.model.GameStateManagerFolder.IGameStateManager
import de.htwg.se.TradingGame.model.InterpretterComponent.Interpreter
import de.htwg.se.TradingGame.model.InterpretterComponent.Interpreterimpl.GetMarktDataforInterpreterFolder.GetMarketDataforInterpreter._
import de.htwg.se.TradingGame.model.InterpretterComponent.Interpreterimpl.GetMarktDataforInterpreterFolder.PrintMarketData
import de.htwg.se.TradingGame.model.InterpretterComponent.Interpreterimpl._
import de.htwg.se.TradingGame.model._
import net.codingwell.scalaguice.InjectorExtensions.*
import org.checkerframework.checker.units.qual.g
import scalafx.scene.input.KeyCode.D

import scala.io.Source

class EvaluationInterpreter @Inject() (val gameStateManager: IGameStateManager) extends Interpreter {




  var descriptor: String = "Please Enter one of the following save:\n\n1. command1\n2. command2\n3. .*"

  val save: String = "save"
  val command2: String = "command2"
  val wrongInput: String = ".*"

  def savecommand(input: String): (String, Interpreter) = {
    // TODO: Implement the action for command1
    gameStateManager.saveCurrentState()
    ("Command1 executed", this)
  }

  def doCommand2(input: String): (String, Interpreter) = {
    // Implement the action for command2
    // Return a message and a new interpreter
    ("Command2 executed", this)
  }

  def doWrongInput(input: String): (String, Interpreter) = ("Invalid input. Please type a valid command", this)

  override def resetState: Interpreter = EvaluationInterpreter(gameStateManager)

  override val actions: Map[String, String => (String, Interpreter)] = Map(
    (save,savecommand),
    (command2, doCommand2),
    (wrongInput, doWrongInput)
  )
}