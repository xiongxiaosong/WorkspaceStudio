package com.xxs.sdk.util;

import java.text.NumberFormat;

public class MathUtil {
	/**
	 * 将Float保留小数位的方法
	 * 
	 * @param f
	 *            传入的floatsh
	 * @param points
	 *            保留小数位数
	 */
	public static float saveFloatPointMethod(float f, int points) {
		NumberFormat ddf1 = NumberFormat.getNumberInstance();
		ddf1.setMaximumFractionDigits(points);
		String s = ddf1.format(f);
		return Float.valueOf(s.replaceAll(",", ""));
	}

	/**
	 * 将Double保留小数位的方法
	 * 
	 * @param f
	 *            传入的floatsh
	 * @param points
	 *            保留小数位数
	 */
	public static double saveLongPointMethod(double f, int points) {
		NumberFormat ddf1 = NumberFormat.getNumberInstance();
		ddf1.setMaximumFractionDigits(points);
		String s = ddf1.format(f);
		return Double.valueOf(s.replaceAll(",", ""));
	}

	/**
	 * 将传出的数字进行0-9的反转
	 * 
	 * @param num
	 *            传入的数字
	 * @return 反转后得到的数字
	 */
	public static int reverseone2ten(int num) {
		switch (num) {
		case 0:
			return 9;
		case 1:
			return 8;
		case 2:
			return 7;
		case 3:
			return 6;
		case 4:
			return 5;
		case 5:
			return 4;
		case 6:
			return 3;
		case 7:
			return 2;
		case 8:
			return 1;
		case 9:
			return 0;
		default:
			return -1;
		}
	}
}
