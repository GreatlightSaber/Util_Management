/******************************
 * StringUtil.js
 * @description : 문자열 관련 함수 js
 * @author : KEI
 * @version : 1.0
 * @since : 
 ******************************/
// 출처: https://javafactory.tistory.com/1026 [FreeLife의 저장소]

/**
 * 정규식에 쓰이는 특수문자를 찾아서 escape 한다.
 * 
 * @return 특수문자가 escape된 문자열
 */
String.prototype.meta = function() {
	var str = this;
	var result = '';
	for (var i = 0; i < str.length; i++) {
		if ((/([\$\(\)\*\+\.\[\]\?\\\^\{\}\|]{1})/).test(str.charAt(i))) {
			result += str.charAt(i).replace(
					(/([\$\(\)\*\+\.\[\]\?\\\^\{\}\|]{1})/), "\\$1");
		} else {
			result += str.charAt(i);

		}
	}
	return result;
}

/**
 * 정규식에 쓰이는 특수문자를 찾아서 escape하여 반환한다.
 * 
 * @return 특수문자가 escape된 문자열
 */
String.prototype.remove = function(pattern) {
	return (pattern == null) ? this : eval("this.replace(/[" + pattern.meta()
			+ "]/g, \"\")");
}

/**
 * 문자열이 대상 문자열과 동일한 값인지의 여부를 반환한다.
 * 
 * @param tagetStr -
 *            비교대상 문자열
 * @return 동일값 여부
 */
String.prototype.eq = function(tagetStr) {
	return (tagetStr != null && typeof (tagetStr) != 'undefined' && this == tagetStr);
}

/**
 * 문자열이 대상 문자열과 다른 값인지의 여부를 반환한다.
 * 
 * @param tagetStr -
 *            비교대상 문자열
 * @return 다른값 여부
 */
String.prototype.neq = function(tagetStr) {
	return !this.eq(tagetStr);
}

/**
 * 문자열이 숫자형인지의 여부를 반환한다.
 * 
 * @param exceptChar -
 *            추가 허용할 문자
 * @return 숫자형여부
 */
String.prototype.isNum = function(exceptChar) {
	return (/^[0-9]+$/).test(this.remove(exceptChar)) ? true : false;
}

/**
 * 문자열을 숫자형으로 캐스팅한다.
 * 
 * @return 캐스팅된 숫자
 */
String.prototype.toNum = function() {
	if (this.isNum()) {
		return Number(this);
	} else {
		return -1;
	}
}

/**
 * 문자열을 3자리마다 콤마를 찍어서 반환한다.
 * 
 * @return 콤마가 첨가된 문자열
 */
String.prototype.money = function() {
	var num = this.replace(/,/g, "");
	while ((/(-?[0-9]+)([0-9]{3})/).test(num)) {
		num = num.replace((/(-?[0-9]+)([0-9]{3})/), "$1,$2");
	}
	return num;
}

/**
 * 문자열의 byte 길이를 반환한다.
 * 
 * @return 문자열의 byte 길이
 */
String.prototype.getByte = function() {
	var cnt = 0;
	for (var i = 0; i < this.length; i++) {
		if (this.charCodeAt(i) > 127) {
			cnt += 2;
		} else {
			cnt++;
		}
	}
	return cnt;
}

/**
 * 문자열이 지정한 최소길이 이상인지의 여부를 반환한다.
 * 
 * @param minLen -
 *            최소길이
 * @return 최소길이 이상인지의 여부
 */
String.prototype.isMin = function(minLen) {
	return this.length >= minLen;
}

/**
 * 문자열이 지정한 최대길이 이하인지의 여부를 반환한다.
 * 
 * @param maxLen -
 *            최대길이
 * @return 최대길이 이하인지의 여부
 */
String.prototype.isMax = function(maxLen) {
	return this.length <= maxLen;
}

/**
 * 문자열이 지정한 최소바이트수 이상인지의 여부를 반환한다.
 * 
 * @param minByte -
 *            최소바이트수
 * @return 최소바이트수 이상인지의 여부
 */
String.prototype.isMinByte = function(minByte) {
	return this.getByte() >= minByte;
}

/**
 * 문자열이 지정한 최대바이트수 이하인지의 여부를 반환한다.
 * 
 * @param maxByte -
 *            최대바이트수
 * @return 최대바이트수 이하인지의 여부
 */
String.prototype.isMaxByte = function(maxByte) {
	return this.getByte() <= maxByte;
}

/**
 * 문자열이 영문인경우 대문자로 치환한다.
 * 
 * @return 대문자로 치환된 문자열
 */
String.prototype.upper = function() {
	return this.toUpperCase();
}

/**
 * 문자열이 영문인경우 소문자로 치환한다.
 * 
 * @return 소문자로 치환된 문자열
 */
String.prototype.lower = function() {
	return this.toLowerCase();
}

/**
 * 문자열 좌우 공백을 제거한다.
 * 
 * @return 좌우 공백 제거된 문자열
 */
String.prototype.trim = function() {
	return this.replace(/^\s+/g, '').replace(/\s+$/g, '');
}

/**
 * 문자열 좌 공백을 제거한다.
 * 
 * @return 좌 공백 제거된 문자열
 */
String.prototype.ltrim = function() {
	return this.replace(/(^\s*)/, "");
}

/**
 * 문자열 우 공백을 제거한다.
 * 
 * @return 우 공백 제거된 문자열
 */
String.prototype.rtrim = function() {
	return this.replace(/(\s*$)/, "");
}

/**
 * 문자열에서 모든 교체할 문자열을 대체 문자열로 치환한다.
 * 
 * @param pattnStr -
 *            찾을 문자열
 * @param chngStr -
 *            대체 문자열
 * @return 치환된 문자열
 */
String.prototype.replaceAll = function(pattnStr, chngStr) {
	var retsult = "";
	var trimStr = this.replace(/(^\s*)|(\s*$)/g, "");

	if (trimStr && pattnStr != chngStr) {
		retsult = trimStr;
		while (retsult.indexOf(pattnStr) > -1) {
			retsult = retsult.replace(pattnStr, chngStr);
		}
	}
	return retsult;
}

/**
 * 문자열을 거꾸로 치환한다.
 * 
 * @return 거꾸로 치환된 문자열
 */
String.prototype.reverse = function() {
	var result = '';

	for (var i = this.length - 1; i > -1; i--) {
		result += this.substring(i, i + 1);
	}
	return result;
}

/**
 * 지정한 길이만큼 원본 문자열 왼쪽에 패딩문자열을 채운다.
 * 
 * @param len -
 *            채울 길이
 * @param padStr -
 *            채울 문자열
 * @return 채워진 문자열
 */
String.prototype.lpad = function(len, padStr) {
	var result = '';
	var loop = Number(len) - this.length;
	for (var i = 0; i < loop; i++) {
		result += padStr.toString();
	}
	return result + this;
}

/**
 * 지정한 길이만큼 원본 문자열 오른쪽에 패딩문자열을 채운다.
 * 
 * @param len -
 *            채울 길이
 * @param padStr -
 *            채울 문자열
 * @return 채워진 문자열
 */
String.prototype.rpad = function(len, padStr) {

	var result = '';
	var loop = Number(len) - this.length;
	for (var i = 0; i < loop; i++) {
		result += padStr.toString();
	}
	return this + result;
}

/**
 * 문자열이 공백이나 널인지의 여부를 반환한다.
 * 
 * @return 공백이나 널인지의 여부
 */
String.prototype.isBlank = function() {
	var str = this.trim();
	for (var i = 0; i < str.length; i++) {
		if ((str.charAt(i) != "\t") && (str.charAt(i) != "\n")
				&& (str.charAt(i) != "\r")) {
			return false;
		}
	}
	return true;
}

/**
 * 문자열이 영어만으로 구성되어 있는지의 여부를 반환한다.
 * 
 * @param exceptChar -
 *            추가 허용할 문자
 * @return 영어만으로 구성되어 있는지의 여부
 */
String.prototype.isEng = function(exceptChar) {
	return (/^[a-zA-Z]+$/).test(this.remove(exceptChar)) ? true : false;
}

/**
 * 문자열이 숫자와 영어만으로 구성되어 있는지의 여부를 반환한다.
 * 
 * @param exceptChar -
 *            추가 허용할 문자
 * @return 숫자와 영어만으로 구성되어 있는지의 여부
 */
String.prototype.isEngNum = function(exceptChar) {
	return (/^[0-9a-zA-Z]+$/).test(this.remove(exceptChar)) ? true : false;
}

/**
 * 문자열이 한글만으로 구성되어 있는지의 여부를 반환한다.
 * 
 * @param exceptChar -
 *            추가 허용할 문자
 * @return 한글만으로 구성되어 있는지의 여부
 */
String.prototype.isKor = function(exceptChar) {
	return (/^[가-힣]+$/).test(this.remove(exceptChar)) ? true : false;
}

/**
 * 문자열이 숫자와 한글만으로 구성되어 있는지의 여부를 반환한다.
 * 
 * @param exceptChar -
 *            추가 허용할 문자
 * @return 숫자와 한글만으로 구성되어 있는지의 여부
 */
String.prototype.isKorNum = function(exceptChar) {
	return (/^[0-9가-힣]+$/).test(this.remove(exceptChar)) ? true : false;
}

/**
 * 문자열이 영문과 한글만으로 구성되어 있는지의 여부를 반환한다.
 * 
 * @param exceptChar -
 *            추가 허용할 문자
 * @return 영문과 한글만으로 구성되어 있는지의 여부
 */
String.prototype.isEngKor = function(exceptChar) {
	return (/^[a-zA-Z가-힣]+$/).test(this.remove(exceptChar)) ? true : false;
}

/**
 * 이메일 주소의 유효성 여부를 반환한다.
 * 
 * @return 유효성 여부
 */
String.prototype.isEmail = function() {
	return (/\w+([-+.]\w+)*@\w+([-.]\w+)*\.[a-zA-Z]{2,4}$/).test(this.trim());
}

/**
 * 전화번호의 유효성 여부를 반환한다.
 * 
 * @param dlm -
 *            구분자(default : '-')
 * @return 유효성 여부
 */
String.prototype.isPhone = function(dlm) {
	var arg = dlm != null && typeof (dlm) != 'undefined' && dlm.neq('') ? dlm
			: '-';
	return eval("(/(02|0[3-9]{1}[0-9]{1})" + arg + "[1-9]{1}[0-9]{2,3}" + arg
			+ "[0-9]{4}$/).test(this)");
}

/**
 * 휴대폰번호 유효성 여부를 반환한다.
 * 
 * @param dlm -
 *            구분자(default : '-')
 * @return 유효성 여부
 */
String.prototype.isMobile = function(dlm) {
	var arg = dlm != null && typeof (dlm) != 'undefined' && dlm.neq('') ? dlm
			: '-';
	return eval("(/01[016789]" + arg + "[1-9]{1}[0-9]{2,3}" + arg
			+ "[0-9]{4}$/).test(this)");
}

/**
 * 날짜의 유효성 여부를 반환한다.
 * 
 * @return 유효성 여부
 */
String.prototype.isDate = function() {
	var result = false;
	if (this.length == 8 && this.isNum()) {
		var y = Number(this.substring(0, 4));
		var m = Number(this.substring(4, 6));
		var d = Number(this.substring(6, 8));
		var inDate = new Date(y, m - 1, d);
		result = (Number(inDate.getFullYear()) == y
				&& Number(inDate.getMonth() + 1) == m && Number(inDate
				.getDate()) == d);
	}
	return result;
}

/**
 * 파일의 확장자를 구하여 반환한다.
 * @return 확장자
 */
String.prototype.ext = function() {
	return (this.indexOf(".") < 0) ? "" : this.substring(
			this.lastIndexOf(".") + 1, this.length);
}
