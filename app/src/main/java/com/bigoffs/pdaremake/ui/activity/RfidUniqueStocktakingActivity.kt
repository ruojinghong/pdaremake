package com.bigoffs.pdaremake.ui.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextUtils
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
import com.bigoffs.pdaremake.app.base.BaseRfidFActivity
import com.bigoffs.pdaremake.app.base.BaseScanActivity
import com.bigoffs.pdaremake.app.ext.addOnEditorActionListener
import com.bigoffs.pdaremake.app.ext.addOnNoneEditorActionListener
import com.bigoffs.pdaremake.app.ext.init
import com.bigoffs.pdaremake.app.ext.initTitle
import com.bigoffs.pdaremake.app.util.CacheUtil
import com.bigoffs.pdaremake.app.util.DeviceUtil
import com.bigoffs.pdaremake.app.util.Timestamp
import com.bigoffs.pdaremake.data.model.bean.*
import com.bigoffs.pdaremake.databinding.ActivityNewInstoreDetailBinding
import com.bigoffs.pdaremake.databinding.ActivityPdaUniqueStocktakingBinding

import com.bigoffs.pdaremake.databinding.ActivityPdaUniqueTallyBinding
import com.bigoffs.pdaremake.databinding.ActivityRfidUniqueStocktakingBinding
import com.bigoffs.pdaremake.ui.activity.rfid.FindEpcByBarcodeActivity
import com.bigoffs.pdaremake.ui.adapter.NewInStoreErrorAdapter
import com.bigoffs.pdaremake.ui.adapter.NewInStoreNormalAdapter
import com.bigoffs.pdaremake.ui.adapter.StocktakingUniquelAdapter
import com.bigoffs.pdaremake.ui.adapter.TallyUniquelAdapter
import com.bigoffs.pdaremake.ui.dialog.EditDialog
import com.bigoffs.pdaremake.ui.dialog.HintDialog
import com.bigoffs.pdaremake.viewmodel.request.RequestInStroreDetailViewModel
import com.bigoffs.pdaremake.viewmodel.request.RequestStocktakingViewModel
import com.bigoffs.pdaremake.viewmodel.request.RequestTallyViewModel
import com.bigoffs.pdaremake.viewmodel.state.NewInStoreDetailViewModel
import com.bigoffs.pdaremake.viewmodel.state.StocktakingViewModel
import com.bigoffs.pdaremake.viewmodel.state.TallyViewModel
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import me.hgj.jetpackmvvm.ext.parseState
import me.hgj.jetpackmvvm.util.ActivityMessenger
import java.io.File
import java.io.FileWriter
import java.io.IOException

/**
 *User:Kirito
 *Time:2021/5/10  22:18
 *Desc:店内码盘点
 */
class RfidUniqueStocktakingActivity :
    BaseRfidFActivity<StocktakingViewModel, ActivityRfidUniqueStocktakingBinding>() {
    override fun layoutId(): Int = R.layout.activity_rfid_unique_stocktaking
    val set = arraySetOf<String>()

    val requestStocktakingViewModel: RequestStocktakingViewModel by viewModels()



    private lateinit var errorRecyclerView: RecyclerView
    private lateinit var errorBottomsheetDialog: BottomSheetDialog
    private lateinit var errorBottomSheetNum: TextView

    private lateinit var editDialog: EditDialog

    private var bean : StocktakingListBean?  = null


    //适配器
    private val errorAdapter: StocktakingUniquelAdapter by lazy { StocktakingUniquelAdapter(arrayListOf()) }




    override fun setStatusBar() {
        initTitle(false, biaoti = "盘点")
    }


     fun onReceiverData(data: String) {

        if(editDialog.isShowing){
                editDialog.setContentText(data)
        }else{
            when (mViewModel.currentFocus.value) {
                //添加店内码
                1 -> {

                    if(errorAdapter.data.size > 3000){
                        ToastUtils.showShort("行数不能超过3000行")
                        beep()
                        return;
                    }

                    if(TextUtils.isEmpty(mViewModel.currentShelf.value)){
                        ToastUtils.showShort("货架号不能为空")
                        beep()
                        return;
                    }
                    errorAdapter.data.forEach {

                        if(mViewModel.currentShelf.value.equals(it.shelf_code)){
                            if (data.equals(it.goods_code)){
                                ToastUtils.showShort("店内码已存在")
                                beep()
                                return
                            }
                        }

                    }
                    mViewModel.currentScanUnique.value = data
                   requestStocktakingViewModel.stocktakingCheckUnique(data);


                }
                //添加货架号
                3 -> {

//                    mViewModel.currentScanShelfCode.value = "YX-100-10-2"
//                    requestStocktakingViewModel.stocktakingCheckShelf("YX-100-10-2");
                    mViewModel.currentScanShelfCode.value = data
                    requestStocktakingViewModel.stocktakingCheckShelf(data);
//                    mDatabind.etShelf.setText(data)

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
        view2.findViewById<TextView>(R.id.title2).text = "店内码"
        view2.findViewById<TextView>(R.id.title).text = "正常数量"

        errorRecyclerView.init(LinearLayoutManager(this), errorAdapter)
        errorBottomsheetDialog = BottomSheetDialog(this, R.style.dialog)
        errorBottomsheetDialog.setContentView(view2)
        errorBottomSheetNum = view2.findViewById(R.id.tv_bottom_error_num)
        BottomSheetBehavior.from(view2.parent as View).peekHeight = getPeekHeight()
        errorAdapter.setOnItemClickListener{ adapter,view,position ->
            showItemDeleteDialog(position)
        }
    }

    fun showItemDeleteDialog(position:Int){
        HintDialog.create(this, HintDialog.STYLE_ONLY_OK).setTitle("").setContent("确定删除？")
            .setLeftBtnText("取消")
            .setRightBtnText("确定")
            .setDialogListener(object : HintDialog.OnHintDialogListener{
                override fun onClickOk() {
                   errorAdapter.data.removeAt(position)
                    errorAdapter.notifyDataSetChanged();
                    updateNum();

                }

                override fun onClickCancel() {

                }

                override fun onClickOther() {

                }
            }).show()
    }

    override fun initView(savedInstanceState: Bundle?) {



        mDatabind.vm = mViewModel
        mDatabind.click = ProxyClick()

        bean = intent.getParcelableExtra("data")
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

        bean?.let {

            if(it.save_type == 1){
                mDatabind.tvRight.text = "预盘数据上传"
            }else{
                mDatabind.tvRight.text = "复盘数据上传"
            }

            mViewModel.stocktakingid.value = it.id

        }


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


        requestStocktakingViewModel.checkUnique.observe(this,{state ->

            parseState(state,{
                mViewModel.currentUniqueSet.add(mViewModel.currentScanUnique.value)
                errorAdapter.addData(UniqueStocktakingBean(mViewModel.currentShelf.value,mViewModel.currentScanUnique.value,"1"))
                updateNum()

            },{
//                mViewModel.currentUniqueSet.add(mViewModel.currentScanUnique.value)
//                errorAdapter.addData(UniqueStocktakingBean(mViewModel.currentShelf.value,mViewModel.currentScanUnique.value,"1"))
//                updateNum()
                beep()
                ToastUtils.showShort(it.message)
            })
        })


        requestStocktakingViewModel.checkShelf.observe(this,{state ->

            parseState(state,{
                mDatabind.etUnique.requestFocus()
                mViewModel.currentShelf.value = mViewModel.currentScanShelfCode.value

            },{
//                mDatabind.etUnique.requestFocus()
//                mViewModel.currentShelf.value = mViewModel.currentScanShelfCode.value
                beep()
                ToastUtils.showShort(it.message)
            })
        })

        requestStocktakingViewModel.uniqueUploadResult.observe(this,{state ->
            parseState(state,{
                resetData()
                ToastUtils.showShort("数据上传成功")
            },{
                beep()
                ToastUtils.showShort(it.message)

            })
        })




    }

    override fun onFinish(data: String) {
        onReceiverData(data)
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
    fun clear(){
        HintDialog.create(this, HintDialog.STYLE_ONLY_OK).setTitle("").setContent("确定清空采集？")
            .setLeftBtnText("取消")
            .setRightBtnText("确定")
            .setDialogListener(object : HintDialog.OnHintDialogListener{
                override fun onClickOk() {

                    resetData();

                }

                override fun onClickCancel() {

                }

                override fun onClickOther() {

                }
            }).show()
    }

    inner class ProxyClick {


        fun openNormalBottomSheet() {
            errorBottomsheetDialog.show()
        }

        fun onClear(){
            clear()
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

        fun onSave(){
            showSaveDialog()
        }

    }

    fun deleteItem(barcode: String) {

        val errorIterator = errorAdapter.data.iterator()
        while (errorIterator.hasNext()) {
            var next = errorIterator.next()
            if (next.shelf_code == mViewModel.currentShelf.value) {
                if(next.goods_code == barcode){
                    mViewModel.currentUniqueSet.remove(barcode)
                    errorIterator.remove()
                    errorAdapter.notifyDataSetChanged()
                    updateNum()
                    return
                }
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
        HintDialog.create(this, HintDialog.STYLE_ONLY_OK).setTitle("").setContent("确定上传本次盘点数据？")
            .setLeftBtnText("取消")
            .setRightBtnText("确定")
            .setDialogListener(object : HintDialog.OnHintDialogListener{
                override fun onClickOk() {
                    bean?.let {
                        requestStocktakingViewModel.uploadStacktakingData(errorAdapter.data,it.save_type,it.id,it.st_type)
                    }

                }

                override fun onClickCancel() {

                }

                override fun onClickOther() {

                }
            }).show()
    }

    fun showSaveDialog() {
        HintDialog.create(this, HintDialog.STYLE_ONLY_OK).setTitle("").setContent("确定生成txt？")
            .setLeftBtnText("取消")
            .setRightBtnText("确定")
            .setDialogListener(object : HintDialog.OnHintDialogListener {
                override fun onClickOk() {
//                    requestPermission()


                    writeTxtFile("",  Environment.getExternalStorageDirectory().getAbsolutePath()+"/pda","${Timestamp().transToString(System.currentTimeMillis())}.txt",true)

                }

                override fun onClickCancel() {

                }

                override fun onClickOther() {

                }
            }).show()
    }



    /**
     * 写文件
     *
     * @param content 文件内容
     * @param filePath 文件路径(不要以/结尾)
     * @param fileName 文件名称（包含后缀,如：ReadMe.txt）
     * 新内容
     * @throws IOException
     */
    fun writeTxtFile(content: String, filePath: String, fileName: String, append: Boolean): Boolean {
        showLoading("生成数据中..")
        var flag: Boolean = true
        val thisFile = File("$filePath/$fileName")
        LogUtils.i("---------------","$filePath/$fileName")
        try {
            if (!thisFile.parentFile.exists()) {
                thisFile.parentFile.mkdirs()
            }
            val fw = FileWriter("$filePath/$fileName", append)
            errorAdapter.data.forEach {
                fw.write( "${it.shelf_code},${it.goods_code},${it.num}\r\n")
            }
            fw.close()
            resetData()
            dismissLoading()
            ToastUtils.showShort("{已生成${thisFile.name}}")
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return flag
    }

    fun resetData(){
        errorAdapter.data.clear();
        errorAdapter.notifyDataSetChanged();
        updateNum();
        mViewModel.currentShelf.value = ""
        mDatabind.etShelf.requestFocus()
    }

    override fun initScan() {

    }

    override fun readOrClose() {

    }
}