/*

 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.demoflavius;

import org.json.JSONObject;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.example.demoflavius.activities.LoginActivity;
import com.example.demoflavius.application.MyApplication;
import com.example.demoflavius.data.Message;
import com.example.demoflavius.fragments.ChatPageFragment;
import com.example.demoflavius.util.MessagingService;
import com.example.demoflavius.util.NotificationService;
import com.google.android.gms.gcm.GoogleCloudMessaging;

/**
 * This {@code IntentService} does the actual handling of the GCM message.
 * {@code GcmBroadcastReceiver} (a {@code WakefulBroadcastReceiver}) holds a
 * partial wake lock for this service while the service does its work. When the
 * service is finished, it calls {@code completeWakefulIntent()} to release the
 * wake lock.
 */
public class GcmIntentService extends IntentService 
{
	public static final int NOTIFICATION_ID = 1;
	private NotificationManager mNotificationManager;
	NotificationCompat.Builder builder;

	public GcmIntentService() 
	{
		super("GcmIntentService");
	}
	public static final String TAG = "GCM Demo";

	@Override
	protected void onHandleIntent(Intent intent) 
	{
		Bundle extras = intent.getExtras();
		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
		String message = intent.getExtras().getString("message");
		// The getMessageType() intent parameter must be the intent you received
		// in your BroadcastReceiver.
		String messageType = gcm.getMessageType(intent);
		
		if (!extras.isEmpty()) 
		{  // has effect of unparcelling Bundle
			/*
			 * Filter messages based on message type. Since it is likely that GCM will be
			 * extended in the future with new message types, just ignore any message types you're
			 * not interested in, or that you don't recognize.
			 */
			if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) 
			{
				sendNotification("Send error: " + extras.toString());
			} 
			else 
				if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) 
				{
					sendNotification("Deleted messages on server: " + extras.toString());
					// If it's a regular GCM message, do some work.
				} 
				else 
					if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) 
					{
						// Post notification of received message.
						sendNotification(message);
					}
		}
		// Release the wake lock provided by the WakefulBroadcastReceiver.
		GcmBroadcastReceiver.completeWakefulIntent(intent);
	}

	// Put the message into a notification and post it.
	// This is just one simple example of what you might choose to do with
	// a GCM message.
	private void sendNotification(String msg) 
	{
		try
		{	
			JSONObject myJson = new JSONObject(msg);
			String content = myJson.getString("content");
			String fromId =  myJson.getString("fromId");
			String fromPicture = myJson.getString("fromPicture");
			String fromName = myJson.getString("fromName");
			
			if( MyApplication.applicationIsActive == false)
			{
				mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
				Uri notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
				
				PendingIntent contentIntent = PendingIntent.getActivity(this, 0,  new Intent(this, LoginActivity.class), 0);
				
				NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
				.setSmallIcon(R.drawable.ninja)
				.setContentTitle(content)
				.setStyle(new NotificationCompat.BigTextStyle()
				.bigText(fromName))
				.setSound(notificationSound)
				.setContentText(content);

				mBuilder.setContentIntent(contentIntent);
				mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
			}
			else
			{
				Message message=  new Message();
				message.setContent(content);
				message.setFromId(fromId);
				message.setFromName(fromName);
				message.setFromPicture(fromPicture);
				
				MessagingService.addMessage(fromId, content, true);
				MyApplication.snd.playSound(R.raw.chat_sound);
				if( fromId.equals(ChatPageFragment.currentCustomerIdITalkTo) == false)
				{
					NotificationService.showInfoCrouton(String.format("%s said: %s", fromName, content), fromId);
				}
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			NotificationService.showDebugCrouton("failed at sendNotification!");
		}
	}
}
