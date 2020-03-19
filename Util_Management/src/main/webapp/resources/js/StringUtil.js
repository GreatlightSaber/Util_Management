
var StringUtil = function(){
	
	this.calByte = {
		/**
		 * 바이트 길이 계산
		 * @param s		: 바이트 길이 계산 할 문자열 
		 * */
		getByteLength : function(s) {

			if (s == null || s.length == 0) {
				return 0;
			}
			var size = 0;

			for ( var i = 0; i < s.length; i++) {
				size += this.charByteSize(s.charAt(i));
			}

			return size;
		},
		/**
		 * 일정 바이트만큼 자르기
		 * @param s		: 자를 문자열
		 * @param len	: 자를 바이트 크기
		 * */
		cutByteLength : function(s, len) {

			if (s == null || s.length == 0) {
				return 0;
			}
			var size = 0;
			var rIndex = s.length;

			for ( var i = 0; i < s.length; i++) {
				size += this.charByteSize(s.charAt(i));
				if( size == len ) {
					rIndex = i + 1;
					break;
				} else if( size > len ) {
					rIndex = i;
					break;
				}
			}

			return s.substring(0, rIndex);
		},
		/**
		 * 해당 문자 바이트 크기 가져오기
		 * @param ch	: 바이트 크기 계산할 문자
		 * */
		charByteSize : function(ch) {

			if (ch == null || ch.length == 0) {
				return 0;
			}

			var charCode = ch.charCodeAt(0);

			if (charCode <= 0x00007F) {
				return 1;
			} else if (charCode <= 0x0007FF) {
				return 2;
			} else if (charCode <= 0x00FFFF) {
				return 3;
			} else {
				return 4;
			}
		}
	};

}