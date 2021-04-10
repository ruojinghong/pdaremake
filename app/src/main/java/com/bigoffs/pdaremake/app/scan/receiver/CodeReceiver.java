package com.bigoffs.pdaremake.app.scan.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


/**
 * Created by okbuy on 17-1-17.
 */
@Deprecated
public class CodeReceiver extends BroadcastReceiver {

    private OnReceiverListener mOnReceiverListener;

    public CodeReceiver(OnReceiverListener listener) {
        super();
        mOnReceiverListener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
//        String code = intent.getExtras().getString(CODE_NAME);
//        if (mOnReceiverListener != null) {
//            mOnReceiverListener.onReceiverData(code);
//        }
    }
}
