package cn.zx.firstapplication.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import cn.zx.firstapplication.R;

/**
 * Created by apple on 15-4-16.
 */
public class PullDownListView extends RelativeLayout implements AbsListView.OnScrollListener {
    static int MAX_PULL_TOP_HEIGHT;
    static int REFRESHING_TOP_HEIGHT;
    private boolean isTop;
    private boolean isRefreshing;
    private boolean isAnimation;
    RelativeLayout layoutHeader;
    private int mCurrentY = 0;
    boolean pullTag = false;
    AbsListView.OnScrollListener mOnScrollListener;
    OnPullHeightChangeListener mOnPullHeightChangeListener;

    public void setOnPullHeightChangeListener(OnPullHeightChangeListener listener) {
        this.mOnPullHeightChangeListener = listener;
    }

    public void setOnScrollListener(AbsListView.OnScrollListener listener) {
        mOnScrollListener = listener;
    }

    public PullDownListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public boolean isRefreshing() {
        return this.isRefreshing;
    }

    private ListView mListView = new ListView(getContext()) {
        int lastY = 0;

        @Override
        public boolean onTouchEvent(MotionEvent ev) {
            if (isAnimation || isRefreshing) {
                return super.onTouchEvent(ev);
            }
            int currentY = (int) ev.getRawY();
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    // 按下时直接记录Y
                    lastY = (int) ev.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE: {
                    // isToBottom 向下划
                    boolean isToBottom = currentY - lastY >= 0 ? true : false;
                    // step 偏移量绝对值
                    int step = Math.abs(currentY - lastY);
                    lastY = currentY;
                    // 在顶部 && 顶部的距离>=0
                    if (isTop && mListView.getTop() >= 0) {
                        // 向下划 && 顶部距离<=MAX_PULL_TOP_HEIGHT
                        if (isToBottom && mListView.getTop() <= MAX_PULL_TOP_HEIGHT) {
                            ev.setAction(MotionEvent.ACTION_UP);
                            super.onTouchEvent(ev);
                            pullTag = true;
                            if (mListView.getTop() > layoutHeader.getHeight()) {
                                step = step / 2;
                            }
                            if ((mListView.getTop() + step) > MAX_PULL_TOP_HEIGHT) {
                                mCurrentY = MAX_PULL_TOP_HEIGHT;
                                scrollTopTo(mCurrentY);
                            } else {
                                mCurrentY += step;
                                scrollTopTo(mCurrentY);
                            }
                        }
                        // 向上划 && 顶部距离>0
                        else if (!isToBottom && mListView.getTop() > 0) {
                            ev.setAction(MotionEvent.ACTION_UP);
                            super.onTouchEvent(ev);
                            if ((mListView.getTop() - step) < 0) {
                                mCurrentY = 0;
                                scrollTopTo(mCurrentY);
                            } else {
                                mCurrentY -= step;
                                scrollTopTo(mCurrentY);
                            }
                        }
                        // 向上划 && 顶部距离=0
                        else if (!isToBottom && mListView.getTop() == 0) {
                            if (!pullTag) {
                                return super.onTouchEvent(ev);
                            }
                        }
                        return true;
                    }
                    break;
                }
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
                    pullTag = false;
                    // 顶部距离>0
                    if (mListView.getTop() > 0) {
                        // 顶部距离>REFRESHING_TOP_HEIGHT
                        if (mListView.getTop() > REFRESHING_TOP_HEIGHT) {
                            animateTopTo(layoutHeader.getMeasuredHeight());
                            isRefreshing = true;
                            if (null != mOnPullHeightChangeListener) {
                                mOnPullHeightChangeListener.onRefreshing(true);
                            }
                        }
                        // 顶部距离<=REFRESHING_TOP_HEIGHT
                        else {
                            animateTopTo(0);
                        }
                    }
            }
            return super.onTouchEvent(ev);
        }
    };

    public void scrollTopTo(int y) {
        mListView.layout(mListView.getLeft(), y, mListView.getRight(), this.getMeasuredHeight() + y);
        if (null != mOnPullHeightChangeListener) {
            mOnPullHeightChangeListener.onTopHeightChange(layoutHeader.getHeight(), y);
        }
    }

    public void animateTopTo(final int y) {
        ValueAnimator animator = ValueAnimator.ofInt(mListView.getTop(), y);
        animator.setDuration(300);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int frameValue = (Integer) animation.getAnimatedValue();
                mCurrentY = frameValue;
                scrollTopTo(frameValue);
                if (frameValue == y) {
                    isAnimation = false;
                }
            }
        });
        isAnimation = true;
        animator.start();
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        REFRESHING_TOP_HEIGHT = layoutHeader.getMeasuredHeight();
        MAX_PULL_TOP_HEIGHT = this.getMeasuredHeight();
    }

    @Override
    public void onFinishInflate() {
        mListView.setBackgroundColor(0xffffffff);
        mListView.setCacheColorHint(Color.TRANSPARENT);
        mListView.setVerticalScrollBarEnabled(false);
        mListView.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        mListView.setOnScrollListener(this);
        this.addView(mListView);
        layoutHeader = (RelativeLayout) this.findViewById(R.id.layoutHeader);
        super.onFinishInflate();
    }

    public ListView getListView() {
        return this.mListView;
    }

    public void pullUp() {
        isRefreshing = false;
        if (mListView.getTop() > 0) {
            animateTopTo(0);
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (null != mOnScrollListener) {
            mOnScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        }
        if (mListView.getCount() > 0) {
            if (firstVisibleItem == 0) {
                View firstItem = mListView.getChildAt(0);
                if (null != firstItem) {
                    if (firstItem.getTop() == 0) {
                        isTop = true;
                    } else {
                        isTop = false;
                    }
                }
            } else {
                isTop = false;
            }
        } else {
            isTop = true;
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (null != mOnScrollListener) {
            mOnScrollListener.onScrollStateChanged(view, scrollState);
        }
    }

    public interface OnPullHeightChangeListener {
        public void onTopHeightChange(int headerHeight, int pullHeight);

        public void onRefreshing(boolean isTop);
    }
}