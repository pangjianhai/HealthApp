package cn.com.hzzc.health.pro.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.logging.Logger;

/**
 * 日期工具类
 * 
 * @author pang
 */
public class CommonDateUtil {

	/**
	 * @description 前一个月第一天
	 * @param date
	 * @return
	 */
	public static Date preMonthFirstDate(Date date) {
		int month = CommonDateUtil.getMonth(date);
		int year = CommonDateUtil.getYear(date);
		month--;
		if (month == 0) {
			month = 12;
			year--;
		}
		String strDate = year + "-" + month + "-" + "1";
		return getDate(strDate);
	}

	/**
	 * @description 前一个月最后一天
	 * @param date
	 * @return
	 */
	public static Date preMonthLastDate(Date date) {
		int month = CommonDateUtil.getMonth(date);
		int year = CommonDateUtil.getYear(date);
		month--;
		if (month == 0) {
			month = 12;
			year--;
		}
		int daysInMonth[] = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
		String strDate = year + "-" + month + "-" + daysInMonth[month];
		return getDate(strDate);
	}

	/**
	 * @description 日期转换 字符串 转为 date
	 * @param value
	 * @param patten
	 * @return
	 */
	public static Date getDate(String value, String patten) {
		Date date = null;
		if (value == null) {
			return new Date();
		}
		SimpleDateFormat fordate = new SimpleDateFormat(patten);
		try {
			date = fordate.parse(value);
		} catch (ParseException e) {
		}
		return date;
	}

	/**
	 * @description 日期时间转换
	 * @param value
	 * @return 形如yyyy-MM-dd HH:mm:ss congge 2012-2-15下午05:32:40
	 */
	public static Date getTime(String value) {
		Date date = null;
		if (value == null) {
			return null;
		}
		SimpleDateFormat fordate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			date = fordate.parse(value);
		} catch (ParseException e) {
		}
		return date;
	}

	/**
	 * @description 日期转换 形如 yyyy-MM-dd
	 * @param value
	 * @return congge 2012-2-15下午05:33:21
	 */
	public static Date getDate(String value) {
		Date date = null;
		if (value == null || "".equals(value)) {
			return new Date();
		}
		SimpleDateFormat fordate = new SimpleDateFormat("yyyy-MM-dd");
		try {
			date = fordate.parse(value);
		} catch (ParseException e) {
			return null;
		}
		return date;
	}

	/**
	 * @description 获取当前时间
	 * @return
	 */
	public static String getNowTime() {
		Date date = new Date();
		SimpleDateFormat fordate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return fordate.format(date);
	}

	/**
	 * @description 获取当前时间 yyyy - mm - dd
	 * @return
	 */
	public static String getCurrTime() {
		Date date = new Date();
		SimpleDateFormat fordate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return fordate.format(date);
	}

	/**
	 * @description 获取当前时间
	 * @return
	 */
	public static Date getNowTimeDate() {
		Date date = new Date();
		SimpleDateFormat fordate = new SimpleDateFormat("yyyy-MM-dd");
		String time = fordate.format(date);
		try {
			date = fordate.parse(time);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * @description 获取当前时间
	 * @return
	 */
	public static Date getNowTimeDateHMS() {
		Date date = new Date();
		SimpleDateFormat fordate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = fordate.format(date);
		try {
			date = fordate.parse(time);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * @description 将 date 型日期转换为 yyyy-MM-dd HH:mm:ss String 型
	 * @param date
	 * @return
	 */
	public static String getStrTimeByDate(Date date) {
		if (date == null) {
			return null;
		}
		try {
			SimpleDateFormat fordate = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			return fordate.format(date);
		} catch (Exception e) {
		}
		return null;
	}

	/**
	 * @description 前一天
	 * @param date
	 * @return
	 */
	public static Date preDate(Date date) {
		long time = date.getTime();
		time = time - 24 * 60 * 60 * 1000;
		Date ytd = new Date(time);
		return ytd;
	}

	/**
	 * @description 昨天
	 * @return
	 */
	public static Date yesterday() {
		Date now = new Date();
		long time = now.getTime();
		time = time - 24 * 60 * 60 * 1000;
		Date ytd = new Date(time);
		return ytd;

	}

	/**
	 * @description 某天 的明天
	 * @param date
	 * @return
	 */
	public static Date nextDay(Date date) {
		long time = date.getTime();
		time = time + 24 * 60 * 60 * 1000;
		Date tomorrow = new Date(time);
		return tomorrow;
	}

	/**
	 * @description 某时间的 几天后的时间
	 * @param date
	 * @param count
	 * @return congge 2012-2-15下午05:34:31
	 */
	public static Date nextSomeDayDate(Date date, int count) {
		long time = date.getTime();
		time = time + 24 * 60 * 60 * 1000 * count;
		Date someDay = new Date(time);
		return someDay;
	}

	/**
	 * @description 某时间 的 多少分钟前 的时间
	 * @param date
	 * @param preMinute
	 * @return
	 */
	public static Date preDateMinute(Date date, int preMinute) {
		long time = date.getTime();
		time = time - 60 * 1000 * preMinute;
		Date preTime = new Date(time);
		return preTime;
	}

	/**
	 * @description 某时间的多少分钟后的时间
	 * @param date
	 * @param delayMinute
	 * @return congge 2012-3-27下午01:27:43
	 */
	public static Date nextDateMinute(Date date, int delayMinute) {
		long time = date.getTime();
		time = time + 60 * 1000 * delayMinute;
		Date delayTime = new Date(time);
		return delayTime;
	}

	/**
	 * @description 某时间的多少秒后的时间
	 * @param date
	 *            目标时间
	 * @param delySecond
	 *            相隔秒数
	 * @return 目标时间之后的时间
	 */
	public static Date nextDateSecond(Date date, int delySecond) {
		long time = date.getTime();
		time = time + 1000 * delySecond;
		Date delayTime = new Date(time);
		return delayTime;
	}

	/**
	 * @description 明天
	 * @return
	 */
	public static Date tomorrow() {
		Date now = new Date();
		long time = now.getTime();
		time = time + 24 * 60 * 60 * 1000;
		Date tomorrow = new Date(time);
		return tomorrow;
	}

	/**
	 * @description 格式化 日期 yyyy-MM-dd
	 * @param date
	 * @return
	 */
	public static String formatDate(Date date) {
		return formatDate(date, "yyyy-MM-dd");
	}

	/**
	 * @description 格式化日期
	 * @param date
	 * @param formater
	 * @return
	 */
	public static String formatDate(Date date, String formater) {
		SimpleDateFormat bartDateFormat = new SimpleDateFormat(formater);
		if (date == null) {
			return null;
		} else {
			String datestring = bartDateFormat.format(date);
			return datestring;
		}
	}

	/**
	 * @description 格式化日期 yyyy.MM
	 * @return
	 */
	public static String getYearMonth() {
		String dateStr = formatDate(new Date(), "yyyy.MM");
		return dateStr;
	}

	/**
	 * @description
	 * @param format
	 * @return congge 2012-2-15下午05:43:02
	 */
	public static String getFormatDate(String format) {
		String dateStr = formatDate(new Date(), format);
		return dateStr;
	}

	/***
	 * @description 获取 年字符串
	 * @param date
	 * @return
	 */
	public static String getYearStr(Date date) {
		String a = formatDate(date, "yyyy");
		return a;
	}

	/**
	 * @description 获取 年.. 整形
	 * @param date
	 * @return
	 */
	public static int getYear(Date date) {
		String a = formatDate(date, "yyyy");
		int b = Integer.parseInt(a);
		return b;
	}

	/**
	 * @description 获取 天字符串
	 * @param date
	 * @return
	 */
	public static String getDayStr(Date date) {
		String a = formatDate(date, "dd");
		return a;
	}

	/**
	 * @description 获取 天 整形
	 * @param date
	 * @return
	 */
	public static int getDay(Date date) {
		String a = formatDate(date, "dd");
		int b = Integer.parseInt(a);
		return b;
	}

	/***
	 * @description 获取 小时 整数型
	 * @param date
	 * @return
	 */
	public static int getHour(Date date) {
		String a = formatDate(date, "HH");
		int b = Integer.parseInt(a);
		return b;
	}

	/***
	 * @description 获取 小时 整数型
	 * @param date
	 * @return
	 */
	public static int getMinut(Date date) {
		String a = formatDate(date, "mm");
		int b = Integer.parseInt(a);
		return b;
	}

	/**
	 * @description 获取 月 字符串
	 * @param date
	 * @return
	 */
	public static String getMonthStr(Date date) {
		String a = formatDate(date, "MM");
		return a;
	}

	/**
	 * @description 获取 月 整数型
	 * @param date
	 * @return
	 */
	public static int getMonth(Date date) {
		String a = formatDate(date, "MM");
		int b = Integer.parseInt(a);
		return b;
	}

	/**
	 * @description 某天后 的几天
	 * @param d
	 * @param day
	 * @return
	 */
	public static Date getDateAfter(Date d, int day) {
		Calendar now = Calendar.getInstance();
		now.setTime(d);
		now.set(Calendar.DATE, now.get(Calendar.DATE) + day);
		return now.getTime();
	}

	/**
	 * @description 某天前的几天
	 * @param d
	 * @param day
	 * @return
	 */
	public static Date getDateBefore(Date d, int day) {
		Calendar now = Calendar.getInstance();
		now.setTime(d);
		now.set(Calendar.DATE, now.get(Calendar.DATE) - day);
		return now.getTime();

	}

	/**
	 * 取得当前时间
	 * 
	 * @return: yyyyMMdd HHmmss
	 */
	public static String getDateTimeForStr(Date date) {
		DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		return df.format(date);
	}

	/**
	 * 取得当前时间
	 * 
	 * @return: yyyyMMdd
	 */
	public static String getDateTime(Date date) {
		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		return df.format(date);
	}

	/**
	 * 两个时间之间 相间隔的天数
	 * 
	 * @description
	 * @param start
	 * @param end
	 * 
	 */
	public static Integer between(Date start, Date end) {
		if (start.getTime() >= end.getTime()) {
			return 0;
		}
		int i = 1;
		start = CommonDateUtil.getDate(CommonDateUtil.formatDate(start));
		end = CommonDateUtil.getDate(CommonDateUtil.formatDate(end));
		while (start.getTime() < end.getTime()) {
			start.setTime(start.getTime() + 24 * 60 * 60 * 1000);
			i++;
		}
		return i;
	}

	/**
	 * @description 两时间间隔月份数
	 * @param startDate
	 * @param endDate
	 */
	public static int minusMonths(Date startDate, Date endDate) {
		long months = (endDate.getTime() - startDate.getTime())
				/ (30 * 24 * 60 * 60 * 1000);
		return Integer.parseInt(Long.toString(months));
	}

	/**
	 * @description 两时间间隔的天数
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static int minusDays(Date startDate, Date endDate) {
		long times = (endDate.getTime() - startDate.getTime())
				/ (24 * 60 * 60 * 1000);
		return Integer.parseInt(Long.toString(times));
	}

	/**
	 * @description 两个时间 间隔的分钟数
	 * @param date1
	 *            前面小时间
	 * @param date2
	 *            后面大时间 congge 2012-4-23下午04:16:20
	 */
	public static int minusMinutes(Date date1, Date date2) {
		long times = (date2.getTime() - date1.getTime()) / (60 * 1000);
		return Integer.parseInt(Long.toString(times));
	}

	/**
	 * @description 两 时间间隔的周数
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static int minusWeeks(Date startDate, Date endDate) {
		return minusDays(startDate, endDate) / 7;
	}

	/**
	 * 获取某时间 在本周是第几天
	 * 
	 * @param date
	 *            Date
	 * @return int
	 */
	public static int getDayOfWeek(Date date) {
		Calendar calendar = Calendar.getInstance();
		if (date == null)
			date = new Date();
		calendar.setTime(date);
		return calendar.get(Calendar.DAY_OF_WEEK);
	}

	/***
	 * @description 返回 时间 是本周第几天
	 * @param date
	 * @return "一,二"
	 */
	public static String getDayOfWeekStr(Date date) {
		Calendar calendar = Calendar.getInstance();
		if (date == null)
			date = new Date();
		calendar.setTime(date);
		int tmp = calendar.get(Calendar.DAY_OF_WEEK);
		String str = "";
		switch (tmp) {
		case 1:
			str = "日";
			break;
		case 2:
			str = "一";
			break;
		case 3:
			str = "二";
			break;
		case 4:
			str = "三";
			break;
		case 5:
			str = "四";
			break;
		case 6:
			str = "五";
			break;
		case 7:
			str = "六";
			break;
		}
		return str;
	}

	// 必须是类似于12/01/09 12:11
	public static Date getTimeByStr(String value) {
		Date result = null;
		try {
			String zz = "[0-9]{1,2}/[0-9]{1,2}/[0-9]{2}//s*[0-9]{1,2}:[0-9]{1,2}";
			String zzS = "[0-9]{4}";
			if (value == null || value.equals(""))
				return null;
			int index = value.indexOf(" ");
			String time = value.substring(index + 1);
			String date = value.substring(0, index);
			String days[] = date.split("/");
			String timeStr = "20" + days[2] + "-" + days[0] + "-" + days[1]
					+ " " + time + ":00";
			result = CommonDateUtil.getDate(timeStr, "yyyy-MM-dd HH:mm:ss");
		} catch (Exception e) {
		}
		return result;
	}

	/**
	 * @description 得到一个时间延后或前移几天的时间,nowdate为时间,delay为前移或后延的天数
	 * @param nowdate
	 * @param delay
	 *            congge 2011-12-19上午09:06:01
	 */
	public static String getNextDay(String nowdate, String delay) {
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			String mdate = "";
			// Date d = strToDate(nowdate);
			Date d = format.parse(nowdate);
			long myTime = (d.getTime() / 1000) + Integer.parseInt(delay) * 24
					* 60 * 60;
			d.setTime(myTime * 1000);
			mdate = format.format(d);
			return mdate;
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * 将短时间格式字符串转换为时间 yyyy-MM-dd HH:mm:ss
	 * 
	 * @param strDate
	 * @return
	 */
	public static Date strToDate(String strDate) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		ParsePosition pos = new ParsePosition(0);
		Date strtodate = formatter.parse(strDate, pos);
		return strtodate;
	}

	/**
	 * 获取某一周指定的周几的时间 如上一周周三：week = -1,weekDay = 4(周一为第一天)
	 * 
	 * @param weekDay
	 * @param week
	 * @throws Exception
	 */
	public static String getOneDayOfWeek(int week, int weekDay)
			throws Exception {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.WEEK_OF_YEAR, week);
		int preNowWeekDay = c.get(Calendar.DAY_OF_WEEK);
		int preSubDays = weekDay - preNowWeekDay + 1;
		c.add(Calendar.DAY_OF_WEEK, preSubDays);
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		return sf.format(c.getTime());
	}

	/**
	 * 获取一天的开始时间
	 */
	public static Date getDayStart() {
		Calendar currentDate = new GregorianCalendar();
		currentDate.set(Calendar.HOUR_OF_DAY, 0);
		currentDate.set(Calendar.MINUTE, 0);
		currentDate.set(Calendar.SECOND, 0);
		Date d = (Date) currentDate.getTime().clone();
		return d;
	}

	/**
	 * 获取一天的结束时间
	 * 
	 * @return
	 */
	public static Date getDayEnd() {
		Calendar currentDate = new GregorianCalendar();
		currentDate.set(Calendar.HOUR_OF_DAY, 23);
		currentDate.set(Calendar.MINUTE, 59);
		currentDate.set(Calendar.SECOND, 59);
		Date d = (Date) currentDate.getTime().clone();
		return d;
	}

	/**
	 * @param date
	 * @return 指示当前年中的星期数
	 */
	public static int getWeekOfYear(Date date) {
		if (date == null)
			date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.WEEK_OF_YEAR);
	}

	/**
	 * 判断是否是否当天
	 * 
	 * @description
	 * @param inc
	 * @return
	 */
	public static boolean isToday(Date inDate) {
		Calendar now = Calendar.getInstance();
		Calendar inc = Calendar.getInstance();
		inc.setTime(inDate);
		if (now.get(Calendar.YEAR) == inc.get(Calendar.YEAR)
				&& now.get(Calendar.MONTH) == inc.get(Calendar.MONTH)
				&& now.get(Calendar.DAY_OF_MONTH) == inc
						.get(Calendar.DAY_OF_MONTH)) {
			return true;
		}
		return false;
	}

	/**
	 * 比较两个时间是否相等
	 * 
	 * @param d1
	 *            时间参数1
	 * @param d2
	 *            时间参数2
	 * @return 是否相等
	 */
	public static boolean compareTwoDate(Date d1, Date d2) {
		return d1.equals(d2);
	}

	/**
	 * 获取当前季节
	 */
	public static int getCurrentSeason() {
		Calendar c = Calendar.getInstance();
		int month = c.get(Calendar.MONTH);
		if (month >= 0 && month < 3) {
			return 1;
		} else if (month > 3 && month < 6) {
			return 2;
		} else if (month > 6 && month < 9) {
			return 3;
		}
		return 4;
	}

	/**
	 * @param date
	 *            Date 要比较的时间
	 * @param begin
	 *            Date 开始时间
	 * @param end
	 *            Date 结束时间
	 * @return boolean true表示在开始和结束时间之间
	 */
	public static boolean getBetweenTime(Date date, Date begin, Date end) {
		if (date == null || begin == null || end == null) {
			return false;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		Calendar beginDate = Calendar.getInstance();
		beginDate.setTime(begin);
		Calendar endDate = Calendar.getInstance();
		endDate.setTime(end);
		int month = cal.get(Calendar.MONTH) + 1;
		int beginMonth = beginDate.get(Calendar.MONTH) + 1;
		int endMonth = endDate.get(Calendar.MONTH) + 1;
		if (beginMonth == month || endMonth == month) {
			return true;
		} else if (beginMonth < month && month < endMonth) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 获取当天最大时间
	 * 
	 * @description
	 * @return 返回如下格式yyyy-MM-dd hh:mm:ss eg:2012.01.01 10:10:10
	 */
	public static String getMaxTimeOfToday() {
		// DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		// String ymd = sdf.format(Calendar.getInstance().getTime());
		// return ymd + " 23:59:59";
		return getMaxTimeByDay(Calendar.getInstance().getTime());
	}

	/**
	 * @description 根据日期获取最大时间
	 * @param date
	 *            日期
	 * @return
	 */
	public static String getMaxTimeByDay(Date date) {
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(date) + " 23:59:59";
	}

	/**
	 * 获取当天最小时间
	 * 
	 * @description
	 * @return
	 */
	public static String getMinTimeOfToday() {
		// DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		// String ymd = sdf.format(Calendar.getInstance().getTime());
		// return ymd + " 00:00:00";
		return getMinTimeByDay(Calendar.getInstance().getTime());
	}

	/**
	 * @description 根据日获取最小时间
	 * @param date
	 *            日期
	 * @return yanping 2012-5-12下午2:00:57
	 */
	public static String getMinTimeByDay(Date date) {
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(date) + " 00:00:00";
	}

	/**
	 * @description 比较时间
	 * @param Date1
	 * @param Date2
	 * @return
	 */
	public static int compareDate(String Date1, String Date2) {

		DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

		try {
			Date dt1 = df.parse(Date1);
			Date dt2 = df.parse(Date2);
			if (dt1.getTime() > dt2.getTime()) {
				return 1;
			} else if (dt1.getTime() < dt2.getTime()) {
				return -1;
			} else {
				return 0;
			}
		} catch (Exception exception) {
		}
		return 0;
	}

	/**
	 * @Description: 获取指定日期所在月的最后一天
	 * @author pangjianhai
	 * @date 2014-8-19 下午2:05:18
	 * @Date
	 * @param date
	 * @return
	 */
	public static Date lastDayOfMonth(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.roll(Calendar.DAY_OF_MONTH, -1);
		return cal.getTime();
	}

	/**
	 * 
	 * @Description: 获取指定日期最后一个月的数字（几号 28或30、31）
	 * @author pangjianhai
	 * @date 2014-8-19 下午2:09:46
	 * @int
	 * 
	 * @param date
	 * @return
	 */
	public static int lastDayNumOfMonth(Date date) {
		return getDay(lastDayOfMonth(date));
	}

	/**
	 * @Description: 两个日期相差多少年
	 * @author pangjianhai
	 * @int
	 * 
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static int getSpan(Date d1, Date d2) {
		int n = 0;
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		try {
			c1.setTime(d1);
			c2.setTime(d2);
		} catch (Exception e) {
		}
		while (!c1.after(c2)) {
			n++;
			c1.add(Calendar.DATE, 1); // 比较天数，日期+1
		}
		n = n - 1;
		return n = (int) n / 365;
	}

	public static void main(String[] args) {
		Date d1 = getDate("2008-07-06");
		Date d2 = getDate("2014-10-06");
	}
}