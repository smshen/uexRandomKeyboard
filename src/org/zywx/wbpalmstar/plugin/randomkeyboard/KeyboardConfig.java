package org.zywx.wbpalmstar.plugin.randomkeyboard;

import java.io.Serializable;

/**
 * 随机键盘插件的配置参数信息
 * 
 * @author yipeng.zhang
 * @createdAt 2014年7月23日
 */
public class KeyboardConfig implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3137479796925379085L;
	private int fontSize;
	private int fontColor;
	private String placeholderText;
	private int secureTextEntry;
	private String backgroundColor;
	private String randomKeyboard ;
	private String randomDigitalKeyboard;
	private String defaultKeyboard ;
	private String isSystemKeyboard;
	private String isTextRight;

	public KeyboardConfig(int fontSize, int fontColor, String placeholderText,
			int secureTextEntry, String backgroundColor,String randomKeyboard,
			String israndomKeyboardForNumber, String defaultKeyboard,String isSystemKeyboard,
			String isTextRight) {
		super();
		this.fontSize = fontSize;
		this.fontColor = fontColor;
		this.placeholderText = placeholderText;
		this.secureTextEntry = secureTextEntry;
		this.backgroundColor = backgroundColor;
		this.randomKeyboard=randomKeyboard;
		this.randomDigitalKeyboard = israndomKeyboardForNumber;
		this.defaultKeyboard=defaultKeyboard;
		this.isSystemKeyboard = isSystemKeyboard;
		this.isTextRight = isTextRight;
	}
	
	public String getIsTextRight() {
		return isTextRight;
	}

	public void setIsTextRight(String isTextRight) {
		this.isTextRight = isTextRight;
	}

	public String getIsSystemKeyboard() {
		return isSystemKeyboard;
	}

	public String getRandomDigitalKeyboard() {
	    return this.randomDigitalKeyboard;
	  }


	public int getFontSize() {
		return fontSize;
	}

	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
	}

	public int getFontColor() {
		return fontColor;
	}

	public void setFontColor(int fontColor) {
		this.fontColor = fontColor;
	}

	public String getPlaceholderText() {
		return placeholderText;
	}

	public void setPlaceholderText(String placeholderText) {
		this.placeholderText = placeholderText;
	}

	public int getSecureTextEntry() {
		return secureTextEntry;
	}

	public void setSecureTextEntry(int secureTextEntry) {
		this.secureTextEntry = secureTextEntry;
	}

	public String getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(String backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	public String getRandomKeyboard() {
		return randomKeyboard;
	}

	public void setRandomKeyboard(String randomKeyboard) {
		this.randomKeyboard = randomKeyboard;
	}

	public String getDefaultKeyboard() {
		return defaultKeyboard;
	}

	public void setDefaultKeyboard(String defaultKeyboard) {
		this.defaultKeyboard = defaultKeyboard;
	}
}
