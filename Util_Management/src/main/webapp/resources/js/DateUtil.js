
var DateUtil = function(date){
	// 파라미터 갯수 확인
	if(0 === arguments.length){ // 파라미터 없음
		// 현재 날짜 사용
		date = new Date();
	}
	
	function cloneDate(){
		return new Date(date.valueOf());
	}
	
	// 현재 날짜
	this.today = function(){
		return new Date();
	}
	
	// 지정 날짜 리턴
	this.getDate = function(){
		return cloneDate();
	}
	
	// 지정날짜에서 일자를 nDay 만큼 더한 날짜 반환
	this.addDay = function(nDay){
		var date = cloneDate();
		date.setDate(date.getDate() + nDay);
		return date;
	}
	
	// 지정날짜에서 월을 nMonth 만큼 더한 날짜 반환
	this.addMonth = function(nMonth){
		var date = cloneDate();
		date.setMonth(date.getMonth() + nMonth);
		return date;
	}
	
	// 지정날짜에서 년도를 nYear 만큼 더한 날짜 반환
	this.addYear = function(nYear){
		var date = cloneDate();
		date.setYear(date.getYear() + nYear);
		return date;
	}
	
	// 지정날짜에서 nMonth만큼 더한 월의 1일
	this.month_first = function(nMonth){
		var date = cloneDate();
		return new Date(date.getYear(), date.getMonth(), 1);
	}
	
	// 지정날짜에서 nMonth만큼 더한 월의 마지막 일
	this.month_last = function(nMonth){
		var date = cloneDate();
		return new Date(date.getYear(), date.getMonth() + 1, 0);
	}
	
	/**
	 * date 간의 날짜 차이 계산
	 * diffDate2 매개 변수 없을 시 현재 날짜와 diffDate1와 차이 계산
	 * */
	this.diffDay = function(diffDate1, diffDate2){
		// diffDate2 매개변수 없을 시
		if(diffDate2 == undefined || diffDate2 == null){
			var date = new Date();
			if(date > diffDate1){
				return date.getDate() - diffDate1.getDate();
			}else{
				return diffDate1.getDate() - date.getDate();
			}
		}else{
			if(diffDate1 > diffDate2){
				return diffDate2.getDate() - diffDate1.getDate();
			}else{
				return diffDate1.getDate() - diffDate2.getDate();
			}
		}
	}
	
	/**
	 * string형 날짜를 Date 형으로 변경
	 * @param str : 문자열 날짜 (yyyyMMdd, yyyy-MM-dd, yyyy.MM.dd 등...)
	 * */
	this.stringToDate = function(str){
		if(str.length == 8){
			var year = str.substring(0,4);
			var month = str.substring(4,6);
			var day = str.substring(6);
			return new Date(year + "-" + month + "-" + day);
		}else if(str.length == 10){
			return new Date(str);
		}else{
			return false;
		}
	}
	
	/**
	 * Date형을 string형으로 변경
	 * @param date : date형 날짜
	 * */
	this.dateToString = function(date, sp){
		var year = date.getFullYear();
		var month = date.getMonth() + 1;
		month = month >= 10 ? month : "0" + month;
		var day = date.getDate();
		day = day >= 10 ? day : "0" + day;
		if(sp != undefined && sp != null){
			return year + sp + month + sp + day;
		}else{
			return year + month + day;
		}
	}
	
}