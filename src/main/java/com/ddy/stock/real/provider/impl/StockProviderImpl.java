package com.ddy.stock.real.provider.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.ddy.stock.real.chain.ServiceChain;
import com.ddy.stock.real.domain.Stock;
import com.ddy.stock.real.provider.StockProvider;

public class StockProviderImpl implements StockProvider{

	@Override
	public BigDecimal getStockNowPrice(String stockCode) {
		return new ServiceChain().getStockNowPrice(stockCode);
	}

	@Override
	public BigDecimal getStockYesPrice(String stockCode) {
		return new ServiceChain().getStockYesPrice(stockCode);
	}

	@Override
	public BigDecimal getStockOpenPrice(String stockCode) {
		return new ServiceChain().getStockOpenPrice(stockCode);
	}

	@Override
	public Map<String, BigDecimal> getMapPrice(List<String> stockCodeList,
			String field) {
		return new ServiceChain().getMapPrice(stockCodeList, field);
	}

	@Override
	public Stock getStockInfo(String stockCode) {
		return new ServiceChain().getStockInfo(stockCode);
	}

	@Override
	public List<Stock> getStockInfoList(List<String> stockCodeList) {
		return new ServiceChain().getStockInfoList(stockCodeList);
	}

	@Override
	public List<Stock> AllMarketAStock() {
		return new ServiceChain().AllMarketAStock();
	}

}
