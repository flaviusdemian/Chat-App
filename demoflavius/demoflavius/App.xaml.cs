using Windows.Networking.PushNotifications;
using Windows.Security.Cryptography;
using Windows.System.Profile;
using Windows.UI.Notifications;
using demoflavius_W8.Helpers;
using demo_flavius_Core;
using demo_flavius_Core.Models;
using Microsoft.WindowsAzure.MobileServices;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Runtime.InteropServices.WindowsRuntime;
using Windows.ApplicationModel;
using Windows.ApplicationModel.Activation;
using Windows.Foundation;
using Windows.Foundation.Collections;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;
using Windows.UI.Xaml.Controls.Primitives;
using Windows.UI.Xaml.Data;
using Windows.UI.Xaml.Input;
using Windows.UI.Xaml.Media;
using Windows.UI.Xaml.Navigation;
using System.Threading.Tasks;
using System.Net.Http;
using demoflavius_W8.ViewModel;
using Microsoft.Practices.ServiceLocation;
using System.Collections.ObjectModel;
using demoflavius.Helpers;
using demo_flavius_Core.Helpers;
using Newtonsoft.Json;
using Newtonsoft.Json.Linq;
using Windows.ApplicationModel.Background;
using demoflavius_W8.Models;
using Windows.Storage;

namespace demoflavius_W8
{
    /// <summary>
    /// Provides application-specific behavior to supplement the default Application class.
    /// </summary>
    sealed partial class App : Application
    {
        // This MobileServiceClient has been configured to communicate with your Mobile Service's url
        // and application key. You're all set to start working with your Mobile Service!
        public static MobileServiceClient MobileService = new MobileServiceClient("https://demoflavius.azure-mobile.net/", "BAnpBgjwvAiseBuqYzJohLfKjwSWbD28");
        public static PushNotificationChannel Channel = null;

        public static readonly IMobileServiceTable<UserEntity> UsersTable = App.MobileService.GetTable<UserEntity>();
        public static readonly IMobileServiceTable<Message> MessagesTable = App.MobileService.GetTable<Message>();
        public static readonly IMobileServiceTable<Channel> ChannelsTable = App.MobileService.GetTable<Channel>();

        public static MobileServiceUser MobileServicesUser = null;
        public static UserEntity CurrentUser = new UserEntity();
        public static Channel CurrentChannel = new Channel();

        public static ChatPageViewModel ChatPageViewModel;
        public static ChatPage ChatPageReference { get; set; }
        /// <summary>
        /// Initializes the singleton application object.  This is the first line of authored code
        /// executed, and as such is the logical equivalent of main() or WinMain().
        /// </summary>

        DispatcherTimer timer = new DispatcherTimer();

        public static Enums.ApplicationStateType ApplicationApplicationState;
        public App()
        {
            this.InitializeComponent();
            this.Suspending += OnSuspending;
        }

        /// <summary>
        /// Invoked when the application is launched normally by the end MobileServicesUser.  Other entry points
        /// will be used such as when the application is launched to open a specific file.
        /// </summary>
        /// <param name="e">Details about the launch request and process.</param>
        protected override async void OnLaunched(LaunchActivatedEventArgs e)
        {

#if DEBUG
            if (System.Diagnostics.Debugger.IsAttached)
            {
                this.DebugSettings.EnableFrameRateCounter = true;
            }
#endif
            timer.Interval = new TimeSpan(0, 0, 0, 1);
            //timer.Tick += CheckIfAppIsSuspended();
            Frame rootFrame = Window.Current.Content as Frame;
            ApplicationApplicationState = Enums.ApplicationStateType.Active;
            // Do not repeat app initialization when the Window already has content,
            // just ensure that the window is active
            if (rootFrame == null)
            {
                // Create a Frame to act as the navigation context and navigate to the first page
                rootFrame = new Frame();
                // Set the default language
                rootFrame.Language = Windows.Globalization.ApplicationLanguages.Languages[0];

                rootFrame.NavigationFailed += OnNavigationFailed;

                if (e.PreviousExecutionState == ApplicationExecutionState.Terminated)
                {
                    //TODO: Load state from previously suspended application
                }

                // Place the frame in the current Window
                Window.Current.Content = rootFrame;
            }

            ChatPageViewModel = ServiceLocator.Current.GetInstance<ChatPageViewModel>();

            Window.Current.VisibilityChanged += CheckIfAppIsSuspended;

            //MainPage.RegisterWithMobileServices();

            // Ensure the current window is active
            Window.Current.Activate();

            RegisterBackgroundTask();

            if (rootFrame.Content == null)
            {
                // When the navigation stack isn't restored navigate to the first page,
                // configuring the new page by passing required information as a navigation
                // parameter

                Object userEntry = ApplicationDataManager.GetValue(ApplicationConstants.UserKey);
                if (userEntry != null)
                {
                    try
                    {
                        App.CurrentUser = JsonConvert.DeserializeObject<UserEntity>((String)userEntry);
                        if (App.CurrentUser != null)
                        {
                            try
                            {
                                if (CurrentUser.IdentityProvider == Enums.IdentityProvider.MicrosoftAccount)
                                {
                                    App.MobileServicesUser = await App.MobileService.LoginWithMicrosoftAccountAsync(CurrentUser.AccessToken);
                                }

                                else
                                {
                                    dynamic jToken = new JObject();
                                    IdentityProviderParser.RetrieveTokenObjectSpecificToIdentityProvider(CurrentUser.IdentityProvider, CurrentUser.AccessToken, CurrentUser.AccessTokenSecret, out jToken);
                                    App.MobileServicesUser = await App.MobileService.LoginAsync(CurrentUser.IdentityProvider.ToString(), jToken);
                                }
                                RegisterWithMobileServices(CurrentUser.IdentityProvider.ToString());
                                rootFrame.Navigate(typeof(ChatPage), e.Arguments);
                                return;
                            }
                            catch (Exception ex)
                            {
                                ex.ToString();
                            }
                        }
                    }
                    catch (Exception ex)
                    {
                        ex.ToString();
                    }
                }

                rootFrame.Navigate(typeof(LoginPage), e.Arguments);
            }
        }

        private async void RegisterBackgroundTask()
        {
            try
            {
                string TASK_NAME = "MessagesUpdaterTask";
                string TASK_ENTRY = "W8Tasks.MessagesUpdaterTask";

                var result = await BackgroundExecutionManager.RequestAccessAsync();
                if (result == BackgroundAccessStatus.AllowedMayUseActiveRealTimeConnectivity ||
                    result == BackgroundAccessStatus.AllowedWithAlwaysOnRealTimeConnectivity)
                {
                    foreach (var task in BackgroundTaskRegistration.AllTasks)
                    {
                        if (task.Value.Name == TASK_NAME)
                            task.Value.Unregister(true);
                    }

                    BackgroundTaskBuilder builder = new BackgroundTaskBuilder();
                    builder.Name = TASK_NAME;
                    builder.TaskEntryPoint = TASK_ENTRY;
                    builder.SetTrigger(new PushNotificationTrigger("W8MOBILESERVICESDEMO"));
                    var registration = builder.Register();
                    registration.Completed += RegistrationCompleted;
                }
            }
            catch (Exception ex)
            {
                ex.ToString();
            }
        }

        private void RegistrationCompleted(BackgroundTaskRegistration sender, BackgroundTaskCompletedEventArgs args)
        {
            ApplicationDataContainer container = ApplicationData.Current.LocalSettings;
            try
            {
                var content = container.Values["RawMessage"].ToString();
                if (content != null)
                {
                    ToastMessage message = JsonConvert.DeserializeObject<ToastMessage>(content);
                    ToastHelper.DisplayTextToast(ToastTemplateType.ToastImageAndText01, message.FromId, message.Content, message.FromPicture);
                }
            }
            catch (Exception ex)
            {
                ex.ToString();
            }
        }

        private void CheckIfAppIsSuspended(object sender, Windows.UI.Core.VisibilityChangedEventArgs e)
        {
            if (Window.Current.Visible == true)
            {
                if (ApplicationApplicationState != Enums.ApplicationStateType.Active)
                {
                    ApplicationApplicationState = Enums.ApplicationStateType.Active;
                }
            }
            else
            {
                if (ApplicationApplicationState != Enums.ApplicationStateType.Suspended)
                {
                    ApplicationApplicationState = Enums.ApplicationStateType.Suspended;
                }
            }
        }

        protected override void OnActivated(IActivatedEventArgs args)
        {
            base.OnActivated(args);
            int x = 0;
            x++;
        }

        /// <summary>
        /// Invoked when Navigation to a certain page fails
        /// </summary>
        /// <param name="sender">The Frame which failed navigation</param>
        /// <param name="e">Details about the navigation failure</param>
        void OnNavigationFailed(object sender, NavigationFailedEventArgs e)
        {
            throw new Exception("Failed to load Page " + e.SourcePageType.FullName);
        }

        /// <summary>
        /// Invoked when application execution is being suspended.  Application state is saved
        /// without knowing whether the application will be terminated or resumed with the contents
        /// of memory still intact.
        /// </summary>
        /// <param name="sender">The source of the suspend request.</param>
        /// <param name="e">Details about the suspend request.</param>
        private void OnSuspending(object sender, SuspendingEventArgs e)
        {
            ApplicationApplicationState = Enums.ApplicationStateType.Suspended;
            var deferral = e.SuspendingOperation.GetDeferral();
            //TODO: Save application state and stop any background activity
            deferral.Complete();
        }


        #region push notification area

        private static void NotificationReceived(PushNotificationChannel sender, PushNotificationReceivedEventArgs args)
        {
            try
            {
                String notificationContent = String.Empty;

                switch (args.NotificationType)
                {
                    case PushNotificationType.Badge:
                        notificationContent = args.BadgeNotification.Content.GetXml();
                        break;

                    case PushNotificationType.Tile:
                        notificationContent = args.TileNotification.Content.GetXml();
                        break;

                    case PushNotificationType.Toast:
                        notificationContent = args.ToastNotification.Content.GetXml();
                        break;

                    case PushNotificationType.Raw:
                        notificationContent = args.RawNotification.Content;
                        break;
                }

                ToastMessage message = JsonConvert.DeserializeObject<ToastMessage>(notificationContent);
                if (App.ApplicationApplicationState == Enums.ApplicationStateType.Active)
                {
                    args.Cancel = true;
                    try
                    {
                        if (ChatPageViewModel.SelectedFriend.Id != message.FromId)
                        {
                            ToastHelper.DisplayTextToast(ToastTemplateType.ToastImageAndText01, message.FromId, message.Content, message.FromPicture);
                        }
                    }
                    catch (Exception ex)
                    {
                        ex.ToString();
                    }
                }
                else
                {

                }
                ChatPageViewModel chatPageViewModel = ServiceLocator.Current.GetInstance<ChatPageViewModel>();
                chatPageViewModel.UpdateMessages(message.FromId, message.Content);

            }
            catch (Exception ex)
            {
                ex.ToString();
            }
        }

        public static async void RegisterWithMobileServices(string provider)
        {
            App.CurrentUser.Id = Guid.NewGuid().ToString();
            App.CurrentUser.ProviderIdLong = App.MobileServicesUser.UserId;
            App.CurrentUser.ProviderIdShort = IdentityProviderParser.GetShortProvider(App.CurrentUser.ProviderIdLong);
            App.CurrentUser.Token = App.MobileServicesUser.MobileServiceAuthenticationToken;
            App.CurrentUser.IdentityProvider = IdentityProviderConverter.GetProvider(provider);

            string registrationId = String.Empty;
            try
            {
                Channel = await PushNotificationChannelManager.CreatePushNotificationChannelForApplicationAsync();
                Channel.PushNotificationReceived += NotificationReceived;
                HardwareToken token = HardwareIdentification.GetPackageSpecificToken(null);
                registrationId = CryptographicBuffer.EncodeToBase64String(token.Id);
                CurrentChannel.Id = Guid.NewGuid().ToString();
                CurrentChannel.ChannelUri = Channel.Uri;
                CurrentChannel.DeviceType = Enums.DeviceType.Windows8;
                CurrentChannel.UserId = CurrentUser.ProviderIdLong;
                CurrentChannel.RegistrationId = registrationId;
            }
            catch (Exception ex)
            {
                HandleInsertChannelException(ex);
            }
            try
            {
                ApplicationDataManager.StoreValue(ApplicationConstants.UserKey, App.CurrentUser);
                await UsersTable.InsertAsync(App.CurrentUser);
                await ChannelsTable.InsertAsync(App.CurrentChannel);
            }
            catch (Exception ex)
            {
                HandleInsertChannelException(ex);
            }

            CompleteUserProfile();
            RetrieveFriends();
        }

        private async static void CompleteUserProfile()
        {
            try
            {
                UserEntityProfileCompletion result = await App.MobileService.InvokeApiAsync<UserEntityProfileCompletion>(ApplicationConstants.COMPLETE_USER_PROFILE_METHOD, HttpMethod.Get, null);
                if (result != null)
                {
                    App.CurrentUser.Name = result.Name;
                    App.CurrentUser.Picture = result.Picture;
                    App.CurrentUser.AccessToken = result.AccessToken;
                    App.CurrentUser.AccessTokenSecret = result.AccessTokenSecret;
                    ApplicationDataManager.StoreValue(ApplicationConstants.UserKey, App.CurrentUser);
                }
            }
            catch (Exception ex)
            {
                HandleInsertChannelException(ex);
            }
        }

        private async static void RetrieveFriends()
        {
            try
            {
                Dictionary<String, String> query = new Dictionary<string, string>();
                query.Add("userId", CurrentUser.ProviderIdLong);
                List<Friend> friends = await App.MobileService.InvokeApiAsync<List<Friend>>(ApplicationConstants.GET_USERS_METHOD, HttpMethod.Get, query);
                if (friends != null)
                {
                    ChatPageViewModel chatPageViewModel = ServiceLocator.Current.GetInstance<ChatPageViewModel>();
                    chatPageViewModel.FriendsViewModel = new ObservableCollection<Friend>(friends);
                    chatPageViewModel.SelectedFriend = friends[0];
                }
            }
            catch (Exception ex)
            {
                HandleInsertChannelException(ex);
            }
        }

        private static void HandleInsertChannelException(Exception ex)
        {
            int x = 0;
            x++;
        }

        #endregion

    }
}
