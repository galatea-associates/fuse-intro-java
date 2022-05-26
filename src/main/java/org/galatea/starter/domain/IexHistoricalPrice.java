package org.galatea.starter.domain;

import java.math.BigInteger;
import java.util.Date;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IexHistoricalPrice {
  private float close;
  private float high;
  private float low;
  private float open;
  private String symbol;
  private BigInteger volume;
  private String date;
}
