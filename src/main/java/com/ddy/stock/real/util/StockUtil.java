package com.ddy.stock.real.util;

public class StockUtil {
	public static String getHSStockFullCode(String stockCode){
		char[] array=stockCode.toCharArray();
		 if(array.length!=6){
			 return null;
		 }
		 if(stockCode.startsWith("6")){
			 return stockCode+".SS";
	   	}else{
	   		 return stockCode+".SZ";
	   	}
	}
	public static String getStockFullCode(String stockCode){
		String fullCode = null;
    	if(stockCode.startsWith("6")){
    		fullCode = "sh"+stockCode;
    	}else{
    		fullCode = "sz"+stockCode;
    	}
    	return fullCode;
	}
	
	public static String HsFullCodeTransferCode(String stockFullCode){
		return stockFullCode.substring(0, 5);
	}
	
	public static String fieldsTransfer(String fields){
		return null;
	}
	
	public static String decodeUnicode(String theString) {
		 
        char aChar;
 
        int len = theString.length();
 
        StringBuffer outBuffer = new StringBuffer(len);
 
        for (int x = 0; x < len;) {
 
            aChar = theString.charAt(x++);
 
            if (aChar == '\\') {
 
                aChar = theString.charAt(x++);
 
                if (aChar == 'u') {
 
                    // Read the xxxx
 
                    int value = 0;
 
                    for (int i = 0; i < 4; i++) {
 
                        aChar = theString.charAt(x++);
 
                        switch (aChar) {
 
                        case '0':
 
                        case '1':
 
                        case '2':
 
                        case '3':
 
                        case '4':
 
                        case '5':
 
                        case '6':
                        case '7':
                        case '8':
                        case '9':
                            value = (value << 4) + aChar - '0';
                            break;
                        case 'a':
                        case 'b':
                        case 'c':
                        case 'd':
                        case 'e':
                        case 'f':
                            value = (value << 4) + 10 + aChar - 'a';
                            break;
                        case 'A':
                        case 'B':
                        case 'C':
                        case 'D':
                        case 'E':
                        case 'F':
                            value = (value << 4) + 10 + aChar - 'A';
                            break;
                        default:
                            throw new IllegalArgumentException(
                                    "Malformed   \\uxxxx   encoding.");
                        }
 
                    }
                    outBuffer.append((char) value);
                } else {
                    if (aChar == 't')
                        aChar = '\t';
                    else if (aChar == 'r')
                        aChar = '\r';
 
                    else if (aChar == 'n')
 
                        aChar = '\n';
 
                    else if (aChar == 'f')
 
                        aChar = '\f';
 
                    outBuffer.append(aChar);
 
                }
 
            } else
 
                outBuffer.append(aChar);
 
        }
 
        return outBuffer.toString();
 
    }
	
}
