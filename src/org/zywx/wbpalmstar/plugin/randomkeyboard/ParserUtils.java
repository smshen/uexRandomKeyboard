package org.zywx.wbpalmstar.plugin.randomkeyboard;

import org.json.JSONException;
import org.json.JSONObject;
import org.zywx.wbpalmstar.base.BUtility;
import org.zywx.wbpalmstar.widgetone.dataservice.WWidgetData;

import android.content.Context;

public class ParserUtils {
	public static KeyboardConfig parseOpenJson(String jsonStr, Context context,
			WWidgetData wData) {
		KeyboardConfig data = null;
		try {
			JSONObject json = new JSONObject(jsonStr);
			String fontColor = json.getString("fontColor");
			String backgroundColor = json.optString("backgroundColor");
			int fontColorCode = BUtility.parseColor(fontColor);

			int fontSize = json.getInt("fontSize");
			String placeholderText = json.getString("placeholderText");
			int secureTextEntry = json.getInt("secureTextEntry");

			String randomLetterKeyboard = json.getString("randomLetterKeyboard");
		    String randomDigitalKeyboard = json.getString("randomDigitalKeyboard");
			String defaultKeyboard = json.getString("defaultKeyboard");
			String isSystemKeyboard = json.getString("isSystemKeyboard");
			String isTextRight = json.getString("isTextRight");
			
			data = new KeyboardConfig(fontSize, fontColorCode, placeholderText,secureTextEntry,
					backgroundColor,randomLetterKeyboard,randomDigitalKeyboard,defaultKeyboard,
					isSystemKeyboard,isTextRight);
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}

	/**
	 * 保留1位小数的方式取得中间刻度
	 * 
	 * @return
	 */
	public static String parseMidFloat(float src) {
		return String.valueOf(((int) ((src + 0.05) * 10)) / 10f);
	}

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * 将px值转换为sp值，保证文字大小不变
	 * 
	 * @param pxValue
	 * @param fontScale
	 *            （DisplayMetrics类中属性scaledDensity）
	 * @return
	 */
	public static int px2sp(Context context, float pxValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (pxValue / fontScale + 0.5f);
	}

	/**
	 * 将sp值转换为px值，保证文字大小不变
	 * 
	 * @param spValue
	 * @param fontScale
	 *            （DisplayMetrics类中属性scaledDensity）
	 * @return
	 */
	public static int sp2px(Context context, float spValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (spValue * fontScale + 0.5f);
	}

	public static int parseColor(String inColor) {
		int reColor = 0;
		try {
			if (inColor != null && inColor.length() != 0) {
				inColor = inColor.replace(" ", "");
				if (inColor.charAt(0) == 'r') { // rgba
					int start = inColor.indexOf('(') + 1;
					int off = inColor.indexOf(')');
					inColor = inColor.substring(start, off);
					String[] rgba = inColor.split(",");
					int r = Integer.parseInt(rgba[0]);
					int g = Integer.parseInt(rgba[1]);
					int b = Integer.parseInt(rgba[2]);
					int a = Integer.parseInt(rgba[3]);
					reColor = (a << 24) | (r << 16) | (g << 8) | b;
				} else { // #
					inColor = inColor.substring(1);
					if (3 == inColor.length()) {
						char[] t = new char[6];
						t[0] = inColor.charAt(0);
						t[1] = inColor.charAt(0);
						t[2] = inColor.charAt(1);
						t[3] = inColor.charAt(1);
						t[4] = inColor.charAt(2);
						t[5] = inColor.charAt(2);
						inColor = String.valueOf(t);
					} else if (6 == inColor.length()) {
						;
					}
					long color = Long.parseLong(inColor, 16);
					reColor = (int) (color | 0x00000000ff000000);
				}
			}
		} catch (Exception e) {
			;
		}
		return reColor;
	}
}
