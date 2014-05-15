package com.example.demoflavius.util;

import java.util.ArrayList;
import java.util.HashMap;

import com.example.demoflavius.application.MyApplication;
import com.example.demoflavius.data.ConversationMessage;
import com.example.demoflavius.data.Friend;
import com.example.demoflavius.fragments.ChatPageFragment;

public class MessagingService 
{
	public static HashMap<String, ArrayList<ConversationMessage>> chatConversations = new HashMap<String, ArrayList<ConversationMessage>>();

	public static void addMessage(final String currentCustomerIdITalkTo, final String content, final boolean rightPart)
	{
		try
		{
			MyApplication.getCurrentActivity().runOnUiThread(new Runnable() {

				@Override
				public void run() {
					ArrayList<ConversationMessage> conversation = MessagingService.chatConversations.get(currentCustomerIdITalkTo);
					if( conversation == null)
					{
						conversation = new ArrayList<ConversationMessage>();
						MessagingService.chatConversations.put(currentCustomerIdITalkTo, conversation);
					}
					conversation.add(new ConversationMessage(currentCustomerIdITalkTo, content, rightPart));
					MyApplication.chatAdapter.notifyDataSetChanged();
				}	
			});
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			NotificationService.showDebugCrouton("failed at add Message!");
		}
	}

	public static void updateSelectedUser(final String currentCustomerIdITalkTo)
	{
		try
		{
			MyApplication.getCurrentActivity().runOnUiThread(new Runnable() {

				@Override
				public void run() 
				{
					ArrayList<ConversationMessage> conversation = MessagingService.chatConversations.get(currentCustomerIdITalkTo);
					if( conversation == null)
					{
						conversation = new ArrayList<ConversationMessage>();
					}
					
					MessagingService.chatConversations.put(currentCustomerIdITalkTo, conversation);
					ArrayList<Friend> friends = MyApplication.getChatAdapterForSlidingMenu().getDataSource();
					Friend senderFriend = null;
					for (Friend friend : friends) 
					{
						if( friend.getId().equals(currentCustomerIdITalkTo) == true)
						{
							senderFriend = friend;
							break;
						}
					}

					if( senderFriend != null)	
					{
						ChatPageFragment.updateChatConversations(senderFriend.getId(), senderFriend.getName(), senderFriend.getPicture());
					}
					MyApplication.chatAdapter.setDataSource(conversation);	
				}
			});
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			NotificationService.showDebugCrouton("failed at add Message!");
		}
	}
}
