package com.bigoffs.pdaremake.ui.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.collection.arraySetOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bigoffs.pdaremake.R
import com.bigoffs.pdaremake.app.base.BaseScanActivity
import com.bigoffs.pdaremake.app.ext.*
import com.bigoffs.pdaremake.app.util.DeviceUtil
import com.bigoffs.pdaremake.data.model.bean.*
import com.bigoffs.pdaremake.databinding.ActivityNewInstoreDetailBinding
import com.bigoffs.pdaremake.databinding.ActivityPdaBarcodeTallyBinding

import com.bigoffs.pdaremake.databinding.ActivityPdaUniqueTallyBinding
import com.bigoffs.pdaremake.ui.adapter.NewInStoreErrorAdapter
import com.bigoffs.pdaremake.ui.adapter.NewInStoreNormalAdapter
import com.bigoffs.pdaremake.ui.adapter.StocktakingOffAdapter
import com.bigoffs.pdaremake.ui.adapter.StocktakingOnlAdapter
import com.bigoffs.pdaremake.ui.dialog.EditDialog
import com.bigoffs.pdaremake.ui.dialog.HintDialog
import com.bigoffs.pdaremake.ui.dialog.InputDialog
import com.bigoffs.pdaremake.viewmodel.request.RequestInStroreDetailViewModel
import com.bigoffs.pdaremake.viewmodel.request.RequestTallyViewModel
import com.bigoffs.pdaremake.viewmodel.state.NewInStoreDetailViewModel
import com.bigoffs.pdaremake.viewmodel.state.TallyViewModel
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import me.hgj.jetpackmvvm.ext.parseState

/**
 *User:Kirito
 *Time:2021/5/10  22:18
 *Desc:条形码理货
 */
class PdaBarcodeTallyActivity :
    BaseScanActivity<TallyViewModel, ActivityPdaBarcodeTallyBinding>() {
    override fun layoutId(): Int = R.layout.activity_pda_barcode_tally
    val set = arraySetOf<String>()

    val requestTallyViewModel: RequestTallyViewModel by viewModels()

    private lateinit var errorRecyclerView: RecyclerView
    private lateinit var normalRecyclerView: RecyclerView
    private lateinit var normalBottomsheetDialog: BottomSheetDialog
    private lateinit var errorBottomsheetDialog: BottomSheetDialog
    private lateinit var errorBottomSheetNum: TextView
    private lateinit var normalBottomSheetNum: TextView

    private lateinit var editDialog: EditDialog

    private var isUndercarriage = true


    //适配器
    private val offAdapter: StocktakingOffAdapter by lazy { StocktakingOffAdapter(arrayListOf()) }
    private val onAdapter: StocktakingOnlAdapter by lazy { StocktakingOnlAdapter(arrayListOf()) }



    override fun setStatusBar() {
        initTitle(false, biaoti = "理货")
    }


    override fun onReceiverData(data: String) {

        if(editDialog.isShowing){
                editDialog.setContentText(data)
        }else{
            when (mViewModel.currentFocus.value) {
                //添加店内码
                1 -> {

                    if(isUndercarriage){
                        //下架操作
                        if (mViewModel.currentUniqueSet.contains(data)) {
                            ToastUtils.showShort("条形码已存在")
                            beep()
                        } else {
                            mViewModel.currentUniqueSet.add(data)
                            showDownDialog(data)
                        }

                    }else{
                        //上架操作
                            for(i in offAdapter.data.indices){
                                if(offAdapter.data[i].goods_code == data){
                                    showUpDialog(i,data)
                                    return
                                }
                                beep()
                                ToastUtils.showShort("未找到待上架条形码")
                            }
                    }


                }
                //添加货架号
                3 -> {
//                    mDatabind.etShelf.setText(data)
                     mDatabind.etUnique.requestFocus()
                     mViewModel.currentShelf.value = data
//                        addNormalList(data)


                }

            }
        }
        mDatabind.etShelf.requestFocus()

    }

    private fun initBottomSheet() {

        var view1 = View.inflate(this, R.layout.bottom_newinstoreerror, null)
        errorRecyclerView = view1.findViewById(R.id.dialog_recycleView)
        errorRecyclerView.init(LinearLayoutManager(this), offAdapter)
        errorBottomsheetDialog = BottomSheetDialog(this, R.style.dialog)
        errorBottomsheetDialog.setContentView(view1)
        view1.findViewById<ImageView>(R.id.iv_unfold).setOnClickListener {
            errorBottomsheetDialog.dismiss()
        }
        view1.findViewById<TextView>(R.id.title).setText("待上架数量")
        view1.findViewById<TextView>(R.id.title1).setText("条形码")
        view1.findViewById<TextView>(R.id.title2).setText("数量")
        errorBottomSheetNum = view1.findViewById(R.id.tv_bottom_error_num)
        BottomSheetBehavior.from(view1.parent as View).peekHeight = getPeekHeight()


        var view2 = View.inflate(this, R.layout.bottom_newinstorenormal, null)
        normalRecyclerView = view2.findViewById(R.id.dialog_recycleView)
        view2.findViewById<ImageView>(R.id.iv_unfold).setOnClickListener {
            normalBottomsheetDialog.dismiss()
        }
        view2.findViewById<TextView>(R.id.title).setText("已上架数量")
        view2.findViewById<TextView>(R.id.title1).setText("货架号")
        view2.findViewById<TextView>(R.id.title2).setText("条形码")
        view2.findViewById<TextView>(R.id.title3).setText("数量")
        normalRecyclerView.init(LinearLayoutManager(this), onAdapter)
        normalBottomsheetDialog = BottomSheetDialog(this, R.style.dialog)
        normalBottomsheetDialog.setContentView(view2)
        normalBottomSheetNum = view2.findViewById(R.id.tv_bottom_normal_num)
        BottomSheetBehavior.from(view2.parent as View).peekHeight = getPeekHeight()
    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)


        mDatabind.vm = mViewModel
        mDatabind.click = ProxyClick()
        mDatabind.viewPager.init(this, arrayListOf(Fragment(), Fragment()))
        mDatabind.magicIndicator.bindViewPager2(mDatabind.viewPager, arrayListOf("条码下架","条码上架")){
            isUndercarriage = it == 0
            mDatabind.tvRight.isClickable = !isUndercarriage
            if(isUndercarriage){
                mDatabind.tvRight.setBackgroundColor(Color.GRAY)
            }else{
                mDatabind.tvRight.setBackgroundColor(Color.parseColor("#0033CC"))
            }
        }
        mDatabind.viewPager.offscreenPageLimit = 2

        mDatabind.etUnique.setOnFocusChangeListener() { v, hasFocus ->
            if (hasFocus) {
                mViewModel.currentFocus.value = 1
                mDatabind.devideUnique.setBackgroundColor(Color.parseColor("#0033cc"))
            } else {
                mDatabind.devideUnique.setBackgroundColor(Color.parseColor("#EEEEEE"))
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

            mDatabind.etShelf.addOnNoneEditorActionListener{
                mDatabind.etShelf.setText("")
                onReceiverData(it)
            }
            mDatabind.etUnique.addOnNoneEditorActionListener {
                mDatabind.etUnique.setText("")
                onReceiverData(it)
            }
        }

        mDatabind.etUnique.requestFocus()
    }

    override fun createObserver() {
        super.createObserver()

        requestTallyViewModel.barcodeUploadResult.observe(this,{state ->

            parseState(state,{
                ToastUtils.showShort("操作成功")
                finish()
            },{
                beep()
                ToastUtils.showShort(it.message)
            })
        })

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



    fun addNormalList(shelf: String) {

//        normalAdapter.addData(mViewModel.currenNormalList)

    }

    inner class ProxyClick {


        fun openNormalBottomSheet() {
            normalBottomsheetDialog.show()
        }
        fun openErrorBottomSheet() {
            errorBottomsheetDialog.show()
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
        mViewModel.currentUniqueSet.remove(barcode)
        val errorIterator = offAdapter.data.iterator()
        while (errorIterator.hasNext()) {
            var next = errorIterator.next()
            if (next.goods_code == barcode) {
                errorIterator.remove()
                offAdapter.notifyDataSetChanged()
                updateNum()

            }
        }
        val normalIterator = onAdapter.data.iterator()
        while (normalIterator.hasNext()) {
            var next = normalIterator.next()
            if (next.goods_code == barcode) {
                normalIterator.remove()
                onAdapter.notifyDataSetChanged()
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

        normalBottomSheetNum.text = onAdapter.data.size.toString()
        mViewModel.normalNum.value = onAdapter.data.size
        mViewModel.errorNum.value = offAdapter.data.size
        mViewModel.scanNum.value = mViewModel.scanList.size

    }

    fun upload(){
        if(offAdapter.data.isNotEmpty()){
            beep()
            showMessage("待上架列表该条码数量为0才能上传数据")
            return
        }

        showUploadDialog()

    }
    fun showChangeNumDialog(position:Int,barcode:String){


//        InputDialog.create(this)
//            .setTitle("条形码${barcode}已被扫描，是否需要修改数量")
//            .setRightBtnText("确定")
//            .setOnClickListener(object : InputDialog.OnHintDialogListener{
//                override fun onClickOk(content: String) {
//                    var maxNum = normalAdapter.data[position].maxNum
//                    if(content.toInt() <=  normalAdapter.data[position].maxNum){
//                        normalAdapter.data[position].num = content
//                        var num = 0
//                        for (i in normalAdapter.data){
//                            num += i.num.toInt()
//                        }
//                        mViewModel.normalNum.value = num
//                        normalBottomSheetNum.text =  num.toString()
//                        normalAdapter.notifyDataSetChanged()
//                    }else{
//                        beep()
//                        ToastUtils.showShort("${barcode}条码已超量入库，请及时联系买手确认")
//                    }
//
//                }
//
//                override fun onClickCancel() {
//
//                }
//            }).show()

    }
    //下架数量dialog
    fun showDownDialog(barcode:String){
        InputDialog.create(this)
            .setTitle("条形码${barcode}")
            .setRightBtnText("确定")
            .setOnClickListener(object : InputDialog.OnHintDialogListener{
                override fun onClickOk(content: String) {
                    val inputNum = content.toInt()
                    offAdapter.addData(StocktakingOffBean(barcode,content,mViewModel.currentShelf.value))
                    mViewModel.errorNum.value = offAdapter.data.size
                    updateNum()

                }

                override fun onClickCancel() {

                }
            }).show()
    }

    fun showUpDialog(position: Int,barcode:String){
        InputDialog.create(this)
            .setTitle("条形码${barcode}")
            .setRightBtnText("确定")
            .setOnClickListener(object : InputDialog.OnHintDialogListener{
                override fun onClickOk(content: String) {
                    val inputNum = content.toInt()
                   if(inputNum>offAdapter.data[position].num.toInt()){
                        beep()
                       ToastUtils.showShort("上架数量不能大于待上架数量")
                   }else{

                       for(i in onAdapter.data.indices){
                           if (onAdapter.data[i].goods_code == barcode && onAdapter.data[i].shelf_code == mViewModel.currentShelf.value){
                               onAdapter.data[i].num = onAdapter.data[i].num+inputNum
                               onAdapter.data[i].old_num = onAdapter.data[i].num
                               onAdapter.notifyDataSetChanged()
                               offAdapter.data[position].num = (offAdapter.data[position].num.toInt()-inputNum).toString()
                               if(offAdapter.data[position].num == "0"){
                                   offAdapter.removeAt(position)
                                   offAdapter.notifyDataSetChanged()
                               }else{
                                   offAdapter.notifyDataSetChanged()
                               }
                               return
                           }

                       }
                       //没找到就是新数据
                       onAdapter.addData(StocktakingOnBean(mViewModel.currentShelf.value,barcode,inputNum.toString()
                           ,"2",offAdapter.data[position].shelf_code,inputNum.toString()))
                       offAdapter.data[position].num = (offAdapter.data[position].num.toInt()-inputNum).toString()
                       if(offAdapter.data[position].num == "0"){
                           offAdapter.removeAt(position)
                           offAdapter.notifyDataSetChanged()
                       }else{
                           offAdapter.notifyDataSetChanged()
                       }

                   }
                    updateNum()
                }

                override fun onClickCancel() {

                }
            }).show()
    }

    fun showUploadDialog(){
        HintDialog.create(this, HintDialog.STYLE_ONLY_OK).setTitle("").setContent("确定上传本次理货数据？")
            .setLeftBtnText("取消")
            .setRightBtnText("确定")
            .setDialogListener(object : HintDialog.OnHintDialogListener{
                override fun onClickOk() {
                    requestTallyViewModel.uploadBarcodeTallyData(onAdapter.data,"2")
                }

                override fun onClickCancel() {

                }

                override fun onClickOther() {

                }
            }).show()
    }

}