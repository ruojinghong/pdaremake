package com.bigoffs.pdaremake.ui.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.collection.arraySetOf
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bigoffs.pdaremake.R
import com.bigoffs.pdaremake.app.base.BaseRfidFActivity
import com.bigoffs.pdaremake.app.base.BaseScanActivity
import com.bigoffs.pdaremake.app.ext.addOnNoneEditorActionListener
import com.bigoffs.pdaremake.app.ext.init
import com.bigoffs.pdaremake.app.ext.initTitle
import com.bigoffs.pdaremake.app.util.DeviceUtil
import com.bigoffs.pdaremake.data.model.bean.InStoreBean
import com.bigoffs.pdaremake.data.model.bean.NewInStoreErrorBean
import com.bigoffs.pdaremake.data.model.bean.NewInStoreNormalBean
import com.bigoffs.pdaremake.databinding.ActivityNewInstoreDetailBinding
import com.bigoffs.pdaremake.databinding.ActivityNewInstoreRfidDetailBinding
import com.bigoffs.pdaremake.ui.adapter.NewInStoreErrorAdapter
import com.bigoffs.pdaremake.ui.adapter.NewInStoreNormalAdapter
import com.bigoffs.pdaremake.ui.dialog.EditDialog
import com.bigoffs.pdaremake.ui.dialog.HintDialog
import com.bigoffs.pdaremake.viewmodel.request.RequestInStroreDetailViewModel
import com.bigoffs.pdaremake.viewmodel.state.NewInStoreDetailViewModel
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import me.hgj.jetpackmvvm.ext.parseState

/**
 *User:Kirito
 *Time:2021/5/10  22:18
 *Desc:新品入库detailactivity
 */
class NewInStoreByUniqueDetailRfidActivity :
    BaseRfidFActivity<NewInStoreDetailViewModel, ActivityNewInstoreRfidDetailBinding>() {

    val set = arraySetOf<String>()

    val requestInStroreDetailViewModel: RequestInStroreDetailViewModel by viewModels()

    private lateinit var errorRecyclerView: RecyclerView
    private lateinit var normalRecyclerView: RecyclerView
    private lateinit var errorBottomsheetDialog: BottomSheetDialog
    private lateinit var normalBottomsheetDialog: BottomSheetDialog

    private lateinit var errorBottomSheetNum: TextView
    private lateinit var normalBottomSheetNum: TextView
    lateinit var task: InStoreBean
    private lateinit var editDialog: EditDialog


    //适配器
    private val errorAdapter: NewInStoreErrorAdapter by lazy { NewInStoreErrorAdapter(arrayListOf()) }
    private val normalAdapter: NewInStoreNormalAdapter by lazy { NewInStoreNormalAdapter(arrayListOf()) }

    override fun layoutId(): Int = R.layout.activity_new_instore_rfid_detail

    override fun setStatusBar() {
        initTitle(false, biaoti = "新品入库")
    }


     fun onReceiverData(data: String) {

        if(editDialog.isShowing){
                editDialog.setContentText(data)
        }else{
            when (mViewModel.currentFocus.value) {
                //添加店内码
                1 -> {
                    if (mViewModel.currentUniqueSet.contains(data)) {

                        ToastUtils.showShort("店内码已存在")
                        beep()

                    } else {
                        mViewModel.currentUniqueSet.add(data)
                        if (mViewModel.alReadyInStoreSet.contains(data)) {
                            beep()
                            ToastUtils.showShort("店内码已入库")
                        } else {
//                            mDatabind.etUnique.setText(data)
                            mViewModel.currentUnique.value = data
                            mDatabind.etBarcode.requestFocus()
                        }

                    }
                }
                //添加条形码
                2 -> {
                    if (mViewModel.currentBarCodeSet.contains(data)) {
                        beep()
                        ToastUtils.showShort("条形码已存在")
                    } else {
                        mViewModel.currentBarCodeSet.add(data)
//                        mDatabind.etBarcode.setText(data)
                        addErrorOrNormalList(data)
                        mDatabind.etUnique.requestFocus()

                    }

                }
                //添加货架号
                3 -> {
//                    mDatabind.etShelf.setText(data)
                    mDatabind.etUnique.requestFocus()
//                        addNormalList(data)
                    normalAdapter.data.forEach {
                        if (it.shelf_code == "") {
                            it.shelf_code = data
                        }
                    }
                    normalAdapter.notifyDataSetChanged()

                }

            }
        }


    }

    private fun initBottomSheet() {
        var view1 = View.inflate(this, R.layout.bottom_newinstoreerror, null)
        errorRecyclerView = view1.findViewById(R.id.dialog_recycleView)
        errorRecyclerView.init(LinearLayoutManager(this), errorAdapter)
        errorBottomsheetDialog = BottomSheetDialog(this, R.style.dialog)
        errorBottomsheetDialog.setContentView(view1)
        view1.findViewById<ImageView>(R.id.iv_unfold).setOnClickListener {
            errorBottomsheetDialog.dismiss()
        }

        errorBottomSheetNum = view1.findViewById(R.id.tv_bottom_error_num)
        BottomSheetBehavior.from(view1.parent as View).peekHeight = getPeekHeight()

        var view2 = View.inflate(this, R.layout.bottom_newinstorenormal, null)
        normalRecyclerView = view2.findViewById(R.id.dialog_recycleView)
        view2.findViewById<ImageView>(R.id.iv_unfold).setOnClickListener {
            normalBottomsheetDialog.dismiss()
        }
        normalRecyclerView.init(LinearLayoutManager(this), normalAdapter)
        normalBottomsheetDialog = BottomSheetDialog(this, R.style.dialog)
        normalBottomsheetDialog.setContentView(view2)
        normalBottomSheetNum = view2.findViewById(R.id.tv_bottom_normal_num)
        BottomSheetBehavior.from(view2.parent as View).peekHeight = getPeekHeight()
    }

    override fun initView(savedInstanceState: Bundle?) {



        mDatabind.vm = mViewModel
        mDatabind.click = ProxyClick()
        task = intent.getParcelableExtra<InStoreBean>("task")!!
        if (task != null) {
            mViewModel.taskNo.value = "入库批次：${task.in_stock_no}"
        }

        mDatabind.etUnique.setOnFocusChangeListener() { v, hasFocus ->
            if (hasFocus) {
                mViewModel.currentFocus.value = 1
                mDatabind.devideUnique.setBackgroundColor(Color.parseColor("#0033cc"))
            } else {
                mDatabind.devideUnique.setBackgroundColor(Color.parseColor("#EEEEEE"))
            }
        }
        mDatabind.etBarcode.setOnFocusChangeListener() { v, hasFocus ->
            if (hasFocus) {
                mViewModel.currentFocus.value = 2
                mDatabind.devideBarcode.setBackgroundColor(Color.parseColor("#0033cc"))
            } else {
                mDatabind.devideBarcode.setBackgroundColor(Color.parseColor("#EEEEEE"))
            }
        }
        mDatabind.etShelf.setOnFocusChangeListener() { v, hasFocus ->
            if (hasFocus) {
                mViewModel.currentFocus.value = 3
                mDatabind.devideShelf.setBackgroundColor(Color.parseColor("#0033cc"))
            } else {
                mDatabind.devideShelf.setBackgroundColor(Color.parseColor("#EEEEEE"))
            }
        }

        task?.id?.let {
            requestInStroreDetailViewModel.getInStoreDetail(it)
        }

        initBottomSheet();
        editDialog = EditDialog.create(mContext)
            .setTitle("请扫描剔除商品编码")
            .setHintText("请扫描店内码或条形码")
            .setOnClickListener(object : EditDialog.OnHintDialogListener{
                override fun onClickOk(content: String?) {
                    deleteItem(editDialog.genContentText())
                    editDialog.setContentText("")
                    editDialog.dismiss()

                }

                override fun onClickCancel() {
                  editDialog.dismiss()
                }

            })

        if(DeviceUtil.isRfidDevice()){
            mDatabind.etBarcode.addOnNoneEditorActionListener{
                mDatabind.etBarcode.setText("")
                onReceiverData(it)
            }
            mDatabind.etShelf.addOnNoneEditorActionListener{
                mDatabind.etShelf.setText("")
                onReceiverData(it)
            }
            mDatabind.etUnique.addOnNoneEditorActionListener {
                mDatabind.etUnique.setText("")
                onReceiverData(it)
            }
        }
    }

    override fun createObserver() {
        super.createObserver()

        requestInStroreDetailViewModel.detail.observe(this, Observer {

            parseState(it, { storeDetail ->
                var allNum: Int = 0
                storeDetail.task_list.sku_list.forEach { map ->
                    allNum += map.value
                }
                mViewModel.goodsCount.value = allNum
                var alReadyNum: Int = 0
                storeDetail.in_store_list.sku_list.forEach { map ->
                    alReadyNum += map.value
                }
                mViewModel.inStoreCount.value = alReadyNum
                mViewModel.thisCount.value = 0
                mViewModel.detail.value = storeDetail
                mViewModel.alReadyInStoreSet.addAll(storeDetail.in_store_list.unique_code_list)
                for (map in storeDetail.task_list.sku_list) {
                    if (storeDetail.in_store_list.sku_list.containsKey(map.key)) {
                        mViewModel.currentSkuNumMap.put(
                            map.key,
                            map.value - storeDetail.in_store_list.sku_list.get(map.key)!!
                        )
                    } else {
                        mViewModel.currentSkuNumMap.put(map.key, map.value)
                    }

                }


            }, {

                ToastUtils.showShort(it.errorMsg)
                finish()

            })

        })

        requestInStroreDetailViewModel.uploadResult.observe(this, Observer {

            parseState(it,{
               ToastUtils.showShort("入库成功")
            },{ exception ->
             ToastUtils.showShort(exception.errorMsg)
            })
        })

    }

    override fun onFinish(data: String) {
    }


    fun addErrorOrNormalList(barcode: String) {

        if (mViewModel.detail.value?.barcode_sku_map?.containsKey(barcode) == true) {
            val sku = mViewModel.detail.value?.barcode_sku_map?.get(barcode).toString()
//                if(mViewModel.detail.value!!.in_store_list.sku_list.get(sku) == null){
//                    addErrorList(barcode)
//                }else{
            if (mViewModel.currentSkuNumMap.containsKey(sku)) {
                var num = mViewModel.currentSkuNumMap.get(sku)
                if (num != null) {
                    if (num <= 0) {
                        addErrorList(barcode)
                    } else {
                        num--?.let { mViewModel.currentSkuNumMap.put(sku, it) }

//                                normalAdapter.data.forEach{
//                                    if(it.barcode == barcode){
//                                        it.num++
//                                        normalAdapter.notifyDataSetChanged()
//                                        return
//                                    }
//                                }

                        normalAdapter.addData(
                            NewInStoreNormalBean(
                                "",
                                barcode,
                                mViewModel.currentUnique.value
                            )
                        )
                        mViewModel.normalNum.value = normalAdapter.data.size
                        normalBottomSheetNum.text = mViewModel.normalNum.value.toString()
                    }
                }

            } else {
                addErrorList(barcode)
            }

//                }
        } else {
            addErrorList(barcode)

        }

        mViewModel.thisCount.value = errorAdapter.data.size + normalAdapter.data.size

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

    fun addErrorList(barcode: String) {
        errorAdapter.addData(NewInStoreErrorBean(barcode))
        mViewModel.errorNum.value = errorAdapter.data.size
        errorBottomSheetNum.text = mViewModel.errorNum.value.toString()
    }

    fun addNormalList(shelf: String) {
        mViewModel.currenNormalList.forEach {
            it.shelf_code = shelf
        }
        normalAdapter.addData(mViewModel.currenNormalList)
        mViewModel.currenNormalList.clear()
    }

    inner class ProxyClick {
        fun openErrorBottomSheet() {
            errorBottomsheetDialog.show()
        }

        fun openNormalBottomSheet() {
            normalBottomsheetDialog.show()

        }

        fun onDelete() {
            editDialog.show()

        }
        fun onCancel(){
            finish()
        }

        fun onUpload(){
            upload()
        }

    }

    fun deleteItem(barcode: String) {
        mViewModel.currentBarCodeSet.remove(barcode)
        mViewModel.currentUniqueSet.remove(barcode)
        val errorIterator = errorAdapter.data.iterator()
        while (errorIterator.hasNext()) {
            var next = errorIterator.next()
            if (next.unique == barcode) {
                errorIterator.remove()
                errorAdapter.notifyDataSetChanged()
                updateNum()

            }
        }
        val normalIterator = normalAdapter.data.iterator()
        while (normalIterator.hasNext()) {
            var next = normalIterator.next()
            if (next.barcode == barcode) {
                normalIterator.remove()
                normalAdapter.notifyDataSetChanged()
                updateNum()
            }
        }
        editDialog.setContentText("")


//        var position = -1
//        errorList@ for(i in errorAdapter.data.indices){
//            if(errorAdapter.data[i].unique == barcode){
//                position = i
//                break@errorList
//            }
//            if(position >= 0){
//                errorAdapter.data.removeAt(position)
//            }
//
//
//
//        }
//         errorAdapter.data.forEach { errorData ->
//            if(errorData.unique  == barcode){
//                position = errorData.
//            }
//
//        }


    }

    private fun updateNum() {
        mViewModel.thisCount.value = errorAdapter.data.size + normalAdapter.data.size
        normalBottomSheetNum.text = normalAdapter.data.size.toString()
        mViewModel.normalNum.value = normalAdapter.data.size
        errorBottomSheetNum.text = errorAdapter.data.size.toString()
        mViewModel.errorNum.value = errorAdapter.data.size


    }

    fun upload(){
            var string = Gson().toJson(normalAdapter.data)
       requestInStroreDetailViewModel.upload(task.id.toString(),1,normalAdapter.data)
    }

    override fun initScan() {

    }

    override fun readOrClose() {

    }
}