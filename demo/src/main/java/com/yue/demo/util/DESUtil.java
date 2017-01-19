package com.yue.demo.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.SecureRandom;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import org.apache.http.impl.conn.Wire;

import com.iflytek.android.framework.util.StringUtils;

public class DESUtil {

	private final static String DES = "DES";

	public static void main(String[] args) throws Exception {
        
        String key = "iflytek_wjbz";
        File file =new File("E://workspace/BZFamily/assets/property.properties");
        File writeFile = new File("E://property.properties");
        if(!writeFile.exists()){
        	writeFile.createNewFile();
        }
        FileInputStream fis=new FileInputStream(file);
        FileInputStream writFis=new FileInputStream(writeFile);
        Properties properties = new Properties();
        Properties writProperties =new Properties();
        OutputStream fos = new FileOutputStream(writeFile);  
       
		try {
			properties.load(fis);
			writProperties.load(writFis);
			Enumeration enu = properties.propertyNames();
			Map<String, String> proMap=new HashMap<String, String>();
			while (enu.hasMoreElements()) {
				String count = (String)enu.nextElement();
				System.out.println("/**  */");
				String value = properties.getProperty(count).replace("@ip", "ip + \"")+"\";";
				System.out.println("public final static String "+count +" = "+ value);
				String str=encrypt(properties.getProperty(count), key);
//				System.err.println(str);
//				proMap.put((String)enu.nextElement(), str);
				writProperties.setProperty(count, str);
			}
			writProperties.store(fos, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
//        System.err.println(encrypt(data, key));
//        System.err.println(decrypt(encrypt(data, key), key));
//		String v = "Z4r9Ia1EaPG/qE2uDanZUX4ujgnQAHD34YsPOYxwgj+24Tins52gcZZYoUI/KysFRthbDzTWdzE\\=";
//		System.out.println("====" + decrypt(v, key));
 
    }

	/**
	 * Description 根据键值进行加密
	 * 
	 * @param data
	 * @param key
	 *            加密键byte数组
	 * @return
	 * @throws Exception
	 */
	public static String encrypt(String data, String key){
		if(StringUtils.isBlank(data)){
			return "";
		}
		byte[] bt=null;
		try {
			bt = encrypt(data.getBytes(), key.getBytes());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String strs = Base65.getEncoder().encodeToString(bt);
		return strs;
	}

	/**
	 * Description 根据键值进行解密
	 * 
	 * @param data
	 * @param key
	 *            加密键byte数组
	 * @return
	 * @throws IOException
	 * @throws Exception
	 */
	public static String decrypt(String data, String key){
		if(StringUtils.isBlank(data)){
			return "";
		}

		byte[] buf = Base65.getDecoder().decode(data);
		byte[] bt=null;
		try {
			bt = decrypt(buf, key.getBytes());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new String(bt);
	}

	/**
	 * Description 根据键值进行加密
	 * 
	 * @param data
	 * @param key
	 *            加密键byte数组
	 * @return
	 * @throws Exception
	 */
	private static byte[] encrypt(byte[] data, byte[] key) throws Exception {
		// 生成一个可信任的随机数源
		SecureRandom sr = new SecureRandom();

		// 从原始密钥数据创建DESKeySpec对象
		DESKeySpec dks = new DESKeySpec(key);

		// 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
		SecretKey securekey = keyFactory.generateSecret(dks);

		// Cipher对象实际完成加密操作
		Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");

		// 用密钥初始化Cipher对象
		cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);

		return cipher.doFinal(data);
	}

	/**
	 * Description 根据键值进行解密
	 * 
	 * @param data
	 * @param key
	 *            加密键byte数组
	 * @return
	 * @throws Exception
	 */
	private static byte[] decrypt(byte[] data, byte[] key) throws Exception {
		// 生成一个可信任的随机数源
		SecureRandom sr = new SecureRandom();

		// 从原始密钥数据创建DESKeySpec对象
		DESKeySpec dks = new DESKeySpec(key);

		// 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
		SecretKey securekey = keyFactory.generateSecret(dks);

		// Cipher对象实际完成解密操作
		Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");

		// 用密钥初始化Cipher对象
		cipher.init(Cipher.DECRYPT_MODE, securekey, sr);

		return cipher.doFinal(data);
	}
}
