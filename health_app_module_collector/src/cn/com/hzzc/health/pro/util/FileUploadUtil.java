package cn.com.hzzc.health.pro.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.view.WindowManager;
import cn.com.hzzc.health.pro.config.HealthApplication;

/**
 * 
 * @author pang
 * @todo 文件上传
 *
 */
public class FileUploadUtil {

	public static final String SUCCESS = "1";
	public static final String FAILURE = "0";

	/**
	 * 
	 * @tags @param actionUrl
	 * @tags @param params
	 * @tags @param files
	 * @tags @return
	 * @date 2015-04-25
	 * @todo 上传文本和文件
	 * @author pang
	 * @throws IOException
	 */
	public static String uploadFileAndStringPair(String actionUrl,
			Map<String, String> params, List<File> files) {
		HttpURLConnection conn = null;
		try {
			String BOUNDARY = java.util.UUID.randomUUID().toString();
			String PREFIX = "--";
			String LINEND = "\r\n";
			String MULTIPART_FORM_DATA = "multipart/form-data";
			String CHARSET = "UTF-8";
			URL uri = new URL(actionUrl);
			conn = (HttpURLConnection) uri.openConnection();
			conn.setReadTimeout(5 * 1000);
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("connection", "keep-alive");
			conn.setRequestProperty("Charset", "UTF-8");
			conn.setRequestProperty("Content-Type", MULTIPART_FORM_DATA
					+ ";boundary=" + BOUNDARY);
			DataOutputStream outStream = new DataOutputStream(
					conn.getOutputStream());
			if (params != null && !params.isEmpty()) {
				StringBuilder textEntity = new StringBuilder("\r\n");
				for (Map.Entry<String, String> entry : params.entrySet()) {//
					textEntity.append("--");
					textEntity.append(BOUNDARY);
					textEntity.append("\r\n");
					textEntity.append("Content-Disposition: form-data; name=\""
							+ entry.getKey() + "\"\r\n\r\n");
					textEntity.append(entry.getValue());
					textEntity.append("\r\n");
				}
				textEntity.append("\r\n");
				outStream.write(textEntity.toString().getBytes());
			}
			if (files != null && files.size() > 0) {
				for (int i = 0; i < files.size(); i++) {
					File f = files.get(i);
					StringBuilder sb1 = new StringBuilder("");
					sb1.append(PREFIX);
					sb1.append(BOUNDARY);
					sb1.append(LINEND);
					sb1.append("Content-Disposition:form-data;name=\"file" + i
							+ "\";filename=\"" + f.getName() + "\"" + LINEND);
					sb1.append("Content-Type:application/octet-stream;charset="
							+ CHARSET + LINEND);
					sb1.append(LINEND);
					outStream.write(sb1.toString().getBytes());
					InputStream is = new FileInputStream(f);
					byte[] buffer = new byte[1024];
					int len = 0;
					while ((len = is.read(buffer)) != -1) {
						outStream.write(buffer, 0, len);
					}
					is.close();
					outStream.write(LINEND.getBytes());
				}
			}

			byte[] end_data = ((PREFIX + BOUNDARY + PREFIX + LINEND))
					.getBytes();
			outStream.write(end_data);
			outStream.flush();
			int res = conn.getResponseCode();
			if (res == 200) {
				return SUCCESS;
			} else {
				return FAILURE;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}
		return SUCCESS;
	}

	/**
	 * 
	 * @tags @param actionUrl
	 * @tags @param params
	 * @tags @param files
	 * @tags @return
	 * @date 2015年5月11日
	 * @todo 请求返回结果
	 * @author pang
	 */
	public static String uploadFileAndStringPairForResult(String actionUrl,
			Map<String, String> params, List<File> files) {
		StringBuffer sb = new StringBuffer("");
		HttpURLConnection conn = null;
		try {
			String BOUNDARY = java.util.UUID.randomUUID().toString();
			String PREFIX = "--";
			String LINEND = "\r\n";
			String MULTIPART_FORM_DATA = "multipart/form-data";
			String CHARSET = "UTF-8";
			URL uri = new URL(actionUrl);
			conn = (HttpURLConnection) uri.openConnection();
			conn.setReadTimeout(5 * 1000);
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("connection", "keep-alive");
			conn.setRequestProperty("Charset", "UTF-8");
			conn.setRequestProperty("Content-Type", MULTIPART_FORM_DATA
					+ ";boundary=" + BOUNDARY);
			DataOutputStream outStream = new DataOutputStream(
					conn.getOutputStream());
			if (params != null && !params.isEmpty()) {
				StringBuilder textEntity = new StringBuilder("\r\n");
				for (Map.Entry<String, String> entry : params.entrySet()) {//
					textEntity.append("--");
					textEntity.append(BOUNDARY);
					textEntity.append("\r\n");
					textEntity.append("Content-Disposition: form-data; name=\""
							+ entry.getKey() + "\"\r\n\r\n");
					textEntity.append(entry.getValue());
					textEntity.append("\r\n");
				}
				textEntity.append("\r\n");
				outStream.write(textEntity.toString().getBytes());
			}
			if (files != null && files.size() > 0) {
				for (int i = 0; i < files.size(); i++) {
					File f = files.get(i);
					StringBuilder sb1 = new StringBuilder("");
					sb1.append(PREFIX);
					sb1.append(BOUNDARY);
					sb1.append(LINEND);
					sb1.append("Content-Disposition:form-data;name=\"file" + i
							+ "\";filename=\"" + f.getName() + "\"" + LINEND);
					sb1.append("Content-Type:application/octet-stream;charset="
							+ CHARSET + LINEND);
					sb1.append(LINEND);
					outStream.write(sb1.toString().getBytes());
					InputStream is = new FileInputStream(f);
					byte[] buffer = new byte[1024];
					int len = 0;
					while ((len = is.read(buffer)) != -1) {
						outStream.write(buffer, 0, len);
					}
					is.close();
					outStream.write(LINEND.getBytes());
				}
			}

			byte[] end_data = ((PREFIX + BOUNDARY + PREFIX + LINEND))
					.getBytes();
			outStream.write(end_data);
			outStream.flush();
			int res = conn.getResponseCode();
			if (res == 200) {
				InputStream is = conn.getInputStream();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is));
				String line;
				while ((line = reader.readLine()) != null) {
					sb.append(line);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}
		return sb.toString();
	}

	public static String uploadFileAndStringPairCompressed(String actionUrl,
			Map<String, String> params, List<File> files) {
		HttpURLConnection conn = null;
		try {
			String BOUNDARY = java.util.UUID.randomUUID().toString();
			String PREFIX = "--";
			String LINEND = "\r\n";
			String MULTIPART_FORM_DATA = "multipart/form-data";
			String CHARSET = "UTF-8";
			URL uri = new URL(actionUrl);
			conn = (HttpURLConnection) uri.openConnection();
			conn.setReadTimeout(5 * 1000);
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("connection", "keep-alive");
			conn.setRequestProperty("Charset", "UTF-8");
			conn.setRequestProperty("Content-Type", MULTIPART_FORM_DATA
					+ ";boundary=" + BOUNDARY);
			DataOutputStream outStream = new DataOutputStream(
					conn.getOutputStream());
			if (params != null && !params.isEmpty()) {
				StringBuilder textEntity = new StringBuilder("\r\n");
				for (Map.Entry<String, String> entry : params.entrySet()) {// 分别上传文本字段
					textEntity.append("--");
					textEntity.append(BOUNDARY);
					textEntity.append("\r\n");
					textEntity.append("Content-Disposition: form-data; name=\""
							+ entry.getKey() + "\"\r\n\r\n");
					textEntity.append(entry.getValue());
					textEntity.append("\r\n");
				}
				textEntity.append("\r\n");
				outStream.write(textEntity.toString().getBytes());
			}
			if (files != null && files.size() > 0) {
				for (int i = 0; i < files.size(); i++) {
					File f = files.get(i);
					StringBuilder sb1 = new StringBuilder("");
					sb1.append(PREFIX);
					sb1.append(BOUNDARY);
					sb1.append(LINEND);
					sb1.append("Content-Disposition:form-data;name=\"file" + i
							+ "\";filename=\"" + f.getName() + "\"" + LINEND);
					sb1.append("Content-Type:application/octet-stream;charset="
							+ CHARSET + LINEND);
					sb1.append(LINEND);
					outStream.write(sb1.toString().getBytes());
					// *************处理文件开
					String filePath = f.getPath();

					// 尺寸压缩
					Bitmap bm = thumbnailImg(filePath);
					saveMyBitmap("aaaa", bm);
					// 质量压缩
					Bitmap bm2 = compressImage(bm);
					saveMyBitmap("bbb", bm);
					// 上传压缩信息
					ByteArrayOutputStream stream = new ByteArrayOutputStream();
					bm2.compress(Bitmap.CompressFormat.JPEG, 100, stream);
					InputStream is = new ByteArrayInputStream(
							stream.toByteArray());
					// ************处理文件结束
					byte[] buffer = new byte[1024];
					int len = 0;
					while ((len = is.read(buffer)) != -1) {
						outStream.write(buffer, 0, len);
					}
					is.close();
					outStream.write(LINEND.getBytes());
				}
			}

			byte[] end_data = ((PREFIX + BOUNDARY + PREFIX + LINEND))
					.getBytes();
			outStream.write(end_data);
			outStream.flush();
			int res = conn.getResponseCode();
			if (res == 200) {
				return SUCCESS;
			} else {
				return FAILURE;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}
		return SUCCESS;
	}

	/**
	 * 
	 * @tags @param pathName
	 * @tags @return
	 * @date 2015年5月6日
	 * @todo 尺寸压缩
	 * @author pang
	 */
	public static Bitmap thumbnailImg(String pathName) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inPurgeable = true;
		// 将inJustDecodeBounds设置为true确定压缩比例，不会分配内存空间
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(pathName, options);
		// 根据需要压缩的尺寸和真是的尺寸确定压缩的比例
		options.inSampleSize = calculateInSampleSize(options, 480, 800);
		options.inJustDecodeBounds = false;
		Bitmap bm = BitmapFactory.decodeFile(pathName, options);
		return bm;
	}

	/**
	 * 
	 * @tags @param options
	 * @tags @param reqWidth
	 * @tags @param reqHeight
	 * @tags @return
	 * @date 2015年5月6日
	 * @todo 根据展示的尺寸确定压缩的比例
	 * @author pang
	 */
	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// 目标展示尺寸
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			// 确定高度、宽度比
			int heightRatio = Math.round((float) height / (float) reqHeight);
			int widthRatio = Math.round((float) width / (float) reqWidth);
			// 确定最后 的比例
			inSampleSize = heightRatio < widthRatio ? widthRatio : heightRatio;
		}

		return inSampleSize;
	}

	/**
	 * 
	 * @tags @param image
	 * @tags @return
	 * @date 2015年5月6日
	 * @todo 质量压缩
	 * @author pang
	 */
	public static Bitmap compressImage(Bitmap image) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		// 进行压缩
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		int options = 100;
		// 确定压缩后的大小
		while (baos.toByteArray().length / 1024 > 100) {
			// 清空
			baos.reset();
			// 再次压缩
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);
			// 确定比例
			options -= 10;
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);
		return bitmap;
	}

	public static void saveMyBitmap(String bitName, Bitmap mBitmap)
			throws IOException {
		System.out.println("00000000000000000000000000000");
		File f = new File("/sdcard/" + bitName + ".png");
		f.createNewFile();
		FileOutputStream fOut = null;
		try {
			fOut = new FileOutputStream(f);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
		try {
			fOut.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			fOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
