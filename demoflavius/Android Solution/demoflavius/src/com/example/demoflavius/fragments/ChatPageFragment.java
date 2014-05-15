package com.example.demoflavius.fragments;

import java.util.ArrayList;
import java.util.UUID;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.example.demoflavius.R;
import com.example.demoflavius.activities.SlidingMenuParentActivity;
import com.example.demoflavius.application.MyApplication;
import com.example.demoflavius.data.ConversationMessage;
import com.example.demoflavius.data.Enums.DeviceType;
import com.example.demoflavius.data.Friend;
import com.example.demoflavius.data.Message;
import com.example.demoflavius.util.ImageLoaderManager;
import com.example.demoflavius.util.MessagingService;
import com.example.demoflavius.util.NotificationService;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.TableOperationCallback;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;


public class ChatPageFragment extends SherlockFragment{
	private static ListView messagesList;
	private View rootView;
	private EditText et_message; 
	private static TextView tv_usernameITalkTo;
	private Button sendMessageButton;
	private static ImageView iv_friendPictureITalkTo;
	public static String currentCustomerUsernameITalkTo = null, currentCustomerIdITalkTo = null, friendsPicture = null;
	private static ImageLoader imageLoader = ImageLoaderManager.getImageLoader();
	private static DisplayImageOptions options = ImageLoaderManager.getImageLoaderOptions();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) 
	{	
		rootView = null;
		try
		{
			rootView = inflater.inflate(R.layout.activity_chat, container, false);
			initializeUIElements();	
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			NotificationService.showDebugCrouton("failed at onCreateView on ChatPageFragment!");
		}
		return rootView;
	}

	private void initializeUIElements() {
		try
		{
			et_message = (EditText)rootView.findViewById(R.id.message);
			messagesList = (ListView) rootView.findViewById(R.id.messageHistory);	
			messagesList.setAdapter(MyApplication.chatAdapter);

			iv_friendPictureITalkTo = (ImageView) rootView.findViewById(R.id.iv_user);

			tv_usernameITalkTo = (TextView) rootView.findViewById(R.id.tv_username);
			if( tv_usernameITalkTo != null)
			{
				tv_usernameITalkTo.setText(currentCustomerUsernameITalkTo);
			}
			sendMessageButton = (Button) rootView.findViewById(R.id.sendMessageButton);
			sendMessageButton.setOnClickListener(new View.OnClickListener() 
			{
				public void onClick(View v) 
				{
					try
					{
						if( currentCustomerIdITalkTo == null && currentCustomerUsernameITalkTo == null)
						{
							if( MyApplication.friends != null && MyApplication.friends.size() > 0)
							{
								Friend friend = MyApplication.friends.get(0);

								currentCustomerIdITalkTo =   friend.getId();
								currentCustomerUsernameITalkTo = friend.getName();
								imageLoader.displayImage(friend.getPicture(), iv_friendPictureITalkTo, options,new ImageLoadingListener() {

									@Override
									public void onLoadingStarted(String arg0, View arg1) {}

									@Override
									public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {}

									@Override
									public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {}

									@Override
									public void onLoadingCancelled(String arg0, View arg1) {}
								});	
							}
						}
						if (et_message.getText().toString().equals(""))
							return;

						String content = et_message.getText().toString();
						et_message.setText("");

						Message message = new Message();
						message.setId(UUID.randomUUID().toString());
						message.setContent(content);
						message.setDeviceType(DeviceType.Android);
						message.setFromId(MyApplication.getCurrentUser().getProviderIdLong());
						message.setFromName(MyApplication.getCurrentUser().getName());
						message.setFromPicture(MyApplication.getCurrentUser().getPicture());
						message.setToId(currentCustomerIdITalkTo);

						MyApplication.mMessageTable.insert(message, new TableOperationCallback<Message>() {
							public void onCompleted(Message entity, Exception exception, ServiceFilterResponse response) 
							{
								if (exception == null) 
								{
									int x = 0;
									x++;
								} 
								else 
								{
									NotificationService.showDebugCrouton("failed at insert message!");
								}
							}
						});
						MessagingService.addMessage(currentCustomerIdITalkTo, content, false);

						InputMethodManager in = (InputMethodManager) getSherlockActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
						in.hideSoftInputFromWindow(et_message.getApplicationWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
					}		
					catch(Exception ex)
					{
						NotificationService.showDebugCrouton("Failed at InitializeUiElements in chat Activity!");
						ex.printStackTrace();
					}
				}
			});

		}
		catch(Exception ex)
		{
			NotificationService.showDebugCrouton("failed at initializeUIElements ChatPageFragment");
			ex.printStackTrace();
		}
	}

	public static void updateChatConversations(String customerId, String customerName, String friendPic) 
	{
		try
		{
			currentCustomerIdITalkTo = customerId;
			currentCustomerUsernameITalkTo = customerName;
			imageLoader.displayImage(friendPic, iv_friendPictureITalkTo, options,new ImageLoadingListener() {

				@Override
				public void onLoadingStarted(String arg0, View arg1) {}

				@Override
				public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {}

				@Override
				public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {}

				@Override
				public void onLoadingCancelled(String arg0, View arg1) {}
			});	
			if( tv_usernameITalkTo != null)
			{
				tv_usernameITalkTo.setText(currentCustomerUsernameITalkTo);
			}
			ArrayList<ConversationMessage> conversation = MessagingService.chatConversations.get(currentCustomerIdITalkTo);
			if( conversation == null)
			{
				conversation = new ArrayList<ConversationMessage>();
			}
			MyApplication.chatAdapter.setDataSource(conversation);

			MessagingService.chatConversations.put(currentCustomerIdITalkTo, conversation);
		}
		catch(Exception ex)
		{
			NotificationService.showDebugCrouton("failed at initializeChatConversations!");
			ex.printStackTrace();
		}
	}
}
