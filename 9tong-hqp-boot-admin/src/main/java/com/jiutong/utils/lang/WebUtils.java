package com.jiutong.utils.lang;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @description
 * 
 * @author Frank
 * @version 1.0
 * @create_time 2010-2-22
 */
public class WebUtils {

	/**
	 * 使用Nginx做分发处理时，获取客户端IP的方法
	 * 
	 * @param request
	 * @return
	 */
	public static String getNgigxAddress(HttpServletRequest request) {
		String ip = request.getHeader("X-Forwarded-For");
		if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	/**
	 * 获取当前访问页面经response.encodeURL处理后的URL(绝对路径)
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public static String getRewritedURL(HttpServletRequest request,
			HttpServletResponse response) {

		return request.getRequestURL().toString()
				.replace(request.getRequestURI(), "")
				+ getRewritedURI(request, response);
	}

	/**
	 * 获取当前访问页面经response.encodeURL处理后的URI(相对路径)
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public static String getRewritedURI(HttpServletRequest request,
			HttpServletResponse response) {

		return response.encodeURL(getRequestURI(request).toString());

	}

	/**
	 * 获取当前访问页面的URL(绝对路径)
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public static String getRequestURL(HttpServletRequest request) {

		StringBuilder url = new StringBuilder(request.getRequestURL());

		String queryString = request.getQueryString();

		if (!StringUtils.isEmpty(queryString))
			url.append("?").append(queryString);

		return url.toString();
	}

	/**
	 * 获取当前访问页面的URI(相对路径)
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public static String getRequestURI(HttpServletRequest request) {

		StringBuilder url = new StringBuilder(request.getRequestURI());

		String queryString = request.getQueryString();

		if (!StringUtils.isEmpty(queryString))
			url.append("?").append(queryString);

		return url.toString();
	}

	/**
	 * 获取前一个访问页面的URL
	 * 
	 * @param request
	 * @return
	 */
	public static String getFromUrl(HttpServletRequest request) {

		return getFromUrl(request, true);

	}

	/**
	 * 获取前一个访问页面的URL
	 * 
	 * @param request
	 * @param site
	 *            是否不需要获取本站Url
	 * @return
	 */
	public static String getFromUrl(HttpServletRequest request, boolean site) {

		String fromUrl = request.getHeader("Referer");

		if (StringUtils.isEmpty(fromUrl))
			return "";

		if (site
				&& (PatternUtils.regex("17buy\\.com", fromUrl, false) || PatternUtils
						.regex("127.0.0.1", fromUrl, false)))
			return "";

		return fromUrl;

	}



	/**
	 * 根据所提供的URL获取网页内容
	 * 
	 * @param url
	 * @return
	 */
	public static String getHtmlContent(String url) {
		return getHtmlContent(url, 10 * 1000, 15 * 1000, "utf-8");
	}

	/**
	 * 根据所提供的URL获取网页内容
	 * 
	 * @param url
	 * @param charset
	 * @return
	 */
	public static String getHtmlContent(String url, String charset) {
		return getHtmlContent(url, 10 * 1000, 15 * 1000, charset);
	}

	/**
	 * 根据所提供的URL获取网页内容
	 * 
	 * @param url
	 * @param charset
	 * @param connectTimeout
	 * @param readTimeout
	 * @return
	 */
	public static String getHtmlContent(String url, int connectTimeout,
			int readTimeout, String charset) {

		StringBuffer inputLine = new StringBuffer();

		try {
			HttpURLConnection urlConnection = (HttpURLConnection) new URL(url)
					.openConnection();

			HttpURLConnection.setFollowRedirects(true);
			urlConnection.setConnectTimeout(connectTimeout);
			urlConnection.setReadTimeout(readTimeout);
			urlConnection.setRequestProperty("User-Agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
			urlConnection
					.setRequestProperty(
							"Accept",
							"text/vnd.wap.wml,text/html, application/xml;q=0.9, application/xhtml+xml;q=0.9, image/png, image/jpeg, image/gif, image/x-xbitmap, */*;q=0.1");

			BufferedReader in;

			if ("gzip".equalsIgnoreCase(urlConnection.getContentEncoding()))
				in = new BufferedReader(new InputStreamReader(
						new GZIPInputStream(urlConnection.getInputStream()),
						charset));
			else
				in = new BufferedReader(new InputStreamReader(
						urlConnection.getInputStream(), charset));

			String str;
			while ((str = in.readLine()) != null)
				inputLine.append(str).append("\r\n");

			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return inputLine.toString();
	}

	/**
	 * 判断URL是否包含协议声明,若无则使用HTTP协议
	 * 
	 * @param url
	 * @return
	 */
	public static String parseTargetUrl(String url) {

		if (!Pattern.matches("\\w+://.+", url))
			url = "http://" + url;

		return url;

	}


}
