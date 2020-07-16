package com.tang.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.core.view.ViewCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

/**
 * support non-direct descendant scrolling views
 */
public class SwipeRefreshLayoutExtend extends SwipeRefreshLayout {
    private View mScrollUpChild;
    public SwipeRefreshLayoutExtend(Context context) {
        super(context);
    }


    public SwipeRefreshLayoutExtend(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean canChildScrollUp() {
        if (mScrollUpChild != null) {
            return ViewCompat.canScrollVertically(mScrollUpChild, -1);
        }
        return super.canChildScrollUp();
    }

    public void setScrollUpChild(View view) {
        mScrollUpChild = view;
    }
}
