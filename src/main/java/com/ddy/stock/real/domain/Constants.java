package com.ddy.stock.real.domain;

import java.io.InputStream;
import java.util.Properties;

public class Constants {
	public static final String OPEN="open";
	public static final String YEST="yest";
	public static final String NOW="now";
	
	public static  String oneSort="1,3,2";
	public static  String allSort="3,2,1,4";
	
	static {
        try {
            InputStream in = Constants.class.getClassLoader().getResourceAsStream("config.properties");
            Properties prop = new Properties();
            prop.load(in);
            oneSort = prop.getProperty("oneSort").trim();
            allSort = prop.getProperty("oneSort").trim();
        } catch (Exception e) {
        	e.printStackTrace();
        }
    }
}
