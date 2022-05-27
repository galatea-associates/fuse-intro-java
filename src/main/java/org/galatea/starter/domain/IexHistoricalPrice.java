package org.galatea.starter.domain;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IexHistoricalPrice {
  private String symbol;
  private BigDecimal close;
  private BigDecimal high;
  private BigDecimal low;
  private BigDecimal open;
  private Integer volume;
  private String date;

  /**
   * Convert the date from epoch time-stamp to human readable.
   *
   * @return the same object with a readable date.
   */
  public IexHistoricalPrice fixDate() {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    Calendar c = Calendar.getInstance();
    // Due to a peculiarity of the Date library, the generated date will be one day earlier than
    // expected. To account for this, we increment the date by 1.
    Date toFix = new Date(Long.parseLong(this.date));
    c.setTime(toFix);
    c.add(Calendar.DATE, 1);
    this.date = sdf.format(c.getTime());
    return this;
  }
}

