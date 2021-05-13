package com.example.testandroid.view.banner;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.example.testandroid.R;
import com.example.testandroid.ui.banner.BannerAdapter;
import com.example.testandroid.utils.UtilsSize;
import com.youth.banner.listener.OnPageChangeListener;

public class BannerView extends FrameLayout {

    // 是否允许无限轮播（即首尾直接切换）
    private boolean mIsInfiniteLoop = true;
    private ViewPager2 mViewPager;
    private RecyclerView mRecyclerView;
    private CompositePageTransformer mCompositePageTransformer;
    private BannerAdapter mAdapter;
    private OnPageChangeListener mOnPageChangeListener;
    private BannerOnPageChangeCallback mPageChangeCallback;
    private int mMargin = 25;
    private float mScale = 0.8f;
    private float mRoundScale = 0.1f;

    public BannerView(@NonNull Context context) {
        this(context,null);
    }

    public BannerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public BannerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        View.inflate(context, R.layout.layout_banner,this);
        mViewPager = findViewById(R.id.view_pager);
        mRecyclerView = (RecyclerView) mViewPager.getChildAt(0);
        mCompositePageTransformer = new CompositePageTransformer();
        mPageChangeCallback = new BannerOnPageChangeCallback();
        mViewPager.registerOnPageChangeCallback(mPageChangeCallback);
        mViewPager.setPageTransformer(mCompositePageTransformer);

        mCompositePageTransformer = new CompositePageTransformer();
        mViewPager.setPageTransformer(mCompositePageTransformer);

        updateBannerGalleryEffect();

        RecyclerView.ItemAnimator itemAnimator = mRecyclerView.getItemAnimator();
        if (itemAnimator instanceof SimpleItemAnimator) {
            ((SimpleItemAnimator) itemAnimator).setSupportsChangeAnimations(false);
        }
    }

    private void updateBannerGalleryEffect(){
        int itemWidth = pxToDp(UtilsSize.getScreenWidth(getContext()) * mRoundScale);
        setBannerGalleryEffect(itemWidth);
    }

    private void setBannerGalleryEffect(int itemWidth){
        mCompositePageTransformer.addTransformer(new MarginPageTransformer(dpToPx(mMargin)));
        mCompositePageTransformer.addTransformer(new ScaleInTransformer(mScale));
        setRecyclerViewPadding(itemWidth > 0 ? dpToPx(itemWidth + mMargin) : 0, itemWidth > 0 ? dpToPx(itemWidth + mMargin) : 0);
    }

    private void setRecyclerViewPadding(int leftItemPadding, int rightItemPadding) {
        mRecyclerView.setPadding(leftItemPadding, 0, rightItemPadding, 0);
        mRecyclerView.setClipToPadding(false);
    }


    private int dpToPx(float dp){
        return UtilsSize.dpToPx(getContext(),dp);
    }

    private int pxToDp(float px){
        return UtilsSize.pxToDp(getContext(),px);
    }


    public void setRoundScale(float roundScale) {
        this.mRoundScale = roundScale;
        updateBannerGalleryEffect();
    }

    public void setMargin(int margin) {
        this.mMargin = margin;
        updateBannerGalleryEffect();
    }

    public void setScale(float scale) {
        this.mScale = scale;
        updateBannerGalleryEffect();
    }

    /**
     * 添加viewpager切换事件
     * <p>
     * 在viewpager2中切换事件{@link ViewPager2.OnPageChangeCallback}是一个抽象类，
     * 为了方便使用习惯这里用的是和viewpager一样的{@link ViewPager.OnPageChangeListener}接口
     * </p>
     */
    public BannerView addOnPageChangeListener(OnPageChangeListener pageListener) {
        this.mOnPageChangeListener = pageListener;
        return this;
    }

    /**
     * 返回banner真实总数
     */
    public int getRealCount() {
        return mAdapter.getRealCount();
    }

    private boolean isInfiniteLoop(){
        return mIsInfiniteLoop;
    }

    public void setAdapter(@NonNull BannerAdapter adapter){
        mAdapter = adapter;
        mAdapter.setIsInfiniteLoop(mIsInfiniteLoop);
        mViewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(isInfiniteLoop() ? Integer.MAX_VALUE / 2 : 0);
    }

    /**
     * 跳转到指定位置（最好在设置了数据后在调用，不然没有意义）
     */
    public void setCurrentItem(int position, boolean smoothScroll) {
        mViewPager.setCurrentItem(position, smoothScroll);
    }

    public int getItemCount() {
        if (mAdapter == null) {
            return 0;
        }
        return mAdapter.getItemCount();
    }

    public void destroy(){
        if (mViewPager != null && mPageChangeCallback != null) {
            mViewPager.unregisterOnPageChangeCallback(mPageChangeCallback);
            mPageChangeCallback = null;
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        destroy();
    }

    class BannerOnPageChangeCallback extends ViewPager2.OnPageChangeCallback {
        private boolean isScrolled;

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            int realPosition = mAdapter.getRealPosition(position);
            if (mOnPageChangeListener != null) {
                mOnPageChangeListener.onPageScrolled(realPosition, positionOffset, positionOffsetPixels);
            }
        }

        @Override
        public void onPageSelected(int position) {
            if (isScrolled) {
                int realPosition = mAdapter.getRealPosition(position);
                if (mOnPageChangeListener != null) {
                    mOnPageChangeListener.onPageSelected(realPosition);
                }
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            //手势滑动中,代码执行滑动中
            if (state == ViewPager2.SCROLL_STATE_DRAGGING || state == ViewPager2.SCROLL_STATE_SETTLING) {
                isScrolled = true;
            } else if (state == ViewPager2.SCROLL_STATE_IDLE) {
                //滑动闲置或滑动结束
                isScrolled = false;
            }
            if (mOnPageChangeListener != null) {
                mOnPageChangeListener.onPageScrollStateChanged(state);
            }
        }

    }



}
