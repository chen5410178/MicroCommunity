/**
 *
 * Licensed Property to China UnionPay Co., Ltd.
 *
 * (C) Copyright of China UnionPay Co., Ltd. 2010
 *     All Rights Reserved.
 *
 *
 * Modification History:
 * =============================================================================
 *   Author         Date          Description
 *   ------------ ---------- ---------------------------------------------------
 *   xshu       2014-05-28       HTTP通信工具类
 * =============================================================================
 */
package com.java110.acct.cmd.payment.util;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.*;

/**
 *
 * @ClassName HttpClient
 * @Description acpsdk发送后台http请求类
 * @date 2016-7-22 下午4:03:25
 */
public class HttpClient {

	private final static int connectTimeOut = 30000;// 连接超时时间
	private final static int readTimeOut = 30000;// 读超时时间

	/**
	 * 功能：后台交易提交请求报文并接收同步应答报文
	 *
	 */
	public static String post(String url, String jsonParam, boolean bUseProxy) throws Exception {
		HttpURLConnection httpURLConnection = createConnection(url, bUseProxy);
		if (null == httpURLConnection) {
			throw new Exception("Create httpURLConnection Failure");
		}
		String str = "";
		try {
			requestServer(httpURLConnection, jsonParam);
			str = response(httpURLConnection);
		} catch (IOException e) {
			System.err.println("网络异常:" + e.getMessage());
			// 网络异常
			throw e;
		} catch (Exception e) {
			System.err.println("系统错误:" + e.getMessage());
			throw e;
		}
		return str;
	}

	/**
	 * HTTP Post发送消息
	 *
	 */
	private static void requestServer(final URLConnection connection, String message) throws Exception {
		PrintStream out = null;
		try {
			connection.connect();
			out = new PrintStream(connection.getOutputStream(), false, "utf-8");
			out.print(message);
			out.flush();
		} catch (Exception e) {
			throw e;
		} finally {
			if (null != out) {
				out.close();
			}
		}
	}

	/**
	 * 显示Response消息
	 *
	 */
	private static String response(final HttpURLConnection connection) throws URISyntaxException, IOException, Exception {
		InputStream in = null;
		StringBuilder sb = new StringBuilder(1024);
		BufferedReader br = null;
		try {
			if (200 == connection.getResponseCode()) {
				in = connection.getInputStream();
				sb.append(new String(read(in), "utf-8"));
			} else {
				in = connection.getErrorStream();
				sb.append(new String(read(in), "utf-8"));
			}
			return sb.toString();
		} catch (Exception e) {
			throw e;
		} finally {
			if (null != br) {
				br.close();
			}
			if (null != in) {
				in.close();
			}
			if (null != connection) {
				connection.disconnect();
			}
		}
	}

	private static byte[] read(InputStream in) throws IOException {
		byte[] buf = new byte[1024];
		int length = 0;
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		while ((length = in.read(buf, 0, buf.length)) > 0) {
			bout.write(buf, 0, length);
		}
		bout.flush();
		return bout.toByteArray();
	}

	/**
	 * 创建连接
	 *
	 */
	private static HttpURLConnection createConnection(String url, boolean bUseProxy) throws Exception {
		HttpURLConnection httpURLConnection = null;
		URL method = new URL(url);
		try {
			// 交易请求地址 0-专线；1-公网
			if (bUseProxy) {
				// 创建代理服务器
				// HttpHost proxy = new HttpHost("代理服务器IP", 代理服务器端口);
				InetSocketAddress addr = new InetSocketAddress("127.0.0.1", 8828);
				// http 代理
				Proxy proxy = new Proxy(Proxy.Type.HTTP, addr);
				httpURLConnection = (HttpURLConnection) method.openConnection(proxy);
			} else {
				httpURLConnection = (HttpURLConnection) method.openConnection();
			}
		} catch (IOException e) {
			return null;
		}
		httpURLConnection.setConnectTimeout(connectTimeOut);// 连接超时时间
		httpURLConnection.setReadTimeout(readTimeOut);// 读取结果超时时间
		httpURLConnection.setDoInput(true); // 可读
		httpURLConnection.setDoOutput(true); // 可写
		httpURLConnection.setUseCaches(false);// 取消缓存
		httpURLConnection.setRequestProperty("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
		httpURLConnection.setRequestMethod("POST");
		if ("https".equalsIgnoreCase(method.getProtocol())) {
			HttpsURLConnection husn = (HttpsURLConnection) httpURLConnection;
			// 是否验证https证书
			husn.setSSLSocketFactory(new BaseHttp());
			husn.setHostnameVerifier(new BaseHttp.TrustAnyHostnameVerifier());// 解决由于服务器证书问题导致HTTPS无法访问的情况
			return husn;
		}
		return httpURLConnection;
	}
}
