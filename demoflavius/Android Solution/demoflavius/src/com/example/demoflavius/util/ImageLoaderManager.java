package com.example.demoflavius.util;


import com.example.demoflavius.R;
import com.example.demoflavius.application.MyApplication;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class ImageLoaderManager 
{

	private static DisplayImageOptions options = null;
	private static ImageLoader imageLoader = null;
	
	private ImageLoaderManager() {}
	
	private static void initializeImageLoader()
	{
		imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration.createDefault(MyApplication.getContextOfApplication()));	
	}
	
	private static void initializeImageLoaderOptions()
	{
		options = new DisplayImageOptions.Builder().showImageOnFail(R.drawable.ninja).
				  showImageForEmptyUri(R.drawable.ninja).cacheOnDisc(true).cacheInMemory(true).build();
	}
	
	public static ImageLoader getImageLoader()
	{
		if ( imageLoader == null)
		{
			initializeImageLoader();
		}
		return imageLoader;
	}
	
	public static DisplayImageOptions getImageLoaderOptions()
	{
		if( options == null)
		{
			initializeImageLoaderOptions();
		}
		return options;
	}
}
