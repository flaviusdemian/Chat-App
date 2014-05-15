package com.example.demoflavius.activities;

import java.io.IOException;
import java.util.UUID;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.demoflavius.data.Enums.DeviceType;
import com.example.demoflavius.data.Enums.IdentityProvider;
import com.example.demoflavius.data.Message;
import com.example.demoflavius.data.UserEntity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.microsoft.windowsazure.mobileservices.MobileServiceAuthenticationProvider;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.MobileServiceUser;
import com.microsoft.windowsazure.mobileservices.NextServiceFilterCallback;
import com.microsoft.windowsazure.mobileservices.ServiceFilter;
import com.microsoft.windowsazure.mobileservices.ServiceFilterRequest;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponseCallback;
import com.microsoft.windowsazure.mobileservices.TableOperationCallback;
import com.microsoft.windowsazure.mobileservices.UserAuthenticationCallback;

public class ToMoveInAppActivity extends Activity{

	public static final String EXTRA_MESSAGE = "message"; 
	public static final String PROPERTY_REG_ID = "registration_id"; 
	public static final String SENDER_ID = "263861920437";
	static final String TAG = "GCM Demo";
	private static final String PROPERTY_APP_VERSION = "appVersion";
	private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
	private static UserEntity currentUserEntity = new UserEntity();

	String registrationId;
	GoogleCloudMessaging gcm;
	Context context;

	/**
	 * Mobile Service Client reference
	 */
	private MobileServiceClient mClient;

	/**
	 * Mobile Service Table used to access data
	 */
	private MobileServiceTable<UserEntity> mUserEntryTable;
	private MobileServiceTable<Message> mMessageTable;

	/**
	 * Adapter to sync the items list with the view
	 */
//	private ToDoItemAdapter mAdapter;

	/**
	 * EditText containing the "New ToDo" text
	 */
	private EditText mTextNewToDo;

	/**
	 * Progress spinner to use for table operations
	 */
	private ProgressBar mProgressBar;

	/**
	 * Initializes the activity
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_to_do);

		context = getApplicationContext();

		// Initialize the progress bar
		mProgressBar.setVisibility(ProgressBar.GONE);

		try 
		{
			// Check device for Play Services APK. If check succeeds, proceed with GCM registration.
			if (checkPlayServices()) 
			{
				gcm = GoogleCloudMessaging.getInstance(this);
				registrationId = getRegistrationId(context);
				if (registrationId.isEmpty()) 
				{
					registerInBackground();
				}
			} 
			else 
			{
				//Log.i(TAG, "No valid Google Play Services APK found.");
			}

			// Create the Mobile Service Client instance, using the provided
			// Mobile Service URL and key
			mClient = new MobileServiceClient(
					"https://demoflavius.azure-mobile.net/",
					"BAnpBgjwvAiseBuqYzJohLfKjwSWbD28",
					this).withFilter(new ProgressFilter());

			authenticate();

			// Get the Mobile Service Table instance to use
			mUserEntryTable = mClient.getTable(UserEntity.class);

//			mTextNewToDo = (EditText) findViewById(R.id.textNewToDo);

			// Create an adapter to bind the items with the view
//			mAdapter = new ToDoItemAdapter(this, R.layout.row_list_to_do);
//			ListView listViewToDo = (ListView) findViewById(R.id.listViewToDo);
			//			listViewToDo.setAdapter(mAdapter);

			// Load the items from the Mobile Service
			refreshItemsFromTable();
		} 
		catch (Exception e) 
		{
			createAndShowDialog(new Exception("There was an error creating the Mobile Service. Verify the URL"), "Error");
		}
	}

	@Override
	protected void onResume() 
	{
		super.onResume();
		// Check device for Play Services APK.
		checkPlayServices();
	}

	/**
	 * Initializes the activity menu
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
//		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	/**
	 * Select an option from the menu
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
//		if (item.getItemId() == R.id.menu_refresh) 
//		{
//			refreshItemsFromTable();
//		}
		return true;
	}


	private boolean checkPlayServices() 
	{
		int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
		if (resultCode != ConnectionResult.SUCCESS) 
		{
			if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) 
			{
				GooglePlayServicesUtil.getErrorDialog(resultCode, this, PLAY_SERVICES_RESOLUTION_REQUEST).show();
			}
			else 
			{
				Log.i(TAG, "This device is not supported.");
				finish();
			}
			return false;
		}
		return true;

	}

	/**
	 * Stores the registration ID and the app versionCode in the application's
	 * {@code SharedPreferences}.
	 *
	 * @param context application's context.
	 * @param regId registration ID
	 */
	private void storeRegistrationId(Context context, String regId) 
	{
		final SharedPreferences prefs = getGcmPreferences(context);
		int appVersion = getAppVersion(context);
		Log.i(TAG, "Saving regId on app version " + appVersion);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(PROPERTY_REG_ID, regId);
		editor.putInt(PROPERTY_APP_VERSION, appVersion);
		editor.commit();
	}

	/**
	 * Registers the application with GCM servers asynchronously.
	 * <p>
	 * Stores the registration ID and the app versionCode in the application's
	 * shared preferences.
	 */
	private void registerInBackground() {
		new AsyncTask<Void, Void, String>() {
			@Override
			protected String doInBackground(Void... params) {
				String msg = "";
				try {
					if (gcm == null) {
						gcm = GoogleCloudMessaging.getInstance(context);
					}
					registrationId = gcm.register(SENDER_ID);
					msg = "Device registered, registration ID=" + registrationId;

					// You should send the registration ID to your server over HTTP, so it
					// can use GCM/HTTP or CCS to send messages to your app.

					// For this demo: we don't need to send it because the device will send
					// upstream messages to a server that echo back the message using the
					// 'from' address in the message.

					// Persist the regID - no need to register again.
					storeRegistrationId(context, registrationId);
				} catch (IOException ex) {
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
		String registrationId = prefs.getString(PROPERTY_REG_ID, "");
		try
		{
			if (registrationId.isEmpty()) 
			{
				Log.i(TAG, "Registration not found.");
				return "";
			}
			// Check if app was updated; if so, it must clear the registration ID
			// since the existing regID is not guaranteed to work with the new
			// app version.
			int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
			int currentVersion = getAppVersion(context);
			if (registeredVersion != currentVersion) {
				Log.i(TAG, "App version changed.");
				return "";
			}
		}
		catch(Exception ex)
		{
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
	 * Mark an item as completed
	 * 
	 * @param item
	 *            The item to mark
	 */
	public void checkItem(UserEntity item) {
		if (mClient == null) {
			return;
		}

		// Set the item as completed and update it in the table
		//item.setComplete(true);

		//		mUserEntryTable.update(item, new TableOperationCallback<ToDoItem>() {
		//
		//			public void onCompleted(ToDoItem entity, Exception exception, ServiceFilterResponse response) {
		//				if (exception == null) {
		//					if (entity.isComplete()) {
		//						mAdapter.remove(entity);
		//					}
		//				} else {
		//					createAndShowDialog(exception, "Error");
		//				}
		//			}
		//
		//		});
	}

	/**
	 * Add a new item
	 * 
	 * @param view
	 *            The view that originated the call
	 */
	public void addItem(View view) 
	{
		try
		{
			if (mClient == null) 
			{
				return;
			}

			// Create a new item
			//			ToDoItem item = new ToDoItem();
			//			item.setText(mTextNewToDo.getText().toString());
			//			item.setComplete(false);

			// Insert the new item
			//			mUserEntryTable.insert(item, new TableOperationCallback<ToDoItem>() 
			//					{
			//				public void onCompleted(ToDoItem entity, Exception exception, ServiceFilterResponse response) 
			//				{
			//					if (exception == null) 
			//					{
			//						if (!entity.isComplete()) 
			//						{
			//							mAdapter.add(entity);
			//						}
			//					} 
			//					else 
			//					{
			//						createAndShowDialog(exception, "Error");
			//					}
			//				}
			//					});
			//			mTextNewToDo.setText("");
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}

	/**
	 * Refresh the list with the items in the Mobile Service Table
	 */
	private void refreshItemsFromTable() {

		// Get the items that weren't marked as completed and add them in the
		// adapter
		//		mUserEntryTable.where().field("complete").eq(val(false)).execute(new TableQueryCallback<ToDoItem>() {
		//
		//			public void onCompleted(List<ToDoItem> result, int count, Exception exception, ServiceFilterResponse response) {
		//				if (exception == null) {
		//					mAdapter.clear();
		//
		//					for (ToDoItem item : result) {
		//						mAdapter.add(item);
		//					}
		//
		//				} else {
		//					createAndShowDialog(exception, "Error");
		//				}
		//			}
		//		});
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

	private class ProgressFilter implements ServiceFilter {

		@Override
		public void handleRequest(ServiceFilterRequest request, NextServiceFilterCallback nextServiceFilterCallback,
				final ServiceFilterResponseCallback responseCallback) {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					if (mProgressBar != null) mProgressBar.setVisibility(ProgressBar.VISIBLE);
				}
			});

			nextServiceFilterCallback.onNext(request, new ServiceFilterResponseCallback() {

				@Override
				public void onResponse(ServiceFilterResponse response, Exception exception) {
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							if (mProgressBar != null) mProgressBar.setVisibility(ProgressBar.GONE);
						}
					});

					if (responseCallback != null)  responseCallback.onResponse(response, exception);
				}
			});
		}
	}

	//identity integration

	private void authenticate() 
	{
		try
		{
			// Login using the Google provider.
			mClient.login(MobileServiceAuthenticationProvider.Facebook, new UserAuthenticationCallback() 
			{
				@Override
				public void onCompleted(MobileServiceUser user,Exception exception, ServiceFilterResponse response) {
					if (exception == null) {
						//						createAndShowDialog(String.format(
						//								"You are now logged in - %1$2s",
						//								user.getUserId()), "Success");
//						currentUserEntity.setId(UUID.randomUUID().toString());
//						currentUserEntity.setUsername(user.getUserId());
//						currentUserEntity.setProviderId(user.getUserId());
//						currentUserEntity.setToken(user.getAuthenticationToken());

						registerToPushNotifications();
						createTable();
					} else {
						createAndShowDialog("You must log in. Login Required", "Error");
					}
				}
			});
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}

	private void registerToPushNotifications()
	{
		currentUserEntity.setIdentityProvider(IdentityProvider.Facebook);

		// Insert the new item
		mUserEntryTable.insert(currentUserEntity, new TableOperationCallback<UserEntity>() 
		{
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

	private void createTable() 
	{
		try
		{
			// Get the Mobile Service Table instance to use
			mMessageTable = mClient.getTable(Message.class);
			mUserEntryTable = mClient.getTable(UserEntity.class);

//			mTextNewToDo = (EditText) findViewById(R.id.ninja);

			// Create an adapter to bind the items with the view
//			mAdapter = new ToDoItemAdapter(this, R.layout.row_list_to_do);
//			ListView listViewToDo = (ListView) findViewById(R.id.listViewToDo);
//			listViewToDo.setAdapter(mAdapter);

			// Load the items from the Mobile Service
			refreshItemsFromTable();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}

}
