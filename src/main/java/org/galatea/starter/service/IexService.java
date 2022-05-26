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
  public List<IexSymbol> getAllSymbols() {
    return iexClient.getAllSymbols();
  }

  /**
   * Get the last traded price for each Symbol that is passed in.
   *
   * @param symbols the list of symbols to get a last traded price for.
   * @return a list of last traded price objects for each Symbol that is passed in.
   */
  public List<IexLastTradedPrice> getLastTradedPriceForSymbols(final List<String> symbols) {
    if (CollectionUtils.isEmpty(symbols)) {
      return Collections.emptyList();
    } else {
      return iexClient.getLastTradedPriceForSymbols(symbols.toArray(new String[0]));
    }
  }

  /**
   * Get the historical price of a stock.
   *
   * @param symbol stock symbol
   * @return
   */
  public List<IexHistoricalPrice> getHistoricalPriceSymbol(final String symbol) {
    return iexClient.getHistoricalPriceSymbol(symbol);
  }

  /**
   * Get the historical price of a stock for a given range.
   *
   * @param symbol stock symbol
   * @param range time duration for the stock price
   * @return
   */
  public List<IexHistoricalPrice> getHistoricalPriceSymbolRange(final String symbol, final String range) {
    return iexClient.getHistoricalPriceSymbolRange(symbol, range);
  }

  /**
   * Get the historical price of a stock for a given date.
   *
   * @param symbol stock symbol
   * @param date to find stock price variations of a given date
   * @return
   */
  public List<IexHistoricalPrice> getHistoricalPriceSymbolDate(final String symbol, final String date) {
    return iexClient.getHistoricalPriceSymbolDate(symbol, date);
  }

}
