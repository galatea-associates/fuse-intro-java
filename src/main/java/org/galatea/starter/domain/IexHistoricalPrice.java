package org.galatea.starter.domain;

import lombok.Builder;
import lombok.Data;

import java.math.BigInteger;
import java.util.Date;

@Data
@Builder
public class IexHistoricalPrice {

    private float close;
    private float high;
    private float low;
    private float open;
    private String symbol;
    private BigInteger volume;
    private Date date;

}
