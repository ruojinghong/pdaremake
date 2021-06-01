package com.bigoffs.pdaremake.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.bigoffs.pdaremake.R;
import com.bigoffs.pdaremake.ui.customview.WpkEditText;

/**
 *   WpkCommEditDialog.create(getActivity())
 *                         .setTitle("test")
 *                         .setContent("test")
 *                         .setHintText("hehe")
 *                         .setWordLimit(10)
 *                         .setOnTextChangeListener(new WpkCommEditDialog.OnTextChangeListener() {
 *                             @Override
 *                             public void beforeTextChanged(CharSequence s, int start, int count, int after) {
 *                             }
 *
 *                             @Override
 *                             public void onTextChanged(CharSequence s, int start, int before, int count, TextView tvErrorHint) {
 *                                 if (WpkCommonUtil.isContainSpecialChar(s.toString())) {
 *                                     tvErrorHint.setVisibility(View.VISIBLE);
 *                                     tvErrorHint.setText(s.toString() + "中有特殊字符");
 *                                 } else {
 *                                     tvErrorHint.setVisibility(View.GONE);
 *                                     tvErrorHint.setText("");
 *                                 }
 *                             }
 *
 *                             @Override
 *                             public void afterTextChanged(Editable s) {
 *                             }
 *                         }).setOnClickListener(new WpkCommEditDialog.OnHintDialogListener() {
 *                     @Override
 *                     public void onClickOk(String content) {
 *                     }
 *
 *                     @Override
 *                     public void onClickCancel() {
 *                     }
 *                 }).show();
 */
public class InputDialog extends Dialog{

    private TextView tvTitle;

    private TextView tbLeft;
    private TextView tbRight;
    private TextView tvErrorHint;
    private WpkEditText wet_count;

    private OnHintDialogListener mListener;
    private OnTextChangeListener mTextChangeListener;

    private int limitNum = 0;
    private boolean isAllowEmpty = false;


    public InputDialog(Context context) {
        super(context, R.style.Theme_AppCompat_Dialog_Alert);
        setContentView(R.layout.dialog_input);
        // 设置宽高
        getWindow().setLayout(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.CENTER);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        tvTitle = findViewById(R.id.tv_title);

        tbLeft = findViewById(R.id.tb_left);
        tbRight = findViewById(R.id.tb_right);
        wet_count = findViewById(R.id.wet_count);

        tvErrorHint = findViewById(R.id.tv_error_hint);





        tbLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onClickCancel();
                    dismiss();
                }
            }
        });
        tbRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onClickOk(wet_count.getCount()+"");
                    dismiss();
                }
            }
        });


        setCancelable(false);
        setCanceledOnTouchOutside(false);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    public static InputDialog create(Context context, final int style){
        return new InputDialog(context);
    }

    public static InputDialog create(Context context){
        return new InputDialog(context);
    }

    public InputDialog setTitle(String titleContent){
        setTitleText(titleContent);
        return this;
    }

    public InputDialog hideTitle(boolean isHideTitle){
        if(isHideTitle){
            hideTitle();
        }else{
            showTitle();
        }
        return this;
    }





    public InputDialog setLeftBtnText(String text){
        setLeftText(text);
        return this;
    }

    public InputDialog setRightBtnText(String text){
        setRightText(text);
        return this;
    }

    public InputDialog setRightHintText(String text){
        setRightText(text);
        return this;
    }



    public InputDialog setErrorText(String errorText){
        tvErrorHint.setVisibility(View.VISIBLE);
        tvErrorHint.setText(errorText);
        return this;
    }

    public InputDialog setOnTextChangeListener(OnTextChangeListener listener){
        mTextChangeListener = listener;
        return this;
    }

    public InputDialog setOnClickListener(OnHintDialogListener listener) {
        this.mListener = listener;
        return this;
    }


    public void hideTitle() {

        tvTitle.setVisibility(View.GONE);
    }

    public void showTitle() {
        tvTitle.setVisibility(View.VISIBLE);
    }

    public void setTitleText(String titleText) {
        tvTitle.setText(titleText);
        tvTitle.setVisibility(View.VISIBLE);
    }



    public void setLeftText(String leftText) {
        tbLeft.setText(leftText);
    }

    public void hideLeft() {
        findViewById(R.id.view_stand).setVisibility(View.GONE);
        tbLeft.setVisibility(View.GONE);
    }

    public void showLeft() {
        tbLeft.setVisibility(View.VISIBLE);
    }

    public void setRightText(String rightText) {
        tbRight.setText(rightText);
    }

    public void hideRight() {
        findViewById(R.id.view_stand).setVisibility(View.GONE);
        tbRight.setVisibility(View.GONE);
    }

    @Override
    public void show() {
        super.show();
    }

    /**
     * 设置回调接口
     */

    public static class SimpleOnHintDialogListener implements OnHintDialogListener {
        @Override
        public void onClickOk(String content) {
        }

        @Override
        public void onClickCancel() {
        }

    }

    /**
     * 设置回调接口
     */

    public interface OnHintDialogListener {
        public void onClickOk(String content);

        public void onClickCancel();

    }

    public interface OnTextChangeListener {
        void beforeTextChanged(CharSequence s, int start, int count, int after);

        void onTextChanged(CharSequence s, int start, int before, int count,TextView tvErrorHint);

        void afterTextChanged(Editable s);

    }



}
