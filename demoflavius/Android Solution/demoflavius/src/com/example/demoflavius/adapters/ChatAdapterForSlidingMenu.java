package com.example.demoflavius.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.demoflavius.R;
import com.example.demoflavius.activities.SlidingMenuParentActivity;
import com.example.demoflavius.application.MyApplication;
import com.example.demoflavius.data.Friend;
import com.example.demoflavius.util.ImageLoaderManager;
import com.example.demoflavius.util.NotificationService;
import com.example.demoflavius.viewholders.ChatRightSideViewHolder;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

public class ChatAdapterForSlidingMenu  extends ArrayAdapter<Friend> 
{
	public ArrayList<Friend> getDataSource() 
	{
		return dataSource;
	}

	public void setDataSource(ArrayList<Friend> dataSource) 
	{
		this.dataSource = dataSource;
		this.notifyDataSetChanged();
	}

	private Context context;
	private ArrayList<Friend> dataSource;
	private Friend currentItem;
	private int row;
	private DisplayImageOptions options;
	ImageLoader imageLoader;

	public ChatAdapterForSlidingMenu(Context context, int resource, ArrayList<Friend> friends) {
		super(context, resource, friends);
		this.context = context;
		this.row = resource;
		this.dataSource = friends;

		options = ImageLoaderManager.getImageLoaderOptions();
		imageLoader = ImageLoaderManager.getImageLoader();
	}

	@Override
	public int getCount()
	{
		return dataSource.size();
	}

	public void UpdateDateSource(ArrayList<Friend> newDataSource)
	{
		try
		{
			this.dataSource = newDataSource;
			this.notifyDataSetChanged();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			NotificationService.showDebugCrouton("failed at UpdateDateSource!");
		}
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		View view = convertView;
		ChatRightSideViewHolder holder;
		if (view == null) 
		{
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(row, null);

			holder = new ChatRightSideViewHolder();
			view.setTag(holder);
		} 
		else 
		{
			holder = (ChatRightSideViewHolder) view.getTag();
		}
		try
		{
			if ((dataSource == null) || ((position + 1) > dataSource.size()))
				return view;

			currentItem = dataSource.get(position);
			if(currentItem != null)
			{
				holder.tvName = (TextView) view.findViewById(R.id.slidingmenu_chat_tv_name);
				holder.imgView = (ImageView) view.findViewById(R.id.slidingmenu_chat_image);
				holder.progressBar = (ProgressBar) view.findViewById(R.id.slidingmenu_chat_progressbar);
				holder.tvLocation = (TextView) view.findViewById(R.id.slidingmenu_chat_location);
				
				if (holder.tvName != null && null != currentItem.getName() && currentItem.getName().trim().length() > 0) {
					holder.tvName.setText(Html.fromHtml(currentItem.getName()));
				}
				
				if (holder.tvLocation != null && null != currentItem.getLocation() && currentItem.getLocation().trim().length() > 0) {
					holder.tvLocation.setText(Html.fromHtml(currentItem.getLocation()));
				}

				if (holder.imgView != null) {
					if (null != currentItem.getPicture() && currentItem.getPicture().trim().length() > 0) {
						final ProgressBar pbar = holder.progressBar;

						imageLoader.displayImage(currentItem.getPicture(), holder.imgView, options,new ImageLoadingListener() {
							@Override
							public void onLoadingCancelled(String arg0, View arg1) {
								pbar.setVisibility(View.INVISIBLE);
							}

							@Override
							public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
								pbar.setVisibility(View.INVISIBLE);
							}

							@Override
							public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
								pbar.setVisibility(View.INVISIBLE);
							}

							@Override
							public void onLoadingStarted(String arg0, View arg1) {
								pbar.setVisibility(View.VISIBLE);
							}
						});						
					}
					else 
					{
						holder.imgView.setImageResource(R.drawable.ninja);
						holder.progressBar.setVisibility(View.INVISIBLE);
					}
				}
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			NotificationService.showDebugCrouton("failed at getView at ChatAdapter!");
		}
		return view;
	}
}
