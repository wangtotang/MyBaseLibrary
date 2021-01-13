/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.tang.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * RecyclerView 线性分割线
 */
public class RecyclerViewLinearDivider extends RecyclerView.ItemDecoration {

    private static final int[] ATTRS = new int[]{
            android.R.attr.listDivider
    };
    private int mOrientation = RecyclerView.VERTICAL;
    private Drawable divider;
    private int weight = 0;

    public RecyclerViewLinearDivider(Context context, int orientation) {
        mOrientation = orientation;
        TypedArray typedArray = context.obtainStyledAttributes(ATTRS);
        divider = typedArray.getDrawable(0);
        typedArray.recycle();
    }

    public void setDivider(Drawable divider) {
        this.divider = divider;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    @Override
    public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        if (mOrientation == RecyclerView.HORIZONTAL) {
            drawHorizontal(c, parent);
        } else {
            drawVertical(c, parent);
        }
    }

    private void drawVertical(Canvas c, RecyclerView parent) {

        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();

        for (int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) child.getLayoutParams();

//            if (i == 0) {
//                divider.setBounds(left, child.getTop(), right, child.getTop() + getDividerWeight());
//                divider.draw(c);
//            }

            int top = child.getBottom() + layoutParams.bottomMargin;
            int bottom = top + getDividerWeight();

            divider.setBounds(left, top, right, bottom);
            divider.draw(c);

        }

    }

    private void drawHorizontal(Canvas c, RecyclerView parent) {
        int top = parent.getPaddingTop();
        int bottom = parent.getHeight() - parent.getPaddingBottom();

        for (int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) child.getLayoutParams();

//            if (i == 0) {
//                divider.setBounds(child.getLeft(), top, child.getLeft() + getDividerWeight(), bottom);
//                divider.draw(c);
//            }

            int left = child.getRight() + layoutParams.rightMargin;
            int right = left + getDividerWeight();

            divider.setBounds(left, top, right, bottom);
            divider.draw(c);

        }
    }

    private int getDividerWeight() {
        return weight > 0 ? weight : divider.getIntrinsicHeight();
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        if (mOrientation == RecyclerView.HORIZONTAL) {
            outRect.set(0, 0, getDividerWeight(), 0);
        } else {
            outRect.set(0, 0, 0, getDividerWeight());
        }
    }

}
