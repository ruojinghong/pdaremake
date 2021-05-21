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
public class EditDialog extends Dialog{

    private TextView tvTitle;
    private EditText etContent;
    private TextView tbLeft;
    private TextView tbRight;
    private TextView tvTextNum;
    private TextView tvErrorHint;

    private OnHintDialogListener mListener;
    private OnTextChangeListener mTextChangeListener;

    private int limitNum = 0;
    private boolean isAllowEmpty = false;


    public EditDialog(Context context) {
        super(context, R.style.Theme_AppCompat_Dialog_Alert);
        setContentView(R.layout.dialog_edit);
        // 设置宽高
        getWindow().setLayout(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.CENTER);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        tvTitle = findViewById(R.id.tv_title);
        etContent = findViewById(R.id.et_content);
        tbLeft = findViewById(R.id.tb_left);
        tbRight = findViewById(R.id.tb_right);
        tvTextNum = findViewById(R.id.tv_text_num);
        tvErrorHint = findViewById(R.id.tv_error_hint);


        etContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(mTextChangeListener!=null){
                    mTextChangeListener.beforeTextChanged(s,start,count,after);
                }
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(mTextChangeListener!=null){
                    mTextChangeListener.onTextChanged(s,start,before,count,tvErrorHint);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
                if(mTextChangeListener!=null){
                    mTextChangeListener.afterTextChanged(s);
                }

                tvTextNum.setText(s.length()+"/"+limitNum);
//                if(s.length()>limitNum){
//                    tbRight.setEnabled(false);
//                    tbRight.setTextColor(getContext().getResources().getColor(R.color.wyze_off_disabled));
//                    tvTextNum.setTextColor(getContext().getResources().getColor(R.color.wyze_text_BE4027));
//                }else if(!isAllowEmpty && s.length() <= 0){
//                    tbRight.setEnabled(false);
//                    tbRight.setTextColor(getContext().getResources().getColor(R.color.wyze_off_disabled));
//
//                }else{
//                    tvTextNum.setTextColor(getContext().getResources().getColor(R.color.wyze_text_DEE3E3));
//                    if(tvErrorHint.getVisibility() == View.VISIBLE){
//                        tbRight.setEnabled(false);
//                        tbRight.setTextColor(getContext().getResources().getColor(R.color.wyze_off_disabled));
//                    }else{
//                        tbRight.setEnabled(true);
//                        tbRight.setTextColor(getContext().getResources().getColor(R.color.wyze_text_color_1C9E90));
//                    }
//                }
            }
        });


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
                    mListener.onClickOk(etContent.getText().toString());
                    dismiss();
                }
            }
        });


        setCancelable(false);
        setCanceledOnTouchOutside(false);
        etContent.setFocusable(true);
        etContent.setFocusableInTouchMode(true);
        etContent.requestFocus();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    public static EditDialog create(Context context,final int style){
        return new EditDialog(context);
    }

    public static EditDialog create(Context context){
        return new EditDialog(context);
    }

    public EditDialog setTitle(String titleContent){
        setTitleText(titleContent);
        return this;
    }

    public EditDialog hideTitle(boolean isHideTitle){
        if(isHideTitle){
            hideTitle();
        }else{
            showTitle();
        }
        return this;
    }

    public EditDialog setContent(String content){
        setContentText(content);
        return this;
    }

    public EditDialog setHintText(String hintText){
        etContent.setHint(hintText);
        return this;
    }

    public EditDialog isAllowEmpty(boolean isAllowEmpty){
        this.isAllowEmpty = isAllowEmpty;
        etContent.setText(etContent.getText());
        return this;
    }

    public EditDialog setLeftBtnText(String text){
        setLeftText(text);
        return this;
    }

    public EditDialog setRightBtnText(String text){
        setRightText(text);
        return this;
    }

    public EditDialog setRightHintText(String text){
        setRightText(text);
        return this;
    }

    public EditDialog setWordLimit(int num){
        limitNum = num;
        tvTextNum.setText(etContent.getText().length()+"/"+num);
        etContent.setText(etContent.getText());
        return this;
    }

    public EditDialog setErrorText(String errorText){
        tvErrorHint.setVisibility(View.VISIBLE);
        tvErrorHint.setText(errorText);
        return this;
    }

    public EditDialog setOnTextChangeListener(OnTextChangeListener listener){
        mTextChangeListener = listener;
        return this;
    }

    public EditDialog setOnClickListener(OnHintDialogListener listener) {
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

    public void setContentText(CharSequence contentText) {
        etContent.setText(contentText);
        etContent.setVisibility(View.VISIBLE);
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
        etContent.requestFocus();
        etContent.setSelection(etContent.getText().toString().length());
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
