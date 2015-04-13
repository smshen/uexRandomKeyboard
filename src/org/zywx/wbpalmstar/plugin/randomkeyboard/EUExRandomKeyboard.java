package org.zywx.wbpalmstar.plugin.randomkeyboard;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;
import org.zywx.wbpalmstar.engine.EBrowserView;
import org.zywx.wbpalmstar.engine.universalex.EUExBase;
import org.zywx.wbpalmstar.engine.universalex.EUExCallback;
import org.zywx.wbpalmstar.widgetone.dataservice.WWidgetData;

import android.app.Activity;
import android.app.ActivityGroup;
import android.app.LocalActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.text.InputType;
import android.text.Selection;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class EUExRandomKeyboard extends EUExBase {

	private static final String TAG = "UexRandomKeyboard";
	private static final String CALLBACK_ON_GETCONTENT = "uexRandomKeyboard.onGetContent";
	private boolean isOpenInput = false;
	private boolean isOpenNativeInput = false;
	private WWidgetData mCurWData;
	private EditText mInputFrame;
	private EditText NativeInputFrame;
	private int requestCode = 0;
	private int mScrolledBy;
	private int scroll_hight;
	//成为静态的时候当切换页面会导致其变化。
	private static EUExRandomKeyboard sPluginInstance;
	//记录所在页的当前对象
	private EUExRandomKeyboard thisPluginInstance;
	private KeyboardConfig configmInput;
	private KeyboardConfig configNative;
	private int isPsd = -1;
	private String initData = null;
	
	/**
	 * 记录滚动的view，以便回滚
	 */
	private View scrollview;
	
	/**
	 * 显示键盘的view
	 */
	private View keyBoardView;
	
	/**
	 * 记录当前的InputMode（南方基金项目中单独的）
	 */
	private int softInputMode;
	private LocalActivityManager mgr;

	public EUExRandomKeyboard(Context context, EBrowserView view) {
		super(context, view);
		mCurWData = view.getCurrentWidget();
		sPluginInstance = this;
		this.thisPluginInstance = this;
		//获取当前的softInputMode 
		softInputMode = ((Activity) mContext)
				.getWindow().getAttributes().softInputMode;
		//以Activity形式使用时需要的回滚
		/*this.registerAppEventListener(new EUExEventListener() {
			
			@Override
			public boolean onEvent(int arg0) {
				if(arg0 == EUExBase.F_UEX_EVENT_TYPE_APP_ON_RESUME && mScrolledBy != 0){
					scrollview.scrollBy(0, -mScrolledBy);
					mScrolledBy = 0;
				}
				return false;
			}
		});*/
	}

	/**
	 * 获取当前的插件对象
	 * 
	 * @return
	 */
	public static EUExRandomKeyboard getPluginInstance() {
		return sPluginInstance;
	}

	public void open(String[] params) {
		Log.i(TAG, "open");
		if (params.length < 5) {
			return;
		}
		try {
			int x = Integer.parseInt(params[0]);
			int y = Integer.parseInt(params[1]);
			int w = Integer.parseInt(params[2]);
			int h = Integer.parseInt(params[3]);
			String jsonStr = params[4];
			Log.i(TAG, "jsonStr==" + jsonStr);
			KeyboardConfig config = ParserUtils.parseOpenJson(jsonStr,
					mContext, mCurWData);
			if (config == null) {
				Log.e(TAG, "json data parse error");
				return;
			}
			openKeyboard(x, y, w, h, config);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 初始化输入框参数和监听
	 * 
	 * @param config
	 */
	private void initInputFrame(EditText InputFrame ,final KeyboardConfig config) {
		if (InputFrame == null) {
			InputFrame = new EditText(mContext);
		}
		String hint = config.getPlaceholderText();
		InputFrame.setHint(hint);
		if (TextUtils.isEmpty(config.getBackgroundColor())) {
			// 如果是空，或者没有传入此键值，则设置为透明
			InputFrame.getBackground().setAlpha(0);
		} else {
			//使用以前的方式，不识别#00000000(透明) 只能识别#后6位
			/*int backgroundColorCode = ParserUtils.parseColor(config
				.getBackgroundColor());*/
			int backgroundColorCode =Color.parseColor(config
					.getBackgroundColor());
			InputFrame.setBackgroundColor(backgroundColorCode);
		}
		InputFrame.setTextColor(config.getFontColor());
		// mInputFrame.setTextSize(config.getFontSize());
		InputFrame.setTextSize(ParserUtils.px2sp(mContext,
				config.getFontSize()));
		if("1".equals(config.getIsTextRight())){
			InputFrame.setGravity(Gravity.CENTER_VERTICAL|Gravity.RIGHT);
		}else{
			InputFrame.setGravity(Gravity.CENTER_VERTICAL);
		}
		InputFrame.setPadding(5, 5, 5, 5);
		InputFrame.setSingleLine();
		InputFrame.setLongClickable(false);
		/*mInputFrame.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus) {
					hideKeyboard();
				}
			}
		});*/
		if ("1".equals(config.getIsSystemKeyboard())) {
			mInputFrame = InputFrame;
			mInputFrame.setOnFocusChangeListener(new OnFocusChangeListener() {
				
				@SuppressWarnings("deprecation")
				@Override
				public void onFocusChange(View v, boolean hasFocus) {
					
					if(hasFocus){
						mInputFrame.requestFocus();
						mInputFrame.requestFocusFromTouch();
						mInputFrame.setCursorVisible(true);// 显示光标
						if (Build.VERSION.SDK_INT >= 11) {
								Method setShowSoftInputOnFocus = null;
								try {
									setShowSoftInputOnFocus = mInputFrame
											.getClass().getMethod(
													"setShowSoftInputOnFocus",
													boolean.class);
									setShowSoftInputOnFocus.setAccessible(true);
									setShowSoftInputOnFocus.invoke(mInputFrame,
											false);
								} catch (SecurityException e) {
									e.printStackTrace();
								} catch (NoSuchMethodException e) {
									e.printStackTrace();
								} catch (Exception e) {
									e.printStackTrace();
								}
							} else {
								mInputFrame.setInputType(InputType.TYPE_NULL);
							}
							InputMethodManager imm = (InputMethodManager) mContext
									.getSystemService(Context.INPUT_METHOD_SERVICE);
							imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
							requestCode = (int) (Math.random() * 10000);
							String inputValue = mInputFrame.getText().toString();
							String encryptValue = "";
							if (mInputFrame.getTag() != null) {
								encryptValue = ((String) mInputFrame.getTag());
							}
							if (null != initData) {
								inputValue = "";
							}
							// onShowKeyboard(700);
							Intent intent = new Intent(mContext,
									RandomKeyboardActivity.class);
							intent.putExtra("config", config);
							intent.putExtra("inputValue", inputValue);
							intent.putExtra("encryptValue", encryptValue);
							intent.putExtra("initData", initData);
							// startActivityForResult(intent, requestCode);

							if (keyBoardView != null) {
								destroy((ActivityGroup) mContext, TAG);
								removeViewFromCurrentWindow(keyBoardView);
							}
							
							//获取所在view的sPluginInstance
							sPluginInstance = thisPluginInstance;
							
							
							mgr = ((ActivityGroup) mContext)
									.getLocalActivityManager();
							Window window = mgr.startActivity(TAG, intent);
							keyBoardView = window.getDecorView();
							RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
									RelativeLayout.LayoutParams.MATCH_PARENT,
									RelativeLayout.LayoutParams.MATCH_PARENT);
							addView2CurrentWindow(keyBoardView, lp);

							initData = null;
							// showKeyboard();
					}
					
					
				}
			});
			mInputFrame.setOnTouchListener(new OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					mInputFrame.clearFocus();
					mInputFrame.requestFocus();
					mInputFrame.requestFocusFromTouch();
					mInputFrame.setCursorVisible(true);// 显示光标
					/*switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						if (Build.VERSION.SDK_INT >= 11) {
							Method setShowSoftInputOnFocus = null;
							try {
								setShowSoftInputOnFocus = mInputFrame
										.getClass().getMethod(
												"setShowSoftInputOnFocus",
												boolean.class);
								setShowSoftInputOnFocus.setAccessible(true);
								setShowSoftInputOnFocus.invoke(mInputFrame,
										false);
							} catch (SecurityException e) {
								e.printStackTrace();
							} catch (NoSuchMethodException e) {
								e.printStackTrace();
							} catch (Exception e) {
								e.printStackTrace();
							}
						} else {
							mInputFrame.setInputType(InputType.TYPE_NULL);
						}
						InputMethodManager imm = (InputMethodManager) mContext
								.getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
						requestCode = (int) (Math.random() * 10000);
						String inputValue = mInputFrame.getText().toString();
						String encryptValue = "";
						if (mInputFrame.getTag() != null) {
							encryptValue = ((String) mInputFrame.getTag());
						}
						if (null != initData) {
							inputValue = "";
						}
						// onShowKeyboard(700);
						Intent intent = new Intent(mContext,
								RandomKeyboardActivity.class);
						intent.putExtra("config", config);
						intent.putExtra("inputValue", inputValue);
						intent.putExtra("encryptValue", encryptValue);
						intent.putExtra("initData", initData);
						// startActivityForResult(intent, requestCode);

						if (keyBoardView != null) {
							destroy((ActivityGroup) mContext, TAG);
							removeViewFromCurrentWindow(keyBoardView);

						}
						
						//获取所在view
						sPluginInstance = thisPluginInstance;
						
						mgr = ((ActivityGroup) mContext)
								.getLocalActivityManager();
						Window window = mgr.startActivity(TAG, intent);
						keyBoardView = window.getDecorView();
						RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
								RelativeLayout.LayoutParams.MATCH_PARENT,
								RelativeLayout.LayoutParams.MATCH_PARENT);
						addView2CurrentWindow(keyBoardView, lp);

						initData = null;
						// showKeyboard();
						return true;
					}*/
					return true;
				}
			});
		}else {
			NativeInputFrame = InputFrame;
			NativeInputFrame.setOnTouchListener(new OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					NativeInputFrame.requestFocus();
					NativeInputFrame.requestFocusFromTouch();
					NativeInputFrame.setCursorVisible(true);// 显示光标
					return false;
				}
			});
			NativeInputFrame
					.setOnFocusChangeListener(new OnFocusChangeListener() {
						@Override
						public void onFocusChange(View v, boolean hasFocus) {
							InputMethodManager imm = (InputMethodManager) mContext
									.getSystemService(Context.INPUT_METHOD_SERVICE);
							if (!hasFocus) {
								String namecontent = NativeInputFrame.getText()
										.toString();
								onGetConent(namecontent, "name", false);
								imm.hideSoftInputFromWindow(
										NativeInputFrame.getWindowToken(), 0);
							} else {
								NativeInputFrame.requestFocus();
								NativeInputFrame.requestFocusFromTouch();
								NativeInputFrame.setCursorVisible(true);// 显示光标
								imm.toggleSoftInput(0,
										InputMethodManager.HIDE_NOT_ALWAYS);
							}
						}
					});
		}
	}

	/**
	 * 打开键盘
	 * 
	 * @param x
	 * @param y
	 * @param w
	 * @param h
	 * @param config
	 */
	private void openKeyboard(final int x, final int y, final int w,
			final int h, final KeyboardConfig config) {
		((Activity) mContext).runOnUiThread(new Runnable() {
			public void run() {
				try {
					if ("1".equals(config.getIsSystemKeyboard())) {
						if (isOpenInput) {
							Log.i(TAG, "password input already opened");
							return;
						}
						isOpenInput = true;
						Log.i(TAG, "open x=" + x + " y=" + y + " w=" + w
								+ " h=" + h + "  open " + isOpenInput
								+ "  this " + sPluginInstance.toString());
						initInputFrame(mInputFrame, config);
						configmInput = config;
						isPsd = config.getSecureTextEntry();
						RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
								w, h);
						lp.leftMargin = x;
						lp.topMargin = y;
						addView2CurrentWindow(mInputFrame, lp);
					} else {
						if (isOpenNativeInput) {
							Log.i(TAG, "isOpenNativeInput input already opened");
							return;
						} else {
							isOpenNativeInput = true;
							Log.i(TAG, "openNative x=" + x + " y=" + y + " w="
									+ w + " h=" + h + "  open "
									+ isOpenNativeInput + "  this "
									+ sPluginInstance.toString());
							initInputFrame(NativeInputFrame, config);
							configNative = config;
							RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
									w, h);
							lp.leftMargin = x;
							lp.topMargin = y;
							addView2CurrentWindow(NativeInputFrame, lp);
						}
					}
					
					Log.i(TAG, "NativeInpoutFrame is hasFocus !   and  the softInputMode" + softInputMode);
					if(isOpenInput || isOpenNativeInput){
						// 需要转换为平移模式，压缩会导致错位的问题
						if (softInputMode != (WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)) {
							((Activity) mContext)
									.getWindow()
									.setSoftInputMode(
											WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN
													| WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
						}
					}
//					((Activity) mContext).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN
//			                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_UNSPECIFIED);
					// WindowManager.LayoutParams mainLp =
					// (WindowManager.LayoutParams) ((Activity) mContext)
					// .getWindow().getDecorView().getLayoutParams();
					// FrameLayout.LayoutParams mainLp =
					// (FrameLayout.LayoutParams) ((View) mBrwView
					// .getParent()).getLayoutParams();
					// mainLp.topMargin = -200;
					// mBrwView.recomputeViewAttributes(mBrwView);
					// ((LayoutParams) mBrwView.getLayoutParams()).topMargin =
					// -200;
					// DisplayMetrics dm = new DisplayMetrics();
					// ((Activity) mContext).getWindowManager()
					// .getDefaultDisplay().getMetrics(dm);
					// int viewWidth = dm.widthPixels;
					// int viewHeight = 4 * viewWidth / 5;
					// if (viewHeight > dm.heightPixels / 2) {
					// viewHeight = dm.heightPixels;
					// }
					//
					// EUExRandomKeyboard.getPluginInstance().onShowKeyboard(
					// viewHeight);
					// ((Activity) mContext).getWindow().getDecorView()
					// .scrollBy(0, 200);
					// ((Activity) mContext)
					// .getWindow()
					// .getDecorView()
					// .startAnimation(
					// new TranslateAnimation(
					// Animation.RELATIVE_TO_SELF, 0,
					// Animation.RELATIVE_TO_SELF, 0,
					// Animation.RELATIVE_TO_SELF, 0,
					// Animation.RELATIVE_TO_SELF, 200));
					// ((Activity) mContext).getWindow().getDecorView()
					// .setLayoutParams(mainLp);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * 绕开引擎的添加view的方法，防止被webview控制大小
	 * 
	 * @param child
	 * @param parms
	 */
	private void addView2CurrentWindow(View child,
			RelativeLayout.LayoutParams parms) {
		int l = (int) (parms.leftMargin);
		int t = (int) (parms.topMargin);
		int w = parms.width;
		int h = parms.height;
		FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(w, h);
		lp.gravity = Gravity.NO_GRAVITY;
		lp.leftMargin = l;
		lp.topMargin = t;
		adptLayoutParams(parms, lp);
		Log.i(TAG, "addView2CurrentWindow");
		mBrwView.addViewToCurrentWindow(child, lp);
	}
	
	@Override
	protected boolean clean() {
		
		
		// 将切换的键盘模式改变回原来的
		close(null);
		Log.i(TAG, "clean");
		return true;
	}

	/**
	 * 关闭插件
	 * @param params
	 */
	public void close(String[] params) {
		// ((Activity) mContext).runOnUiThread(new Runnable() {
		// public void run() {
		// FrameLayout.LayoutParams mainLp = (FrameLayout.LayoutParams) ((View)
		// mBrwView
		// .getParent()).getLayoutParams();
		// mainLp.topMargin = -200;
		// mBrwView.getParent().recomputeViewAttributes(mBrwView);
		// ((View) mBrwView.getParent()).postInvalidate();
		// }
		// });
		Log.i(TAG, "close  " + isOpenInput + "  this " + sPluginInstance.toString());
		if (!isOpenInput && !isOpenNativeInput) {
			Log.i(TAG, "not open");
			return;
		}
		
		if (scrollview != null && mScrolledBy != 0) {
			Log.i(TAG, "scroll");
			LayoutParams lp = scrollview.getLayoutParams();
			lp.height = scrollview.getHeight() - mScrolledBy;
			scrollview.setLayoutParams(lp);
			scrollview.scrollBy(0, -mScrolledBy);
			mScrolledBy = 0;
			Log.i(TAG, "scroll over");
		}
		onRemoveKeyboard();
		try {
			if (mInputFrame != null) {
				removeViewFromCurrentWindow(mInputFrame);
				mInputFrame = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			if (NativeInputFrame != null) {
				removeViewFromCurrentWindow(NativeInputFrame);
				NativeInputFrame = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (softInputMode != (((Activity) mContext).getWindow().getAttributes().softInputMode)) {
			((Activity) mContext).runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					
					((Activity) mContext).getWindow().setSoftInputMode(softInputMode);
					Log.i(TAG,
							"NativeInpoutFrame is not hasFocus !  and  the softInputMode "
									+ softInputMode 
									+ "    " + ((Activity) mContext)
									.getWindow().getAttributes().softInputMode);
				}
			});
		}
		
		isPsd = -1;
		initData = null;
		configmInput = null;
		configNative = null;
		isOpenInput = false;
		isOpenNativeInput = false;
		Log.i(TAG, "close over");
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (data != null) {
			String content = data.getStringExtra("content");
			boolean isComplete = data.getBooleanExtra("isOk", false);
			onGetConent(content,"passWord",isComplete);
			// onHideKeyboard(700);
		}
	}
	
	/**
	 * 以窗口运行时使用替代上面的方法
	 * @param data
	 */
	public void onResult(Intent data){
		if (data != null) {
			String content = data.getStringExtra("content");
			boolean isComplete = data.getBooleanExtra("isOk", false);
			onGetConent(content,"passWord",isComplete);
			Log.i(TAG, "onResult");
			// onHideKeyboard(700);
		}
	}

	/**
	 * 回调前端
	 * 
	 * @param content
	 */
	private void onGetConent(String content,String type,boolean isOk) {
		if (TextUtils.isEmpty(content)) {
			content = "";
		}
		if(isOk){
			content = getJson(content,type, "0");
		}else{
			content = getJson(content,type, "1");
		}
		jsCallback(CALLBACK_ON_GETCONTENT, 0, EUExCallback.F_C_TEXT, content);
		Log.i(TAG, "onGetContent==" + content);
	}
	/**
	 * 	拼接回调的json字符串
	 */
	public String getJson(String content,String type, String isOk) {
		String myString;
		try {
			myString = new JSONStringer().object().key("text").value(content)
					.key("type").value(type)
					.key("complete").value(isOk).endObject().toString();
			return myString;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 同步显示键盘的输入
	 * 
	 * @param content
	 */
	public void changeInputContent(String content, String encryptValue) {
		if (mInputFrame == null) {
			Log.w(TAG, "mInputFrame doesn't exist, no content changed.");
			return;
		}
		mInputFrame.setText(content);
		mInputFrame.setTag(encryptValue);
		Selection.setSelection(mInputFrame.getText(), mInputFrame.getText()
				.length());// 移动光标到最右
	}
	
	/**
	 * 当显示键盘面板时调用，用于防止遮盖输入框故推移屏幕
	 * 
	 * @param keyboardHeight
	 */
	public void onShowKeyboard(int keyboardHeight) {
		Log.i(TAG, "onShowKeyboard");
		DisplayMetrics dm = new DisplayMetrics();
		((Activity) mContext).getWindowManager().getDefaultDisplay()
				.getMetrics(dm);
		if (mInputFrame != null) {
			int inputFrameY = dm.heightPixels - mInputFrame.getTop()
					- mInputFrame.getHeight();
			int scrollBy = keyboardHeight
					- inputFrameY
					+ mInputFrame.getHeight()
					- ((Activity) mContext).getWindow().getDecorView()
							.getScrollY();
			if (scrollBy > 0) {
				mScrolledBy = scrollBy;
				// FrameLayout.LayoutParams mainLp = (FrameLayout.LayoutParams)
				// ((View) mBrwView
				// .getParent()).getLayoutParams();
				// mainLp.topMargin = mainLp.topMargin - scrollBy;
				// ((View) mBrwView.getParent()).setLayoutParams(mainLp);
				scrollview = (View) (mInputFrame.getParent());
				LayoutParams lp = scrollview.getLayoutParams();
				scroll_hight = lp.height;
				lp.height = scrollview.getHeight() + mScrolledBy;
				scrollview.setLayoutParams(lp);
				((View) (mInputFrame.getParent())).scrollBy(0, scrollBy);
				
				// FrameLayout.LayoutParams inputFrameLp =
				// (FrameLayout.LayoutParams) ((View) mInputFrame
				// .getParent()).getLayoutParams();
				// inputFrameLp.topMargin = inputFrameLp.topMargin - scrollBy;
				// ((View)
				// mInputFrame.getParent()).setLayoutParams(inputFrameLp);
				Log.i(TAG, "scrolledBy==" + scrollBy);
			}
		}
	}

	/**
	 * 当隐藏键盘面板时调用，用于推移屏幕回到初始位置
	 * 
	 * @param keyboardHeight
	 */
	public void onHideKeyboard(int keyboardHeight) {
		Log.i(TAG, "onHideKeyboard");
		if (mInputFrame != null) {
			if (mScrolledBy > 0) {
				// FrameLayout.LayoutParams mainLp = (FrameLayout.LayoutParams)
				// ((View) mBrwView
				// .getParent()).getLayoutParams();
				// mainLp.topMargin = mainLp.topMargin + mScrolledBy;
				// ((View) mBrwView.getParent()).setLayoutParams(mainLp);
				LayoutParams lp = scrollview.getLayoutParams();
//				lp.height = scrollview.getHeight() - mScrolledBy;
				lp.height = scroll_hight;
				scrollview.setLayoutParams(lp);
				((View) (mInputFrame.getParent())).scrollBy(0, -mScrolledBy);
				
				// FrameLayout.LayoutParams inputFrameLp =
				// (FrameLayout.LayoutParams) ((View) mInputFrame
				// .getParent()).getLayoutParams();
				// inputFrameLp.topMargin = inputFrameLp.topMargin +
				// mScrolledBy;
				// ((View)
				// mInputFrame.getParent()).setLayoutParams(inputFrameLp);
				Log.i(TAG, "scrolledBy== -" + mScrolledBy);
				scrollview = null;
				mScrolledBy = 0;
			}
		}
	}
	
	/**
	 * 后期添加的要求以view的方式使用键盘
	 * 所以每次隐藏键盘都要要移除键盘view
	 * 
	 */
	@SuppressWarnings("deprecation")
	public void onRemoveKeyboard(){
		if(keyBoardView != null){
			destroy(((ActivityGroup) mContext), TAG);
			removeViewFromCurrentWindow(keyBoardView);
			keyBoardView = null;
			Log.i(TAG, "onremovrekeyboary");
		}
	}

	/**
	 * 用来销毁LocalActivityManager中的Activity
	 * @param activityGroup
	 * @param id
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static boolean destroy(ActivityGroup activityGroup, String id) {
		final LocalActivityManager activityManager = activityGroup
				.getLocalActivityManager();
		if (activityManager != null) {
			activityManager.destroyActivity(id, false);
			try {
				final Field mActivitiesField = LocalActivityManager.class
						.getDeclaredField("mActivities");
				if (mActivitiesField != null) {
					mActivitiesField.setAccessible(true);
					@SuppressWarnings("unchecked")
					final Map<String, Object> mActivities = (Map<String, Object>) mActivitiesField
							.get(activityManager);
					if (mActivities != null) {
						mActivities.remove(id);
					}
					final Field mActivityArrayField = LocalActivityManager.class
							.getDeclaredField("mActivityArray");
					if (mActivityArrayField != null) {
						mActivityArrayField.setAccessible(true);
						@SuppressWarnings("unchecked")
						final ArrayList<Object> mActivityArray = (ArrayList<Object>) mActivityArrayField
								.get(activityManager);
						if (mActivityArray != null) {
							for (Object record : mActivityArray) {
								final Field idField = record.getClass()
										.getDeclaredField("id");
								if (idField != null) {
									idField.setAccessible(true);
									final String _id = (String) idField
											.get(record);
									if (id.equals(_id)) {
										mActivityArray.remove(record);
										break;
									}
								}
							}
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return true;
		}
		return false;
	}

	/**
	 * 清除输入框显示的内容
	 * @param parm 
	 */
	public void clearData(String[] parm){
		
		try {
			JSONObject objclear = new JSONObject(parm[0]);
			final String type = objclear.getString("type");

			((Activity) mContext).runOnUiThread(new Runnable() {

				@Override
				public void run() {
					if ("name".equals(type) && NativeInputFrame != null){
						NativeInputFrame.setTag("");
						NativeInputFrame.setText("");
					}
					if ("passWord".equals(type) && mInputFrame != null){
						mInputFrame.setTag("");
						mInputFrame.setText("");
					}	
				}
			});
		} catch (JSONException e) {
			Log.i(TAG, "clear参数错误");
			e.printStackTrace();
		}
	}
	
	String datatext_Name = null;
	String datatext_PassWord = null;
	String dataplaceHolder_Name = null;
	String dataplaceHolder_PassWord = null;
	
	/**
	 * 设置输入框的数据
	 * '{"name":{"text":"qqqqqq","placeHolder":"66666"},"passWord":{"text":"123456","placeHolder":"99999"}}';
	 * 其中text是要显示的内容，placeHolder是提示的内容，Android只选取passWord使用
	 * @param parm
	 */
	public void setData(String[] parm){
		
		String json = parm[0];
		Log.i(TAG, "setData     " + json);
		
		try {
			JSONObject jsObj = new JSONObject(json);
			JSONObject name = jsObj.getJSONObject("name");
			JSONObject PassWord = jsObj.getJSONObject("passWord");
			
			datatext_Name = name.getString("text");
			dataplaceHolder_Name = name.getString("placeHolder");
			
			datatext_PassWord = PassWord.getString("text");
			dataplaceHolder_PassWord = PassWord.getString("placeHolder");
			
		} catch (JSONException e) {
			Log.i(TAG, "setData参数有误");
			e.printStackTrace();
		}
		
		
		((Activity) mContext).runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				if (mInputFrame != null) {
					initData = datatext_PassWord;
					configmInput.setPlaceholderText(dataplaceHolder_PassWord);
					initInputFrame(mInputFrame, configmInput);
					mInputFrame.setTag("");
					mInputFrame.setText("");
					if (isPsd == 1) {
						if (null != datatext_PassWord) {
							StringBuffer mima = new StringBuffer();
							for (int i = 0; i < datatext_PassWord.length(); i++) {
								mima.append("*");
							}
							mInputFrame.setText(mima);
						}
					} else if (isPsd == 0) {
						mInputFrame.setText(datatext_PassWord);
					} else {
						Toast.makeText(mContext, "错误操作", Toast.LENGTH_SHORT)
								.show();
					}
					onGetConent(initData,"passWord", false);
				}
				if(NativeInputFrame != null){
					configNative.setPlaceholderText(dataplaceHolder_Name);
					initInputFrame(NativeInputFrame, configNative);
					if (null != datatext_Name) {
						NativeInputFrame.setText(datatext_Name);
						onGetConent(datatext_Name, "name", false);
					}
				}
			}
		});
	}
	
	public void setFirstResponder(final String[] parm){
		Log.i(TAG, "setFirstResponder    " + parm[0]);
		
		((Activity) mContext).runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				if("1".equals(parm[0]) && NativeInputFrame != null){
					NativeInputFrame.requestFocus();
				}else if("2".equals(parm[0])  && mInputFrame != null){
					mInputFrame.clearFocus();
					mInputFrame.requestFocus();
				}else if("1".equals(parm[0]) || "2".equals(parm[0]) ){
					Log.i(TAG, "setFirstResponder   input未打开 ");
				}else{
					Log.i(TAG, "setFirstResponder   参数错误 ");
				}
			}
		});
	}

}
