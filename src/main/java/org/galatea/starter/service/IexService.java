package org.galatea.starter.service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.galatea.starter.domain.IexHistoricalPrice;
import org.galatea.starter.domain.IexLastTradedPrice;
import org.galatea.starter.domain.IexSymbol;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * A layer for transformation, aggregation, and business required when retrieving data from IEX.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class IexService {

  @NonNull
  private IexClient iexClient;


  /**
   * Get all stock symbols from IEX.
   *
   * @param token for authorization.
   * @return a list of all Stock Symbols from IEX.
   */
  public List<IexSymbol> getAllSymbols(final String token) {
    return iexClient.getAllSymbols(token);
  }

  /**
   * Get the last traded price for each Symbol that is passed in.
   *
   * @param symbols the list of symbols to get a last traded price for.
   * @param token for authorization.
   * @return a list of last traded price objects for each Symbol that is passed in.
   */
  public List<IexLastTradedPrice> getLastTradedPriceForSymbols(final List<String> symbols,
      final String token) {
    if (CollectionUtils.isEmpty(symbols)) {
      return Collections.emptyList();
    } else {
      return iexClient.getLastTradedPriceForSymbols(symbols.toArray(new String[0]), token);
    }
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
  public List<IexHistoricalPrice> getHistoricalPricesForSymbol(final String symbol,
                                                             final String date,
                                                             final String from,
                                                             final String to,
                                                             final String interval,
                                                             final String token) {
    if (symbol.isEmpty()) {
      return Collections.emptyList();
    } else {
      return iexClient.getHistoricalPricesForSymbol(symbol, date, from, to, interval, token)
          .stream().map(e -> e.fixDate()).collect(Collectors.toList());
    }
  }

}
