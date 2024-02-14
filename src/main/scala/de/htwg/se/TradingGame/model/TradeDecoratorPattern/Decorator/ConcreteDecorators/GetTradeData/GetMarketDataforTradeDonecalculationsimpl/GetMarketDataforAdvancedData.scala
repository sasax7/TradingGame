package de.htwg.se.TradingGame.model.TradeDecoratorPattern.Decorator.ConcreteDecorators.GetTradeData.GetMarketDataforTradeDonecalculationsimpl


import de.htwg.se.TradingGame.model.GameStateManagerFolder.IGameStateManager
import de.htwg.se.TradingGame.model.TradeDecoratorPattern.Decorator.TradeDecorator
import de.htwg.se.TradingGame.model.EvalMapDesign.ITradeDataInput
import de.htwg.se.TradingGame.model.TradeDecoratorPattern.TradeComponent
import java.sql.DriverManager
import java.sql.Connection
import java.time.LocalDateTime
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.time.format.DateTimeFormatter

class GetMarketDataforAdvancedData(trade: TradeComponent, gameStateManager: IGameStateManager, mapdata: ITradeDataInput) extends TradeDecorator(trade) {
    val formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd,HH:mm")
    val outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    var conn: Connection = null
    val url = gameStateManager.currentState.databaseConnectionString

    var stopLossMap: Map[String, (Double, String, Double, Double, Boolean)] = _ //(Name, StopLossPrice,Date, highest tp price befor hitting sl, highest RR before hitting sl, true if hit 1 RR)
    var takeProfitMap: Map[String, (Double, String, Double, List[Double])] = _//(Name, TakeProfitPrice, Date, lowest sl price befor hitting tp,  RR )
    var dateTradeTriggered: String = _


    val trueifBuy: Boolean = if(trade.takeProfitTrade > trade.stopLossTrade){
    true
  } else {
    false
  }

    try {
        conn = DriverManager.getConnection(url)
        calculateall
    } finally {
        if (conn != null) conn.close()
    }


    def calculateall ={
        dateWhenTradeTriggeredcalculation
        stopLossMapcalculation
        takeProfitMapcalculation
    }


    def dateWhenTradeTriggeredcalculation = {
    var pstmt: PreparedStatement = null
    var rs: ResultSet = null
    var date: String = "Trade was not triggered"

  
      val datestart = LocalDateTime.parse(trade.datestart, formatter)
      val formattedDatestart = datestart.format(outputFormatter)
  

      // Prepare the SQL statement
      val sql = s"""SELECT c.Zeitstempel, c.HighPrice, c.LowPrice FROM Candlestick c
                    JOIN Markt m ON c.MarktID = m.MarktID
                    WHERE m.Marktname = '${trade.ticker}' AND c.TimeframeID = 1 AND datetime(c.Zeitstempel / 1000, 'unixepoch') >= datetime('${formattedDatestart}')
                    ORDER BY c.Zeitstempel"""

      pstmt = conn.prepareStatement(sql)

      // Execute the query and get the result
      rs = pstmt.executeQuery()
    
      // Process the result
      var found = false
      while (rs.next() && !found) {
        val line = rs.getTimestamp("Zeitstempel").toLocalDateTime.format(formatter) + "," + rs.getDouble("HighPrice") + "," + rs.getDouble("LowPrice")
        if (trueifBuy) {
          
          if (LocalDateTime.parse(line.split(",")(0)+ "," + line.split(",")(1), formatter).isAfter(LocalDateTime.parse(trade.datestart, formatter)) && trade.entryTrade > line.split(",")(3).toDouble) {
            date = line.split(",")(0)+ "," + line.split(",")(1)
            found = true
          }
        } else {
          if (LocalDateTime.parse(line.split(",")(0)+ "," + line.split(",")(1), formatter).isAfter(LocalDateTime.parse(trade.datestart, formatter)) && trade.entryTrade < line.split(",")(2).toDouble) {
            date = line.split(",")(0)+ "," + line.split(",")(1)
            found = true
          }
        }
      }

      if (rs != null) rs.close()
      if (pstmt != null) pstmt.close()
    dateTradeTriggered = date
    date
  }

  
  def stopLossMapcalculation = {
  stopLossMap = mapdata.getStopLossData.map { tradeDataInput =>
    val stopLossPrice = tradeDataInput._2
    val (stopLossDate, priceBeforeStopLoss) = getStopLossDateandHighest(stopLossPrice)
    val highestTpPriceBeforeHittingSl = priceBeforeStopLoss
    val highestRrBeforeHittingSl = (highestTpPriceBeforeHittingSl - trade.entryTrade) / (trade.entryTrade - tradeDataInput._2)
    val hit1Rr = if (highestRrBeforeHittingSl >= 1) true else false
    tradeDataInput._1 -> (stopLossPrice, stopLossDate, highestTpPriceBeforeHittingSl, highestRrBeforeHittingSl, hit1Rr)
  }.toMap
}

def getStopLossDateandHighest(stopLossPrice: Double): (String, Double) = {
  var pstmt: PreparedStatement = null
  var rs: ResultSet = null
  var stopLossDate: String = "Stop loss was not hit"
  var priceBeforeStopLoss: Double = if (trueifBuy) Double.MinValue else Double.MaxValue

  // Prepare the SQL statement
  val sql = s"""SELECT c.Zeitstempel, c.LowPrice, c.HighPrice FROM Candlestick c
                JOIN Markt m ON c.MarktID = m.MarktID
                WHERE m.Marktname = '${trade.ticker}' AND c.TimeframeID = 1 AND datetime(c.Zeitstempel / 1000, 'unixepoch') >= datetime('${dateTradeTriggered}')
                ORDER BY c.Zeitstempel"""

  pstmt = conn.prepareStatement(sql)

  // Execute the query and get the result
  rs = pstmt.executeQuery()

  // Process the result
  var found = false
  while (rs.next() && !found) {
    val timestamp = rs.getTimestamp("Zeitstempel").toLocalDateTime.format(formatter)
    val lowPrice = rs.getDouble("LowPrice")
    val highPrice = rs.getDouble("HighPrice")

    if (trueifBuy && lowPrice <= stopLossPrice) {
      stopLossDate = timestamp
      found = true
    } else if (!trueifBuy && highPrice >= stopLossPrice) {
      stopLossDate = timestamp
      found = true
    }

    if (!found) {
      if (trueifBuy) {
        priceBeforeStopLoss = Math.max(priceBeforeStopLoss, highPrice)
      } else {
        priceBeforeStopLoss = Math.min(priceBeforeStopLoss, lowPrice)
      }
    }
  }

  if (rs != null) rs.close()
  if (pstmt != null) pstmt.close()

  (stopLossDate, priceBeforeStopLoss)
}
def takeProfitMapcalculation = {
  takeProfitMap = mapdata.getTakeProfitData.map { tradeDataInput =>
    val takeProfitPrice = tradeDataInput._2
    val (takeProfitDate, priceBeforeTakeProfit) = getTakeProfitDateandLowest(takeProfitPrice)
    val lowestSlPriceBeforeHittingTp = priceBeforeTakeProfit
    val rRforeachSL: List[Double] = mapdata.getStopLossData.map { slDataInput =>
      (trade.entryTrade - lowestSlPriceBeforeHittingTp) / (trade.entryTrade - slDataInput._2)
    }.toList
    tradeDataInput._1 -> (takeProfitPrice, takeProfitDate, lowestSlPriceBeforeHittingTp, rRforeachSL)
  }.toMap
}
def getTakeProfitDateandLowest(takeProfitPrice: Double): (String, Double) = {
  var pstmt: PreparedStatement = null
  var rs: ResultSet = null
  var takeProfitDate: String = "Take profit was not hit"
  var priceBeforeTakeProfit: Double = if (trueifBuy) Double.MaxValue else Double.MinValue

  // Prepare the SQL statement
  val sql = s"""SELECT c.Zeitstempel, c.LowPrice, c.HighPrice FROM Candlestick c
                JOIN Markt m ON c.MarktID = m.MarktID
                WHERE m.Marktname = '${trade.ticker}' AND c.TimeframeID = 1 AND datetime(c.Zeitstempel / 1000, 'unixepoch') >= datetime('${dateTradeTriggered}')
                ORDER BY c.Zeitstempel"""

  pstmt = conn.prepareStatement(sql)

  // Execute the query and get the result
  rs = pstmt.executeQuery()

  // Process the result
  var found = false
  while (rs.next() && !found) {
    val timestamp = rs.getTimestamp("Zeitstempel").toLocalDateTime.format(formatter)
    val lowPrice = rs.getDouble("LowPrice")
    val highPrice = rs.getDouble("HighPrice")

    if (trueifBuy && highPrice >= takeProfitPrice) {
      takeProfitDate = timestamp
      found = true
    } else if (!trueifBuy && lowPrice <= takeProfitPrice) {
      takeProfitDate = timestamp
      found = true
    }

    if (!found) {
      if (trueifBuy) {
        priceBeforeTakeProfit = Math.min(priceBeforeTakeProfit, lowPrice)
      } else {
        priceBeforeTakeProfit = Math.max(priceBeforeTakeProfit, highPrice)
      }
    }
  }

  if (rs != null) rs.close()
  if (pstmt != null) pstmt.close()

  (takeProfitDate, priceBeforeTakeProfit)
}
}