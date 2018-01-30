package com.hgad.warehousemanager.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hgad.warehousemanager.R;

public class Toggle extends LinearLayout {

	private boolean isToggleOn;
	private ImageView ivToggle;

	/**
	 * 在setContentView加载布局时创建对象,系统调用  可以在该构造方法中获取用户设置的属性和样式
	 * @param context
	 * @param attrs
	 * @param defStyleAttr
	 */
	public Toggle(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}
	
	/**
	 * 在setContentView加载布局时创建对象,系统调用  可以在该构造方法中获取用户设置的属性
	 * @param context
	 * @param attrs
	 */
	public Toggle(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		this.setClickable(true);
		
		// getContext() = context
		
		View toggleView = View.inflate(context, R.layout.view_toggle, null);
		//添加儿子
		this.addView(toggleView);
		
		//获取自定义属性  参数1:系统传递给咱们的属性  用户设置的属性     参数2:自定义属性后会在R文件中生成styleable.Toggle
		//伪代码: String title = null;  title = attrs.title    String type = null;   type = attrs.type
		TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.Toggle);
		String title = typedArray.getString(R.styleable.Toggle_name);
		String type = typedArray.getString(R.styleable.Toggle_type);
		boolean isToggle = typedArray.getBoolean(R.styleable.Toggle_isToggle, true);
		//使用完毕后记得回收哦
		typedArray.recycle();
		
		TextView tvTitle = (TextView) findViewById(R.id.tv_title);
		ivToggle = (ImageView) findViewById(R.id.iv_toggle);
		
		if (!isToggle) {
			ivToggle.setVisibility(View.GONE);
		}
		
		tvTitle.setText(title);
		
		if ("top".equals(type)) {
			toggleView.setBackgroundResource(R.drawable.top_selector);
		} else if ("mid".equals(type)) {
			toggleView.setBackgroundResource(R.drawable.mid_selector);
		} else if ("bottom".equals(type)) {
			toggleView.setBackgroundResource(R.drawable.bottom_selector);
		}
		
	}
	
	/**
	 * 开关功能
	 */
	public void toggle() {
		//某鸟的代码
		/*if (isToggleOn) {
			setToggleOn(false);
 		} else {
			setToggleOn(true);
		}*/
		
		//传智人的代码
		setToggleOn(!isToggleOn);
	}
	
	/**
	 * 设置开关状态
	 * @param isToggleOn
	 */
	public void setToggleOn(boolean isToggleOn) {
		this.isToggleOn = isToggleOn;
		if (isToggleOn) {
			ivToggle.setImageResource(R.mipmap.on);
		} else {
			ivToggle.setImageResource(R.mipmap.off);
		}
	}
	
	/**
	 * 获取开关状态
	 * @return
	 */
	public boolean isToggleOn() {
		return isToggleOn;
	}
	
	/**
	 * 在java代码中创建对象时调用   new Toggle(context);
	 * @param context
	 */
	public Toggle(Context context) {
		super(context);
	}

}
