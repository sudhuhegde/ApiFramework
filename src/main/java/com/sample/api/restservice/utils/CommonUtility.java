package com.sample.api.restservice.utils;

import java.security.SecureRandom;
import java.util.HashSet;
import java.util.List;

public class CommonUtility {

	public static long generateRandom(int length) {
		SecureRandom random = new SecureRandom();
		char[] digits = new char[length];
		digits[0] = (char) (random.nextInt(9) + '1');
		for (int i = 1; i < length; i++) {
			digits[i] = (char) (random.nextInt(10) + '0');
		}
		return Long.parseLong(new String(digits));
	}

	public static  <T> boolean listEqualsIgnoreOrder(List<T> list1, List<T> list2) {
		if(list1==null && list2==null) {
			return true;
		}
		if(list1!=null && list2 !=null) {
			if(list1.equals(list2)) {
				return true;
			}
			return ((list1.size() == list2.size()) && (new HashSet<>(list1).equals(new HashSet<>(list2))));
		}
		else {
			return false;
		}
	}
}
