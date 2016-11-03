package com.ddy.stock.real.util;

import java.util.Random;
/**
 * 加密处理数据随机
 * @author luopeng12856
 * 2016—05-26
 */
public class RandomString {
	
	private char[] chars = null;
	
	public RandomString() {
		chars = new char[]{'!','"','#','$','%','&','\'','('
				,')','*','+',',','-','.','/','0','1','2'
				,'3','4','5','6','7','8','9',':',';','<'
				,'=','>','?','@','A','B','C','D','E','F'
				,'G','H','I','J','K','L','M','N','O','P'
				,'Q','R','S','T','U','V','W','X','Y','Z'
				,'[', '\\',']','^','_','`','a','b','c','d'
				,'e','f','g','h','i','j','k','l','m','n'
				,'o','p','q','r','s','t','u','v','w','x'
				,'y','z','{','|','}','~'};
	}
	
	public RandomString(char[] chars) {
		this.chars = chars;
	}
	
	public String random(long seed, int length) {
		Random random = new Random(seed);
		StringBuffer buffer = new StringBuffer(length);
		for(int i=0; i<length; i++) {
			buffer.append(chars[random.nextInt(length)]);
		}
		return buffer.toString();
	}

}
