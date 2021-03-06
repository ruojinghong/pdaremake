package com.bigoffs.pdaremake.ui.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Environment
import android.os.Parcel
import android.os.Parcelable
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
import com.bigoffs.pdaremake.databinding.*

import com.bigoffs.pdaremake.ui.activity.rfid.FindEpcByBarcodeActivity
import com.bigoffs.pdaremake.ui.adapter.NewInStoreErrorAdapter
import com.bigoffs.pdaremake.ui.adapter.NewInStoreNormalAdapter
import com.bigoffs.pdaremake.ui.adapter.StocktakingUniquelAdapter
import com.bigoffs.pdaremake.ui.adapter.TallyUniquelAdapter
import com.bigoffs.pdaremake.ui.dialog.EditDialog
import com.bigoffs.pdaremake.ui.dialog.HintDialog
import com.bigoffs.pdaremake.ui.dialog.InputDialog
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
 *Desc:???????????????
 */
class RfidBarcodeStocktakingActivity() :
    BaseRfidFActivity<StocktakingViewModel, ActivityRfidBarcodeStocktakingBinding>() {
    override fun layoutId(): Int = R.layout.activity_rfid_barcode_stocktaking
    val set = arraySetOf<String>()

    val requestStocktakingViewModel: RequestStocktakingViewModel by viewModels()



    private lateinit var errorRecyclerView: RecyclerView
    private lateinit var errorBottomsheetDialog: BottomSheetDialog
    private lateinit var errorBottomSheetNum: TextView

    private lateinit var editDialog: EditDialog

    private var bean : StocktakingListBean?  = null


    //?????????
    private val errorAdapter: StocktakingUniquelAdapter by lazy { StocktakingUniquelAdapter(arrayListOf()) }

    constructor(parcel: Parcel) : this() {
        bean = parcel.readParcelable(StocktakingListBean::class.java.classLoader)
    }


    override fun setStatusBar() {
        initTitle(false, biaoti = "??????")
    }


     fun onReceiverData(data: String) {

        if(editDialog.isShowing){
                editDialog.setContentText(data)
        }else{
            when (mViewModel.currentFocus.value) {
                //???????????????
                1 -> {

                    if(errorAdapter.data.size > 3000){
                        ToastUtils.showShort("??????????????????3000???")
                        beep()
                        return;
                    }

                    if(TextUtils.isEmpty(mViewModel.currentShelf.value)){
                        ToastUtils.showShort("?????????????????????")
                        beep()
                        return;
                    }

                    mViewModel.currentScanBarcode.value = data
                   requestStocktakingViewModel.stocktakingCheckBarcode(data);


                }
                //???????????????
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
        view2.findViewById<TextView>(R.id.title2).text = "?????????"
        view2.findViewById<TextView>(R.id.title).text = "????????????"

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
        HintDialog.create(this, HintDialog.STYLE_ONLY_OK).setTitle("").setContent("???????????????")
            .setLeftBtnText("??????")
            .setRightBtnText("??????")
            .setDialogListener(object : HintDialog.OnHintDialogListener{
                override fun onClickOk() {
                    mViewModel.errorNum.value -= errorAdapter.data[position].num.toInt()
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
            .setTitle("????????????????????????")
            .setHintText("??????????????????")
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

            if(it.st_status == 1){
                mDatabind.tvRight.text = "??????????????????"
            }else{
                mDatabind.tvRight.text = "??????????????????"
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


        requestStocktakingViewModel.checkBarcode.observe(this,{state ->

            parseState(state,{
                mViewModel.currentUniqueSet.add(mViewModel.currentScanUnique.value)
                showDownDialog(mViewModel.currentScanBarcode.value)


            },{
//               mViewModel.currentUniqueSet.add(mViewModel.currentScanUnique.value)
//                showDownDialog(mViewModel.currentScanBarcode.value)
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
                ToastUtils.showShort("??????????????????")
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
                    mViewModel.errorNum.value -= next.num.toInt()
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

        errorBottomSheetNum.text = mViewModel.errorNum.value.toString()
        mViewModel.normalNum.value = mViewModel.errorNum.value
        mViewModel.scanNum.value = mViewModel.errorNum.value

    }

    fun upload(){
        showUploadDialog()


    }

    fun showUploadDialog(){
        HintDialog.create(this, HintDialog.STYLE_ONLY_OK).setTitle("").setContent("?????????????????????????????????")
            .setLeftBtnText("??????")
            .setRightBtnText("??????")
            .setDialogListener(object : HintDialog.OnHintDialogListener{
                override fun onClickOk() {
                    bean?.let {
                        requestStocktakingViewModel.uploadStacktakingData(errorAdapter.data,it.st_status,it.id,it.st_type)
                    }

                }

                override fun onClickCancel() {

                }

                override fun onClickOther() {

                }
            }).show()
    }

    fun showSaveDialog() {
        HintDialog.create(this, HintDialog.STYLE_ONLY_OK).setTitle("").setContent("????????????txt???")
            .setLeftBtnText("??????")
            .setRightBtnText("??????")
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
     * ?????????
     *
     * @param content ????????????
     * @param filePath ????????????(?????????/??????)
     * @param fileName ???????????????????????????,??????ReadMe.txt???
     * ?????????
     * @throws IOException
     */
    fun writeTxtFile(content: String, filePath: String, fileName: String, append: Boolean): Boolean {
        showLoading("???????????????..")
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
            ToastUtils.showShort("{?????????${thisFile.name}}")
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return flag
    }

    fun resetData(){
        errorAdapter.data.clear();
        errorAdapter.notifyDataSetChanged();
        mViewModel.errorNum.value = 0
        updateNum();
        mViewModel.currentShelf.value = ""
        mDatabind.etShelf.requestFocus()
    }

    fun clear(){
        HintDialog.create(this, HintDialog.STYLE_ONLY_OK).setTitle("").setContent("?????????????????????")
            .setLeftBtnText("??????")
            .setRightBtnText("??????")
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


    //???????????????dialog
    fun showDownDialog(barcode:String){

        var position = -1;
        for (i in 0 until errorAdapter.data.size){
            if(mViewModel.currentShelf.value == errorAdapter.data[i].shelf_code){
                if(errorAdapter.data[i].goods_code == barcode){
                        position = i
                        break
                }
            }
        }
        if(position <0){
            InputDialog.create(this)
                .setTitle("?????????${barcode}")
                .setRightBtnText("??????")
                .setOnClickListener(object : InputDialog.OnHintDialogListener{
                    override fun onClickOk(content: String) {
                        val inputNum = content.toInt()
                        errorAdapter.addData(UniqueStocktakingBean(mViewModel.currentShelf.value,barcode,content))
                        mViewModel.errorNum.value += inputNum
                        updateNum()

                    }

                    override fun onClickCancel() {

                    }
                }).show()
        }else{
            InputDialog.create(this)
                .setTitle("???????????????${barcode}")
                .setRightBtnText("??????")
                .setOnClickListener(object : InputDialog.OnHintDialogListener{
                    override fun onClickOk(content: String) {
                        val inputNum = content.toInt()
                        mViewModel.errorNum.value -= errorAdapter.data[position].num.toInt()
                        errorAdapter.data[position].num = content
                        mViewModel.errorNum.value += inputNum
                        errorAdapter.notifyDataSetChanged()
                        updateNum()
                    }

                    override fun onClickCancel() {

                    }
                }).show()
        }






    }

    override fun initScan() {

    }

    override fun readOrClose() {

    }
}