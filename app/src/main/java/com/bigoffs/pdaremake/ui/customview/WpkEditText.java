package com.bigoffs.pdaremake.ui.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;

import androidx.appcompat.widget.AppCompatEditText;

import com.bigoffs.pdaremake.R;
import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.SizeUtils;


/**
 *  <com.WpkEditText
 *                     android:id="@+id/wet_count"
 *                     android:layout_width="wrap_content"
 *                     android:layout_height="wrap_content"
 *                     android:layout_marginBottom="20dp"
 *                     app:input_style="count_style"/>
 *
 *
 *
 */
public class WpkEditText extends AppCompatEditText {

    private Drawable hideIcon;
    private Drawable clearIcon;
    private Drawable customRightIcon;
    private Drawable customLeftIcon;

    private OnClickCustomListener mCustomLeftListener;
    private OnClickCustomListener mCustomRightListener;
    private OnClickHideListener mHideListener;
    private OnClickClearListener mClearListener;

    private int inputStyle;
    private final int CUSTOM_STYLE = 0;
    private final int CLEAR_STYLE_DARK = 1;
    private final int CLEAR_STYLE_LIGHT = 2;
    private final int HIDE_STYLE_DARK = 3;
    private final int HIDE_STYLE_LIGHT = 4;
    private final int COUNT_STYLE = 5;
    private int count = 0;
    private int maxCount = 999;

    private TextWatcher mTextWatcher;

    public WpkEditText(Context context) {
        super(context);
    }

    public WpkEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public WpkEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {

        if (null == attrs) {
            return;
        }
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.WpkEditText);
        inputStyle = a.getInteger(R.styleable.WpkEditText_input_style, CUSTOM_STYLE);
        a.recycle();

        setBackgroundResource(R.drawable.wyze_edit_bg);
        setTextSize(14);
        setTextColor(getResources().getColor(R.color.wyze_input_border_color));
        setPadding(SizeUtils.dp2px(20), 0, SizeUtils.dp2px(10), 0);
        setHeight(SizeUtils.dp2px(40));
        setGravity(Gravity.CENTER_VERTICAL);

        Drawable[] drawables = getCompoundDrawables();
        customLeftIcon = drawables[0];
        customRightIcon = drawables[2];

        switch (inputStyle) {
            case HIDE_STYLE_DARK:
//                hideIcon = getResources().getDrawable(R.drawable.wyze_login_icon_close_black);
                setCompoundDrawablesWithIntrinsicBounds(null, null, hideIcon, null);
                setCompoundDrawablePadding(10);
                setTransformationMethod(PasswordTransformationMethod.getInstance());
                break;
            case HIDE_STYLE_LIGHT:
//                hideIcon = getResources().getDrawable(R.drawable.wyze_login_icon_close_white);
                setCompoundDrawablesWithIntrinsicBounds(null, null, hideIcon, null);
                setCompoundDrawablePadding(10);
                setTransformationMethod(PasswordTransformationMethod.getInstance());
                setTextColor(getResources().getColor(R.color.white));
                setBackgroundResource(R.drawable.wyze_edit_bg_light);
                break;
            case CLEAR_STYLE_DARK:
//                clearIcon = getResources().getDrawable(R.drawable.wyze_login_icon_empty_black);
                setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                setCompoundDrawablePadding(10);
                break;
            case CLEAR_STYLE_LIGHT:
//                clearIcon = getResources().getDrawable(R.drawable.wyze_login_icon_empty_white);
                setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                setCompoundDrawablePadding(10);
                setTextColor(getResources().getColor(R.color.white));
                setBackgroundResource(R.drawable.wyze_edit_bg_light);
                break;
            case COUNT_STYLE:
                customLeftIcon = getResources().getDrawable(R.mipmap.wyze_shop_icon_quantitypicker_off);
                customRightIcon = getResources().getDrawable(R.mipmap.wyze_shop_icon_quantitypicker_on);
                setCompoundDrawablesWithIntrinsicBounds(customLeftIcon, null, customRightIcon, null);
                setCompoundDrawablePadding(10);
                setGravity(Gravity.CENTER);
                setText(count+"");
                setTextColor(getResources().getColor(R.color.wyze_text_color));
//                setFocusable(false);
//                setFocusableInTouchMode(false);
                setInputType(InputType.TYPE_CLASS_NUMBER);
                setCursorVisible(false);
                setSelection((count+"").length());
                setPadding(SizeUtils.dp2px(14), 0, SizeUtils.dp2px(14), 0);
                setWidth(SizeUtils.dp2px(100));
                setHeight(SizeUtils.dp2px(40));
                mCustomLeftListener = new OnClickCustomListener() {
                    @Override
                    public void onClick() {
                        if(count>0){
                            setText(--count+"");
//                            setCount(--count);
                        }
                    }
                };

                mCustomRightListener = new OnClickCustomListener() {
                    @Override
                    public void onClick() {
                        setText(++count+"");
                    }
                };
                break;

        }

        mHideListener = new OnClickHideListener() {
            @Override
            public void clickHideInput() {
                    if (!getTransformationMethod().equals(PasswordTransformationMethod.getInstance())) {
                        setTransformationMethod(PasswordTransformationMethod.getInstance());
                        hideIcon = inputStyle == HIDE_STYLE_DARK?
                                 getResources().getDrawable(R.mipmap.wyze_login_icon_close_black)
                                :getResources().getDrawable(R.mipmap.wyze_login_icon_close_white);
                        setCompoundDrawablesWithIntrinsicBounds(null, null, hideIcon, null);
                        setCompoundDrawablePadding(10);
                    } else {
                        setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        hideIcon = inputStyle == HIDE_STYLE_DARK?
                                 getResources().getDrawable(R.mipmap.wyze_login_icon_open_black)
                                :getResources().getDrawable(R.mipmap.wyze_login_icon_open_white);
                        setCompoundDrawablesWithIntrinsicBounds(null, null, hideIcon, null);
                        setCompoundDrawablePadding(10);
                    }
                    if(getText()!=null){
                        setSelection(getText().toString().length());//光标移到最后
                    }
            }
        };

        mClearListener = new OnClickClearListener() {
            @Override
            public void clickClearInput() {
                setText("");
            }
        };

    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        if (inputStyle == CLEAR_STYLE_DARK || inputStyle == CLEAR_STYLE_LIGHT) {
            if (!TextUtils.isEmpty(text)) {
                setCompoundDrawablesWithIntrinsicBounds(null, null, clearIcon, null);
            } else {
                setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);

            }
        }else if(inputStyle == COUNT_STYLE){
            if(!TextUtils.isEmpty(text)) {
                count = Integer.parseInt(text.toString());
                if(count > maxCount){
                    count = maxCount;
                }
            }
        }
    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            switch (inputStyle) {
                case HIDE_STYLE_DARK:
                case HIDE_STYLE_LIGHT:
                    if (hideIcon != null) {
                        if (event.getX() > (getWidth()
                                - hideIcon.getIntrinsicWidth()
                                - getPaddingRight())
                                && event.getX() < getWidth()) {
                            if (getCompoundDrawables()[2] != null && mHideListener != null) {
                                mHideListener.clickHideInput();
                                cancelLongPress();
                                return true;
                            }
                        }
                    }
                    break;
                case CLEAR_STYLE_DARK:
                case CLEAR_STYLE_LIGHT:
                    if (clearIcon != null) {
                        if (event.getX() > (getWidth()
                                - clearIcon.getIntrinsicWidth()
                                - getPaddingRight())
                                && event.getX() < getWidth()) {
                            if (getCompoundDrawables()[2] != null && mClearListener != null) {
                                mClearListener.clickClearInput();
                                cancelLongPress();
                                return true;
                            }
                        }
                    }
                    break;
                case CUSTOM_STYLE:
                    if (customRightIcon != null) {
                        if (event.getX() > (getWidth()
                                - customRightIcon.getIntrinsicWidth()
                                - getPaddingRight())
                                && event.getX() < getWidth()) {
                            if (getCompoundDrawables()[2] != null && mCustomRightListener != null) {
                                mCustomRightListener.onClick();
                                cancelLongPress();
                                return true;
                            }
                        }
                    }
                    break;
                case COUNT_STYLE:
                    if (customRightIcon != null) {
                        if (event.getX() > (getWidth()
                                - customRightIcon.getIntrinsicWidth()
                                - getPaddingRight())
                                && event.getX() < getWidth()) {
                            if (getCompoundDrawables()[2] != null && mCustomRightListener != null) {
                                mCustomRightListener.onClick();
                                cancelLongPress();
                                return true;
                            }
                        }
                    }

                    if (customLeftIcon != null) {
                        if (event.getX() < customLeftIcon.getIntrinsicWidth() + getPaddingLeft()
                                && event.getX() > 0) {
                            if (getCompoundDrawables()[0] != null && mCustomLeftListener != null) {
                                mCustomLeftListener.onClick();
                                cancelLongPress();
                                return true;
                            }
                        }
                    }
                    break;
            }

        }
        return super.onTouchEvent(event);
    }

    public interface OnClickCustomListener {
        void onClick();
    }

    public interface OnClickHideListener {
        void clickHideInput();
    }

    public interface OnClickClearListener {
        void clickClearInput();
    }

    public void setOnLeftIconClickListener(OnClickCustomListener mCustomListener) {
        this.mCustomLeftListener = mCustomListener;
    }

    public void setOnRightIconClickListener(OnClickCustomListener mCustomListener) {
        this.mCustomRightListener = mCustomListener;
    }

    @Override
    public void addTextChangedListener(TextWatcher watcher) {
        super.addTextChangedListener(watcher);
        this.mTextWatcher = watcher;
    }

    public void setCount(int count){
        if(count > maxCount){
            count = maxCount;
        }else if(count < 0){
            count = 0;
        }
        this.count = count;
        if(mTextWatcher!=null){
            removeTextChangedListener(mTextWatcher);
            setText(count+"");
            addTextChangedListener(mTextWatcher);
        }else{
            setText(count+"");
        }
        setSelection((count+"").length());
    }
}
