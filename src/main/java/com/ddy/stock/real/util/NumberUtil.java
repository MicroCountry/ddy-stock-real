package com.ddy.stock.real.util;

import java.math.BigDecimal;

public class NumberUtil {
	 public static Double doubleHalfUp(Double number)
	  {
	    if (number == null)
	      return number;
	    BigDecimal bd = new BigDecimal(number.doubleValue());
	    number = Double.valueOf(bd.setScale(2, 4).doubleValue());
	    return number;
	  }
	  public static Double doubleHalf4Up(Double number) {
	    if (number == null)
	      return number;
	    BigDecimal bd = new BigDecimal(number.doubleValue());
	    number = Double.valueOf(bd.setScale(4, 4).doubleValue());
	    return number;
	  }
	  
	  public static BigDecimal decimalHalf2Up(BigDecimal b) {
	    return b.setScale(2, 4);
	  }
}
