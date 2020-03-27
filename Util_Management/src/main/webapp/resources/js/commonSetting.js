$(function(){
	// ajax 설정
	$.ajaxSetup({
        cache : false
       ,beforeSend: function(xhr){
    	   
    	   // 헤더에 값 붙여서 전송
           // xhr.setRequestHeader("isAjax", true);
           
    	   // 인디케이터 추가
           // fnLoading(true);
       }
       ,complete: function(xhr, statusText){
    	   
    	   // 인디케이터 삭제
           //fnLoading(false);
       }
       ,error: function(xhr, statusText, error){
    	   // 애러 이벤트 설정
       }
   });
	
	// datepicker 설정
	$.datepicker.setDefaults($.datepicker.regional["ko"]);
    $.MonthPicker.i18n.months = ["1월", "2월", "3월", "4월", "5월", "6월", "7월", "8월", "9월", "10월", "11월", "12월"];
    $.MonthPicker.i18n.year = ["년"];
    $.MonthPicker.i18n.backTo = ["년으로 돌아가기"];
});