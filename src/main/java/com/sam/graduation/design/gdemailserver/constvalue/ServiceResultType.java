/**
 * 
 */
package com.sam.graduation.design.gdemailserver.constvalue;

/**
 * 服务返回结果的code
 * 
 * @author louxinhua
 *
 */
public class ServiceResultType {

	/**
	 * 没有异常
	 */
	public static final Integer RESULT_TYPE_SUCCESS = 1;

	// 
	/**
	 * 系统异常
	 */
	public static final Integer RESULT_TYPE_SYSTEM_ERROR = -99999;


	/**
	 * 业务异常
	 */
	public static final Integer RESULT_TYPE_SERVICE_ERROR = -98888;


	/**
	 * md5解析异常
	 */
	public static final Integer RESULT_MD5_SYSTEM_ERROR = -10001;

 
	/**
	 * 业务异常
	 */
	public static final Integer RESULT_BUSINESS_SYSTEM_ERROR = -10002;
	
	/**
	 * 请求参数异常
	 */
	public static final Integer RESULT_TYPE_ERROR_PARAM_EXCEPTION = -10003;
	
	/**
	 * 需要登录
	 */
	public static final Integer RESULT_TYPE_ERROR_NEED_LOGIN = -100;
	
	

}
