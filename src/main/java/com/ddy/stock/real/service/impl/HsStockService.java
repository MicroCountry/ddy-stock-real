package com.ddy.stock.real.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ddy.stock.real.domain.BusError;
import com.ddy.stock.real.domain.Constants;
import com.ddy.stock.real.domain.Stock;
import com.ddy.stock.real.domain.TokenError;
import com.ddy.stock.real.service.StockService;
import com.ddy.stock.real.util.HSUtil;
import com.ddy.stock.real.util.HttpClientUtil;
import com.ddy.stock.real.util.JacksonUtils;

public class HsStockService implements StockService {
	
	public BigDecimal getStockNowPrice(String stockCode) {
		return getPrice(stockCode, Constants.NOW);
	}

	public BigDecimal getStockYesPrice(String stockCode) {
		return getPrice(stockCode, Constants.YEST);
	}

	public BigDecimal getStockOpenPrice(String stockCode) {
		return getPrice(stockCode, Constants.OPEN);
	}

	public Map<String, BigDecimal> getMapPrice(List<String> stockCodeList,
			String field) {
		Map<String, BigDecimal> map = new HashMap<String, BigDecimal>();
		List<Stock> list = this.getStockInfoList(stockCodeList);
		if(list==null){
			return null;
		}
		for(Stock stock : list){
			if(field.equals(Constants.NOW)){
				map.put(stock.getStockCode(), stock.getNowPrice());
			}else if(field.equals(Constants.YEST)){
				map.put(stock.getStockCode(), stock.getYesPrice());
			}else if(field.equals(Constants.OPEN)){
				map.put(stock.getStockCode(), stock.getOpenPrice());
			}
		}
		return map;
	}

	public Stock getStockInfo(String stockCode) {
		List<String> stockCodeList = new ArrayList<String>();
		stockCodeList.add(stockCode);
		List<Stock> list = this.getStockInfoList(stockCodeList);
		if(list==null){
			return null;
		}
		if(!list.isEmpty()){
			return list.get(0);
		}
		return null;
	}

	public List<Stock> getStockInfoList(List<String> stockCodeList) {
		String prodCode = null;
		Integer count = 0;
		Integer length = 0;
		List<Stock> list = new ArrayList<Stock>();
		Map<String, String> real_map = new HashMap<String, String>();
		real_map.put("fields","last_px,preclose_px,open_px,prod_name");
		for(String code:stockCodeList){
			if(code.startsWith("6")){
				code =code+".SS";
	    	}else{
	    		code =code+".SZ";
	    	}
			if(prodCode==null){
				prodCode = code;
				count++;
				length++;
			}else{
				prodCode = prodCode+","+code;
				count++;
				length++;
			}
			if(count>=98||stockCodeList.size()==length){//恒生接口每次最多请求100只股票，这里进行分批处理
				real_map.put("en_prod_code", prodCode);//股票代码
				List<Stock> one = getOneHundred(real_map);
				if(one==null){
					return null;
				}
				list.addAll(one);
				count=0;
				prodCode=null;
			}
		 }
		return list;
	}
	
	public List<Stock> getOneHundred(Map<String, String> real_map){
		String token = HSUtil.getRedisToken();
		if(token==null){
			token = HSUtil.getAccessToken();
			HSUtil.setRedisToken(token);
		}
		String result = HttpClientUtil.sendGet(HttpClientUtil.URL + "/quote/v1/real", real_map, HttpClientUtil.CHARSET, null,
				HttpClientUtil.BEARER + token, "行情报价");
		if(result==null){
			return null;
		}
		TokenError error = JacksonUtils.json2Object(result, TokenError.class);
		if(error!=null){
			return null;
		}
		BusError buserror = JacksonUtils.json2Object(result, BusError.class);
		if(buserror!=null){
			return null;
		}
		String[] arryStock = real_map.get("en_prod_code").split(",");
		List<Stock> list = HSUtil.paseResult(arryStock, result);
		return list;
	}
	
	public  BigDecimal getPrice(String stockCode,String field){
		List<String> stockCodeList = new ArrayList<String>();
		stockCodeList.add(stockCode);
	    Map<String,BigDecimal> map = getMapPrice(stockCodeList, field);
	    if(map==null){
	    	return null;
	    }
	    
	    BigDecimal price = map.get(stockCode);
	    return price;
	 }

	@Override
	public List<Stock> AllMarketAStock() {
		String token = HSUtil.getRedisToken();
		if(token==null){
			token = HSUtil.getAccessToken();
			HSUtil.setRedisToken(token);
		}
		Map<String, String> real_map = new HashMap<String, String>();
		real_map.put("en_hq_type_code", "SS.ESA,SZ.ESA");
		real_map.put("fields", "last_px,preclose_px,open_px,prod_name");
		real_map.put("data_count", "10000");
		real_map.put("sort_field_name", "last_px");
		String result = null;
		for(int i=0;i<5;i++){
			result = HttpClientUtil.sendGet(HttpClientUtil.URL + "/quote/v1/sort", real_map, HttpClientUtil.CHARSET, null,
					HttpClientUtil.BEARER + token, "全部A股行情");
			if(result==null){
				continue;
			}
			TokenError error = JacksonUtils.json2Object(result, TokenError.class);
			BusError buserror = JacksonUtils.json2Object(result, BusError.class);
			if(error!=null||buserror!=null){
				result=null;
				continue;
			}else{
				break;
			}
		}
		if(result==null){
			return null;
		}
		List<Stock> list = HSUtil.paseAllResult(result);
		return list;
	}

}
