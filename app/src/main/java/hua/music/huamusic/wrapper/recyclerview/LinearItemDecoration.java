package hua.music.huamusic.wrapper.recyclerview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import hua.music.huamusic.R;


/**
 * Created by hua on 2017/6/16.
 * {@link LinearLayoutManager}布局管理器的分割线
 */

public class LinearItemDecoration extends RecyclerView.ItemDecoration {

    private Drawable mDivider;

    private final Rect mBounds = new Rect();
    private boolean isContainPadding;

    private int mLeftPadding = 0; //px
    private int mRightPadding = 0; //px


    public LinearItemDecoration(Context context) {
        setDrawable(context.getResources().getDrawable(R.drawable.item_divider));
    }

    /**
     * 设置分割线的图片
     */
    public void setDrawable(@NonNull Drawable drawable) {
        mDivider = drawable;
    }

    /**
     * 是否绘制最后一行
     */
    protected boolean hasLast() {
        return false;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (parent.getLayoutManager() == null) {
            return;
        }
        c.save();
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount - (hasLast() ? 0 : 1); i++) {
            final View child = parent.getChildAt(i);
            parent.getDecoratedBoundsWithMargins(child, mBounds);
            final int left = mBounds.left + getLeftMarginAndPadding(child) + mLeftPadding;
            final int right = parent.getWidth() - getRightMarginAndPadding(child) - mRightPadding;
            final int bottom = mBounds.bottom + Math.round(ViewCompat.getTranslationY(child));
            final int top = bottom - mDivider.getIntrinsicHeight();
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
        c.restore();
    }

    private int getLeftMarginAndPadding(View child) {
        RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) child.getLayoutParams();
        return lp.leftMargin + (isContainPadding ? child.getPaddingLeft() : 0);
    }

    private int getRightMarginAndPadding(View child) {
        RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) child.getLayoutParams();
        return lp.rightMargin + (isContainPadding ? child.getPaddingRight() : 0);
    }

    /**
     * 设置分割线是否包含padding距离。默认是不包含
     *
     * @param isContain true包含，false不包含
     */
    public void setDividerContainPadding(boolean isContain) {
        isContainPadding = isContain;
    }

    public void setLeftPadding(int leftPadding) {
        mLeftPadding = leftPadding;
    }

    public void setRightPadding(int rightPadding) {
        mLeftPadding = rightPadding;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        int position = parent.getChildLayoutPosition(view);
        int totalCount = parent.getAdapter().getItemCount();
        if (position < totalCount - (hasLast() ? 0 : 1)) {
            outRect.set(0, 0, 0, mDivider.getIntrinsicHeight());
        }
    }
}
