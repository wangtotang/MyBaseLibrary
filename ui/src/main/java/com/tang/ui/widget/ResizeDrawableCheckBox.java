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

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatCheckBox;

import com.tang.ui.R;


/**
 * 可设置 drawable size的CheckBox
 */
public class ResizeDrawableCheckBox extends AppCompatCheckBox {

    private final int DEFAULT_SIZE = 24;
    private int drawableSize;
    private Drawable checkDrawable;

    public ResizeDrawableCheckBox(Context context) {
        this(context,null);
    }

    public ResizeDrawableCheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ResizeDrawableCheckBox);
        drawableSize = a.getDimensionPixelSize(R.styleable.ResizeDrawableCheckBox_drawableSize,DEFAULT_SIZE);
        checkDrawable = a.getDrawable(R.styleable.ResizeDrawableCheckBox_checkDrawable);

        a.recycle();

        resizeDrawable();
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

        if(checkDrawable != null){
            ResizeDrawable drawable = new ResizeDrawable(checkDrawable,drawableSize);
            setButtonDrawable(drawable);
        }

    }

    @Nullable
    public Drawable getCheckDrawable(){
        return checkDrawable;
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
