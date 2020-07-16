/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.tang.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;

import com.tang.ui.R;


/**
 * 自定义Toolbar
 * todo: 修复title在left和right的margin
 */
public class ToolbarExtend extends Toolbar {

    private final int DEFAULT_SIZE = 24;
    private int drawableSize;
    private final int DEFAULT_GRAVITY = GravityCompat.START | Gravity.CENTER_VERTICAL;
    private int titleGravity;

    public ToolbarExtend(Context context) {
        this(context,null);
    }

    public ToolbarExtend(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ToolbarExtend);
        drawableSize = a.getDimensionPixelSize(R.styleable.ToolbarExtend_drawableSize,DEFAULT_SIZE);
        titleGravity = a.getInt(R.styleable.ToolbarExtend_titleGravity,DEFAULT_GRAVITY);

        a.recycle();

        resizeDrawable();
        changeTitleGravity();
    }
    public int getDrawableSize() {
        return drawableSize;
    }

    public void setDrawableSize(int drawableSize) {
        if (drawableSize == this.drawableSize) {
            return;
        }

        this.drawableSize = drawableSize;
        resizeDrawable();
    }

    protected void resizeDrawable(){

        Drawable buttonDrawable = getNavigationIcon();
        if(buttonDrawable != null){
            ResizeDrawable drawable = new ResizeDrawable(buttonDrawable,drawableSize);
            setNavigationIcon(drawable);
        }

    }

    public int getTitleGravity() {
        return titleGravity;
    }

    public void setTitleGravity(int titleGravity) {
        if (titleGravity == this.titleGravity) {
            return;
        }

        this.titleGravity = Gravity.CENTER_VERTICAL|titleGravity;
        changeTitleGravity();
    }

    protected void changeTitleGravity() {
        for(int i = 0; i < getChildCount(); i++){
            View view = getChildAt(i);
            if(view != null && view instanceof TextView){
                LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                lp.gravity = titleGravity;
                view.setLayoutParams(lp);
            }
        }
    }

    class ResizeDrawable extends LayerDrawable {

        int size;

        public ResizeDrawable(Drawable d, int size){
            super(new Drawable[] { d });
            setSize(size);
        }

        public void setSize(int size){
            this.size = size;
            setBounds(0,0,size,size);
        }

        @Override
        public int getIntrinsicWidth() {
            return size;
        }

        @Override
        public int getIntrinsicHeight() {
            return size;
        }

    }

}
