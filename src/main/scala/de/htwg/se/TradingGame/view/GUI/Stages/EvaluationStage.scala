package de.htwg.se.TradingGame.view.GUI.Stages

import de.htwg.se.TradingGame.Main.controller
import de.htwg.se.TradingGame.controller.IController
import de.htwg.se.TradingGame.model.TradeDecoratorPattern.Decorator.ConcreteDecorators.TradeDoneCalculations
import de.htwg.se.TradingGame.model.TradeDecoratorPattern.Decorator.ConcreteDecorators.TradeWithVolume
import de.htwg.se.TradingGame.model.TradeDecoratorPattern.Decorator.ConcreteDecorators.TradeisBuy
import scalafx.Includes._
import scalafx.application.JFXApp3
import scalafx.beans.property.ObjectProperty
import scalafx.beans.property.StringProperty
import scalafx.collections.ObservableBuffer
import scalafx.geometry.Insets
import scalafx.scene.Scene
import scalafx.scene.chart.LineChart
import scalafx.scene.chart.NumberAxis
import scalafx.scene.chart.XYChart
import scalafx.scene.control.Button
import scalafx.scene.control.Label
import scalafx.scene.control.SplitPane
import scalafx.scene.control.SplitPane.Divider
import scalafx.scene.control.TableColumn
import scalafx.scene.control.TableColumn.CellDataFeatures
import scalafx.scene.control.TableView
import scalafx.scene.control.TextField
import scalafx.scene.layout.HBox
import scalafx.scene.layout.VBox
import scalafx.scene.chart.PieChart

object EvaluationStage extends JFXApp3 {
  override def start(): Unit = EvaluationStage(controller).createStage().show()
}

class EvaluationStage(controller: IController) {



  def createStage(): JFXApp3.PrimaryStage = {
    controller.interpreter.gameStateManager.changeSaveName("EURUSDEval")
    controller.interpreter.gameStateManager.changeDatabaseConnectionString("jdbc:sqlite:src/main/scala/de/htwg/se/TradingGame/Database/litedbCandleSticks.db")
    controller.interpreter.gameStateManager.loadCurrentState()
    val entryCollum = new TableColumn[TradeDoneCalculations, Double] {
            text = "Entry Price"
            cellValueFactory = { (features: CellDataFeatures[TradeDoneCalculations, Double]) => 
                ObjectProperty(features.value.entryTrade)
                }
            }
        val stoplossCollum = new TableColumn[TradeDoneCalculations, Double] {
            text = "Stop Loss"
            cellValueFactory = { (features: CellDataFeatures[TradeDoneCalculations, Double]) => 
                ObjectProperty(features.value.stopLossTrade)
                }
            }
        val takeprofitCollum = new TableColumn[TradeDoneCalculations, Double] {
            text = "Take Profit"
            cellValueFactory = { (features: CellDataFeatures[TradeDoneCalculations, Double]) => 
                ObjectProperty(features.value.takeProfitTrade)
                }
            }
        val riskCollum = new TableColumn[TradeDoneCalculations, Double] {
            text = "Risk"
            cellValueFactory = { (features: CellDataFeatures[TradeDoneCalculations, Double]) => 
                ObjectProperty(features.value.risk)
                }
            }
        val dateCollum = new TableColumn[TradeDoneCalculations, String] {
            text = "Date"
            cellValueFactory = { (features: CellDataFeatures[TradeDoneCalculations, String]) => 
                ObjectProperty(features.value.datestart)
                }
            }
        val tickerCollum = new TableColumn[TradeDoneCalculations, String] {
            text = "Ticker"
            cellValueFactory = { (features: CellDataFeatures[TradeDoneCalculations, String]) => 
                ObjectProperty(features.value.ticker)
                }
            }
        val volumeCollum = new TableColumn[TradeDoneCalculations, Double] {
            text = "Volume"
            cellValueFactory = { (features: CellDataFeatures[TradeDoneCalculations, Double]) => 
                ObjectProperty(new TradeWithVolume(features.value, controller.interpreter.gameStateManager.currentState.balance).volume)
            }
        }
        val tradebuysell = new TableColumn[TradeDoneCalculations, String] {
            text = "Type"
            cellValueFactory = { (features: CellDataFeatures[TradeDoneCalculations, String]) => 
                ObjectProperty(new TradeisBuy(features.value).isTradeBuy)
            }
        }
        val dateTradeTriggeredCollum = new TableColumn[TradeDoneCalculations, String] {
            text = "Date Trade Triggered"
            cellValueFactory = { (features: CellDataFeatures[TradeDoneCalculations, String]) => 
                ObjectProperty(features.value.dateTradeTriggered)
                }
            }
        val tradeWinOrLoseCollum = new TableColumn[TradeDoneCalculations, String] {
            text = "Trade Win or Lose"
            cellValueFactory = { (features: CellDataFeatures[TradeDoneCalculations, String]) => 
                ObjectProperty(features.value.tradeWinOrLose)
                }
            }
        val dateTradeDoneCollum = new TableColumn[TradeDoneCalculations, String] {
            text = "Date Trade Done"
            cellValueFactory = { (features: CellDataFeatures[TradeDoneCalculations, String]) => 
                ObjectProperty(features.value.dateTradeDone)
                }
            }
        val endProfitCollum = new TableColumn[TradeDoneCalculations, Double] {
            text = "End Profit"
            cellValueFactory = { (features: CellDataFeatures[TradeDoneCalculations, Double]) => 
                ObjectProperty(features.value.endProfit)
                }
            }
        val RiskRewardRatioCollum = new TableColumn[TradeDoneCalculations, Double] {
        text = "Risk Reward Ratio"
        cellValueFactory = { (features: CellDataFeatures[TradeDoneCalculations, Double]) => 
            ObjectProperty(BigDecimal(features.value.riskRewardRatio).setScale(2, BigDecimal.RoundingMode.HALF_UP).toDouble)
        }
        }

        val rRMadeCollum = new TableColumn[TradeDoneCalculations, Double] {
            text = "RR Made"
            cellValueFactory = { (features: CellDataFeatures[TradeDoneCalculations, Double]) => 
                ObjectProperty(BigDecimal(features.value.rRMade).setScale(2, BigDecimal.RoundingMode.HALF_UP).toDouble)
            }
        }
      val tradesBuffer = ObservableBuffer[TradeDoneCalculations]()
        tradesBuffer ++= controller.interpreter.gameStateManager.currentState.doneTrades

     val table = new TableView[TradeDoneCalculations](tradesBuffer) {
            columns ++= List(dateCollum, tradebuysell, volumeCollum, riskCollum, tickerCollum, entryCollum, stoplossCollum, takeprofitCollum, dateTradeTriggeredCollum, tradeWinOrLoseCollum, dateTradeDoneCollum, endProfitCollum, RiskRewardRatioCollum, rRMadeCollum)
        }




    val lineChartData = ObservableBuffer[javafx.scene.chart.XYChart.Data[Number, Number]]()
    val sortedTrades = controller.interpreter.gameStateManager.currentState.doneTrades.sortBy(_.dateTradeDone)
    
    var balance = controller.interpreter.gameStateManager.currentState.startbalance
    lineChartData += XYChart.Data[Number, Number](0, balance)
    sortedTrades.zipWithIndex.foreach { case (trade, index) =>
    balance += trade.endProfit
    lineChartData += XYChart.Data[Number, Number](index+1, balance)
    }
    val balances = sortedTrades.scanLeft(controller.interpreter.gameStateManager.currentState.startbalance) {
    case (balance, trade) => balance + trade.endProfit
    }
    
    val xAxis = NumberAxis()
    val yAxis = NumberAxis()
    xAxis.label = "Trade Number"
    yAxis.label = "Balance"
    yAxis.autoRanging = false
    yAxis.lowerBound = balances.min
    yAxis.upperBound = balances.max
    yAxis.tickUnit = (yAxis.upperBound.toDouble - yAxis.lowerBound.toDouble) / 100  

    val lineChartSeries = XYChart.Series[Number, Number]("Balance Over Time", lineChartData)
    val lineChart = LineChart[Number, Number](xAxis, yAxis)
    lineChart.data = ObservableBuffer(lineChartSeries)


    val lineChartDataRR = ObservableBuffer[javafx.scene.chart.XYChart.Data[Number, Number]]()
    var rRstart = 0.0
    lineChartDataRR += XYChart.Data[Number, Number](0, rRstart)
    sortedTrades.zipWithIndex.foreach { case (trade, index) =>
    rRstart += trade.rRMade
    lineChartDataRR += XYChart.Data[Number, Number](index+1, rRstart)
    }
    val riskreward = sortedTrades.scanLeft(0.0) {
    case (rRstart, trade) => rRstart + trade.rRMade
    }
    
    val xAxisRR = NumberAxis()
    val yAxisRR = NumberAxis()
    xAxisRR.label = "Trade Number"
    yAxisRR.label = "R"


  
    val lineChartSeriesRR = XYChart.Series[Number, Number]("R Over Time", lineChartDataRR)
    val lineChartRR = LineChart[Number, Number](xAxisRR, yAxisRR)
    lineChartRR.data = ObservableBuffer(lineChartSeriesRR)

    val charts = new VBox {
    children.addAll(lineChart, lineChartRR)
}

  val totalTrades = tradesBuffer.length
val wonTrades = tradesBuffer.count(_.tradeWinOrLose == "Trade hit take profit")
val lostTrades = tradesBuffer.count(_.tradeWinOrLose == "Trade hit stop loss")

val pieChartData = ObservableBuffer(
    PieChart.Data("Won", wonTrades),
    PieChart.Data("Lost", lostTrades)
)

val pieChart = new PieChart {
    data = pieChartData
    title = s"Win Rate: ${BigDecimal(wonTrades.toDouble / totalTrades.toDouble * 100).setScale(2, BigDecimal.RoundingMode.HALF_UP)}%"
}

// Set the pie chart colors
    pieChartData(0).getNode.setStyle("-fx-pie-color: #1BFF1A;")
    pieChartData(1).getNode.setStyle("-fx-pie-color: #FF201A;")
val statsBox = new VBox {
    children = List(
        new Label(s"Total Trades: $totalTrades"),
        new Label(s"Won Trades: $wonTrades"),
        new Label(s"Lost Trades: $lostTrades"),
        pieChart,
        new Label(s"overall R: ${BigDecimal(riskreward.last).setScale(2, BigDecimal.RoundingMode.HALF_UP)}"),
        new Label(s"expectancy per Trade R: ${BigDecimal(riskreward.last / totalTrades).setScale(2, BigDecimal.RoundingMode.HALF_UP)}"),
        new Label(s"Max Drawdown %: ${BigDecimal((balances.max - balances.min) / balances.max * 100).setScale(2, BigDecimal.RoundingMode.HALF_UP)}"),
        new Label(s"Average won  R: ${BigDecimal(riskreward.filter(_ > 0).sum / wonTrades).setScale(2, BigDecimal.RoundingMode.HALF_UP)}"), 
    )
    padding = Insets(10)
}

new JFXApp3.PrimaryStage {
    title = "Evaluation Stage"
    scene = new Scene {
        root = new SplitPane {
            items.addAll(table, statsBox, charts)
        }
        stylesheets.add("de/htwg/se/TradingGame/view/GUI/CSS/darkmode.css")
    }
}
}
  }