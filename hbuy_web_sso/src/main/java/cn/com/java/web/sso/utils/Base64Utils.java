package cn.com.java.web.sso.utils;

import sun.misc.BASE64Decoder;

public class Base64Utils {

	/**
	 * 对字符串进行base64加密
	 * @param b
	 * @return
	 */
	public static String getBASE64(String b) {
		String base64 = null;
		if (b != null) {
			byte[] a=b.getBytes();
			base64 = new sun.misc.BASE64Encoder().encode(a);
		}
		return base64;
	}

	/**
	 * 对base64的字符串进行解密
	 * @param base64
	 * @return
	 */
	public static String getFromBASE64(String base64) {
		String r=null;
		try{
			if (base64 != null) {
				BASE64Decoder decoder = new BASE64Decoder();
				r= new String(decoder.decodeBuffer(base64));
			}
			return r;
		}catch(Exception e){
			return r;
		}
	}

	public static void main(String[] args) {
		try {
			//String number = "888";

			//String a = Base64Utils.getBASE64(number);

			////System.out.println("a:" + a);

			//String b = Base64Utils.getFromBASE64("55");

			//System.out.println("b:" + b);

			String buyCarStr = "productId=10001,num=3";

			//加密
			String base64 = Base64Utils.getBASE64(buyCarStr);
			System.out.println("加密后字符串："+base64);

			//解密
			String buyCarStr02 = Base64Utils.getFromBASE64(base64);
			System.out.println("解密后的字符串："+buyCarStr02);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
