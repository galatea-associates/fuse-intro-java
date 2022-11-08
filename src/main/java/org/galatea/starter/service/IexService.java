package org.galatea.starter.service;

import java.util.Collections;
import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.galatea.starter.domain.IexHistoricalPrices;
import org.galatea.starter.domain.IexLastTradedPrice;
import org.galatea.starter.domain.IexSymbol;
import org.galatea.starter.entrypoint.exception.EntityNotFoundException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
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
  public List<IexHistoricalPrices> getHistoricalPricesForSymbols(final String symbol, final String range, final String date) throws Exception{

    // Input validation
    if(StringUtils.isBlank(symbol)){ // Symbol is required
      throw new EntityNotFoundException(symbol.getClass(), symbol);
    } else if(StringUtils.isBlank(range) && StringUtils.isBlank(date)){ // No range or date specified
      return iexClient.getHistoricalPricesForSymbol(symbol);
    } else if ((StringUtils.isBlank(range) == false) && StringUtils.isBlank(date)) { // Range specified but not date
      if(range == ""){
        throw new HttpMessageNotReadableException("\"range\" is not allowed to be empty");
      }
      return iexClient.getHistoricalPricesRange(symbol, range);
    } else if ((StringUtils.isBlank(range)) && StringUtils.isBlank(date) == false) { // Date specified but not range
      if(date == ""){
        throw new HttpMessageNotReadableException("\"date\" is not allowed to be empty");
      }
      return iexClient.getHistoricalPricesDate(symbol, date);
    }

    // All parameters were valid
    return iexClient.getHistoricalPricesForSymbols(symbol, range, date);
  }
}
