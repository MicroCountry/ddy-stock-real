package com.ddy.stock.real.provider;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.ddy.stock.real.domain.Stock;

public interface StockProvider {
	BigDecimal getStockNowPrice(String stockCode);//某只股票现价
	BigDecimal getStockYesPrice(String stockCode);//某只股票昨日收盘价
	BigDecimal getStockOpenPrice(String stockCode);//某只股票开盘价
	/**
	 * @param stockCodeList 股票队列
	 * @param field			价格类型: 现价:"now"，收盘价:"yest"，开盘价:"open"
	 * @return
	 */
	Map<String,BigDecimal> getMapPrice(List<String> stockCodeList,String field);//股票队列对应标志位的map
	Stock getStockInfo(String stockCode);//股票完整信息
	List<Stock> getStockInfoList(List<String> stockCodeList);//股票信息列表
	List<Stock> AllMarketAStock();//所有A股股票
}
