package de.htwg.se.TradingGame.model.EvalMapDesign

class TradeDataInput(evalTradeData: IEvalTradeData) extends ITradeDataInput {
  var stopLossData: Map[String, Double] = Map()
  var takeProfitData: Map[String, Double] = Map()
  var indicatorData: Map[String, Double] = Map()
  var pricePointData: Map[String, (Double, String)] = Map()

  def addStopLoss(stopLossPriceList: List[Double]): Unit = {
    stopLossData = evalTradeData.getStopLoss.zip(stopLossPriceList).toMap
  }

  def addTakeProfit(takeProfitPriceList: List[Double]): Unit = {
    takeProfitData = evalTradeData.getTakeProfit.zip(takeProfitPriceList).toMap
  }

  def addIndicator(indicatorValueList: List[Double]): Unit = {
    indicatorData = evalTradeData.getIndicator.zip(indicatorValueList).toMap
  }

  def addPricePoint(pricePointValueList: List[Double], dateList: List[String]): Unit = {
  pricePointData = evalTradeData.getPricePoint.zip(pricePointValueList.zip(dateList)).toMap
}

  def getStopLossData: Map[String, Double] = {
    stopLossData
  }

  def getTakeProfitData: Map[String, Double] = {
    takeProfitData
  }

  def getIndicatorData: Map[String, Double] = {
    indicatorData
  }

  def getPricePointData: Map[String, (Double, String)] = {
    pricePointData
  }
}