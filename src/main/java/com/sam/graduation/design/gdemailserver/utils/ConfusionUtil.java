/**
 * 
 */
package com.sam.graduation.design.gdemailserver.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;

/**
 * 
 * 混淆工具类
 * @author louxinhua
 *
 */
public class ConfusionUtil {

	
	private static final Logger logger = LoggerFactory.getLogger(ConfusionUtil.class);



	
	final static char[] digits = { 
			'1', '2', '3', '4', '5', '6',
			'c', 'd', 'e', 'f', 'g', 'h',
			'i', 'j', 'k', 'l', 'm', 'n',
			'G', 'H', 'I', 'J', 'K', 'L',
			'7', '8', '9', '0', 'a', 'b',
			'u', 'v', 'w', 'x', 'y', 'z',
			'o', 'p', 'q', 'r', 's', 't',
			'A', 'B', 'C', 'D', 'E', 'F',
			'S', 'T', 'U', 'V', 'W', 'X',
			'M', 'N', 'O', 'P', 'Q', 'R',
			'Y', 'Z', '-', '_' };
	
//	public static final long WEB_COOKIE_TIME_OUT
	
//	public static final long MOBILE_DRIVER_TOKEN_TIME_INTERNAL = 30*24*60*60*1000;
	
	
	
	/**
	 * 混淆密码
	 * @param pw
	 * @param uID
	 * @return
	 */
	public static String pwToMD5(String pw, Long uID) {
		
		String result = ""; 
		
		try {
			
			String str = StringUtils.trim(pw);
			String uIDString = String.valueOf(uID);
			
			int index = 4;
			String s = uIDString.substring(0, index);
			String e = uIDString.substring(index);
			
			String inString = s + str + e;
			
			
	        byte [] buf = inString.getBytes();
	        MessageDigest md5 = MessageDigest.getInstance("MD5");
	        md5.update(buf);
	        byte [] tmp = md5.digest();
	        StringBuilder sb = new StringBuilder();
	        for (byte b:tmp) {
	            sb.append(Integer.toHexString(b&0xff));
	        }
	        
	        result = sb.toString();
			
		} catch (Exception e) {
			logger.error("pwToMD5 error : ", e);
		}
		
		return result;
	}
	
	/** 
     * 把10进制的数字转换成64进制 
     *  
     * @param number 
     * @param shift 
     * @return 
     */ 
	public static String compressNumber(long number) {  
        char[] buf = new char[64];  
        int charPos = 64;  
        int radix = 1 << 6;  
        long mask = radix - 1;  
        do {  
            buf[--charPos] = digits[(int) (number & mask)];  
            number >>>= 6;  
        } while (number != 0);  
        return new String(buf, charPos, (64 - charPos));  
    }
	
	/** 
     * 把64进制的字符串转换成10进制 
     *  
     * @param decompStr 
     * @return 
     */  
    private static long UnCompressNumber(String decompStr) {  
        long result = 0;  
        for (int i = decompStr.length() - 1; i >= 0; i--) {  
            for (int j = 0; j < digits.length; j++) {  
                if (decompStr.charAt(i) == digits[j]) {  
                    result += ((long) j) << 6 * (decompStr.length() - 1 - i);  
                }  
            }  
        }  
        return result;  
    }
    
	
//	public static void main(String[] aaaa) {
//		
//		String a = ConfusionUtil.pwToMD5("louxinhua", 45821634L);
//		String a = ConfusionUtil.pwToMD5("louxinhua", 45821634);
////		String b = ConfusionUtil.pwToMD5("LOUXINHUA", 45821634);
//		System.out.println(a);
////		System.out.println(b);
//		
////		System.out.println(compressNumber(Long.valueOf("45821634")));
//	}
	
}
