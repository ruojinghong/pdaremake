package com.bigoffs.pdaremake.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.bigoffs.pdaremake.R;

/**
 *  WpkHintDialog.create(getActivity(), WpkHintDialog.STYLE_ONLY_OK)
 *                         .setTitle("HintDialog1")
 *                         .setContent("Template App is a test program for third-party companies to integrate and test plug-ins. It consists of Wyze Platform Kit and test engineering.")
 *                         .setRightBtnText("OK")
 *                         .setDialogListener(new WpkHintDialog.SimpleOnHintDialogListener())
 *                         .show();
 */


public class HintDialog extends Dialog {

    private TextView tvTitle;
    private TextView tvContent;
    private TextView tbLeft;
    private TextView tbRight;
    private TextView tvOk;
    private TextView tvCancel;

    private OnHintDialogListener mListener;

    public static final int STYLE_CANCEL_OK = 0;
    public static final int STYLE_ONLY_OK = 1;
    public static final int STYLE_ALL = 2;

    public HintDialog(Context context, final int style) {
        super(context, R.style.Theme_AppCompat_Dialog_Alert);
        setContentView(R.layout.dialog_hint);
        // 设置宽高
        getWindow().setLayout(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.CENTER);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        tvTitle = findViewById(R.id.tv_title);
        tvContent = findViewById(R.id.tv_content);
        tbLeft = findViewById(R.id.tb_left);
        tbRight = findViewById(R.id.tb_right);
        tvOk = findViewById(R.id.tv_ok);
        tvCancel = findViewById(R.id.tv_cancel);
        tvContent.setVisibility(View.GONE);
        

        switch (style) {
            case STYLE_CANCEL_OK:
                tvOk.setVisibility(View.GONE);
                tvCancel.setVisibility(View.GONE);
                findViewById(R.id.view_line_1).setVisibility(View.GONE);
                findViewById(R.id.view_line_2).setVisibility(View.GONE);
                break;
            case STYLE_ONLY_OK:
                tvOk.setVisibility(View.GONE);
                tvCancel.setVisibility(View.GONE);
                tbLeft.setVisibility(View.GONE);
                findViewById(R.id.view_stand).setVisibility(View.GONE);
                findViewById(R.id.view_line_1).setVisibility(View.GONE);
                findViewById(R.id.view_line_2).setVisibility(View.GONE);
                break;
            case STYLE_ALL:
                tbLeft.setVisibility(View.GONE);
                findViewById(R.id.view_stand).setVisibility(View.GONE);
                tbRight.setVisibility(View.VISIBLE);
                break;
        }


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
                if (null != mListener) {
                    if (style == STYLE_ALL) {
                        mListener.onClickOther();
                    } else {
                        mListener.onClickOk();
                    }
                    dismiss();
                }
            }
        });
        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onClickOk();
                    dismiss();
                }
            }
        });

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onClickCancel();
                    dismiss();
                }
            }
        });

        setCancelable(true);
        setCanceledOnTouchOutside(true);

//        mHintDialog = this;
    }

//    private static HintDialog mHintDialog;

    public static HintDialog create(Context context, final int style) {
        return new HintDialog(context, style);
    }

    public HintDialog setTitle(String titleContent) {
        setTitleText(titleContent);
        return this;
    }

    public HintDialog hideTitle(boolean isHideTitle) {
        if (isHideTitle) {
            hideTitle();
        } else {
            showTitle();
        }
        return this;
    }

    public HintDialog showTitle(boolean isShow) {
        if (isShow) {
            showTitle();
        } else {
            hideTitle();
        }
        return this;
    }



    public HintDialog setContent(String content) {
        setContentText(content);
        return this;
    }

    public HintDialog hideContent(boolean isHideContent) {
        if (isHideContent) {
            hideContent();
        } else {
            showContent();
        }
        return this;
    }

    public HintDialog setLeftBtnText(String text) {
        setLeftText(text);
        return this;
    }

    public HintDialog setRightBtnText(String text) {
        setRightText(text);
        return this;
    }

    public HintDialog setRightBtnColor(int color) {
        tbRight.setTextColor(color);
        return this;
    }

    public HintDialog setLeftBtnColor(int color) {
        tbLeft.setTextColor(color);
        return this;
    }

    public HintDialog setMiddleColor(int color) {
        tvOk.setTextColor(color);
        return this;
    }

    public void setDoneSizeAndBold(int size, boolean isBold) {
        if (tvOk == null || tvCancel == null || tbRight == null || tbLeft == null) {
            return;
        }
        tvOk.setTextSize(size);
        tvCancel.setTextSize(size);
        tbRight.setTextSize(size);
        tbLeft.setTextSize(size);
        if (isBold) {
            tvOk.setTypeface(Typeface.DEFAULT_BOLD);
            tvCancel.setTypeface(Typeface.DEFAULT_BOLD);
            tbRight.setTypeface(Typeface.DEFAULT_BOLD);
            tbLeft.setTypeface(Typeface.DEFAULT_BOLD);
        }
    }

    public HintDialog setTopBtnText(String text) {
        setTopText(text);
        return this;
    }

    public HintDialog setMiddleBtnText(String text) {
        setMiddleText(text);
        return this;
    }

    public HintDialog setBottomBtnText(String text) {
        setBottomText(text);
        return this;
    }

    public HintDialog setDialogListener(SimpleOnHintDialogListener listener) {
        setOnListener(listener);
        return this;
    }

    public HintDialog setDialogListener(OnHintDialogListener listener) {
        setOnListener(listener);
        return this;
    }


    public void showDialog() {
        show();
    }


    public void hideTitle() {

        tvTitle.setVisibility(View.GONE);
    }

    public void showTitle() {
        tvTitle.setVisibility(View.VISIBLE);
    }

    public void setTopText(String text) {
        tbRight.setText(text);
        tbRight.setVisibility(View.VISIBLE);
    }

    //文字加粗
    public void setTopTextBold() {
        tbRight.setTypeface(Typeface.DEFAULT_BOLD);
    }

    public void setTitleBold() {
        tvTitle.setTypeface(Typeface.DEFAULT_BOLD);
    }

    public void setMiddleText(String text) {
        tvOk.setText(text);
        tvOk.setVisibility(View.VISIBLE);
    }

    public void setBottomText(String text) {
        tvCancel.setText(text);
        tvCancel.setVisibility(View.VISIBLE);
    }

    public void hideContent() {
        tvContent.setVisibility(View.GONE);
    }

    public void showContent() {
        tvContent.setVisibility(View.VISIBLE);
    }

    public void setContentText(CharSequence contentText) {
        tvContent.setText(contentText);
        tvContent.setVisibility(View.VISIBLE);
    }

    public void setTitleText(String titleText) {
        tvTitle.setText(titleText);
        tvTitle.setVisibility(View.VISIBLE);
    }

    public void addContentText(CharSequence contentText) {
        tvContent.setVisibility(View.VISIBLE);
        tvContent.append(contentText);
    }

    public void addContentText(CharSequence contentText, int color) {
        tvContent.setVisibility(View.VISIBLE);
        if (TextUtils.isEmpty(contentText)) contentText = " ";
        SpannableString spanString = new SpannableString(contentText);
        ForegroundColorSpan span = new ForegroundColorSpan(color);
        spanString.setSpan(span, 0, contentText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvContent.append(spanString);
    }

    public void addContentText(CharSequence contentText, int color, int sizeDp) {
        tvContent.setVisibility(View.VISIBLE);
        if (TextUtils.isEmpty(contentText)) contentText = " ";
        SpannableString spanString = new SpannableString(contentText);
        CharacterStyle span = new ForegroundColorSpan(color);
        spanString.setSpan(span, 0, contentText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        span = new AbsoluteSizeSpan((int) (sizeDp * getContext().getResources().getDisplayMetrics().density));
        spanString.setSpan(span, 0, contentText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvContent.append(spanString);
    }

    public TextView getContentText() {
        return tvContent;
    }

    public void setLeftText(String leftText) {
        tbLeft.setVisibility(View.VISIBLE);
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
        tbRight.setVisibility(View.VISIBLE);
        tbRight.setText(rightText);
    }

    public void hideRight() {
        findViewById(R.id.view_line_top).setVisibility(View.GONE);
        findViewById(R.id.view_stand).setVisibility(View.GONE);
        tbRight.setVisibility(View.GONE);
    }

    public void setOnListener(OnHintDialogListener listener) {
        this.mListener = listener;
    }


    /**
     * 设置回调接口
     */

    public interface OnHintSureListener {
        void OnHintSure();
    }

    /**
     * 设置回调接口
     */

    public static class SimpleOnHintDialogListener implements OnHintDialogListener {
        @Override
        public void onClickOk() {
        }

        @Override
        public void onClickCancel() {
        }

        @Override
        public void onClickOther() {

        }
    }

    /**
     * 设置回调接口
     */

    public interface OnHintDialogListener {
        public void onClickOk();

        public void onClickCancel();

        public void onClickOther();
    }


}
