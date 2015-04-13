package org.zywx.wbpalmstar.plugin.randomkeyboard;

import org.zywx.wbpalmstar.engine.universalex.EUExUtil;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.RelativeLayout;

public class RandomKeyboardActivity extends Activity {

	public static final String TAG = "RandomKeyboardActivity";
	private static final int IS_PASSWORD = 1;
	private AllKeyBoardMgr mAllKeyBoard;
	private KeyboardConfig mConfig;
	private boolean isPassword = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(TAG, "onCreate");
		RelativeLayout contentLayout = new RelativeLayout(this);
		contentLayout.setBackgroundColor(Color.TRANSPARENT);

		View keyboardPad = View.inflate(this,
				EUExUtil.getResLayoutID("plugin_randomkeyboard_allkeyboard"),
				null);
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);

		Log.i(TAG, "ScreenWidth== " + dm.widthPixels + "  ScreenHeight== "
				+ dm.heightPixels);

		int viewWidth = dm.widthPixels;
		int viewHeight = 4 * viewWidth / 5;
		if (viewHeight > dm.heightPixels / 2) {
			viewHeight = dm.heightPixels / 2;
		}
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
				viewWidth, viewHeight);
		lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		contentLayout.addView(keyboardPad, lp);

		setContentView(contentLayout);

		/*contentLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mAllKeyBoard != null) {
					mAllKeyBoard.hideKeyboard();
				}
			}
		});*/
		contentLayout.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(MotionEvent.ACTION_DOWN == event.getAction()){
					if (mAllKeyBoard != null) {
						mAllKeyBoard.hideKeyboard();
					}
				}
				return false;
			}
		});

		mConfig = (KeyboardConfig) getIntent().getSerializableExtra("config");
		if (mConfig.getSecureTextEntry() == IS_PASSWORD) {
			isPassword = true;
		}
		String inputValue = getIntent().getStringExtra("inputValue");
		String encryptValue = getIntent().getStringExtra("encryptValue");
		String initData = getIntent().getStringExtra("initData");

		
		//
		mAllKeyBoard = new AllKeyBoardMgr(this, isPassword, Integer.parseInt(mConfig.getDefaultKeyboard())+1, "100", 16,
				inputValue, encryptValue,Integer.parseInt(mConfig.getRandomKeyboard()),
				Integer.parseInt(this.mConfig.getRandomDigitalKeyboard()),initData);

		mAllKeyBoard.showKeyboard();
	}

	@Override
	public void onBackPressed() {
		MyLog.i(TAG, "onBackPressed");
		if (mAllKeyBoard != null) {
			mAllKeyBoard.hideKeyboard();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		MyLog.i(TAG, "onDestroy");
	}

}
