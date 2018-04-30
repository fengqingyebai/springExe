package com.kendy.util;

import java.io.File;

import javax.swing.filechooser.FileSystemView;

public class PathUtil {

	public static void main(String[] args) {
		System.out.println("桌面路径是：" + getUserDeskPath());

	}
	
	/**
	 * 获取桌面路径
	 * @time 2018年4月18日
	 * @return
	 */
	public static String getUserDeskPath() {
    	String absolutePath = FileSystemView.getFileSystemView() .getHomeDirectory().getAbsolutePath();
    	return absolutePath + File.separator;
    }

}
