package com.example.demoflavius.activities;


import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

import com.example.demoflavius.R;
import com.example.demoflavius.application.ApplicationConstants;
import com.example.demoflavius.application.MyApplication;
import com.example.demoflavius.data.Channel;
import com.example.demoflavius.data.Enums;
import com.example.demoflavius.data.Friend;
import com.example.demoflavius.data.Message;
import com.example.demoflavius.data.UserEntity;
import com.example.demoflavius.data.UserEntityProfileCompletion;
import com.example.demoflavius.fragments.ChatPageFragment;
import com.example.demoflavius.util.IdentityProviderConverter;
import com.example.demoflavius.util.IdentityProviderParser;
import com.example.demoflavius.util.NavigationService;
import com.example.demoflavius.util.NotificationService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.microsoft.windowsazure.mobileservices.ApiJsonOperationCallback;
import com.microsoft.windowsazure.mobileservices.ApiOperationCallback;
import com.microsoft.windowsazure.mobileservices.MobileServiceAuthenticationProvider;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceUser;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.TableOperationCallback;
import com.microsoft.windowsazure.mobileservices.UserAuthenticationCallback;

public class LoginActivity extends Activity 
{
	private Button btn_facebook, btn_microsoft, btn_google, btn_twitter;


	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		try
		{
			super.onCreate(savedInstanceState);
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(R.layout.activity_login);
			MyApplication.setCurrentActivity(LoginActivity.this);
			
			try 
			{
				MyApplication.setMobileServiceClient(new MobileServiceClient("url", "key", LoginActivity.this));
				MyApplication.mUserEntryTable = MyApplication.getMobileServiceClient().getTable(UserEntity.class);
				MyApplication.mMessageTable = MyApplication.getMobileServiceClient().getTable(Message.class);
				MyApplication.mChannelTable = MyApplication.getMobileServiceClient().getTable(Channel.class);
			}
			catch(Exception ex)
			{
				NotificationService.showDebugCrouton("failed at loginactivity on create!");
				ex.printStackTrace();
			}

			try 
			{
				// Check device for Play Services APK. If check succeeds, proceed with GCM registration.
				if (checkPlayServices()) 
				{
					MyApplication.gcm = GoogleCloudMessaging.getInstance(this);
					MyApplication.registrationId = getRegistrationId(MyApplication.getContextOfApplication());
					if (MyApplication.registrationId.isEmpty()) 
					{
						registerInBackground();
					}
				} 
				else 
				{
					NotificationService.showDebugCrouton("No valid Google Play Services APK found.");
				}
			} 
			catch (Exception e) 
			{
				createAndShowDialog(new Exception("There was an error creating the Mobile Service. Verify the URL"), "Error");
			}
			InitializeUIElements();
		}
		catch(Exception ex)
		{
			NotificationService.showDebugCrouton("failed at login oncreate!");
			ex.printStackTrace();
		}
	}

	@Override
	protected void onResume() 
	{
		super.onResume();
		MyApplication.setCurrentActivity(LoginActivity.this);
		// Check device for Play Services APK.
		checkPlayServices();

	}

	private boolean checkPlayServices() 
	{
		int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
		if (resultCode != ConnectionResult.SUCCESS) 
		{
			if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) 
			{
				GooglePlayServicesUtil.getErrorDialog(resultCode, this, MyApplication.PLAY_SERVICES_RESOLUTION_REQUEST).show();
			}
			else 
			{
				finish();
			}
			return false;
		}
		return true;

	}

	private void storeRegistrationId(Context context, String regId) 
	{
		final SharedPreferences prefs = getGcmPreferences(context);
		int appVersion = getAppVersion(context);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(MyApplication.PROPERTY_REG_ID, regId);
		editor.putInt(MyApplication.PROPERTY_APP_VERSION, appVersion);
		editor.commit();
	}

	private void registerInBackground() {
		new AsyncTask<Void, Void, String>() {
			@Override
			protected String doInBackground(Void... params) {
				String msg = "";
				try {
					if (MyApplication.gcm == null) {
						MyApplication.gcm = GoogleCloudMessaging.getInstance(MyApplication.getContextOfApplication());
					}
					MyApplication.registrationId = MyApplication.gcm.register(MyApplication.SENDER_ID);
					msg = "Device registered, registration ID=" + MyApplication.registrationId;

					// You should send the registration ID to your server over HTTP, so it
					// can use GCM/HTTP or CCS to send messages to your app.

					// For this demo: we don't need to send it because the device will send
					// upstream messages to a server that echo back the message using the
					// 'from' address in the message.

					// Persist the regID - no need to register again.
					storeRegistrationId(MyApplication.getContextOfApplication(), MyApplication.registrationId);
				} 
				catch (IOException ex) 
				{
					msg = "Error :" + ex.getMessage();
					// If there is an error, don't just keep trying to register.
					// Require the user to click a button again, or perform
					// exponential back-off.
				}
				return msg;
			}

			@Override
			protected void onPostExecute(String msg) 
			{
				int x = 0 ;
				x++;
				//mDisplay.append(msg + "\n");
			}
		}.execute(null, null, null);
	}


	/**
	 * @return Application's version code from the {@code PackageManager}.
	 */
	private static int getAppVersion(Context context) 
	{
		try 
		{
			PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionCode;
		} 
		catch (NameNotFoundException e) 
		{
			throw new RuntimeException("Could not get package name: " + e);
		}
	}

	private String getRegistrationId(Context context) 
	{
		final SharedPreferences prefs = getGcmPreferences(context);
		String registrationId = prefs.getString(MyApplication.PROPERTY_REG_ID, "");
		try
		{
			if (registrationId.isEmpty()) 
			{
				return "";
			}
			// Check if app was updated; if so, it must clear the registration ID
			// since the existing regID is not guaranteed to work with the new
			// app version.
			int registeredVersion = prefs.getInt(MyApplication.PROPERTY_APP_VERSION, Integer.MIN_VALUE);
			int currentVersion = getAppVersion(context);
			if (registeredVersion != currentVersion) {
				return "";
			}
		}
		catch(Exception ex)
		{
			NotificationService.showDebugCrouton("failed at getRegistrationId");
			ex.printStackTrace();
		}
		return registrationId;
	}

	/**
	 * @return Application's {@code SharedPreferences}.
	 */
	private SharedPreferences getGcmPreferences(Context context) 
	{
		// This sample app persists the registration ID in shared preferences, but
		// how you store the regID in your app is up to you.
		return getSharedPreferences(LoginActivity.class.getSimpleName(),Context.MODE_PRIVATE);
	}

	/**
	 * Creates a dialog and shows it
	 * 
	 * @param exception
	 *            The exception to show in the dialog
	 * @param title
	 *            The dialog title
	 */
	private void createAndShowDialog(Exception exception, String title) 
	{
		try
		{
			Throwable ex = exception;
			if(exception.getCause() != null){
				ex = exception.getCause();
			}
			createAndShowDialog(ex.getMessage(), title);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}

	/**
	 * Creates a dialog and shows it
	 * 
	 * @param message
	 *            The dialog message
	 * @param title
	 *            The dialog title
	 */
	private void createAndShowDialog(String message, String title) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setMessage(message);
		builder.setTitle(title);
		builder.create().show();
	}

	//identity integration

	private void authenticate(final MobileServiceAuthenticationProvider provider) 
	{
		try
		{
			// Login using the Google provider.
			MyApplication.getMobileServiceClient().login(provider, new UserAuthenticationCallback() 
			{
				@Override
				public void onCompleted(MobileServiceUser user,Exception exception, ServiceFilterResponse response) {
					if (exception == null) 
					{
						MyApplication.mobileServicesUser = user;
						registerWithMobileServices(provider);
					} 
					else 
					{
						createAndShowDialog("You must log in. Login Required", "Error");
					}
				}
			});
		}
		catch(Exception ex)
		{
			NotificationService.showDebugCrouton("Failed at authenticate!");
			ex.printStackTrace();
		}
	}


	private void registerWithMobileServices(MobileServiceAuthenticationProvider provider) 
	{
		try
		{
			MyApplication.getCurrentUser().setId(UUID.randomUUID().toString());
			MyApplication.getCurrentUser().setProviderIdLong(MyApplication.mobileServicesUser.getUserId());
			MyApplication.getCurrentUser().setProviderIdShort(IdentityProviderParser.GetShortProvider(MyApplication.mobileServicesUser.getUserId()));
			MyApplication.getCurrentUser().setToken(MyApplication.mobileServicesUser.getAuthenticationToken());
			MyApplication.getCurrentUser().setIdentityProvider(IdentityProviderConverter.GetProvider(provider));

			// Insert the new user
			MyApplication.mUserEntryTable.insert(MyApplication.getCurrentUser(), new TableOperationCallback<UserEntity>() {
				public void onCompleted(UserEntity entity, Exception exception, ServiceFilterResponse response) 
				{
					if (exception == null) 
					{
						int x = 0;
						x++;
					} 
					else 
					{
						createAndShowDialog(exception, "Error");
					}
				}
			});
		}
		catch(Exception ex)
		{
			NotificationService.showDebugCrouton("failed at insert user!");
			ex.printStackTrace();
		}
		try
		{
			MyApplication.currentChannel.setId(UUID.randomUUID().toString());
			MyApplication.currentChannel.setChannelUri("");
			MyApplication.currentChannel.setDeviceType(Enums.DeviceType.Android);
			MyApplication.currentChannel.setUserId(MyApplication.getCurrentUser().getProviderIdLong());
			MyApplication.currentChannel.setRegistrationId(MyApplication.registrationId);

			MyApplication.mChannelTable.insert(MyApplication.currentChannel, new TableOperationCallback<Channel>() {
				public void onCompleted(Channel entity, Exception exception, ServiceFilterResponse response) 
				{
					if (exception == null) 
					{
						int x = 0;
						x++;
					} 
					else 
					{
						createAndShowDialog(exception, "Error");
					}
				}
			});
		}
		catch(Exception ex)
		{
			NotificationService.showDebugCrouton("failed at insert channel!");
			ex.printStackTrace();
		}

		try
		{
			//concrete api call
			MyApplication.getMobileServiceClient().invokeApi(ApplicationConstants.COMPLETE_USER_PROFILE_METHOD, "GET", null, UserEntityProfileCompletion.class,  new ApiOperationCallback<UserEntityProfileCompletion>() {
				@Override
				public void onCompleted(UserEntityProfileCompletion result, Exception exception, ServiceFilterResponse response) {
					if (exception == null) 
					{
						MyApplication.getCurrentUser().setName(result.getName());
						MyApplication.getCurrentUser().setPicture(result.getPicture());
						MyApplication.getCurrentUser().setAccessToken(result.getAccessToken());
						MyApplication.getCurrentUser().setAccessTokenSecret(result.getAccessTokenSecret());	
						NavigationService.navigate(LoginActivity.this, ChatActivity.class, null);
					} 
					else 
					{
						createAndShowDialog(exception, "Error");
					}
				}
			});		
		}
		catch(Exception ex)
		{
			NotificationService.showDebugCrouton("failed at complete_user_profile api call!");
			ex.printStackTrace();
		}

		try
		{
			ArrayList<Pair<String, String>> parameters = new ArrayList<Pair<String,String>>();
			parameters.add(new Pair<String, String>("userId", MyApplication.getCurrentUser().getProviderIdLong()));
			MyApplication.getMobileServiceClient().invokeApi(ApplicationConstants.USERS_METHOD, "GET", parameters, new ApiJsonOperationCallback() {
				@Override
				public void onCompleted(JsonElement result, Exception exception, ServiceFilterResponse respone) {
					if (exception == null) 
					{
						java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<ArrayList<Friend>>() {} .getType();
						MyApplication.friends = new Gson().fromJson(result, type);
						if( MyApplication.friends != null)
						{
							Friend friend = MyApplication.friends.get(0);
							ChatPageFragment.updateChatConversations(friend.getId(), friend.getName(), friend.getPicture());
							MyApplication.chatAdapterForSlidingMenu.setDataSource(MyApplication.friends);
						}
						else
						{
							NotificationService.showDebugCrouton("something went wrong at friends...");
						}
					} 
					else 
					{
						createAndShowDialog(exception, "Error");
					}
				}
			});
		}
		catch(Exception ex)
		{
			NotificationService.showDebugCrouton("failed at user api call!");
			ex.printStackTrace();
		}
	}

	private void InitializeUIElements() 
	{
		btn_facebook = (Button) findViewById(R.id.btn_facebook);
		btn_facebook.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				authenticate(MobileServiceAuthenticationProvider.Facebook);
			}
		});

		btn_microsoft = (Button) findViewById(R.id.btn_microsoft);
		btn_microsoft.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				authenticate(MobileServiceAuthenticationProvider.MicrosoftAccount);
			}
		});
		btn_google = (Button) findViewById(R.id.btn_google);
		btn_google.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				authenticate(MobileServiceAuthenticationProvider.Google);
			}
		});
		btn_twitter = (Button) findViewById(R.id.btn_twitter);
		btn_twitter.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				authenticate(MobileServiceAuthenticationProvider.Twitter);
			}
		});
	}
}
