package com.sandglass.sandglasslibrary.functionmoudle.adapter.recycler;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import androidx.recyclerview.widget.RecyclerView;

import com.sandglass.sandglasslibrary.utils.logutil.SLFLogUtil;

import java.util.ArrayList;

/**
 * Greated by yangjie
 * describe:RecyclerView item 展开view
 * time:2022/12/15
 */
public class SLFExpandableViewHoldersUtil {
    public static final long ANIMAL_DURATION = 300;
    public static final long ALPHA_DURATION = 100;
    private static ArrayList<String> explanedList; //缓存的数据
    private boolean needExplanedOnlyOne = false;
    @SuppressWarnings("rawtypes")
    private SLFExpandableViewHoldersUtil.KeepOneHolder keepOne;

    private static SLFExpandableViewHoldersUtil holdersUtil;

    /**
     * 获取单例
     */
    public static SLFExpandableViewHoldersUtil getInstance() {
        if (holdersUtil == null) {
            holdersUtil = new SLFExpandableViewHoldersUtil();
        }

        return holdersUtil;
    }

    @SuppressWarnings("java:S2696")
    public SLFExpandableViewHoldersUtil init() {
        explanedList = new ArrayList<>();
        keepOne = new KeepOneHolder<>();
        return this;
    }

    /**
     * 点击第二个会收缩前一个 ，remove object
     */
    public void setNeedExplanedOnlyOne(boolean needExplanedOnlyOne) {
        this.needExplanedOnlyOne = needExplanedOnlyOne;
    }

    public static boolean isExpaned(int index){
        return explanedList.contains(index + "");
    }

    @SuppressWarnings("java:S3398")
    private void addPositionInExpaned(int pos) {
        if (!explanedList.contains(pos + "")) {
            explanedList.add(pos + "");
        }
    }

    @SuppressWarnings("java:S3398")
    private void deletePositionInExpaned(int pos) {
        //remove Object 直接写int，会变成index,造成数组越界
        explanedList.remove(pos + "");
    }

    public void resetExpanedList() {
        explanedList.clear();
    }

    @SuppressWarnings("rawtypes")
    public KeepOneHolder getKeepOneHolder() {
        if(keepOne!=null){
            return keepOne;
        }
        return new KeepOneHolder<>();
    }

    //自定义列表中icon 的旋转的动画
    public void rotateExpandIcon(final View imageView, float from, float to) {
        final ValueAnimator valueAnimator = ValueAnimator.ofFloat(from, to);
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.addUpdateListener(animation -> imageView.setRotation((float) valueAnimator.getAnimatedValue()));
        valueAnimator.start();
    }

    //自定义列表中icon 的旋转的动画
    public void translationView(final View view, float from, float to) {
        final ValueAnimator valueAnimator = ValueAnimator.ofFloat(from, to);
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.addUpdateListener(animation -> view.setTranslationX((float) valueAnimator.getAnimatedValue()));
        valueAnimator.start();
    }

    //参数介绍：1、holder对象 2、展开部分的View，由holder.getExpandView()方法获取 3、animate参数为true，则有动画效果
    @SuppressWarnings("java:S3398")
    private void openHolder(final RecyclerView.ViewHolder holder, final View expandView, final boolean animate) {

        if(expandView == null){
            return;
        }

        if (animate) {
            expandView.setVisibility(View.VISIBLE);
            //改变高度的动画
            final Animator animator = SLFViewHolderAnimator.ofItemViewHeight(holder);

            //扩展的动画，透明度动画开始
            ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(expandView, View.ALPHA, 1);
            alphaAnimator.setDuration(ANIMAL_DURATION + ALPHA_DURATION);
            alphaAnimator.addListener(new SLFViewHolderAnimator.ViewHolderAnimatorListener(holder));

            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playTogether(animator, alphaAnimator);
            animatorSet.start();

        } else { //为false时直接显示
            expandView.setVisibility(View.VISIBLE);
            expandView.setAlpha(1);
        }
    }

    //类似于打开的方法
    @SuppressWarnings("java:S3398")
    private void closeHolder(final RecyclerView.ViewHolder holder, final View expandView, final boolean animate) {

        if(expandView == null){
            return;
        }

        if (animate) {

            expandView.setVisibility(View.GONE);
            final Animator animator = SLFViewHolderAnimator.ofItemViewHeight(holder);
            expandView.setVisibility(View.VISIBLE);
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    expandView.setVisibility(View.GONE);
                    expandView.setAlpha(0);
                }

                @SuppressWarnings("java:S4144")
                @Override
                public void onAnimationCancel(Animator animation) {
                    expandView.setVisibility(View.GONE);
                    expandView.setAlpha(0);
                }
            });
            animator.start();
        } else {
            expandView.setVisibility(View.GONE);
            expandView.setAlpha(0);
        }
    }


    //获取展开部分的View
    public interface Expandable {
        View getExpandView();

        void doCustomAnim(boolean isOpen);
    }

    //-1表示所有item是关闭状态，opend为pos值的表示pos位置的item为展开的状态
    private int opened = -1;

    @SuppressWarnings({"deprecation","java:S119"})
    public class KeepOneHolder<VH extends RecyclerView.ViewHolder & Expandable> {
        int preOpen;

        /**
         * 此方法是在Adapter的onBindViewHolder()方法中调用
         *
         * @param holder holder对象
         * @param pos    下标
         */
        public void bind(VH holder, int pos) {
            if (explanedList.contains(pos + "")) {
                SLFExpandableViewHoldersUtil.getInstance().openHolder(holder, holder.getExpandView(), false);
            } else {
                SLFExpandableViewHoldersUtil.getInstance().closeHolder(holder, holder.getExpandView(), false);
            }
        }

        /**
         * 响应ViewHolder的点击事件
         *
         * @param holder holder对象
         */
        @SuppressWarnings("unchecked")
        public void toggle(VH holder) {
            int position = holder.getPosition();
            if (explanedList.contains(position + "")) {
                opened = -1;
                deletePositionInExpaned(position);

                holder.doCustomAnim(true);
                SLFExpandableViewHoldersUtil.getInstance().closeHolder(holder, holder.getExpandView(), true);
            } else {
                preOpen = opened;
                opened = position;

                addPositionInExpaned(position);
                holder.doCustomAnim(false);
                SLFExpandableViewHoldersUtil.getInstance().openHolder(holder, holder.getExpandView(), true);

                //是否要关闭上一个
                if (needExplanedOnlyOne && preOpen != position) {
                    final VH oldHolder = (VH) ((RecyclerView) holder.itemView.getParent()).findViewHolderForPosition(preOpen);
                    if (oldHolder != null) {
                        SLFLogUtil.sdke("KeepOneHolder", "oldHolder != null");
                        SLFExpandableViewHoldersUtil.getInstance().closeHolder(oldHolder, oldHolder.getExpandView(), true);
                        deletePositionInExpaned(preOpen);
                    }
                }
            }
        }
    }

}