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
import com.bigoffs.pdaremake.app.base.BaseScanActivity
import com.bigoffs.pdaremake.app.ext.init
import com.bigoffs.pdaremake.app.ext.initTitle
import com.bigoffs.pdaremake.data.model.bean.InStoreBean
import com.bigoffs.pdaremake.data.model.bean.NewInStoreErrorBean
import com.bigoffs.pdaremake.data.model.bean.NewInStoreNormalBean
import com.bigoffs.pdaremake.databinding.ActivityNewInstoreByBarcodeDetailBinding
import com.bigoffs.pdaremake.databinding.ActivityNewInstoreDetailBinding
import com.bigoffs.pdaremake.ui.adapter.NewInStoreErrorAdapter
import com.bigoffs.pdaremake.ui.adapter.NewInStoreNormalAdapter
import com.bigoffs.pdaremake.ui.dialog.EditDialog
import com.bigoffs.pdaremake.ui.dialog.HintDialog
import com.bigoffs.pdaremake.ui.dialog.InputDialog
import com.bigoffs.pdaremake.viewmodel.request.RequestInStroreDetailViewModel
import com.bigoffs.pdaremake.viewmodel.state.NewInStoreDetailViewModel
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import me.hgj.jetpackmvvm.ext.parseState
import org.w3c.dom.Text

/**
 *User:Kirito
 *Time:2021/5/10  22:18
 *Desc:新品入库(条形码)detailactivity
 */
class NewInStoreByBarCodeDetailActivity :
    BaseScanActivity<NewInStoreDetailViewModel, ActivityNewInstoreByBarcodeDetailBinding>() {

    val set = arraySetOf<String>()

    val requestInStroreDetailViewModel: RequestInStroreDetailViewModel by viewModels()

    private lateinit var errorRecyclerView: RecyclerView
    private lateinit var normalRecyclerView: RecyclerView
    private lateinit var errorBottomsheetDialog: BottomSheetDialog
    private lateinit var normalBottomsheetDialog: BottomSheetDialog

    private lateinit var errorBottomSheetNum: TextView
    private lateinit var normalBottomSheetNum: TextView
     var task:InStoreBean? = null
    private lateinit var editDialog: EditDialog


    //适配器
    private val errorAdapter: NewInStoreErrorAdapter by lazy { NewInStoreErrorAdapter(arrayListOf()) }
    private val normalAdapter: NewInStoreNormalAdapter by lazy { NewInStoreNormalAdapter(arrayListOf()) }

    override fun layoutId(): Int = R.layout.activity_new_instore_by_barcode_detail

    override fun setStatusBar() {
        initTitle(false, biaoti = "新品入库")
    }


    override fun onReceiverData(data: String) {

        if(editDialog.isShowing){
                editDialog.setContentText(data)
        }else{
            when (mViewModel.currentFocus.value) {
                //添加店内码
//                1 -> {
//                    if (mViewModel.currentUniqueSet.contains(data)) {
//
//                        ToastUtils.showShort("店内码已存在")
//                        beep()
//
//                    } else {
//                        mViewModel.currentUniqueSet.add(data)
//                        if (mViewModel.alReadyInStoreSet.contains(data)) {
//                            beep()
//                            ToastUtils.showShort("店内码已入库")
//                        } else {
//                            mDatabind.etUnique.setText(data)
//                            mDatabind.etBarcode.requestFocus()
//                        }
//
//                    }
//                }
                //添加条形码
                2 -> {
                    if (mViewModel.currentBarCodeSet.contains(data)) {
                        beep()
                        ToastUtils.showShort("条形码已存在")
                    } else {
                        for (i in normalAdapter.data.indices){
                            if(normalAdapter.data[i].equals(data)){
                                showChangeNumDialog(i,data)
                                return
                            }

                        }

                        addErrorOrNormalList("29aaaaaaa")
                    }

                }
                //添加货架号
                3 -> {
                    mDatabind.etShelf.setText(data)
                    mDatabind.etBarcode.requestFocus()
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
        view2.findViewById<TextView>(R.id.title2).setText("商品编码")
        view2.findViewById<TextView>(R.id.title3).setText("数量")
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
        super.initView(savedInstanceState)


        mDatabind.vm = mViewModel
        mDatabind.click = ProxyClick()
        task = intent.getParcelableExtra<InStoreBean>("task")

        mViewModel.taskNo.value = "入库批次：${task?.in_stock_no}"


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

        requestInStroreDetailViewModel.getInStoreDetail(1)

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
                        showErrorDialog(barcode,"没有可入库的数量")

                    } else {
                        InputDialog.create(this)
                            .setTitle("条形码${barcode}")
                            .setRightBtnText("确定")
                            .setOnClickListener(object : InputDialog.OnHintDialogListener{
                                override fun onClickOk(content: String) {
                                    val inputNum = content.toInt()
                                    if (num!! < inputNum){
                                        beep()
                                        ToastUtils.showShort("${barcode}条码已超量入库，请及时联系买手确认")
                                        mViewModel.currentBarCodeSet.remove(barcode)
                                    }else{
                                        num = num!!- inputNum
//                                        mViewModel.currentSkuNumMap.put(sku, num!!)
                                        normalAdapter.addData(
                                            NewInStoreNormalBean(
                                                "",
                                                barcode,
                                                content
                                            )
                                        )
                                        var count = 0
                                        for (i in normalAdapter.data){
                                            count += i.unique_code.toInt()
                                        }
                                        mViewModel.normalNum.value = count
                                        normalBottomSheetNum.text = mViewModel.normalNum.value.toString()
                                    }


                                }

                                override fun onClickCancel() {

                                }
                            }).show()


                    }
                }

            } else {
                addErrorList(barcode)
                showErrorDialog(barcode,"获取不到商品信息")
            }

//                }
        } else {
            addErrorList(barcode)
            showErrorDialog(barcode,"获取不到商品信息")
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
       requestInStroreDetailViewModel.upload(task?.id.toString(),1,normalAdapter.data)
    }

    fun showErrorDialog(barcode: String,content:String){
        HintDialog.create(this,HintDialog.STYLE_ONLY_OK).setTitle(barcode).setContent(content)
            .setLeftBtnText("取消")
            .setRightBtnText("删除")
            .setDialogListener(object : HintDialog.OnHintDialogListener{
            override fun onClickOk() {
                deleteItem(barcode)
            }

            override fun onClickCancel() {

            }

            override fun onClickOther() {

            }
        }).show()
    }

    fun showChangeNumDialog(position:Int,barcode:String){


        InputDialog.create(this)
            .setTitle("条形码${barcode}已被扫描，是否需要修改数量")
            .setRightBtnText("确定")
            .setOnClickListener(object : InputDialog.OnHintDialogListener{
                override fun onClickOk(content: String) {
                  normalAdapter.data[position].unique_code = content
                    var num = 0
                        for (i in normalAdapter.data){
                            num += i.unique_code.toInt()
                        }
                        mViewModel.normalNum.value = num
                        normalBottomSheetNum.text =  num.toString()
                    normalAdapter.notifyDataSetChanged()




                }

                override fun onClickCancel() {

                }
            }).show()

    }
}