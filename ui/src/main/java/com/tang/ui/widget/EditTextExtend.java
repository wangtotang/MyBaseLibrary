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
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.appcompat.widget.AppCompatEditText;

import com.tang.ui.R;
import com.tang.ui.listener.SimpleTextWatcher;


/**
 *  扩展EditText，
 *  1.可设置Drawable大小；
 *  2.右icon可以设置是否有文本显示，点击监听和替换icon；
 *  PS:不要用setTransformationMethod()切换，请使用setInputType()
 */

public class EditTextExtend extends AppCompatEditText implements View.OnTouchListener {

    private final int DEFAULT_SIZE = 24;
    private int drawableSize;
    private boolean hasTextShow;
    private Drawable rightIcon;

    private OnRightIconClickListener listener;

    public EditTextExtend(Context context) {
        this(context, null);
    }

    public EditTextExtend(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.EditTextExtend);
        drawableSize = a.getDimensionPixelSize(R.styleable.EditTextExtend_drawableSize, DEFAULT_SIZE);
        hasTextShow = a.getBoolean(R.styleable.EditTextExtend_hasTextShow, false);

        a.recycle();

        resizeDrawable();

        rightIcon = getCompoundDrawables()[2];

        showRightIcon();
        setOnTouchListener(this);
    }

    protected void showRightIcon() {
        final Drawable[] drawables = getCompoundDrawables();
        if (hasTextShow) {
            if (drawables[2] != null) {
                if (getText().length() <= 0) {
                    super.setCompoundDrawables(drawables[0], drawables[1], null, drawables[3]);
                }
                addTextChangedListener(new SimpleTextWatcher() {
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    setVisible(s.length()>0,drawables);
                    }
                });
                setOnFocusChangeListener(new OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        setVisible(hasFocus&&!TextUtils.isEmpty(getText()),drawables);
                    }
                });
            }
        } else {
            addTextChangedListener(null);
            EditTextExtend.super.setCompoundDrawables(drawables[0], drawables[1], rightIcon, drawables[3]);
        }
    }

    private void setVisible(boolean visible, Drawable[] drawables) {
        if (visible) {
            super.setCompoundDrawables(drawables[0], drawables[1], rightIcon, drawables[3]);
        } else {
            super.setCompoundDrawables(drawables[0], drawables[1], null, drawables[3]);
        }
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

    public boolean isHasTextShow() {
        return hasTextShow;
    }

    public void setHasTextShow(boolean hasTextShow) {
        this.hasTextShow = hasTextShow;
        showRightIcon();
    }

    public void setOnRightIconClickListener(OnRightIconClickListener listener) {
        this.listener = listener;
    }

    protected void resizeDrawable() {
        Drawable[] compoundDrawables = getCompoundDrawables();
        for (Drawable compoundDrawable : compoundDrawables) {
            if (compoundDrawable != null) {
                compoundDrawable.setBounds(0, 0, drawableSize, drawableSize);
            }
        }
        if (rightIcon != null) {
            rightIcon.setBounds(0, 0, drawableSize, drawableSize);
        }
        super.setCompoundDrawables(compoundDrawables[0], compoundDrawables[1], compoundDrawables[2], compoundDrawables[3]);

    }

    public void setRightIcon(Drawable drawable) {
        rightIcon = drawable;
        if (rightIcon != null) {
            rightIcon.setBounds(0, 0, drawableSize, drawableSize);
        }
    }

    @Override
    public void setCompoundDrawables(Drawable left, Drawable top, Drawable right, Drawable bottom) {
        Drawable[] compoundDrawables = new Drawable[]{left, top, right, bottom};
        for (Drawable compoundDrawable : compoundDrawables) {
            if (compoundDrawable != null) {
                compoundDrawable.setBounds(0, 0, drawableSize, drawableSize);
            }
        }

        rightIcon = compoundDrawables[2];

        super.setCompoundDrawables(compoundDrawables[0], compoundDrawables[1], rightIcon, compoundDrawables[3]);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        final int x = (int) motionEvent.getX();
        Drawable drawable = getCompoundDrawables()[2];
        if (drawable != null && drawable.isVisible() && x > getWidth() - getPaddingRight() - drawableSize) {
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                if (listener != null) {
                    listener.onClick();
                    setSelection(getText().length());
                }
            }
            return true;
        }
        return false;
    }

    public interface OnRightIconClickListener {
        void onClick();
    }

}
