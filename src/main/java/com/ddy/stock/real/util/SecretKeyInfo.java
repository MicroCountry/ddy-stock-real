package com.ddy.stock.real.util;

import javax.crypto.spec.SecretKeySpec;
class SecretKeyInfo {
	
	public static final String SECRET_AES_256 = "AES-256";
	
	public static final String SECRET_RC4_128 = "RC-128";
	
	private String secret;
	
	private long time;
	
	private String random;
	
	private SecretKeySpec key;
	
	public SecretKeyInfo(String secret) {
		this.secret = secret;
		time = System.currentTimeMillis();
		RandomString temp = new RandomString();
		if(secret.equals(SECRET_AES_256)) {//32bit
			random = temp.random(time, 32);
			key = new SecretKeySpec(random.getBytes(), "AES");
		} else if(secret.equals(SECRET_RC4_128)) {//16bit
			random = temp.random(time, 16);
			key = new SecretKeySpec(random.getBytes(), "RC4");
		}
	}
	
	public SecretKeySpec getKey() {
		return key;
	}
	
	public String getSecret() {
		return secret;
	}
	
	public long getTime() {
		return time;
	}
	
	@Override
	public String toString() {
		return secret+"|"+time+"|"+random;
	}
	
	public static void main(String[] args) {
		SecretKeyInfo  inf=new SecretKeyInfo("AES-256");
		System.out.println(inf.toString());
		
		
	}
}
