package com.baidu.disconf.core.common.restful.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Rest接口调用
 * @author 周宁
 * @创建时间 2016年12月8日
 * @version 1.0
 */
public class RestUtil {

	/**
	 * 
	 * 调用GET请求rest
	 * @author 周宁
	 * @date 2016年12月8日
	 * @param 
	 * @return
	 * @throws
	 */
	public static String restGet(String url)throws Exception{
		URL restServiceURL = new URL(url);
		HttpURLConnection httpConnection = (HttpURLConnection) restServiceURL
				.openConnection();
		String output = null;
		try {
			httpConnection.setRequestMethod("GET");
			httpConnection.setRequestProperty("Accept", "application/json");
			if (httpConnection.getResponseCode() != 200) {
				throw new RuntimeException(
						"HTTP GET Request Failed with Error code : "
								+ httpConnection.getResponseCode());
			}
			BufferedReader responseBuffer = new BufferedReader(
					new InputStreamReader((httpConnection.getInputStream()),"utf-8"));
			output = responseBuffer.readLine();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			httpConnection.disconnect();
		}
		return output;
	}
}
