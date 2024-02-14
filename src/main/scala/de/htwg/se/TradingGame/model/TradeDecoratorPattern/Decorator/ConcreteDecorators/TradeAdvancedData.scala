package de.htwg.se.TradingGame.model.TradeDecoratorPattern.Decorator.ConcreteDecorators

import de.htwg.se.TradingGame.model.GameStateManagerFolder.IGameStateManager
import de.htwg.se.TradingGame.model.ProfitcalculationStrategyPattern.ConcreteFactorieCreators._
import de.htwg.se.TradingGame.model.ProfitcalculationStrategyPattern._
import de.htwg.se.TradingGame.model.TradeDecoratorPattern.Decorator.ConcreteDecorators.GetTradeData
import de.htwg.se.TradingGame.model.TradeDecoratorPattern.Decorator.ConcreteDecorators.GetTradeData.GetMarketDataforTradeDonecalculationsimpl._
import de.htwg.se.TradingGame.model.TradeDecoratorPattern.Decorator.TradeDecorator
import de.htwg.se.TradingGame.model.TradeDecoratorPattern.ProfitcalculationStrategyPattern._
import de.htwg.se.TradingGame.model.TradeDecoratorPattern._
import de.htwg.se.TradingGame.model.EvalMapDesign.ITradeDataInput
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class TradeAdvancedData private (trade: TradeComponent, gameStateManager: IGameStateManager) extends TradeDecorator(trade) {
  var trueifBuy: Boolean = _
  var dateTradeTriggered: String = _
  var stopLossMap: Map[String, (Double, String, Double, Double, Boolean)] = _  //(Name, StopLossPrice,Date, highest tp price befor hitting sl, highest RR before hitting sl, true if hit 1 RR)
  var takeProfitMap: Map[String, (Double, String, Double, List[Double])] = _ //(Name, TakeProfitPrice, Date, lowest sl price befor hitting tp,  RR )
  var indicatorMap: Map[String, Double] = _ //(Name, IndicatorValue)
  var pricePointMap: Map[String,( Double, String)] = _ //(Name, Price, Date)
  // First constructor that performs calculations
  def this(trade: TradeComponent, gameStateManager: IGameStateManager, mapdata: ITradeDataInput) = {
    this(trade, gameStateManager)
    indicatorMap = mapdata.getIndicatorData
    pricePointMap = mapdata.getPricePointData
    val getMarketData = new GetMarketDataforAdvancedData(trade, gameStateManager, mapdata)
    trueifBuy = getMarketData.trueifBuy
    stopLossMap = getMarketData.stopLossMap
    takeProfitMap = getMarketData.takeProfitMap
    dateTradeTriggered = getMarketData.dateTradeTriggered

  }

  // Second constructor that takes all values as inputs and doesn't perform calculations
  def this(trade: TradeComponent, gameStateManager: IGameStateManager, trueifBuy: Boolean, dateTradeTriggered: String, stopLossMap: Map[String, (Double, String, Double, Double, Boolean)], takeProfitMap: Map[String, (Double, String, Double, List[Double])], indicatorMap: Map[String, Double], pricePointMap: Map[String,( Double, String)]) = {
    this(trade, gameStateManager)
    this.trueifBuy = trueifBuy
    this.dateTradeTriggered = dateTradeTriggered
    this.stopLossMap = stopLossMap
    this.takeProfitMap = takeProfitMap
    this.indicatorMap = indicatorMap
    this.pricePointMap = pricePointMap
  }
}


  