package com.example.demoflavius.util;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demoflavius.R;
import com.example.demoflavius.application.MyApplication;

import de.keyboardsurfer.android.widget.crouton.Crouton;

public class NotificationService {
	public static void showDebugCrouton(String content) {
		try
		{
			final Activity currentActivity = MyApplication.getCurrentActivity();
			LayoutInflater inflater = (LayoutInflater)currentActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			final View view = inflater.inflate(R.layout.crouton_custom_warning, null);
			TextView tv_croutonText = (TextView) view.findViewById(R.id.tv_croutonText);
			tv_croutonText.setText(content);
			currentActivity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					final Crouton crouton = Crouton.make(currentActivity, view);
					crouton.show();
				}
			});
		}
		catch(Exception ex)
		{
			showToast("Error at showDebugCrouton!");
			ex.printStackTrace();
		}
	}
	
	public static void showInfoCrouton(String content, String sender) {
		try
		{
			final Activity currentActivity = MyApplication.getCurrentActivity();
			LayoutInflater inflater = (LayoutInflater)currentActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			final View view = inflater.inflate(R.layout.crouton_custom_info, null);
			view.setTag(sender);
			view.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					String sender = (String)v.getTag();
					MessagingService.updateSelectedUser(sender);
				}
			});
			TextView tv_croutonText = (TextView) view.findViewById(R.id.tv_croutonText);
			tv_croutonText.setText(content);
			currentActivity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					final Crouton crouton = Crouton.make(currentActivity, view);
					crouton.show();
				}
			});
		}
		catch(Exception ex)
		{
			showToast("Error at showDebugCrouton!");
			ex.printStackTrace();
		}
	}
	
	public static void showToast(final String text)
	{
		try
		{
			Activity currentActivity = MyApplication.getCurrentActivity();
			int duration = Toast.LENGTH_LONG;	
			Toast toast = null;
			if( currentActivity != null)
			{
				toast = Toast.makeText(currentActivity , text, duration);
			}
			else
			{
				toast = Toast.makeText(MyApplication.getContextOfApplication() , text, duration);
			}
			toast.show();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}

}
