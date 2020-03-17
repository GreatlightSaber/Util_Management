package co.kr.util.http;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


/**
 * Session 관리를 위한 Util
 * @author uracle
 * @since 2019.03.29
 */
public class SessionUtil {
    private static final String _LOGIN_USER_INFO = "loginUserInfo"; //로그인 한 사용자 정보
    
    private static RequestAttributes getRequestAttributes(){
        return RequestContextHolder.getRequestAttributes();
    }
    
    public static HttpServletRequest getHttpServletRequest(){
        return ((ServletRequestAttributes) getRequestAttributes()).getRequest();
    }
    
    /**
     * Session Id 가져오기
     * @return
     */
    public static String getSessionId(){
        return getRequestAttributes().getSessionId();
    }
    /**
     * Session 정보 조회
     * @param key
     * @return
     */
    public static Object getAttribute(String key){
        try{
            return getRequestAttributes().getAttribute(key, RequestAttributes.SCOPE_SESSION);
        }catch(NullPointerException npe){
            return null;
        }
    }
    public static Object getAttributeStr(String key){
        try{
            Object data =  getRequestAttributes().getAttribute(key, RequestAttributes.SCOPE_SESSION);
            return StringUtils.isEmpty(data) ? "" : data;
        }catch(NullPointerException npe){
            return null;
        }
    }
    
    /**
     * Session 설정
     * @param key
     * @param obj
     */
    public static void setAttribute(String key, Object obj){
        getRequestAttributes().setAttribute(key, obj, RequestAttributes.SCOPE_SESSION);
    }
    /**
     * Session 삭제
     * @param key
     */
    public static void removeAttribute(String key){
        getRequestAttributes().removeAttribute(key, RequestAttributes.SCOPE_SESSION);
    }
    
    /**
     * Session 삭제
     * @param keys
     */
    public static void removeAttributes(String ... keys){
        for(String key : keys){
            getRequestAttributes().removeAttribute(key, RequestAttributes.SCOPE_SESSION);
        }
    }
}
