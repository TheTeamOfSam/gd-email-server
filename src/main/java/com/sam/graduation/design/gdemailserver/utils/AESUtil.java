/**
 * 
 */
package com.sam.graduation.design.gdemailserver.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * 加密解密类
 * @author louxinhua
 *
 */
public class AESUtil {

	private static final Logger logger = LoggerFactory.getLogger(AESUtil.class);
	
	
	/**
	 * 加密的 key
	 */
	private static final String p_w_a_ = "#41^#$*shihe^$jn111[];;;;q#41^#$*sehsfgdjn111[];;;;q#41^#$*jdx1fgdjn111[];;;;q#41^#$*";
	
	
	
	/** 
	 * 加密 
	 *  
	 * @param content 需要加密的内容 
	 * @param password  加密密码 
	 * @return 
	 */  
	public static String encrypt(String content) {  
		
        try {
         	String password = p_w_a_; // 先写死
        	
        	SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            random.setSeed(password.getBytes());
        	
	        KeyGenerator kgen = KeyGenerator.getInstance("AES");  
	        kgen.init(128, random);  
	        SecretKey secretKey = kgen.generateKey();  
	        byte[] enCodeFormat = secretKey.getEncoded();  
	        SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");  
	        Cipher cipher = Cipher.getInstance("AES");// 创建密码器  
	        cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化  
	        byte[] byteContent = content.getBytes("utf-8");  
	        byte[] result = cipher.doFinal(byteContent);  
	        return parseByte2HexStr(result); // 加密
            
	        
        } catch (NoSuchAlgorithmException e) {  
            
        	logger.error("encrypt '" + content + "' fail, error : ", e);
        	
        } catch (NoSuchPaddingException e) {  
            
        	logger.error("encrypt '" + content + "' fail, error : ", e);
        	
        } catch (InvalidKeyException e) {  
            
        	logger.error("encrypt '" + content + "' fail, error : ", e);
        	
        } catch (UnsupportedEncodingException e) {  
            
        	logger.error("encrypt '" + content + "' fail, error : ", e);
        	
        } catch (IllegalBlockSizeException e) {  
            
        	logger.error("encrypt '" + content + "' fail, error : ", e);
        	
        } catch (BadPaddingException e) {  
            
        	logger.error("encrypt '" + content + "' fail, error : ", e);
        	
        }
        return null;  
	}
	
	private static Cipher de_cipher = null;
	
	static {
		init_decrypt();
	}
	
	public static void init_decrypt()
	{
		//System.out.println(222);
		try
		{
			String password = p_w_a_; // 先写死
	        KeyGenerator kgen = KeyGenerator.getInstance("AES");
	        
	        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
	        random.setSeed(password.getBytes());
	        
	        kgen.init(128, random);  
	        SecretKey secretKey = kgen.generateKey();  
	        byte[] enCodeFormat = secretKey.getEncoded();  
	        SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");              
	        de_cipher = Cipher.getInstance("AES");// 创建密码器
	        de_cipher.init(Cipher.DECRYPT_MODE, key);// 初始化
	    } catch (NoSuchAlgorithmException e) {  
	        logger.error("decrypt fail, error : ", e);  
		} catch (NoSuchPaddingException e) {  
		        logger.error("decrypt fail, error : ", e);  
		} catch (InvalidKeyException e) {  
		        logger.error("decrypt fail, error : ", e);  
		} catch (Exception e) {
			logger.error("decrypt fail, error : ", e);
        }
		
	}
	/**解密 
	 * @param content  待解密内容 
	 * @param password 解密密钥 
	 * @return 
	 */  
	public static String decrypt(String enCodecontent) {  
        try {  
        		
            byte[] decryptFrom = parseHexStr2Byte(enCodecontent);  
            byte[] result = de_cipher.doFinal(decryptFrom);//cipher.doFinal(decryptFrom);  
            return new String(result); // 加密  
        } catch (IllegalBlockSizeException e) {  
            logger.error("decrypt '" + enCodecontent + "' fail, error : ", e);  
        } catch (BadPaddingException e) {  
            logger.error("decrypt '" + enCodecontent + "' fail, error : ", e);  
        } catch (Exception e) {
        		logger.error("decrypt '" + enCodecontent + "' fail, error : ", e);
        }
        return null;  
	}
	
	public static String decrypt_old(String enCodecontent) {  
        try {  
        		String password = p_w_a_; // 先写死
                KeyGenerator kgen = KeyGenerator.getInstance("AES");
                
                SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
                random.setSeed(password.getBytes());
                
                kgen.init(128, random);  
                SecretKey secretKey = kgen.generateKey();  
                byte[] enCodeFormat = secretKey.getEncoded();  
                SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");              
                Cipher cipher = Cipher.getInstance("AES");// 创建密码器
                cipher.init(Cipher.DECRYPT_MODE, key);// 初始化*/
                byte[] decryptFrom = parseHexStr2Byte(enCodecontent);  
                byte[] result = cipher.doFinal(decryptFrom);//cipher.doFinal(decryptFrom);  
                return new String(result); // 加密  
	    } catch (NoSuchAlgorithmException e) {  
	        logger.error("decrypt '" + enCodecontent + "' fail, error : ", e);  
		} catch (NoSuchPaddingException e) {  
		        logger.error("decrypt '" + enCodecontent + "' fail, error : ", e);  
		} catch (InvalidKeyException e) {  
		        logger.error("decrypt '" + enCodecontent + "' fail, error : ", e);  
        } catch (IllegalBlockSizeException e) {  
                logger.error("decrypt '" + enCodecontent + "' fail, error : ", e);  
        } catch (BadPaddingException e) {  
                logger.error("decrypt '" + enCodecontent + "' fail, error : ", e);  
        } catch (Exception e) {
        	logger.error("decrypt '" + enCodecontent + "' fail, error : ", e);
        }
        return null;  
	}
	
	
	
	/**将16进制转换为二进制 
	 * @param hexStr 
	 * @return 
	 */  
	private static byte[] parseHexStr2Byte(String hexStr) {  
        if (hexStr.length() < 1)  
                return null;  
        byte[] result = new byte[hexStr.length()/2];  
        
        int high, low;
            int j = 0;
        for(int i = 0; i < hexStr.length(); i += 2)
        {
        	high = hexStr.charAt(i);
        	if(high >= '0' && high <= '9')
        		high -= '0';
        	else if(high >= 'A' && high <= 'F')
        		high -= 'A' - 10;
        	else if(high >= 'a' && high <= 'f')
        		high -= 'a' - 10;
        	else
        		high = 0;
        	
        	low = hexStr.charAt(i + 1);
        	if(low >= '0' && low <= '9')
        		low -= '0';
        	else if(low >= 'A' && low <= 'F')
        		low -= 'A' - 10;
        	else if(low >= 'a' && low <= 'f')
        		low -= 'a' - 10;
        	else
        		low = 0;
        	
        	result[j++] = (byte) ((high << 4) + low);
        }
        return result;  
	}
	
	
	/**将二进制转换成16进制 
	 * @param buf 
	 * @return 
	 */  
	private static String parseByte2HexStr(byte buf[]) {  
		
        StringBuffer sb = new StringBuffer();
        
        String hex = "0123456789ABCDEF";
        int j;
        for (int i = 0; i < buf.length; i++)
        {
        	j = (buf[i] >> 4) & 0x0F;
        	sb.append(hex.charAt(j));
        	j = buf[i] & 0x0F;
        	sb.append(hex.charAt(j));
        }
        
        return sb.toString();  
	}
	
	
	
	
	
	
}
