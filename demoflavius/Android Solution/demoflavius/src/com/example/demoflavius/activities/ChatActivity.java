package com.example.demoflavius.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.example.demoflavius.R;
import com.example.demoflavius.application.MyApplication;
import com.example.demoflavius.fragments.ChatPageFragment;
import com.example.demoflavius.util.NotificationService;

public class ChatActivity extends SlidingMenuParentActivity {
	private ChatPageFragment fragmentChat = new ChatPageFragment();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try
		{
			super.onCreate(savedInstanceState);
			MyApplication.setCurrentActivity(ChatActivity.this);
		}
		catch(Exception ex)
		{
			NotificationService.showDebugCrouton("Failed at onCreate");
			ex.printStackTrace();
		}
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		MyApplication.setCurrentActivity(ChatActivity.this);
	}
	@Override
	protected void selectItem() {
		try
		{
			FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
			ft.replace(R.id.content_frame, fragmentChat);
			ft.commit();
			
		}
		catch(Exception ex)
		{
			NotificationService.showDebugCrouton("failed at select item in SlidingMenuCustomersAndInterpretersTabActivity");
			ex.printStackTrace();
		}
	}
}
