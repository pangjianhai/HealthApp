package cn.com.hzzc.health.pro.config;

import java.io.File;

import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.Handler;
import android.os.Vibrator;
import cn.com.hzzc.health.pro.R;
import cn.com.hzzc.health.pro.SystemConst;
import cn.com.hzzc.health.pro.listener.MyLocationListener;
import cn.com.hzzc.health.pro.model.ShareInOrderEntity;
import cn.com.hzzc.health.pro.persist.SharedPreInto;
import cn.jpush.android.api.JPushInterface;

import com.baidu.location.GeofenceClient;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

/**
 * 
 * @author pang
 * @todo 全局配置
 *
 */
public class HealthApplication extends Application {

	private static final String TAG = "HealthApplication";

	/**
	 * 百度地图相关
	 */
	public LocationClient mLocationClient;
	public GeofenceClient mGeofenceClient;
	public MyLocationListener mMyLocationListener;
	public Vibrator mVibrator;

	/**
	 * 当前上下文环境
	 */
	private static Context context;

	/**
	 * 当前系统登陆用户
	 */
	private static String userId;

	@Override
	public void onCreate() {
		super.onCreate();
		context = getApplicationContext();
		/**
		 * 初始化配置
		 */
		File p = Environment
				.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
		String parent = p.getParent();
		/**
		 * 手机图片缓存路径
		 */
		File cacheDir = new File(parent + "/"
				+ SystemConst.mobile_local_dir_for_pic);
		if (!cacheDir.exists()) {
			cacheDir.mkdirs();
		}

		/**
		 * app下载存放路径
		 */
		File downDir = new File(parent + "/"
				+ SystemConst.mobile_local_dir_for_download_app);
		if (!downDir.exists()) {
			downDir.mkdirs();
		}
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				getContext())
				.memoryCacheExtraOptions(480, 800)
				// 本地缓存的详细信息(缓存的最大长宽)
				.diskCacheExtraOptions(480, 800, null)
				// default 线程池内加载的数量
				.threadPoolSize(3)
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.tasksProcessingOrder(QueueProcessingType.FIFO)
				.denyCacheImageMultipleSizesInMemory()
				// 可以通过自己的内存缓存实现
				.memoryCacheSize(2 * 1024 * 1024)
				// 内存缓存的最大值
				.memoryCacheSizePercentage(13)
				// 50 Mb sd卡(本地)缓存的最大值
				// default为使用HASHCODE对UIL进行加密命名， 还可以用MD5(new
				// Md5FileNameGenerator())加密
				.diskCacheFileNameGenerator(new HashCodeFileNameGenerator())
				.diskCache(new UnlimitedDiscCache(cacheDir))
				.diskCacheSize(50 * 1024 * 1024)
				.defaultDisplayImageOptions(getDisplayImageOption())
				.defaultDisplayImageOptions(DisplayImageOptions.createSimple())
				.imageDownloader(
						new BaseImageDownloader(this, 5 * 1000, 30 * 1000))
				.writeDebugLogs().build();// 创建
		ImageLoader.getInstance().init(config);

		// 初始化地图
		initMap();
		initLocation();
		startMap();
		initJPush();
		initTable();
	}

	public void initJPush() {
		JPushInterface.setDebugMode(true); // 设置开启日志,发布时请关闭日志
		JPushInterface.init(this); // 初始化 JPush
	}

	/**
	 * 
	 * @user:pang
	 * @data:2015年6月12日
	 * @todo:初始化地图的东西
	 * @return:void
	 */
	private void initMap() {
		mLocationClient = new LocationClient(this.getApplicationContext());
		mMyLocationListener = new MyLocationListener();
		mLocationClient.registerLocationListener(mMyLocationListener);
		mGeofenceClient = new GeofenceClient(getApplicationContext());
		mVibrator = (Vibrator) getApplicationContext().getSystemService(
				Service.VIBRATOR_SERVICE);

	}

	/**
	 * 
	 * 
	 * @user:pang
	 * @data:2015年6月12日
	 * @todo:TODO
	 * @return:void
	 */
	private void initLocation() {
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);// 设置定位模式
		option.setCoorType("gcj02");// 返回的定位结果是百度经纬度，默认值gcj02
		int span = 50000;
		option.setScanSpan(span);// 设置发起定位请求的间隔时间为5000ms
		option.setIsNeedAddress(true);
		mLocationClient.setLocOption(option);
	}

	/**
	 * 
	 * @user:pang
	 * @data:2015年6月12日
	 * @todo:开始地图
	 * @return:void
	 */
	public void startMap() {
		mLocationClient.start();
	}

	/**
	 * 
	 * @tags @return
	 * @date 2015年5月19日
	 * @todo 获取加载图片时候的配置
	 * @author pang
	 */
	public static DisplayImageOptions getDisplayImageOption() {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.resetViewBeforeLoading(false)
				// default 设置图片在加载前是否重置、复位
				.delayBeforeLoading(1000)
				// 下载前的延迟时间
				.cacheInMemory(true)
				// 设置下载的图片是否缓存在内存中
				.cacheOnDisk(true)
				// 设置下载的图片是否缓存在SD卡中
				.imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
				// 设置图片以如何的编码方式显示、
				.imageScaleType(ImageScaleType.IN_SAMPLE_INT)
				.bitmapConfig(Bitmap.Config.RGB_565)
				// default 设置图片的解码类型
				// .displayer(new RoundedBitmapDisplayer(5)) // 设置成圆角图片
				.imageScaleType(ImageScaleType.IN_SAMPLE_INT)
				.considerExifParams(true).handler(new Handler()) // default
				.build();
		return options;
	}

	/**
	 * 
	 * @tags @return
	 * @date 2015年5月5日
	 * @todo 获取当前上下文环境
	 * @author pang
	 */
	public static Context getContext() {
		return context;
	}

	/**
	 * 
	 * @tags @return
	 * @date 2015年5月12日
	 * @todo 获取当前用户ID
	 * @author pang
	 */
	public static String getUserId() {
		if (userId == null || "".equals(userId)) {
			String id = new SharedPreInto(getContext())
					.getSharedFieldValue("id");
			if (id == null || "".equals(id)) {
				try {
					throw new Exception("本地没有保存当前登陆者的信息");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			userId = id;
		}
		return userId;
	}

	/**
	 * 
	 * @tags @param id
	 * @date 2015年5月12日
	 * @todo TODO
	 * @author pang
	 */
	public static void setUserId(String id) {
		userId = id;
	}
	
	/**
	 * @user:pang
	 * @data:2015年8月13日
	 * @todo:初始化相关表格
	 * @return:void
	 */
	public void initTable(){
		try {
			System.out.println("-----------initTable");
			DbUtils.create(this).createTableIfNotExist(ShareInOrderEntity.class);
		} catch (DbException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

}
