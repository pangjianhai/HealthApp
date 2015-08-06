package cn.com.hzzc.health.pro.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.util.Base64;
import android.util.Log;

public class BitmapUtil {
	/**
	 * bitmap转为base64
	 * 
	 * @param bitmap
	 * @return
	 */
	public static String bitmapToBase64(Bitmap bitmap) {

		String result = null;
		ByteArrayOutputStream baos = null;
		try {
			if (bitmap != null) {
				baos = new ByteArrayOutputStream();
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
				baos.flush();
				baos.close();
				byte[] bitmapBytes = baos.toByteArray();
				result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (baos != null) {
					baos.flush();
					baos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * base64转为bitmap
	 * 
	 * @param base64Data
	 * @return
	 */
	public static Bitmap base64ToBitmap(String base64Data) {
		byte[] bytes = Base64.decode(base64Data, Base64.DEFAULT);
		return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
	}

	/** 保存方法 */
	public static void saveBitmap(Bitmap bitmap, String fileName) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
			FileOutputStream fos = new FileOutputStream(fileName);
			fos.write(baos.toByteArray());
			fos.close();
			baos.close();
		} catch (Exception e) {
		}

	}

	/**
	 * 得到 图片旋转 的角度
	 * 
	 * @param filepath
	 * @return
	 */
	public static int getExifOrientation(String filepath) {
		int degree = 0;
		ExifInterface exif = null;
		try {
			exif = new ExifInterface(filepath);
		} catch (IOException ex) {
			Log.e("test", "cannot read exif", ex);
		}
		if (exif != null) {
			int orientation = exif.getAttributeInt(
					ExifInterface.TAG_ORIENTATION, -1);
			if (orientation != -1) {
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
				}
			}
		}
		return degree;
	}

	/**
	 * 把图片旋转多少度
	 */
	public static Bitmap getRotateBitmap(Bitmap res, int angle) {
		if (angle != 0) { // 如果照片出现了 旋转 那么 就更改旋转度数
			Matrix matrix = new Matrix();
			matrix.postRotate(angle);
			res = Bitmap.createBitmap(res, 0, 0, res.getWidth(),
					res.getHeight(), matrix, true);
		}
		return res;
	}

	/**
	 * 让图片正常显示，不要颠倒
	 */
	public static void rightImag(int ra, String path) {
		Bitmap bt = BitmapFactory.decodeFile(path);
		Bitmap bt1 = getRotateBitmap(bt, ra);
		saveBitmap(bt1, path);
	}

	public static void zoopImage(Bitmap bm, String srcPath, String toPath,
			int maxSize, int single) throws Exception {
		int version = Integer.valueOf(android.os.Build.VERSION.SDK);

		if (version > 11) {
			bm = getImagToRam(srcPath, 640, 960);
		} else {
			bm = getImagToRam(srcPath, 240, 320);
		}
		Matrix matrix = new Matrix();
		matrix.postRotate(single);
		if (single != 0) {
			bm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(),
					matrix, true);
		}
		File f = new File(toPath);
		if (f.exists()) {
			f.delete();
		}
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		FileOutputStream fos = new FileOutputStream(f);
		int options = 100;
		int len = baos.toByteArray().length;
		// 如果大于80kb则再次压缩,最多压缩三次
		while (len / 1024 > maxSize && options != 10) {
			// Log.e("zxf", ""+(baos.toByteArray().length / 1024) );
			// 清空baos
			baos.reset();
			// 这里压缩options%，把压缩后的数据存放到baos中
			bm.compress(Bitmap.CompressFormat.JPEG, options, baos);
			len = baos.toByteArray().length;
			options -= 3;
		}
		len = baos.toByteArray().length;
		// Log.e("zxf", "最后一次压缩大小："+(len/ 1024) );
		fos.write(baos.toByteArray());
		fos.close();
		baos.close();
		// Log.e("zxf", srcPath+"7");
		if (!bm.isRecycled()) {
			bm.recycle(); // 回收图片所占的内存
			System.gc(); // 提醒系统及时回收
		}
		bm = null;
		// 旋转角度
	}

	public static Bitmap getImagToRam(String srcPath, int width, int height) {
		// 压缩
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		// 开始读入图片，此时把options.inJustDecodeBounds 设回true了
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);// 此时返回bm为空
		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		// 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
		float hh = width;//
		float ww = height;//
		// 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = 1;// be=1表示不缩放
		if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;// 设置缩放比例
		// 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
		// bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
		newOpts.inDither = false;
		newOpts.inPurgeable = true;
		newOpts.inTempStorage = new byte[12 * 1024];
		newOpts.inPreferredConfig = Bitmap.Config.RGB_565;
		File file = new File(srcPath);
		FileInputStream fs = null;
		/*
		 * try { fs = new FileInputStream(file); Log.e("zxf", srcPath+"5"+fs);
		 * if(fs != null) //bitmap =
		 * BitmapFactory.decodeFileDescriptor(fs.getFD(), null, newOpts);
		 * 
		 * Log.e("zxf", srcPath+"6"+bitmap); } catch (Exception e) {
		 * e.printStackTrace(); }
		 */
		try {
			// bitmap = BitmapFactory.decodeFileDescriptor(fs.getFD(), null,
			// newOpts);
			bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bitmap;
	}
}
