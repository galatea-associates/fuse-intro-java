package org.galatea.starter.entrypoint;

import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.aspect4log.Log;
import net.sf.aspect4log.Log.Level;
import org.galatea.starter.domain.IexHistoricalPrice;
import org.galatea.starter.domain.IexLastTradedPrice;
import org.galatea.starter.domain.IexSymbol;
import org.galatea.starter.service.IexService;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Log(enterLevel = Level.INFO, exitLevel = Level.INFO)
@Validated
@RestController
@RequiredArgsConstructor
public class IexRestController {

  @NonNull
  private IexService iexService;

  /**
   * Exposes an endpoint to get all of the symbols available on IEX.
   *
   * @param token for authorization.
   * @return a list of all IexStockSymbols.
   */
  @GetMapping(value = "${mvc.iex.getAllSymbolsPath}", produces = {MediaType.APPLICATION_JSON_VALUE})
  public List<IexSymbol> getAllStockSymbols(
      @RequestParam(value = "token") final String token) {
    return iexService.getAllSymbols(token);
  }

  /**
   * Get the last traded price for each of the symbols passed in.
   *
   * @param symbols list of symbols to get last traded price for.
   * @param token for authorization.
   * @return a List of IexLastTradedPrice objects for the given symbols.
   */
  @GetMapping(value = "${mvc.iex.getLastTradedPricePath}", produces = {
      MediaType.APPLICATION_JSON_VALUE})
  public List<IexLastTradedPrice> getLastTradedPrice(
      @RequestParam(value = "symbols") final List<String> symbols,
      @RequestParam(value = "token") final String token) {
    return iexService.getLastTradedPriceForSymbols(symbols, token);
  }

  /**
   * Get the historical price for the symbol at each date.
   *
   * @param symbol the symbol for which to get the prices.
   * @param date     (optional) the date on which to get the prices.
   * @param from     (optional) the start date of the interval.
   * @param to       (optional) the end date of the interval.
   * @param interval (optional) takes every n-th entry in the given interval.
   * @param token for authorization.
   * @return if date is provided: historical price data on the date.
   *         if interval is provided: a list of historical price data at every point in the series.
   */
  @GetMapping(value = "${mvc.iex.getHistoricalPricesPath}",
          produces = {MediaType.APPLICATION_JSON_VALUE})
  public List<IexHistoricalPrice> getHistoricalPrice(
      @RequestParam(value = "symbol") final String symbol,
      @RequestParam(value = "date",     required = false) final String date,
      @RequestParam(value = "from",     required = false) final String from,
      @RequestParam(value = "to",       required = false) final String to,
      @RequestParam(value = "interval", required = false) final String interval,
      @RequestParam(value = "token") final String token) {
    return iexService.getHistoricalPricesForSymbol(symbol, date, from, to, interval, token);
  }

}
