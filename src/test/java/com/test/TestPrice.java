package com.test;

import java.util.ArrayList;
import java.util.List;

import com.ddy.stock.real.domain.TokenError;
import com.ddy.stock.real.provider.impl.StockProviderImpl;
import com.ddy.stock.real.util.JacksonUtils;

public class TestPrice {
	public static void main(String[] args) {
		StockProviderImpl impl = new StockProviderImpl();
		/*System.out.println(impl.getStockNowPrice("600000"));
		List<String> stockCodeList = new ArrayList<String>();
		stockCodeList.add("600000");
		stockCodeList.add("300170");
		stockCodeList.add("002212");
		System.out.println(impl.getMapPrice(stockCodeList , "now"));
		System.out.println(impl.getStockInfo("600000").getStockCode());
		System.out.println(impl.getStockInfoList(stockCodeList).size());*/
		System.out.println(impl.AllMarketAStock().size());
	}
}
