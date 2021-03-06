package com.bigoffs.pdaremake.app.ext

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.bigoffs.pdaremake.R
import com.bigoffs.pdaremake.app.network.ListDataUiState
import com.bigoffs.pdaremake.app.util.SettingUtil
import com.bigoffs.pdaremake.app.weight.loadCallBack.EmptyCallback
import com.bigoffs.pdaremake.app.weight.loadCallBack.ErrorCallback
import com.bigoffs.pdaremake.app.weight.loadCallBack.LoadingCallback
import com.bigoffs.pdaremake.app.weight.recyclerview.DefineLoadMoreView
import com.blankj.utilcode.util.LogUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.enums.PopupAnimation
import com.lxj.xpopup.interfaces.OnSelectListener
import com.yanzhenjie.recyclerview.SwipeRecyclerView
import me.hgj.jetpackmvvm.base.appContext
import me.hgj.jetpackmvvm.demo.app.weight.viewpager.ScaleTransitionPagerTitleView
import me.hgj.jetpackmvvm.ext.util.toHtml
import net.lucode.hackware.magicindicator.MagicIndicator
import net.lucode.hackware.magicindicator.buildins.UIUtil
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.badge.BadgeAnchor
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.badge.BadgeRule

/**
 * ???????????????
 */
fun hideSoftKeyboard(activity: Activity?) {
    activity?.let { act ->
        val view = act.currentFocus
        view?.let {
            val inputMethodManager =
                act.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(
                view.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
        }
    }
}
fun AppCompatActivity.showBottomSheedList(
    context: Context,
    data: Array<String>,
    title:String = "????????????",
    listener: OnSelectListener
){
    XPopup.Builder(context)
        .isDestroyOnDismiss(false) //???????????????????????????????????????????????????
        .asBottomList(
            title,
            data,
            null,
            2,
            false,
            listener,
            R.layout._xpopup_bottom_impl_list,
            R.layout._xpopup_adapter_text_match
        )
        .show()
}

fun Fragment.showBottomSheedList(context: Context, data: Array<String>, listener: OnSelectListener){
    XPopup.Builder(context)
        .isDestroyOnDismiss(false) //???????????????????????????????????????????????????
        .asBottomList(
            "????????????",
            data,
            null,
            2,
            false,
            listener,
            R.layout._xpopup_bottom_impl_list,
            R.layout._xpopup_adapter_text_match
        )
        .show()
}

fun AppCompatActivity.showSpinner(
    v: View,
    context: Context,
    data: List<String>,
    listener: OnSelectListener
){
    XPopup.Builder(context).popupAnimation(PopupAnimation.ScrollAlphaFromTop)
        .isDestroyOnDismiss(false) //???????????????????????????????????????????????????
        .hasShadowBg(false)
        .atView(v)
        .asAttachList(
            data.toTypedArray(),
            null,
            listener,
            R.layout._xpopup_down_impl_list,
            R.layout._xpopup_adapter_text_match
        ).show()


}

fun Fragment.showSpinner(
    v: View,
    context: Context,
    data: List<String>,
    listener: OnSelectListener
){
    XPopup.Builder(context).popupAnimation(PopupAnimation.ScrollAlphaFromTop)
        .isDestroyOnDismiss(false) //???????????????????????????????????????????????????
        .hasShadowBg(false)
        .atView(v)
        .asAttachList(
            data.toTypedArray(),
            null,
            listener,
            R.layout._xpopup_down_impl_list,
            R.layout._xpopup_adapter_text_match
        ).show()


}


fun loadServiceInit(view: View, callback: () -> Unit): LoadService<Any> {
    val loadsir = LoadSir.getDefault().register(view) {
        //??????????????????????????????
        callback.invoke()
    }
    loadsir.showSuccess()
    SettingUtil.setLoadingColor(SettingUtil.getColor(appContext), loadsir)
    return loadsir
}


fun LoadService<*>.setErrorText(message: String) {
    if (message.isNotEmpty()) {
        this.setCallBack(ErrorCallback::class.java) { _, view ->
            view.findViewById<TextView>(R.id.error_text).text = message
        }
    }
}

/**
 * ??????????????????
 * @param message ?????????????????????????????????
 */
fun LoadService<*>.showError(message: String = "") {
    this.setErrorText(message)
    this.showCallback(ErrorCallback::class.java)
}

/**
 * ???????????????
 */
fun LoadService<*>.showEmpty() {
    this.showCallback(EmptyCallback::class.java)
}

/**
 * ???????????????
 */
fun LoadService<*>.showLoading() {
    this.showCallback(LoadingCallback::class.java)
}

/**
 *
 */
fun EditText.addOnEditorActionListener(action: (String) -> Unit){
        setOnEditorActionListener { p0, p1, p2 ->
            if (p1 == EditorInfo.IME_ACTION_SEARCH) {
                action(p0.text.toString().trim())
            }else if(p1 == EditorInfo.IME_ACTION_NONE){
                action(p0.text.toString().trim())
            }
            true
        }


}

//??????rfid????????????????????????
fun EditText.addOnNoneEditorActionListener(action: (String) -> Unit){
    setOnEditorActionListener { p0, p1, p2 ->
//        if(p2.action == EditorInfo.IME_ACTION_NONE){
        if(p0.text.toString().trim().isNotEmpty() && p1 == EditorInfo.IME_ACTION_UNSPECIFIED || p1 == EditorInfo.IME_ACTION_SEARCH){
            LogUtils.i("----------------",p1)
            action(p0.text.toString().trim())
        }
        true
    }
//    addTextChangedListener(object :TextWatcher{
//        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//
//        }
//
//        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//
//        }
//
//        override fun afterTextChanged(s: Editable?) {
//            if(s.toString().isEmpty()){
//                return
//            }
//            action.invoke(s.toString())
//
//        }
//    })


}

//???????????????Recyclerview
fun RecyclerView.init(
    layoutManger: RecyclerView.LayoutManager,
    bindAdapter: RecyclerView.Adapter<*>,
    isScroll: Boolean = true
): RecyclerView {
    layoutManager = layoutManger
    setHasFixedSize(true)
    adapter = bindAdapter
    isNestedScrollingEnabled = isScroll
    return this
}

//??????SwipeRecyclerView
fun SwipeRecyclerView.init(
    layoutManger: RecyclerView.LayoutManager,
    bindAdapter: RecyclerView.Adapter<*>,
    isScroll: Boolean = true
): SwipeRecyclerView {
    layoutManager = layoutManger
    setHasFixedSize(true)
    adapter = bindAdapter
    isNestedScrollingEnabled = isScroll
    return this
}

fun SwipeRecyclerView.initFooter(loadmoreListener: SwipeRecyclerView.LoadMoreListener): DefineLoadMoreView {
    val footerView = DefineLoadMoreView(appContext)
    //?????????????????????
    footerView.setLoadViewColor(SettingUtil.getOneColorStateList(appContext))
    //????????????????????????
    footerView.setmLoadMoreListener(SwipeRecyclerView.LoadMoreListener {
        footerView.onLoading()
        loadmoreListener.onLoadMore()
    })
    this.run {
        //????????????????????????
        addFooterView(footerView)
        setLoadMoreView(footerView)
        //????????????????????????
        setLoadMoreListener(loadmoreListener)
    }
    return footerView
}

fun RecyclerView.initFloatBtn(floatbtn: FloatingActionButton) {
    //??????recyclerview?????????????????????????????????????????????????????????????????????
    addOnScrollListener(object : RecyclerView.OnScrollListener() {
        @SuppressLint("RestrictedApi")
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if (!canScrollVertically(-1)) {
                floatbtn.visibility = View.INVISIBLE
            }
        }
    })
    floatbtn.backgroundTintList = SettingUtil.getOneColorStateList(appContext)
    floatbtn.setOnClickListener {
        val layoutManager = layoutManager as LinearLayoutManager
        //????????????recyclerview ?????????????????????????????????????????????40????????????????????????????????????????????????????????????????????????
        if (layoutManager.findLastVisibleItemPosition() >= 40) {
            scrollToPosition(0)//?????????????????????????????????(??????)
        } else {
            smoothScrollToPosition(0)//??????????????????????????????(?????????)
        }
    }
}

//????????? SwipeRefreshLayout
fun SwipeRefreshLayout.init(onRefreshListener: () -> Unit) {
    this.run {
        setOnRefreshListener {
            onRefreshListener.invoke()
        }
        //??????????????????
        setColorSchemeColors(SettingUtil.getColor(appContext))
    }
}

//??????????????????????????????
fun BaseQuickAdapter<*, *>.setAdapterAnimation(mode: Int) {
    //??????0????????????????????? ????????????
    if (mode == 0) {
        this.animationEnable = false
    } else {
        this.animationEnable = true
        this.setAnimationWithDefault(BaseQuickAdapter.AnimationType.values()[mode - 1])
    }
}

fun MagicIndicator.bindViewPager2(
    viewPager: ViewPager2,
    mStringList: List<String> = arrayListOf(),
    action: (index: Int) -> Unit = {}
) {
    val commonNavigator = CommonNavigator(appContext)
    //????????????????????????????????????weight
    commonNavigator.isAdjustMode = true
    commonNavigator.adapter = object : CommonNavigatorAdapter() {

        override fun getCount(): Int {
            return  mStringList.size
        }
        override fun getTitleView(context: Context, index: Int): IPagerTitleView {
            return ScaleTransitionPagerTitleView(appContext).apply {
                //????????????
                text = mStringList[index].toHtml()
                //????????????
                textSize = 16f
                //???????????????
                normalColor = Color.parseColor("#999999")
                //????????????
                selectedColor = ContextCompat.getColor(context, R.color.colorPrimary)
                //????????????
                setOnClickListener {
                    viewPager.currentItem = index
                    action.invoke(index)
                }
            }


            // setup badge

//            // setup badge
//            if (index != 2) {
//                val badgeTextView = LayoutInflater.from(context)
//                    .inflate(R.layout.simple_count_badge_layout, null) as TextView
//                badgeTextView.text = "" + (index + 1)
//                badgePagerTitleView.setBadgeView(badgeTextView)
//            } else {
//                val badgeImageView = LayoutInflater.from(context)
//                    .inflate(R.layout.simple_red_dot_badge_layout, null) as ImageView
//                badgePagerTitleView.setBadgeView(badgeImageView)
//            }
//
//            // set badge position
//
//            // set badge position
//            if (index == 0) {
//                badgePagerTitleView.setXBadgeRule(
//                    BadgeRule(
//                        BadgeAnchor.CONTENT_LEFT,
//                        -UIUtil.dip2px(context, 6.0)
//                    )
//                )
//                badgePagerTitleView.setYBadgeRule(BadgeRule(BadgeAnchor.CONTENT_TOP, 0))
//            } else if (index == 1) {
//                badgePagerTitleView.setXBadgeRule(
//                    BadgeRule(
//                        BadgeAnchor.CONTENT_RIGHT,
//                        -UIUtil.dip2px(context, 6.0)
//                    )
//                )
//                badgePagerTitleView.setYBadgeRule(BadgeRule(BadgeAnchor.CONTENT_TOP, 0))
//            } else if (index == 2) {
//                badgePagerTitleView.setXBadgeRule(
//                    BadgeRule(
//                        BadgeAnchor.CENTER_X,
//                        -UIUtil.dip2px(context, 3.0)
//                    )
//                )
//                badgePagerTitleView.setYBadgeRule(
//                    BadgeRule(
//                        BadgeAnchor.CONTENT_BOTTOM,
//                        UIUtil.dip2px(context, 2.0)
//                    )
//                )
//            }
//
//            // don't cancel badge when tab selected
//
//            // don't cancel badge when tab selected
//            badgePagerTitleView.setAutoCancelBadge(false)

        }
        override fun getIndicator(context: Context): IPagerIndicator {
            return LinePagerIndicator(context).apply {
                mode = LinePagerIndicator.MODE_EXACTLY
                //??????????????????
                lineHeight = UIUtil.dip2px(appContext, 3.0).toFloat()
                lineWidth = UIUtil.dip2px(appContext, 30.0).toFloat()
                //???????????????
                roundRadius = UIUtil.dip2px(appContext, 6.0).toFloat()
                startInterpolator = AccelerateInterpolator()
                endInterpolator = DecelerateInterpolator(2.0f)
                //???????????????
                setColors(ContextCompat.getColor(context, R.color.colorPrimary))
            }
        }




    }
    this.navigator = commonNavigator

    viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            this@bindViewPager2.onPageSelected(position)
            action.invoke(position)
        }

        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
            super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            this@bindViewPager2.onPageScrolled(position, positionOffset, positionOffsetPixels)
        }

        override fun onPageScrollStateChanged(state: Int) {
            super.onPageScrollStateChanged(state)
            this@bindViewPager2.onPageScrollStateChanged(state)
        }
    })
}

fun ViewPager2.init(
    fragment: Fragment,
    fragments: ArrayList<Fragment>,
    isUserInputEnabled: Boolean = true
): ViewPager2 {
    //???????????????
    this.isUserInputEnabled = isUserInputEnabled
    //???????????????
    adapter = object : FragmentStateAdapter(fragment) {
        override fun createFragment(position: Int) = fragments[position]
        override fun getItemCount() = fragments.size
    }
    return this
}

fun ViewPager2.init(
    activity: FragmentActivity,
    fragments: ArrayList<Fragment>,
    isUserInputEnabled: Boolean = true
): ViewPager2 {
    //???????????????
    this.isUserInputEnabled = isUserInputEnabled
    //???????????????
    adapter = object : FragmentStateAdapter(activity) {
        override fun createFragment(position: Int) = fragments[position]
        override fun getItemCount() = fragments.size

    }
    return this
}


/**
 * ??????????????????
 */
fun <T> loadListData(
    data: ListDataUiState<T>,
    baseQuickAdapter: BaseQuickAdapter<T, *>,
    loadService: LoadService<*>,
    recyclerView: SwipeRecyclerView,
    swipeRefreshLayout: SwipeRefreshLayout
) {
    swipeRefreshLayout.isRefreshing = false
    recyclerView.loadMoreFinish(data.isEmpty, data.hasMore)
    if (data.isSuccess) {
        //??????
        when {
            //???????????????????????? ?????????????????????
            data.isFirstEmpty -> {
                loadService.showEmpty()
            }
            //????????????
            data.isRefresh -> {
                baseQuickAdapter.setList(data.listData)
                loadService.showSuccess()
            }
            //???????????????
            else -> {
                baseQuickAdapter.addData(data.listData)
                loadService.showSuccess()
            }
        }
    } else {
        //??????
        if (data.isRefresh) {
            //??????????????????????????????????????????????????????????????????
            loadService.showError(data.errMessage)
        } else {
            recyclerView.loadMoreError(0, data.errMessage)
        }
    }
}



