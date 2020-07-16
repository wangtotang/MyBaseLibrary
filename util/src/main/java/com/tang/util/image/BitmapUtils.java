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
package com.tang.util.image;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;


import com.tang.util.convert.MathUtils;
import com.tang.util.storage.FileUtils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * 位图处理工具
 */
public class BitmapUtils {

    /** 图片处理：裁剪. */
    public static final int CUT_IMG = 0;

    /** 图片处理：缩放. */
    public static final int SCALE_IMG = 1;

    /** 图片处理：不处理. */
    public static final int ORIGINAL_IMG = 2;

    /** 图片最大宽度. */
    public static final int MAX_WIDTH = 4096/2;

    /** 图片最大高度. */
    public static final int MAX_HEIGHT = 4096/2;

    /**
     * 直接获取互联网上的图片.
     *
     * @param url
     *            要下载文件的网络地址
     * @param type
     *            图片的处理类型（剪切或者缩放到指定大小，参考AbConstant类）
     * @param desiredWidth
     *            新图片的宽
     * @param desiredHeight
     *            新图片的高
     * @return Bitmap 新图片
     */
    public static Bitmap getBitmap(String url, int type,
                                   int desiredWidth, int desiredHeight) {
        Bitmap bm = null;
        URLConnection con;
        InputStream is = null;
        try {
            URL imageURL = new URL(url);
            con = imageURL.openConnection();
            con.setDoInput(true);
            con.connect();
            is = con.getInputStream();
            // 获取资源图片
            Bitmap wholeBm = BitmapFactory.decodeStream(is, null, null);
            if (type == CUT_IMG) {
                bm = getCutBitmap(wholeBm, desiredWidth, desiredHeight);
            } else if (type == SCALE_IMG) {
                bm = getScaleBitmap(wholeBm, desiredWidth, desiredHeight);
            } else {
                bm = wholeBm;
            }
        } catch (Exception e) {
            Log.e(""+ImageUtils.class, "" + e.getMessage());
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return bm;
    }

    /**
     * 描述：获取原图.
     *
     * @param file
     *            File对象
     * @return Bitmap 图片
     */
    public static Bitmap getBitmap(File file) {
        Bitmap resizeBmp = null;
        try {
            resizeBmp = BitmapFactory.decodeFile(file.getPath());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resizeBmp;
    }

    /**
     * 描述：缩放图片.压缩
     *
     * @param file
     *            File对象
     * @param desiredWidth
     *            新图片的宽
     * @param desiredHeight
     *            新图片的高
     * @return Bitmap 新图片
     */
    public static Bitmap getScaleBitmap(File file, int desiredWidth, int desiredHeight) {

        Bitmap resizeBmp;
        BitmapFactory.Options opts = new BitmapFactory.Options();
        // 设置为true,decodeFile先不创建内存 只获取一些解码边界信息即图片大小信息
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file.getPath(), opts);

        // 获取图片的原始宽度高度
        int srcWidth = opts.outWidth;
        int srcHeight = opts.outHeight;
        int[] size = resizeToMaxSize(srcWidth, srcHeight, desiredWidth, desiredHeight);
        desiredWidth = size[0];
        desiredHeight = size[1];

        // 缩放的比例
        float scale = getMinScale(srcWidth, srcHeight, desiredWidth, desiredHeight);
        int destWidth = srcWidth;
        int destHeight = srcHeight;
        if (scale != 0) {
            destWidth = (int) (srcWidth * scale);
            destHeight = (int) (srcHeight * scale);
        }

        // 默认为ARGB_8888.
        opts.inPreferredConfig = Bitmap.Config.RGB_565;
        // 以下两个字段需一起使用：
        // 产生的位图将得到像素空间，如果系统gc，那么将被清空。当像素再次被访问，如果Bitmap已经decode，那么将被自动重新解码
        opts.inPurgeable = true;
        // 位图可以共享一个参考输入数据(inputStream、阵列等)
        opts.inInputShareable = true;

        // inSampleSize=2 表示图片宽高都为原来的二分之一，即图片为原来的四分之一
        // 缩放的比例，SDK中建议其值是2的指数值，通过inSampleSize来进行缩放，其值表明缩放的倍数
        if (scale < 0.25) {
            // 缩小到4分之一
            opts.inSampleSize = 2;
        } else if (scale < 0.125) {
            // 缩小到8分之一
            opts.inSampleSize = 4;
        } else {
            opts.inSampleSize = 1;
        }

        // 设置大小
        opts.outWidth = destWidth;
        opts.outHeight = destHeight;

        // 创建内存
        opts.inJustDecodeBounds = false;
        // 使图片不抖动
        opts.inDither = false;

        resizeBmp = BitmapFactory.decodeFile(file.getPath(), opts);
        // 缩小或者放大
        resizeBmp = getScaleBitmap(resizeBmp, scale);
        return resizeBmp;
    }

    /**
     * 描述：缩放图片,不压缩的缩放.
     *
     * @param bitmap
     *            the bitmap
     * @param desiredWidth
     *            新图片的宽
     * @param desiredHeight
     *            新图片的高
     * @return Bitmap 新图片
     */
    public static Bitmap getScaleBitmap(Bitmap bitmap, int desiredWidth, int desiredHeight) {

        if (!checkBitmap(bitmap)) {
            return null;
        }
        Bitmap resizeBmp;

        // 获得图片的宽高
        int srcWidth = bitmap.getWidth();
        int srcHeight = bitmap.getHeight();

        int[] size = resizeToMaxSize(srcWidth, srcHeight, desiredWidth, desiredHeight);
        desiredWidth = size[0];
        desiredHeight = size[1];

        float scale = getMinScale(srcWidth, srcHeight, desiredWidth, desiredHeight);
        resizeBmp = getScaleBitmap(bitmap, scale);
        //超出的裁掉
        if ( resizeBmp != null && (resizeBmp.getWidth() > desiredWidth || resizeBmp.getHeight() > desiredHeight)) {
            resizeBmp  = getCutBitmap(resizeBmp,desiredWidth,desiredHeight);
        }
        return resizeBmp;
    }

    /**
     * 描述：根据等比例缩放图片.
     *
     * @param bitmap
     *            the bitmap
     * @param scale
     *            比例
     * @return Bitmap 新图片
     */
    public static Bitmap getScaleBitmap(Bitmap bitmap, float scale) {

        if (!checkBitmap(bitmap)) {
            return null;
        }

        if (scale == 1) {
            return bitmap;
        }

        Bitmap resizeBmp = null;
        try {
            // 获取Bitmap资源的宽和高
            int bmpW = bitmap.getWidth();
            int bmpH = bitmap.getHeight();

            // 注意这个Matrix是android.graphics底下的那个
            Matrix matrix = new Matrix();
            // 设置缩放系数，分别为原来的0.8和0.8
            matrix.postScale(scale, scale);
            resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bmpW, bmpH, matrix, true);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (resizeBmp != bitmap) {
                bitmap.recycle();
            }
        }
        return resizeBmp;
    }

    /**
     * 描述：裁剪图片.
     *
     * @param file
     *            File对象
     * @param desiredWidth
     *            新图片的宽
     * @param desiredHeight
     *            新图片的高
     * @return Bitmap 新图片
     */
    public static Bitmap getCutBitmap(File file, int desiredWidth, int desiredHeight) {

        Bitmap resizeBmp = null;

        BitmapFactory.Options opts = new BitmapFactory.Options();
        // 设置为true,decodeFile先不创建内存 只获取一些解码边界信息即图片大小信息
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file.getPath(), opts);

        // 获取图片的原始宽度
        int srcWidth = opts.outWidth;
        // 获取图片原始高度
        int srcHeight = opts.outHeight;

        int[] size = resizeToMaxSize(srcWidth, srcHeight, desiredWidth, desiredHeight);
        desiredWidth = size[0];
        desiredHeight = size[1];

        // 缩放的比例
        float scale = getMinScale(srcWidth, srcHeight, desiredWidth, desiredHeight);
        int destWidth = srcWidth;
        int destHeight = srcHeight;
        if (scale != 1) {
            destWidth = (int) (srcWidth * scale);
            destHeight = (int) (srcHeight * scale);
        }

        // 默认为ARGB_8888.
        opts.inPreferredConfig = Bitmap.Config.RGB_565;
        // 以下两个字段需一起使用：
        // 产生的位图将得到像素空间，如果系统gc，那么将被清空。当像素再次被访问，如果Bitmap已经decode，那么将被自动重新解码
        opts.inPurgeable = true;
        // 位图可以共享一个参考输入数据(inputStream、阵列等)
        opts.inInputShareable = true;
        // 缩放的比例，缩放是很难按准备的比例进行缩放的，通过inSampleSize来进行缩放，其值表明缩放的倍数，SDK中建议其值是2的指数值
        if (scale < 0.25) {
            // 缩小到4分之一
            opts.inSampleSize = 2;
        } else if (scale < 0.125) {
            // 缩小
            opts.inSampleSize = 4;
        }else {
            opts.inSampleSize = 1;
        }
        // 设置大小
        opts.outHeight = destHeight;
        opts.outWidth = destWidth;
        // 创建内存
        opts.inJustDecodeBounds = false;
        // 使图片不抖动
        opts.inDither = false;
        Bitmap bitmap = BitmapFactory.decodeFile(file.getPath(), opts);
        if (bitmap != null) {
            resizeBmp = getCutBitmap(bitmap, desiredWidth, desiredHeight);
        }
        return resizeBmp;
    }

    private static float getMinScale(int srcWidth, int srcHeight, int desiredWidth, int desiredHeight) {
        // 缩放的比例
        float scale;
        // 计算缩放比例，宽高的最小比例
        float scaleWidth = (float) desiredWidth / srcWidth;
        float scaleHeight = (float) desiredHeight / srcHeight;
        if (scaleWidth > scaleHeight) {
            scale = scaleWidth;
        } else {
            scale = scaleHeight;
        }

        return scale;
    }

    private static int[] resizeToMaxSize(int srcWidth, int srcHeight, int desiredWidth, int desiredHeight) {
        int[] size = new int[2];
        if(desiredWidth <= 0){
            desiredWidth = srcWidth;
        }
        if(desiredHeight <= 0){
            desiredHeight = srcHeight;
        }
        if (desiredWidth > MAX_WIDTH) {
            // 重新计算大小
            desiredWidth = MAX_WIDTH;
            float scaleWidth = (float) desiredWidth / srcWidth;
            desiredHeight = (int) (desiredHeight * scaleWidth);
        }

        if (desiredHeight > MAX_HEIGHT) {
            // 重新计算大小
            desiredHeight = MAX_HEIGHT;
            float scaleHeight = (float) desiredHeight / srcHeight;
            desiredWidth = (int) (desiredWidth * scaleHeight);
        }
        size[0] = desiredWidth;
        size[1] = desiredHeight;
        return size;
    }

    /**
     * 描述：裁剪图片.
     *
     * @param bitmap
     *            the bitmap
     * @param desiredWidth
     *            新图片的宽
     * @param desiredHeight
     *            新图片的高
     * @return Bitmap 新图片
     */
    public static Bitmap getCutBitmap(Bitmap bitmap, int desiredWidth, int desiredHeight) {

        if (!checkBitmap(bitmap)) {
            return null;
        }

        if (!checkSize(desiredWidth, desiredHeight)) {
            return null;
        }

        Bitmap resizeBmp = null;

        try {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();

            int offsetX = 0;
            int offsetY = 0;

            if (width > desiredWidth) {
                offsetX = (width - desiredWidth) / 2;
            } else {
                desiredWidth = width;
            }

            if (height > desiredHeight) {
                offsetY = (height - desiredHeight) / 2;
            } else {
                desiredHeight = height;
            }

            resizeBmp = Bitmap.createBitmap(bitmap, offsetX, offsetY, desiredWidth,desiredHeight);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (resizeBmp != bitmap) {
                bitmap.recycle();
            }
        }
        return resizeBmp;
    }


    private static boolean checkBitmap(Bitmap bitmap) {
        if (bitmap == null) {
            Log.e("" + ImageUtils.class, "原图Bitmap为空了");
            return false;
        }

        if (bitmap.getWidth() <= 0 || bitmap.getHeight() <= 0) {
            Log.e(""+ImageUtils.class, "原图Bitmap大小为0");
            return false;
        }
        return true;
    }

    private static boolean checkSize(int desiredWidth, int desiredHeight) {
        if (desiredWidth <= 0 || desiredHeight <= 0) {
            Log.e(""+ImageUtils.class, "请求Bitmap的宽高参数必须大于0");
            return false;
        }
        return true;
    }

    /**
     * Drawable转Bitmap.
     *
     * @param drawable
     *            要转化的Drawable
     * @return Bitmap
     */
    public static Bitmap drawable2Bitmap(Drawable drawable) {
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(),
                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }


    /**
     * bitmap转drawable
     *
     * @param res    resources对象
     * @param bitmap bitmap对象
     * @return drawable
     */
    public static Drawable bitmap2Drawable(Resources res, Bitmap bitmap) {
        return bitmap == null ? null : new BitmapDrawable(res, bitmap);
    }

    /**
     * 将Bitmap转换为byte[].
     *
     * @param bitmap
     *            the bitmap
     * @param mCompressFormat
     *            图片格式 Bitmap.CompressFormat.JPEG,CompressFormat.PNG
     * @param needRecycle
     *            是否需要回收
     * @return byte[] 图片的byte[]
     */
    public static byte[] bitmap2Bytes(Bitmap bitmap, Bitmap.CompressFormat mCompressFormat, final boolean needRecycle) {
        byte[] result = null;
        ByteArrayOutputStream output = null;
        try {
            output = new ByteArrayOutputStream();
            bitmap.compress(mCompressFormat, 100, output);
            result = output.toByteArray();
            if (needRecycle) {
                bitmap.recycle();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    /**
     * 获取Bitmap大小.
     *
     * @param bitmap
     *            the bitmap
     * @param mCompressFormat
     *            图片格式 Bitmap.CompressFormat.JPEG,CompressFormat.PNG
     * @return 图片的大小
     */
    public static int getByteCount(Bitmap bitmap, Bitmap.CompressFormat mCompressFormat) {
        int size = 0;
        ByteArrayOutputStream output = null;
        try {
            output = new ByteArrayOutputStream();
            bitmap.compress(mCompressFormat, 100, output);
            byte[] result = output.toByteArray();
            size = result.length;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return size;
    }

    /**
     * 描述：将byte[]转换为Bitmap.
     *
     * @param b
     *            图片格式的byte[]数组
     * @return bitmap 得到的Bitmap
     */
    public static Bitmap bytes2Bitmap(byte[] b) {
        Bitmap bitmap = null;
        try {
            if (b.length != 0) {
                bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * 将ImageView转换为Bitmap.
     *
     * @param view
     *            要转换为bitmap的View
     * @return byte[] 图片的byte[]
     */
    public static Bitmap imageView2Bitmap(ImageView view) {
        Bitmap bitmap = null;
        try {
            bitmap = Bitmap.createBitmap(view.getDrawingCache());
            view.setDrawingCacheEnabled(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * 将View转换为Bitmap.需要最上层布局为LinearLayout
     *
     * @param view
     *            要转换为bitmap的View
     * @return byte[] 图片的byte[]
     */
    public static Bitmap view2Bitmap(View view) {
        Bitmap bitmap = null;
        try {
            if (view != null) {
                view.setDrawingCacheEnabled(true);
                view.measure(
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                view.layout(0, 0, view.getMeasuredWidth(),
                        view.getMeasuredHeight());
                view.buildDrawingCache();
                bitmap = view.getDrawingCache();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * 描述：旋转Bitmap为一定的角度.
     *
     * @param bitmap
     *            the bitmap
     * @param degrees
     *            the degrees
     * @return the bitmap
     */
    public static Bitmap rotateBitmap(Bitmap bitmap, float degrees) {
        Bitmap mBitmap = null;
        try {
            Matrix m = new Matrix();
            m.setRotate(degrees % 360);
            mBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mBitmap;
    }

    /**
     * 描述：旋转Bitmap为一定的角度并四周暗化处理.
     *
     * @param bitmap
     *            the bitmap
     * @param degrees
     *            the degrees
     * @return the bitmap
     */
    public static Bitmap rotateBitmapTranslate(Bitmap bitmap, float degrees) {
        Bitmap mBitmap = null;
        int width;
        int height;
        try {
            Matrix matrix = new Matrix();
            if ((degrees / 90) % 2 != 0) {
                width = bitmap.getWidth();
                height = bitmap.getHeight();
            } else {
                width = bitmap.getHeight();
                height = bitmap.getWidth();
            }
            int cx = width / 2;
            int cy = height / 2;
            matrix.preTranslate(-cx, -cy);
            matrix.postRotate(degrees);
            matrix.postTranslate(cx, cy);
            mBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mBitmap;
    }

    /**
     * 转换图片转换成圆形.
     *
     * @param bitmap
     *            传入Bitmap对象
     * @return the bitmap
     */
    public static Bitmap toRoundBitmap(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float roundPx;
        float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
        if (width <= height) {
            roundPx = width / 2;
            top = 0;
            bottom = width;
            left = 0;
            right = width;
            height = width;
            dst_left = 0;
            dst_top = 0;
            dst_right = width;
            dst_bottom = width;
        } else {
            roundPx = height / 2;
            float clip = (width - height) / 2;
            left = clip;
            right = width - clip;
            top = 0;
            bottom = height;
            width = height;
            dst_left = 0;
            dst_top = 0;
            dst_right = height;
            dst_bottom = height;
        }

        Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect src = new Rect((int) left, (int) top, (int) right,
                (int) bottom);
        final Rect dst = new Rect((int) dst_left, (int) dst_top,
                (int) dst_right, (int) dst_bottom);
        final RectF rectF = new RectF(dst);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, src, dst, paint);
        return output;
    }

    /**
     * 转换图片转换成镜面效果的图片.
     *
     * @param bitmap
     *            传入Bitmap对象
     * @return the bitmap
     */
    public static Bitmap toReflectionBitmap(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }

        try {
            int reflectionGap = 1;
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();

            // This will not scale but will flip on the Y axis
            Matrix matrix = new Matrix();
            matrix.preScale(1, -1);

            // Create a Bitmap with the flip matrix applied to it.
            // We only want the bottom half of the image
            Bitmap reflectionImage = Bitmap.createBitmap(bitmap, 0, height / 2,
                    width, height / 2, matrix, false);

            // Create a new bitmap with same width but taller to fit
            // reflection
            Bitmap bitmapWithReflection = Bitmap.createBitmap(width,
                    (height + height / 2), Bitmap.Config.ARGB_8888);

            // Create a new Canvas with the bitmap that's big enough for
            // the image plus gap plus reflection
            Canvas canvas = new Canvas(bitmapWithReflection);
            // Draw in the original image
            canvas.drawBitmap(bitmap, 0, 0, null);
            // Draw in the gap
            Paint defaultPaint = new Paint();
            canvas.drawRect(0, height, width, height + reflectionGap, defaultPaint);
            // Draw in the reflection
            canvas.drawBitmap(reflectionImage, 0, height + reflectionGap, null);
            // Create a shader that is a linear gradient that covers the
            // reflection
            Paint paint = new Paint();
            LinearGradient shader = new LinearGradient(0, bitmap.getHeight(),
                    0, bitmapWithReflection.getHeight() + reflectionGap,
                    0x70ffffff, 0x00ffffff, Shader.TileMode.CLAMP);
            // Set the paint to use this shader (linear gradient)
            paint.setShader(shader);
            // Set the Transfer mode to be porter duff and destination in
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
            // Draw a rectangle using the paint with our linear gradient
            canvas.drawRect(0, height, width, bitmapWithReflection.getHeight() + reflectionGap, paint);

            bitmap = bitmapWithReflection;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * 释放Bitmap对象.
     *
     * @param bitmap
     *            要释放的Bitmap
     */
    public static void releaseBitmap(Bitmap bitmap) {
        if (bitmap != null) {
            try {
                if (!bitmap.isRecycled()) {
                    Log.d(""+ImageUtils.class, "Bitmap释放" + bitmap.toString());
                    bitmap.recycle();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 释放Bitmap数组.
     *
     * @param bitmaps
     *            要释放的Bitmap数组
     */
    public static void releaseBitmapArray(Bitmap[] bitmaps) {
        if (bitmaps != null) {
            try {
                for (Bitmap bitmap : bitmaps) {
                    if (bitmap != null && !bitmap.isRecycled()) {
                        Log.d(""+ImageUtils.class,
                                "Bitmap释放" + bitmap.toString());
                        bitmap.recycle();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 描述：简单的图像的特征值，用于缩略图找原图比较好.
     *
     * @param bitmap
     *            the bitmap
     * @return the hash code
     */
    public static String getHashCode(Bitmap bitmap) {
        // 第一步，缩小尺寸。
        // 将图片缩小到8x8的尺寸，总共64个像素。这一步的作用是去除图片的细节，
        // 只保留结构、明暗等基本信息，摒弃不同尺寸、比例带来的图片差异。

        Bitmap temp = Bitmap.createScaledBitmap(bitmap, 8, 8, false);

        int width = temp.getWidth();
        int height = temp.getHeight();
        Log.i("th", "将图片缩小到8x8的尺寸:" + width + "*" + height);

        // 第二步，第二步，简化色彩。
        // 将缩小后的图片，转为64级灰度。也就是说，所有像素点总共只有64种颜色。
        int[] pixels = new int[width * height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                pixels[i * height + j] = rgbToGray(temp.getPixel(i, j));
            }
        }

        releaseBitmap(temp);

        // 第三步，计算平均值。
        // 计算所有64个像素的灰度平均值。
        int avgPixel = MathUtils.average(pixels);

        // 第四步，比较像素的灰度。
        // 将每个像素的灰度，与平均值进行比较。大于或等于平均值，记为1；小于平均值，记为0。
        int[] comps = new int[width * height];
        for (int i = 0; i < comps.length; i++) {
            if (pixels[i] >= avgPixel) {
                comps[i] = 1;
            } else {
                comps[i] = 0;
            }
        }

        // 第五步，计算哈希值。
        // 将上一步的比较结果，组合在一起，就构成了一个64位的整数，
        // 这就是这张图片的指纹。
        StringBuffer hashCode = new StringBuffer();
        for (int i = 0; i < comps.length; i += 4) {
            int result = comps[i] * (int) Math.pow(2, 3) + comps[i + 1]
                    * (int) Math.pow(2, 2) + comps[i + 2]
                    * (int) Math.pow(2, 1) + comps[i + 2];
            hashCode.append(MathUtils.binaryToHex(result));
        }
        // 得到指纹以后，就可以对比不同的图片，看看64位中有多少位是不一样的。
        // 在理论上，这等同于计算"汉明距离"（Hamming distance）。
        // 如果不相同的数据位不超过5，就说明两张图片很相似；如果大于10，就说明这是两张不同的图片。
        return hashCode.toString();
    }

    /**
     * 灰度值计算.
     *
     * @param pixels
     *            像素
     * @return int 灰度值
     */
    private static int rgbToGray(int pixels) {
        // int _alpha = (pixels >> 24) & 0xFF;
        int _red = (pixels >> 16) & 0xFF;
        int _green = (pixels >> 8) & 0xFF;
        int _blue = (pixels) & 0xFF;
        return (int) (0.3 * _red + 0.59 * _green + 0.11 * _blue);
    }

    /**
     * 描述：图像的特征值颜色分布 将颜色分4个区，0,1,2,3 区组合共64组，计算每个像素点属于哪个区.
     *
     * @param bitmap
     *            the bitmap
     * @return the color histogram
     */
    public static int[] getColorHistogram(Bitmap bitmap) {

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        // 区颜色分布
        int[] areaColor = new int[64];

        // 获取色彩数组。
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int pixels = bitmap.getPixel(i, j);
                //int alpha = (pixels >> 24) & 0xFF;
                int red = (pixels >> 16) & 0xFF;
                int green = (pixels >> 8) & 0xFF;
                int blue = (pixels) & 0xFF;
                int redArea = 0;
                int greenArea = 0;
                int blueArea = 0;
                // 0-63 64-127 128-191 192-255
                if (red >= 192) {
                    redArea = 3;
                } else if (red >= 128) {
                    redArea = 2;
                } else if (red >= 64) {
                    redArea = 1;
                } else if (red >= 0) {
                    redArea = 0;
                }

                if (green >= 192) {
                    greenArea = 3;
                } else if (green >= 128) {
                    greenArea = 2;
                } else if (green >= 64) {
                    greenArea = 1;
                } else if (green >= 0) {
                    greenArea = 0;
                }

                if (blue >= 192) {
                    blueArea = 3;
                } else if (blue >= 128) {
                    blueArea = 2;
                } else if (blue >= 64) {
                    blueArea = 1;
                } else if (blue >= 0) {
                    blueArea = 0;
                }
                int index = redArea * 16 + greenArea * 4 + blueArea;
                // 加入
                areaColor[index] += 1;
            }
        }
        return areaColor;
    }

    /**
     * 回收垃圾 recycle
     *
     * @throws
     */
    public static void recycleBitmap(Bitmap bitmap) {
        // 先判断是否已经回收
        if (bitmap != null && !bitmap.isRecycled()) {
            // 回收并且置为null
            bitmap.recycle();
        }
        System.gc();
    }

    /**
     * 提取缩略图
     * @param path
     * @param requireWidth
     * @param requireHeight
     * @param loseLessQuality 是否较少地丢失图片质量 如果为true表示按宽度和高度的缩小倍数中的较少者进行采样。
     * @return
     */
    public static Bitmap sample(String path, int requireWidth, int requireHeight, boolean loseLessQuality) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPurgeable = true;
        options.inInputShareable = true;

        /**
         * "options.inJustDecodeBounds = true"这步是为了设置options.inSampleSize
         */
        options.inJustDecodeBounds = true;
        InputStream is;
        try {
            is = new FileInputStream(path);
        } catch (FileNotFoundException e) {
            return null;
        }
        BitmapFactory.decodeStream(is, null, options);
        int widthMinimum = (int) Math.ceil((float) options.outWidth / (float) requireWidth);
        int heightMinimum = (int) Math.ceil((float) options.outHeight / (float) requireHeight);

        if (loseLessQuality) {
            options.inSampleSize = Math.min(widthMinimum, heightMinimum);
        } else {
            options.inSampleSize = Math.max(widthMinimum, heightMinimum);
        }
        FileUtils.closeIO(is);

        /**
         * 这一步做真正的提取缩略图
         */
        options.inJustDecodeBounds = false;
        try {
            is = new FileInputStream(path);
        } catch (FileNotFoundException e) {
            return null;
        }
        Bitmap bitmap = BitmapFactory.decodeStream(is, null, options);
        FileUtils.closeIO(is);

        return bitmap;
    }

    /**
     * 根据指定的图像路径和大小来获取缩略图
     * 此方法有两点好处：
     * 1. 使用较小的内存空间，第一次获取的bitmap实际上为null，只是为了读取宽度和高度，
     * 第二次读取的bitmap是根据比例压缩过的图像，第三次读取的bitmap是所要的缩略图。
     * 2. 缩略图对于原图像来讲没有拉伸，这里使用了2.2版本的新工具ThumbnailUtils，使
     * 用这个工具生成的图像不会被拉伸。
     *
     * @param imagePath 图像的路径
     * @param width     指定输出图像的宽度
     * @param height    指定输出图像的高度
     * @return 生成的缩略图
     */
    public static Bitmap getImageThumbnail(String imagePath, int width, int height) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        // 获取这个图片的宽和高，注意此处的bitmap为null
        BitmapFactory.decodeFile(imagePath, options);

        // 重新读入图片，读取缩放后的bitmap，注意这次要把options.inJustDecodeBounds 设为 false
        options.inJustDecodeBounds = false; // 设为 false
        // 计算缩放比
        int h = options.outHeight;
        int w = options.outWidth;
        int beWidth = w / width;
        int beHeight = h / height;
        int be;
        if (beWidth < beHeight) {
            be = beWidth;
        } else {
            be = beHeight;
        }
        if (be <= 0) {
            be = 1;
        }
        options.inSampleSize = be;
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);
        // 利用ThumbnailUtils来创建缩略图，这里要指定要缩放哪个Bitmap对象
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;
    }


    /**
     * 获取视频的缩略图
     * 先通过ThumbnailUtils来创建一个视频的缩略图，然后再利用ThumbnailUtils来生成指定大小的缩略图。
     * 如果想要的缩略图的宽和高都小于MICRO_KIND，则类型要使用MICRO_KIND作为kind的值，这样会节省内存。
     *
     * @param videoPath 视频的路径
     * @param width     指定输出视频缩略图的宽度
     * @param height    指定输出视频缩略图的高度度
     * @param kind      参照MediaStore.Images.Thumbnails类中的常量MINI_KIND和MICRO_KIND。
     *                  其中，MINI_KIND: 512 x 384，MICRO_KIND: 96 x 96
     * @return 指定大小的视频缩略图
     */
    public static Bitmap getVideoThumbnail(String videoPath, int width, int height, int kind) {
        Bitmap bitmap;
        // 获取视频的缩略图
        bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, kind);
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;
    }

    /**
     * 合并两种Bitmap，合并后会以宽度较小者作为输出尺寸
     * @param background 作为背景
     * @param foreground 作为前景
     * @return
     */
    public static Bitmap mergeBitmap(Bitmap background, Bitmap foreground) {
        if( background == null ||foreground==null) {
            return null;
        }

        int bgWidth = background.getWidth();
        int bgHeight = background.getHeight();
        int fgWidth = foreground.getWidth();
        int fgHeight = foreground.getHeight();
        int newWidth;
        int newHeight;

        //缩放到原始尺寸较小的尺寸
        if(bgWidth>fgWidth){
            //创建所需尺寸居中缩放的位图
            background = ThumbnailUtils.extractThumbnail(background, fgWidth, fgHeight);
            newWidth=fgWidth;
            newHeight=fgHeight;
        }else{
            foreground = ThumbnailUtils.extractThumbnail(foreground, bgWidth, bgHeight);
            newWidth=bgWidth;
            newHeight=bgHeight;
        }

        Bitmap newBitmap = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888);
        Canvas cv = new Canvas(newBitmap);
        //draw bg into
        cv.drawBitmap(background, 0, 0, null);//在 0，0坐标开始画入bg
        //draw fg into
        cv.drawBitmap(foreground, 0, 0, null);//在 0，0坐标开始画入fg ，可以从任意位置画入
        //save all clip
        cv.save();//保存
        //store
        cv.restore();//存储

        return newBitmap;
    }

    /**
     * 保存Bitmap到本地
     *
     * @param dirPath  保存目录
     * @param filename 图片名（包含后缀）
     * @param bitmap   欲保存的图片
     * @param isDelete 是否只保留一张
     */
    public static void saveBitmap(String dirPath, String filename, Bitmap bitmap, boolean isDelete) {
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File file = new File(dirPath, filename);
        // 若存在即删除-默认只保留一张
        if (isDelete) {
            if (file.exists()) {
                file.delete();
            }
        }

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            if (bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)) {
                out.flush();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 将file转为bitmap
     *
     * @param file
     * @param width  指定宽度
     * @param height 指定高度
     * @return
     */
    public static Bitmap getBitmapFromFile(File file, int width, int height) {
        if (null != file && file.exists()) {
            BitmapFactory.Options opts = null;
            if (width > 0 && height > 0) {
                opts = new BitmapFactory.Options();
                opts.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(file.getPath(), opts);
                // 计算图片缩放比例
                final int minSideLength = Math.min(width, height);
                opts.inSampleSize = computeSampleSize(opts, minSideLength,
                        width * height);
                opts.inJustDecodeBounds = false;
                opts.inInputShareable = true;
                opts.inPurgeable = true;
            }
            try {
                return BitmapFactory.decodeFile(file.getPath(), opts);
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 旋转图片一定角度
     * rotateImageView
     *
     * @return Bitmap
     * @throws
     */
    public static Bitmap rotateImageView(int angle, Bitmap bitmap) {
        // 旋转图片 动作
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        // 创建新的图片
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    /**
     * 将bitmap转为file
     *
     * @param bitmap
     * @param path
     * @return
     */
    public static File Bitmap2File(Bitmap bitmap, String path) {
        File file = new File(path);//将要保存图片的路径
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * 计算压缩比
     *
     * @param options
     * @param minSideLength
     * @param maxNumOfPixels
     * @return
     */
    private static int computeSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength,
                maxNumOfPixels);

        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }

        return roundedSize;
    }

    private static int computeInitialSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;

        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
                .sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(Math
                .floor(w / minSideLength), Math.floor(h / minSideLength));

        if (upperBound < lowerBound) {
            // return the larger one when there is no overlapping zone.
            return lowerBound;
        }

        if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }

    private static int calculateSampleSize(BitmapFactory.Options options,int reqWidth,int reqHeight){
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if(height > reqHeight || width > reqWidth){
            final int heightRatio = Math.round((float) height/(float) reqHeight);
            final int widthRatio = Math.round((float) width/(float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    public static Bitmap getBitmap(String path,int reqWidth,int reqHeight){
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path,options);
        options.inSampleSize = calculateSampleSize(options,reqWidth,reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path,options);
    }

    public static Bitmap compressBitmap(String path,int width,int height,int quality){
        Bitmap bitmap = getBitmap(path,width,height);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,quality,baos);
        return bytes2Bitmap(baos.toByteArray());
    }

    public static String bitmap2String(Bitmap bitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);
        return Base64.encodeToString(baos.toByteArray(),Base64.DEFAULT);
    }

}
