package com.example.demoflavius.application;

public class ApplicationConstants {


	//////////////////////////////////// API METHODS ///////////////////////////////	

	public static final String GET_INTERPRETER_EXPERTISE = "User/GetExpertise";
	public static final String GET_INTERPRETER_HISTORY = "User/GetHistory";
	public static final String GET_CUSTOMERS = "User/GetCustomers";
	public static final String GET_INTERPRETERS = "User/GetInterpreters";
	public static final String GET_FILTER_RESULTS = "TranslationService/SuggestInterpreters"; 
	public static final String GET_CUSTOMER_PROFILE = "User/GetCustomerProfile";
	public static final String GET_INTERPRETER_PROFILE = "User/GetInterpreterProfile";
	public static final String GET_PROFILE_BY_USER_EMAIL = "User/GetProfileByUserEmail";
	public static final String GET_PROFILE_BY_USERNAME = "User/GetProfileByUsername";
	public static final String GET_FRIENDS = "Account/GetFriends";
	public static final String GET_FRIEND_REQUESTS = "FriendRequest/GetFriendRequests";
	public static final String GET_LANGUAGES = "language/getLanguages";
	
	public static final String POST_SEND_FRIEND_REQUEST = "FriendRequest/SendFriendRequest";
	public static final String POST_SEND_FRIEND_REQUEST_APPROVAL = "FriendRequest/ApproveFriendRequest";
	public static final String POST_SEND_FRIEND_REQUEST_DENIAL = "FriendRequest/DenyFriendRequest";
	public static final String POST_UPLOAD_PROFILE_IMAGE = "PictureUpload/UploadProfilePicture";
	public static final String POST_ADD_FILTER = "Filter/CreateFilter";
	public static final String POST_CALL_INTERPRETER = "Opentok/CreateSession";
	public static final String POST_SAVE_REVIEW = "Review/CreateReview";
	public static final String POST_REPORT_A_PROBLEM = "Feedback/ReportAProblem";
	public static final String POST_SEND_FEEDBACK = "Feedback/SendFeedback";
	public static final String POST_CREATE_TRANSLATION_JOB = "TranslationService/RegisterJob";
	public static final String POST_TRANSFER_AMOUNT = "TranslationService/Transferamount";
	public static final String POST_REGISTER_CUSTOM_USER = "Account/CustomRegister";
	public static final String POST_REGISTER_FACEBOOK_USER = "Account/FacebookRegister";
	public static final String POST_LOGIN_CUSTOM_USER = "Account/CustomLogin";
	public static final String POST_LOGOUT_USER = "Account/Logout";
	public static final String POST_LOGIN_FACEBOOK_USER = "Account/FacebookLogin";
	public static final String POST_VERIFY_USER_VALIDITY = "Account/VerifyUserValidity";
	public static final String POST_USER_PROFILE_FOR_SESSION_RESTORE = "Account/GetProfileForSessionRestore";

	public static final String UNKNOWN_USER = "user0";

	public static final String WEBSOCKETS_SERVER = "ws://192.168.16.142:84/WebSocketServer.ashx";
	public static final String WEBAPI_SERVER = "http://192.168.16.142:88/api/";
//		public static final String WEBSOCKETS_SERVER = "ws://193.226.9.134:8000/WebSocketsServer/WebSocketServer.ashx";
//		public static final String WEBAPI_SERVER = "http://193.226.9.134:8000/webapi/api/";

    public static String GENERIC_SUCCESS_MESSAGE = "success";
    public static String GENERIC_NO_RESULTS_MESSAGE = "empty";

	//////////////////////////////////// END API METHODS ///////////////////////////////	

	//////////////////////////////////// KEYS FOR INTENTS //

	public static final String EXTRA_CURRENT_USER_PROFILE_KEY = "currentUser";
	public static final String EXTRA_CUSTOMER_USERNAME_KEY = "username";
	public static final String EXTRA_CUSTOMER_ID_KEY = "customerId";
	public static final String EXTRA_INTERPRETER_ID_KEY = "interpreterId";
	public static final String EXTRA_FILTER_PAGE_SOURCE_KEY = "source";
	public static final String EXTRA_MESSAGE_KEY = "message";
	public static final String EXTRA_FILTER_PAGE_DESTINATION_KEY = "destination";
	public static final String EXTRA_FILTER_NAVIGATION_FROM_FILTER_SEARCH_KEY = "navigationFromSearchFiltersPage";
	public static final String EXTRA_USER_MANUAL_PCITURES_KEY = "pictures";
	public static final String EXTRA_USER_MANUAL_PICTURE_CURRENT_INDEX = "currentIndex";
	public static final String EXTRA_EDIT_OR_CREATE_FILTER_KEY = "editOrCreate";
	public static final String EXTRA_FACEBOOK_REGISTER_OR_LOGIN = "facebookRegisterOrLoginFlag";
	public static final String EXTRA_LOAD_LOGIN_OR_REGISTER_PAGE = "loadLoginOrRegisterpage";
	public static final String EXTRA_MESSAGE_FROM_USER = "message";

	//////////////////////////////////// END KEYS ///////////////////////////////


	//////////////////////////////////// SHARED PREFERENCES KEYS ///////////////////////////////

	public static final String SHARED_PREFERENCES_USER_ASSOCIATION_DICTIONARY_KEY = "userAssociationsDictionary";
	public static final String SHARED_PREFERENCES_USER_CHAT_ASSOCIATION_DICTIONARY_KEY = "userChatAssociationsDictionary";
	public static final String SHARED_PREFERENCES_USER_FILTERS_ASSOCIATION_DICTIONARY_KEY = "userFilterAssociationsDictionary";
	public static final String SHARED_PREFERENCES_CURRENT_USER_CONTAINER_KEY = "currentUserContainer";
	public static final String SHARED_PREFERENCES_LAST_USER_KEY = "currentUsername";
	//	public static final String EXTRA_FILTER_PAGE_DESTINATION_KEY = "destination";

	//////////////////////////////////// END KEYS ///////////////////////////////


	//////////////////////////////////// APPLICATION VALUES ///////////////////////////////

	public static final String EMPTY_PASSWORD_FOR_FACEBOOK = "emptyPassword";
	public static final float DEFAULT_AMOUNT_PER_MINUTE = 100;
	public static final String TOAST_NOTIFICATION_TYPE = "SMS";
	public static final String DEFAULT_IMAGE = "Images/users/unknown2.png";
	public static final String PAYPAL_MERCHANT_ID = "flaviusdemian91-facilitator@yahoo.com";
	public static final String COUNTRIES_ASSETS = "countries-languages/";
	public static final String USER_MANUAL_ASSETS = "user-manual/";
	public static final int CROUTON_LENGTH = 20;
	public static final int NOTIFICATION_LENGTH = 20;
	public static final int API_LIMIT_PARAMETER_VALUE = 300;
	public static final double DEFAULT_CREDIT = 100;
	public static final double DEFAULT_RATING = 1;
	public static final int DESCRIPTION_LIMITATION_LENGTH = 300;
	public static final String LOCATION_NOT_SET = "Location not set.";
	public static final String UNKNOWN_COUNTRY = "Unknown country";
	public static final String COIN_PRICE = "1";
	public static final int MAXIMUM_PAYPAL_AMOUNT_PER_DEPOSIT = 1000;
	public static final int MAXIMUM_WEBSOCKET_RECONNECT_NUMBER = 10;
	public static final int MINIMUM_ENTRIES_ON_THE_SCREEEN = 8;

	
	public static final String COMPLETE_USER_PROFILE_METHOD = "complete_user_profile";
	public static final String USERS_METHOD = "users";

	//////////////////////////////////// END APPLICATION VALUES ///////////////////////////////
}