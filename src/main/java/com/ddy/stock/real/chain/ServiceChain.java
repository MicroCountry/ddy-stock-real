package com.ddy.stock.real.chain;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.ddy.stock.real.domain.Constants;
import com.ddy.stock.real.domain.Stock;
import com.ddy.stock.real.service.impl.HsStockService;
import com.ddy.stock.real.service.impl.QTStockService;
import com.ddy.stock.real.service.impl.SinaStockService;
import com.ddy.stock.real.service.impl.WYStockService;

public class ServiceChain {
	public BigDecimal getStockNowPrice(String stockCode) {
		String[] arr = Constants.oneSort.split(",");//配置调用顺序
		BigDecimal nowPrice =null;
		for(String sort :arr){
			if(sort.equals("1")){
				nowPrice=new HsStockService().getStockNowPrice(stockCode);
				if(nowPrice!=null){
					return nowPrice;
				}
			}
			
			if(sort.equals("2")){
				nowPrice=new QTStockService().getStockNowPrice(stockCode);
				if(nowPrice!=null){
					return nowPrice;
				}
			}
			
			if(sort.equals("3")){
				nowPrice=new WYStockService().getStockNowPrice(stockCode);
				if(nowPrice!=null){
					return nowPrice;
				}
			}
		}
		return null;
	}

	public BigDecimal getStockYesPrice(String stockCode) {
		String[] arr = Constants.oneSort.split(",");//配置调用顺序
		BigDecimal nowPrice =null;
		for(String sort :arr){
			if(sort.equals("1")){
				nowPrice=new HsStockService().getStockYesPrice(stockCode);
				if(nowPrice!=null){
					return nowPrice;
				}
			}
			
			if(sort.equals("2")){
				nowPrice=new QTStockService().getStockYesPrice(stockCode);
				if(nowPrice!=null){
					return nowPrice;
				}
			}
			
			if(sort.equals("3")){
				nowPrice=new WYStockService().getStockYesPrice(stockCode);
				if(nowPrice!=null){
					return nowPrice;
				}
			}
		}
		return null;
	}

	public BigDecimal getStockOpenPrice(String stockCode) {
		String[] arr = Constants.oneSort.split(",");//配置调用顺序
		BigDecimal nowPrice =null;
		for(String sort :arr){
			if(sort.equals("1")){
				nowPrice=new HsStockService().getStockOpenPrice(stockCode);
				if(nowPrice!=null){
					return nowPrice;
				}
			}
			
			if(sort.equals("2")){
				nowPrice=new QTStockService().getStockOpenPrice(stockCode);
				if(nowPrice!=null){
					return nowPrice;
				}
			}
			
			if(sort.equals("3")){
				nowPrice=new WYStockService().getStockOpenPrice(stockCode);
				if(nowPrice!=null){
					return nowPrice;
				}
			}
		}
		return null;
	}

	public Map<String, BigDecimal> getMapPrice(List<String> stockCodeList,
			String field) {
		String[] arr = Constants.oneSort.split(",");//配置调用顺序
		Map<String, BigDecimal> map =null;
		for(String sort :arr){
			if(sort.equals("1")){
				map=new HsStockService().getMapPrice(stockCodeList,field);
				if(map!=null&&!map.isEmpty()){
					return map;
				}
			}
			
			if(sort.equals("2")){
				map=new QTStockService().getMapPrice(stockCodeList,field);
				if(map!=null&&!map.isEmpty()){
					return map;
				}
			}
			
			if(sort.equals("3")){
				map=new WYStockService().getMapPrice(stockCodeList,field);
				if(map!=null&&!map.isEmpty()){
					return map;
				}
			}
		}
		return null;
	}

	public Stock getStockInfo(String stockCode) {
		String[] arr = Constants.oneSort.split(",");//配置调用顺序
		Stock stock =null;
		for(String sort :arr){
			if(sort.equals("1")){
				stock=new HsStockService().getStockInfo(stockCode);
				if(stock!=null){
					return stock;
				}
			}
			
			if(sort.equals("2")){
				stock=new QTStockService().getStockInfo(stockCode);
				if(stock!=null){
					return stock;
				}
			}
			
			if(sort.equals("3")){
				stock=new WYStockService().getStockInfo(stockCode);
				if(stock!=null){
					return stock;
				}
			}
		}
		return null;
	}

	public List<Stock> getStockInfoList(List<String> stockCodeList) {
		String[] arr = Constants.oneSort.split(",");//配置调用顺序
		List<Stock> list =null;
		for(String sort :arr){
			if(sort.equals("1")){
				list=new HsStockService().getStockInfoList(stockCodeList);
				if(list!=null&&!list.isEmpty()){
					return list;
				}
			}
			
			if(sort.equals("2")){
				list=new QTStockService().getStockInfoList(stockCodeList);
				if(list!=null&&!list.isEmpty()){
					return list;
				}
			}
			
			if(sort.equals("3")){
				list=new WYStockService().getStockInfoList(stockCodeList);
				if(list!=null&&!list.isEmpty()){
					return list;
				}
			}
		}
		return null;
	}

	public List<Stock> AllMarketAStock() {
		String[] arr = Constants.allSort.split(",");//配置调用顺序
		List<Stock> list =null;
		for(String sort :arr){
			if(sort.equals("1")){
				list=new HsStockService().AllMarketAStock();
				if(list!=null&&!list.isEmpty()){
					return list;
				}
			}
			
			if(sort.equals("2")){
				list=new QTStockService().AllMarketAStock();
				if(list!=null&&!list.isEmpty()){
					return list;
				}
			}
			
			if(sort.equals("3")){
				list=new WYStockService().AllMarketAStock();
				if(list!=null&&!list.isEmpty()){
					return list;
				}
			}
			
			if(sort.equals("4")){
				list=new SinaStockService().AllMarketAStock();
				if(list!=null&&!list.isEmpty()){
					return list;
				}
			}
		}
		return null;
	}
}
