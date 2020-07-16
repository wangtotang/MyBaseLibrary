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
package com.tang.ui.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

/**
 * 毛玻璃效果
 */
@TargetApi(17)
public class BlurFrameLayout extends FrameLayout {

    private BlurCalculate mBlurCalculate;

    public BlurFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mBlurCalculate=new BlurCalculate(this);
    }

    public BlurFrameLayout(Context context) {
        super(context);
    }
    /***
     * radius for linearlayout***/
    public void setRadius(int arg0) {
        if(mBlurCalculate!=null)
            mBlurCalculate.setRadius(arg0);
        invalidate();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mBlurCalculate.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mBlurCalculate.BluronDetachedFromWindow();
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {

        if(mBlurCalculate.isCanvasChanged(canvas)){
            mBlurCalculate.BlurCanvas();
        } else {
            mBlurCalculate.DrawCanvas(canvas);
            super.dispatchDraw(canvas);

        }

    }

    public class BlurCalculate {

        private View mView;
        private Bitmap bitmap;
        private Canvas mCanvas;
        private Rect mRect;
        private Matrix mMatrix;
        private Matrix mDrawMatrix;
        private int realheight,realwidth;
        // rs
        private RenderScript rs;
        private Allocation input;
        private Allocation output;
        private ScriptIntrinsicBlur script;
        private int radius=5;
        private int i=-1;
        private int action=0;
        private static final float BITMAP_RATIO = 0.4f; // 毛玻璃模糊程度，值越小越模糊

        public BlurCalculate(View view) {
            this.mView = view;
            rs = RenderScript.create(view.getContext());
            mCanvas=new Canvas();
            mRect=new Rect();
            mMatrix=new Matrix();
            mDrawMatrix=new Matrix();
        }

        public void setaction(int action) {
            this.action=action;
        }

        public boolean isCanvasChanged(Canvas canvas) {
            return canvas==mCanvas;
        }

        public void onAttachedToWindow() {
            mView.getViewTreeObserver().addOnPreDrawListener(onPreDrawListener);
        }

        public void BluronDetachedFromWindow() {
            mView.getViewTreeObserver().removeOnPreDrawListener(onPreDrawListener);
            if(bitmap!=null)
                bitmap.recycle();
            bitmap=null;
        }

        public void DrawCanvas(Canvas canvas) {
            if(bitmap!=null)
                canvas.drawBitmap(bitmap, mDrawMatrix, null);

        }

        public void BlurCanvas() {
            input = Allocation.createFromBitmap(rs, bitmap, Allocation.MipmapControl.MIPMAP_NONE,
                    Allocation.USAGE_SCRIPT);
            output = Allocation.createTyped(rs, input.getType());
            script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
            script.setRadius(radius);
            script.setInput(input);
            script.forEach(output);
            output.copyTo(bitmap);
        }

        public void setRadius(int arg0) {
            radius=arg0;
        }

        private void getScreenBitmap() {
            mView.getGlobalVisibleRect(mRect);
            realheight=mView.getHeight();
            realwidth=mView.getWidth();
            int w= Math.round(realwidth*BITMAP_RATIO)+4;
            int h= Math.round(realheight*BITMAP_RATIO)+(action==0?-4:4);
            w = w & ~0x03;
            h = h & ~0x03;
            if(w<=0||h<=0)
                return;
            if (bitmap == null || bitmap.getWidth() != w || bitmap.getHeight() != h) {
                bitmap= Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
                mMatrix.setScale(BITMAP_RATIO, BITMAP_RATIO);
                mMatrix.invert(mDrawMatrix);
            }
            float dx = -(Math.min(0, mView.getLeft()) + mRect.left);
            float dy = action==0?(-(Math.min(0, mView.getTop()) + mRect.top)):-(mRect.bottom-((mView.getBottom()-mView.getTop())));
            mCanvas.restoreToCount(1);
            mCanvas.setBitmap(bitmap);
            mCanvas.setMatrix(mMatrix);
            mCanvas.translate(dx, dy);
            mCanvas.save();
            mView.getRootView().draw(mCanvas);

        }

        private final ViewTreeObserver.OnPreDrawListener onPreDrawListener = new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                if (mView.getVisibility() == View.VISIBLE)
                    getScreenBitmap();
                return true;
            }
        };

    }

}
