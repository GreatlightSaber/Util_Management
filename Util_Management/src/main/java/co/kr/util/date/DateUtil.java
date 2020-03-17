package co.kr.util.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Component;

/**
 * @Class Name : DateUtil.java
 * @Author : kth
 * @Description : 날짜 계산 모듈
 *
 * @Copyright (c) 조폐공사 지류상품권 관리시스템 Uracle All right reserved.
 *------------------------------------------------------------------------
 * Modification Information
 *------------------------------------------------------------------------   
 * 수정일 / 수정자 / 수정내용
 * 2019.08.27 / kth / 최초 개발
 *------------------------------------------------------------------------  
 */
@Component
public class DateUtil {
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	/**
     * 현 시각 날짜 리턴
     * @param dateFrmt 받을 날짜 포멧 ""(빈값)일 경우 yyyyMMdd HH:mm:ss으로 출력
     * @return String 날짜
     */
	public String nowDate(String dateFrmt){
		
		String rtrnDate = "";
		
		if(dateFrmt.equals("")) dateFrmt = "yyyyMMdd HH:mm:ss";
		
		java.util.Date nowDate = new java.util.Date();    	
    	SimpleDateFormat df = new SimpleDateFormat(dateFrmt);
    	
    	rtrnDate = df.format(nowDate);
    	
    	return rtrnDate;
    	
	}
	
	/**
     * item(String)을 date 형식으로 변환
     * @param item String 형식의 날짜 (ex: 20190518 135945)
     * @param dateFrmt item의 날짜 포멧 (ex: yyyyMMdd HHmmss)
     * @param strnFrmt 출력 할 날짜 포멧 (ex: yyyy-MM-dd HH:mm:ss)
     * @return String 날짜
	 * @throws ParseException 
     */
	public String toDate(String item, String dateFrmt, String strnFrmt) {
		/*
		StringUtil util = new StringUtil();
		
		String msg = "";
		
		item 		= util.nvl(item);
		dateFrmt 	= util.nvl(dateFrmt);
		strnFrmt 	= util.nvl(strnFrmt);
		
		if(item.equals("")) msg = "item ";
		if(dateFrmt.equals("")) msg += "dateFrmt ";		
		if(strnFrmt.equals("")) msg += "strnFrmt ";
		if(!msg.equals("")){
			msg += "에 대한 값을 확인하세요";			
			return msg;
		}
		*/
		java.text.SimpleDateFormat format = new java.text.SimpleDateFormat(dateFrmt);
		java.text.SimpleDateFormat rsltFormat = new java.text.SimpleDateFormat(strnFrmt);
		
		java.util.Date date = new Date();
		
		try {
			date = format.parse(item);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return rsltFormat.format(date);

	}
	
	/**
	 * 오늘 날짜 기준으로 날짜를 계산 (하루 전, 일주일 전, 일주일 후, 하루 후 등등) 
	 * @param optn1 1: 년 계산, 2: 월 계산, 3: 주 계산, 5: 일 계산
	 * @param optn2 optn1 기준 +- 계산
	 * @param optn2 sample: optn1: 1, optn2: -1 (1년전)
	 * @param optn2 sample: optn1: 3, optn2: 1  (1주후)
	 * @param optn2 sample: optn1: 5, optn2: -2 (이틀전)
	 * */
	public String calDate(int optn1, int optn2){
		
		Calendar cal = Calendar.getInstance(Locale.KOREAN);
		
		TimeZone timezone = cal.getTimeZone();
		timezone = timezone.getTimeZone("Asia/Seoul");

		cal = Calendar.getInstance(timezone);
		cal.add(optn1, optn2);

		Date currentTime = cal.getTime();

		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd", Locale.KOREAN);

		return formatter.format(currentTime);

	}
	
	/**
	 * 입력한 날짜 기준으로 날짜를 계산 (하루 전, 일주일 전, 일주일 후, 하루 후 등등) 
	 * @param optn1 1: 년 계산, 2: 월 계산, 3: 주 계산, 5: 일 계산
	 * @param optn2 optn1 기준 +- 계산
	 * @param optn2 sample: optn1: 1, optn2: -1 (1년전)
	 * @param optn2 sample: optn1: 3, optn2: 1  (1주후)
	 * @param optn2 sample: optn1: 5, optn2: -2 (이틀전)
	 * @param item String yyyyMMdd 형식의 날짜 (ex: 20190518)
	 * */
	public String calToDate(int optn1, int optn2, String item) throws ParseException{
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.KOREAN);
		Date date = sdf.parse(item);// all done
		
		Calendar cal = sdf.getCalendar();		
		cal.add(optn1, optn2);
		
		Date currentTime = cal.getTime();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd", Locale.KOREAN);
		
		return formatter.format(currentTime);
		
	}
	
	
	public static String getTodayTimeString() {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
		return format.format(new Date());
	}

}
