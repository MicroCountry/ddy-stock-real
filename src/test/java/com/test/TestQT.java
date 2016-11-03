package com.test;

import com.ddy.stock.real.service.impl.QTStockService;

public class TestQT {
	public static void main(String[] args) {
		QTStockService service = new QTStockService();
		System.out.println(service.AllMarketAStock().size());
	}
}
