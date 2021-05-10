package com.bigoffs.pdaremake.ui.activity

import android.graphics.Color
import android.os.Bundle
import com.bigoffs.pdaremake.R
import com.bigoffs.pdaremake.app.base.BaseScanActivity
import com.bigoffs.pdaremake.app.ext.initTitle
import com.bigoffs.pdaremake.databinding.ActivityMainBinding
import com.bigoffs.pdaremake.databinding.ActivityMainBindingImpl
import com.bigoffs.pdaremake.databinding.ActivityNewInstoreDetailBinding
import com.bigoffs.pdaremake.viewmodel.state.NewInStoreDetailViewModel
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel

/**
 *User:Kirito
 *Time:2021/5/10  22:18
 *Desc:新品入库detailactivity
 */
class NewInStoreDetailActivity : BaseScanActivity<NewInStoreDetailViewModel, ActivityNewInstoreDetailBinding>() {
    override fun layoutId(): Int = R.layout.activity_new_instore_detail

    override fun setStatusBar() {
        initTitle(false, biaoti = "新品入库")
    }

    override fun onReceiverData(data: String) {
                when(mViewModel.currentFocus.value){
                    1->{

                        mDatabind.etUnique.setText(data)
                        mDatabind.etBarcode.requestFocus()

                    }
                    2->{
                        mDatabind.etBarcode.setText(data)
                        mDatabind.etUnique.requestFocus()
                    }
                    3->{
                        mDatabind.etShelf.setText(data)
                        mDatabind.etUnique.requestFocus()
                    }

                }
    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)



        mDatabind.etUnique.setOnFocusChangeListener(){v,hasFocus ->
            if(hasFocus){
                mViewModel.currentFocus.value = 1
                mDatabind.devideUnique.setBackgroundColor(Color.parseColor("#0033cc"))
            }else{
                mDatabind.devideUnique.setBackgroundColor(Color.parseColor("#EEEEEE"))
            }
        }
        mDatabind.etBarcode.setOnFocusChangeListener(){v,hasFocus ->
            if(hasFocus){
                mViewModel.currentFocus.value = 2
                mDatabind.devideBarcode.setBackgroundColor(Color.parseColor("#0033cc"))
            }else{
                mDatabind.devideBarcode.setBackgroundColor(Color.parseColor("#EEEEEE"))
            }
        }
        mDatabind.etShelf.setOnFocusChangeListener(){v,hasFocus ->
            if(hasFocus){
                mViewModel.currentFocus.value = 3
                mDatabind.devideShelf.setBackgroundColor(Color.parseColor("#0033cc"))
            }else{
                mDatabind.devideShelf.setBackgroundColor(Color.parseColor("#EEEEEE"))
            }
        }

    }
}