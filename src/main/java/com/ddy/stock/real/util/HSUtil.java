package com.ddy.stock.real.util;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import redis.clients.jedis.Jedis;

import com.ddy.stock.real.domain.AccessToken;
import com.ddy.stock.real.domain.Stock;
import com.ddy.stock.real.domain.StockCache;
import com.ddy.stock.real.domain.TokenError;

public class HSUtil {
	private static String BASIC     = "Basic ";
	public static final String LAST_PRICE = "last_price";//现价
	public static final String PRECLOSE_PX = "preclose_px";//昨日收盘价
	public static final String OPEN_PX = "open_px";//开盘价
	public static final String ACCESS_TOKEN= "ACCESS_TOKEN";
	public static final String HSET_STOCK_CACHE = "HSET_STOCK_CACHE";
	public static String getAuth(){
		String clientId=null;
		 String clientSecret =null;
		 clientId="36925ca9-6768-47f7-8124-fa2a9a8a2c7d";
	     clientSecret="d392dff7-14f5-4437-a0b2-5386fa72162f";
		String auth = null;
		try {
			auth = HttpClientUtil.Base64(clientId, clientSecret, BASIC);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	   	return auth;
	}
	
	public static String getAccessToken(){
		// 客户端凭证模式 获取令牌
		Map<String, String> token_map = new HashMap<String, String>();
		token_map.put("grant_type", "client_credentials");// 客户端凭证模式时，必须为“client_credentials”；
		String tokenResult = HttpClientUtil.sendPost(HttpClientUtil.URL + "/oauth2/oauth2/token", token_map,
				HttpClientUtil.CHARSET, HttpClientUtil.CHARSET, null, HSUtil.getAuth(),
				"获取公共令牌");
		//首先检查错误状态
		TokenError error = JacksonUtils.json2Object(tokenResult, TokenError.class);
		String token = null;
		if(error==null){
			// 解析返回数据json
			AccessToken accesstoken = JacksonUtils.json2Object(tokenResult, AccessToken.class);
			token = accesstoken.getAccess_token();
		}
		return token;
	}
	
	public static String getRedisToken(){
		Jedis jedis = JedisUtil.getJedis();
		if(jedis!=null){
			try {
				return jedis.hget(ACCESS_TOKEN, "ACCESS_TOKEN");
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				JedisUtil.returnResource(jedis);
			}
		}else{
			return null;
		}
		return null;
	}
	
	public static void setRedisToken(String token){
		Jedis jedis = JedisUtil.getJedis();
		if(jedis!=null){
			try {
				jedis.hset(ACCESS_TOKEN, "ACCESS_TOKEN", token);
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				JedisUtil.returnResource(jedis);
			}
		}
	}
	
	public static void setRedisStockCache(StockCache cache){
		Jedis jedis = JedisUtil.getJedis();
		if(jedis!=null){
			try {
				jedis.hset(HSET_STOCK_CACHE, cache.getStockFullCode(), JSONObject.fromObject(cache).toString());
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				JedisUtil.returnResource(jedis);
			}
		}
	}
	
	public static List<Stock> paseResult(String[] arryStock,String result){
		 try {
				Integer last_px =0;
	     		Integer preclose_px=0;
	     		Integer open_px=0;
	     		Integer prod_name=0;
	     		JSONObject jsonObj = JSONObject.fromObject(result).getJSONObject("data").getJSONObject("snapshot"); 
	     		JSONArray jsonField = jsonObj.getJSONArray("fields");
	     		for(int i=0;i<jsonField.size();i++){//获取字段的index值
	     			if(jsonField.getString(i).trim().equals("last_px")){
	     				last_px=i;
	     			}else if(jsonField.getString(i).trim().equals("preclose_px")){
	     				preclose_px=i;
	     			}else if(jsonField.getString(i).trim().equals("open_px")){
	     				open_px=i;
	     			}else if(jsonField.getString(i).trim().equals("prod_name")){
	     				prod_name=i;
	     			}
	     		}
	     		JSONObject realJson  = JSONObject.fromObject(result);
	     		List<Stock> stockList = new ArrayList<Stock>();
	     		for(String data:arryStock){
	     			JSONArray array = (JSONArray) realJson.getJSONObject("data").getJSONObject("snapshot").get(StockUtil.getHSStockFullCode(data));
						if(array==null){
							continue;
						}
						Long dataTimestamp = Long.parseLong(array.getString(0));
						BigDecimal lastPx=new BigDecimal(array.getString(last_px));//最新价格
						BigDecimal preclosePx = new BigDecimal(array.getString(preclose_px));//昨日收盘价
						BigDecimal openPx=new BigDecimal(array.getString(open_px));//今日开盘价
						String prodName= array.getString(prod_name);
						Stock stock = new Stock();
						stock.setStockCode(data);
						stock.setStockFullCode(StockUtil.getStockFullCode(data));
						stock.setStockName(prodName);
						stock.setNowPrice(lastPx);
						stock.setOpenPrice(openPx);
						stock.setYesPrice(preclosePx);
						stockList.add(stock);
						//更新到cache中
						if(lastPx!=null||preclosePx!=null||openPx!=null){
							StockCache stockCache = new StockCache();
							stockCache = new StockCache();
				            stockCache.setStockCode(data);
				            stockCache.setStockFullCode(StockUtil.getStockFullCode(data));
				            stockCache.setTodayStartPrice(openPx);
				            stockCache.setNowPrice(lastPx);
				            stockCache.setYesClosePrice(preclosePx);
				            stockCache.setUpdatetime(dataTimestamp);
				            HSUtil.setRedisStockCache(stockCache);
				        }
	     		}
	     		return stockList;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
	}

	public static List<Stock> paseAllResult(String result) {
		try {
			Integer last_px =0;
			Integer preclose_px=0;
			Integer open_px=0;
			Integer prod_name=0;
			JSONObject jsonObj = JSONObject.fromObject(result).getJSONObject("data").getJSONObject("sort"); 
			JSONArray jsonField = jsonObj.getJSONArray("fields");
			for(int i=0;i<jsonField.size();i++){//获取字段的index值
				if(jsonField.getString(i).trim().equals("last_px")){
					last_px=i;
				}else if(jsonField.getString(i).trim().equals("preclose_px")){
					preclose_px=i;
				}else if(jsonField.getString(i).trim().equals("open_px")){
					open_px=i;
				}else if(jsonField.getString(i).trim().equals("prod_name")){
					prod_name=i;
				}
			}
			List<Stock> stockList = new ArrayList<Stock>();
			@SuppressWarnings("rawtypes")
			Set keys = jsonObj.keySet();
			for(Object key:keys){
				if(!key.equals("fields")){
					JSONArray stockArray = jsonObj.getJSONArray((String) key);
					if(!stockArray.isEmpty()){
						BigDecimal lastPx=new BigDecimal(stockArray.getString(last_px));//最新价格
						BigDecimal preclosePx = new BigDecimal(stockArray.getString(preclose_px));//昨日收盘价
						BigDecimal openPx=new BigDecimal(stockArray.getString(open_px));//今日开盘价
						String prodName= stockArray.getString(prod_name);
						Stock stock = new Stock();
						stock.setStockCode(StockUtil.HsFullCodeTransferCode(key.toString()));
						stock.setStockFullCode(StockUtil.getStockFullCode(key.toString()));
						stock.setStockName(prodName);
						stock.setNowPrice(lastPx);
						stock.setOpenPrice(openPx);
						stock.setYesPrice(preclosePx);
						stockList.add(stock);
					}
				}
			}
			return stockList;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
