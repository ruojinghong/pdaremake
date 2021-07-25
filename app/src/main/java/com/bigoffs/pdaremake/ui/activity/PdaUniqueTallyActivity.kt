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
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bigoffs.pdaremake.R
import com.bigoffs.pdaremake.app.base.BaseScanActivity
import com.bigoffs.pdaremake.app.ext.addOnEditorActionListener
import com.bigoffs.pdaremake.app.ext.addOnNoneEditorActionListener
import com.bigoffs.pdaremake.app.ext.init
import com.bigoffs.pdaremake.app.ext.initTitle
import com.bigoffs.pdaremake.app.util.CacheUtil
import com.bigoffs.pdaremake.app.util.DeviceUtil
import com.bigoffs.pdaremake.data.model.bean.InStoreBean
import com.bigoffs.pdaremake.data.model.bean.NewInStoreErrorBean
import com.bigoffs.pdaremake.data.model.bean.NewInStoreNormalBean
import com.bigoffs.pdaremake.data.model.bean.TallyBean
import com.bigoffs.pdaremake.databinding.ActivityNewInstoreDetailBinding

import com.bigoffs.pdaremake.databinding.ActivityPdaUniqueTallyBinding
import com.bigoffs.pdaremake.ui.adapter.NewInStoreErrorAdapter
import com.bigoffs.pdaremake.ui.adapter.NewInStoreNormalAdapter
import com.bigoffs.pdaremake.ui.adapter.TallyUniquelAdapter
import com.bigoffs.pdaremake.ui.dialog.EditDialog
import com.bigoffs.pdaremake.ui.dialog.HintDialog
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
 *Desc:店内码理货
 */
class PdaUniqueTallyActivity :
    BaseScanActivity<TallyViewModel, ActivityPdaUniqueTallyBinding>() {
    override fun layoutId(): Int = R.layout.activity_pda_unique_tally
    val set = arraySetOf<String>()

    val requestTallyViewModel: RequestTallyViewModel by viewModels()



    private lateinit var errorRecyclerView: RecyclerView
    private lateinit var errorBottomsheetDialog: BottomSheetDialog
    private lateinit var errorBottomSheetNum: TextView

    private lateinit var editDialog: EditDialog


    //适配器
    private val errorAdapter: TallyUniquelAdapter by lazy { TallyUniquelAdapter(arrayListOf()) }




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
                    if (mViewModel.currentUniqueSet.contains(data)) {
                        ToastUtils.showShort("店内码已存在")
                        beep()

                    } else {
                        mViewModel.currentUniqueSet.add(data)
                        errorAdapter.addData(TallyBean("",data,"1","1"))
                        updateNum()
                    }
                }
                //添加货架号
                3 -> {
//                    mDatabind.etShelf.setText(data)
                     mDatabind.etUnique.requestFocus()
                    mViewModel.currentShelf.value = data
                    errorAdapter.data.forEach {
                        if(it.shelf_code.isEmpty()){
                            it.shelf_code = data
                        }
                    }
                    errorAdapter.notifyDataSetChanged()
//                        addNormalList(data)


                }

            }
        }


    }

    private fun initBottomSheet() {


        var view2 = View.inflate(this, R.layout.bottom_newinstoreerror, null)
        errorRecyclerView = view2.findViewById(R.id.dialog_recycleView)
        view2.findViewById<ImageView>(R.id.iv_unfold).setOnClickListener {
            errorBottomsheetDialog.dismiss()
        }
        view2.findViewById<TextView>(R.id.title2).setText("店内码")
        errorRecyclerView.init(LinearLayoutManager(this), errorAdapter)
        errorBottomsheetDialog = BottomSheetDialog(this, R.style.dialog)
        errorBottomsheetDialog.setContentView(view2)
        errorBottomSheetNum = view2.findViewById(R.id.tv_bottom_error_num)
        BottomSheetBehavior.from(view2.parent as View).peekHeight = getPeekHeight()
    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)


        mDatabind.vm = mViewModel
        mDatabind.click = ProxyClick()


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
            .setTitle("请扫描剔除店内码")
            .setHintText("请扫描店内码")
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
    }

    override fun createObserver() {
        super.createObserver()
        requestTallyViewModel.uniqueUploadResult.observe(this,{state ->

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
        val errorIterator = errorAdapter.data.iterator()
        while (errorIterator.hasNext()) {
            var next = errorIterator.next()
            if (next.goods_code == barcode) {
                errorIterator.remove()
                errorAdapter.notifyDataSetChanged()
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

        errorBottomSheetNum.text = errorAdapter.data.size.toString()
        mViewModel.normalNum.value = errorAdapter.data.size
        mViewModel.errorNum.value = errorAdapter.data.size
        mViewModel.scanNum.value = mViewModel.scanList.size

    }

    fun upload(){
        showUploadDialog()


    }

    fun showUploadDialog(){
        HintDialog.create(this, HintDialog.STYLE_ONLY_OK).setTitle("").setContent("确定上传本次理货数据？")
            .setLeftBtnText("取消")
            .setRightBtnText("确定")
            .setDialogListener(object : HintDialog.OnHintDialogListener{
                override fun onClickOk() {
                    requestTallyViewModel.uploadTallyData(errorAdapter.data,"1")
                }

                override fun onClickCancel() {

                }

                override fun onClickOther() {

                }
            }).show()
    }
}