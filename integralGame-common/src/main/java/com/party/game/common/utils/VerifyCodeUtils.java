package com.party.game.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Random;

/**
 * 核销码工具类
 * 
 * @author yifeng
 * @version 2016/7/14 0014
 */
public class VerifyCodeUtils {

	private static final Integer VERIFY_CODE_LENGTH = 8;

	protected static Logger logger = LoggerFactory.getLogger(VerifyCodeUtils.class);

	/**
	 * 获取核销码
	 *
	 * @param orderFormId
	 * @return
	 */
	public static String buildVerifyCode(String orderFormId) {
		// 生成随机数
		String random = RandomString(VERIFY_CODE_LENGTH);

		// 订单号加当前日期作为加密参数
		String keyset = orderFormId + DateUtils.getDays();

		String value = null;
		try {
			value = DesUtil.encrypt(random, keyset);
		} catch (Exception e) {
			logger.error("生成核销码异常{}", e);
		}

		if (StringUtils.isNotEmpty(value)) {
			String result = value.substring(0, VERIFY_CODE_LENGTH);
			return result;
		}
		return null;
	}

	/**
	 * s生成随机数
	 * 
	 * @param length
	 *            长度
	 * @return 随机数
	 */
	public static String RandomString(int length) {
		String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		Random random = new Random();
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int num = random.nextInt(62);
			buf.append(str.charAt(num));
		}
		return buf.toString();
	}

	public static void main(String[] args) {
		String value = VerifyCodeUtils.buildVerifyCode("123");
		System.out.println(value);
	}

	/**
	 * 生成随机单号 时间格式化+三位随机数
	 *
	 * @param targetDate
	 */
	public static String generateOrderNum(Date targetDate) {
		Date newDate = targetDate;
		if (newDate == null) {
			newDate = new Date();
		}
		String orderNum = DateUtils.formatDate(newDate, "yyyyMMddHHmmssSSS")
				+ (System.nanoTime() + "").substring(7, 10);
		return orderNum;
	}

	/**
	 * 生成整数随机数
	 *
	 * @param length 长度
	 * @return 随机数
	 */
	public static String RandomNumber(int length) {
		String str = "0123456789";
		Random random = new Random();
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int num = random.nextInt(9);
			buf.append(str.charAt(num));

		}
		return buf.toString();
	}
}
