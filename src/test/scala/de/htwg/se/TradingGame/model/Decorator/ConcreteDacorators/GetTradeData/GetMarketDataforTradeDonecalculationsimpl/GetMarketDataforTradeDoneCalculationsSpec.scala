package de.htwg.se.TradingGame.model.Decorator.ConcreteDacorators.GetTradeData.GetMarketDataforTradeDonecalculationsimpl

import de.htwg.se.TradingGame.model.GameStateManagerFolder.FileIO.FileIOXMLComponent.TradeDataXMLFileIO
import de.htwg.se.TradingGame.model.GameStateManagerFolder.GameStateFolder.GameState
import de.htwg.se.TradingGame.model.GameStateManagerFolder.GameStateManagerImplementation.GameStateManager
import de.htwg.se.TradingGame.model.GameStateManagerFolder.IGameStateManager
import de.htwg.se.TradingGame.model.TradeDecoratorPattern.Decorator.ConcreteDecorators.GetTradeData.GetMarketDataforTradeDonecalculationsimpl.GetMarketDataforTradeDoneCalculations
import de.htwg.se.TradingGame.model.TradeDecoratorPattern.Decorator.ConcreteDecorators.TradeDoneCalculations
import de.htwg.se.TradingGame.model.TradeDecoratorPattern.Decorator.Trade
import de.htwg.se.TradingGame.model.TradeDecoratorPattern.TradeComponent
import org.mockito.ArgumentMatchers._
import org.mockito.Mockito._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Timestamp
import java.time.Clock
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

class GetMarketDataforTradeDoneCalculationsSpec extends AnyFlatSpec with Matchers {
  val tradenevertriggerslong = new Trade(0.8, 0.6, 1.1, 2.0, "2023.01.09,12:12", "EURUSD")
  val tradenwvertriggersshort = new Trade(2.0, 2.1, 1.0, 2.0, "2023.01.09,12:12", "EURUSD")
  val tradehittakeprofitlong = new Trade(1.07409, 1.07189, 1.08170, 2.0, "2023.01.12,11:12", "EURUSD")
  val tradehittakeprofitshort = new Trade(1.08474, 1.08777, 1.08174, 2.0, "2023.01.19,04:00", "EURUSD")
  val tradehitstoplosslong = new Trade(1.0812, 1.07905, 1.09106, 2.0, "2023.01.13,00:00", "EURUSD")
  val tradehitstoplossshort = new Trade(1.08513, 1.08786, 1.07509, 2.0, "2023.01.19,12:00", "EURUSD")
  val tradeafterlastdate = new Trade(1.08513, 1.08786, 1.07509, 2.0, "2024.01.24,12:00", "EURUSD")
  val tradedidnothittakeprofitnorstoploss = new Trade(1.08513, 2.0, 1.0, 2.0, "2023.01.19,12:00", "EURUSD")
  val tradehitstoplossbutnotakeprofitshort = new Trade(1.0812, 1.07905, 3.0, 2.0, "2023.01.13,00:00", "EURUSD")
  val tradehittakeprofitbutnotstoplosslong = new Trade(1.07409, 0.9, 1.08170, 2.0, "2023.01.12,11:12", "EURUSD")

  val fileio = new TradeDataXMLFileIO
  val gameStateManager: IGameStateManager = new GameStateManager(fileio)
  gameStateManager.changeDatabaseConnectionString("jdbc:sqlite:testDatabase.db")
  gameStateManager.changeStartBalance(1000.0)
  
  val tradeDoneCalculationstradenevertriggerslong = new TradeDoneCalculations(tradenevertriggerslong, gameStateManager)
  val tradeDoneCalculationstradenwvertriggersshort = new TradeDoneCalculations(tradenwvertriggersshort, gameStateManager)
  val tradeDoneCalculationstradehittakeprofitlong = new TradeDoneCalculations(tradehittakeprofitlong, gameStateManager)
  val tradeDoneCalculationstradehittakeprofitshort = new TradeDoneCalculations(tradehittakeprofitshort, gameStateManager)
  val tradeDoneCalculationstradehitstoplosslong = new TradeDoneCalculations(tradehitstoplosslong, gameStateManager)
  val tradeDoneCalculationstradehitstoplossshort = new TradeDoneCalculations(tradehitstoplossshort, gameStateManager)
  val tradeDoneCalculationstradeafterlastdate = new TradeDoneCalculations(tradeafterlastdate, gameStateManager)
  val tradeDoneCalculationstradedidnothittakeprofitnorstoploss = new TradeDoneCalculations(tradedidnothittakeprofitnorstoploss, gameStateManager)
  val tradeDoneCalculationstradehitstoplossbutnotakeprofitshort = new TradeDoneCalculations(tradehitstoplossbutnotakeprofitshort, gameStateManager)
  val tradeDoneCalculationstradehittakeprofitbutnotstoplosslong = new TradeDoneCalculations(tradehittakeprofitbutnotstoplosslong, gameStateManager)



  "tradenevertriggerslong" should "have right currentprofit: 0.0" in {
      tradeDoneCalculationstradenevertriggerslong.currentprofit should be(0.0)
    }

    it should "have right endProfit: 0.0" in {
      tradeDoneCalculationstradenevertriggerslong.endProfit should be(0.0)
    }

    it should "have right tradeWinOrLose: Trade was not triggered" in {
      tradeDoneCalculationstradenevertriggerslong.tradeWinOrLose should be("Trade was not triggered")
    }

    it should "have right dateTradeDone: Trade was not triggered" in {
      tradeDoneCalculationstradenevertriggerslong.dateTradeDone should be("Trade was not triggered")
    }

    it should "have right dateTradeTriggered: Trade was not triggered" in {
      tradeDoneCalculationstradenevertriggerslong.dateTradeTriggered should be("Trade was not triggered")
    }

  "tradenwvertriggersshort" should "have right currentprofit: 0.0" in {
      tradeDoneCalculationstradenwvertriggersshort.currentprofit should be(0.0)
    }

    it should "have right endProfit: 0.0" in {
      tradeDoneCalculationstradenwvertriggersshort.endProfit should be(0.0)
    }

    it should "have right tradeWinOrLose: Trade was not triggered" in {
      tradeDoneCalculationstradenwvertriggersshort.tradeWinOrLose should be("Trade was not triggered")
    }

    it should "have right dateTradeDone: Trade was not triggered" in {
      tradeDoneCalculationstradenwvertriggersshort.dateTradeDone should be("Trade was not triggered")
    }

    it should "have right dateTradeTriggered: Trade was not triggered" in {
      tradeDoneCalculationstradenwvertriggersshort.dateTradeTriggered should be("Trade was not triggered")
    }

  "tradehittakeprofitlong" should "have right currentprofit: 0.0" in {
      tradeDoneCalculationstradehittakeprofitlong.currentprofit should be(0.0)
    }

    it should "have right endProfit: 69.18" in {
      tradeDoneCalculationstradehittakeprofitlong.endProfit should be(69.18)
    }

    it should "have right tradeWinOrLose: Trade hit take profit" in {
      tradeDoneCalculationstradehittakeprofitlong.tradeWinOrLose should be("Trade hit take profit")
    }

    it should "have right dateTradeDone: 2023.01.12,15:54" in {
      tradeDoneCalculationstradehittakeprofitlong.dateTradeDone should be("2023.01.12,15:54")
    }

    it should "have right dateTradeTriggered: 2023.01.12,15:30" in {
      tradeDoneCalculationstradehittakeprofitlong.dateTradeTriggered should be("2023.01.12,15:30")
    }

  "tradehittakeprofitshort" should "have right currentprofit: 0.0" in {
      tradeDoneCalculationstradehittakeprofitshort.currentprofit should be(0.0)
    }

    it should "have right endProfit: 19.8" in {
      tradeDoneCalculationstradehittakeprofitshort.endProfit should be(19.8)
    }

    it should "have right tradeWinOrLose: Trade hit take profit" in {
      tradeDoneCalculationstradehittakeprofitshort.tradeWinOrLose should be("Trade hit take profit")
    }

    it should "have right dateTradeDone: 2023.01.20,14:16" in {
      tradeDoneCalculationstradehittakeprofitshort.dateTradeDone should be("2023.01.20,14:16")
    }

    it should "have right dateTradeTriggered: 2023.01.20,10:07" in {
      tradeDoneCalculationstradehittakeprofitshort.dateTradeTriggered should be("2023.01.20,10:07")
    }

  "tradehitstoplosslong" should "have right currentprofit: 0.0" in {
      tradeDoneCalculationstradehitstoplosslong.currentprofit should be(0.0)
    }

    it should "have right endProfit: -20.0" in {
      tradeDoneCalculationstradehitstoplosslong.endProfit should be(-20.0)
    }

    it should "have right tradeWinOrLose: Trade hit stop loss" in {
      tradeDoneCalculationstradehitstoplosslong.tradeWinOrLose should be("Trade hit stop loss")
    }

    it should "have right dateTradeDone: 2023.01.13,00:00" in {
      tradeDoneCalculationstradehitstoplosslong.dateTradeDone should be("2023.01.13,16:01")
    }

    it should "have right dateTradeTriggered: 2023.01.12,15:30" in {
      tradeDoneCalculationstradehitstoplosslong.dateTradeTriggered should be("2023.01.13,13:32")
    }

  "tradehitstoplossshort" should "have right currentprofit: 0.0" in {
      tradeDoneCalculationstradehitstoplossshort.currentprofit should be(0.0)
    }

    it should "have right endProfit: -20.0" in {
      tradeDoneCalculationstradehitstoplossshort.endProfit should be(-20.0)
    }

    it should "have right tradeWinOrLose: Trade hit stop loss" in {
      tradeDoneCalculationstradehitstoplossshort.tradeWinOrLose should be("Trade hit stop loss")
    }

    it should "have right dateTradeDone: 2023.01.23,12:00" in {
      tradeDoneCalculationstradehitstoplossshort.dateTradeDone should be("2023.01.23,02:48")
    }

    it should "have right dateTradeTriggered: 2023.01.20,10:07" in {
      tradeDoneCalculationstradehitstoplossshort.dateTradeTriggered should be("2023.01.20,10:08")
    }
  
  "tradeafterlastdate" should "have right currentprofit: 0.0" in {
      tradeDoneCalculationstradeafterlastdate.currentprofit should be(0.0)
    }

    it should "have right endProfit: 0.0" in {
      tradeDoneCalculationstradeafterlastdate.endProfit should be(0.0)
    }

    it should "have right tradeWinOrLose: Trade was not triggered" in {
      tradeDoneCalculationstradeafterlastdate.tradeWinOrLose should be("Trade was not triggered")
    }

    it should "have right dateTradeDone: Trade was not triggered" in {
      tradeDoneCalculationstradeafterlastdate.dateTradeDone should be("Trade was not triggered")
    }

    it should "have right dateTradeTriggered: Trade was not triggered" in {
      tradeDoneCalculationstradeafterlastdate.dateTradeTriggered should be("Trade was not triggered")
    }

  "tradedidnothittakeprofitnorstoploss" should "have right currentprofit: 0.0" in {
      tradeDoneCalculationstradedidnothittakeprofitnorstoploss.currentprofit should be(0.0)
    }

    it should "have right endProfit: 0.0" in {
      tradeDoneCalculationstradedidnothittakeprofitnorstoploss.endProfit should be(0.0)
    }

    it should "have right tradeWinOrLose: Trade did not hit take profit or stop loss" in {
      tradeDoneCalculationstradedidnothittakeprofitnorstoploss.tradeWinOrLose should be("Trade did not hit take profit or stop loss")
    }

    it should "have right dateTradeDone: Trade did not hit take profit or stop loss" in {
      tradeDoneCalculationstradedidnothittakeprofitnorstoploss.dateTradeDone should be("Trade did not hit take profit or stop loss")
    }

    it should "have right dateTradeTriggered: 2023.01.20,10:08" in {
      tradeDoneCalculationstradedidnothittakeprofitnorstoploss.dateTradeTriggered should be("2023.01.20,10:08")
    }


  "tradeDoneCalculationstradehitstoplossbutnotakeprofitshort" should "have right currentprofit: 0.0" in {
      tradeDoneCalculationstradehitstoplossbutnotakeprofitshort.currentprofit should be(0.0)
    }

    it should "have right endProfit: -20.0" in {
      tradeDoneCalculationstradehitstoplossbutnotakeprofitshort.endProfit should be(-20.0)
    }

    it should "have right tradeWinOrLose: Trade hit stop loss" in {
      tradeDoneCalculationstradehitstoplossbutnotakeprofitshort.tradeWinOrLose should be("Trade hit stop loss")
    }

    it should "have right dateTradeDone: 2023.01.13,00:00" in {
      tradeDoneCalculationstradehitstoplossbutnotakeprofitshort.dateTradeDone should be("2023.01.13,16:01")
    }

    it should "have right dateTradeTriggered: 2023.01.13,13:32" in {
      tradeDoneCalculationstradehitstoplossbutnotakeprofitshort.dateTradeTriggered should be("2023.01.13,13:32")
    }

  "tradeDoneCalculationstradehittakeprofitbutnotstoplosslong" should "have right currentprofit: 0.0" in {
      tradeDoneCalculationstradehittakeprofitbutnotstoplosslong.currentprofit should be(0.0)
    }

    it should "have right endProfit: 0.87" in {
      tradeDoneCalculationstradehittakeprofitbutnotstoplosslong.endProfit should be(0.87)
    }

    it should "have right tradeWinOrLose: Trade hit take profit" in {
      tradeDoneCalculationstradehittakeprofitbutnotstoplosslong.tradeWinOrLose should be("Trade hit take profit")
    }

    it should "have right dateTradeDone: 2023.01.12,15:54" in {
      tradeDoneCalculationstradehittakeprofitbutnotstoplosslong.dateTradeDone should be("2023.01.12,15:54")
    }

    it should "have right dateTradeTriggered: 2023.01.12,15:30" in {
      tradeDoneCalculationstradehittakeprofitbutnotstoplosslong.dateTradeTriggered should be("2023.01.12,15:30")
    }


  

}