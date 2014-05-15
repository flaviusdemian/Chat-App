using System;
using System.Diagnostics;
using System.Resources;
using System.Windows;
using System.Windows.Markup;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using Coding4Fun.Toolkit.Controls;
using demoflavius.Resources;
using demo_flavius_Core.Models;
using Microsoft.Phone.Controls;
using Microsoft.Phone.Shell;
using Microsoft.WindowsAzure.Messaging;
using Microsoft.WindowsAzure.MobileServices;
using Microsoft.Phone.Notification;
using demo_flavius_Core;
using System.Net.Http;
using System.Threading.Tasks;
using System.Collections.Generic;
using Microsoft.Practices.ServiceLocation;
using System.Collections.ObjectModel;
using demo_flavius_Core.Helpers;
using demoflavius_WP8.Helpers;
using demoflavius_WP8.ViewModel;
using Newtonsoft.Json;
using Newtonsoft.Json.Linq;
using Windows.Foundation;
using System.Windows.Input;

namespace demoflavius
{
    public partial class App : Application
    {
        /// <summary>
        /// Provides easy access to the root frame of the Phone Application.
        /// </summary>
        /// <returns>The root frame of the Phone Application.</returns>
        public static PhoneApplicationFrame RootFrame { get; private set; }

        public static MobileServiceClient MobileService = new MobileServiceClient("https://demoflavius.azure-mobile.net/", "BAnpBgjwvAiseBuqYzJohLfKjwSWbD28");
        public static HttpNotificationChannel Channel { get; set; }

        public static readonly IMobileServiceTable<UserEntity> UsersTable = App.MobileService.GetTable<UserEntity>();
        public static readonly IMobileServiceTable<Message> MessagesTable = App.MobileService.GetTable<Message>();
        public static readonly IMobileServiceTable<Channel> ChannelsTable = App.MobileService.GetTable<Channel>();

        //private MobileServiceCollection<Message, Message> items;
        public static MobileServiceUser MobileServicesUser = null;
        public static UserEntity CurrentUser = new UserEntity();
        public static Channel CurrentChannel = new Channel();

        public static ChatPageViewModel ChatPageViewModel;
        public static ChatPage ChatPageReference { get; set; }
        /// <summary>
        /// Constructor for the Application object.
        /// </summary>
        public App()
        {
            // Global handler for uncaught exceptions.
            UnhandledException += Application_UnhandledException;

            // Standard XAML initialization
            InitializeComponent();

            // Phone-specific initialization
            InitializePhoneApplication();

            // Language display initialization
            InitializeLanguage();

            // Show graphics profiling information while debugging.
            if (Debugger.IsAttached)
            {
                // Display the current frame rate counters.
                Application.Current.Host.Settings.EnableFrameRateCounter = true;

                // Show the areas of the app that are being redrawn in each frame.
                //Application.Current.Host.Settings.EnableRedrawRegions = true;

                // Enable non-production analysis visualization mode,
                // which shows areas of a page that are handed off to GPU with a colored overlay.
                //Application.Current.Host.Settings.EnableCacheVisualization = true;

                // Prevent the screen from turning off while under the debugger by disabling
                // the application's idle detection.
                // Caution:- Use this under debug mode only. Application that disables MobileServicesUser idle detection will continue to run
                // and consume battery power when the MobileServicesUser is not using the phone.
                PhoneApplicationService.Current.UserIdleDetectionMode = IdleDetectionMode.Disabled;
            }

            ChatPageViewModel = ServiceLocator.Current.GetInstance<ChatPageViewModel>();

        }

        // Code to execute when the application is launching (eg, from Start)
        // This code will not execute when the application is reactivated
        private async void Application_Launching(object sender, LaunchingEventArgs e)
        {

            //}
        }

        // Code to execute when the application is activated (brought to foreground)
        // This code will not execute when the application is first launched
        private void Application_Activated(object sender, ActivatedEventArgs e)
        {
            //Object userEntry = ApplicationDataManager.GetValue(ApplicationConstants.UserKey);
            //if (userEntry != null)
            //{
            //    try
            //    {
            //        App.CurrentUser = JsonConvert.DeserializeObject<UserEntity>((String)userEntry);
            //        if (App.CurrentUser != null)
            //        {
            //            RegisterWithMobileServices();
            //            rootFrame.Navigate(typeof(ChatPage), e.Arguments);
            //        }
            //    }
            //    catch (Exception ex)
            //    {
            //        ex.ToString();
            //    }
            //}

            //rootFrame.Navigate(typeof(LoginPage), e.Arguments);
        }

        // Code to execute when the application is deactivated (sent to background)
        // This code will not execute when the application is closing
        private void Application_Deactivated(object sender, DeactivatedEventArgs e)
        {
        }

        // Code to execute when the application is closing (eg, MobileServicesUser hit Back)
        // This code will not execute when the application is deactivated
        private void Application_Closing(object sender, ClosingEventArgs e)
        {
        }

        // Code to execute if a navigation fails
        private void RootFrame_NavigationFailed(object sender, NavigationFailedEventArgs e)
        {
            if (Debugger.IsAttached)
            {
                // A navigation has failed; break into the debugger
                Debugger.Break();
            }
        }

        // Code to execute on Unhandled Exceptions
        private void Application_UnhandledException(object sender, ApplicationUnhandledExceptionEventArgs e)
        {
            if (Debugger.IsAttached)
            {
                // An unhandled exception has occurred; break into the debugger
                Debugger.Break();
            }
        }

        #region Phone application initialization

        // Avoid double-initialization
        private bool phoneApplicationInitialized = false;

        // Do not add any additional code to this method
        private void InitializePhoneApplication()
        {
            if (phoneApplicationInitialized)
                return;

            // Create the frame but don't set it as RootVisual yet; this allows the splash
            // screen to remain active until the application is ready to render.
            RootFrame = new PhoneApplicationFrame();
            RootFrame.Navigated += CompleteInitializePhoneApplication;

            // Handle navigation failures
            RootFrame.NavigationFailed += RootFrame_NavigationFailed;

            // Handle reset requests for clearing the backstack
            RootFrame.Navigated += CheckForResetNavigation;

            // Ensure we don't initialize again
            phoneApplicationInitialized = true;
        }

        // Do not add any additional code to this method
        private void CompleteInitializePhoneApplication(object sender, NavigationEventArgs e)
        {
            // Set the root visual to allow the application to render
            if (RootVisual != RootFrame)
                RootVisual = RootFrame;

            // Remove this handler since it is no longer needed
            RootFrame.Navigated -= CompleteInitializePhoneApplication;
        }

        private void CheckForResetNavigation(object sender, NavigationEventArgs e)
        {
            // If the app has received a 'reset' navigation, then we need to check
            // on the next navigation to see if the page stack should be reset
            if (e.NavigationMode == NavigationMode.Reset)
                RootFrame.Navigated += ClearBackStackAfterReset;
        }

        private void ClearBackStackAfterReset(object sender, NavigationEventArgs e)
        {
            // Unregister the event so it doesn't get called again
            RootFrame.Navigated -= ClearBackStackAfterReset;

            // Only clear the stack for 'new' (forward) and 'refresh' navigations
            if (e.NavigationMode != NavigationMode.New && e.NavigationMode != NavigationMode.Refresh)
                return;

            // For UI consistency, clear the entire page stack
            while (RootFrame.RemoveBackEntry() != null)
            {
                ; // do nothing
            }
        }

        #endregion

        // Initialize the app's font and flow direction as defined in its localized resource strings.
        //
        // To ensure that the font of your application is aligned with its supported languages and that the
        // FlowDirection for each of those languages follows its traditional direction, ResourceLanguage
        // and ResourceFlowDirection should be initialized in each resx file to match these values with that
        // file's culture. For example:
        //
        // AppResources.es-ES.resx
        //    ResourceLanguage's value should be "es-ES"
        //    ResourceFlowDirection's value should be "LeftToRight"
        //
        // AppResources.ar-SA.resx
        //     ResourceLanguage's value should be "ar-SA"
        //     ResourceFlowDirection's value should be "RightToLeft"
        //
        // For more info on localizing Windows Phone apps see http://go.microsoft.com/fwlink/?LinkId=262072.
        //
        private void InitializeLanguage()
        {
            try
            {
                // Set the font to match the display language defined by the
                // ResourceLanguage resource string for each supported language.
                //
                // Fall back to the font of the neutral language if the Display
                // language of the phone is not supported.
                //
                // If a compiler error is hit then ResourceLanguage is missing from
                // the resource file.
                RootFrame.Language = XmlLanguage.GetLanguage(AppResources.ResourceLanguage);

                // Set the FlowDirection of all elements under the root frame based
                // on the ResourceFlowDirection resource string for each
                // supported language.
                //
                // If a compiler error is hit then ResourceFlowDirection is missing from
                // the resource file.
                FlowDirection flow = (FlowDirection)Enum.Parse(typeof(FlowDirection), AppResources.ResourceFlowDirection);
                RootFrame.FlowDirection = flow;
            }
            catch
            {
                // If an exception is caught here it is most likely due to either
                // ResourceLangauge not being correctly set to a supported language
                // code or ResourceFlowDirection is set to a value other than LeftToRight
                // or RightToLeft.

                if (Debugger.IsAttached)
                {
                    Debugger.Break();
                }

                throw;
            }
        }



        #region push notification area

        public async static void RegisterWithMobileServices(string provider)
        {
            try
            {
                App.CurrentUser.Id = Guid.NewGuid().ToString();
                App.CurrentUser.ProviderIdLong = App.MobileServicesUser.UserId;
                App.CurrentUser.ProviderIdShort = IdentityProviderParser.GetShortProvider(App.CurrentUser.ProviderIdLong);
                App.CurrentUser.Token = App.MobileServicesUser.MobileServiceAuthenticationToken;
                App.CurrentUser.IdentityProvider = IdentityProviderConverter.GetProvider(provider);

                App.Channel = HttpNotificationChannel.Find("MyPushChannel");

                if (App.Channel == null)
                {
                    App.Channel = new HttpNotificationChannel("MyPushChannel");
                    App.Channel.Open();
                }

                if (App.Channel.ConnectionStatus == ChannelConnectionStatus.Disconnected)
                {
                    App.Channel.Open();
                }

                if (!App.Channel.IsShellToastBound)
                {
                    App.Channel.BindToShellToast();
                }
                if (!App.Channel.IsShellToastBound)
                {
                    App.Channel.BindToShellTile();
                }
                App.Channel.ShellToastNotificationReceived += ToastReceived;
                App.Channel.ChannelUriUpdated += ChannelUriUpdated;
                App.Channel.ConnectionStatusChanged += ConnectionStatusChanged;
            }
            catch (Exception ex)
            {
                HandleInsertChannelException(ex);
            }
            try
            {
                CurrentChannel.Id = Guid.NewGuid().ToString();
                CurrentChannel.DeviceType = Enums.DeviceType.WindowsPhone8;
                CurrentChannel.UserId = CurrentUser.ProviderIdLong;
                CurrentChannel.RegistrationId = String.Empty;
            }
            catch (Exception ex)
            {
                ex.ToString();
            }
            try
            {
                ApplicationDataManager.StoreValue(ApplicationConstants.UserKey, App.CurrentUser);
                await UsersTable.InsertAsync(App.CurrentUser);
            }
            catch (Exception ex)
            {
                ex.ToString();
            }
            try
            {
                if (App.Channel != null && App.Channel.ChannelUri != null)
                {
                    CurrentChannel.ChannelUri = App.Channel.ChannelUri.AbsoluteUri;
                }
                else
                {
                    int x = 0;
                    x++;
                }
                await ChannelsTable.InsertAsync(App.CurrentChannel);
            }
            catch (Exception ex)
            {
                ex.ToString();
            }
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
                ex.ToString();
            }
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
                ex.ToString();
            }
        }

        private static void ChannelUriUpdated(object sender, NotificationChannelUriEventArgs e)
        {
            if (App.Channel != null && App.Channel.ChannelUri != null)
            {
                CurrentChannel.ChannelUri = App.Channel.ChannelUri.AbsoluteUri;
            }
        }

        private static void ConnectionStatusChanged(object sender, NotificationChannelConnectionEventArgs e)
        {
            if (App.Channel != null && App.Channel.ChannelUri != null)
            {
                CurrentChannel.ChannelUri = App.Channel.ChannelUri.AbsoluteUri;
            }
        }

        private static void ToastReceived(object sender, NotificationEventArgs e)
        {
            string path = String.Empty;
            ToastPrompt toast = null;
            string externalImagePath = null;
            if (e.Collection.Keys.Count >= 3)
            {
                try
                {
                    string content = e.Collection["wp:Text1"];
                    string extraContent = e.Collection["wp:Param"];
                    String senderId = RetrieveSenderId(extraContent);

                    if (String.IsNullOrWhiteSpace(senderId) == false)
                    {
                        ToastMessage message = new ToastMessage()
                        {
                            Content = content,
                            FromPicture = externalImagePath,
                            FromId = senderId
                        };

                        Deployment.Current.Dispatcher.BeginInvoke(() =>
                        {
                            if (ChatPageViewModel.SelectedFriend.Id != message.FromId)
                            {
                                ShowToastMessage(e.Collection, extraContent, content, senderId);
                            }
                            else
                            {

                            }
                            ChatPageViewModel.UpdateMessages(message.FromId, message.Content);
                        });
                    }
                    else
                    {
                        int x = 0;
                        x++;
                    }
                }
                catch (Exception ex)
                {
                    ex.ToString();
                }
            }
        }

        private static void ShowToastMessage(IDictionary<String, String> collection, string extraContent, string content, string senderId)
        {
            try
            {

                ToastPrompt toast = new ToastPrompt();
                string externalImagePath;
                externalImagePath = RetrieveExternalImage(extraContent);
                if (string.IsNullOrWhiteSpace(externalImagePath) == true)
                {
                    toast.ImageSource = new BitmapImage(new Uri("../images/user.png", UriKind.RelativeOrAbsolute));
                }
                else
                {
                    toast.ImageSource = new BitmapImage(new Uri(externalImagePath, UriKind.RelativeOrAbsolute));
                }
                toast.Message = content;
                toast.Title = collection["wp:Text2"];
                toast.TextWrapping = TextWrapping.NoWrap;
                toast.Tag = senderId;
                toast.ImageWidth = 36;
                toast.ImageHeight = 36;
                toast.Tap += MessageToastTapped;
                toast.Show();
                toast.Name = collection["wp:Param"];
            }
            catch (Exception ex)
            {
                ex.ToString();
            }
        }

        private static string RetrieveSenderId(string extraContent)
        {
            try
            {
                string[] elements = extraContent.Split(new string[] { "?" }, StringSplitOptions.RemoveEmptyEntries);
                if (elements.Length == 2)
                {
                    elements = elements[1].Split(new string[] { "&" }, StringSplitOptions.RemoveEmptyEntries);
                    string value = elements[0].Split(new string[] { "=" }, StringSplitOptions.RemoveEmptyEntries)[1];
                    return value;
                }
            }
            catch (Exception ex)
            {
                ex.ToString();
            }
            return null;
        }

        private static string RetrieveExternalImage(string link)
        {
            try
            {
                string[] elements = link.Split(new string[] { "?" }, StringSplitOptions.RemoveEmptyEntries);
                if (elements.Length == 2)
                {
                    elements = elements[1].Split(new string[] { "&" }, StringSplitOptions.RemoveEmptyEntries);
                    string value = elements[2].Split(new string[] { "=" }, StringSplitOptions.RemoveEmptyEntries)[1];
                    return value;
                }
            }
            catch (Exception ex)
            {
                ex.ToString();
            }
            return null;
        }

        public static void MessageToastTapped(object sender, GestureEventArgs args)
        {
            try
            {
                if (sender is ToastPrompt)
                {
                    ToastPrompt prompt = sender as ToastPrompt;
                    ChatPageViewModel.SetSelectedUser(prompt.Tag.ToString());
                }

            }
            catch (Exception ex)
            {
                ex.ToString();
            }
        }

        public static void HandleInsertChannelException(Exception ex)
        {
            ex.ToString();
        }
        #endregion

    }
}