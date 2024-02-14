package de.htwg.se.TradingGame.model.EvalMapDesign

trait ITradeDataInput {
  def addStopLoss(stopLossPriceList: List[Double]): Unit
  def addTakeProfit(takeProfitPriceList: List[Double]): Unit
  def addIndicator(indicatorValueList: List[Double]): Unit
  def addPricePoint(pricePointValueList: List[Double], dateList: List[String]): Unit
  def getStopLossData: Map[String, Double]
  def getTakeProfitData: Map[String, Double]
  def getIndicatorData: Map[String, Double]
  def getPricePointData: Map[String, (Double, String)]
}