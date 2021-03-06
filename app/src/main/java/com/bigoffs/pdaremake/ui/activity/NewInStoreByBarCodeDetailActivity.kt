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
import com.bigoffs.pdaremake.app.ext.showBottomSheedList
import com.bigoffs.pdaremake.data.model.bean.InStoreBean
import com.bigoffs.pdaremake.data.model.bean.NewInStoreErrorBean
import com.bigoffs.pdaremake.data.model.bean.NewInStoreNormalBarcodeBean
import com.bigoffs.pdaremake.data.model.bean.NewInStoreNormalBean
import com.bigoffs.pdaremake.databinding.ActivityNewInstoreByBarcodeDetailBinding
import com.bigoffs.pdaremake.databinding.ActivityNewInstoreDetailBinding
import com.bigoffs.pdaremake.ui.adapter.NewInStoreErrorAdapter
import com.bigoffs.pdaremake.ui.adapter.NewInStoreNormalAdapter
import com.bigoffs.pdaremake.ui.adapter.NewInStoreNormalBarcodeAdapter
import com.bigoffs.pdaremake.ui.dialog.EditDialog
import com.bigoffs.pdaremake.ui.dialog.HintDialog
import com.bigoffs.pdaremake.ui.dialog.InputDialog
import com.bigoffs.pdaremake.viewmodel.request.RequestInStroreDetailViewModel
import com.bigoffs.pdaremake.viewmodel.state.NewInStoreBarcodeDetailViewModel
import com.bigoffs.pdaremake.viewmodel.state.NewInStoreDetailViewModel
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import me.hgj.jetpackmvvm.ext.parseState
import me.hgj.jetpackmvvm.util.ActivityMessenger
import org.w3c.dom.Text

/**
 *User:Kirito
 *Time:2021/5/10  22:18
 *Desc:????????????(?????????)detailactivity
 */
class NewInStoreByBarCodeDetailActivity :
    BaseScanActivity<NewInStoreBarcodeDetailViewModel, ActivityNewInstoreByBarcodeDetailBinding>() {

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


    //?????????
    private val errorAdapter: NewInStoreErrorAdapter by lazy { NewInStoreErrorAdapter(arrayListOf()) }
    private val normalAdapter: NewInStoreNormalBarcodeAdapter by lazy { NewInStoreNormalBarcodeAdapter(arrayListOf()) }

    override fun layoutId(): Int = R.layout.activity_new_instore_by_barcode_detail

    override fun setStatusBar() {
        initTitle(false, biaoti = "????????????")
    }


    override fun onReceiverData(data: String) {

        if(editDialog.isShowing){
                editDialog.setContentText(data)
        }else{
            when (mViewModel.currentFocus.value) {
                //???????????????
//                1 -> {
//                    if (mViewModel.currentUniqueSet.contains(data)) {
//
//                        ToastUtils.showShort("??????????????????")
//                        beep()
//
//                    } else {
//                        mViewModel.currentUniqueSet.add(data)
//                        if (mViewModel.alReadyInStoreSet.contains(data)) {
//                            beep()
//                            ToastUtils.showShort("??????????????????")
//                        } else {
//                            mDatabind.etUnique.setText(data)
//                            mDatabind.etBarcode.requestFocus()
//                        }
//
//                    }
//                }
                //???????????????
                2 -> {
                    if (mViewModel.currentBarCodeSet.contains(data)) {
                        beep()
                        ToastUtils.showShort("??????????????????")
                    } else {
                        for (i in normalAdapter.data.indices){
                            if(normalAdapter.data[i].barcode == data){
                                showChangeNumDialog(i,data)
                                return
                            }

                        }
                            //????????????  29aaaaaaa
                        addErrorOrNormalList(data)
                    }

                }
                //???????????????
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
        view2.findViewById<TextView>(R.id.title2).setText("????????????")
        view2.findViewById<TextView>(R.id.title3).setText("??????")
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

        mViewModel.taskNo.value = "???????????????${task?.in_stock_no}"


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
            .setTitle("???????????????????????????")
            .setHintText("??????????????????????????????")
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

                ToastUtils.showShort(it.msg)
                finish()

            })

        })

        requestInStroreDetailViewModel.uploadResult.observe(this, Observer {

            parseState(it,{
               ToastUtils.showShort("????????????")
            },{ exception ->
             ToastUtils.showShort(exception.msg)
            })
        })

    }

    fun showSelectDateDialog(dataString:Array<String>,barcode:String,n:Int){


        showBottomSheedList(
            mContext, dataString,"??????????????????"
        ) { position, text ->
            //???????????? ?????????????????????????????????

            showNormalDialog(dataString[position],barcode,n)
        }

    }

    fun showNormalDialog(data:String,barcode:String,n : Int){

        var num  = n
        InputDialog.create(this)
            .setTitle("?????????${barcode}")
            .setRightBtnText("??????")
            .setOnClickListener(object : InputDialog.OnHintDialogListener{
                override fun onClickOk(content: String) {
                    val inputNum = content.toInt()
                    if (num!! < inputNum){
                        beep()
                        ToastUtils.showShort("${barcode}???????????????????????????????????????????????????")
                        mViewModel.currentBarCodeSet.remove(barcode)
                    }else{
                        normalAdapter.addData(
                            NewInStoreNormalBarcodeBean(
                                "",
                                barcode,
                                content,
                                maxNum = num!!,
                                production_at = data
                            )
                        )
                        num = num!!- inputNum
//                                        mViewModel.currentSkuNumMap.put(sku, num!!)
                        var count = 0
                        for (i in normalAdapter.data){
                            count += i.num.toInt()
                        }
                        mViewModel.normalNum.value = count
                        normalBottomSheetNum.text = mViewModel.normalNum.value.toString()
                    }


                }

                override fun onClickCancel() {

                }
            }).show()
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
                        showErrorDialog(barcode,"????????????????????????")

                    } else {

                        mViewModel.detail.value?.let {
                            var dataString  = it.production_sku_map.get(sku)?.toArray()

                            showSelectDateDialog(dataString as Array<String>,barcode,num)

                        }



                    }
                }

            } else {
                addErrorList(barcode)
                showErrorDialog(barcode,"????????????????????????")
            }

//                }
        } else {
            addErrorList(barcode)
            showErrorDialog(barcode,"????????????????????????")
        }

        mViewModel.thisCount.value = errorAdapter.data.size + normalAdapter.data.size

    }

    /**
     * ???????????????????????????????????????????????????
     * ??????????????????????????????peekHeight
     *
     * @return height
     */
    protected fun getPeekHeight(): Int {
        val peekHeight = resources.displayMetrics.heightPixels
        //????????????????????????????????????3/4
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
       requestInStroreDetailViewModel.uploadBarcode(task?.id.toString(),2,normalAdapter.data)
    }

    fun showErrorDialog(barcode: String,content:String){
        HintDialog.create(this,HintDialog.STYLE_ONLY_OK).setTitle(barcode).setContent(content)
            .setLeftBtnText("??????")
            .setRightBtnText("??????")
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
            .setTitle("?????????${barcode}???????????????????????????????????????")
            .setRightBtnText("??????")
            .setOnClickListener(object : InputDialog.OnHintDialogListener{
                override fun onClickOk(content: String) {
                    var maxNum = normalAdapter.data[position].maxNum
                    if(content.toInt() <=  normalAdapter.data[position].maxNum){
                        normalAdapter.data[position].num = content
                        var num = 0
                        for (i in normalAdapter.data){
                            num += i.num.toInt()
                        }
                        mViewModel.normalNum.value = num
                        normalBottomSheetNum.text =  num.toString()
                        normalAdapter.notifyDataSetChanged()
                    }else{
                        beep()
                        ToastUtils.showShort("${barcode}???????????????????????????????????????????????????")
                    }

                }

                override fun onClickCancel() {

                }
            }).show()

    }
}