package com.ddy.stock.real.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.ddy.stock.real.domain.Constants;
import com.ddy.stock.real.domain.Stock;
import com.ddy.stock.real.domain.StockCache;
import com.ddy.stock.real.service.StockService;
import com.ddy.stock.real.util.HSUtil;
import com.ddy.stock.real.util.HttpUtil;
import com.ddy.stock.real.util.StockUtil;

public class WYStockService implements StockService {
	
	public static final String NAME="name";
	public static final String SYMBOA="symbol";
	public static final String PRICE="price";
	public static final String OPEN="open";
	public static final String YESTCLOSE="yestclose";
	public final static String FETCH_WY_STOCK_URL = "http://api.money.126.net/data/feed/";
	public static final String ALL_STOCK_UTL="http://quotes.money.163.com/hs/service/diyrank.php?host=http://quotes.money.163.com/hs/service/diyrank.php&page=0&query=STYPE:EQA&fields=NO,SYMBOL,NAME,PRICE,OPEN,YESTCLOSE&sort=SYMBOL&count=100&type=query";
	
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
		for(String code:stockCodeList){
			if(prodCode==null){
				prodCode = getWYFullCode(code);
				count++;
				length++;
			}else{
				prodCode = prodCode+","+getWYFullCode(code);
				count++;
				length++;
			}
			if(count>=98||stockCodeList.size()==length){//恒生接口每次最多请求100只股票，这里进行分批处理
				List<Stock> one = getOneHundred(prodCode);
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
	
	private List<Stock> getOneHundred(String prodCode){
		String content = HttpUtil.doGet(FETCH_WY_STOCK_URL+prodCode+",money.api");
		List<Stock> list = parseContent(prodCode,content.replace("_ntes_quote_callback(", "").replace(");",""));
		if(list==null){
			return null;
		}
		updateCache(list);
		return list;
	}
	
	private List<Stock> parseContent(String prodCode ,String content){
		String[] arr = prodCode.split(",");
		JSONObject jsonObj = JSONObject.fromObject(content);
		List<Stock> list = new ArrayList<Stock>();
		for(String code :arr){
			Stock stock = new Stock();
			JSONObject obj = jsonObj.getJSONObject(code);
			String stockCode = obj.getString("symbol");
			String stockName = StockUtil.decodeUnicode(obj.getString("name"));
			BigDecimal nowPrice = new BigDecimal(obj.getString("price"));
			BigDecimal yesPrice = new BigDecimal(obj.getString("yestclose"));
			BigDecimal openPrice = new BigDecimal(obj.getString("open"));
			stock.setStockCode(stockCode);
			stock.setStockName(stockName);
			stock.setStockFullCode(StockUtil.getStockFullCode(stockCode));
			stock.setNowPrice(nowPrice);
			stock.setOpenPrice(openPrice);
			stock.setYesPrice(yesPrice);
			list.add(stock);
		}
		return list;
	}
	
	private List<Stock> parseAllCode(String content){
		JSONObject jsonObj = JSONObject.fromObject(content);
		JSONArray array = jsonObj.getJSONArray("list");
		List<Stock> list = new ArrayList<Stock>();
		for(int i=0;i<array.size();i++){
			Stock stock = new Stock();
			JSONObject arr = array.getJSONObject(i);
			stock.setStockName(StockUtil.decodeUnicode(arr.getString("NAME")));
			stock.setOpenPrice(new BigDecimal(arr.getString("OPEN")));
			stock.setNowPrice(new BigDecimal(arr.getString("PRICE")));
			stock.setStockCode(arr.getString("SYMBOL"));
			stock.setYesPrice(new BigDecimal(arr.getString("YESTCLOSE")));
			stock.setStockFullCode(StockUtil.getStockFullCode(arr.getString("SYMBOL")));
			list.add(stock);
		}
		return list;
	}
	
	private void updateCache(List<Stock> list){
		for(Stock stock:list){
			 //更新至缓存
			 StockCache stockCache = new StockCache();
			 stockCache = new StockCache();
			 stockCache.setStockCode(stock.getStockCode());
			 stockCache.setStockFullCode(StockUtil.getStockFullCode(stock.getStockCode()));
			 stockCache.setTodayStartPrice(stock.getOpenPrice());
			 stockCache.setNowPrice(stock.getNowPrice());
			 stockCache.setYesClosePrice(stock.getYesPrice());
			 stockCache.setUpdatetime(System.currentTimeMillis());
			 HSUtil.setRedisStockCache(stockCache);
		}
	}

	@Override
	public List<Stock> AllMarketAStock() {
		String content = HttpUtil.doGet(ALL_STOCK_UTL);
		JSONObject obj = JSONObject.fromObject(content);
		Integer pageCount = Integer.valueOf(obj.getString("pagecount"));
		List<Stock> list = new ArrayList<Stock>();
		for(int i=0;i<pageCount;i++){
			list.addAll(parseAllCode(HttpUtil.doGet(getUrl(i))));
		}
		return list;
	}

	private BigDecimal getPrice(String stockCode, String field) {
		List<String> stockCodeList = new ArrayList<String>();
		stockCodeList.add(stockCode);
	    Map<String,BigDecimal> map = getMapPrice(stockCodeList, field);
	    if(map==null){
	    	return null;
	    }
	    
	    BigDecimal price = map.get(stockCode);
	    return price;
	}
	
	private String getWYFullCode(String stockCode){
		if(stockCode.startsWith("6")){
			return "0"+stockCode;
		}else{
			return "1"+stockCode;
		}
	}
	
	private String getUrl(int i){
		return "http://quotes.money.163.com/hs/service/diyrank.php?host=http://quotes.money.163.com/hs/service/diyrank.php&page="+i+"&query=STYPE:EQA&fields=NO,SYMBOL,NAME,PRICE,OPEN,YESTCLOSE&sort=SYMBOL&count=100&type=query";
	}
}
