package org.galatea.starter.service;

import feign.Feign;
import java.util.List;
import org.galatea.starter.domain.IexHistoricalPrice;
import org.galatea.starter.domain.IexLastTradedPrice;
import org.galatea.starter.domain.IexSymbol;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * A Feign Declarative REST Client to access endpoints from the Free and Open IEX API to get market
 * data. See https://iextrading.com/developer/docs/
 */
@FeignClient(name = "IEX", url = "${spring.rest.iexBasePath}")
public interface IexClient {
  String token = "${spring.rest.iex.token}$";
  /**
   * Get a list of all stocks supported by IEX. See https://iextrading.com/developer/docs/#symbols.
   * As of July 2019 this returns almost 9,000 symbols, so maybe don't call it in a loop.
   *
   * @return a list of all of the stock symbols supported by IEX.
   */
  @GetMapping("/ref-data/symbols?token=" + token)
  List<IexSymbol> getAllSymbols();

  /**
   * Get the last traded price for each stock symbol passed in. See https://iextrading.com/developer/docs/#last.
   *
   * @param symbols stock symbols to get last traded price for.
   * @return a list of the last traded price for each of the symbols passed in.
   */
  @GetMapping("/tops/last/{symbol}&token=" + token)
  List<IexLastTradedPrice> getLastTradedPriceForSymbols(@RequestParam("symbols") String[] symbols);

  /**
   *
   * @param symbols stock symbols to get historical data for
   * @return a list of the historical price for the stock symbol passed in
   */
  @GetMapping("/stock/{symbol}/chart?token=" + token)
  List<IexHistoricalPrice> getHistoricalPricesForSymbol(@RequestParam("symbol") String symbol);

  /**
   *
   * @param symbols stock symbols to get historical data for
   * @param range time range
   * @return a list of the historical price for the stock symbol passed in and the range passed in
   */
  @GetMapping("/stock/{symbol}/chart/{range}?token=" + token)
  List<IexHistoricalPrice> getHistoricalPricesForRange(@RequestParam("symbol") String symbol, @RequestParam("range") String range);

  /**
   *
   * @param symbols stock symbols to get historical data for
   * @param date specified date to get the price for
   * @return a list of the historical price for the stock symbol passed in and the date passed in
   */
  @GetMapping("/stock/{symbol}/chart/date/{date}?token=" + token)
  List<IexHistoricalPrice> getHistoricalPricesForDate(@RequestParam("symbol") String symbol, @RequestParam("date") String date);



}
