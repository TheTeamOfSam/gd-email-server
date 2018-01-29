/**
 * 
 */
package com.sam.graduation.design.gdemailserver.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.Random;
import java.util.UUID;

/**
 * uuid 工具类
 * @author louxinhua
 *
 */
public class UUIDUtil {

	
	private static final int max=256;
	
	
	/**
	 * 获取纯的 UUID，没有横线
	 * @return
	 */
	public static String getUUIDWithoutLine() {
		
		String uuidIDFileString = UUID.randomUUID().toString().replaceAll("-", "");
		
		return uuidIDFileString;
	}
	
	
	/**
	 * 根据用户的内网ip、外网ip，组合uuid生成一串字符
	 * @param ipInternal
	 * @param ipOut
	 * @return
	 */
	public static String getUUIDAppendIPs(String ipInternal, String ipOut) {
		
		if ( StringUtils.isBlank(ipInternal) ) {
			ipInternal = random4();
		}
		else {}
		
		if ( StringUtils.isBlank(ipOut) ) {
			ipOut = random4();
		}
		else {}
		
		String randomIP = random4();
		
		String uuid = UUIDUtil.getUUIDWithoutLine();
		
		
		return (ipInternal + uuid + ipOut + "_" + randomIP).replaceAll(":", ".");
		
		
	}
	
	private static final int RANDOM_LENGTH = 4;
	/**
	 * 生成随机ip
	 * @return
	 */
	public static String random4() {
	
	   Random random = new Random();
	   String result = "";
       for (int i = 0; i < RANDOM_LENGTH; i++) {
    	   int s = random.nextInt(max);
    	   result += s;
       }
       return result;
	}
	
	
	
}
