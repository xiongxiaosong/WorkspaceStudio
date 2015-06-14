package com.xxs.sdk.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import com.xxs.sdk.app.AppContext;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

/**
 * Bitmap与DrawAble与byte[]与InputStream之间的转换工具类
 * @author xiongxs
 *
 */
public class Bitmaputil {

	/** 将byte[]转换成InputStream*/
	public static InputStream Byte2InputStream(byte[] b) {
		ByteArrayInputStream bais = new ByteArrayInputStream(b);
		return bais;
	}
	/** 将InputStream转换成byte[]*/
	public static byte[] InputStream2Bytes(InputStream is) {
		String str = "";
		byte[] readByte = new byte[1024];
		try {
			while (is.read(readByte, 0, 1024) != -1) {
				str += new String(readByte).trim();
			}
			return str.getBytes();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**将Bitmap转换成InputStream*/ 
	public static InputStream Bitmap2InputStream(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(CompressFormat.JPEG, 100, baos);
		InputStream is = new ByteArrayInputStream(baos.toByteArray());
		return is;
	}

	/**将Bitmap转换成InputStream
	 * 
	 * @param bm
	 * @param quality 百分比 0-100
	 * @return
	 */
	public static InputStream Bitmap2InputStream(Bitmap bm, int quality) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(CompressFormat.PNG, quality, baos);
		InputStream is = new ByteArrayInputStream(baos.toByteArray());
		return is;
	}

	/** 将InputStream转换成Bitmap*/
	public static Bitmap InputStream2Bitmap(InputStream is) {
		return BitmapFactory.decodeStream(is);
	}

	/** Drawable转换成InputStream*/
	public static InputStream Drawable2InputStream(Drawable d) {
		Bitmap bitmap = drawable2Bitmap(d);
		return Bitmap2InputStream(bitmap);
	}

	/** InputStream转换成Drawable*/
	public static Drawable InputStream2Drawable(InputStream is) {
		Bitmap bitmap = InputStream2Bitmap(is);
		return bitmap2Drawable(bitmap);
	}

	/** Drawable转换成byte[]*/
	public static byte[] Drawable2Bytes(Drawable d) {
		Bitmap bitmap = drawable2Bitmap(d);
		return Bitmap2Bytes(bitmap);
	}

	/** byte[]转换成Drawable*/
	public static Drawable Bytes2Drawable(byte[] b) {
		Bitmap bitmap = Bytes2Bitmap(b);
		return bitmap2Drawable(bitmap);
	}

	/** Bitmap转换成byte[]*/
	public static byte[] Bitmap2Bytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(CompressFormat.PNG, 100, baos);
		bm.recycle();
		return baos.toByteArray();
	}

	/** byte[]转换成Bitmap*/
	public static Bitmap Bytes2Bitmap(byte[] b) {
		if (b.length != 0) {
			return BitmapFactory.decodeByteArray(b, 0, b.length);
		}
		return null;
	}

	/**Drawable转换成Bitmap*/ 
	public static Bitmap drawable2Bitmap(Drawable drawable) {
		Bitmap bitmap = Bitmap
				.createBitmap(
						drawable.getIntrinsicWidth(),
						drawable.getIntrinsicHeight(),
						drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
								: Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
				drawable.getIntrinsicHeight());
		drawable.draw(canvas);
		return bitmap;
	}

	/** Bitmap转换成Drawable*/
	public static Drawable bitmap2Drawable(Bitmap bitmap) {
		BitmapDrawable bd = new BitmapDrawable(AppContext.mMainContext.getResources(), bitmap);
//		BitmapDrawable bd = new BitmapDrawable(bitmap);
		bitmap.recycle();
		Drawable d = (Drawable) bd;
		return d;
	}
	/** 将bitmap对象转换为Base64string */
	public static String bitmap2Base64String(Bitmap bitmap) {
		if (bitmap == null) {
			return "";
		}
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(CompressFormat.JPEG, 100, baos);
		bitmap.recycle();
		byte[] appicon = baos.toByteArray();
		return android.util.Base64.encodeToString(appicon, android.util.Base64.DEFAULT);
	}

}