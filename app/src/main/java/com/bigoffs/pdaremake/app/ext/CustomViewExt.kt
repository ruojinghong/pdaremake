package com.bigoffs.pdaremake.app.ext

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Color
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
 * 隐藏软键盘
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
    listener: OnSelectListener
){
    XPopup.Builder(context)
        .isDestroyOnDismiss(false) //对于只使用一次的弹窗，推荐设置这个
        .asBottomList(
            "选择仓库",
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
        .isDestroyOnDismiss(false) //对于只使用一次的弹窗，推荐设置这个
        .asBottomList(
            "选择仓库",
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
        .isDestroyOnDismiss(false) //对于只使用一次的弹窗，推荐设置这个
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
        .isDestroyOnDismiss(false) //对于只使用一次的弹窗，推荐设置这个
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
        //点击重试时触发的操作
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
 * 设置错误布局
 * @param message 错误布局显示的提示内容
 */
fun LoadService<*>.showError(message: String = "") {
    this.setErrorText(message)
    this.showCallback(ErrorCallback::class.java)
}

/**
 * 设置空布局
 */
fun LoadService<*>.showEmpty() {
    this.showCallback(EmptyCallback::class.java)
}

/**
 * 设置加载中
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

//绑定普通的Recyclerview
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

//绑定SwipeRecyclerView
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
    //给尾部设置颜色
    footerView.setLoadViewColor(SettingUtil.getOneColorStateList(appContext))
    //设置尾部点击回调
    footerView.setmLoadMoreListener(SwipeRecyclerView.LoadMoreListener {
        footerView.onLoading()
        loadmoreListener.onLoadMore()
    })
    this.run {
        //添加加载更多尾部
        addFooterView(footerView)
        setLoadMoreView(footerView)
        //设置加载更多回调
        setLoadMoreListener(loadmoreListener)
    }
    return footerView
}

fun RecyclerView.initFloatBtn(floatbtn: FloatingActionButton) {
    //监听recyclerview滑动到顶部的时候，需要把向上返回顶部的按钮隐藏
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
        //如果当前recyclerview 最后一个视图位置的索引大于等于40，则迅速返回顶部，否则带有滚动动画效果返回到顶部
        if (layoutManager.findLastVisibleItemPosition() >= 40) {
            scrollToPosition(0)//没有动画迅速返回到顶部(马上)
        } else {
            smoothScrollToPosition(0)//有滚动动画返回到顶部(有点慢)
        }
    }
}

//初始化 SwipeRefreshLayout
fun SwipeRefreshLayout.init(onRefreshListener: () -> Unit) {
    this.run {
        setOnRefreshListener {
            onRefreshListener.invoke()
        }
        //设置主题颜色
        setColorSchemeColors(SettingUtil.getColor(appContext))
    }
}

//设置适配器的列表动画
fun BaseQuickAdapter<*, *>.setAdapterAnimation(mode: Int) {
    //等于0，关闭列表动画 否则开启
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
    //我吐了，下边属性用来控制weight
    commonNavigator.isAdjustMode = true
    commonNavigator.adapter = object : CommonNavigatorAdapter() {

        override fun getCount(): Int {
            return  mStringList.size
        }
        override fun getTitleView(context: Context, index: Int): IPagerTitleView {
            return ScaleTransitionPagerTitleView(appContext).apply {
                //设置文本
                text = mStringList[index].toHtml()
                //字体大小
                textSize = 16f
                //未选中颜色
                normalColor = Color.parseColor("#999999")
                //选中颜色
                selectedColor = ContextCompat.getColor(context, R.color.colorPrimary)
                //点击事件
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
                //线条的宽高度
                lineHeight = UIUtil.dip2px(appContext, 3.0).toFloat()
                lineWidth = UIUtil.dip2px(appContext, 30.0).toFloat()
                //线条的圆角
                roundRadius = UIUtil.dip2px(appContext, 6.0).toFloat()
                startInterpolator = AccelerateInterpolator()
                endInterpolator = DecelerateInterpolator(2.0f)
                //线条的颜色
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
    //是否可滑动
    this.isUserInputEnabled = isUserInputEnabled
    //设置适配器
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
    //是否可滑动
    this.isUserInputEnabled = isUserInputEnabled
    //设置适配器
    adapter = object : FragmentStateAdapter(activity) {
        override fun createFragment(position: Int) = fragments[position]
        override fun getItemCount() = fragments.size

    }
    return this
}


/**
 * 加载列表数据
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
        //成功
        when {
            //第一页并没有数据 显示空布局界面
            data.isFirstEmpty -> {
                loadService.showEmpty()
            }
            //是第一页
            data.isRefresh -> {
                baseQuickAdapter.setList(data.listData)
                loadService.showSuccess()
            }
            //不是第一页
            else -> {
                baseQuickAdapter.addData(data.listData)
                loadService.showSuccess()
            }
        }
    } else {
        //失败
        if (data.isRefresh) {
            //如果是第一页，则显示错误界面，并提示错误信息
            loadService.showError(data.errMessage)
        } else {
            recyclerView.loadMoreError(0, data.errMessage)
        }
    }
}



