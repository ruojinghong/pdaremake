package com.bigoffs.pdaremake.ui.fragment

import android.os.Bundle
import android.widget.LinearLayout
import com.bigoffs.pdaremake.R
import com.bigoffs.pdaremake.app.base.BaseFragment
import com.bigoffs.pdaremake.app.base.BaseInStoreFragment
import com.bigoffs.pdaremake.app.ext.showSpinner
import com.bigoffs.pdaremake.databinding.FragmentPdaNewinstoreBinding
import com.bigoffs.pdaremake.databinding.FragmentPdaTransferinstoreBinding
import com.bigoffs.pdaremake.viewmodel.state.PdaNewInstoreViewModel
import com.bigoffs.pdaremake.viewmodel.state.RfidQueryViewModel
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel

/**
 *User:Kirito
 *Time:2021/5/5  23:40
 *Desc:调拨入库
 */
class PdaTransferInstoreFragment : BaseInStoreFragment<RfidQueryViewModel,FragmentPdaTransferinstoreBinding>(){


    var select = arrayListOf<String>("店内码", "箱号", "调拨单")


    override fun goInStoreDetail() {

    }

    override fun layoutId(): Int  = R.layout.fragment_pda_transferinstore

    override fun initView(savedInstanceState: Bundle?) {

        mDatabind.viewmodel = mViewModel
        activity?.findViewById<LinearLayout>(R.id.ll_select)?.setOnClickListener {

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
}