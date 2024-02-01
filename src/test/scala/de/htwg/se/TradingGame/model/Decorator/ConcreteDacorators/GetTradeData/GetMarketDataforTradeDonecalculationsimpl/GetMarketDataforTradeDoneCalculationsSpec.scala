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
import java.time.LocalDateTime

class GetMarketDataforTradeDoneCalculationsSpec extends AnyFlatSpec with Matchers {
  val tradenevertriggerslong = new Trade(0.8, 0.6, 1.1, 2.0, "2023.01.09,12:12", "EURUSD")
  val tradenwvertriggersshort = new Trade(2.0, 2.1, 1.0, 2.0, "2023.01.09,12:12", "EURUSD")
  val tradehittakeprofitlong = new Trade(1.07409, 1.07189, 1.08170, 2.0, "2023.01.12,11:12", "EURUSD")
  val fileio = new TradeDataXMLFileIO
  val gameStateManager: IGameStateManager = new GameStateManager(fileio)
  gameStateManager.changeDatabaseConnectionString("jdbc:sqlite:testDatabase.db")
  
  val tradeDoneCalculationstradenevertriggerslong = new TradeDoneCalculations(tradenevertriggerslong, gameStateManager)
  val tradeDoneCalculationstradenwvertriggersshort = new TradeDoneCalculations(tradenwvertriggersshort, gameStateManager)
  val tradeDoneCalculationstradehittakeprofitlong = new TradeDoneCalculations(tradehittakeprofitlong, gameStateManager)




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

    it should "have right endProfit: 0.06918" in {
      tradeDoneCalculationstradehittakeprofitlong.endProfit should be(0.06918)
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





}