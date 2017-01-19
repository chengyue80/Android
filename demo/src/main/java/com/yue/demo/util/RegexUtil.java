package com.yue.demo.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @desc: 正则验证
 * @author: chengyue
 * @createTime: 2015-10-10下午2:58:24
 * @version: v1.0
 */
public abstract class RegexUtil {

	/**
	 * 正则,匹配URL
	 */
	private static final String REGEX_URL = "http://([\\w-]+\\.)+[\\w-]+(/[\\w-./?%&=]*)?";
	/**
	 * 正则,匹配全汉字
	 */
	private static final String REGEX_CHINESE_ONLY = "[\\u4e00-\\u9fa5]+";
	/**
	 * 正则,匹配a-z(英文小写)
	 */
	private static final String REGEX_LOWERCASE_ONLY = "[a-z]+";
	/**
	 * 正则,匹配A-Z(英文大写)
	 */
	private static final String REGEX_UPPERCASE_ONLY = "[A-Z]+";
	/**
	 * 正则,匹配a-z A-Z(英文大写或小写)
	 */
	private static final String REGEX_LowerOrUpperCase = "[A-Za-z]+";
	/**
	 * 正则,匹配0-9(数字)
	 */
	private static final String REGEX_NUM_ONLY = "[0-9]+";
	/**
	 * 正则,匹配0和非0开头的数字
	 */
	private static final String REGEX_ZERO_OR_NOTSTARTWITHZERO = "(0|[1-9][0-9]*)";
	/**
	 * 正则,只能由a-z A-Z 0-9 组成
	 */
	private static final String REGEX_AZ_az_Num_DOWNLINE = "[a-zA-Z0-9]+";
	/**
	 * 正则,匹配必须是英文字母开头,只能由a-z A-Z 0-9 和 下划线"_"组成
	 */
	private static final String REGEX_AZ_az_Num_DOWNLINE_STARTWITHENGLISH = "[a-zA-Z][a-zA-Z0-9_]+";
	/**
	 * 正则,匹配月份,正确格式为01~09或1~12
	 */
	private static final String REGEX_MOUTH = "(0?[1-9]|1[0-2])";
	/**
	 * 正则,匹配一个月的31天,正确格式为01~09或1~31
	 */
	private static final String REGEX_DAY = "((0?[1-9])|((1|2)[0-9])|30|31)";
	/**
	 * 正则,验证是否含有特殊的字符(^%&',;=?$\")
	 */
	private static final String REGEX_HAVE_SOECIAL_CHAR = "[^%&',;=?$\\x22]+";
	/**
	 * 正则,匹配身份证 15~18位 可以判断出生年月是否符合要求
	 */
	private static final String REGEX_CARD_ID = "[\\d]{6}(19|20)*[\\d]{2}((0[1-9])|(11|12))([012][\\d]|(30|31))[\\d]{3}[xX\\d]*";
	/**
	 * 正则,匹配邮箱
	 */
	private static final String REGEX_EMAIL = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
	/**
	 * 正则,匹配日期 yyyy-MM-dd(0001-9999)
	 */
	private static final String REGEX_DATE_YYYY_MM_DD = "(?:(?!0000)[0-9]{4}-(?:(?:0[1-9]|1[0-2])-(?:0[1-9]|1[0-9]|2[0-8])|(?:0[13-9]|1[0-2])-(?:29|30)|(?:0[13578]|1[02])-31)|(?:[0-9]{2}(?:0[48]|[2468][048]|[13579][26])|(?:0[48]|[2468][048]|[13579][26])00)-02-29)";
	/**
	 * 正则,匹配日期 yyyyMMdd(0001-9999)
	 */
	private static final String REGEX_DATE_YYYYMMDD = "(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})(((0[13578]|1[02])(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)(0[1-9]|[12][0-9]|30))|(02(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))0229)";
	/**
	 * 正则,匹配日期dd/MM/yyyy
	 */
	private static final String REGEX_DATE_DD_MM_YYYY = "(?:(?:(?:0?[1-9]|1[0-9]|2[0-8])([-/.]?)(?:0?[1-9]|1[0-2])|(?:29|30)([-/.]?)(?:0?[13-9]|1[0-2])|31([-/.]?)(?:0?[13578]|1[02]))([-/.]?)(?!0000)[0-9]{4}|29([-/.]?)0?2([-/.]?)(?:[0-9]{2}(?:0[48]|[2468][048]|[13579][26])|(?:0[48]|[2468][048]|[13579][26])00))";
	/**
	 * 正则,匹配日期 yyyy-MM-dd hh:mm:ss
	 */
	private static final String REGEX_DATE_YYYY_MM_DD_HH_MM_SS = "(?:(?!0000)[0-9]{4}-(?:(?:0[1-9]|1[0-2])-(?:0[1-9]|1[0-9]|2[0-8])|(?:0[13-9]|1[0-2])-(?:29|30)|(?:0[13578]|1[02])-31)|(?:[0-9]{2}(?:0[48]|[2468][048]|[13579][26])|(?:0[48]|[2468][048]|[13579][26])00)-02-29)\\s+([01][0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]";
	/**
	 * 正则,匹配中国的邮政编码
	 */
	private static final String REGEX_POST_CODE = "[1-9]{1}(\\d+){5}";
	/**
	 * 正则,匹配手机号 13000000000-13999999999 14000000000-14999999999 1500000000-15999999999 1800000000-18999999999
	 */
	private static final String REGEX_MOBILE = "(13|14|15|18){1}\\d{9}";
	/**
	 * 正则,匹配手机号 可以由+86或86开头 13000000000-13999999999 14000000000-14999999999 1500000000-15999999999 1800000000-18999999999
	 */
	private static final String REGEX_MOBILE_STARTWITH86 = "((\\+86)|(86))?(13|14|15|18){1}\\d{9}";
	/**
	 * 正则,匹配QQ号 10000-9999999999 5位到10位的数字
	 */
	private static final String REGEX_QQ = "[1-9][0-9]{4,10}";

	private RegexUtil() {
		throw new InstantiationError("工具类无法实例化");
	}

	/**
	 * 是否为Url 是:返回true 否:返回false
	 * 
	 * @param string
	 *        被验证的字符串
	 * @author dreamcoder
	 */
	public static boolean isUrl(String string) {
		return matcher(string, REGEX_URL);
	}

	/**
	 * 验证是否只为汉字 是:返回true 否:返回false
	 * 
	 * @param string
	 *        被验证的字符串
	 * @author dreamcoder
	 */
	public static boolean isChineseOnly(String string) {
		return matcher(string, REGEX_CHINESE_ONLY);
	}

	/**
	 * 验证是否只为英文小写(a-z) 是:返回true 否:返回false
	 * 
	 * @param string
	 *        被验证的字符串
	 * @author dreamcoder
	 */
	public static boolean isLowerCaseOnly(String string) {
		return matcher(string, REGEX_LOWERCASE_ONLY);
	}

	/**
	 * 验证是否只为英文大写(A-Z) 是:返回true 否:返回false
	 * 
	 * @param string
	 *        被验证的字符串
	 * @author dreamcoder
	 */
	public static boolean isUpperCaseOnly(String string) {
		return matcher(string, REGEX_UPPERCASE_ONLY);
	}

	/**
	 * 验证是否只为英文(A-Z a-z) 是:返回true 否:返回false
	 * 
	 * @param string
	 *        被验证的字符串
	 * @author dreamcoder
	 */
	public static boolean isLowerOrUpperCase(String string) {
		return matcher(string, REGEX_LowerOrUpperCase);
	}

	/**
	 * 是否为指定长度的字符串 是:返回true 否:返回false
	 * 
	 * @param string
	 *        被验证的字符串
	 * @param length
	 *        指定长度
	 * @author dreamcoder
	 */
	public static boolean isLength(String string, int length) {
		if (length <= 0) {
			throw new IllegalArgumentException("Length Is Must Big Then 0");
		}
		String regex = ".{" + length + "}";
		return matcher(string, regex);
	}

	/**
	 * 验证指定字符串的长度是否至少是指定长度(就是字符串长度是否大于或等于指定长度) 是:返回true 否:返回false
	 * 
	 * @param string
	 *        被验证的字符串
	 * @param minLength
	 *        指定长度
	 * @author dreamcoder
	 */
	public static boolean isMaxThenLength(String string, int length) {
		if (length <= 0) {
			throw new IllegalArgumentException("Length Is Must Big Then 0");
		}
		String regex = ".{" + length + ",}";
		return matcher(string, regex);
	}

	/**
	 * 验证指定指定长度是否至少是字符串的长度(就是字符串长度是否小于或等于指定长度) 是:返回true 否:返回false
	 * 
	 * @param string
	 *        被验证的字符串
	 * @param minLength
	 *        指定长度
	 * @author dreamcoder
	 */
	public static boolean isMinThenLength(String string, int length) {
		if (length <= 0) {
			throw new IllegalArgumentException("Length Is Must Big Then 0");
		}
		String regex = ".{0," + length + "}";
		return matcher(string, regex);
	}

	/**
	 * 是否为指定长度的字符串 是:返回true 否:返回false
	 * 
	 * @param string
	 *        被验证的字符串
	 * @param minLength
	 *        指定最小长度
	 * @param maxLength
	 *        指定最大长度
	 * @author dreamcoder
	 */
	public static boolean isBetweenMinAndMax(String string, int minLength, int maxLength) {
		if (minLength <= 0 || maxLength <= 0) {
			throw new IllegalArgumentException("MinLength Or MaxLength Is Must Big Then 0");
		}
		if (minLength > maxLength) {
			throw new IllegalArgumentException("MaxLength Is Must Big Then MinLength");
		}
		String regex = ".{" + minLength + "," + maxLength + "}";
		return matcher(string, regex);
	}

	/**
	 * 验证是否为Email 是:返回true 否:返回false
	 * 
	 * @param string
	 *        被验证的字符串
	 * @author dreamcoder
	 */
	public static boolean isEmail(String string) {
		return matcher(string, REGEX_EMAIL);
	}

	/**
	 * 验证是否为身份证(匹配身份证 15~18位 可以判断出生年月是否符合要求) 是:返回true 否:返回false
	 * 
	 * @param string
	 *        被验证的字符串
	 * @author dreamcoder
	 */
	public static boolean isCardId(String string) {
		return matcher(string, REGEX_CARD_ID);
	}

	/**
	 * 验证是否只为数字(0~9) 是:返回true 否:返回false
	 * 
	 * @param string
	 *        被验证的字符串
	 * @author dreamcoder
	 */
	public static boolean isNumOnly(String string) {
		return matcher(string, REGEX_NUM_ONLY);
	}

	/**
	 * 验证是否为0或者不为0开头的数字 是:返回true 否:返回false
	 * 
	 * @param string
	 *        被验证的字符串
	 * @author dreamcoder
	 */
	public static boolean isZeroOrNotStartWithZeroNum(String string) {
		return matcher(string, REGEX_ZERO_OR_NOTSTARTWITHZERO);
	}

	/**
	 * 验证只能由A-Z a-z 0-9组成
	 * 
	 * @param string
	 * @return
	 * @author dreamcoder
	 */
	public static boolean isAzazNum(String string) {
		return matcher(string, REGEX_AZ_az_Num_DOWNLINE);
	}

	/**
	 * 验证是否为由A-Z a-z 0-9 和 下划线"_"组成并且是字母开头的字符串 是:返回true 否:返回false
	 * 
	 * @param string
	 *        被验证的字符串
	 * @author dreamcoder
	 */
	public static boolean isAzazNumDownLineAndStartWithEnglish(String string) {
		return matcher(string, REGEX_AZ_az_Num_DOWNLINE_STARTWITHENGLISH);
	}

	/**
	 * 验证匹配一个月的31天,正确格式为01~09或1~12 是:返回true 否:返回false
	 * 
	 * @param string
	 *        被验证的字符串
	 * @author dreamcoder
	 */
	public static boolean isMouth(String string) {
		return matcher(string, REGEX_MOUTH);
	}

	/**
	 * 验证匹配一个月的31天,正确格式为01~09或1~31 是:返回true 否:返回false
	 * 
	 * @param string
	 *        被验证的字符串
	 * @author dreamcoder
	 */
	public static boolean isDay(String string) {
		return matcher(string, REGEX_DAY);
	}

	/**
	 * 验证是否含有特殊的字符(^%&',;=?$\") 是:返回true 否:返回false
	 * 
	 * @param string
	 *        被验证的字符串
	 * @author dreamcoder
	 */
	public static boolean isHasSoecialChar(String string) {
		return matcher(string, REGEX_HAVE_SOECIAL_CHAR);
	}

	/**
	 * 验证是否匹配日期 yyyy-MM-dd(0001-9999) 是:返回true 否:返回false
	 * 
	 * @param string
	 *        被验证的字符串
	 * @author dreamcoder
	 */
	public static boolean isDateyyyyMMdd(String string) {
		return matcher(string, REGEX_DATE_YYYY_MM_DD);
	}

	/**
	 * 验证是否匹配日期 yyyyMMdd(0001-9999) 否:返回false
	 * 
	 * @param string
	 *        被验证的字符串
	 * @author dreamcoder
	 */
	public static boolean isDateyyyyMMddAll(String string) {
		return matcher(string, REGEX_DATE_YYYYMMDD);
	}

	/**
	 * 验证是否匹配日期dd/MM/yyyy 是:返回true 否:返回false
	 * 
	 * @param string
	 *        被验证的字符串
	 * @author dreamcoder
	 */
	public static boolean isDateddMMyyyy(String string) {
		return matcher(string, REGEX_DATE_DD_MM_YYYY);
	}

	/**
	 * 验证是否匹配日期 yyyy-MM-dd hh:mm:ss 是:返回true 否:返回false
	 * 
	 * @param string
	 *        被验证的字符串
	 * @author dreamcoder
	 */
	public static boolean isDateyyyyMMddhhmmss(String string) {
		return matcher(string, REGEX_DATE_YYYY_MM_DD_HH_MM_SS);
	}

	/**
	 * 验证是否匹配手机号 13000000000-13999999999 14000000000-14999999999 1500000000-15999999999 1800000000-18999999999 是:返回true 否:返回false
	 * 
	 * @param string
	 *        被验证的字符串
	 * @author dreamcoder
	 */
	public static boolean isMobile(String string) {
		return matcher(string, REGEX_MOBILE);
	}

	/**
	 * 验证是否匹配手机号 可以由+86或86开头 13000000000-13999999999 14000000000-14999999999 1500000000-15999999999 1800000000-18999999999 是:返回true 否:返回false
	 * 
	 * @param string
	 *        被验证的字符串
	 * @author dreamcoder
	 */
	public static boolean isMobileOrStartWith86(String string) {
		return matcher(string, REGEX_MOBILE_STARTWITH86);
	}

	/**
	 * 验证是否匹配邮政编码 格式为100000~999999 是:返回true 否:返回false
	 * 
	 * @param string
	 *        被验证的字符串
	 * @author dreamcoder
	 */
	public static boolean isPostCode(String string) {
		return matcher(string, REGEX_POST_CODE);
	}

	/**
	 * 验证是否匹配QQ号 10000-9999999999 5位到10位的数字 是:返回true 否:返回false
	 * 
	 * @param string
	 *        被验证的字符串
	 * @author dreamcoder
	 */
	public static boolean isQQ(String string) {
		return matcher(string, REGEX_QQ);
	}

	/**
	 * 正则匹配
	 * 
	 * @param string
	 *        字符串
	 * @param regex
	 *        正则表达式
	 * @return 验证结果
	 * @author dreamcoder
	 */
	private static boolean matcher(String string, String regex) {
		if (string == null) {
			return false;
		}
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(string);
		return m.matches();
	}
}
