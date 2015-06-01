package cn.com.health.pro;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends Activity {

	public static final int TAKE_PHOTO = 1;
	public static final int CROP_PHOTO = 2;
	private Button take;
	private Button choose;
	private ImageView picture;
	private Uri uri;

	@Override
	public void onCreate(Bundle b) {
		super.onCreate(b);
		setContentView(R.layout.activity_layout);

		take = (Button) findViewById(R.id.take_photo);
		choose = (Button) findViewById(R.id.choose_photo);
		picture = (ImageView) findViewById(R.id.picture);

		take.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				File ouputImage = new File(Environment
						.getExternalStorageDirectory(), "output_image.jpg");
				try {
					if (ouputImage.exists()) {
						ouputImage.delete();
					}
					ouputImage.createNewFile();
				} catch (Exception e) {
				}
				uri = Uri.fromFile(ouputImage);
				Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
				intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
				startActivityForResult(intent, TAKE_PHOTO);
			}

		});

		choose.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				File outputImg = new File(Environment
						.getExternalStorageDirectory(), "output_img.jpg");
				try {
					if (outputImg.exists()) {
						outputImg.delete();
					}
					outputImg.createNewFile();
				} catch (Exception e) {
				}
				uri = Uri.fromFile(outputImg);
				Intent intent = new Intent("android.intent.action.GET_CONTENT");
				intent.setType("image/*");
				intent.putExtra("crop", true);
				intent.putExtra("scale", true);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, CROP_PHOTO);
				startActivityForResult(intent, CROP_PHOTO);
			}
		});

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case TAKE_PHOTO:
			if (resultCode == RESULT_OK) {
				Intent intent = new Intent("com.android.camera.action.CROP");
				intent.setDataAndType(uri, "image/*");
				intent.putExtra("scale", true);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
				startActivityForResult(intent, CROP_PHOTO);
			}
			break;
		case CROP_PHOTO:
			try {
				Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver()
						.openInputStream(uri));
				picture.setImageBitmap(bitmap);
			} catch (Exception e) {
			}
			break;
		default:
			break;
		}
	}
}
