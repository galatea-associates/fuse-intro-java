package org.galatea.starter.service;

import java.util.Collections;
import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.galatea.starter.domain.IexHistoricalPrices;
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

  /** Get historical prices for the symbol
   * @param symbol a symbol to find historical prices for
   * @param range an optional range to check
   * @param date an optional date to check
   */
  public List<IexHistoricalPrices> getHistoricalPricesForSymbols(final String symbol, final String range, final String date) {

    if (symbol == null) { // No symbol
      return iexClient.getHistoricalPrices();
    } else if(range == null && date == null){ // Only symbol specified
      return iexClient.getHistoricalPricesForSymbol(symbol);
    } else if (range != null && date == null) { // Range specified but not date
      return iexClient.getHistoricalPricesRange(symbol, range);
    } else if (range == null && date != null) { // Date specified but not range
      return iexClient.getHistoricalPricesDate(symbol, date);
    }

    return iexClient.getHistoricalPricesForSymbols(symbol, range, date);
  }
}
