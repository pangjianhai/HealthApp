package cn.com.health.weather.db;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * 
 * @author pang
 * @todo 数据库管理
 *
 */
public class AssetsDatabaseManager {
	private static String tag = "Health_Module_Weather_AssetsDatabase"; // 日志
	/**
	 * 数据库文件存放位置
	 */
	private static String databasePath = "/data/data/cn.com.health.weather.activity/database";

	/**
	 * 路径-数据库文件映射对应关系
	 */
	private Map<String, SQLiteDatabase> databasesMap = new HashMap<String, SQLiteDatabase>();

	/**
	 * 上下文环境
	 */
	private Context context = null;

	/**
	 * 单例模式
	 */
	private static AssetsDatabaseManager mInstance = null;

	/**
	 * 
	 * @param context
	 * @user:pang
	 * @data:2015年6月1日
	 * @todo:初始化 AssetsDatabaseManager
	 * @return:void
	 */
	public static void initManager(Context context) {
		if (mInstance == null) {
			mInstance = new AssetsDatabaseManager(context);
		}
	}

	/**
	 * 
	 * @return
	 * @user:pang
	 * @data:2015年6月1日
	 * @todo:获取AssetsDatabaseManager object
	 * @return:AssetsDatabaseManager
	 */
	public static AssetsDatabaseManager getManager() {
		return mInstance;
	}

	private AssetsDatabaseManager(Context context) {
		this.context = context;
	}

	/**
	 * 
	 * @param dbfile
	 *            文件的名字（data.db）
	 * @return
	 * @user:pang
	 * @data:2015年6月1日
	 * @todo:根据数据库文件生成SQLiteDatabase对象，如果对象已经存在，则从MAP中直接返回
	 * @return:SQLiteDatabase
	 */
	public SQLiteDatabase getDatabase(String dbfile) {
		if (databasesMap.get(dbfile) != null) {
			Log.i(tag, String.format("Return a database copy of %s", dbfile));
			return (SQLiteDatabase) databasesMap.get(dbfile);
		}
		if (context == null)
			return null;

		Log.i(tag, String.format("Create database %s", dbfile));
		String spath = getDatabaseFilepath();
		String sfile = getDatabaseFile(dbfile);

		File file = new File(sfile);
		SharedPreferences dbs = context.getSharedPreferences(
				AssetsDatabaseManager.class.toString(), 0);
		boolean flag = dbs.getBoolean(dbfile, false); // Get Database file flag,
														// if true means this
														// database file was
														// copied and valid
		if (!flag || !file.exists()) {
			/**
			 * 把项目中的DB文件写到手机的制定"文件夹"下面去
			 */
			file = new File(spath);
			/**
			 * 文件夹不存在并且创建文件夹失败报错
			 */
			if (!file.exists() && !file.mkdirs()) {
				Log.i(tag, "Create \"" + spath + "\" fail!");
				return null;
			}
			/**
			 * 将APP的data.db复制到制定手机文件夹下面出问题，报错
			 */
			if (!copyAssetsToFilesystem(dbfile, sfile)) {
				Log.i(tag, String.format("Copy %s to %s fail!", dbfile, sfile));
				return null;
			}

			dbs.edit().putBoolean(dbfile, true).commit();
		}

		/**
		 * 根据数据库文件获取SQLiteDatabase
		 */
		SQLiteDatabase db = SQLiteDatabase.openDatabase(sfile, null,
				SQLiteDatabase.NO_LOCALIZED_COLLATORS);
		if (db != null) {
			databasesMap.put(dbfile, db);
		}
		return db;
	}

	private String getDatabaseFilepath() {
		String path = String.format(databasePath,
				context.getApplicationInfo().packageName);
		return path;
	}

	private String getDatabaseFile(String dbfile) {
		return getDatabaseFilepath() + "/" + dbfile;
	}

	private boolean copyAssetsToFilesystem(String assetsSrc, String des) {
		Log.i(tag, "Copy " + assetsSrc + " to " + des);
		InputStream istream = null;
		OutputStream ostream = null;
		try {
			AssetManager am = context.getAssets();
			istream = am.open(assetsSrc);
			ostream = new FileOutputStream(des);
			byte[] buffer = new byte[1024];
			int length;
			while ((length = istream.read(buffer)) > 0) {
				ostream.write(buffer, 0, length);
			}
			istream.close();
			ostream.close();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				if (istream != null)
					istream.close();
				if (ostream != null)
					ostream.close();
			} catch (Exception ee) {
				ee.printStackTrace();
			}
			return false;
		}
		return true;
	}

	/**
	 * 
	 * @param dbfile
	 * @return
	 * @user:pang
	 * @data:2015年6月1日
	 * @todo: 关闭assets下面的database文件
	 * @return:boolean
	 */
	public boolean closeDatabase(String dbfile) {
		if (databasesMap.get(dbfile) != null) {
			SQLiteDatabase db = (SQLiteDatabase) databasesMap.get(dbfile);
			db.close();
			databasesMap.remove(dbfile);
			return true;
		}
		return false;
	}

	/**
	 * 
	 * 
	 * @user:pang
	 * @data:2015年6月1日
	 * @todo:关闭数据库
	 * @return:void
	 */
	static public void closeAllDatabase() {
		Log.i(tag, "closeAllDatabase");
		if (mInstance != null) {
			for (int i = 0; i < mInstance.databasesMap.size(); ++i) {
				if (mInstance.databasesMap.get(i) != null) {
					mInstance.databasesMap.get(i).close();
				}
			}
			mInstance.databasesMap.clear();
		}
	}
}
