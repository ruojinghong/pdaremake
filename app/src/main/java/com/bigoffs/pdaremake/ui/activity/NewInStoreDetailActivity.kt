package com.bigoffs.pdaremake.ui.activity

import android.graphics.Color
import android.os.Bundle
import androidx.activity.viewModels
import androidx.collection.arraySetOf
import androidx.lifecycle.Observer
import com.bigoffs.pdaremake.R
import com.bigoffs.pdaremake.app.base.BaseScanActivity
import com.bigoffs.pdaremake.app.ext.initTitle
import com.bigoffs.pdaremake.app.ext.showMessage
import com.bigoffs.pdaremake.app.util.SoundUtils
import com.bigoffs.pdaremake.data.model.bean.NewInStoreErrorBean
import com.bigoffs.pdaremake.data.model.bean.NewInStoreNormalBean
import com.bigoffs.pdaremake.databinding.ActivityMainBinding
import com.bigoffs.pdaremake.databinding.ActivityMainBindingImpl
import com.bigoffs.pdaremake.databinding.ActivityNewInstoreDetailBinding
import com.bigoffs.pdaremake.viewmodel.request.RequestInStroreDetailViewModel
import com.bigoffs.pdaremake.viewmodel.state.NewInStoreDetailViewModel
import com.blankj.utilcode.util.ToastUtils
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.ext.parseState

/**
 *User:Kirito
 *Time:2021/5/10  22:18
 *Desc:新品入库detailactivity
 */
class NewInStoreDetailActivity : BaseScanActivity<NewInStoreDetailViewModel, ActivityNewInstoreDetailBinding>() {

    val set = arraySetOf<String>()

    val requestInStroreDetailViewModel:RequestInStroreDetailViewModel by viewModels()

    override fun layoutId(): Int = R.layout.activity_new_instore_detail

    override fun setStatusBar() {
        initTitle(false, biaoti = "新品入库")
    }

    override fun onReceiverData(data: String) {
                when(mViewModel.currentFocus.value){
                    1->{
                        if(mViewModel.currentUniqueSet.add(data)){
                                if(mViewModel.alReadyInStoreSet.contains(data)){
                                    beep()
                                    ToastUtils.showShort("店内码已入库")
                                }else{
                                    mDatabind.etUnique.setText(data)
                                    mDatabind.etBarcode.requestFocus()
                                }


                        }else{
                           beep()
                        }


                    }
                    2->{
                        if(mViewModel.currentBarCodeSet.add(data)){
                            mDatabind.etBarcode.setText(data)
                            addErrorOrNormalList("2222")
                            mDatabind.etUnique.requestFocus()




                        }else{
                           beep()
                        }

                    }
                    3->{
                        mDatabind.etShelf.setText(data)
                        mDatabind.etUnique.requestFocus()
                    }

                }
    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)


        mDatabind.vm = mViewModel
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

        requestInStroreDetailViewModel.getInStoreDetail(1)

    }

    override fun createObserver() {
        super.createObserver()

        requestInStroreDetailViewModel.detail.observe(this, Observer {

                parseState(it,{ storeDetail ->
                    mViewModel.goodsCount.value = storeDetail.task_list.unique_code_list.size+storeDetail.in_store_list.unique_code_list.size
                    mViewModel.inStoreCount.value = storeDetail.in_store_list.unique_code_list.size
                    mViewModel.thisCount.value = 0
                    mViewModel.detail.value = storeDetail
                    mViewModel.alReadyInStoreSet.addAll(storeDetail.in_store_list.unique_code_list)
                    for (map in storeDetail.task_list.sku_list){
                        if(storeDetail.in_store_list.sku_list.containsKey(map.key)){
                                mViewModel.currentSkuNumMap.put(map.key,map.value - storeDetail.in_store_list.sku_list.get(map.key)!!)
                        }

                    }



                },{

                   ToastUtils.showShort(it.errorMsg)
                   finish()

                })

        })

    }



    fun addErrorOrNormalList(barcode:String){

            if(mViewModel.detail.value?.barcode_sku_map?.containsKey(barcode) == true){
                val sku = mViewModel.detail.value?.barcode_sku_map?.get(barcode).toString()
                if(mViewModel.detail.value!!.in_store_list.sku_list.get(sku) == null){
                    mViewModel.currenErrorList.add(NewInStoreErrorBean(barcode))
                }else{
                    if(mViewModel.currentSkuNumMap.containsKey(sku)){
                        var num = mViewModel.currentSkuNumMap.get(sku)
                        if (num != null) {
                            if(num <= 0){
                                mViewModel.currenErrorList.add(NewInStoreErrorBean(barcode))
                            }else{
                                num--?.let { mViewModel.currentSkuNumMap.put(sku, it) }
                                mViewModel.currenNormalList.add(NewInStoreNormalBean("",mDatabind.etUnique.text.toString(),barcode,mViewModel.detail.value!!.in_store_list.sku_list.get(sku)!!))
                            }
                        }

                    }else{
                        mViewModel.currenErrorList.add(NewInStoreErrorBean(barcode))
                    }

                }
            }else{
                mViewModel.currenErrorList.add(NewInStoreErrorBean(barcode))

            }

    }


}