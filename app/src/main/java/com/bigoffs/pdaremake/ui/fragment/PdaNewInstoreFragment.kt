package com.bigoffs.pdaremake.ui.fragment

import android.os.Bundle
import android.widget.LinearLayout
import androidx.fragment.app.viewModels
import com.bigoffs.pdaremake.R
import com.bigoffs.pdaremake.app.base.BaseFragment
import com.bigoffs.pdaremake.app.base.BaseInStoreFragment
import com.bigoffs.pdaremake.app.ext.showSpinner
import com.bigoffs.pdaremake.databinding.FragmentPdaNewinstoreBinding
import com.bigoffs.pdaremake.viewmodel.request.RequestInstoreViewmodel
import com.bigoffs.pdaremake.viewmodel.state.PdaNewInstoreViewModel
import com.bigoffs.pdaremake.viewmodel.state.RfidQueryViewModel
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel

/**
 *User:Kirito
 *Time:2021/5/5  23:40
 *Desc:新品入库
 */
class PdaNewInstoreFragment : BaseInStoreFragment<PdaNewInstoreViewModel,FragmentPdaNewinstoreBinding>(){


    var select = arrayListOf<String>("条形码", "供应商", "入库批次")


    val requestInstoreViewmodel :RequestInstoreViewmodel  by viewModels()

    override fun goInStoreDetail() {

    }

    override fun layoutId(): Int  = R.layout.fragment_pda_newinstore

    override fun initView(savedInstanceState: Bundle?) {

        mDatabind.viewmodel = mViewModel
        mDatabind.root.findViewById<LinearLayout>(R.id.ll_select_new)?.setOnClickListener {

            context?.let { it1 ->
                showSpinner(it, it1, select) { position, text ->
                    setCurrentType(position)
                }
            }

        }

        mViewModel.currentCodeText.value = select[0]
    }


    fun setCurrentType(position: Int) {
        if (position >= select.size) {
            return
        }
        mViewModel.currentCodeText.value = select[position]

    }

    override fun lazyLoadData() {
        super.lazyLoadData()
        requestInstoreViewmodel.getNewInstoreList("","","")

    }
}