package com.example.testandroid.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.example.testandroid.R;
import com.example.testandroid.utils.UtilsSize;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LuckyLayout extends FrameLayout {

    private static final int ANIM_DELAYED = 2000;
    private LuckyAdapter mAdapter;

    public LuckyLayout(@NonNull Context context) {
        this(context,null);
    }

    public LuckyLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LuckyLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.layout_lottery,this);
        init();
    }

    private void init() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));
        mAdapter = new LuckyAdapter(this);
        recyclerView.setAdapter(mAdapter);
        recyclerView.addItemDecoration(new LuckItemDecoration(UtilsSize.dpToPx(getContext(),3)));
        RecyclerView.ItemAnimator itemAnimator = recyclerView.getItemAnimator();
        if (itemAnimator instanceof SimpleItemAnimator) {
            ((SimpleItemAnimator) itemAnimator).setSupportsChangeAnimations(false);
        }
        mAdapter.setOnLuckListener(new LuckyAdapter.OnLuckListener() {
            @Override
            public void onLuckStart() {
                mAdapter.startLuck(new Random().nextInt(8));
            }

            @Override
            public void onLuckFinish(int luckIndex) {
                Toast.makeText(getContext(),"抽奖结束 index="+luckIndex,Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setLuckyText(List<String> data){
        if (data == null || data.isEmpty()) {
            return;
        }
        mAdapter.updateTextList(data);
    }



    private static class LuckyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        private static final int ACTION_TYPE = 1;
        private static final int NORMAL_STATUS = 0;
        private static final int NO_TIMES_STATUS = 1;

        private int mSelectedPosition = -1;
        private final SparseIntArray mSparseIntArray;
        private int mLuckyIndex = -1;
        private int mLuckStatus = NORMAL_STATUS;//当前可以抽奖状态
        private int mLastIndex = -1;
        private ValueAnimator mAnimator = null;
        private final WeakReference<LuckyLayout> mLuckyLayoutReference;
        private OnLuckListener mOnLuckListener;
        private final List<String> mTextList;

        private LuckyAdapter(LuckyLayout luckyLayout){
            mLuckyLayoutReference = new WeakReference<>(luckyLayout);
            mSparseIntArray = new SparseIntArray();
            mSparseIntArray.put(0,0);
            mSparseIntArray.put(1,1);
            mSparseIntArray.put(2,2);
            mSparseIntArray.put(3,5);
            mSparseIntArray.put(4,8);
            mSparseIntArray.put(5,7);
            mSparseIntArray.put(6,6);
            mSparseIntArray.put(7,3);
            mTextList = new ArrayList<>();
        }

        public void setOnLuckListener(OnLuckListener onLuckListener) {
            mOnLuckListener = onLuckListener;
        }

        public void updateTextList(List<String> data){
            if(data == null || data.isEmpty()){
                return;
            }
            if (data.size() > 8) {
                data = data.subList(0,8);
            }
            mTextList.clear();
            mTextList.addAll(data);
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            if (viewType == ACTION_TYPE) {
                return new ActionViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_luck_action,parent,false));
            }
            return new LuckyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_lottery_item,parent,false));
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof ActionViewHolder) {
                holder.itemView.setOnClickListener(v->{
                    if (mOnLuckListener != null) {
                        mOnLuckListener.onLuckStart();
                    }
                });
                return;
            }
            int key = mSparseIntArray.indexOfValue(position);
            ((LuckyViewHolder)holder).tvLucyText.setText(mTextList.get(key));
            ((LuckyViewHolder)holder).ivBg.setImageResource(position == mSelectedPosition ? R.drawable.bg_luck_selected : R.drawable.bg_luck);
        }


        public void setNoTimesStatus(){
            mLuckStatus = NO_TIMES_STATUS;
        }

        public void resetStatus(){
            mLuckStatus = NORMAL_STATUS;
        }

        public void startLuck(int luckIndex){
            if (mLuckStatus == NO_TIMES_STATUS) {
                Toast.makeText(mLuckyLayoutReference.get().getContext(),"没有机会了",Toast.LENGTH_SHORT).show();
                return;
            }
            mLuckyIndex = luckIndex;
            startAnim();
        }

        private void startAnim(){
            if (mAnimator != null && mAnimator.isRunning()) {
                return;
            }
            mAnimator = new ValueAnimator();
            mAnimator.setDuration(ANIM_DELAYED);
            mAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
            mAnimator.addUpdateListener(mUpdateListener);
            mAnimator.addListener(mAdapterListener);
            int count = 3 * 8 + mLuckyIndex;
            mAnimator.setIntValues(mLastIndex + 1,count);
            mLastIndex = -1;
            mAnimator.start();
        }

        private final ValueAnimator.AnimatorUpdateListener mUpdateListener = animation -> {
            int index = (int) animation.getAnimatedValue();
            mLastIndex = index % 8;
            updateSelectedPosition(mLastIndex);
        };

        private final AnimatorListenerAdapter mAdapterListener = new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mLastIndex = mLuckyIndex;
                updateSelectedPosition(mLastIndex);
                if (mOnLuckListener != null) {
                    mOnLuckListener.onLuckFinish(mLuckyIndex);
                }
            }
        };

        public void updateSelectedPosition(int position){
            if (mSelectedPosition >= 0) {
                notifyItemChanged(mSelectedPosition);
            }
            mSelectedPosition = mSparseIntArray.get(position);
            notifyItemChanged(mSelectedPosition);
        }

        @Override
        public int getItemCount() {
            return 9;
        }

        @Override
        public int getItemViewType(int position) {
            if (position == 4) {
                return ACTION_TYPE;
            }
            return super.getItemViewType(position);
        }

        public interface OnLuckListener{
            void onLuckStart();
            void onLuckFinish(int luckIndex);
        }
    }

    private static class LuckyViewHolder extends RecyclerView.ViewHolder{

        private final ImageView ivBg;
        private final TextView tvLucyText;

        public LuckyViewHolder(@NonNull View itemView) {
            super(itemView);
            ivBg = itemView.findViewById(R.id.iv_bg);
            tvLucyText = itemView.findViewById(R.id.tv_lucy_text);
        }
    }

    private static class ActionViewHolder extends RecyclerView.ViewHolder{

        public ActionViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

}
