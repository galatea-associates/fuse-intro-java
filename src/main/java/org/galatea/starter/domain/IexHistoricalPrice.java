package org.galatea.starter.domain;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.bind.annotation.GetMapping;

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
}

