package co.kr.util.string;

import java.io.File;
import java.io.FileInputStream;
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
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

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
public class StringUtil {
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
	public boolean isNumeric(String item) {
		try {
			item = item.replaceAll("-", "").trim();
			Double.parseDouble(item);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	
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
	
}
