package de.htwg.se.TradingGame.model.EvalMapDesign

class EvalTradeData extends IEvalTradeData {
  var stopLossList: List[String] = List()
  var takeProfitList: List[String] = List()
  var indicatorList: List[String] = List()
  var pricePointList: List[String] = List()

  def addStopLoss(data: String): Unit = {
    stopLossList = stopLossList :+ data
  }

  def addTakeProfit(data: String): Unit = {
    takeProfitList = takeProfitList :+ data
  }

  def addIndicator(value: String): Unit = {
    indicatorList = indicatorList :+ value
  }

  def addPricePoint(data: String): Unit = {
    pricePointList = pricePointList :+ data
  }

  def getStopLoss: List[String] = {
    stopLossList
  }

  def getTakeProfit: List[String] = {
    takeProfitList
  }

  def getIndicator: List[String] = {
    indicatorList
  }

  def getPricePoint: List[String] = {
    pricePointList
  }
}