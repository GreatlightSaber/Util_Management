package co.kr.util.http;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.kr.util.string.StringUtil;

@Component
public class HttpClientManger {
	private static final Logger logger = LoggerFactory.getLogger(HttpClientManger.class);
		
	@Autowired
	StringUtil stringUtil;
	
	public Map<String, Object> sendPostByMapSSL(String url, Map<String, Object> map){
		HttpClient httpClient = null;
		HttpResponse  response = null;
	    Map<String, Object> rst = new HashMap<>();
	    BufferedReader rd = null;
	    try {
	    	TrustManager easyTrustManager = new X509TrustManager() {
				
	    		@Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    // TODO Auto-generated method stub
                    return null;
                }
                
                @Override
                public void checkServerTrusted(java.security.cert.X509Certificate[] chain,
                        String authType) throws CertificateException {
                    // TODO Auto-generated method stub 
                }
                
                @Override
                public void checkClientTrusted(java.security.cert.X509Certificate[] chain,
                        String authType) throws CertificateException {
                    // TODO Auto-generated method stub
                }
            };

            SSLContext sslcontext = SSLContext.getInstance("TLS");//SSLContext 지정된 시큐어 소켓 프로토콜 구현
            sslcontext.init(null, new TrustManager[] { easyTrustManager }, null);

            SSLSocketFactory socketFactory = new SSLSocketFactory(sslcontext,SSLSocketFactory.STRICT_HOSTNAME_VERIFIER);
            Scheme sch = new Scheme("https", 443, socketFactory);//SSL기본포트 : 443
            
            httpClient = HttpClientPool.getHttpClient();
            httpClient.getConnectionManager().getSchemeRegistry().register(sch);
	    	
	        HttpPost httpPost = new HttpPost(url);
	        
	        ArrayList<NameValuePair> parameters = new ArrayList<NameValuePair>();
	        for (Map.Entry<String, ?> entry : map.entrySet()) {
	            Object value = entry.getValue();
	            if (value instanceof Collection) {
	                Collection<?> values = (Collection<?>) value;
	                for (Object v : values) {
	                    // This will add a parameter for each value in the Collection/List
	                    parameters.add(new BasicNameValuePair(entry.getKey(), v == null ? null : String.valueOf(v)));
	                }
	            } else {
	                parameters.add(new BasicNameValuePair(entry.getKey(), value == null ? null : String.valueOf(value)));
	            }
	        }
	        
	        UrlEncodedFormEntity entityRequest = new UrlEncodedFormEntity(parameters, "UTF-8");
	        httpPost.setEntity(entityRequest);
	        response = httpClient.execute(httpPost);
	        
	        rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
            String line = "";
            StringBuffer sb = new StringBuffer();
            while ((line = rd.readLine()) != null) {
            	sb.append(line);
            }
            rst = stringUtil.jsonToMap(sb.toString());
	    } catch (Exception e) {
	    	//logger.error(LocalPayConst.ERROR_TAG, e);
	    } finally {
	    	HttpClientPool.release(response);
	    	try{ 
	    		if(rd != null) rd.close();
	    	}catch (Exception e) {
	    		//logger.error(LocalPayConst.ERROR_TAG, e);
			}
	    }
	    return rst;
	}
	
	// request값 로그 확인
	public static void logGetRequestParameter(HttpServletRequest request){
		Enumeration<String> paramNames = request.getParameterNames();
		while(paramNames.hasMoreElements()){
			String name = paramNames.nextElement();
			String [] data = request.getParameterValues(name);
			for(String eachData : data){
				logger.debug("########## parameter " + name + " : " + eachData);
			}
		}
	}

}
