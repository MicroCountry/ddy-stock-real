package com.ddy.stock.real.domain;

import java.math.BigDecimal;

public class Stock {
	private String stockCode;//股票代码
	private String stockFullCode;//股票全码
	private String stockName;//股票名称
	private BigDecimal yesPrice;//股票昨日收盘价
	private BigDecimal openPrice;//开盘价
	private BigDecimal nowPrice;//现价
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
	public String getStockName() {
		return stockName;
	}
	public void setStockName(String stockName) {
		this.stockName = stockName;
	}
	public BigDecimal getYesPrice() {
		return yesPrice;
	}
	public void setYesPrice(BigDecimal yesPrice) {
		this.yesPrice = yesPrice;
	}
	public BigDecimal getOpenPrice() {
		return openPrice;
	}
	public void setOpenPrice(BigDecimal openPrice) {
		this.openPrice = openPrice;
	}
	public BigDecimal getNowPrice() {
		return nowPrice;
	}
	public void setNowPrice(BigDecimal nowPrice) {
		this.nowPrice = nowPrice;
	}
	
}
