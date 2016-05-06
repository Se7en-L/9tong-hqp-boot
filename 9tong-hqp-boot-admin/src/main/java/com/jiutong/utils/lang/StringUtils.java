package com.jiutong.utils.lang;

import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import sun.misc.BASE64Encoder;

@SuppressWarnings("all")
public class StringUtils extends org.apache.commons.lang.StringUtils {
	public static boolean compareStrSplitByComma(String firstStr,
			String secondStr) {
		if (firstStr == null || secondStr == null) {
			return false;
		}
		String[] firstArray = firstStr.split(",");
		String[] secondArray = secondStr.split(",");
		Map<String, String> secondStrMap = new HashMap<String, String>();
		if (firstArray.length != secondArray.length) {
			return false;
		}
		for (int i = 0; i < secondArray.length; i++) {
			secondStrMap.put(secondArray[i], secondArray[i]);
		}
		for (int i = 0; i < firstArray.length; i++) {
			if (!secondStrMap.containsKey(firstArray[i])) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 将指定double类型转换为金钱格式字符
	 * 
	 * @param doubleValue
	 * @return String
	 */
	public static String doubleToCurrency(double doubleValue) {
		Object[] args = { new Double(doubleValue) };
		return MessageFormat.format(
				"{0,number,￥,#,###,###,###,###,###,##0.00}", args);
	}

	/**
	 * 将字符串对象按srcEncoding编码转换为destEncoding编码格
	 * 
	 * @param stringValue
	 * @param srcEncoding
	 * @param destEncoding
	 * @return String
	 */
	public static String encodeString(String stringValue, String srcEncoding,
			String destEncoding) {
		// 如果参数为null，返回null
		if (stringValue == null || srcEncoding == null || destEncoding == null) {
			return null;
		}
		String value = null;
		try {
			value = new String(stringValue.getBytes(srcEncoding), destEncoding);
		} catch (UnsupportedEncodingException ex) {
			value = stringValue;
		}
		return value;
	}

	/**
	 * 判断是否指定字符串为空字符串(null或者长度为0
	 * 
	 * @param stringValue
	 * @return boolean
	 */
	public static boolean isEmptyString(String stringValue) {
		if (stringValue == null || stringValue.trim().length() < 1
				|| stringValue.trim().equalsIgnoreCase("null")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 判断是否指定字符串为空字符串(null或者长度为0
	 * 
	 * @param stringValue
	 * @return boolean
	 */
	public static boolean isEmpty(String stringValue) {
		if (stringValue == null || stringValue.trim().length() < 1
				|| stringValue.trim().equalsIgnoreCase("null")) {

			return true;
		} else {
			return false;
		}
	}

	/**
	 * 判断是否指定字符串为空字符串(null或者长度为0
	 * 
	 * @param stringValue
	 * @return boolean
	 */
	public static boolean isNotEmpty(String stringValue) {
		if (stringValue == null || stringValue.trim().length() < 1
				|| stringValue.trim().equalsIgnoreCase("null")) {
			return false;
		} else {
			return true;
		}
	}

	public static boolean isNumber(String str) {
		if (StringUtils.isEmptyString(str)) {
			return false;
		}

		for (int i = 0; i < str.length(); i++) {
			char ch = str.charAt(i);

			if (ch < '0' || ch > '9') {
				return false;

			}
		}
		return true;
	}

	public static boolean isDouble(String str) {
		try {
			Double.parseDouble(str);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	/**
	 * 提取指定字符串中的所有的数字字符
	 * 
	 * @param stringValue
	 * @return String[]
	 */
	public static String[] getNumStringArray(String stringValue) {
		if (stringValue == null) {
			return null;
		}
		ArrayList<String> list = new ArrayList<String>();
		Pattern p = Pattern.compile("([0-9])+");
		Matcher m = p.matcher(stringValue);
		while (m.find()) {
			list.add(m.group());
		}
		return (String[]) list.toArray(new String[0]);
	}

	/**
	 * 将十进制数字字符串转换为整型数字，如果转换失败返回-
	 * 
	 * @param stringValue
	 * @return int
	 */
	public static int stringToInt(String stringValue) {
		return stringToInt(stringValue, -1);
	}

	/**
	 * 将十进制数字字符串转换为整型数字，如果转换失败返回默认
	 * 
	 * @param stringValue
	 * @param defaultValue
	 * @return int
	 */
	public static int stringToInt(String stringValue, int defaultValue) {
		int intValue = defaultValue;
		if (stringValue != null) {
			try {
				intValue = Integer.parseInt(stringValue);
			} catch (NumberFormatException ex) {
				intValue = defaultValue;
			}
		}
		return intValue;

	}

	/**
	 * 将指定字符串对象默认编码IOS8859_1编码转为GBK编码的字符串对
	 * 
	 * @param stringValue
	 * @return String
	 */
	public static String toGBKString(String stringValue) {
		return encodeString(stringValue, "ISO8859_1", "GBK");

	}

	/**
	 * 构造函数
	 * 
	 */
	public StringUtils() {
	}


	/**
	 * 两个值是否相等 (参数如果是空则为false)
	 * 
	 * @param string
	 * @param value
	 * @return
	 */
	public static boolean isEquals(Object string, Object value) {
		return string != null && value != null
				&& string.toString().equals(value.toString());
	}

	public static boolean isNotNull(String str) {
		if (str == null || str.equals("")) {
			return false;
		}
		return true;
	}

	public static boolean isNULL(Object o) {
		if (o == null)
			return true;
		if (o instanceof String)
			return isEmpty((String) o);
		return false;
	}

	public static boolean isNotNULL(Object o) {
		return !isNULL(o);
	}
	
	
	/**
	 * 将字符串转换成BASE64编码
	 * 
	 * @param src
	 * @return
	 */
	public static String base64Encode(String src) {

		return isNULL(src) ? "" : base64Encode(src
				.getBytes());

	}

	/**
	 * 将字符串转换成BASE64编码
	 * 
	 * @param byte[]
	 * @return
	 */
	public static String base64Encode(byte b[]) {

		return (null == b || b.length < 1) ? "" : new BASE64Encoder().encode(b);

	}
}
