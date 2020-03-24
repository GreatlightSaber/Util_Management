package co.kr.util.string;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * @Class Name : StringUtil.java
 * @Author : kth
 * @Description : 문자열 처리 모듈
 *
 * @Copyright (c) IPARK SYSTEM Uracle All right reserved.
 * <pre>
 *------------------------------------------------------------------------
 * Modification Information
 *------------------------------------------------------------------------   
 * 수정일 / 수정자 / 수정내용
 * 2019.08.27 / kth / 최초 개발
 *------------------------------------------------------------------------
 * </pre>  
 */
@Component
public class StringUtil  extends StringUtils{
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	/**
	 * @Method Name : toHlflChar
	 * @Description : 반각 -> 전각, 전각 -> 반각
	 * @param item : 변경 할 값
	 * @return String : 변환 데이터
	 */
	public String toHlflChar(String item){
		
		String rslt = null;
		boolean itemChck = true; // 반각일 경우 true, 아니면 false
		
		// item 값이 null이면 null을 주자
        if (item == null)
            return null;
		
        itemChck = hlflCheck(item);
        
        // 반각은 전각으로 전각은 반각으로 변환
        if(itemChck == true){
        	rslt = toEmChar(item);
        }else{
        	rslt = toEnChar(item);
        }
        
		return rslt;
	}
	
	/**
	 * @Method Name : nvl
	 * @Description : item 값이 null거나 ""이면 ""으로 치환하여 return한다
	 * @param item : 변경 할 값
	 * @return String : 변환 데이터
	 */
    public static String nvl(String item) {
		
		try {
			if (item == null || "".equals(item) || "null".equalsIgnoreCase(item)){
				return "";
			}
		} catch (Exception e) {
			return "";
		}
		
		return item;
	}
		
	/**
	 * @Method Name : nvl
	 * @Description : item 값이 null거나 ""이면 valItem으로 치환하여 return한다
	 * @param item : 확인할 값
     * @param valItem : 치환할 값
	 * @return String : 변환 데이터
	 */
    public static String nvl(String item, String valItem) {
    	
		try {
			if (item == null || "".equals(item) || "null".equalsIgnoreCase(item)){
				return valItem;
			}
		} catch (Exception e) {
			return valItem;
		}
		
		return item;
	}
    
	/**
	 * @Method Name : nvl
	 * @Description : item에서 key로 데이터 추출 후 null거나 ""이면 ""으로 치환하여 return한다
	 * @param item : 확인할 값
     * @param valItem : 치환할 값
	 * @return String : 변환 데이터
	 */
    public static String nvl(Map<String, Object> item, String valKey) {		
    	
		try {
			return item.get(valKey).toString();
		} catch (Exception e) {
			return "";
		}
		
	}
    
	/**
	 * @Method Name : nvl
	 * @Description : item에서 key로 데이터 추출 후 null거나 ""이면 nullVal를 return한다
	 * @param item : 확인할 값
     * @param valItem : 치환할 값
     * @param nullVal : 빈값일 경우 치환할 값
	 * @return String : 변환 데이터
	 */
    public static String nvl(Map<String, Object> item, String valKey, String nullVal) {		
    	
		try {
			return item.get(valKey).toString();
		} catch (Exception e) {
			return "";
		}
		
	}
    
    public String dbFormat(double item) {		
    	
    	DecimalFormat fm = new DecimalFormat("#.##");
   	 	    	
    	return fm.format(item).toString();		
	}
    
    
	/**
	 * @Method Name : nvl
	 * @Description : item(String)을 replaceAll 시켜 반환한다. 단 item 값이 null or "" 일 경우 "" 값을 반환함
     * @param item : 확인할 object
     * @param chckVal : 변경 이전 값
     * @param chngVal : 변경 이후 값
	 * @return String : 변환 데이터
	 */
    public String nvl2(String item, String chckVal, String chngVal) {
    	
    	String chckItem = nvl(item);
    	
    	if(chckItem.equals("")) return "";
    	
		try {
			return item.replaceAll(chckVal, chngVal);
		} catch (Exception e) {
			return "";
		}
		
	}
    
    /**
     * item(Map)을 replaceAll 시켜 반환한다. 단 item 값이 null or "" 일 경우 "" 값을 반환함
     * @param item 확인할 object
     * @param valKey Map 추출 key
     * @param chckVal 변경 이전 값
     * @param chngVal 변경 이후 값
     * @return String 변경한 값
     */
    public String nvl2(Map<String, Object> item, String valKey, String chckVal, String chngVal) {
    	
    	String chckItem = nvl(item, valKey);
    	
    	if(chckItem.equals("")) return "";
    	
		try {
			return chckItem.replaceAll(chckVal, chngVal);
		} catch (Exception e) {
			return "";
		}
		
	}
    
	/**
     * 반각으로만 구성이 되어있는지 구분
     * @param item 구분할 값
     * @return boolean 반각일 경우 true, 아니면 false
     */
    public boolean hlflCheck(String item){
        byte[] byteArray = null;
        byteArray = item.getBytes();
        for(int i = 0; i < byteArray.length; i++){
            if((byteArray[i] >= (byte)0x81 && byteArray[i] <= (byte)0x9f) ||
                (byteArray[i] >= (byte)0xe0 && byteArray[i] <= (byte)0xef)) {
                if((byteArray[i+1] >= (byte)0x40 && byteArray[i+1] <= (byte)0x7e) ||
                    (byteArray[i+1] >= (byte)0x80 && byteArray[i+1] <= (byte)0xfc)) {
                    return false;
                }
            }
        }        
        return true;
    }
	
	/**
     * 반각문자로 변환
     * @param item 변경할 값
     * @return String 변경한 값
     */
    public String toEnChar(String item){
        
    	StringBuffer strBuf = new StringBuffer();    	
        
    	try {
    		char c = 0;            
            int nSrcLength = item.length();
            
            for (int i = 0; i < nSrcLength; i++){
                c = item.charAt(i);
               
                if (c >= '！' && c <= '～'){
                    c -= 0xfee0;
                }else if (c == '　'){
                    c = 0x20;
                }
                
                strBuf.append(c);
            }
            
            return strBuf.toString();
            
		} catch (Exception e) {
			return null;
		}
    	
    }
    
    /**
     * 전각문자로 변환
     * @param item 변경할 값
     * @return String 변경한 값
     */
    public String toEmChar(String item){
        
        StringBuffer strBuf = new StringBuffer();
        
        try {
            char c = 0;            
            int nSrcLength = item.length();
            
            for (int i = 0; i < nSrcLength; i++){
                c = item.charAt(i);            
               
                if (c >= 0x21 && c <= 0x7e){
                    c += 0xfee0;
                }else if (c == 0x20){
                    c = 0x3000;
                }
                
                strBuf.append(c);
            }
            
            return strBuf.toString();
            
		} catch (Exception e) {
			return null;
		}
        
    }
    
    /**
     * 세틀뱅크 연동을 위한 인코딩 모듈
     * @param text - 인코딩 할 텍스트
     * @return 인코딩 값
     * */
    public String toHexString(byte[] text) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i<text.length; i++) {
			sb.append(String.format("%02X",text[i]));
		}
		return sb.toString();
	}
    
    /**
     * 세틀뱅크 연동을 위한 SHA256 인코딩 모듈
     * @param str - 인코딩 할 텍스트
     * @return 인코딩 값
     * */
    public String getSHA256(String str) {
		String SHA = "";
		try {
			MessageDigest sh = MessageDigest.getInstance("SHA-256");

			sh.update(str.getBytes());
			byte byteData[] = sh.digest();
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i<byteData.length; i++) {
				sb.append(Integer.toString((byteData[i]&0xff)+0x100,16).substring(1));
			}
			SHA = sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			SHA = null;
		}
		return SHA;

	}
    
    /**
     * 세틀뱅크 연동을 위한 AES 암호화 모듈
     * @param sKey - 암호화 키
     * @param sText - 암호화 할 텍스트
     * @return 암호화한 값
     * */
    public byte[] aesEncryptEcb(String sKey, String sText) {
		int aesKeySize = 128;
		return aesEncryptEcb(sKey, aesKeySize, sText);
	}
    
    private byte[] aesEncryptEcb(String sKey, int aesKeySize, String sText) {
		byte[] key = null;
		byte[] text = null;
		byte[] encrypted = null;

		try {
			// UTF-8
			key = sKey.getBytes("UTF-8");
			// Key size (128bit, 16byte)
			key = Arrays.copyOf(key, aesKeySize/8);
			// UTF-8
			text = sText.getBytes("UTF-8");
			// AES/EBC/PKCS5Padding
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE,new SecretKeySpec(key,"AES"));
			encrypted = cipher.doFinal(text);
		} catch (Exception e) {
			encrypted = null;
			e.printStackTrace();
		}

		return encrypted;
	}
    
    /**
	 * response ModelAndView 생성 및 데이터 설정
	 * 
	 * @param reqHeadMap
	 * @param respBodyMap
	 * @return
	 */
	public Map<String,Object> jsonToMap(String json) {
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			if (json!=null&&!json.trim().isEmpty()) {
				Gson gson = new Gson();
				map = (Map<String,Object>) gson.fromJson(json,map.getClass());
			}
		} catch (Exception ignored) {
			//logger.error(LocalPayConst.ERROR_TAG, ignored);
		}
		return map;
	}
	
    /**
	 * response ModelAndView 생성 및 데이터 설정
	 * 
	 * @param reqHeadMap
	 * @param respBodyMap
	 * @return
	 */
	public static Map<String,Object> jsonToMap(String json, String key) {
		Map<String,Object> rtnMap = new HashMap<String,Object>();
		try {
			if (json!=null&&!json.trim().isEmpty()) {
				
		    	JsonElement jelementMain = new JsonParser().parse(json);    	
		    	JsonObject  jobjectMain = jelementMain.getAsJsonObject();	
		    	
		    	String rtnData = jobjectMain.get(key).toString();
		    	
		    	JsonElement jelementSub = new JsonParser().parse(rtnData);    	
		    	JsonObject  jobjectSub = jelementSub.getAsJsonObject();	
		    	
		    	Map<String,Object> map = new HashMap<String,Object>();    	
		    	Gson gson = new Gson();
		    	rtnMap = (Map<String,Object>) gson.fromJson(jobjectSub, map.getClass());    					
				
			}
		} catch (Exception ignored) {
			//logger.error(LocalPayConst.ERROR_TAG, ignored);
		}
		return rtnMap;
	}
	
    /**
	 * json 특정 String 파싱
	 * 
	 * @param reqHeadMap
	 * @param respBodyMap
	 * @return
	 */
	public static String jsonToString(String json, String Key) {
		
		String rtnData = "";
		
		try {
			JsonElement jelement = new JsonParser().parse(json);    	
	    	JsonObject  jobject = jelement.getAsJsonObject();	
	    	
	    	rtnData = jobject.get(Key).getAsString();
		} catch (Exception e) {
			// TODO: handle exception
		}
    	
    	return rtnData;
	}
	
	/**
	 * json 특정 List<Map<String, Object>> 파싱
	 * 
	 * @param json
	 * @param Key
	 * @return List<Map<String, Object>>
	 */
	public List<Map<String, Object>> jsonToListMap(String json, String Key) {
		
		List<Map<String, Object>> listMap = new ArrayList<>();
		
		JsonElement jelement = new JsonParser().parse(json.toString());    	
    	JsonObject  jobjectMain = jelement.getAsJsonObject();	
    	
    	JsonArray MainArr = jobjectMain.get(Key).getAsJsonArray();
    	
    	int cnt = 0;
    	
    	for (JsonElement jsonElementArr : MainArr) {
    		JsonObject  jobjectSub = jsonElementArr.getAsJsonObject();
    		    		
    		Map<String, Object> __Map = new HashMap<>();
    		
			for (Entry<String, JsonElement> e : jobjectSub.entrySet()) {
				__Map.put(e.getKey(), jobjectSub.get(e.getKey()).getAsString());
				
			}
			
			listMap.add(cnt, __Map);
			
			cnt++;
			
		}
    	
    	return listMap;
	}
	
	/**
	 * 숫자인지 확인
	 * 
	 * @param item
	 * @return boolean
	 */
//	public boolean isNumeric(String item) {
//		try {
//			item = item.replaceAll("-", "").trim();
//			Double.parseDouble(item);
//			return true;
//		} catch (NumberFormatException e) {
//			return false;
//		}
//	}
	
	/**
	 *  휴대전화인지 확인
	 * 
	 * @param item
	 * @return boolean
	 */
	public boolean isPhoneNumeric(String item) {
		try {
			item = item.replaceAll("-", "").trim();
			Double.parseDouble(item);
			
			if(item.length() == 11 || item.length() == 10){
				return true;
			}else{
				return false;
			}
			
		} catch (NumberFormatException e) {
			return false;
		}
	}
	
	/**
	 * 전화번호 마스킹(*) 처리
	 * 
	 * @param telNo
	 * @return String
	 */
	public String maskTelNo(String telNo) {
		String rtnValue = "";
		
        int strLength = 0;
        String mask = "";
        boolean isNotFirstDash = false; //010-*******
        boolean isNotLastDash = false; //010***-****
        int startMaskPosition = 0;
        int endMaskPosition = 0;
        
        if (telNo == null || "".equals(telNo.trim())) {
        	rtnValue = "";

        } else {
        	strLength = telNo.length();
        	
        	String temp = telNo.replaceAll("\\D", "");
        	
        	if (temp.length()>=9) {
        		if(telNo.indexOf("02")==0){
        			rtnValue = telNo.substring(0, 2);
        		} else {
        			rtnValue = telNo.substring(0, 3);
        		}
        		
        		if(telNo.indexOf("02")==0){
        			isNotFirstDash = Pattern.matches("^[0-9]*$", telNo.substring(2, 3));
        		} else {
        			isNotFirstDash = Pattern.matches("^[0-9]*$", telNo.substring(3, 4));
        		}
        		isNotLastDash = Pattern.matches("^[0-9]*$", telNo.substring(strLength-5, strLength-4));
                
        		if (isNotFirstDash) {
        			if(telNo.indexOf("02")==0){
        				startMaskPosition = 2;
        			} else {
        				startMaskPosition = 3;
        			}
        		} else {
        			if(telNo.indexOf("02")==0){
        				startMaskPosition = 3;
        			} else {
        				startMaskPosition = 4;
        			}
        		}
        		if (isNotLastDash) {
        			endMaskPosition = strLength-4;
        		} else {
        			endMaskPosition = strLength-5;
        		}
        		for (int i=startMaskPosition; i<endMaskPosition; i++) {
        			mask += "*";
        		}
        		if (!isNotFirstDash) {
        			if(telNo.indexOf("02")==0){
        				rtnValue += telNo.substring(2, 3);
        			} else {
        				rtnValue += telNo.substring(3, 4);
        			}
        		}
        		rtnValue += mask;
        		if (!isNotLastDash) {
        			rtnValue += telNo.substring(strLength-5, strLength-4);
        		}
        		
        		rtnValue += telNo.substring(strLength-4, strLength);
        		
        	} else if (strLength > 3) {
        		rtnValue = telNo.substring(0, strLength-3) + "***";
        	} else {
        		rtnValue = telNo;
        	}
        }
        
		return rtnValue;
	}
	
	/**
	 * 고객명 마스킹(*) 처리
	 * 
	 * @param custName
	 * @return String
	 */
	public static String maskCustName(String custName) {
		String rtnValue = "";
		
        int strLength = 0;
        String mask = "";
        boolean isForeigner = false;
        
        if (custName == null || "".equals(custName.trim())) {
        	rtnValue = "";

        } else {
        	strLength = custName.length();
        	
        	if (strLength > 4) {
        		isForeigner = Pattern.matches("^[a-zA-Z]*$", custName.substring(0, 5));
        	}
        	
        	if (strLength == 2) {
        		rtnValue = custName.substring(0, 1) + "*";
        		
        	} else if (strLength >= 3) {
        		if (isForeigner) {
        			for (int i=0; i<strLength-4; i++) {
	        			mask += "*";
	        		}
        			
        		} else {
	        		for (int i=0; i<strLength-2; i++) {
	        			mask += "*";
	        		}
        		}
        		if (isForeigner) {
        			rtnValue = custName.substring(0, 4) + mask;
        			
        		} else {
        			rtnValue = custName.substring(0, 1) + mask + custName.substring(strLength-1, strLength);
        		}
        	}		
        }
        
		return rtnValue;
	}
	
	/**
	 * 전화번호 특수문자 추가 (예: 하이픈, 점)
	 * 
	 * @param src : 전화번호
	 * @param sp : 	특수문자
	 * @return String
	 */
	public static String phone(String src, String sp) {
		if (src == null) {
			return "";
		}
		if (src.length() == 8) {
			return src.replaceFirst("^([0-9]{4})([0-9]{4})$", "$1" + sp + "$2");
		} else if (src.length() == 12) {
			return src.replaceFirst("(^[0-9]{4})([0-9]{4})([0-9]{4})$", "$1" + sp + "$2" + sp + "$3");
		}
		return src.replaceFirst("(^02|[0-9]{3})([0-9]{3,4})([0-9]{4})$", "$1" + sp + "$2" + sp + "$3");
	}
	
	/**
	 * 날짜형태로 변환 (yyyy.mm.dd, YYYY-MM-DD)
	 * 
	 * @param src : 날짜(YYYYMMDD)
	 * @param sp : 	특수문자
	 * @return String
	 */
	public String dateFormat(String src, String sp) {
		LocalDateTime datetime = LocalDateTime.parse(src, DateTimeFormatter.ofPattern("yyyy" + sp + "MM" + sp + "dd"));
		return datetime.format(DateTimeFormatter.ofPattern("yyyy" + sp + "MM" + sp + "dd"));
	}
		
	/**
	 * @Method Name : rpad
	 * @Description : 문자열 오른쪽에 원하는 수만큼의 문자를 붙여서 리턴
	 * @param str : 문자열
	 * @param cnt : 길이
	 * @param str : 붙일 문자열
	 * @return String : 변환 데이터
	 */
	public String rpad(String str, int cnt, String padStr) {
		String tagStr = nvl(str);
		
		while (tagStr.length() < cnt) {
			tagStr += padStr;
		}
		
		return tagStr;
	}
	
	/**
	 * @Method Name : nvl
	 * @Description : List<Map<String, Object>> 체크 후 null 이면 ""을 return
	 * @param item : 데이터
     * @param num :  로우 번호
     * @param valItem :  Key
	 * @return String : 추출 데이터
	 */
    public static String nvl(List<Map<String, Object>> item, int num, String valItem) {
    	String rtnData = "";
		try {
			rtnData = item.get(num).get(valItem).toString();
		} catch (Exception e) {
			return valItem;
		}
		
		return rtnData;
	}
    
    
    /**
     * org.apache.commons.lang.StringUtils 상속후 필요 메소드 추가
     * 자세한 기타 자세한 스펙은 org.apache.commons.lang.StringUtils 참조
     * (url : http://jakarta.apache.org/commons/lang/api-release/org/apache/commons/lang/StringUtils.html)
     */
     
     public static String nullToZero(String value){
      ;
      if(value == null || value.equals("")){
       value   =   "0";
      }
      return value;
     }
     
     
     
     /**
      * 문자열 좌측의 공백을 제거하는 메소드
      * @param  str 대상 문자열
      * @return trimed string with white space removed from the front.
      */
     public static String ltrim(String str){
      int len = str.length();
      int idx = 0;

      while ((idx < len) && (str.charAt(idx) <= ' '))
      {
       idx++;
      }
      return str.substring(idx, len);
     }

     /**
      * 문자열 우측의 공백을 제거하는 메소드
      * @param  str 대상 문자열
      * @return trimed string with white space removed from the end.
      */
     public static String rtrim(String str){
      int len = str.length();

      while ((0 < len) && (str.charAt(len-1) <= ' '))
      {
       len--;
      }
      return str.substring(0, len);
     }


     /**
      * String을
      * @param str
      * @return
      */
     public static String changeMoney(String str) {
      DecimalFormat df = new DecimalFormat("###,###");

      return df.format(parseInt(str));
     }

     /**
      * 파라미터로 넘어오는 String을 , 를 제거해준다.
      *
      * @param s java.lang.String
      * @return java.lang.String
      */
     public static String removeComma(String str) {
      String rtnValue = str;
      if ( isNull(str) ) {
       return "";
      }

      rtnValue = replace(rtnValue, ",", "");
      return rtnValue;
     }

     /**
      * 숫자 0이 넘어오면 ""로 대치
      * @param  int 대상 숫자
      * @return java.lang.String
      */
     public static String isOneNull(int num){
      if (num == 0) return "";
      else return Integer.toString(num);
     }
     
     /**
      * str이 null 이거나 "", "    " 일경우 return true
      * @param str
      * @return
      */
     public static boolean isNull(String str) {

      return (str == null || (str.trim().length()) == 0 );
     }

     public static boolean isNull(Object obj) {
      String str = null;
      if( obj instanceof String ) {
       str = (String)obj;
      } else {
       return true;
      }

      return isNull(str);
     }

     
     /**
      * null이 아닐때.
      * @param str
      * @return
      */
     public static boolean isNotNull(String str) {
      /**
       * isNull이 true이면 false
       * false이면 true
       */
      if( isNull(str) ){
       return false;

      } else {
       return true;
      }
     }

     /***
      * 널체크
      * @param obj
      * @return
      */
     public static boolean isNotNull(Object obj) {
      String str = null;
      if( obj instanceof String ) {
       str = (String)obj;
      } else {
       return false;
      }

      return isNotNull(str);
     }

     /**
      * 파라미터가 null 이거나 공백이 있을경우
      * "" 로 return
      * @param value
      * @return
      */
     public static String replaceNull(String value) {
      return replaceNull(value, "");
     }

     /**
      * Object를 받아서 String 형이 아니거나 NULL이면 ""를 return
      * String 형이면 형 변환해서 넘겨준다.
      * @param value
      * @return
      */
     public static String replaceNull(Object value) {
      Object rtnValue = value;
      if( rtnValue == null || !"java.lang.String".equals(rtnValue.getClass().getName())) {
       rtnValue = "";
      }

      return replaceNull((String)rtnValue, "");
     }

     /**
      * 파라미터로 넘어온 값이 null 이거나 공백이 포함된 문자라면
      * defaultValue를 return
      * 아니면 값을 trim해서 넘겨준다.
      * @param value
      * @param repStr
      * @return
      */
     public static String replaceNull(String value, String defaultValue) {
      if (isNull(value)) {
       return defaultValue;
      }

      return value.trim();
     }

     /**
      * Object를 받아서 String 형이 아니거나 NULL이면 defaultValue를 return
      * String 형이면 형 변환해서 넘겨준다.
      * @param value
      * @param repStr
      * @return
      */
     public static String replaceNull(Object value, String defaultValue) {
      String valueStr = replaceNull(value);
      if ( isNull(valueStr) ) {
       return defaultValue;
      }

      return valueStr.trim();
     }

     /**
      * Method ksc2asc.
      * 8859-1를 euc-kr로 인코딩하는 함수
      * @param str - String
      * @return String
      */
     public static String ksc2asc(String str) {
      String result = "";

      if (isNull(str)) {
       result = "";
      } else {
       try {
        result = new String( str.getBytes("euc-kr"), "8859_1" );
       } catch( Exception e ) {
        result = "";
       }
      }

      return result;
     }

     /**
      * Method asc2ksc.
      * euc-kr을 8859-1로 인코딩하는 함수
      * @param str - String
      * @return String
      */
     public static String asc2ksc(String str) {
      String result = "";

      if (isNull(str)) {
       result = "";
      } else {
       try {
        result = new String( str.getBytes("8859_1"), "euc-kr" );
       } catch( Exception e ) {
        result = "";
       }
      }

      return result;
     }

     /**************************************************************************************/
     /* parse method start */


     /**
      * String을 int형으로
      * @param value
      * @return
      */
     public static int parseInt(String value) {
      return parseInt(value, 0);
     }
     /**
      * Object를 int형으로
      * defaultValue는 0이다.
      * @param value
      * @return
      */
     public static int parseInt(Object value) {
      String valueStr = replaceNull(value);
      return parseInt(valueStr, 0);
     }
     /**
      * Object를 int형으로
      * Object가 null이면 defaultValue return
      * @param value
      * @param defaultValue
      * @return
      */
     public static int parseInt(Object value, int defaultValue) {
      String valueStr = replaceNull(value);
      return parseInt(valueStr, defaultValue);
     }
     /**
      * String을 int형으로
      * String이 숫자 형식이 아니면 defaultValue return
      * @param value
      * @param defaultValue
      * @return
      */
     public static int parseInt(String value, int defaultValue) {
      int returnValue = 0;

      if( isNull(value) ) {
       returnValue = defaultValue;
      } else if( !isNumeric(value) ) {
       returnValue = defaultValue;
      } else {
       returnValue = Integer.parseInt(value);
      }

      return returnValue;
     }

     /**
      * String을 long형으로
      * defaultValue는 0이다.
      * @param value
      * @return
      */
     public static long parseLong(String value) {
      return parseLong(value, 0);
     }

     /**
      * String을 long형으로
      * 잘못된 데이타 일시 return은 defaultValue
      * @param value
      * @return
      */
     public static long parseLong(String value, long defaultValue) {
      long returnValue = 0;

      if( isNull(value) ) {
       returnValue = defaultValue;
      } else if( !isNumeric(value) ) {
       returnValue = defaultValue;
      } else {
       returnValue = Long.parseLong(value);
      }

      return returnValue;
     }

     /**
      * Exception을 String으로 뽑아준다.
      * @param ex
      * @return
      */
     public static String stackTraceToString(Throwable e) {
      try {
       StringWriter sw = new StringWriter();
       PrintWriter pw = new PrintWriter(sw);
       e.printStackTrace(pw);
       return "------\r\n" + sw.toString() + "------\r\n";
       }catch(Exception e2) {
        return StringUtil.stackTraceToString2(e);
       }
     }
     /**
      * Exception을 String으로 뽑아준다.
      * @param ex
      * @return
      */
     public static String stackTraceToString2(Throwable e) {
      ByteArrayOutputStream b = new ByteArrayOutputStream();
      PrintStream p = new PrintStream(b);
      e.printStackTrace(p);
      p.close();
      String stackTrace = b.toString();
      try {
       b.close();
      } catch (IOException ex) {
       ex.printStackTrace();
      }

    //  return convertHtmlBr(stackTrace);
      return stackTrace;
     }

     /**
      * Html 코드에서 &#60;br&#62; 태크 제거
      * @param comment
      * @return
      */
     public static String convertHtmlBr(String comment) {
      String rtnValue = "";
      if( isNull(comment) ) {
       return "";
      }

      rtnValue = replace(comment, "\r\n", "<br>");

      return rtnValue;
     }


     /**
      * String 배열을 List로 변환한다.
      * @param values
      * @return
      */
     public static List changeList(String [] values) {
      List list = new ArrayList();

      if( values == null ) {
       return list;
      }
      for(int i=0,n=values.length; i<n; i++) {
       list.add(values[i]);
      }

      return list;
     }


     public static String[] toTokenArray(String str, String sep){

      String[] temp = null;

      try{
       StringTokenizer st = new StringTokenizer(str, sep);
       temp = new String[st.countTokens()];
       int index = 0;
       while(st.hasMoreTokens()){
        temp[index++] = st.nextToken();
       }
      }catch(Exception e){
       e.printStackTrace();
      }

      return temp;
     }
        public static String strip(String str, String str1){

         if(str == null || "".equals(str.trim())) return "";

         String temp = str;
      int pos = -1;
      while((pos = temp.indexOf(str1, pos)) != -1) {
       String left = temp.substring(0, pos);
       String right = temp.substring(pos + 1, temp.length());
       temp = left + "" + right;
       pos += 1;
      }
      return temp;
        }

        /**
      * Method ksc2asc.
      * euc-kr을 euc-kr로 인코딩하는 함수
      * @param str - String
      * @return String
      */
     public static String ksc2utf8(String str) {
      String result = "";

      if (isNull(str)) {
       result = "";
      } else {
       try {
        result = new String( str.getBytes("euc-kr"), "utf-8" );
       } catch( Exception e ) {
        result = "";
       }
      }

      return result;
     }

     /**
      * string에 있는 ', ", \r\n 를 HTML 코드로 변환한다.
      * @param str
      * @return
      */
     public static String changeQuotation(String str) {
      String rtnValue = str;
      rtnValue = replaceNull(rtnValue);
      rtnValue = replace(replace(replace(rtnValue, "'", "&#39;"), "\"", "&#34;"), "\r\n", "<br>");

      return rtnValue;
     }
     public static String changeQuotation(Object obj) {
      if( isStringInteger(obj) ) {
       return changeQuotation(String.valueOf(obj));
      }

      return "";
     }

     /**
      * 해당 Object가 String or Integer 이면 true
      * 아니면 false
      * @param obj
      * @return
      */
     public static boolean isStringInteger(Object obj) {

      boolean flag = false;

      if( obj instanceof String || obj instanceof Integer ) {
       flag = true;
      }

      return flag;
     }

     /**
      * 백분율을 구한다.
      * %는 빼고 값만 리턴
      * @param value
      * @param total
      * @return
      */
     public static String percentValue(int value, int total) {
      double val = Double.parseDouble(String.valueOf(value)) / Double.parseDouble(String.valueOf(total)) * 100;

      DecimalFormat df = new DecimalFormat("##0.0");
      return df.format(val);
     }

     


     /**
      *  XSS(Cross Site Scripting) 취약점 해결을 위한 처리
      *
      * @param sourceString String 원본문자열
      * @return String 변환문자열
      */
     public static String replaceXSS(String sourceString){
      String rtnValue = null;
      if(sourceString!=null){
       rtnValue = sourceString;
       if(rtnValue.indexOf("<x-") == -1){
        rtnValue = rtnValue.replaceAll("< *(j|J)(a|A)(v|V)(a|A)(s|S)(c|C)(r|R)(i|I)(p|P)(t|T)", "<x-javascript");
        rtnValue = rtnValue.replaceAll("< *(v|V)(b|B)(s|S)(c|C)(r|R)(i|I)(p|P)(t|T)", "<x-vbscript");
        rtnValue = rtnValue.replaceAll("< *(s|S)(c|C)(r|R)(i|I)(p|P)(t|T)", "<x-script");
        rtnValue = rtnValue.replaceAll("< *(i|I)(f|F)(r|R)(a|A)(m|M)(e|E)", "<x-iframe");
        rtnValue = rtnValue.replaceAll("< *(f|F)(r|R)(a|A)(m|M)(e|E)", "<x-frame");
        rtnValue = rtnValue.replaceAll("(e|E)(x|X)(p|P)(r|R)(e|E)(s|S)(s|S)(i|I)(o|O)(n|N)", "x-expression");
        rtnValue = rtnValue.replaceAll("(a|A)(l|L)(e|E)(r|R)(t|T)", "x-alert");
        rtnValue = rtnValue.replaceAll(".(o|O)(p|P)(e|E)(n|N)", ".x-open");
        rtnValue = rtnValue.replaceAll("< *(m|M)(a|A)(r|R)(q|Q)(u|U)(e|E)(e|E)", "<x-marquee");
        rtnValue = rtnValue.replaceAll("&#", "&amp;#");
       }
      }

      return rtnValue;
        }


     /**
      * 특정문자를 HTML TAG형식으로 변경하는 메소드.
      *
      * <xmp>
      * & --> &amp;
      * < --> &lt;
      * > --> &gt;
      * " --> &quot;
      * ' --> &#039;
      *-----------------------------------------------------------------
      * <option type=radio  name=r value="xxxxxxxx"> yyyyyyy
      * <input  type=hidden name=h value="xxxxxxxx">
      * <input  type=text   name=t value="xxxxxxxx">
      * <textarea name=msg rows=20 cols=53>xxxxxxx</textarea>
      *-
      * 위와 같은 HTML 소스를 생성할 때, xxxxxxx 부분의 문자열 중에서
      * 아래에 있는 몇가지 특별한 문자들을 변환하여야 합니다.
      * 만약 JSP 라면 미리 변환하여 HTML 전체 TAG를 만들거나, 혹은 아래처럼 사용하세요.
      *-
      * <option type=radio  name=r value="<%= StringUtil.translate(s) %>"> yyyyyyy
      * <input  type=hidden name=n value="<%= StringUtil.translate(s) %>">
      * <input  type=text   name=n value="<%= StringUtil.translate(s) %>">
      * <textarea name=body rows=20 cols=53><%= StringUtil.translate(s) %></textarea>
      *-
      * 또 필요하다면 yyyyyyy 부분도  translate(s)를 할 필요가 있을 겁니다.
      * 필요할 때 마다 사용하세요.
      *-
      * </xmp>
      *
      * @return the translated string.
      * @param str java.lang.String
      */
     public static String translate(String str){
      if ( str == null ) return null;

      StringBuffer buf = new StringBuffer();
      char[] c = str.toCharArray();
      int len = c.length;

      for ( int i=0; i < len; i++){
       if      ( c[i] == '&' ) buf.append("&amp;");
       else if ( c[i] == '<' ) buf.append("&lt;");
       else if ( c[i] == '>' ) buf.append("&gt;");
       else if ( c[i] == '"' ) buf.append("&quot;"); // (char)34
       else if ( c[i] == '\'') buf.append("&#039;"); // (char)39
       else buf.append(c[i]);
      }
      return buf.toString();
     }

       /**
        * String 앞 또는 뒤를 특정문자로 지정한 길이만큼 채워주는 함수    <BR>
        * (예) pad("1234","0", 6, 1) --> "123400"   <BR>
        *
        * @param    src  Source string
        * @param    pad  pad string
        * @param    totLen     total length
        * @param    mode     앞/뒤 구분 (-1:front, 1:back)
        * @return   String
        */
     public static String pad(String src, String pad, int totLen, int mode){
      String paddedString = "";

      if(src == null) return "";
      int srcLen = src.length();

      if((totLen<1)||(srcLen>=totLen)) return src;

      for(int i=0; i< (totLen-srcLen); i++){
       paddedString += pad;
      }

      if(mode == -1)
       paddedString += src; // front padding
      else
           paddedString = src + paddedString; //back padding

      return paddedString;
     }

     /**
      * 주어진 길이(iLength)만큼 주어진 문자(cPadder)를 strSource의 왼쪽에 붙혀서 보내준다.
      * ex) lpad("abc", 5, '^') ==> "^^abc"
      *     lpad("abcdefghi", 5, '^') ==> "abcde"
      *     lpad(null, 5, '^') ==> "^^^^^"
      *
      * @param strSource
      * @param iLength
      * @param cPadder
      */
     public static String lpad(String strSource, int iLength, char cPadder){
      StringBuffer sbBuffer = null;

      if (!isEmpty(strSource)){
       int iByteSize = getByteSize(strSource);
       if (iByteSize > iLength){
        return strSource.substring(0, iLength);
       }else if (iByteSize == iLength){
        return strSource;
       }else{
        int iPadLength = iLength - iByteSize;
        sbBuffer = new StringBuffer();
        for (int j = 0; j < iPadLength; j++){
         sbBuffer.append(cPadder);
        }
        sbBuffer.append(strSource);
        return sbBuffer.toString();
       }
      }

      //int iPadLength = iLength;
      sbBuffer = new StringBuffer();
      for (int j = 0; j < iLength; j++){
       sbBuffer.append(cPadder);
      }
      return sbBuffer.toString();
     }

     /**
      * 주어진 길이(iLength)만큼 주어진 문자(cPadder)를 strSource의 오른쪽에 붙혀서 보내준다.
      * ex) lpad("abc", 5, '^') ==> "abc^^"
      *     lpad("abcdefghi", 5, '^') ==> "abcde"
      *     lpad(null, 5, '^') ==> "^^^^^"
      *
      * @param strSource
      * @param iLength
      * @param cPadder
      */
     public static String rpad(String strSource, int iLength, char cPadder){
      StringBuffer sbBuffer = null;
       if (!isEmpty(strSource)){
       int iByteSize = getByteSize(strSource);
       if (iByteSize > iLength){
        return strSource.substring(0, iLength);
       }else if (iByteSize == iLength){
        return strSource;
       }else{
        int iPadLength = iLength - iByteSize;
        sbBuffer = new StringBuffer(strSource);
        for (int j = 0; j < iPadLength; j++){
         sbBuffer.append(cPadder);
        }
        return sbBuffer.toString();
       }
      }
      sbBuffer = new StringBuffer();
      for (int j = 0; j < iLength; j++){
       sbBuffer.append(cPadder);
      }
      return sbBuffer.toString();
     }

     /**
      *  byte size를 가져온다.
      *
      * @param str String target
      * @return int bytelength
      */
     public static int getByteSize(String str){
      if (str == null || str.length() == 0)
       return 0;
      byte[] byteArray = null;
       try{
       byteArray = str.getBytes("UTF-8");
      }catch (UnsupportedEncodingException ex){}
      if (byteArray == null) return 0;
      return byteArray.length;
     }
          /**
         * 긴 문자열 자르기
         * @param srcString      대상문자열
         * @param nLength        길이
         * @param isNoTag        테그 제거 여부
         * @param isAddDot        "..."을추가 여부
         * @return
         */
        public static String strCut(String srcString, int nLength, boolean isNoTag, boolean isAddDot){  // 문자열 자르기
            String rtnVal = srcString;
            int oF = 0, oL = 0, rF = 0, rL = 0;
            int nLengthPrev = 0;

            // 태그 제거
            if(isNoTag) {
                Pattern p = Pattern.compile("<(/?)([^<>]*)?>", Pattern.CASE_INSENSITIVE);  // 태그제거 패턴
                rtnVal = p.matcher(rtnVal).replaceAll("");
            }
            rtnVal = rtnVal.replaceAll("&amp;", "&");
            rtnVal = rtnVal.replaceAll("(!/|\r|\n|&nbsp;)", "");  // 공백제거
            try {
             byte[] bytes = rtnVal.getBytes("UTF-8");     // 바이트로 보관
             // x부터 y길이만큼 잘라낸다. 한글안깨지게.
             int j = 0;
             if(nLengthPrev > 0) while(j < bytes.length) {
              if((bytes[j] & 0x80) != 0) {
               oF+=2; rF+=3; if(oF+2 > nLengthPrev) {break;} j+=3;
                 } else {if(oF+1 > nLengthPrev) {break;} ++oF; ++rF; ++j;}
             }

             j = rF;

             while(j < bytes.length) {
              if((bytes[j] & 0x80) != 0) {
               if(oL+2 > nLength) {break;} oL+=2; rL+=3; j+=3;
                 } else {if(oL+1 > nLength) {break;} ++oL; ++rL; ++j;}
             }

             rtnVal = new String(bytes, rF, rL, "UTF-8");  // charset 옵션

             if(isAddDot && rF+rL+3 <= bytes.length) {rtnVal+="...";}  // ...을 붙일지말지 옵션
            } catch(UnsupportedEncodingException e){
             e.printStackTrace();
             return srcString;
            }

         return rtnVal;
        }
       
        /**
         * total과 success 로 % 구하고 소수점 1자리까지 계산
         * @param int success     
         * @param int total      

         * @return String %
         */
        public static String calculatePercent(int success,int total){
         String result   =  "0";
         
         if(total == 0){
           
          
         }else{
          
            Double tempSuccess  = new Double(success+".0");
            Double tempTotal    = new Double(total+".0");
            Double tempPercent   = new Double(100+".0");
          
            double cal =  tempSuccess.doubleValue()*tempPercent.doubleValue()/tempTotal.doubleValue();
          
          result = new java.text.DecimalFormat("#.#").format(cal);
          
         }
         
         return result;
        }

	
}
