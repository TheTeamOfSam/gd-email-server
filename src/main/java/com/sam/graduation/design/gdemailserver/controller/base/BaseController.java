/**
 *
 */
package com.sam.graduation.design.gdemailserver.controller.base;

import com.sam.graduation.design.gdemailserver.constvalue.ServiceResultType;
import com.sam.graduation.design.gdemailserver.utils.AESUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Controller接口的基类
 *
 * @author louxinhua
 */
public class BaseController {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 表示请求是否成功的 key
     *
     * @author louxinhua
     */
    public static final String RESULT_ISSUCCESS_KEY = "is_success";

    /**
     * 错误码
     *
     * @author louxinhua
     */
    public static final String RESULT_ERROR_CODE_KEY = "error_code";

    /**
     * 消息 key, 一般用于错误时候的消息提示
     *
     * @author louxinhua
     */
    public static final String RESULT_MSG_KEY = "message";

    /**
     * 返回的业务数据内容, 值必须是 object.
     *
     * @author louxinhua
     */
    public static final String RESULT_DATA_KEY = "result";

    public static final String RESULT_IS_LOGIN = "isLogin";

    /**
     * 客户端唯一表示的 key
     */
    public static final String CLIENT_UNI_FLAG_KEY = "JALKSJFJASKDFJKALSJDFLJSF";

    /**
     * http请求的token参数的key
     */
    public static final String KEY_TOKEN = "token";

    /**
     * 客户端信息对象的key
     */
    public static final String KEY_CLIENT_INFO = "KEY_CLIENT_INFO";

    /**
     * 用户id的key
     */
    public static final String KEY_USER_ID = "userId";

    /**
     * 表示成功
     *
     * @param value
     * @return
     */
    public Map<String, Object> success(Object value) {
        //return message(MESSAGE_TYPE_SUCCESS, value, 0);
        return createResultMap(true, ServiceResultType.RESULT_TYPE_SUCCESS, "", value);
    }

    public Map<String, Object> error(String msg, int errCode) {
        //return message(MESSAGE_TYPE_ERROR, msg, ErrCode);
        return createResultMap(false, errCode, msg, null);
    }


    /**
     * 返回给客户端的 json map 对象
     *
     * @param isSuccess  是否成功
     * @param errorCode  错误码, 只有 isSuccess 为 false 的时候才用到.
     * @param msg        提示信息, 客户端可以根据这个现实 toast,alert 等.
     * @param resultData 返回的业务数据, 只有isSuccess为 true 的时候才会设置.
     * @return
     */
    protected Map<String, Object> createResultMap(Boolean isSuccess, Integer errorCode, String msg, Object resultData) {

        Map<String, Object> result = new HashMap<String, Object>();

        result.put(RESULT_ISSUCCESS_KEY, isSuccess);

        if (isSuccess) {
            result.put(RESULT_DATA_KEY, resultData);
        } else {
            result.put(RESULT_ERROR_CODE_KEY, errorCode);
        }

        result.put(RESULT_MSG_KEY, msg);

        return result;

    }


    /**
     * 获取 cookie 的值
     *
     * @param key
     * @return
     */
    protected String getCookieByKey(String key) {

        if (StringUtils.isBlank(key)) {
            return null;
        } else {
            String tempKey = key.trim();
            Cookie[] cookies = getHttpServletRequest().getCookies();
            String result = null;
            if (cookies != null) {
                boolean hasKey = false;
                for (Cookie cookie : cookies) {
                    String name = cookie.getName();// get the cookie name
                    if (tempKey.equals(name)) {
                        hasKey = true;
                        result = cookie.getValue(); // get the cookie value
                        if (StringUtils.isNotBlank(result)) {
                            break;
                        } else {
                        }
                    } else {
                    }
                }

                if (!hasKey) {
                    logger.warn("Warn, no cookies key, {}", key);
                } else {

                }

            } else {
                logger.warn("Warn, cookies is null!");
            }
            return result;
        }
    }


    /**
     * 获取 request
     *
     * @return
     */
    protected HttpServletRequest getHttpServletRequest() {
        // FIXME:如果要获取带上传文件的 request，则不是这样的。
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return request;
    }


    /**
     * 获取 response
     *
     * @return
     */
    protected HttpServletResponse getHttpServletResponse() {
        HttpServletResponse resp = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        return resp;
    }

    /**
     * 通过HttpServletRequest返回IP地址
     *
     * @return ip String
     * @throws Exception
     */
    protected String getIpAddr() {
        HttpServletRequest request = this.getHttpServletRequest();
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }


    /**
     * 获取用户id
     *
     * @return
     */
    protected Long getUserIDFromToken() {
        Object obj = this.getHttpServletRequest().getAttribute(KEY_CLIENT_INFO);

        Map<String, String> clientInfo = null;

        if (obj != null && obj instanceof HashMap) {
            @SuppressWarnings("unchecked")
            Map<String, String> obj2 = (HashMap<String, String>) obj;
            clientInfo = obj2;
        } else {
        }


        if (clientInfo != null && clientInfo.size() > 0) {
            // 获取userid
            String userId = StringUtils.trimToEmpty(clientInfo.get(KEY_USER_ID));
            return Long.valueOf(userId);
        } else {

        }
        return 0L;
    }




    public String setLoginInfo(Long userid, String ip) {
        //
        // :原来的测试代码
        // String token = String.valueOf(user.getId());
        // return token;

        // :对id进行加密处理
        String userID = String.valueOf(userid);
        String a = "kljfaljdsfkljalksdj46728nsdndjk"; // 占位， deviceCode, 用于单点登录,
        // 先写死。。。
        String b = String.valueOf(new Date().getTime()); // 时间

        String bToken = userID + "_" + a + "_" + b;

        String encode = AESUtil.encrypt(bToken);
        return encode;
    }




}
