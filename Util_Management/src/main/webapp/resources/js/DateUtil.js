
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
}