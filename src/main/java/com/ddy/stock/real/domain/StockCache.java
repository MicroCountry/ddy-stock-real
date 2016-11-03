package com.ddy.stock.real.domain;

import java.math.BigDecimal;

public class StockCache {
	private String stockCode;
	private String stockName;
	private String stockFullCode;
	private BigDecimal nowPrice;
	private BigDecimal todayStartPrice;
	private Long updatetime;
	private BigDecimal yesClosePrice;

    public String getStockName() {
		return stockName;
	}

	public void setStockName(String stockName) {
		this.stockName = stockName;
	}

	public BigDecimal getTodayStartPrice() {
        return todayStartPrice;
    }
    
    public BigDecimal getYesClosePrice() {
		return yesClosePrice;
	}


	public void setYesClosePrice(BigDecimal yesClosePrice) {
		this.yesClosePrice = yesClosePrice;
	}


	public Long getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(Long updatetime) {
		this.updatetime = updatetime;
	}

	public void setTodayStartPrice(BigDecimal todayStartPrice) {
        this.todayStartPrice = todayStartPrice;
    }
    public String getStockCode() {
		return stockCode;
	}
	public void setStockCode(String stockCode) {
		this.stockCode = stockCode;
	}
	public String getStockFullCode() {
		return stockFullCode;
	}
	public void setStockFullCode(String stockFullCode) {
		this.stockFullCode = stockFullCode;
	}
	public BigDecimal getNowPrice() {
		return nowPrice;
	}
	public void setNowPrice(BigDecimal nowPrice) {
		this.nowPrice = nowPrice;
	}
}
