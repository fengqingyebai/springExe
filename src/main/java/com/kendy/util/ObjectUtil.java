package com.kendy.util;

import java.util.Collection;

public class ObjectUtil {

	/**
	 * 是否其中一个为空
	 * @time 2018年6月30日
	 * @param objects
	 * @return
	 */
	public static boolean isAnyNull(Object... objects) {
		if(objects != null || objects.length > 0) {
			for(Object obj : objects) {
				if( obj == null) {
					return Boolean.TRUE;
				}
				if( obj instanceof Collection) {
					
				}
			}
		}
		return Boolean.FALSE;
	}
}
