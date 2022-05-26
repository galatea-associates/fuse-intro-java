package org.galatea.starter.service;

import java.util.List;

import org.galatea.starter.domain.IexHistoricalPrice;
import org.galatea.starter.domain.IexLastTradedPrice;
import org.galatea.starter.domain.IexSymbol;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * A Feign Declarative REST Client to access endpoints from the Free and Open IEX API to get market
 * data. See https://iextrading.com/developer/docs/
 */
@FeignClient(name = "IEX", url = "${spring.rest.iexBasePath}")
public interface IexClient {

  /**
   * Get a list of all stocks supported by IEX. See https://iextrading.com/developer/docs/#symbols.
   * As of July 2019 this returns almost 9,000 symbols, so maybe don't call it in a loop.
   *
   * @return a list of all of the stock symbols supported by IEX.
   */
  @GetMapping("/ref-data/symbols")
  List<IexSymbol> getAllSymbols();

  /**
   * Get the last traded price for each stock symbol passed in. See https://iextrading.com/developer/docs/#last.
   *
   * @param symbols stock symbols to get last traded price for.
   * @return a list of the last traded price for each of the symbols passed in.
   */
  @GetMapping("/tops/last")
  List<IexLastTradedPrice> getLastTradedPriceForSymbols(@RequestParam("symbols") String[] symbols);

  /**
   * Get the historical price of a stock.
   *
   * @param symbol stock symbol
   * @return
   */
  @GetMapping("/stock/{symbol}/chart?token=${spring.rest.authtoken}")
  List<IexHistoricalPrice> getHistoricalPriceSymbol(@PathVariable(value = "symbol") String symbol);

  /**
   * Get the historical price of a stock.
   *
   * @param symbol symbol for the stock
   * @param range time duration for the stock price
   * @return
   */
  @GetMapping("/stock/{symbol}/chart/{range}?token=${spring.rest.authtoken}")
  List<IexHistoricalPrice> getHistoricalPriceSymbolRange(
          @PathVariable(value = "symbol") String symbol,
          @PathVariable(value = "range") String range);

  /**
   * Get the historical price of a stock.
   *
   * @param symbol symbol for the stock
   * @param date time duration for the stock price
   * @return
   */
  @GetMapping("/stock/{symbol}/chart/date/{date}?token=${spring.rest.authtoken}")
  List<IexHistoricalPrice> getHistoricalPriceSymbolDate(
          @PathVariable(value = "symbol") String symbol,
          @PathVariable(value = "date") String date);

}
