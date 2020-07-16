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
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.view.View;

import java.io.File;
import java.io.IOException;


/**
 * 图片处理类.
 */
public class ImageUtils {

	/**
	 * 将View转换为byte[].
	 *
	 * @param view
	 *            要转换为byte[]的View
	 * @param compressFormat
	 *            the compress format
	 * @return byte[] View图片的byte[]
	 */
	public static byte[] view2Bytes(View view, Bitmap.CompressFormat compressFormat) {
		byte[] b = null;
		try {
			Bitmap bitmap = BitmapUtils.view2Bitmap(view);
			b = BitmapUtils.bitmap2Bytes(bitmap, compressFormat, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return b;
	}

	/**
	 * 将View转换为Drawable.需要最上层布局为LinearLayout
	 *
	 * @param view
	 *            要转换为Drawable的View
	 * @return BitmapDrawable Drawable
	 */
	public static Drawable view2Drawable(View view) {
		BitmapDrawable mBitmapDrawable = null;
		try {
			Bitmap bitmap = BitmapUtils.view2Bitmap(view);
			if (bitmap != null) {
				mBitmapDrawable = new BitmapDrawable(bitmap);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mBitmapDrawable;
	}

	/**
	 * drawable转byteArr
	 *
	 * @param drawable drawable对象
	 * @param format   格式
	 * @return 字节数组
	 */
	public static byte[] drawable2Bytes(Drawable drawable, Bitmap.CompressFormat format) {
		return drawable == null ? null : BitmapUtils.bitmap2Bytes(BitmapUtils.drawable2Bitmap(drawable), format,true);
	}

	/**
	 * byteArr转drawable
	 *
	 * @param res   resources对象
	 * @param bytes 字节数组
	 * @return drawable
	 */
	public static Drawable bytes2Drawable(Resources res, byte[] bytes) {
		return res == null ? null : BitmapUtils.bitmap2Drawable(res, BitmapUtils.bytes2Bitmap(bytes));
	}

	/**
	 * 读取图片属性：旋转的角度
	 *
	 * @param path 图片路径，通过file.getPath（）获取
	 * @return degree旋转的角度 -1表示获取失败
	 */

	public static int readPictureDegree(String path) {
		int degree = -1;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_UNDEFINED);
			switch (orientation) {
				case ExifInterface.ORIENTATION_ROTATE_90:
					degree = 90;
					break;
				case ExifInterface.ORIENTATION_ROTATE_180:
					degree = 180;
					break;
				case ExifInterface.ORIENTATION_ROTATE_270:
					degree = 270;
					break;
				default:
					degree = 0;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;

	}

	/**
	 * 描述：获取图片尺寸
	 * 
	 * @param file File对象
	 */
	public static float[] getImageSize(File file) {
		float[] size = new float[2];
		BitmapFactory.Options opts = new BitmapFactory.Options();
		// 设置为true,decodeFile先不创建内存 只获取一些解码边界信息即图片大小信息
		opts.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(file.getPath(), opts);
		// 获取图片的原始宽度高度
		size[0] = opts.outWidth;
		size[1] = opts.outHeight;
		return size;
	}

	/**
	 * 计算"汉明距离"（Hamming distance）。
	 * 如果不相同的数据位不超过5，就说明两张图片很相似；如果大于10，就说明这是两张不同的图片。.
	 *
	 * @param sourceHashCode
	 *            源hashCode
	 * @param hashCode
	 *            与之比较的hashCode
	 * @return the int
	 */
	public static int hammingDistance(String sourceHashCode, String hashCode) {
		int difference = 0;
		int len = sourceHashCode.length();
		for (int i = 0; i < len; i++) {
			if (sourceHashCode.charAt(i) != hashCode.charAt(i)) {
				difference++;
			}
		}
		return difference;
	}

}
