package com.jiutong.utils.lang;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.Random;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * 通用工具类
 * 
 * @author : weihui
 * @createTime : 2015年3月16日 下午6:17:26
 * @version : 1.0
 * @description :
 *
 */
public class CommonUtils {
	/**
	 * 获取一个从min到max的随机整数
	 * 
	 * @param min
	 * @param max
	 * @return [min, max]
	 */
	public static int getRandomNumber(int min, int max) {

		if (min >= max)
			return max;

		if (min + 1 == max)
			return min;

		// long to int
		return (int) Math.round(Math.random() * (max - min) + min);

	}
	
	/**
	 * 判断Email (Email由帐号@域名组成，格式为xxx@xxx.xx)<br>
	 * 帐号由英文字母、数字、点、减号和下划线组成，<br>
	 * 只能以英文字母、数字、减号或下划线开头和结束。<br>
	 * 域名由英文字母、数字、减号、点组成<br>
	 * www.net.cn的注册规则为：只提供英文字母、数字、减号。减号不能用作开头和结尾。(中文域名使用太少，暂不考虑)<br>
	 * 实际查询时-12.com已被注册。<br>
	 * 以下是几大邮箱极限数据测试结果<br>
	 * 163.com为字母或数字开头和结束。<br>
	 * hotmail.com为字母开头，字母、数字、减号或下划线结束。<br>
	 * live.cn为字母、数字、减号或下划线开头和结束。hotmail.com和live.cn不允许有连续的句号。
	 * 
	 * @param email
	 * @return
	 */
	public static boolean isEmail(String email) {
		
		return StringUtils.isEmpty(email) ? false
				: PatternUtils
						.regex("^[\\w_-]+([\\.\\w_-]*[\\w_-]+)?@[\\w-]+\\.[a-zA-Z]+(\\.[a-zA-Z]+)?$",
								email, true);
	}

	/**
	 * 从输入字符串中截取EMAIL
	 * 
	 * @param input
	 * @return
	 */
	public static String parseEmail(String input) {

		String regex = "[\\s\\p{Punct}]*([\\w_-]+([\\.\\w_-]*[\\w_-]+)?@[\\w-]+\\.[a-zA-Z]+(\\.[a-zA-Z]+)?)[\\s\\p{Punct}]*";

		return PatternUtils.parseStr(input, regex, 1);
	}

	/**
	 * 判断是否为手机号
	 * 
	 * @param mobile
	 * @return
	 */
	public static boolean isMobile(String mobile) {

		return StringUtils.isEmpty(mobile) ? false : PatternUtils.regex(
				"^(\\+86(\\s)?)?0?1(3|4|5|7|8)\\d{9}$", mobile, true);

	}

	/**
	 * 将带有区号的手机号进行标准格式转化
	 * 
	 * @param mobile
	 * @return
	 */
	public static String getPhoneNumber(String phoneNumber, boolean mobileOnly) {

		if (StringUtils.isEmpty(phoneNumber))
			return "";

		phoneNumber = phoneNumber.replaceAll("[^\\d]", "");
		if (phoneNumber.startsWith("86"))
			phoneNumber = phoneNumber.replaceFirst("86", "");

		String ret = PatternUtils.parseStr(phoneNumber.replaceAll("\\s*", ""),
				"0?(1(3|4|5|8)\\d{9})", 1);

		return isMobile(ret) ? phoneNumber.startsWith("0") ? phoneNumber
				.replaceFirst("0", "") : phoneNumber : mobileOnly ? "" : ret;

	}

	/**
	 * 取文件后缀名
	 * 
	 * @param fileName文件名称
	 *            ，无后缀则返回""，有则返回.XX
	 * @return
	 */
	public static String getFilePostFix(String fileName) {
		return "."
				+ PatternUtils.parseStr(fileName, "^.+(\\.[^\\?]+)(\\?.+)?", 1)
						.replaceFirst("\\.", "");
	}
	
	/**
	 * 获取一个类BASE64编码的随机字符串
	 * 
	 * @param num
	 * @return
	 */
	public static String getRandomString(int num) {
		Random rd = new Random();
		StringBuilder content = new StringBuilder(num);

		for (int i = 0; i < num; i++) {
			int n;
			while (true) {
				n = rd.nextInt('z' + 1);
				if (n >= '0' && n <= '9')
					break;
				if (n >= 'a' && n <= 'z')
					break;
				if (n >= 'A' && n <= 'Z')
					break;
			}
			content.append((char) n);
		}
		return content.toString();
	}
	
	/**
	 * 将字符串转换成BASE64编码
	 * 
	 * @param src
	 * @return
	 */
	public static String base64Encode(String src) {

		return StringUtils.isEmptyString(src) ? "" : base64Encode(src
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

	/**
	 * 还原BASE64编码的字符串
	 * 
	 * @param src
	 * @return
	 */
	public static String base64Decode(String src) {

		try {

			return new String(new BASE64Decoder().decodeBuffer(src));

		} catch (Exception e) {

			return "";

		}

	}
	
	/**
	 * 获取传入小数的货币表现形式
	 * 
	 * @param money
	 * @return
	 */
	public static String formatMoney(double money) {

		if ((int) money == money)
			return Integer.toString((int) money);

		return formatMoney("0.00", money);

	}

	/**
	 * 获取传入小数的货币表现形式
	 * 
	 * @param format
	 *            指定的表现形式
	 * @param money
	 * @return
	 */
	public static String formatMoney(String format, double money) {

		DecimalFormat decimalFormat = new DecimalFormat(format);

		return decimalFormat.format(money);

	}
	
	/**
     * 对密码字段进行SHA-256加密,并返回加密后的BASE64编码转换
     * @author liuzj
     * @createTime 2015年5月12日 下午3:36:10
     * @param pswd
     *         注意:该字段必须不为空,且长度为6到32位
     * @return
     */
    public static String encodePassword(String pswd) {

        if (pswd == null || pswd.length() < 6 || pswd.length() > 32)

            throw new IllegalArgumentException(
                    //"Incorrect password! The password must not empty and it's length must between 6 and 32 bits long.");
            		"密码输入错误");

        try {

            MessageDigest alga = MessageDigest.getInstance("SHA-256");

            alga.update(pswd.getBytes());

            byte[] hash = alga.digest();

            return StringUtils.base64Encode(hash);

        } catch (NoSuchAlgorithmException e) {

            return "";

        }
    }
    public static void main(String[] args) {
        System.out.println(encodePassword("123456"));
    }

}
