package com.example.demoflavius.application;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.media.AudioManager;

import com.example.demoflavius.R;
import com.example.demoflavius.adapters.BubbleChatAdapter;
import com.example.demoflavius.adapters.ChatAdapterForSlidingMenu;
import com.example.demoflavius.data.Channel;
import com.example.demoflavius.data.ConversationMessage;
import com.example.demoflavius.data.Friend;
import com.example.demoflavius.data.Message;
import com.example.demoflavius.data.UserEntity;
import com.example.demoflavius.util.NotificationService;
import com.example.demoflavius.util.SoundManager;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.MobileServiceUser;

public class MyApplication extends Application
{
	private static MobileServiceClient mClient;
	public static ChatAdapterForSlidingMenu chatAdapterForSlidingMenu;
	public static BubbleChatAdapter chatAdapter;
	public static ArrayList<Friend> friends = new ArrayList<Friend>();

	private static UserEntity currentUser = new UserEntity();
	public static Channel currentChannel = new Channel();
	public static MobileServiceUser mobileServicesUser = null;
	/**
	 * Mobile Service Table used to access data
	 */
	public static MobileServiceTable<UserEntity> mUserEntryTable;
	public static MobileServiceTable<Message> mMessageTable;
	public static MobileServiceTable<Channel> mChannelTable;
	private static int chatSound;

	private static Context applicationContext;
	private static Activity currentActivity;

	public static final String PROPERTY_REG_ID = "registration_id"; 
	public static final String SENDER_ID = "263861920437";
	public static final String PROPERTY_APP_VERSION = "appVersion";
	public static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

	public static String registrationId;
	public static GoogleCloudMessaging gcm;	
	public static boolean applicationIsActive;

	public static SoundManager snd;
	public static int intro;

	public void onCreate()
	{
		try
		{
			applicationContext = getApplicationContext();
			 // Create an instance of our sound manger
	        snd = new SoundManager(applicationContext);
			
			registerActivityLifecycleCallbacks(new LifecycleHandler());
			chatAdapterForSlidingMenu = new ChatAdapterForSlidingMenu(applicationContext, R.layout.activity_slidingmenu_chat_row, friends);
			chatAdapter = new BubbleChatAdapter(getApplicationContext(),R.layout.activity_chat_row, new ArrayList<ConversationMessage>());
		}
		catch(Exception ex)
		{
			NotificationService.showDebugCrouton("failed at application on create!");
			ex.printStackTrace();
		}
	}
	public static UserEntity getCurrentUser() {
		return currentUser;	
	}

	public static Context getContextOfApplication() {
		return applicationContext;
	}

	public static ChatAdapterForSlidingMenu getChatAdapterForSlidingMenu() {
		return chatAdapterForSlidingMenu;
	}

	public static void setCurrentActivity(Activity activity) {
		currentActivity = activity;
	}

	public static Activity getCurrentActivity() {
		return currentActivity;
	}
	public static MobileServiceClient getMobileServiceClient() {
		return mClient;
	}
	public static void setMobileServiceClient(MobileServiceClient mClient) {
		MyApplication.mClient = mClient;
	}
	public static int getChatSound() {
		
		// Set volume rocker mode to media volume
		currentActivity.setVolumeControlStream(AudioManager.STREAM_MUSIC);
		try
		{
			intro = snd.load(R.raw.chat_sound);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			NotificationService.showDebugCrouton("failed at load intro!");
		}
		return chatSound;
	}
}
