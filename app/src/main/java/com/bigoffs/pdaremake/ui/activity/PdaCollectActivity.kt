package com.bigoffs.pdaremake.ui.activity

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.collection.arraySetOf
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bigoffs.pdaremake.R
import com.bigoffs.pdaremake.app.base.BaseScanActivity
import com.bigoffs.pdaremake.app.ext.*
import com.bigoffs.pdaremake.app.util.DeviceUtil
import com.bigoffs.pdaremake.data.model.bean.*
import com.bigoffs.pdaremake.databinding.ActivityPdaCollectBinding
import com.bigoffs.pdaremake.ui.adapter.*
import com.bigoffs.pdaremake.ui.dialog.EditDialog
import com.bigoffs.pdaremake.ui.dialog.HintDialog
import com.bigoffs.pdaremake.viewmodel.request.RequestTallyViewModel
import com.bigoffs.pdaremake.viewmodel.state.TallyViewModel
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.xujiaji.library.RippleCheckBox
import me.hgj.jetpackmvvm.ext.parseState
import java.io.*


/**
 *User:Kirito
 *Time:2021/5/10  22:18
 *Desc:店内码理货
 */
class PdaCollectActivity :
    BaseScanActivity<TallyViewModel, ActivityPdaCollectBinding>() {
    val MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1
    override fun layoutId(): Int = R.layout.activity_pda_collect
    val set = arraySetOf<String>()

    val requestTallyViewModel: RequestTallyViewModel by viewModels()


    private lateinit var errorRecyclerView: RecyclerView
    private lateinit var checkBoxRecyclerView: RecyclerView
    private lateinit var errorBottomsheetDialog: BottomSheetDialog
    private lateinit var errorBottomSheetNum: TextView

    private lateinit var editDialog: EditDialog


    //适配器
    private val errorAdapter: CollectlAdapter by lazy { CollectlAdapter(arrayListOf()) }
    private val checkBoxAdapter: CheckBoxlAdapter by lazy { CheckBoxlAdapter(arrayListOf()) }


    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)


        mDatabind.vm = mViewModel
        mDatabind.click = ProxyClick()
        checkBoxRecyclerView = mDatabind.checkRecycler
        val manager = LinearLayoutManager(this)
        checkBoxRecyclerView.init(manager, checkBoxAdapter)
//        mDatabind.etUnique.setOnFocusChangeListener() { v, hasFocus ->
//            if (hasFocus) {
//                mViewModel.currentFocus.value = 1
//                mDatabind.devideUnique.setBackgroundColor(Color.parseColor("#0033cc"))
//            } else {
//                mDatabind.devideUnique.setBackgroundColor(Color.parseColor("#EEEEEE"))
//            }
//        }
//
//        mDatabind.etShelf.setOnFocusChangeListener() { v, hasFocus ->
//            if (hasFocus) {
//                mViewModel.currentFocus.value = 3
//                mDatabind.devideShelf.setBackgroundColor(Color.parseColor("#0033cc"))
//            } else {
//                mDatabind.devideShelf.setBackgroundColor(Color.parseColor("#EEEEEE"))
//            }
//        }


        checkBoxAdapter.addData(CheckBoxBean(null, null))
        checkBoxAdapter.setDeleteClick { adapter, view, position ->
            var canDelete = true
            errorAdapter.data.forEach {
                when (position) {
                    0 -> {
                        if (it.code1.isNotEmpty()) {
                            beep()
                            ToastUtils.showShort("这一列有数据，无法删除")
                            canDelete = false
                            return@forEach
                        }
                    }
                    1 -> {
                        if (it.code2.isNotEmpty()) {
                            beep()
                            ToastUtils.showShort("这一列有数据，无法删除")
                            canDelete = false
                            return@forEach
                        }
                    }
                    2 -> {
                        if (it.code3.isNotEmpty()) {
                            beep()
                            ToastUtils.showShort("这一列有数据，无法删除")
                            canDelete = false
                            return@forEach
                        }
                    }
                    3 -> {
                        if (it.code4.isNotEmpty()) {
                            beep()
                            ToastUtils.showShort("这一列有数据，无法删除")
                            canDelete = false
                            return@forEach
                        }
                    }
                    4 -> {
                        if (it.code5.isNotEmpty()) {
                            beep()
                            ToastUtils.showShort("这一列有数据，无法删除")
                            canDelete = false
                            return@forEach
                        }
                    }
                }
            }
            if (canDelete) {
                checkBoxAdapter.removeAt(position)
                checkBoxAdapter.notifyDataSetChanged()
                mDatabind.checkRecycler.requestLayout()
            }

        }



        initBottomSheet();
        editDialog = EditDialog.create(mContext)
            .setTitle("请扫描剔除店内码")
            .setHintText("请扫描店内码")
            .setOnClickListener(object : EditDialog.OnHintDialogListener {
                override fun onClickOk(content: String?) {
                    deleteItem(editDialog.genContentText())
                    editDialog.setContentText("")
                    editDialog.dismiss()

                }

                override fun onClickCancel() {
                    editDialog.dismiss()
                }

            })

        if (DeviceUtil.isRfidDevice()) {

//            mDatabind.etShelf.addOnNoneEditorActionListener{
//                mDatabind.etShelf.setText("")
//                onReceiverData(it)
//            }
//            mDatabind.etUnique.addOnNoneEditorActionListener {
//                mDatabind.etUnique.setText("")
//                onReceiverData(it)
//            }
        }
    }

    override fun setStatusBar() {
        initTitle(false, biaoti = "数据采集")
        var imageButton = findViewById<ImageButton>(R.id.iv_right_btn)
        imageButton.visibility = View.VISIBLE
        imageButton.setImageResource(R.mipmap.icon_plus)
        imageButton.setOnClickListener {
            if (checkBoxAdapter.data.size < 5) {
                checkBoxAdapter.addData(CheckBoxBean(null, null))
                mDatabind.checkRecycler.requestLayout()
            }
        }
    }


    override fun onReceiverData(data: String) {

        if (editDialog.isShowing) {
            editDialog.setContentText(data)
        } else {

            for ((index, value) in checkBoxAdapter.data.withIndex()) {

                if (value.edittext?.hasFocus() == true) {
                    lateinit var collect: CollectBean
                    if (index == 0) {
                        collect = CollectBean(data)
                        errorAdapter.addData(collect)
                    } else {
                        if (errorAdapter.data.isEmpty()) {
                            beep()
                            ToastUtils.showShort("第一行数据不能为空")
                            return
                        }
                        collect = errorAdapter.data[errorAdapter.data.size - 1]

                        when (index) {
                            1 -> {
                                collect.code2 = data
                            }
                            2 -> {
                                collect.code3 = data
                            }
                            3 -> {
                                collect.code4 = data
                            }
                            4 -> {
                                collect.code5 = data
                            }
                        }
                        errorAdapter.notifyDataSetChanged()
                    }
                    updateNum()
                    findNextFocus(index)
                    return
                }

            }


        }


    }

    private fun findNextFocus(position: Int) {
        //如何从中间循环到中间呢

        if (position == checkBoxAdapter.data.size - 1) {

            for ((index, value) in checkBoxAdapter.data.withIndex()) {
                if (value.checkbox?.currentStatus == RippleCheckBox.Status.HOOK) {
                    value.edittext?.let {
                        it.requestFocus()
                        return
                    }
                }
            }

        } else {

            for (i in position + 1 until checkBoxAdapter.data.size) {
                if (checkBoxAdapter.data[i].checkbox?.currentStatus == RippleCheckBox.Status.HOOK) {
                    checkBoxAdapter.data[i].edittext?.let {
                        it.requestFocus()
                        return
                    }
                }

            }
            for (i in 0..position) {
                if (checkBoxAdapter.data[i].checkbox?.currentStatus == RippleCheckBox.Status.HOOK) {
                    checkBoxAdapter.data[i].edittext?.let {
                        it.requestFocus()
                        return
                    }
                }
            }

        }


    }

    private fun initBottomSheet() {


        var view2 = View.inflate(this, R.layout.bottom_collect, null)
        errorRecyclerView = view2.findViewById(R.id.dialog_recycleView)
        view2.findViewById<ImageView>(R.id.iv_unfold).setOnClickListener {
            errorBottomsheetDialog.dismiss()
        }
        errorRecyclerView.init(LinearLayoutManager(this), errorAdapter)
        errorBottomsheetDialog = BottomSheetDialog(this, R.style.dialog)
        errorBottomsheetDialog.setContentView(view2)
        errorBottomSheetNum = view2.findViewById(R.id.tv_bottom_error_num)
        BottomSheetBehavior.from(view2.parent as View).peekHeight = getPeekHeight()
        errorAdapter.setOnItemClickListener { _, _, position ->

            showdeleteDialog(position)
        }
    }


    override fun createObserver() {
        super.createObserver()
        requestTallyViewModel.uniqueUploadResult.observe(this, { state ->

            parseState(state, {
                ToastUtils.showShort("操作成功")
                finish()

            }, {
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
            showClearDialog()

        }

        fun onCancel() {
            finish()
        }

        fun onUpload() {
            upload()
        }

    }

    fun deleteItem(barcode: String) {
        mViewModel.currentUniqueSet.remove(barcode)
        val errorIterator = errorAdapter.data.iterator()
        while (errorIterator.hasNext()) {
            var next = errorIterator.next()
//            if (next.goods_code == barcode) {
//                errorIterator.remove()
//                errorAdapter.notifyDataSetChanged()
//                updateNum()
//
//            }
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

    fun upload() {
        showUploadDialog()


    }

    fun showUploadDialog() {
        HintDialog.create(this, HintDialog.STYLE_ONLY_OK).setTitle("").setContent("确定生成txt？")
            .setLeftBtnText("取消")
            .setRightBtnText("确定")
            .setDialogListener(object : HintDialog.OnHintDialogListener {
                override fun onClickOk() {
//                    requestPermission()


                writeTxtFile("",  Environment.getExternalStorageDirectory().getAbsolutePath()+"/pda","${System.currentTimeMillis()}.txt",true)

                }

                override fun onClickCancel() {

                }

                override fun onClickOther() {

                }
            }).show()
    }

    fun showClearDialog() {
        HintDialog.create(this, HintDialog.STYLE_ONLY_OK).setTitle("").setContent("确定清空数据？")
            .setLeftBtnText("取消")
            .setRightBtnText("确定")
            .setDialogListener(object : HintDialog.OnHintDialogListener {
                override fun onClickOk() {
                    errorAdapter.data.clear()
                    errorAdapter.notifyDataSetChanged()
                    updateNum()
                }

                override fun onClickCancel() {

                }

                override fun onClickOther() {

                }
            }).show()
    }

    fun showdeleteDialog(position: Int) {
        HintDialog.create(this, HintDialog.STYLE_ONLY_OK).setTitle("").setContent("确定删除数据？")
            .setLeftBtnText("取消")
            .setRightBtnText("确定")
            .setDialogListener(object : HintDialog.OnHintDialogListener {
                override fun onClickOk() {
                    errorAdapter.removeAt(position)
                    errorAdapter.notifyDataSetChanged()
                    updateNum()
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
        var flag: Boolean = true
        val thisFile = File("$filePath/$fileName")
        LogUtils.i("---------------","$filePath/$fileName")
        try {
            if (!thisFile.parentFile.exists()) {
                thisFile.parentFile.mkdirs()
            }
            val fw = FileWriter("$filePath/$fileName", append)
            errorAdapter.data.forEach {
                var content:String = ""
                if(it.code1.isNotEmpty()){
                    content+=it.code1+","
                }
                if(it.code2.isNotEmpty()){
                    content+=it.code2+","
                }
                if(it.code3.isNotEmpty()){
                    content+=it.code3+","
                }
                if(it.code4.isNotEmpty()){
                    content+=it.code4+","
                }
                if(it.code5.isNotEmpty()){
                    content+=it.code5+","
                }
                content+=it.numberId+"\r\n"
                fw.write(content)
            }


            fw.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return flag
    }



    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        when(requestCode){
            MY_PERMISSIONS_REQUEST_READ_CONTACTS ->{
                mContext.getCacheDir().getAbsolutePath().let { writeTxtFile("", it,"${System.currentTimeMillis()}.txt",true) }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

}