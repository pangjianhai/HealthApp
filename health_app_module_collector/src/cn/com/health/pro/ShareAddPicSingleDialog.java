package cn.com.health.pro;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.net.Uri;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import cn.com.health.pro.abstracts.ParentShareInfoActivity.AfterDelPicListener;

/**
 * 
 * @todo 删除图片
 * @author pang
 *
 */
public class ShareAddPicSingleDialog extends AlertDialog {

	private static Context mContext;
	private static ShareAddPicSingleDialog mDialog;

	public ShareAddPicSingleDialog(Context context) {
		super(context);
		mContext = context;
	}

	public ShareAddPicSingleDialog(Context context, int theme) {
		super(context, theme);
		mContext = context;
	}

	public static ShareAddPicSingleDialog show(Activity context, String content) {
		mContext = context;
		return show(context, true, true, null, content, 0, null);
	}

	public static ShareAddPicSingleDialog show(Activity context,
			boolean indeterminate, boolean cancelable,
			OnCancelListener cancelListener, String disk, final int position,
			final AfterDelPicListener l) {
		mContext = context;

		mDialog = new ShareAddPicSingleDialog(context);
		mDialog.show();
		mDialog.setCancelable(cancelable);
		mDialog.setOnCancelListener(cancelListener);
		mDialog.getWindow().getAttributes().gravity = Gravity.CENTER;
		WindowManager.LayoutParams lp = mDialog.getWindow().getAttributes();
		lp.dimAmount = 0.8f;
		// lp.width = 892;//定义宽度
		// lp.height = 652;//定义高度

		WindowManager m = context.getWindowManager();
		Display d = m.getDefaultDisplay(); // 为获取屏幕宽、高

		lp.height = ViewGroup.LayoutParams.WRAP_CONTENT; // 高度设置为屏幕的0.6
		lp.width = ViewGroup.LayoutParams.WRAP_CONTENT; // 宽度设置为屏幕的0.95

		mDialog.getWindow().setAttributes(lp);

		View view = (View) LayoutInflater.from(context).inflate(
				R.layout.share_sentence_addimg_window, null);
		ImageView showImg = (ImageView) view.findViewById(R.id.add_pic_iv);
		File mediaFile = new File(disk);
		Uri uri = Uri.fromFile(mediaFile);
		showImg.setImageURI(uri);

		mDialog.setContentView(view);

		Button cbtn = (Button) view.findViewById(R.id.add_pic_close_btn);
		cbtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mDialog.dismiss();
			}
		});

		Button delbtn = (Button) view.findViewById(R.id.add_pic_cancel_btn);
		delbtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				l.afterDelPic(position);
				mDialog.dismiss();
			}
		});

		return mDialog;
	}

	public void dismiss() {
		mContext = null;
		super.dismiss();
	}
}