package de.htwg.se.TradingGame.model.TradeDecoratorPattern.Decorator.ConcreteDecorators

import de.htwg.se.TradingGame.model.GameStateManagerFolder.IGameStateManager
import de.htwg.se.TradingGame.model.ProfitcalculationStrategyPattern.ConcreteFactorieCreators._
import de.htwg.se.TradingGame.model.ProfitcalculationStrategyPattern._
import de.htwg.se.TradingGame.model.TradeDecoratorPattern.Decorator.ConcreteDecorators.GetTradeData
import de.htwg.se.TradingGame.model.TradeDecoratorPattern.Decorator.ConcreteDecorators.GetTradeData.GetMarketDataforTradeDonecalculationsimpl._
import de.htwg.se.TradingGame.model.TradeDecoratorPattern.Decorator.TradeDecorator
import de.htwg.se.TradingGame.model.TradeDecoratorPattern.ProfitcalculationStrategyPattern._
import de.htwg.se.TradingGame.model.TradeDecoratorPattern._

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class TradeDoneCalculations private (trade: TradeComponent, gameStateManager: IGameStateManager) extends TradeDecorator(trade) {
  var dateTradeTriggered: String = _
  var tradeWinOrLose: String = _
  var dateTradeDone: String = _
  var currentprofit: Double = _
  var endProfit: Double = _
  var riskRewardRatio: Double = _
  var rRMade: Double = _
  var maxRbeforeStopLoss: Double = _ //TODO:
  var minStoploss: Double = _//TODO:

  // First constructor that performs calculations
  def this(trade: TradeComponent, gameStateManager: IGameStateManager, number: Integer) = {
    this(trade, gameStateManager)
    val getMarketData = new GetMarketDataforTradeDoneCalculations(trade, gameStateManager)
    dateTradeTriggered = getMarketData.dateTradeTriggered
    tradeWinOrLose = getMarketData.didTradeWinorLoose
    dateTradeDone = getMarketData.dateTradeDone
    currentprofit = 0.0

    val creator: ProfitCalculationStrategyCreator = tradeWinOrLose match {
      case "Trade hit take profit" => new TakeProfitCalculationStrategyCreator()
      case "Trade hit stop loss" => new StopLossCalculationStrategyCreator()
      case "Trade did not hit take profit or stop loss" => new ProfitsetttoZeroStrategyCreator()
      case _ => new ProfitsetttoZeroStrategyCreator()
    }

    val strategy: ProfitCalculationStrategy = creator.createProfitCalculationStrategy(trade)
    endProfit = strategy.calculateProfit(trade) * gameStateManager.currentState.startbalance
    riskRewardRatio = ((trade.takeProfitTrade - trade.entryTrade) / (trade.entryTrade - trade.stopLossTrade)).abs
    rRMade = if(tradeWinOrLose == "Trade hit take profit") riskRewardRatio else -1.0
  }

  // Second constructor that takes all values as inputs and doesn't perform calculations
  def this(trade: TradeComponent, dateTradeTriggered: String, tradeWinOrLose: String, dateTradeDone: String, currentprofit: Double, endProfit: Double, gameStateManager: IGameStateManager) = {
    this(trade, gameStateManager)
    this.dateTradeTriggered = dateTradeTriggered
    this.tradeWinOrLose = tradeWinOrLose
    this.dateTradeDone = dateTradeDone
    this.currentprofit = currentprofit
    this.endProfit = endProfit
    this.riskRewardRatio = ((trade.takeProfitTrade - trade.entryTrade) / (trade.entryTrade - trade.stopLossTrade)).abs
    this.rRMade = if(tradeWinOrLose == "Trade hit take profit") riskRewardRatio else -1.0
  }
}

   // def calculateCurrentProfit(trade: TradeDoneCalculations, volume: Double, currentPrice: Double, currentDate: String): Unit= {
  //   trade.currentprofit = 0.0
  //   if(trade.dateTradeTriggered.equals("Trade was not triggered")){

  //   } else {
  //   val formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd,HH:mm")
  //   val parsedCurrentDate = LocalDateTime.parse(currentDate, formatter)
  //   val parsedDateTradeTriggered = LocalDateTime.parse(trade.dateTradeTriggered, formatter)
  //   val parsedDateTradeDone = LocalDateTime.parse(trade.dateTradeDone, formatter)

    
  //   if(parsedCurrentDate.isAfter(parsedDateTradeTriggered)) {
  //     if(parsedCurrentDate.isBefore(parsedDateTradeDone)) {
  //       trade.currentprofit = BigDecimal((currentPrice - trade.entryTrade) * volume).setScale(2, BigDecimal.RoundingMode.HALF_UP).toDouble
  //     } else {
  //       if(trade.tradeWinOrLose == "Trade hit take profit") {
  //         trade.currentprofit = BigDecimal(scala.math.abs((trade.takeProfitTrade - trade.entryTrade) * volume)).setScale(2, BigDecimal.RoundingMode.HALF_UP).toDouble
  //       }else if(trade.tradeWinOrLose == "Trade hit stop loss"){
  //         trade.currentprofit = BigDecimal(scala.math.abs((trade.stopLossTrade - trade.entryTrade) * volume) * -1).setScale(2, BigDecimal.RoundingMode.HALF_UP).toDouble
  //       } else {
  //         trade.currentprofit = 0.0
  //       }
  //     }
  //     }
  //   }
  // }
