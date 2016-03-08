package com.vocinno.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.view.View;

public final class MethodsData {
	public static final long Time_One_Second_Half = 500;
	public static final long Time_One_Second = 1000;
	public static final long Time_One_Minute = 60 * 1000;
	public static final long Time_One_Hour = 60 * Time_One_Minute;
	public static final long Time_One_Day = 24 * Time_One_Hour;
	public static final long Time_One_Week = 7 * Time_One_Day;
	public static final long Time_One_Month = 30 * Time_One_Day;
	public static final long Time_One_Year = 12 * Time_One_Month;

	/**
	 * 获取指定小数点长度的数值字符串
	 * 
	 * @param dbData
	 * @param len
	 * @return
	 */
	public static String getDecimalLength(double dbData, int len) {
		StringBuffer sb = new StringBuffer("###.0");
		if (len >= 2) {
			for (int i = 2; i <= len; i++) {
				sb.append("0");
			}
		}

		DecimalFormat df = new DecimalFormat(sb.toString());
		return df.format(dbData);
	}

	public static boolean isHaveEnoughNumber(String str, int len) {
		boolean tf = false;
		int hasLen = 0;
		for (int i = 0; i < str.length() - 1; i++) {
			String strOne = str.substring(i, i + 1);
			try {
				Integer.parseInt(strOne);
				if (++hasLen >= len) {
					tf = true;
					break;
				}
			} catch (Exception e) {
				hasLen = 0;
			}
		}
		return tf;
	}

	/**
	 * 自动分割文本
	 * 
	 * @param content
	 *            需要分割的文本
	 * @param p
	 *            画笔，用来根据字体测量文本的宽度
	 * @param width
	 *            最大的可显示像素（一般为控件的宽度）
	 * @return 一个字符串数组，保存每行的文本
	 */
	private String[] autoSplitString(String content, Paint p, float width) {
		int length = content.length();
		float textWidth = p.measureText(content);
		if (textWidth <= width) {
			return new String[] { content };
		}

		int start = 0, end = 1, i = 0;
		int lines = (int) Math.ceil(textWidth / width); // 计算行数
		String[] lineTexts = new String[lines];
		while (start < length) {
			if (p.measureText(content, start, end) > width) { // 文本宽度超出控件宽度时
				lineTexts[i++] = (String) content.subSequence(start, end);
				start = end;
			}
			if (end == length) { // 不足一行的文本
				lineTexts[i] = (String) content.subSequence(start, end);
				break;
			}
			end += 1;
		}
		return lineTexts;
	}

	/**
	 * 去除(中文)特殊字符或将所有中文标号替换为英文标号
	 * 
	 * @param str
	 * @return
	 */
	public static String stringFilter(String str) {
		str = str.replaceAll("【", "[").replaceAll("】", "]")
				.replaceAll("！", "!").replaceAll("：", ":");// 替换中文标号
		String regEx = "[『』]"; // 清除掉特殊字符
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		return m.replaceAll("").trim();
	}

	/**
	 * 半角转换为全角
	 * 
	 * @param input
	 * @return
	 */
	public static String ToDBC(String input) {
		char[] c = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == 12288) {
				c[i] = (char) 32;
				continue;
			}
			if (c[i] > 65280 && c[i] < 65375)
				c[i] = (char) (c[i] - 65248);
		}
		return new String(c);
	}

	/**
	 * 判断字符串是否为空
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isEmptyString(String str) {
		return str == null || str.trim().length() == 0;
	}

	/**
	 * AES加密字符串
	 */
	public static String encryptAES(String input) {
		String result = "";
		if (MethodsData.isEmptyString(input))
			return result;
		try {
			result = AESUtil.encrypt(input);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * AES解密字符串
	 */
	public static String decryptAES(String input) {
		String result = "";
		if (MethodsData.isEmptyString(input))
			return result;
		try {
			result = AESUtil.decrypt(input);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * MD5加密
	 * 
	 * @param input
	 * @return
	 */
	public static String encryptMD5(String input) {
		StringBuilder res = new StringBuilder();
		try {
			MessageDigest algorithm = MessageDigest.getInstance("MD5");
			algorithm.reset();
			algorithm.update(input.getBytes("UTF-8"));
			byte[] md5 = algorithm.digest();
			for (int i = 0; i < md5.length; i++) {
				String tmp = (Integer.toHexString(0xFF & md5[i]));
				if (tmp.length() == 1) {
					res.append("0").append(tmp);
					// res += "0" + tmp;
				} else {
					res.append(tmp);
					// res += tmp;
				}
			}
		} catch (NoSuchAlgorithmException e) {

			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return res.toString();
	}

	/**
	 * 两个日期间生成随机日期
	 * 
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static String randomDateBetween(String beginDate, String endDate) {
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
			Date start = format.parse(beginDate);// 开始日期
			Date end = format.parse(endDate);// 结束日期
			if (start.getTime() >= end.getTime()) {
				return "1990/01/01";
			}
			long date = random(start.getTime(), end.getTime());

			String birthday = format.format(new Date(date));

			return birthday;
		} catch (Exception e) {
			e.printStackTrace();
			return "1990/01/01";
		}
	}

	/**
	 * 生成随机Long
	 * 
	 * @param begin
	 * @param end
	 * @return
	 */
	private static long random(long begin, long end) {
		long rtnn = begin + (long) (Math.random() * (end - begin));
		if (rtnn == begin || rtnn == end) {
			return random(begin, end);
		}
		return rtnn;
	}

	/**
	 * 根据long时间类型，获取友好显示型的数字
	 * 
	 */
	public static String getFriendDateTime(long date) {
		String timeString = "";

		long now = System.currentTimeMillis();
		long delta = now - date;
		if (delta < 0) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(date);
			timeString = calendar.get(Calendar.YEAR) + "-"
					+ (calendar.get(Calendar.MONTH) + 1) + "-"
					+ calendar.get(Calendar.DAY_OF_MONTH);
			return timeString;
		}
		if (delta > Time_One_Week) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(date);
			timeString = calendar.get(Calendar.YEAR) + "-"
					+ (calendar.get(Calendar.MONTH) + 1) + "-"
					+ calendar.get(Calendar.DAY_OF_MONTH);
		} else if (delta > Time_One_Day) {
			int deltaDay = (int) Math.ceil(delta / Time_One_Day);
			timeString = deltaDay + "天前";
		} else if (delta > Time_One_Hour) {
			int deltaDay = (int) Math.ceil(delta / Time_One_Hour);
			timeString = deltaDay + "小时前";
		} else if (delta > Time_One_Minute) {
			int deltaDay = (int) Math.ceil(delta / Time_One_Minute);
			timeString = deltaDay + "分钟前";
		} else if (delta > Time_One_Second) {
			int deltaDay = (int) Math.ceil(delta / Time_One_Second);
			timeString = deltaDay + "秒前";
		} else
			timeString = "1秒前";

		return timeString;
	}

	/**
	 * 根据long时间类型，获取友好显示型的数字
	 * 
	 * @param date
	 * @return
	 */
	public static String getDateStringFromLong(long date) {
		String timeString = "";
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(date);
		timeString = calendar.get(Calendar.YEAR) + "/"
				+ (calendar.get(Calendar.MONTH) + 1) + "/"
				+ calendar.get(Calendar.DAY_OF_MONTH);
		return timeString;
	}

	public static int dip2px(Context context, float dipValue) {
		float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	public static int px2dip(Context context, float pxValue) {
		float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	public static float dip2pxInFloat(Context context, float dipValue) {
		float scale = context.getResources().getDisplayMetrics().density;
		return dipValue * scale;
	}

	public static float px2dipInFloat(Context context, float pxValue) {
		float scale = context.getResources().getDisplayMetrics().density;
		return pxValue / scale;
	}

	public boolean IsEmail(String str) {
		Pattern pattern = Pattern
				.compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
		Matcher matcher = pattern.matcher(str);
		return matcher.matches();
	}

	/**
	 * 判断是否是正确的手机号码
	 */
	public static boolean isMobileNO(String mobiles) {
		Pattern p = Pattern.compile("^1[3,5,7,8]\\d{9}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}

	/**
	 * 把中文转成Unicode码
	 * 
	 * @param str
	 * @return
	 */
	public static String chinaToUnicode(String str) {
		String result = "";
		for (int i = 0; i < str.length(); i++) {
			int chr1 = str.charAt(i);
			if (chr1 >= 19968 && chr1 <= 171941) {// 汉字范围 \u4e00-\u9fa5 (中文)
				result += "\\u" + Integer.toHexString(chr1);
			} else {
				result += str.charAt(i);
			}
		}
		return result;
	}

	/**
	 * 判断是否为中文字符
	 * 
	 * @param c
	 * @return
	 */
	public static boolean isChinese(char c) {
		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
		if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
				|| ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
				|| ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
				|| ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
			return true;
		}
		return false;
	}

	/**
	 * 将Unicode机器码转为String
	 * 
	 * @param theString
	 * @return
	 */
	public static String decodeUnicode(String theString) {
		char aChar;
		int len = theString.length();
		StringBuffer outBuffer = new StringBuffer(len);
		for (int x = 0; x < len;) {
			aChar = theString.charAt(x++);
			if (aChar == '\\') {
				aChar = theString.charAt(x++);
				if (aChar == 'u') {
					int value = 0;
					for (int i = 0; i < 4; i++) {
						aChar = theString.charAt(x++);
						switch (aChar) {
						case '0':
						case '1':
						case '2':
						case '3':
						case '4':
						case '5':
						case '6':
						case '7':
						case '8':
						case '9':
							value = (value << 4) + aChar - '0';
							break;
						case 'a':
						case 'b':
						case 'c':
						case 'd':
						case 'e':
						case 'f':
							value = (value << 4) + 10 + aChar - 'a';
							break;
						case 'A':
						case 'B':
						case 'C':
						case 'D':
						case 'E':
						case 'F':
							value = (value << 4) + 10 + aChar - 'A';
							break;
						default:
							throw new IllegalArgumentException(
									"Malformed      encoding.");
						}

					}
					outBuffer.append((char) value);
				} else {
					if (aChar == 't') {
						aChar = '\t';
					} else if (aChar == 'r') {
						aChar = '\r';
					} else if (aChar == 'n') {
						aChar = '\n';
					} else if (aChar == 'f') {
						aChar = '\f';
					}
					outBuffer.append(aChar);
				}
			} else {
				outBuffer.append(aChar);
			}

		}
		return outBuffer.toString();
	}

	/**
	 * 计算字符串中指定子字符串出现的次数
	 * 
	 * @param strString
	 * @param suffix
	 * @return
	 */
	public static int getCountIndex(String strString, String suffix) {
		if (strString == null || suffix == null) {
			return 0;
		} else if (strString.equals(suffix)) {
			return 1;
		}
		if (strString.startsWith(suffix)) {
			strString = strString.substring(1);
		}
		if (strString.endsWith(suffix)) {
			strString = strString.substring(0, strString.length() - 2);
		}
		int rows = 1;
		while (strString.contains(suffix)) {
			rows++;
			strString = strString.substring(strString.indexOf(suffix) + 1);
		}
		return rows;
	}

	/**
	 * 用指定字符作为分隔符将字符串分解成数组
	 * 
	 * @param strString
	 * @param suffix
	 * @return
	 */
	public static String[] StringToStringArray(String strString, String suffix) {
		int rows = getCountIndex(strString, suffix);
		if (strString.startsWith(suffix)) {
			strString = strString.substring(1);
		}
		if (strString.endsWith(suffix)) {
			strString = strString.substring(0, strString.length() - 1);
		}
		if (rows == 0) {
			return null;
		}
		String[] strArray = new String[rows];
		if (rows == 1) {
			strArray[0] = strString;
		} else {
			String tmpString = strString;
			for (int i = 0; i < strArray.length - 1; i++) {
				strArray[i] = tmpString.substring(0, tmpString.indexOf(suffix));
				if (i == strArray.length - 2) {
					strArray[strArray.length - 1] = tmpString
							.substring(tmpString.indexOf(suffix) + 1);
				} else {
					tmpString = tmpString
							.substring(tmpString.indexOf(suffix) + 1);
				}
			}
		}
		return strArray;
	}

	/**
	 * 判断字符串中是否存在指定的子字符串
	 * 
	 * @param strXML
	 * @param str
	 * @return
	 */
	public static Boolean isContainStr(String strXML, String str) {
		if (strXML == null) {
			return false;
		} else if (strXML.contains(str)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 获取View控件相对屏幕的点坐标，以及宽高：int[0] x坐标，int[1] y坐标，int[2] 宽度，int[3] 高度
	 * 
	 * @param v
	 * @return
	 */
	public static int[] getViewFrameOnScreen(View v) {
		int[] loc = new int[4];
		int[] location = new int[2];
		v.getLocationOnScreen(location);
		loc[0] = location[0];
		loc[1] = location[1];
		int w = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		int h = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		v.measure(w, h);

		loc[2] = v.getMeasuredWidth();
		loc[3] = v.getMeasuredHeight();

		// base = computeWH();
		return loc;
	}

	public static int[] getScreenWidthHeight(Activity activity) {
		int[] intWidthHeight = new int[2];
		DisplayMetrics metrics = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
		intWidthHeight[0] = metrics.widthPixels;
		intWidthHeight[1] = metrics.heightPixels;
		return intWidthHeight;
	}

}