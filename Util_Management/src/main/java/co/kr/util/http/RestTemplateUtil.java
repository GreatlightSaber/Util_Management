package co.kr.util.http;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.google.gson.JsonObject;

import co.kr.util.string.StringUtil;


/**
 * <pre>
 * http 기반 I/F를 위한 공통처리
 * </pre>
 * @author UncleJoe
 *
 */
@Component
public class RestTemplateUtil 
{
	
	private final Logger LOGGER = LoggerFactory.getLogger(RestTemplateUtil.class);


	//env.properties 정의
	@Value("#{env['http.conn_timeout']}")
	private int TIME_OUT;
	
	@Value("#{env['http.conn_max_per_route']}")
	private int MAX_PER_ROUTE;
	
	@Value("#{env['http.conn_max_total']}")
	private int MAX_CONN;
	
	@Value("#{env['http.cps.url']}")
	private String CPS_HTTP_ROOT_URL ;
		
	@Value("#{env['http.mbrshp.url']}")
	private String MBRSHP_HTTP_ROOT_URL ;
	
	@Value("#{env['http.parking.url']}")
	private String PARKING_HTTP_ROOT_URL ;
	
	interface HttpHeaderKey {
		interface Common {

			final String CONTENT_TYPE 	= "Content-Type";
			final String ACCEPT  		= "Accept";
			final String ACCEPT_CHARSET = "Accept-Charset";
			
		}
		interface Mbrshp {
			final String AUTHORIZATION = "AUTHORIZATION";
//			final String H_KEY = "H_KEY";
			
		}
		interface CPS{
			
			final String CPS_SYS_COD 	= "cps-sys-cod";
			final String CPS_USR_ID 	= "cps-usr-id";
			final String CPS_DEPT_ID  	= "cps-dept-id";
			final String CPS_API_ID 	= "cps-api-id";
			final String CPS_API_VER 	= "cps-api-ver";
			final String REG_DT  		= "reg-dt";
		}
	}
	
	interface HttpResBodyKey {
		
		interface Common {
			final String RTN_CODE = "RTN_CODE";
			final String RTN_DATA = "RTN_DATA";

			
		}
		
		interface Mbrshp {
			
			final String MBS_RST_COD 	= "mbs_rst_cod";
			final String MBS_RST_MSG 	= "mbs_rst_msg";
			
		}
		interface CPS{
			
			final String CPS_RST_COD 	= "cps_rst_cod";
			final String CPS_RST_MSG 	= "cps_rst_msg";
		}
		
		
	}
	
	
	/**<pre>
	 * RestTemplate 기반 http 통신 결과를 HttpEntity<String>(jsonBodyStr, headers) 로 리턴
	 * </pre>
	 * @param httpMethod
	 * @param requestURL
	 * @param headers
	 * @param jsonBodyStr
	 * @return
	 */
	protected ResponseEntity<String> httpCommon(String httpMethod, String requestURL, HttpHeaders headers, String jsonBodyStr) {
		
		LOGGER.debug("jsonBodyStr |||| "+jsonBodyStr);
		
		RestTemplate rt = initRestTemplate();
		rt.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
				
		if(jsonBodyStr == null) jsonBodyStr="";  //body값이 없는 경우 Default ""로 처리
		
		HttpEntity<String> httpEntity = new HttpEntity<String>(jsonBodyStr, headers);
				
		return rt.exchange(requestURL, HttpMethod.valueOf(httpMethod.toUpperCase()), httpEntity, String.class);

	}
	
	/**<pre>
	 * RestTemplate 기반 http 통신 결과를 HttpEntity<String>(jsonBodyStr, headers) 로 리턴 (FILE SEND)
	 * </pre>
	 * @param httpMethod
	 * @param requestURL
	 * @param headers
	 * @param jsonBodyStr
	 * @param filePatch
	 * @return
	 */
	protected ResponseEntity<String> httpCommon(String httpMethod, String requestURL, HttpEntity<MultiValueMap<String, Object>> httpEntity) {
		
		RestTemplate rt = initRestTemplate();		
		rt.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
			    
	    return rt.exchange(requestURL, HttpMethod.valueOf(httpMethod.toUpperCase()), httpEntity, String.class);
	}

	

	/**
	 * Connection Pool, 등의설정을 통한 초기화
	 * @return
	 */
	private RestTemplate initRestTemplate() {
		
		RequestConfig config = RequestConfig.custom()
				  .setConnectTimeout(TIME_OUT * 1000)
				  .setConnectionRequestTimeout(TIME_OUT * 1000)
				  .setSocketTimeout(TIME_OUT * 1000)
				  .setExpectContinueEnabled(false)
				  .build();
		
		CloseableHttpClient client    = HttpClients.custom()
				.setSSLHostnameVerifier(new NoopHostnameVerifier())
				.setDefaultRequestConfig(config)
				.setMaxConnTotal(MAX_CONN)
				.setMaxConnPerRoute(MAX_PER_ROUTE)
				.build();
		
	    HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
	    requestFactory.setHttpClient(client);
	    
	    return new RestTemplate(requestFactory);

	}

	/**
	 * PUSH 연동 호출용 http Util
	 * @param httpMethod
	 * @param requestURL
	 * @param headerData
	 * @param jsonBodyStr
	 * @return
	 * @throws Exception
	 */
	public String pushHttp(String httpMethod, String requestURL, Map<String, Object> pushOpt, String filePatch) {
				
		String responseData = "";
		String resultCode = ""; 
		
		try {
			Map<String, String> pushHeaderData = new HashMap<String, String>();
			pushHeaderData.put(HttpHeaderKey.Common.CONTENT_TYPE  , MediaType.MULTIPART_FORM_DATA_VALUE);
			pushHeaderData.put(HttpHeaderKey.Common.ACCEPT 		 , MediaType.APPLICATION_JSON_VALUE);
			pushHeaderData.put(HttpHeaderKey.Common.ACCEPT_CHARSET, "utf-8");
			
			LOGGER.debug(">>>>>>> httpMethod_Push:{}", httpMethod);
			LOGGER.debug(">>>>>>> requestURL_Push:{}", requestURL);
			LOGGER.debug(">>>>>>> headerData_Push:{}", pushHeaderData);
			LOGGER.debug(">>>>>>> pushOpt_Push:{}", pushOpt);
			
			HttpHeaders headers = new HttpHeaders();			
			headers.setAll(pushHeaderData);
			
			MultiValueMap<String, Object> bodyMap = new LinkedMultiValueMap<>();
			bodyMap.setAll(pushOpt);
			
			if(filePatch.equals("")){
			    //headers.setContentType(MediaType.MULTIPART_FORM_DATA);
			    HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(bodyMap, headers);
			    
			    ResponseEntity<String> response =  this.httpCommon(httpMethod, requestURL, httpEntity);
			    responseData = response.getBody();
			    
			    resultCode = StringUtil.jsonToMap(responseData, "HEADER").get("RESULTCODE").toString();
			    
			}			
			
			
			if(!filePatch.equals("")){	    
			    bodyMap.add("CSVFILE", new FileSystemResource(filePatch));			    
			    //headers.setContentType(MediaType.MULTIPART_FORM_DATA);
			    			    
			    HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(bodyMap, headers);
			    
			    ResponseEntity<String> response =  this.httpCommon(httpMethod, requestURL, httpEntity);			    
			    responseData = response.getBody();
			    
			    resultCode = StringUtil.jsonToMap(responseData, "HEADER").get("RESULTCODE").toString();
			    
			}
			LOGGER.debug("responseData || "+responseData);
						
		} catch (Exception e) {
			resultCode = "0001";
			responseData = e.getMessage();
			LOGGER.debug("ERROR pushHttp || "+e.getMessage());
			e.getStackTrace();
		}
		
		return resultCode;
	}
	
	/**
	 * <pre>
	 * 퀸텟 멤버십 호출용 http Util
	 *
	 * ApiResult result = restTemplateUtil.membershipHttp("POST", reqUrl, jsonStr);
	 * Map<String, Object> headData = result.getHeadData();
	 * List<Map<String, Object>> bodyDataList = result.getBodyData();
	 * </pre>
	 * @param httpMethod
	 * @param requestURL
	 * @param headerData
	 * @param jsonBodyStr
	 * @return
	 * @throws Exception
	 */
	public JsonObject membershipHttp(String httpMethod , String requestURL, String jsonBodyStr){
		
		JsonObject jsonInfo = new JsonObject(); //{}
		
		try {
			
			Map<String, String> headerData = new HashMap<String, String>();
			// header 데이터 설정
			// headerData.put(HttpHeaderKey.Common.CONTENT_TYPE , MediaType.APPLICATION_JSON_VALUE);
			
			LOGGER.debug(">>>>>>> httpMethod:{}", httpMethod);
			LOGGER.debug(">>>>>>> requestURL:{}", requestURL);
			LOGGER.debug(">>>>>>> headerData:{}", headerData);
			LOGGER.debug(">>>>>>> jsonBodyStr:{}", jsonBodyStr);
			
			// request url 설정
			if(requestURL.startsWith("/"))
				requestURL = "http://localhost:8080";
				// requestURL = MBRSHP_HTTP_ROOT_URL + requestURL;
			else if(requestURL.startsWith("http")){
				//skip
			}
			else requestURL = "http://localhost:8080" + "/" + requestURL;
			//else requestURL = MBRSHP_HTTP_ROOT_URL + "/" + requestURL;
			
			HttpHeaders headers = new HttpHeaders();
			
			List<MediaType> mediaTypeList = new ArrayList<>();
			mediaTypeList.add(MediaType.APPLICATION_JSON);

			headers.setAccept(mediaTypeList);		
			headers.setAll(headerData);
			
			ResponseEntity<String> response =  this.httpCommon(httpMethod, requestURL, headers, jsonBodyStr);
						
			if (response.getStatusCode().is2xxSuccessful()) {
				
				HttpHeaders hdrs = response.getHeaders();

				LOGGER.debug("\n\n");
				LOGGER.debug("\n\n");
				LOGGER.debug(hdrs.toString());
				LOGGER.debug("\n\n");
				LOGGER.debug("\n\n");
				
				String bodyMsg = response.getBody();
				
				LOGGER.debug(requestURL+"(bodyMsg) ||| "+bodyMsg);
				jsonInfo.addProperty("body", bodyMsg);
				
				//return apiResult;
				
			}else{
			
				jsonInfo.addProperty(HttpResBodyKey.Common.RTN_CODE, response.getStatusCode().toString());
				jsonInfo.addProperty(HttpResBodyKey.Common.RTN_DATA, response.getStatusCodeValue());
				
				
				
				LOGGER.debug(requestURL+"(jsonInfo ||| "+jsonInfo.toString());
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			
			jsonInfo.addProperty(HttpResBodyKey.Mbrshp.MBS_RST_COD, "9999");
			jsonInfo.addProperty(HttpResBodyKey.Mbrshp.MBS_RST_MSG, "inferFace Error\n\n" + e.getMessage());
			
			
			LOGGER.debug(requestURL+"(jsonInfo ||| "+jsonInfo.toString());
			
			//return apiResult;
		}
		
		return jsonInfo;
		
	}
}
