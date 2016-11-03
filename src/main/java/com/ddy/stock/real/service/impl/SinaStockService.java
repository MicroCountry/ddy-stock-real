package com.ddy.stock.real.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import com.ddy.stock.real.domain.Stock;
import com.ddy.stock.real.service.StockService;
import com.ddy.stock.real.util.HttpUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
public class SinaStockService implements StockService {

	@Override
	public BigDecimal getStockNowPrice(String stockCode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BigDecimal getStockYesPrice(String stockCode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BigDecimal getStockOpenPrice(String stockCode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, BigDecimal> getMapPrice(List<String> stockCodeList,
			String field) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Stock getStockInfo(String stockCode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Stock> getStockInfoList(List<String> stockCodeList) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Stock> AllMarketAStock() {
		int pageIndex = 1;
		List<Stock> allList = new ArrayList<Stock>();
        while(true){
            String result = load(pageIndex);
            List<Stock> list = parseResult(result);
            if(list==null||list.isEmpty()) break;
            allList.addAll(list);
            pageIndex++;
        }
		return allList;
	}

	 private String load(int pageIndex) {
	        String result = HttpUtil.doGet("http://money.finance.sina.com.cn/d/api/openapi_proxy.php/?__s=[[%22hq%22,%22hs_a%22,%22%22,0," + pageIndex + ",80]]");
	        return result;
	    }
	    /**
	     * ["symbol","code","name","trade","pricechange","changepercent","buy","sell","settlement","open","high","low","volume","amount","ticktime","per","per_d","nta","pb","mktcap","nmc","turnoverratio","favor","guba"]
	     * @Title: parseResult
	     * @Description: 
	     * @return
	     */
	    private List<Stock> parseResult(String result){
	        JsonParser parser = new JsonParser();
	        JsonElement jsonElement = parser.parse(result);
	        JsonArray jsonArray = jsonElement.getAsJsonArray();
	        List<Stock> list = new ArrayList<Stock>();
	        if(jsonArray!=null && jsonArray.size()>0){
	            JsonElement ele = jsonArray.get(0);
	            JsonObject jobj = ele.getAsJsonObject();
	            if(jobj.get("code").getAsInt() == 0){
	                String day = jobj.get("day").getAsString();
	                JsonArray items = jobj.get("items").getAsJsonArray();
	                if(items.size()==0){
	                    return null;
	                }
	                for(int i=0;i<items.size();i++){
	                   JsonElement jele =  items.get(i);
	                   Stock stock = new Stock();
	                   stock.setStockCode(jele.getAsJsonArray().get(1).getAsString());
	                   stock.setStockName(jele.getAsJsonArray().get(2).getAsString());
	                   stock.setStockFullCode(jele.getAsJsonArray().get(0).getAsString());
	                   stock.setNowPrice(new BigDecimal(jele.getAsJsonArray().get(3).getAsString()));
	                   stock.setOpenPrice(new BigDecimal(jele.getAsJsonArray().get(9).getAsString()));
	                   System.currentTimeMillis();
	                   Calendar cal = Calendar.getInstance();
	                   int hourofDay = cal.get(Calendar.HOUR_OF_DAY);
	                   if((hourofDay>=0 && hourofDay<9) || hourofDay>=15){
	                	   stock.setYesPrice(new BigDecimal(jele.getAsJsonArray().get(3).getAsString()));
	                   }else{
	                	   stock.setYesPrice(new BigDecimal(jele.getAsJsonArray().get(8).getAsString()));
	                   }
	                   list.add(stock);
	                }
	            }
	        }
	        return list;
	    }
}
