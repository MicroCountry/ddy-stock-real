package com.ddy.stock.real.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileReader;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;






import org.apache.commons.codec.binary.Base64;

import Decoder.BASE64Decoder;

/**
 * 加密处理
 * @author luopeng12856
 * 2016—05-26
 */
public class SecretToolkit {

	private static final String AES_ECB_PKCS5PADDING = "AES/ECB/PKCS5Padding";
	
	private static	SecretKeyInfo info=new SecretKeyInfo("AES-256");
	
	
	private static SecretToolkit instance;
	
	public static SecretToolkit getInstance() {
		if(instance == null) {
			instance = new SecretToolkit();
		}
		return instance;
	}
	
	
	
	//加密
	private String encrypt(String src, SecretKeyInfo info) {
		if(info != null) {
			String secret = info.getSecret();
			if(secret != null) {
				if(secret.equals(SecretKeyInfo.SECRET_AES_256)) {
					src = encryptAES256(src, info.getKey());
				} else if(secret.equals(SecretKeyInfo.SECRET_RC4_128)) {
					src = encryptRC4128(src, info.getKey());
				}
			}
		}
		return src;
	}
	
	public static String  password(String password){
		String  result=null;
		SecretToolkit secrettoolkit = new SecretToolkit();
		result=secrettoolkit.encrypt(password, info).trim();
		System.out.println("密码："+secrettoolkit.encrypt(password, info).trim());
		return result.trim();
	}
	
	//获取secret_key_info值
		public static String  secret_key_info(){
			String  result=null;
			try {
				BufferedReader br = new BufferedReader(new FileReader("D:/\\cert.pem"));
				System.out.println(br);
				String s = br.readLine();
				String str = "";
				s = br.readLine();
				while (s.charAt(0) != '-'){
					str += s + "\r";
					s = br.readLine();
				}
				String  tt=str.trim();
				BASE64Decoder decoder = new BASE64Decoder();
				byte[] byteCert = decoder.decodeBuffer(tt);
				//转换成二进制流
				ByteArrayInputStream bain = new ByteArrayInputStream(byteCert);
				/**
				 *  此类定义了用于从相关的编码中生成证书、证书路径 (CertPath) 和证书撤消列表 (CRL) 对象的 CertificateFactory 功能。
                 *  为了实现多个证书组成的编码，如果要解析一个可能由多个不相关证书组成的集合时，应使用 generateCertificates。否则，如果要生成 CertPath（证书链）并随后使用 CertPathValidator 验证它，则应使用 generateCertPath。
                 *  X.509 的 CertificateFactory 返回的证书必须是 java.security.cert.X509Certificate 的实例，CRL 的 CertificateFactory 返回的证书则必须是 java.security.cert.X509CRL 的实例。 
				 */
				CertificateFactory cf = CertificateFactory.getInstance("X.509");
				X509Certificate oCert = (X509Certificate)cf.generateCertificate(bain);
				Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
				cipher.init(Cipher.ENCRYPT_MODE, oCert);
				//按单部分操作加密或解密数据，或者结束一个多部分操作。数据将被加密或解密
				byte[] bytes = cipher.doFinal(info.toString().getBytes());
				result=Base64.encodeBase64String(bytes).replaceAll("\\s*|\t|\r|\n", "");
				} catch (Exception e) {
					e.printStackTrace();
				}
			System.out.println("secret_key_info:"+result.replaceAll("\r", ""));
			return  result.replaceAll("\r", "");
		}
	
	@SuppressWarnings("unused")
	private String decrypt(String src, SecretKeyInfo info) {
		if(info != null) {
			String secret = info.getSecret();
			if(secret != null) {
				if(secret.equals(SecretKeyInfo.SECRET_AES_256)) {
					src = decrytAES256(src, info.getKey());
				} else if(secret.equals(SecretKeyInfo.SECRET_RC4_128)) {
					src = decrytRC4128(src, info.getKey());
				}
			}
		}
		return src;
	}
	
	private String encryptAES256(String password, SecretKeySpec key) {
		if(password != null && key != null) {
			if(key != null) {
				try {
					Cipher cipher = Cipher.getInstance(AES_ECB_PKCS5PADDING);
					cipher.init(Cipher.ENCRYPT_MODE, key);
					byte[] bytes = cipher.doFinal(password.getBytes());
					password = Base64.encodeBase64String(bytes);
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("iTNSDK -- encrypt Exception");
				}
			}
		}
		return password;
	}
	
	private String decrytAES256(String password, SecretKeySpec key) {
		if(password != null && key != null) {
			if(key != null) {
				try {
					Cipher cipher = Cipher.getInstance(AES_ECB_PKCS5PADDING);
					cipher.init(Cipher.DECRYPT_MODE, key);
					byte[] bytes = cipher.doFinal(password.getBytes());
					password = Base64.encodeBase64String(bytes);
				} catch (Exception e) { }
			}
		}
		return password;
	}
	
	private String encryptRC4128(String password, SecretKeySpec key) {
		if (password != null && key != null) {
			try {
				Cipher cipher = Cipher.getInstance("RC4");
				cipher.init(Cipher.ENCRYPT_MODE, key);
				byte[] bytes = cipher.update(password.getBytes());
				password = Base64.encodeBase64String(bytes);
			} catch (Exception e) {
			}
		}
		return password;
	}
	
	private String decrytRC4128(String password, SecretKeySpec key) {
		if (password != null && key != null) {
			try {
				Cipher cipher = Cipher.getInstance("RC4");
				cipher.init(Cipher.DECRYPT_MODE, key);
				byte[] bytes = cipher.update(password.getBytes());
				password = Base64.encodeBase64String(bytes);
			} catch (Exception e) {
			}
		}
		return password;
	}
	
	
	
	
	public static void main(String[] args) {
		try {
			
		
		
		BufferedReader br = new BufferedReader(new FileReader("D:/\\cert.pem"));
		System.out.println(br);
		String s = br.readLine();
		String str = "";
		s = br.readLine();
		while (s.charAt(0) != '-'){
			str += s + "\r";
			s = br.readLine();
		}
		String  tt=str.trim();
		
		BASE64Decoder decoder = new BASE64Decoder();
		byte[] byteCert = decoder.decodeBuffer(tt);
		//转换成二进制流
		ByteArrayInputStream bain = new ByteArrayInputStream(byteCert);
		CertificateFactory cf = CertificateFactory.getInstance("X.509");
		X509Certificate oCert = (X509Certificate)cf.generateCertificate(bain);
		//String info = oCert.getSubjectDN().getName();      
		//System.out.println("证书拥有者:"+info);
		/*InputStream   in_withcode   =   new   ByteArrayInputStream(tt.getBytes());   
		CertificateFactory cf = CertificateFactory.getInstance("X.509");
		mCertificate = (X509Certificate)cf.generateCertificate(in_withcode);
		*/
		//SecretKeyInfo info=new SecretKeyInfo("AES-256");
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		cipher.init(Cipher.ENCRYPT_MODE, oCert);
		byte[] bytes = cipher.doFinal(info.toString().getBytes());
		//SecretKeyInfo	info1 = new SecretKeyInfo(SecretKeyInfo.SECRET_AES_256);
		SecretToolkit secrettoolkit = new SecretToolkit();
		System.out.println("密码："+secrettoolkit.encrypt("111111", info).trim());
		String  yy=Base64.encodeBase64String(bytes).replaceAll("\\s*|\t|\r|\n", "");
		
		System.out.println(yy);
		
		
		//String   sig=(String)signMethod.invoke(null, new Object[] { "http://sandbox.hs.net/oauth2/oauth2/oauthacct_trade_bind?targetbusinsys_no=1000&input_content=6&hs_signature=YWjanolruXfERZKisT9QFg&op_station=192.168.224.50&sendercomp_id=91011&secret_key_info=jaNqXLTYvqK0V0D0whLajdfNTfzEMASoEWNj0iN6BKxvBpXDUxJec9AsK6kqHB3ejekG+BivAjQjOh/Fk7MzqcLMqUsuv9RWygRv1uKcj4PzgkhF4qXeuoA00/NKTkBJm9Ez/X+IXDnMKRZlNOyDt3FdTUmK40sccZfujAB+zv15RjURn2I4g0d+Tg6Vv50qFAXwNMFCuXa4m/bkewXBNF2JAiKukpiml9Ml0BARkTIZH2kb427+TOvLFnBUAQTfM8JW2odD04R1RR4/06Lz1cgGypk70ryh0km6Re11KYDrGVDEFSXhkuh2TLnrlePQrjC1nzY2EoWi6ii8Sq/6JQ==&account_content=70000243&targetcomp_id=91000&password=xZh7MipQEVNY9t7X6YmO0g==]", "appSecretKey", "1F957EED26824BE693C8BDB813FF529F2015091115545938876592" });
		//System.out.println(sig);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
