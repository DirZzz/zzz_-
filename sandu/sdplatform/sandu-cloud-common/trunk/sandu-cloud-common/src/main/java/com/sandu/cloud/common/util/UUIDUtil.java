package com.sandu.cloud.common.util;

import java.util.UUID;

public class UUIDUtil {

	public static String getUUID() {
		return UUID.randomUUID().toString().replace("-", "").toLowerCase();
	}
	
	public static void main(String[] args) {
		for(int i=0;i<6;i++) {
		System.out.println(getUUID());
		}
	}
}
