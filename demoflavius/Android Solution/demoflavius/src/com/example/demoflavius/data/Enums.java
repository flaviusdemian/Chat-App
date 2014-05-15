package com.example.demoflavius.data;

public class Enums {
	public static enum LevelType { 
		Beginner, 
		Intermediate,
		Professional
	};
	

	public enum MessageType 
	{
		 InitializeConnection,
         UserOnline,
         SpecificUserOnline,
         UserOffline,
         Sent,
         Received,
         UserNotAvailable,
         CallRequest,
         CallConfirmation,
         CallDenial,
         ProceedWithCall,
         ApplicationActive,
         ApplicationSuspended,
         FriendRequestSent,
         FriendRequestApproved,
         FriendRequestDenied
	};

	public static enum StateType
	{
		Offline,
		Available, 
		Busy 
	};
	
	public static enum HttpMethodType
	{
		Get,
		Post,
		Put,
		Delete
	};
	
	public static enum ListViewOperationType
	{
		New,
		Append,
		UpdateItems
	};
	
	public static enum DepthDetailsLevelType 
	{ 
		Low, 
		Medium,
		Full 
	};
	
	public static enum AsyncTaskNumberToLoadType
	{
		ScreenHeight,
		DoubleScreenHeight
	}
	
	public static enum LanguageType 
	{ 
		Spoken, 
		SignLanguage
	}
	
	 public static enum DeviceType
     {
         Android,
         IOS,
         Web,
         WindowsPhone
     }
	 
     public enum IdentityProvider
     {
         Facebook,
         Google,
         Twitter,
         MicrosoftAccount
     }
}
