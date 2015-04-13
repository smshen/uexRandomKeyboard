package org.zywx.wbpalmstar.plugin.randomkeyboard;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.zywx.wbpalmstar.engine.universalex.EUExUtil;
import org.zywx.wbpalmstar.plugin.randomkeyboard.securityutils.CryptoKeyUtil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

/**
 * 键盘
 * 
 * @Description
 * @Author Lewis(lgs@yitong.com.cn) 2014年6月18日 下午8:21:07
 * @Class AllKeyBoard Copyright (c) 2014 Shanghai P&C Information Technology
 *        Co.,Ltd. All rights reserved.
 */
/**
 * 随机键盘
 * 
 * @author yipeng.zhang(yipeng.zhang@3g2win.com)
 * @createdAt 2014年7月25日
 */
public class AllKeyBoardMgr {

	private Activity activity;

	private static final String TAG = "AllKeyBoardMgr";

	public boolean isUpper = false;// 是否大写

	public boolean isAnimRunning = false;// 是否正在动画中
	
	private boolean isComplete = false;

	// private EditText inputFrame;

	private boolean isPwd;
	
	private boolean initInput = false;

	// private View menuView;

	// SZApplication application;

	// 默认字母键盘
	private int keyFlag = 1;

	RelativeLayout relativeLayoutKeyBoard;

	LinearLayout keypad_abc, key_board_sign, keypad_num,
			linearLayoutSignBoardOne, linearLayoutSignBoardTwo;

	private String keypadType;

	private Button btnNum_X, btnNumBoardChangeAbc, btnNumBoardPoint;

	private View bottomfirstview;

	private Button btnSignBoardUp, btnSignBoardDown;

	// 输入长度限制(默认15)
	private int inputMaxLength = 16;
	// 录入文本记录
	public String inputValue = "";

	// 是否为随机字母键盘
	private int randomLetterKeyboard;
	private int randomDigitalKeyboard;
	/**
	 * 输入加密
	 */
	private String cryptValue = "";
	private Button[] btnFunctions = new Button[17];
	private Button[] btnNumbs = new Button[10];
	private Button[] btnAbcs = new Button[26];
	private int[] randomAbcs = new int[26];
	private Button[] btnSigns = new Button[32];
	// private ImageButton btn = null;

	// 功能按钮
	private int[] keyFunctionIds = {
			EUExUtil.getResIdID("plugin_randomkeyboard_btnBoardCancle"),
			EUExUtil.getResIdID("plugin_randomkeyboard_btnAbcBoardAbcChange"),
			EUExUtil.getResIdID("plugin_randomkeyboard_btnAbcBoardDel"),
			EUExUtil.getResIdID("plugin_randomkeyboard_btnAbcBoardChangeNum"),
			EUExUtil.getResIdID("plugin_randomkeyboard_btnAbcBoardSpace"),
			EUExUtil.getResIdID("plugin_randomkeyboard_btnAbcBoardChangeSign"),
			EUExUtil.getResIdID("plugin_randomkeyboard_btnAbcBoardOk"),
			EUExUtil.getResIdID("plugin_randomkeyboard_btnNumBoardChangeAbc"),
			EUExUtil.getResIdID("plugin_randomkeyboard_btnNumBoardPoint"),
			EUExUtil.getResIdID("plugin_randomkeyboard_btnNumBoardDelet"),
			EUExUtil.getResIdID("plugin_randomkeyboard_btnSignBoardUp"),
			EUExUtil.getResIdID("plugin_randomkeyboard_btnSignBoardDown"),
			EUExUtil.getResIdID("plugin_randomkeyboard_btnSignBoardDel"),
			EUExUtil.getResIdID("plugin_randomkeyboard_btnSignBoardBack"),
			EUExUtil.getResIdID("plugin_randomkeyboard_btnNumBoardOk"), 
		    EUExUtil.getResIdID("plugin_randomkeyboard_btnNumBoard__key_pad_sign_14"), 
		    EUExUtil.getResIdID("plugin_randomkeyboard_btnNumBoardSpace")};

	// 数字按钮
	private int[] keyNumIds = {
			EUExUtil.getResIdID("plugin_randomkeyboard_key_board_num_zero"),
			EUExUtil.getResIdID("plugin_randomkeyboard_key_board_num_one"),
			EUExUtil.getResIdID("plugin_randomkeyboard_key_board_num_two"),
			EUExUtil.getResIdID("plugin_randomkeyboard_key_board_num_three"),
			EUExUtil.getResIdID("plugin_randomkeyboard_key_board_num_four"),
			EUExUtil.getResIdID("plugin_randomkeyboard_key_board_num_five"),
			EUExUtil.getResIdID("plugin_randomkeyboard_key_board_num_six"),
			EUExUtil.getResIdID("plugin_randomkeyboard_key_board_num_seven"),
			EUExUtil.getResIdID("plugin_randomkeyboard_key_board_num_eight"),
			EUExUtil.getResIdID("plugin_randomkeyboard_key_board_num_nine") };

	// 小写字母按钮
	private int[] keyAbcIds = {
			EUExUtil.getResIdID("plugin_randomkeyboard_key_board_abc_a"),
			EUExUtil.getResIdID("plugin_randomkeyboard_key_board_abc_b"),
			EUExUtil.getResIdID("plugin_randomkeyboard_key_board_abc_c"),
			EUExUtil.getResIdID("plugin_randomkeyboard_key_board_abc_d"),
			EUExUtil.getResIdID("plugin_randomkeyboard_key_board_abc_e"),
			EUExUtil.getResIdID("plugin_randomkeyboard_key_board_abc_f"),
			EUExUtil.getResIdID("plugin_randomkeyboard_key_board_abc_g"),
			EUExUtil.getResIdID("plugin_randomkeyboard_key_board_abc_h"),
			EUExUtil.getResIdID("plugin_randomkeyboard_key_board_abc_i"),
			EUExUtil.getResIdID("plugin_randomkeyboard_key_board_abc_j"),
			EUExUtil.getResIdID("plugin_randomkeyboard_key_board_abc_k"),
			EUExUtil.getResIdID("plugin_randomkeyboard_key_board_abc_l"),
			EUExUtil.getResIdID("plugin_randomkeyboard_key_board_abc_m"),
			EUExUtil.getResIdID("plugin_randomkeyboard_key_board_abc_n"),
			EUExUtil.getResIdID("plugin_randomkeyboard_key_board_abc_o"),
			EUExUtil.getResIdID("plugin_randomkeyboard_key_board_abc_p"),
			EUExUtil.getResIdID("plugin_randomkeyboard_key_board_abc_q"),
			EUExUtil.getResIdID("plugin_randomkeyboard_key_board_abc_r"),
			EUExUtil.getResIdID("plugin_randomkeyboard_key_board_abc_s"),
			EUExUtil.getResIdID("plugin_randomkeyboard_key_board_abc_t"),
			EUExUtil.getResIdID("plugin_randomkeyboard_key_board_abc_u"),
			EUExUtil.getResIdID("plugin_randomkeyboard_key_board_abc_v"),
			EUExUtil.getResIdID("plugin_randomkeyboard_key_board_abc_w"),
			EUExUtil.getResIdID("plugin_randomkeyboard_key_board_abc_x"),
			EUExUtil.getResIdID("plugin_randomkeyboard_key_board_abc_y"),
			EUExUtil.getResIdID("plugin_randomkeyboard_key_board_abc_z") };

	// 符号键盘
	private int[] keySignIds = {
			EUExUtil.getResIdID("plugin_randomkeyboard_key_pad_sign_1"),
			EUExUtil.getResIdID("plugin_randomkeyboard_key_pad_sign_2"),
			EUExUtil.getResIdID("plugin_randomkeyboard_key_pad_sign_3"),
			EUExUtil.getResIdID("plugin_randomkeyboard_key_pad_sign_4"),
			EUExUtil.getResIdID("plugin_randomkeyboard_key_pad_sign_5"),
			EUExUtil.getResIdID("plugin_randomkeyboard_key_pad_sign_6"),
			EUExUtil.getResIdID("plugin_randomkeyboard_key_pad_sign_7"),
			EUExUtil.getResIdID("plugin_randomkeyboard_key_pad_sign_8"),
			EUExUtil.getResIdID("plugin_randomkeyboard_key_pad_sign_9"),
			EUExUtil.getResIdID("plugin_randomkeyboard_key_pad_sign_10"),
			EUExUtil.getResIdID("plugin_randomkeyboard_key_pad_sign_11"),
			EUExUtil.getResIdID("plugin_randomkeyboard_key_pad_sign_12"),
			EUExUtil.getResIdID("plugin_randomkeyboard_key_pad_sign_13"),
			EUExUtil.getResIdID("plugin_randomkeyboard_key_pad_sign_14"),
			EUExUtil.getResIdID("plugin_randomkeyboard_key_pad_sign_15"),
			EUExUtil.getResIdID("plugin_randomkeyboard_key_pad_sign_16"),
			EUExUtil.getResIdID("plugin_randomkeyboard_key_pad_sign_17"),
			EUExUtil.getResIdID("plugin_randomkeyboard_key_pad_sign_18"),
			EUExUtil.getResIdID("plugin_randomkeyboard_key_pad_sign_19"),
			EUExUtil.getResIdID("plugin_randomkeyboard_key_pad_sign_20"),
			EUExUtil.getResIdID("plugin_randomkeyboard_key_pad_sign_21"),
			EUExUtil.getResIdID("plugin_randomkeyboard_key_pad_sign_22"),
			EUExUtil.getResIdID("plugin_randomkeyboard_key_pad_sign_23"),
			EUExUtil.getResIdID("plugin_randomkeyboard_key_pad_sign_24"),
			EUExUtil.getResIdID("plugin_randomkeyboard_key_pad_sign_25"),
			EUExUtil.getResIdID("plugin_randomkeyboard_key_pad_sign_26"),
			EUExUtil.getResIdID("plugin_randomkeyboard_key_pad_sign_27"),
			EUExUtil.getResIdID("plugin_randomkeyboard_key_pad_sign_28"),
			EUExUtil.getResIdID("plugin_randomkeyboard_key_pad_sign_29"),
			EUExUtil.getResIdID("plugin_randomkeyboard_key_pad_sign_30"),
			EUExUtil.getResIdID("plugin_randomkeyboard_key_pad_sign_31"),
			EUExUtil.getResIdID("plugin_randomkeyboard_key_pad_sign_32") };

	String[] keyAbcStrsLower = { "a", "b", "c", "d", "e", "f", "g", "h", "i",
			"j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v",
			"w", "x", "y", "z" };

	String[] keyAbsStrsUpper = { "A", "B", "C", "D", "E", "F", "G", "H", "I",
			"J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
			"W", "X", "Y", "Z" };

	// /**
	// * 用来初始化随机键盘管理器
	// *
	// * @param mContext
	// * 所在的Activity
	// * @param edit
	// * 密码输入框EditText
	// * @param isPassword
	// * 是否密文
	// * @param keyFlag
	// * 1 默认字母 2 默认数字 3 默认符号
	// * @param keypadType
	// * @param length
	// * 最大长度
	// */
	// public AllKeyBoardMgr(Context mContext, EditText edit, boolean
	// isPassword,
	// int keyFlag, String keypadType, int length) {
	// this.activity = (Activity) mContext;
	// this.inputFrame = edit;
	// this.isPwd = isPassword;
	// this.keypadType = keypadType;
	// this.inputMaxLength = length;
	//
	// // 获取控件对象
	// findKeyBoardView();
	// this.keyFlag = keyFlag;
	// if (keyFlag == 1) {
	// randomKeyAbc();
	// } else if (keyFlag == 2) {
	// randomKeyNum();
	// } else if (keyFlag == 3) {
	// randomKeySign();
	// } else {
	// randomKeyAbc();
	// }
	//
	// // if (isPwd) {
	// // ed.setInputType(InputType.TYPE_CLASS_TEXT
	// // | InputType.TYPE_TEXT_VARIATION_PASSWORD);
	// // } else {
	// // ed.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
	// // }
	//
	// // 切换输入框时隐藏键盘
	// this.inputFrame.setOnFocusChangeListener(new OnFocusChangeListener() {
	// @Override
	// public void onFocusChange(View v, boolean hasFocus) {
	// if (!hasFocus) {
	// hideKeyboard();
	// }
	// }
	// });
	//
	// // 设置弹出键盘事件
	// this.inputFrame.setOnTouchListener(new OnTouchListener() {
	// @Override
	// public boolean onTouch(View v, MotionEvent event) {
	// inputFrame.requestFocus();
	// inputFrame.requestFocusFromTouch();
	// inputFrame.setCursorVisible(true);// 显示光标
	// switch (event.getAction()) {
	// case MotionEvent.ACTION_DOWN:
	// if (Build.VERSION.SDK_INT >= 11) {
	// Method setShowSoftInputOnFocus = null;
	// try {
	// setShowSoftInputOnFocus = inputFrame.getClass()
	// .getMethod("setShowSoftInputOnFocus",
	// boolean.class);
	// setShowSoftInputOnFocus.setAccessible(true);
	// setShowSoftInputOnFocus.invoke(inputFrame, false);
	// } catch (SecurityException e) {
	// e.printStackTrace();
	// } catch (NoSuchMethodException e) {
	// e.printStackTrace();
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// } else {
	// inputFrame.setInputType(InputType.TYPE_NULL);
	// }
	// InputMethodManager imm = (InputMethodManager) activity
	// .getSystemService(Context.INPUT_METHOD_SERVICE);
	// imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
	// showKeyboard();
	// return true;
	// }
	// return true;
	// }
	// });
	// }

	/**
	 * 用来初始化随机键盘管理器
	 * 
	 * @param mContext
	 *            所在的Activity
	 * @param edit
	 *            密码输入框EditText
	 * @param isPassword
	 *            是否密文
	 * @param keyFlag
	 *            1 默认字母 2 默认数字 3 默认符号
	 * @param keypadType
	 * @param length
	 *            最大长度
	 * @param inputValue
	 *            输入框显示的内容
	 * @param encryptValue
	 *            输入框加密后的内容（如果是密文）
	 */
	public AllKeyBoardMgr(Context mContext, boolean isPassword, int keyFlag,
			String keypadType, int length, String inputValue,
			String encryptValue, int randomLetterKeyboard, int randomDigitalKeyboard,String initData) {

		this.activity = (Activity) mContext;
		this.isPwd = isPassword;
		this.keypadType = keypadType;
		this.inputMaxLength = length;

		this.inputValue = inputValue;
		this.cryptValue = encryptValue;
		this.randomLetterKeyboard = randomLetterKeyboard;
		this.randomDigitalKeyboard = randomDigitalKeyboard;
		

		// 获取控件对象
		findKeyBoardView();

		this.keyFlag = keyFlag;
		randomAbcs = setUpRadomLetterKeyBoard();
		if (keyFlag == 1) {
			randomKeyNum();
		} else if (keyFlag == 2) {
			// if(randomKeyboard==0){
			// randomKeyAbc();
			// }else{
			randomKeyAbc();
			// }
			
		} else if (keyFlag == 3) {
			randomKeySign();
		} else {
			randomKeyAbc();
		}
		initInput = true;
		if(null != initData){
			for(int i = 0; i < initData.length(); i++){
				setValue(initData.substring(i, i+1));
			}
		}
		initInput = false;

	}

	/**
	 * 初始化随机键盘的view
	 */
	private void findKeyBoardView() {
		MyLog.i(TAG, "findKeyboardView");
		if (activity.getParent() != null
				&& activity.getParent().getParent() != null) {

			bottomfirstview = activity
					.getParent()
					.getParent()
					.findViewById(
							EUExUtil.getResIdID("plugin_randomkeyboard_bottomfirstview"));
			btnNum_X = (Button) activity
					.getParent()
					.getParent()
					.findViewById(
							EUExUtil.getResIdID("plugin_randomkeyboard_btnNum_X"));
			btnNum_X.setOnClickListener(keyBoardInputClickListener);
			btnNumBoardChangeAbc = (Button) activity
					.getParent()
					.getParent()
					.findViewById(
							EUExUtil.getResIdID("plugin_randomkeyboard_btnNumBoardChangeAbc"));
			btnNumBoardPoint = (Button) activity
					.getParent()
					.getParent()
					.findViewById(
							EUExUtil.getResIdID("plugin_randomkeyboard_btnNumBoardPoint"));

			relativeLayoutKeyBoard = (RelativeLayout) activity
					.getParent()
					.getParent()
					.findViewById(
							EUExUtil.getResIdID("plugin_randomkeyboard_linearLayoutKeyBoard"));

			keypad_abc = (LinearLayout) activity
					.getParent()
					.getParent()
					.findViewById(
							EUExUtil.getResIdID("plugin_randomkeyboard_keypad_abc"));

			key_board_sign = (LinearLayout) activity
					.getParent()
					.getParent()
					.findViewById(
							EUExUtil.getResIdID("plugin_randomkeyboard_key_board_sign"));

			keypad_num = (LinearLayout) activity
					.getParent()
					.getParent()
					.findViewById(
							EUExUtil.getResIdID("plugin_randomkeyboard_keypad_num"));

			btnSignBoardUp = (Button) activity
					.getParent()
					.getParent()
					.findViewById(
							EUExUtil.getResIdID("plugin_randomkeyboard_btnSignBoardUp"));
			btnSignBoardDown = (Button) activity
					.getParent()
					.getParent()
					.findViewById(
							EUExUtil.getResIdID("plugin_randomkeyboard_btnSignBoardDown"));
			linearLayoutSignBoardOne = (LinearLayout) activity
					.getParent()
					.getParent()
					.findViewById(
							EUExUtil.getResIdID("plugin_randomkeyboard_linearLayoutSignBoardOne"));
			linearLayoutSignBoardTwo = (LinearLayout) activity
					.getParent()
					.getParent()
					.findViewById(
							EUExUtil.getResIdID("plugin_randomkeyboard_linearLayoutSignBoardTwo"));

			for (int i = 0; i < btnFunctions.length; i++) {
				btnFunctions[i] = (Button) activity.getParent().getParent()
						.findViewById(keyFunctionIds[i]);
				btnFunctions[i]
						.setOnClickListener(keyBoardFunctionClickListener);
			}
			btnSignBoardUp.setBackgroundResource(EUExUtil
					.getResDrawableID("plugin_randomkeyboard_key_up_bga"));
			btnSignBoardUp.setClickable(false);
			btnSignBoardDown.setBackgroundResource(EUExUtil
					.getResDrawableID("plugin_randomkeyboard_key_down_bga3"));
			btnSignBoardDown.setClickable(true);

			for (int i = 0; i < btnNumbs.length; i++) {
				btnNumbs[i] = (Button) activity.getParent().getParent()
						.findViewById(keyNumIds[i]);
				if (null != keypadType && keypadType.equals("11")) {
					btnNumbs[i].setOnClickListener(digitPadClickListener);
				} else {
					btnNumbs[i].setOnClickListener(keyBoardInputClickListener);
				}
			}

			for (int i = 0; i < btnAbcs.length; i++) {
				// setUpRadomLetterKeyBoard();
				btnAbcs[i] = (Button) activity.getParent().getParent()
						.findViewById(keyAbcIds[i]);
				btnAbcs[i].setOnClickListener(keyBoardInputClickListener);
			}
			for (int i = 0; i < btnSigns.length; i++) {
				btnSigns[i] = (Button) activity.getParent().getParent()
						.findViewById(keySignIds[i]);
				btnSigns[i].setOnClickListener(keyBoardInputClickListener);
			}
		} else {
			bottomfirstview = activity.findViewById(EUExUtil
					.getResIdID("plugin_randomkeyboard_bottomfirstview"));
			btnNum_X = (Button) activity.findViewById(EUExUtil
					.getResIdID("plugin_randomkeyboard_btnNum_X"));
			btnNum_X.setOnClickListener(keyBoardInputClickListener);
			btnNumBoardChangeAbc = (Button) activity.findViewById(EUExUtil
					.getResIdID("plugin_randomkeyboard_btnNumBoardChangeAbc"));
			relativeLayoutKeyBoard = (RelativeLayout) activity
					.findViewById(EUExUtil
							.getResIdID("plugin_randomkeyboard_linearLayoutKeyBoard"));

			keypad_abc = (LinearLayout) activity.findViewById(EUExUtil
					.getResIdID("plugin_randomkeyboard_keypad_abc"));

			key_board_sign = (LinearLayout) activity.findViewById(EUExUtil
					.getResIdID("plugin_randomkeyboard_key_board_sign"));

			btnNumBoardPoint = (Button) activity.findViewById(EUExUtil
					.getResIdID("plugin_randomkeyboard_btnNumBoardPoint"));

			keypad_num = (LinearLayout) activity.findViewById(EUExUtil
					.getResIdID("plugin_randomkeyboard_keypad_num"));

			linearLayoutSignBoardOne = (LinearLayout) activity
					.findViewById(EUExUtil
							.getResIdID("plugin_randomkeyboard_linearLayoutSignBoardOne"));
			linearLayoutSignBoardTwo = (LinearLayout) activity
					.findViewById(EUExUtil
							.getResIdID("plugin_randomkeyboard_linearLayoutSignBoardTwo"));
			btnSignBoardUp = (Button) activity.findViewById(EUExUtil
					.getResIdID("plugin_randomkeyboard_btnSignBoardUp"));
			btnSignBoardDown = (Button) activity.findViewById(EUExUtil
					.getResIdID("plugin_randomkeyboard_btnSignBoardDown"));

			for (int i = 0; i < btnFunctions.length; i++) {
				btnFunctions[i] = (Button) activity
						.findViewById(keyFunctionIds[i]);
				if (btnFunctions[i] != null) {
					btnFunctions[i]
							.setOnClickListener(keyBoardFunctionClickListener);
				}
			}
			btnSignBoardUp.setBackgroundResource(EUExUtil
					.getResDrawableID("plugin_randomkeyboard_key_up_bga"));
			btnSignBoardUp.setClickable(false);
			btnSignBoardDown.setBackgroundResource(EUExUtil
					.getResDrawableID("plugin_randomkeyboard_key_down_bga3"));
			btnSignBoardDown.setClickable(true);

			for (int i = 0; i < btnNumbs.length; i++) {
				btnNumbs[i] = (Button) activity.findViewById(keyNumIds[i]);
				if (btnNumbs[i] != null) {
					// 金额键盘的监听
					if (null != keypadType && keypadType.equals("11")) {
						btnNumbs[i].setOnClickListener(digitPadClickListener);
					} else {
						btnNumbs[i]
								.setOnClickListener(keyBoardInputClickListener);
					}
				}
			}

			for (int i = 0; i < btnAbcs.length; i++) {
				btnAbcs[i] = (Button) activity.findViewById(keyAbcIds[i]);
				if (btnAbcs[i] != null) {
					btnAbcs[i].setOnClickListener(keyBoardInputClickListener);
				}
			}
			for (int i = 0; i < btnSigns.length; i++) {
				btnSigns[i] = (Button) activity.findViewById(keySignIds[i]);
				if (btnSigns[i] != null) {
					btnSigns[i].setOnClickListener(keyBoardInputClickListener);
				}
			}
		}
	}

	/**
	 * 更新输入的内容
	 * 
	 * @Description
	 * @version 3.0 2014-8-1
	 */
	private void setValue(String inputStr) {
		if (inputValue.length() >= inputMaxLength) {
			MyLog.w(TAG, "input content is over maxLength");
			return;
		}

		if (isPwd) {
			inputValue = inputValue + "*";
			// inputFrame.setText(inputValue);
			cryptValue = CryptoKeyUtil.encrypt(new StringBuffer(cryptValue),
					inputStr).toString();
			// inputFrame.setTag(cryptValue);
			// encryptionPwd(inputValue);
		} else {
			inputValue = inputValue + inputStr;
			// inputFrame.setText(inputValue);
		}
		EUExRandomKeyboard.getPluginInstance().changeInputContent(inputValue,
				cryptValue);
		
		if (!initInput) {
			String result = getInputContent();
			if (result == null) {
				result = "";
			}
			Intent intent = new Intent();
			intent.putExtra("content", result);
			intent.putExtra("isOk", false);
			EUExRandomKeyboard.getPluginInstance().onResult(intent);
		}
		// Selection.setSelection(inputFrame.getText(), inputFrame.getText()
		// .length());// 移动光标到最右
	}

	/**
	 * 删除输入的内容
	 * 
	 * @Description
	 * @version 3.0 2014-8-1
	 * 
	 */
	private void delValue() {
		if (inputValue.length() <= 0) {
			MyLog.w(TAG, "no content to delete");
			return;
		}
		inputValue = inputValue.substring(0, inputValue.length() - 1);
		if (isPwd) {
			cryptValue = CryptoKeyUtil.delDecrypt(new StringBuffer(cryptValue))
					.toString();
			// inputFrame.setText(inputValue);
			// inputFrame.setTag(cryptValue);
			// encryptionPwd(inputValue);
		} else {
			// inputFrame.setText(inputValue);
		}
		EUExRandomKeyboard.getPluginInstance().changeInputContent(inputValue,
				cryptValue);
		
		String result = getInputContent();
		if (result == null) {
			result = "";
		}
		Intent intent = new Intent();
		intent.putExtra("content", result);
		intent.putExtra("isOk", false);
		EUExRandomKeyboard.getPluginInstance().onResult(intent);
		// Selection.setSelection(inputFrame.getText(), inputFrame.getText()
		// .length());// 移动光标

	}

	/**
	 * 清空内容
	 */
	private void clear() {
		inputValue = "";

		if (isPwd) {
			// inputFrame.setText("");
			// inputFrame.setTag("");
			// encryptionPwd(inputValue);
		} else {
			// inputFrame.setText(inputValue);
		}
		EUExRandomKeyboard.getPluginInstance().changeInputContent(inputValue,
				cryptValue);
		
		String result = getInputContent();
		if (result == null) {
			result = "";
		}
		Intent intent = new Intent();
		intent.putExtra("content", result);
		intent.putExtra("isOk", false);
		EUExRandomKeyboard.getPluginInstance().onResult(intent);
	}

	/**
	 * 获取输入内容
	 * 
	 * @return
	 */
	private String getInputContent() {
		if (!isPwd) {
			return inputValue;
		} else {
			String inputValue = TextUtils.isEmpty(cryptValue) ? ""
					: CryptoKeyUtil.decrypt(cryptValue).toString();
			return inputValue;
		}
	}

	/**
	 * 密码加密 dmq
	 */
	public static String encryPwd;// 加密后的密码

	// public void encryptionPwd(String pwd) {
	// MyLog.i(TAG, "pwd:" + pwd);
	// if (isPwd) {
	// try {
	// // 1.客户端证书加密，后台解密
	// //
	// encryPwd=RSACerPlus.getInstance(activity.getApplication()).doEncrypt(pwd);
	//
	// // 2.AES客户端加密、客户端解密
	// // Key k = AESCoder.toKey("1234567890123456".getBytes());
	// // byte[] encryptData = AESCoder.encrypt(pwd.getBytes(), k);
	// // encryPwd = Converts.base64ToString(encryptData);
	// // 解密实现,登陆上送报文时使用此操作
	// // byte[] decryptData =
	// // AESCoder.decrypt(Converts.strToBase64(encryPwd), k);
	// // String pwdStr = new String(decryptData);
	//
	// // 3.MD5加密
	// encryPwd = MD5.md5(pwd);
	//
	// MyLog.v(TAG, "encryPwd:" + encryPwd);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	// }

	/**
	 * 将密码替换成"*" dmq
	 */
	StringBuffer replace;

	public String replacePwd(String pwd) {
		replace = new StringBuffer();
		for (int i = 0; i < pwd.length(); i++) {
			replace.append("*");
		}
		return replace.toString();
	}

	// 金额键盘的监听
	private OnClickListener digitPadClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Button inputBtn = (Button) v;
			// 拼接录入值
			setValue(inputBtn.getText().toString());
		}
	};

	// 输入监听
	private OnClickListener keyBoardInputClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			MyLog.v(TAG, "inputLength:" + inputMaxLength);
			Button inputBtn = (Button) v;
			// 拼接录入值
			setValue(inputBtn.getText().toString());
		}
	};

	// 功能监听
	private OnClickListener keyBoardFunctionClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {

			if (v.getId() == EUExUtil
					.getResIdID("plugin_randomkeyboard_btnBoardCancle")) {
				hideKeyboard();
			} else if (v.getId() == EUExUtil
					.getResIdID("plugin_randomkeyboard_btnAbcBoardAbcChange")) {
				changeKey();
				//randomKeyAbc();
			} else if (v.getId() == EUExUtil
					.getResIdID("plugin_randomkeyboard_btnAbcBoardDel")) {
				delValue();
			} else if (v.getId() == EUExUtil
					.getResIdID("plugin_randomkeyboard_btnAbcBoardChangeNum")) {
				randomKeyNum();
			} else if (v.getId() == EUExUtil
					.getResIdID("plugin_randomkeyboard_btnAbcBoardSpace")) {
				setValue(" ");
			} else if (v.getId() == EUExUtil
					.getResIdID("plugin_randomkeyboard_btnAbcBoardChangeSign")) {
				randomKeySign();
			} else if (v.getId() == EUExUtil
					.getResIdID("plugin_randomkeyboard_btnAbcBoardOk")) {
				isComplete = true;
				hideKeyboard();
				if (keypadType.equals("100")) {
					// LoginActivity loginActivity = (LoginActivity) activity;
					// loginActivity.loginAction();
				}
			} else if (v.getId() == EUExUtil
					.getResIdID("plugin_randomkeyboard_btnNumBoardChangeAbc")) {
				randomAbcs = setUpRadomLetterKeyBoard();
				randomKeyAbc();
			} else if (v.getId() == EUExUtil
					.getResIdID("plugin_randomkeyboard_btnNumBoardPoint")) {
				// 点赋值
				setValue(activity
						.getResources()
						.getString(
								EUExUtil.getResStringID("plugin_randomkeyboard_char_46")));
			} else if (v.getId() == EUExUtil
					.getResIdID("plugin_randomkeyboard_btnNumBoardDelet")) {
				delValue();
			} else if (v.getId() == EUExUtil
					.getResIdID("plugin_randomkeyboard_btnSignBoardUp")) {
				// 这里需要增加切换动画
				Animation animation = AnimationUtils
						.loadAnimation(
								activity,
								EUExUtil.getResAnimID("plugin_randomkeyboard_dialog_enter"));
				Animation animation2 = AnimationUtils
						.loadAnimation(
								activity,
								EUExUtil.getResAnimID("plugin_randomkeyboard_dialog_enter_2"));
				linearLayoutSignBoardOne.setVisibility(View.VISIBLE);
				linearLayoutSignBoardOne.startAnimation(animation);

				linearLayoutSignBoardTwo.setVisibility(View.GONE);
				linearLayoutSignBoardTwo.startAnimation(animation2);
				btnSignBoardUp.setBackgroundResource(EUExUtil
						.getResDrawableID("plugin_randomkeyboard_key_up_bga"));
				btnSignBoardUp.setClickable(false);
				btnSignBoardDown
						.setBackgroundResource(EUExUtil
								.getResDrawableID("plugin_randomkeyboard_key_down_bga3"));
				btnSignBoardDown.setClickable(true);
			} else if (v.getId() == EUExUtil
					.getResIdID("plugin_randomkeyboard_btnSignBoardDown")) {
				// 这里需要增加切换动画
				Animation animation1 = AnimationUtils
						.loadAnimation(
								activity,
								EUExUtil.getResAnimID("plugin_randomkeyboard_dialog_exit"));
				Animation animation3 = AnimationUtils
						.loadAnimation(
								activity,
								EUExUtil.getResAnimID("plugin_randomkeyboard_dialog_exit_2"));
				linearLayoutSignBoardOne.setVisibility(View.GONE);
				linearLayoutSignBoardOne.startAnimation(animation1);
				linearLayoutSignBoardTwo.setVisibility(View.VISIBLE);
				linearLayoutSignBoardTwo.startAnimation(animation3);
				btnSignBoardUp.setBackgroundResource(EUExUtil
						.getResDrawableID("plugin_randomkeyboard_key_up_bga3"));
				btnSignBoardUp.setClickable(true);
				btnSignBoardDown
						.setBackgroundResource(EUExUtil
								.getResDrawableID("plugin_randomkeyboard_key_down_bga"));
				btnSignBoardDown.setClickable(false);
			} else if (v.getId() == EUExUtil
					.getResIdID("plugin_randomkeyboard_btnSignBoardDel")) {
				delValue();
			} else if (v.getId() == EUExUtil
					.getResIdID("plugin_randomkeyboard_btnSignBoardBack")) {
				randomAbcs = setUpRadomLetterKeyBoard();
				randomKeyAbc();
			} 
			else if (v.getId() == EUExUtil
					.getResIdID("plugin_randomkeyboard_btnNumBoardOk")) {
				isComplete = true;
				hideKeyboard();
				keypadType.equals("100");
			} else if (v.getId() == EUExUtil
					.getResIdID("plugin_randomkeyboard_btnNumBoard__key_pad_sign_14")) {
				setValue(activity.getResources()
								.getString(
										EUExUtil.getResStringID("plugin_randomkeyboard_char_37")));
			} else if (v.getId() == EUExUtil
					.getResIdID("plugin_randomkeyboard_btnNumBoardSpace")) {
				setValue(" ");
			}
		}
	};

	/**
	 * 键盘大小写切换 这里需要进行加密处理
	 */
	@SuppressLint("DefaultLocale")
	private void changeKey() {
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (isUpper) {// 大写切换小写
					isUpper = false;
					Log.i("btnAbcs", btnAbcs.length + "--------");
					for (int i = 0; i < btnAbcs.length; i++) {
						if (randomLetterKeyboard == 0) {
							btnAbcs[i].setText(keyAbcStrsLower[randomAbcs[i]]);
						} else {
							btnAbcs[i].setText(btnAbcs[i].getText().toString()
									.toLowerCase());
						}
					}
					// 设置背景为大写 这里需要补充设置小写背景
					btnFunctions[1].setBackgroundResource(EUExUtil
							.getResDrawableID("plugin_randomkeyboard_key_shift_bg"));
				} else {// 小写切换大写
					isUpper = true;
					for (int i = 0; i < btnAbcs.length; i++) {
						if (randomLetterKeyboard == 0) {
							btnAbcs[i].setText(btnAbcs[i].getText().toString()
									.toUpperCase());
						} else {
							btnAbcs[i].setText(btnAbcs[i].getText().toString()
									.toUpperCase());
						}
					}
					// 设置背景为大写 这里需要补充设置大写背景
					btnFunctions[1].setBackgroundResource(EUExUtil
							.getResDrawableID("plugin_randomkeyboard_key_shift_bg2"));
				}
			}
		});
	}

	// public void showKeyboard(int inputLength) {
	// this.inputMaxLength = inputLength;
	// // 每次打开键盘清空值
	// inputValue = "";
	// activity.runOnUiThread(new Runnable() {
	// @Override
	// public void run() {
	// if (keyFlag == 1) {
	// randomKeyAbc();
	// } else if (keyFlag == 2) {
	// randomKeyNum();
	// } else if (keyFlag == 3) {
	// randomKeySign();
	// } else {
	// randomKeyAbc();
	// }
	// int visibility = relativeLayoutKeyBoard.getVisibility();
	// if (visibility == View.GONE || visibility == View.INVISIBLE) {
	// Animation animation = AnimationUtils.loadAnimation(
	// activity,
	// EUExUtil.getResAnimID("plugin_randomkeyboard_dialog_enter"));
	// relativeLayoutKeyBoard.startAnimation(animation);
	// relativeLayoutKeyBoard.setVisibility(View.VISIBLE);
	// }
	// }
	// });
	// }

	/**
	 * 显示键盘视图
	 */
	public void showKeyboard() {
		if (isAnimRunning) {
			Log.i(TAG, "Animation is Running, cannot run any operations!");
			return;
		}
		if (keyFlag == 1) {
			randomKeyNum();
		} else if (keyFlag == 2) {
			randomKeyAbc();
		} else if (keyFlag == 3) {
			randomKeySign();
		} else {
			randomKeyAbc();
		}
		int visibility = relativeLayoutKeyBoard.getVisibility();
		if (visibility == View.GONE || visibility == View.INVISIBLE) {
			// TODO 读取动画id的方法
			// Animation animation = AnimationUtils.loadAnimation(activity,
			// EUExUtil.getResAnimID("plugin_randomkeyboard_in"));
			Animation animation = AnimationUtils
					.loadAnimation(activity, EUExUtil
							.getResAnimID("plugin_randomkeyboard_dialog_enter"));
			animation.setAnimationListener(new AnimationListener() {

				@Override
				public void onAnimationStart(Animation animation) {
					Log.i(TAG, "onAnimationStart");
					isAnimRunning = true;
					relativeLayoutKeyBoard.setVisibility(View.VISIBLE);
				}

				@Override
				public void onAnimationRepeat(Animation animation) {

				}

				@Override
				public void onAnimationEnd(Animation animation) {
					Log.i(TAG, "onAnimationEnd");
					//int height = relativeLayoutKeyBoard.getHeight();
					int height = relativeLayoutKeyBoard.getHeight();
					Log.i("relativeLayoutKeyBoard", "height-----"+height);
					Log.i(TAG, "keyboard height" + height);
					EUExRandomKeyboard.getPluginInstance().onShowKeyboard(
							height);
					isAnimRunning = false;
				}
			});
			relativeLayoutKeyBoard.setVisibility(View.VISIBLE);
			relativeLayoutKeyBoard.startAnimation(animation);
		}
		// if (inputFrame.getTag() == null
		// || inputFrame.getTag().toString() == null) {
		// cryptValue = "";
		// } else {
		// cryptValue = inputFrame.getTag().toString();
		// }
	}

	/**
	 * 隐藏键盘视图
	 */
	public void hideKeyboard() {
		if (isAnimRunning) {
			Log.i(TAG, "Animation is Running, cannot run any operations!");
			return;
		}
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				int visibility = relativeLayoutKeyBoard.getVisibility();
				if (visibility == View.VISIBLE) {
					/*
					 * Animation animation = AnimationUtils.loadAnimation(
					 * activity, R.anim.dialog_exit);
					 * linearLayoutKeyBoard.startAnimation(animation);
					 */
					// Animation animation = AnimationUtils.loadAnimation(
					// activity,
					// EUExUtil.getResAnimID("plugin_randomkeyboard_out"));
					Animation animation = AnimationUtils.loadAnimation(
							activity,
							EUExUtil.getResAnimID("plugin_randomkeyboard_dialog_exit"));
					animation.setAnimationListener(new AnimationListener() {

						@Override
						public void onAnimationStart(Animation animation) {
							isAnimRunning = true;
							int height = relativeLayoutKeyBoard.getHeight();
							Log.i(TAG, height + "");
							EUExRandomKeyboard.getPluginInstance()
									.onHideKeyboard(height);
						}

						@Override
						public void onAnimationRepeat(Animation animation) {
						}

						@Override
						public void onAnimationEnd(Animation animation) {
							isAnimRunning = false;
							relativeLayoutKeyBoard.setVisibility(View.GONE);
							String result = getInputContent();
							if (result == null) {
								result = "";
							}
							Intent intent = new Intent();
							intent.putExtra("content", result);
							intent.putExtra("isOk", isComplete);
							
							isComplete = false;
//							activity.setResult(0, intent);
//							activity.finish();
							EUExRandomKeyboard.getPluginInstance()
							.onResult(intent);
							
							EUExRandomKeyboard.getPluginInstance()
							.onRemoveKeyboard();
						}
					});

					relativeLayoutKeyBoard.startAnimation(animation);
					// EUExRandomKeyboard.getPluginInstance().onGetConent(result);
				}
			}
		});
	}

	/**
	 * 键盘是否显示
	 * 
	 * @Description
	 * @return
	 * @author 孙靖
	 * @version 1.0 2013年12月29日
	 */
	public boolean isKeyBoardShow() {
		if (null != relativeLayoutKeyBoard) {
			int visibility = relativeLayoutKeyBoard.getVisibility();
			if (visibility == View.VISIBLE) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 加密和随机数字键盘
	 * 
	 * @Description
	 * @author 孙靖
	 * @version 1.0 2013年12月30日
	 */
	private void randomKeyNum() {

		activity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// 非密码 不随机
				if (randomDigitalKeyboard == 1) {
					if (activity.getParent() != null
							&& activity.getParent().getParent() != null) {
						for (int i = 0; i < btnNumbs.length; i++) {
							btnNumbs[i] = (Button) activity.getParent()
									.getParent().findViewById(keyNumIds[i]);
							btnNumbs[i].setText(i + "");
							if (null != keypadType && keypadType.equals("11")) {
								btnNumbs[i]
										.setOnClickListener(digitPadClickListener);
							} else {
								btnNumbs[i]
										.setOnClickListener(keyBoardInputClickListener);
							}
						}
					} else {
						for (int i = 0; i < btnNumbs.length; i++) {
							btnNumbs[i] = (Button) activity
									.findViewById(keyNumIds[i]);
							btnNumbs[i].setText(i + "");
							if (btnNumbs[i] != null) {
								// 金额键盘的监听
								if (null != keypadType
										&& keypadType.equals("11")) {
									btnNumbs[i]
											.setOnClickListener(digitPadClickListener);
								} else {
									btnNumbs[i]
											.setOnClickListener(keyBoardInputClickListener);
								}
							}
						}
					}

					if (null != keypadType && keypadType.equals("9")) {
						showBoardByType(9);
					} else if (null != keypadType && keypadType.equals("10")) {
						showBoardByType(10);
					} else if (null != keypadType && keypadType.equals("1")) {
						showBoardByType(9);
					} else if (null != keypadType && keypadType.equals("7")) {
						showBoardByType(7);
					} else if (null != keypadType && keypadType.equals("11")) {
						showBoardByType(11);
					} else {
						// 缺少加密方法
						showBoardByType(0);
					}
					return;
				}
				else {
					// 更新界面
					Random ran = new Random();
					java.util.List<Integer> list = new java.util.ArrayList<Integer>();
					while (list.size() < 10) {
						int n = ran.nextInt(10);
						if (!list.contains(n))
							list.add(n);// 如果n不包涵在list中，才添加
					}
					for (int i = 0; i < keyNumIds.length; i++) {
						if (activity.getParent() != null
								&& activity.getParent().getParent() != null) {
							btnNumbs[i] = (Button) activity.getParent()
									.getParent().findViewById(keyNumIds[i]);
						} else {
							btnNumbs[i] = (Button) activity
									.findViewById(keyNumIds[i]);
						}

						btnNumbs[i].setText(list.get(i) + "");
						if (null != keypadType && keypadType.equals("11")) {
							btnNumbs[i]
									.setOnClickListener(digitPadClickListener);
						} else {
							btnNumbs[i]
									.setOnClickListener(keyBoardInputClickListener);
						}
					}
					if (null != keypadType && keypadType.equals("2")) {
						showBoardByType(2);
					} else {
						showBoardByType(0);
					}
				}
			}
		});
	}

	/**
	 * 
	 * @Description
	 * @author 孙靖
	 * @version 1.0 2013年12月30日
	 */
	private void randomKeyAbc() {
		activity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if (true) {
					new Handler().post(new Runnable() {

						@Override
						public void run() {
							if (randomLetterKeyboard == 0) {
								for (int i = 0; i < randomAbcs.length; i++) {
									Log.i("keyAbcStrsLower",
											keyAbcStrsLower[randomAbcs[i]]);
									btnAbcs[i]
											.setText(keyAbcStrsLower[randomAbcs[i]]);
								}
							}

						}
					}); // 缺少加密方法
					showBoardByType(1);
					return;
				}
				// 随机暂不需要完成
			}
		});
	}

	/**
	 * 随机加密符号键盘
	 * 
	 * @Description
	 * @author 孙靖
	 * @version 1.0 2013年12月30日
	 */
	private void randomKeySign() {
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (true) {
					// 缺少加密方法
					showBoardByType(3);
					return;
				}
				// 随机暂不需要完成
			}
		});
	}

	/**
	 * 根据参数显示某种键盘
	 * 
	 * @Description
	 * @param flag
	 *            1=abc 2=数字 3=符号
	 */
	private void showBoardByType(int flag) {
		keypad_abc.setVisibility(View.GONE);
		key_board_sign.setVisibility(View.GONE);
		keypad_num.setVisibility(View.GONE);
		if (flag == 0) {
			keypad_num.setVisibility(View.VISIBLE);
			bottomfirstview.setVisibility(View.VISIBLE);
			btnNumBoardPoint.setVisibility(View.VISIBLE);
			btnNum_X.setVisibility(View.GONE);
			btnNumBoardChangeAbc.setVisibility(View.VISIBLE);
		} else if (flag == 1) {
			keypad_abc.setVisibility(View.VISIBLE);
		} else if (flag == 2) {
			keypad_num.setVisibility(View.VISIBLE);
			bottomfirstview.setVisibility(View.GONE);
			btnNumBoardPoint.setVisibility(View.GONE);
			btnNum_X.setVisibility(View.VISIBLE);
			btnNumBoardChangeAbc.setVisibility(View.GONE);
			btnNum_X.setClickable(true);
			btnNum_X.setText("清 空");
			btnNum_X.setTextColor(activity.getResources().getColor(
					EUExUtil.getResColorID("white")));
			btnNum_X.setBackgroundResource(EUExUtil
					.getResDrawableID("plugin_randomkeyboard_key_changenum_bg_select"));
			btnNum_X.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					clear();
				}
			});
		} else if (flag == 3) {
			key_board_sign.setVisibility(View.VISIBLE);
		} else if (flag == 9 || flag == 7) {
			keypad_num.setVisibility(View.VISIBLE);
			bottomfirstview.setVisibility(View.GONE);
			btnNumBoardPoint.setVisibility(View.GONE);
			btnNum_X.setVisibility(View.VISIBLE);
			btnNum_X.setText("清 空");
			btnNum_X.setTextColor(activity.getResources().getColor(
					EUExUtil.getResColorID("white")));
			btnNum_X.setClickable(true);
			btnNum_X.setBackgroundResource(EUExUtil
					.getResDrawableID("plugin_randomkeyboard_key_changenum_bg_select"));
			btnNum_X.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					clear();
				}
			});
			btnNumBoardChangeAbc.setVisibility(View.GONE);
		} else if (flag == 11) {
			keypad_num.setVisibility(View.VISIBLE);
			bottomfirstview.setVisibility(View.GONE);
			btnNumBoardPoint.setVisibility(View.GONE);
			btnNumBoardChangeAbc.setVisibility(View.GONE);
			btnNum_X.setVisibility(View.VISIBLE);
			btnNum_X.setText(".");
			btnNum_X.setTextColor(activity.getResources().getColor(
					EUExUtil.getResColorID("black")));
			btnNum_X.setClickable(true);
			btnNum_X.setBackgroundResource(EUExUtil
					.getResDrawableID("plugin_randomkeyboard_key_bg_select"));
			btnNum_X.setOnClickListener(digitPadClickListener);
		} else {
			keypad_abc.setVisibility(View.VISIBLE);
		}
	}

	// public int setUpRadomLetterKeyBoard() {
	// int ramdomDigit = (int) (Math.random() * 25);
	// Log.i("ramdomDigit", ramdomDigit + "------ramdomDigit");
	// for (int i = 0; i < 26; i++) {
	// }
	// return ramdomDigit;
	// }

	private int[] setUpRadomLetterKeyBoard() {
		int number = 26;// 控制随机数产生的范围
		int random[] = new int[number]; // 用于存放所取的值的数组
		List<Integer> arr = new ArrayList<Integer>();
		for (int i = 0; i < number; i++)
			arr.add(i);// 为ArrayList添加元素
		for (int j = 0; j < random.length; j++) {
			int index = (int) (Math.random() * number);// 产生一个随机数作为索引
			random[j] = (int) arr.get(index);
			arr.remove(index);// 移除已经取过的元素
			number--;// 将随机数范围缩小1
		}

		// for(int k=0;k<random.length;k++){
		// Log.i("setUpRadomLetterKeyBoard", random[k]+"----1111------");
		// }
		// Log.i("setUpRadomLetterKeyBoard", random.length+"----------");

		return random;
	}

}