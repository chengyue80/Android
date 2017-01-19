package com.yue.demo.util;

import java.util.Map;

import Decoder.BASE64Decoder;

public class RSATester {

	static String publicKey;
	static String privateKey;

	public static void main(String[] args) throws Exception {

		try {
			Map<String, Object> keyMap = RSAUtils.genKeyPair();
			publicKey = RSAUtils.getPublicKey(keyMap);
			privateKey = RSAUtils.getPrivateKey(keyMap);
			System.err.println("公钥: \n\r" + publicKey);
			System.err.println("私钥： \n\r" + privateKey);
		} catch (Exception e) {
			e.printStackTrace();
		}

		 RSAUtils.generateKeyPair("PublicKey.pem","PrivateKey.pem");
		 System.out.println(RSAUtils.getKeyFromFile("PublicKey.pem"));

		test();
		test1();

		testSign();
	}

	static void test() throws Exception {
		System.out.println("=====================");
		System.err.println("公钥加密——私钥解密");
		String source = "这是一行没有任何意义的文字，你看完了等于没看，不是吗？";
		System.out.println("加密前文字：\r\n" + source);
		String encodedData = RSAUtils.encryptByPublicKey(source, publicKey);
		System.out.println("加密后文字：\r\n" + new String(encodedData));
		byte[] decodedData = RSAUtils.decryptByPrivateKey(encodedData,
				privateKey);
		String target = new String(decodedData);
		System.out.println("解密后文字: \r\n" + target);
	}

	static void test1() throws Exception {
		System.out.println("=====================");
		System.err.println("私钥加密——公钥解密");
		String source = "这是一行测试RSA数字签名的无意义文字";
		System.out.println("原文字：\r\n" + source);
		String encodedData = RSAUtils.encryptByPrivateKey(source, privateKey);
		System.out.println("加密后：\r\n" + encodedData);
		byte[] decodedData = RSAUtils
				.decryptByPublicKey(encodedData, publicKey);
		String target = new String(decodedData);
		System.out.println("解密后: \r\n" + target);

	}

	static void testSign() throws Exception {
		System.out.println("=====================");
		System.err.println("私钥签名——公钥验证签名");
		String source = "20990000028804岳阳34122519901111000118255364010http://myhome.bozhou.gov.cn/test_download/myhome.apk";
		String sign = RSAUtils.sign(source, privateKey);
		System.out.println("签名:\r" + sign);
		boolean status = RSAUtils.verify(source, publicKey, sign);
		System.err.println("验证结果:\r" + status);
	}

}
