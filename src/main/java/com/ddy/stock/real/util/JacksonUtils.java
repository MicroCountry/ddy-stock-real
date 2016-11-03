package com.ddy.stock.real.util;
import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import java.io.IOException;
import java.text.SimpleDateFormat;

public abstract class JacksonUtils {

    private static final Logger logger = LoggerFactory
        .getLogger(JacksonUtils.class);

    private static ObjectMapper objectMapper = new ObjectMapper();

    static {
        // 设置默认日期格式
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        //提供其它默认设置
        objectMapper.setFilters(new SimpleFilterProvider()
            .setFailOnUnknownId(false));
    }

    /**
     *  转换对象到json 格式字符串，如果转换失败 返回null
     * @param object
     * @return
     */
    public final static String toJsonString(Object object, String... properties) {
        try {
            SimpleFilterProvider fileter = new SimpleFilterProvider();
            fileter.addFilter(
                AnnotationUtils.findAnnotation(object.getClass(),
                    JsonFilter.class).value(),
                SimpleBeanPropertyFilter.filterOutAllExcept(properties));
            return objectMapper.writer(fileter).writeValueAsString(object);
        } catch (JsonProcessingException e) {
            logger.error("转换对象【" + object.toString() + "】到json格式字符串失败：" + e);
            return null;
        }
    }

    public final static  String toJsonString(Object object){
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            logger.error("转换对象【" + object.toString() + "】到json格式字符串失败：" + e);
            return null;
        }
    }

    /**
     * json格式字符串转换成对象
     * @param json
     * @param clazz
     * @param <T>
     * @return
     */
    public final static  <T> T json2Object (String json,Class<T> clazz){
        try {
            return objectMapper.readValue(json,clazz);
        } catch (IOException e) {
            logger.error("转换json【" + json + "】到对象失败：" + e);
            return null;
        }
    }
    
    
    public static void main(String[] args) {
    	
    	
    	//String ttt="{\"code\":\"0000\",\"msg\":\"查询余额成功\",\"obj\":{\"longAccountMoney\":\"99900\"},\"success\":\"true\"}";

    
    //	ttb   data=JacksonUtils.json2Object(ttt, ttb.class);
   // System.out.println("code:"+data.getCode());
   // System.out.println("longAccountMoney:"+data.getObj().getLongAccountMoney());
    	//System.out.println(data.getDatasource().size());
    	
    	
    	/*String yy="{\"token_type\":\"bearer\",\"access_token\":\"768389517E6C4BF2A592B1FE72BB9BA82015052214554434116169\",\"expires_in\":86400,\"refresh_token\":

\"231D3225D7D94D76BDC88AC0C356E85C2015052214554434197823\",\"scope\":\"epay,trade,iuc,info\"}";
    	AccessToken accesstoken=JacksonUtils.json2Object(yy, AccessToken.class);
    	AccessToken accesstoken1=new AccessToken(); 
    	accesstoken1.setAccess_token("1234");
    	String  tt=JacksonUtils.toJsonString(accesstoken1);
    	AccessToken accesstoken2=new AccessToken(); 
    	accesstoken2.setAccess_token("1");
    	accesstoken2.setExpires_in("2");
    	accesstoken2.setRefresh_token("3");
    	accesstoken2.setScope("4");
    	accesstoken2.setToken_type("5");
    	String  yy1=JacksonUtils.toJsonString(accesstoken2,"1");
    	System.out.println(tt);
    	System.out.println(yy1);*/
	}
}
