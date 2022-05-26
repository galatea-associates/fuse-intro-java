package org.galatea.starter.service;

import java.util.Collections;
import java.util.List;
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
   * @return a list of all Stock Symbols from IEX.
   */
  public List<IexSymbol> getAllSymbols(final String token) {
    return iexClient.getAllSymbols(token);
  }

  /**
   * Get the last traded price for each Symbol that is passed in.
   *
   * @param symbols the list of symbols to get a last traded price for.
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
   * Get the historical price for each symbol at each date.
   *
   * @param symbol the symbol for which to get the prices.
   * @param date the date on which to get the prices.
   * @return a List of IexLastTradedPrice objects for the given symbols.
   */
  public List<IexHistoricalPrice> getHistoricalPricesForSymbols(final String symbol,
                                                             final String date,
                                                             final String from,
                                                             final String to,
                                                             final String interval,
                                                             final String token) {
    if (symbol.isEmpty()) {
      return Collections.emptyList();
    } else {
      return iexClient.getHistoricalPricesForSymbols(symbol, date, from, to, interval, token);
    }
  }

}
