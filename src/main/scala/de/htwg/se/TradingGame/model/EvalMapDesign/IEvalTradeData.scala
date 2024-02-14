package de.htwg.se.TradingGame.model.EvalMapDesign

trait IEvalTradeData {
  def addStopLoss(data: String): Unit
  def addTakeProfit(data: String): Unit
  def addIndicator(value: String): Unit
  def addPricePoint(data: String): Unit
  def getStopLoss: List[String]
  def getTakeProfit: List[String]
  def getIndicator: List[String]
  def getPricePoint: List[String]
}