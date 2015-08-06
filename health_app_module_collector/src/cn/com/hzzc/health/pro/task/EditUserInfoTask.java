package cn.com.hzzc.health.pro.task;

import java.io.File;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;
import cn.com.hzzc.health.pro.EditUserInfoDetail;
import cn.com.hzzc.health.pro.model.FormFile;
import cn.com.hzzc.health.pro.util.FileUploadUtil;
import cn.com.hzzc.health.pro.util.UploadUtils;

/**
 * @author pang
 * @todo 上传图片task
 *
 */
public class EditUserInfoTask extends
		AsyncTask<Map<String, Object>, Void, String> {
	/**
	 * 传过来的默认参数key
	 */
	public static final String text_param = "text_param";
	public static final String file_param = "file_param";
	/**
	 * 上传的地址
	 */
	private String requestURL;

	/**
	 * 可变长的输入参数，与AsyncTask.exucute()对应
	 */
	private ProgressDialog pdialog;
	private Activity context = null;

	public EditUserInfoTask(Activity ctx) {
		this.context = ctx;
		pdialog = ProgressDialog.show(context, "正在保存...", "系统正在处理您的请求");
	}

	public EditUserInfoTask(Activity ctx, String requestURL) {
		this.context = ctx;
		this.requestURL = requestURL;
		pdialog = ProgressDialog.show(context, "正在保存...", "系统正在处理您的请求");
	}

	@Override
	protected void onPreExecute() {
		pdialog.show();
	}

	@Override
	protected void onPostExecute(String result) {
		pdialog.dismiss();
		if (UploadUtils.SUCCESS.equalsIgnoreCase(result)) {
			Toast.makeText(context, "上传成功!", Toast.LENGTH_LONG).show();
			((EditUserInfoDetail) context).btn_back(null);
		} else {
			Toast.makeText(context, "上传失败!", Toast.LENGTH_LONG).show();
		}
	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
	}

	@SuppressWarnings("unchecked")
	@Override
	protected String doInBackground(Map... params) {
		Map<String, String> textParamMap = (Map<String, String>) params[0]
				.get(text_param);
		List<File> files = (List<File>) params[0].get(file_param);
		FormFile[] fs = new FormFile[files.size()];
		for (int i = 0; i < files.size(); i++) {
			File f = files.get(i);
			FormFile formFile = new FormFile(f.getName(), f, f.getName(),
					"type");
			fs[i] = formFile;

		}
		return FileUploadUtil.uploadFileAndStringPairCompressed(requestURL,
				textParamMap, files);
	}

	@Override
	protected void onProgressUpdate(Void... values) {
	}

	public String getRequestURL() {
		return requestURL;
	}

	public void setRequestURL(String requestURL) {
		this.requestURL = requestURL;
	}

}