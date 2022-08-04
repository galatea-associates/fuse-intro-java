package org.galatea.starter.entrypoint;

import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.aspect4log.Log;
import net.sf.aspect4log.Log.Level;
import org.galatea.starter.domain.IexHistoricalPrices;
import org.galatea.starter.domain.IexLastTradedPrice;
import org.galatea.starter.domain.IexSymbol;
import org.galatea.starter.service.IexService;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
   * @return a list of all IexStockSymbols.
   */
  @GetMapping(value = "${mvc.iex.getAllSymbolsPath}", produces = {MediaType.APPLICATION_JSON_VALUE})
  public List<IexSymbol> getAllStockSymbols(@RequestParam(value = "token", required = false,
      defaultValue = "${spring.datasource.token}")
          final String token) {
    return iexService.getAllSymbols(token);
  }

  /**
   * Get the last traded price for each of the symbols passed in.
   *
   * @param symbols list of symbols to get last traded price for.
   * @return a List of IexLastTradedPrice objects for the given symbols.
   */
  @GetMapping(value = "${mvc.iex.getLastTradedPricePath}", produces = {
      MediaType.APPLICATION_JSON_VALUE})
  public List<IexLastTradedPrice> getLastTradedPrice(
      @RequestParam(value = "symbols") final List<String> symbols,
      @RequestParam(value = "token",
          required = false,
          defaultValue = "${spring.datasource.token}")
      final String token) {
    return iexService.getLastTradedPriceForSymbols(symbols, token);
  }

  /**
   * Get the Historical Prices.
   *
   * @param token API access token.
   * @return a List of IexHistoricalPrice objects.
   */
  @GetMapping(value = "${mvc.iex.getHistoricalPricesPath}/{symbol}/{range}", produces = {
      MediaType.APPLICATION_JSON_VALUE})
  public List<IexHistoricalPrices> getHistoricalPrices(
      @PathVariable final String symbol,
      @PathVariable final String range,
      @RequestParam(value = "token",
          required = false,
          defaultValue = "${spring.datasource.token}")
      final String token) {
    return iexService.getHistoricalPricesForSymbols(symbol, range, token);
  }


}