package com.bigoffs.pdaremake.ui.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.StyleableRes;
import androidx.appcompat.content.res.AppCompatResources;

import com.bigoffs.pdaremake.R;
import com.blankj.utilcode.util.SizeUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DropDownMenu extends LinearLayout {

    protected int mSelectPosition = -1;
    /**
     * 顶部菜单布局
     */
    private LinearLayout mTabMenuView;
    /**
     * 底部容器，包含popupMenuViews，maskView
     */
    private FrameLayout mContainerView;
    /**
     * 弹出菜单父布局
     */
    private FrameLayout mPopupMenuViews;
    /**
     * 遮罩半透明View，点击可关闭DropDownMenu
     */
    private View mMaskView;
    /**
     * tabMenuView里面选中的tab位置，-1表示未选中
     */
    private int mCurrentTabPosition = -1;

    private int mContentLayoutId;
    private View mContentView;
    /**
     * 分割线颜色
     */
    private int mDividerColor;
    /**
     * 分割线宽度
     */
    private int mDividerWidth;
    /**
     * 分割线的Margin
     */
    private int mDividerMargin;
    /**
     * tab选中颜色
     */
    private int mMenuTextSelectedColor;
    /**
     * tab未选中颜色
     */
    private int mMenuTextUnselectedColor;
    /**
     * tab字体水平padding
     */
    private int mMenuTextPaddingHorizontal;
    /**
     * tab字体水平padding
     */
    private int mMenuTextPaddingVertical;
    /**
     * 遮罩颜色
     */
    private int mMaskColor;
    /**
     * tab字体大小
     */
    private int mMenuTextSize;
    /**
     * tab选中图标
     */
    private Drawable mMenuSelectedIcon;
    /**
     * tab未选中图标
     */
    private Drawable mMenuUnselectedIcon;
    /**
     * 选择菜单的高度/屏幕高度 占比
     */
    private float mMenuHeightPercent = 0.5F;

    private List<String> mData = new ArrayList<String>();
    private DropDownAdapter mAdapter;
    private ListView listView;

    public DropDownMenu(Context context) {
        super(context, null);
    }

    public DropDownMenu(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.DropDownMenuStyle);
    }

    public DropDownMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(VERTICAL);
        //为DropDownMenu添加自定义属性
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.DropDownMenu);
        mContentLayoutId = array.getResourceId(R.styleable.DropDownMenu_ddm_contentLayoutId, -1);
        mDividerColor = array.getColor(R.styleable.DropDownMenu_ddm_dividerColor, Color.BLACK);
        mDividerWidth = array.getDimensionPixelSize(R.styleable.DropDownMenu_ddm_dividerWidth, 0);
        mDividerMargin = array.getDimensionPixelSize(R.styleable.DropDownMenu_ddm_dividerMargin,0);
        int underlineColor = array.getColor(R.styleable.DropDownMenu_ddm_underlineColor,Color.BLACK);
        int underlineHeight = array.getDimensionPixelSize(R.styleable.DropDownMenu_ddm_underlineHeight, 0);
        int menuBackgroundColor = array.getColor(R.styleable.DropDownMenu_ddm_menuBackgroundColor, Color.WHITE);
        mMaskColor = array.getColor(R.styleable.DropDownMenu_ddm_maskColor, Color.parseColor("#88888888"));
        mMenuTextSelectedColor = array.getColor(R.styleable.DropDownMenu_ddm_menuTextSelectedColor,Color.BLACK);
        mMenuTextUnselectedColor = array.getColor(R.styleable.DropDownMenu_ddm_menuTextUnselectedColor, Color.BLACK);
        mMenuTextPaddingHorizontal = array.getDimensionPixelSize(R.styleable.DropDownMenu_ddm_menuTextPaddingHorizontal, 1);
        mMenuTextPaddingVertical = array.getDimensionPixelSize(R.styleable.DropDownMenu_ddm_menuTextPaddingVertical, 1);
        mMenuTextSize = array.getDimensionPixelSize(R.styleable.DropDownMenu_ddm_menuTextSize, 14);
        mMenuUnselectedIcon = getDrawableAttrRes(getContext(), array, R.styleable.DropDownMenu_ddm_menuUnselectedIcon);
        if (mMenuUnselectedIcon == null) {
            mMenuUnselectedIcon = getVectorDrawable(getContext(), R.drawable.ddm_ic_arrow_down);
        }
        mMenuSelectedIcon = getDrawableAttrRes(getContext(), array, R.styleable.DropDownMenu_ddm_menuSelectedIcon);
        if (mMenuSelectedIcon == null) {
            mMenuSelectedIcon = getVectorDrawable(getContext(), R.drawable.ddm_ic_arrow_up);
        }
        mMenuHeightPercent = array.getFloat(R.styleable.DropDownMenu_ddm_menuHeightPercent, mMenuHeightPercent);
        array.recycle();

        //初始化tabMenuView并添加到tabMenuView
        mTabMenuView = new LinearLayout(context);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mTabMenuView.setOrientation(HORIZONTAL);
        mTabMenuView.setBackgroundColor(menuBackgroundColor);
        mTabMenuView.setLayoutParams(params);
        addView(mTabMenuView, 0);

        //为tabMenuView添加下划线
        View underLine = new View(getContext());
        underLine.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, underlineHeight));
        underLine.setBackgroundColor(underlineColor);
        addView(underLine, 1);

        //初始化containerView并将其添加到DropDownMenu
        mContainerView = new FrameLayout(context);
        mContainerView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        addView(mContainerView, 2);

        listView = new ListView(getContext());
        mAdapter = new DropDownAdapter();
        listView.setDividerHeight(0);
        listView.setAdapter(mAdapter);

    }







    /**
     * 初始化DropDownMenu
     *
     * @param tabTexts
     * @param popupViews
     * @param contentView
     */
    public void setDropDownMenu(@NonNull String tabTexts, @NonNull List<String> popupViews, @NonNull View contentView, AdapterView.OnItemClickListener listener) {
//        if (tabTexts.size() != popupViews.size()) {
//            throw new IllegalArgumentException("params not match, tabTexts.size() should be equal popupViews.size()");
//        }

//        for (int i = 0; i < tabTexts.size(); i++) {
            addTab(tabTexts);
//        }

        mContentView = contentView;
//        mContainerView.addView(contentView, 0);

        mMaskView = new View(getContext());
        mMaskView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        mMaskView.setBackgroundColor(mMaskColor);
        mMaskView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                closeMenu();
            }
        });
        mContainerView.addView(mMaskView, 0);
        mMaskView.setVisibility(GONE);
        if (mContainerView.getChildAt(1) != null) {
            mContainerView.removeViewAt(1);
        }

        mPopupMenuViews = new FrameLayout(getContext());
//        mPopupMenuViews.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (DensityUtils.getDisplaySize(getContext(), true).y * mMenuHeightPercent)));
        mPopupMenuViews.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, SizeUtils.dp2px(90)));
        mPopupMenuViews.setVisibility(GONE);
        mContainerView.addView(mPopupMenuViews, 1);



        mData.clear();
        mData.addAll(popupViews);
        mAdapter.notifyDataSetChanged();
        listView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mPopupMenuViews.addView(listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    mAdapter.setSelection(i);
                    setTabMenuText(mData.get(i));
                    closeMenu();
            }
        });
    }

    private void addTab(@NonNull String tabTexts) {
        final TextView tab = new TextView(getContext());
        tab.setSingleLine();
        tab.setEllipsize(TextUtils.TruncateAt.END);
        tab.setGravity(Gravity.CENTER);
        tab.setTextSize(TypedValue.COMPLEX_UNIT_PX, mMenuTextSize);
        tab.setLayoutParams(new LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0F));
        tab.setTextColor(mMenuTextUnselectedColor);
        setArrowIconEnd(tab, mMenuUnselectedIcon);
        tab.setText(tabTexts);
        tab.setPadding(mMenuTextPaddingHorizontal, mMenuTextPaddingVertical, mMenuTextPaddingHorizontal, mMenuTextPaddingVertical);
        //添加点击事件
        tab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                switchMenu(tab);
            }
        });
        mTabMenuView.addView(tab);
        //添加分割线
//        if (index < tabTexts.size() - 1) {
//            View view = new View(getContext());
//            LayoutParams params = new LayoutParams(mDividerWidth, ViewGroup.LayoutParams.MATCH_PARENT);
//            params.topMargin = mDividerMargin;
//            params.bottomMargin = mDividerMargin;
//            view.setLayoutParams(params);
//            view.setBackgroundColor(mDividerColor);
//            mTabMenuView.addView(view);
//        }
    }

    /**
     * 设置tab菜单文字
     *
     * @param text
     */
    public void setTabMenuText(String text) {
        if (mCurrentTabPosition != -1) {
            ((TextView) mTabMenuView.getChildAt(mCurrentTabPosition)).setText(text);
        }
    }

    /**
     * 设置tab菜单是否可点击
     *
     * @param clickable
     */
    public void setTabMenuClickable(boolean clickable) {
        for (int i = 0; i < mTabMenuView.getChildCount(); i = i + 2) {
            mTabMenuView.getChildAt(i).setClickable(clickable);
        }
    }

    /**
     * 关闭菜单
     */
    public void closeMenu() {
        if (mCurrentTabPosition != -1) {
            ((TextView) mTabMenuView.getChildAt(mCurrentTabPosition)).setTextColor(mMenuTextUnselectedColor);
            setArrowIconEnd((TextView) mTabMenuView.getChildAt(mCurrentTabPosition), mMenuUnselectedIcon);
            mPopupMenuViews.setVisibility(View.GONE);
            mPopupMenuViews.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.ddm_menu_out));
            mMaskView.setVisibility(GONE);
            mMaskView.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.ddm_mask_out));
            mCurrentTabPosition = -1;
        }
    }

    /**
     * DropDownMenu是否处于可见状态
     *
     * @return
     */
    public boolean isShowing() {
        return mCurrentTabPosition != -1;
    }

    /**
     * @return 内容页
     */
    public View getContentView() {
        return mContentView;
    }

    /**
     * 切换菜单
     *
     * @param target
     */
    private void switchMenu(View target) {
        for (int i = 0; i < mTabMenuView.getChildCount(); i = i + 2) {
            if (target == mTabMenuView.getChildAt(i)) {
                if (mCurrentTabPosition == i) {
                    closeMenu();
                } else {
                    if (mCurrentTabPosition == -1) {
                        mPopupMenuViews.setVisibility(View.VISIBLE);
                        mPopupMenuViews.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.ddm_menu_in));
                        mMaskView.setVisibility(VISIBLE);
                        mMaskView.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.ddm_mask_in));
                        mPopupMenuViews.getChildAt(i / 2).setVisibility(View.VISIBLE);
                    } else {
                        mPopupMenuViews.getChildAt(i / 2).setVisibility(View.VISIBLE);
                    }
                    mCurrentTabPosition = i;
                    ((TextView) mTabMenuView.getChildAt(i)).setTextColor(mMenuTextSelectedColor);
                    setArrowIconEnd((TextView) mTabMenuView.getChildAt(i), mMenuSelectedIcon);
                }
            } else {
                ((TextView) mTabMenuView.getChildAt(i)).setTextColor(mMenuTextUnselectedColor);
                setArrowIconEnd((TextView) mTabMenuView.getChildAt(i), mMenuUnselectedIcon);
                mPopupMenuViews.getChildAt(i / 2).setVisibility(View.GONE);
            }
        }
    }

    private void setArrowIconEnd(TextView view, Drawable arrowIcon) {
        if (view == null) {
            return;
        }
        view.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, arrowIcon, null);
        view.setCompoundDrawablePadding(SizeUtils.dp2px(10));
    }

    /**
     * 获取svg资源图片
     *
     * @param context
     * @param resId
     * @return
     */
    public static Drawable getVectorDrawable(Context context, @DrawableRes int resId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return context.getDrawable(resId);
        }
        return AppCompatResources.getDrawable(context, resId);
    }

    /**
     * 获取Drawable属性（兼容VectorDrawable）
     *
     * @param context
     * @param typedArray
     * @param index
     * @return
     */
    public static Drawable getDrawableAttrRes(Context context, TypedArray typedArray, @StyleableRes int index) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return typedArray.getDrawable(index);
        } else {
            int resourceId = typedArray.getResourceId(index, -1);
            if (resourceId != -1) {
                return AppCompatResources.getDrawable(context, resourceId);
            }
        }
        return null;
    }

    class DropDownAdapter extends BaseAdapter{



        public void setSelection(int i){
            mSelectPosition = i;
        }
        DropDownAdapter(){

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if(convertView  == null){
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.adapter_drop_down_list_item,parent,false);
                viewHolder = new ViewHolder();
                viewHolder.tv = convertView.findViewById(R.id.text);
                convertView.setTag(viewHolder);
            }else{
                viewHolder =((ViewHolder)convertView.getTag());
            }

            viewHolder.tv.setText(mData.get(position));
            if(mSelectPosition != -1){
                if (mSelectPosition == position) {
                    viewHolder.tv.setSelected(true);
                    viewHolder.tv.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, getContext().getResources().getDrawable(R.mipmap.caiji), null);
                } else {
                    viewHolder.tv.setSelected(false);
                    viewHolder.tv.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, null);
                }
            }
            return convertView;
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public Object getItem(int i) {
            return mData.get(i);
        }
    }

    class ViewHolder{
            TextView tv;

    }

}