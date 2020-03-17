package co.kr.util.http;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpClientPool {
	private static PoolingClientConnectionManager connectionManager;
	private static final Logger logger = LoggerFactory.getLogger(HttpClientPool.class);
	
	public synchronized static HttpClient getHttpClient(){
		if(connectionManager == null){
			connectionManager = new PoolingClientConnectionManager();
			connectionManager.setMaxTotal(128);
			connectionManager.setDefaultMaxPerRoute(32);
		}
		
		int useConnTimeOut = 2;
		int useSoTimeOut = 10;
		
		BasicHttpParams httpParams = new BasicHttpParams();
		httpParams.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, useConnTimeOut *1000);
		httpParams.setParameter(CoreConnectionPNames.SO_TIMEOUT, useSoTimeOut * 1000);
		
		return new DefaultHttpClient(connectionManager, httpParams);
	}
	
	public static void abort(HttpRequestBase httpRequestBase){
		if(httpRequestBase != null){
			try{
				httpRequestBase.abort();
			}catch (Exception e) {
				
			}
		}
	}
	
	public static void release(HttpResponse response){
		if(response != null && response.getEntity() != null){
			EntityUtils.consumeQuietly(response.getEntity());
		}
	}

}
