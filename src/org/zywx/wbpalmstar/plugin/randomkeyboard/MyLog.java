package org.zywx.wbpalmstar.plugin.randomkeyboard;

import android.util.Log;

/**
 * 可控制开关的log，用于测试
 * 
 * @author zyp
 * 
 */
public class MyLog {
	static boolean isLog = true;

	public static void i(String tag, String msg) {
		Log.i(tag, msg);
	}

	public static void w(String tag, String msg) {
		Log.w(tag, msg);
	}

	public static void e(String tag, String msg) {
		Log.e(tag, msg);
	}

	public static void v(String tag, String msg) {
		Log.v(tag, msg);
	}

	public static void d(String tag, String msg) {
		Log.d(tag, msg);
	}
}
