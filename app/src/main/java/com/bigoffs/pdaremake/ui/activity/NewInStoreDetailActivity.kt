package com.bigoffs.pdaremake.ui.activity

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.collection.arraySetOf
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bigoffs.pdaremake.R
import com.bigoffs.pdaremake.app.base.BaseScanActivity
import com.bigoffs.pdaremake.app.ext.init
import com.bigoffs.pdaremake.app.ext.initTitle
import com.bigoffs.pdaremake.data.model.bean.NewInStoreErrorBean
import com.bigoffs.pdaremake.data.model.bean.NewInStoreNormalBean
import com.bigoffs.pdaremake.databinding.ActivityNewInstoreDetailBinding
import com.bigoffs.pdaremake.ui.adapter.NewInStoreErrorAdapter
import com.bigoffs.pdaremake.ui.adapter.NewInStoreNormalAdapter
import com.bigoffs.pdaremake.viewmodel.request.RequestInStroreDetailViewModel
import com.bigoffs.pdaremake.viewmodel.state.NewInStoreDetailViewModel
import com.blankj.utilcode.util.ToastUtils
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import me.hgj.jetpackmvvm.ext.parseState

/**
 *User:Kirito
 *Time:2021/5/10  22:18
 *Desc:新品入库detailactivity
 */
class NewInStoreDetailActivity : BaseScanActivity<NewInStoreDetailViewModel, ActivityNewInstoreDetailBinding>() {

    val set = arraySetOf<String>()

    val requestInStroreDetailViewModel:RequestInStroreDetailViewModel by viewModels()

    private lateinit var errorRecyclerView : RecyclerView
    private lateinit var normalRecyclerView : RecyclerView
    private lateinit var errorBottomsheetDialog : BottomSheetDialog
    private lateinit var normalBottomsheetDialog : BottomSheetDialog

    //适配器
    private val errorAdapter: NewInStoreErrorAdapter by lazy { NewInStoreErrorAdapter(arrayListOf()) }
    private val normalAdapter: NewInStoreNormalAdapter by lazy { NewInStoreNormalAdapter(arrayListOf()) }

    override fun layoutId(): Int = R.layout.activity_new_instore_detail

    override fun setStatusBar() {
        initTitle(false, biaoti = "新品入库")
    }

    override fun onReceiverData(data: String) {

                when(mViewModel.currentFocus.value){
                    //添加店内码
                    1->{
                        if(mViewModel.currentUniqueSet.contains(data)){

                            ToastUtils.showShort("店内码已存在")
                            beep()

                        }else{
                            mViewModel.currentUniqueSet.add(data)
                            if(mViewModel.alReadyInStoreSet.contains(data)){
                                beep()
                                ToastUtils.showShort("店内码已入库")
                            }else{
                                mDatabind.etUnique.setText(data)
                                mDatabind.etBarcode.requestFocus()
                            }

                        }
                    }
                    //添加条形码
                    2->{
                        if(mViewModel.currentBarCodeSet.contains(data)){
                          beep()
                            ToastUtils.showShort("条形码已存在")
                        }else{
                            mViewModel.currentBarCodeSet.add(data)
                            mDatabind.etBarcode.setText(data)
                            addErrorOrNormalList(data)
                            mDatabind.etUnique.requestFocus()

                        }

                    }
                    //添加货架号
                    3->{
                        mDatabind.etShelf.setText(data)
                        mDatabind.etUnique.requestFocus()
//                        addNormalList(data)
                        normalAdapter.data.forEach{
                            if(it.shelf == ""){
                                it.shelf = data
                            }
                        }
                        normalAdapter.notifyDataSetChanged()

                    }

                }
    }

    private fun initBottomSheet() {
        var view1= View.inflate(this,R.layout.bottom_newinstoreerror,null)
        errorRecyclerView  = view1.findViewById(R.id.dialog_recycleView)
        errorRecyclerView.init(LinearLayoutManager(this),errorAdapter)
        errorBottomsheetDialog = BottomSheetDialog(this,R.style.dialog)
        errorBottomsheetDialog.setContentView(view1)
        BottomSheetBehavior.from(view1.parent as View).peekHeight = getPeekHeight()

        var view2= View.inflate(this,R.layout.bottom_newinstorenormal,null)
        normalRecyclerView  = view2.findViewById(R.id.dialog_recycleView)
        normalRecyclerView.init(LinearLayoutManager(this),normalAdapter)
        normalBottomsheetDialog = BottomSheetDialog(this,R.style.dialog)
        normalBottomsheetDialog.setContentView(view2)
        BottomSheetBehavior.from(view2.parent as View).peekHeight = getPeekHeight()
    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)


        mDatabind.vm = mViewModel
        mDatabind.click = ProxyClick()
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

        initBottomSheet();

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
                        }else{
                            mViewModel.currentSkuNumMap.put(map.key,map.value)
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
//                if(mViewModel.detail.value!!.in_store_list.sku_list.get(sku) == null){
//                    addErrorList(barcode)
//                }else{
                    if(mViewModel.currentSkuNumMap.containsKey(sku)){
                        var num = mViewModel.currentSkuNumMap.get(sku)
                        if (num != null) {
                            if(num <= 0){
                                addErrorList(barcode)
                            }else{
                                num--?.let { mViewModel.currentSkuNumMap.put(sku, it) }

                                normalAdapter.data.forEach{
                                    if(it.barcode == barcode){
                                        it.num++
                                        normalAdapter.notifyDataSetChanged()
                                        return
                                    }
                                }

                                normalAdapter.addData(NewInStoreNormalBean("",barcode,mDatabind.etUnique.text.toString(),1))
                            }
                        }

                    }else{
                        addErrorList(barcode)
                    }

//                }
            }else{
                addErrorList(barcode)

            }

    }
    /**
     * 弹窗高度，默认为屏幕高度的四分之三
     * 子类可重写该方法返回peekHeight
     *
     * @return height
     */
    protected fun getPeekHeight(): Int {
        val peekHeight = resources.displayMetrics.heightPixels
        //设置弹窗高度为屏幕高度的3/4
        return peekHeight - peekHeight / 3
    }

    fun addErrorList(barcode: String){
        errorAdapter.addData(NewInStoreErrorBean(barcode))
    }

    fun addNormalList(shelf:String){
        mViewModel.currenNormalList.forEach {
            it.shelf = shelf
        }
        normalAdapter.addData(mViewModel.currenNormalList)
        mViewModel.currenNormalList.clear()
    }

    inner class ProxyClick{
        fun openErrorBottomSheet(){
            errorBottomsheetDialog.show()
        }
        fun openNormalBottomSheet(){
            normalBottomsheetDialog.show()

        }

    }

}