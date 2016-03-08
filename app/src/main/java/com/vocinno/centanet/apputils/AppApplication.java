package com.vocinno.centanet.apputils;

import java.io.File;

import com.baidu.mapapi.SDKInitializer;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.vocinno.utils.MethodsFile;

import android.app.Application;

public class AppApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		initImageLoader();
		// 百度地图初始化
		SDKInitializer.initialize(this.getApplicationContext());
	}

	public void initImageLoader() {
		String strPath = MethodsFile.getAutoFileDirectory() + "imgCache/";
		MethodsFile.mkdirs(strPath);
		File cacheDir = new File(strPath); // 缓存文件夹路径
		// 创建配置ImageLoader(所有的选项都是可选的,只使用那些你真的想定制)，这个可以设定在APPLACATION里面，设置为全局的配置参数
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				getApplicationContext())
				// max width, max height，即保存的每个缓存文件的最大长宽
				.memoryCacheExtraOptions(480, 800)
				// 线程池内加载的数量
				.threadPoolSize(3)
				// 线程优先级
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.diskCache(new UnlimitedDiskCache(cacheDir))// 自定义缓存路径
				.writeDebugLogs() // Remove for release app
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.build();
		MethodsFile.mImageLoader = ImageLoader.getInstance();
		MethodsFile.mImageLoader.init(config);
	}
}
