package com.ddy.stock.real.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import com.ddy.stock.real.domain.Constants;
import com.ddy.stock.real.domain.Stock;
import com.ddy.stock.real.domain.StockCache;
import com.ddy.stock.real.service.StockService;
import com.ddy.stock.real.util.HSUtil;
import com.ddy.stock.real.util.HttpUtil;
import com.ddy.stock.real.util.NumberUtil;
import com.ddy.stock.real.util.StockUtil;

public class QTStockService implements StockService {
	public static final Integer NOW_PRICE =3;
	public static final Integer YES_PRICE =4;
	public static final Integer OPEN_PRICE =5;
	public static final String FETCH_QT_STOCK_URL = "http://qt.gtimg.cn/q=";
	public static final String QT_PUSH_URL="http://push3.gtimg.cn/q=";
	
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
			code = StockUtil.getStockFullCode(code);
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
	
	public List<Stock> getOneHundred(String prodCode){
		String content = HttpUtil.doGet(FETCH_QT_STOCK_URL+prodCode);
		return parseContent(content);
	}
	
	public List<Stock> parseContent(String content){
		if(content.equals("pv_none_match=1;")){
			return null;
		}
		String[] stockArr = content.split(";");
		List<Stock> list = new ArrayList<Stock>();
		for(String stockStr : stockArr){
			 String [] array= stockStr.split("~");
			 if(null!=array&&array.length>0){
				 String stockName = array[1];
				 String stockCode = array[2];
				 BigDecimal nowPrice = NumberUtil.decimalHalf2Up(new BigDecimal(array[3]));
				 BigDecimal yesPrice = NumberUtil.decimalHalf2Up(new BigDecimal(array[4]));
				 BigDecimal openPrice = NumberUtil.decimalHalf2Up(new BigDecimal(array[5]));
				 
				 Stock stock = new Stock();
				 stock.setStockCode(stockCode);
				 stock.setStockName(stockName);
				 stock.setStockFullCode(StockUtil.getStockFullCode(stockCode));
				 stock.setNowPrice(nowPrice);
				 stock.setYesPrice(yesPrice);
				 stock.setOpenPrice(openPrice);
				 list.add(stock);
				 //更新至缓存
				 StockCache stockCache = new StockCache();
				 stockCache = new StockCache();
				 stockCache.setStockCode(stockCode);
				 stockCache.setStockFullCode(StockUtil.getStockFullCode(stockCode));
				 stockCache.setTodayStartPrice(openPrice);
				 stockCache.setNowPrice(nowPrice);
				 stockCache.setYesClosePrice(yesPrice);
				 stockCache.setUpdatetime(System.currentTimeMillis());
				 HSUtil.setRedisStockCache(stockCache);
			 }
		}
		return list;
	}
	
	public List<Stock> parseContentNoCache(String content){
		if(content.equals("pv_none_match=1;")){
			return null;
		}
		String[] stockArr = content.split(";");
		List<Stock> list = new ArrayList<Stock>();
		for(String stockStr : stockArr){
			 String [] array= stockStr.split("~");
			 if(null!=array&&array.length>0){
				 String stockName = array[1];
				 String stockCode = array[2];
				 BigDecimal nowPrice = NumberUtil.decimalHalf2Up(new BigDecimal(array[3]));
				 BigDecimal yesPrice = NumberUtil.decimalHalf2Up(new BigDecimal(array[4]));
				 BigDecimal openPrice = NumberUtil.decimalHalf2Up(new BigDecimal(array[5]));
				 
				 Stock stock = new Stock();
				 stock.setStockCode(stockCode);
				 stock.setStockName(stockName);
				 stock.setStockFullCode(StockUtil.getStockFullCode(stockCode));
				 stock.setNowPrice(nowPrice);
				 stock.setYesPrice(yesPrice);
				 stock.setOpenPrice(openPrice);
				 list.add(stock);
			 }
		}
		return list;
	}

	@Override
	public List<Stock> AllMarketAStock() {
		String shUrl="http://stock.gtimg.cn/data/index.php?appn=rank&t=rankash/chr&p=1&o=0&l=1500&v=list_data";
		String szUrl="http://stock.gtimg.cn/data/index.php?appn=rank&t=rankasz/chr&p=1&o=0&l=2000&v=list_data";
		String shStr = HttpUtil.doGet(shUrl).replace("var list_data=", "").replace(";", "");
		String szStr = HttpUtil.doGet(szUrl).replace("var list_data=", "").replace(";", "");
		JSONObject shJson  = JSONObject.fromObject(shStr);
		JSONObject szJson  = JSONObject.fromObject(szStr);
		String  shCodeStr =shJson.getString("data");
		String  szCodeStr =szJson.getString("data");
		String codeStr = shCodeStr+","+szCodeStr;
		String[] arr = codeStr.split(",");
		String content =null;
		Integer count = 0;
		Integer length = 0;
		StringBuilder req = new StringBuilder();
		for(int i=0;i< arr.length;i++){
			if(i==0){
				req.append(arr[i]);
			}else{
				req.append(",").append(arr[i]);
			}
			count++;
			length++;
			if(length==arr.length||count==60){
				String con = HttpUtil.doGet(QT_PUSH_URL+req+"&m=push");
				content=content+con;
				count=0;
				req =new StringBuilder();
			}
		}
		return parseContentNoCache(content);
	}

	public BigDecimal getPrice(String stockCode,String field){
		List<String> stockCodeList = new ArrayList<String>();
		stockCodeList.add(stockCode);
	    Map<String,BigDecimal> map = getMapPrice(stockCodeList, field);
	    if(map==null){
	    	return null;
	    }
	    
	    BigDecimal price = map.get(stockCode);
	    return price;
	}
}
