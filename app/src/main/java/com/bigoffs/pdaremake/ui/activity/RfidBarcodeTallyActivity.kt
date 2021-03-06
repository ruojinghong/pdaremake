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
import com.bigoffs.pdaremake.app.base.BaseRfidFActivity
import com.bigoffs.pdaremake.app.base.BaseScanActivity
import com.bigoffs.pdaremake.app.ext.*
import com.bigoffs.pdaremake.app.util.DeviceUtil
import com.bigoffs.pdaremake.data.model.bean.*
import com.bigoffs.pdaremake.databinding.ActivityNewInstoreDetailBinding
import com.bigoffs.pdaremake.databinding.ActivityPdaBarcodeTallyBinding

import com.bigoffs.pdaremake.databinding.ActivityPdaUniqueTallyBinding
import com.bigoffs.pdaremake.databinding.ActivityRfidBarcodeTallyBinding
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
 *Desc:???????????????
 */
class RfidBarcodeTallyActivity :
    BaseRfidFActivity<TallyViewModel, ActivityRfidBarcodeTallyBinding>() {
    override fun layoutId(): Int = R.layout.activity_rfid_barcode_tally
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


    //?????????
    private val offAdapter: StocktakingOffAdapter by lazy { StocktakingOffAdapter(arrayListOf()) }
    private val onAdapter: StocktakingOnlAdapter by lazy { StocktakingOnlAdapter(arrayListOf()) }



    override fun setStatusBar() {
        initTitle(false, biaoti = "??????")
    }


     fun onReceiverData(data: String) {
            if(data.isEmpty()){
                return
            }
        if(editDialog.isShowing){
                editDialog.setContentText(data)
        }else{
            when (mViewModel.currentFocus.value) {
                //???????????????
                1 -> {

                    if(isUndercarriage){
                        //????????????
                        if (mViewModel.currentUniqueSet.contains(data)) {
                            ToastUtils.showShort("??????????????????")
                            beep()
                        } else {
                            mViewModel.currentUniqueSet.add(data)
                            showDownDialog(data)
                        }

                    }else{
                        //????????????
                            for(i in offAdapter.data.indices){
                                if(offAdapter.data[i].goods_code == data){
                                    showUpDialog(i,data)
                                    return
                                }
                                beep()
                                ToastUtils.showShort("???????????????????????????")
                            }
                    }


                }
                //???????????????
                3 -> {
//                    mDatabind.etShelf.setText(data)
                     mDatabind.etUnique.requestFocus()
                     mViewModel.currentShelf.value = data
//                        addNormalList(data)


                }

            }
        }


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
        view1.findViewById<TextView>(R.id.title).setText("???????????????")
        view1.findViewById<TextView>(R.id.title1).setText("?????????")
        view1.findViewById<TextView>(R.id.title2).setText("??????")
        errorBottomSheetNum = view1.findViewById(R.id.tv_bottom_error_num)
        BottomSheetBehavior.from(view1.parent as View).peekHeight = getPeekHeight()


        var view2 = View.inflate(this, R.layout.bottom_newinstorenormal, null)
        normalRecyclerView = view2.findViewById(R.id.dialog_recycleView)
        view2.findViewById<ImageView>(R.id.iv_unfold).setOnClickListener {
            normalBottomsheetDialog.dismiss()
        }
        view2.findViewById<TextView>(R.id.title).setText("???????????????")
        view2.findViewById<TextView>(R.id.title1).setText("?????????")
        view2.findViewById<TextView>(R.id.title2).setText("?????????")
        view2.findViewById<TextView>(R.id.title3).setText("??????")
        normalRecyclerView.init(LinearLayoutManager(this), onAdapter)
        normalBottomsheetDialog = BottomSheetDialog(this, R.style.dialog)
        normalBottomsheetDialog.setContentView(view2)
        normalBottomSheetNum = view2.findViewById(R.id.tv_bottom_normal_num)
        BottomSheetBehavior.from(view2.parent as View).peekHeight = getPeekHeight()
    }

    override fun initView(savedInstanceState: Bundle?) {



        mDatabind.vm = mViewModel
        mDatabind.click = ProxyClick()
        mDatabind.viewPager.init(this, arrayListOf(Fragment(), Fragment()))
        mDatabind.magicIndicator.bindViewPager2(mDatabind.viewPager, arrayListOf("????????????","????????????")){
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

        mDatabind.etShelf.requestFocus()
    }

    override fun createObserver() {
        super.createObserver()

        requestTallyViewModel.barcodeUploadResult.observe(this,{state ->

            parseState(state,{
                ToastUtils.showShort("????????????")
                finish()
            },{
                beep()
                ToastUtils.showShort(it.message)
            })
        })

    }

    override fun onFinish(data: String) {

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
            showMessage("?????????????????????????????????0??????????????????")
            return
        }

        showUploadDialog()

    }
    fun showChangeNumDialog(position:Int,barcode:String){


//        InputDialog.create(this)
//            .setTitle("?????????${barcode}???????????????????????????????????????")
//            .setRightBtnText("??????")
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
//                        ToastUtils.showShort("${barcode}???????????????????????????????????????????????????")
//                    }
//
//                }
//
//                override fun onClickCancel() {
//
//                }
//            }).show()

    }
    //????????????dialog
    fun showDownDialog(barcode:String){
        InputDialog.create(this)
            .setTitle("?????????${barcode}")
            .setRightBtnText("??????")
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
            .setTitle("?????????${barcode}")
            .setRightBtnText("??????")
            .setOnClickListener(object : InputDialog.OnHintDialogListener{
                override fun onClickOk(content: String) {
                    val inputNum = content.toInt()
                   if(inputNum>offAdapter.data[position].num.toInt()){
                        beep()
                       ToastUtils.showShort("???????????????????????????????????????")
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
                       //????????????????????????
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
        HintDialog.create(this, HintDialog.STYLE_ONLY_OK).setTitle("").setContent("?????????????????????????????????")
            .setLeftBtnText("??????")
            .setRightBtnText("??????")
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

    override fun initScan() {

    }

    override fun readOrClose() {

    }

}