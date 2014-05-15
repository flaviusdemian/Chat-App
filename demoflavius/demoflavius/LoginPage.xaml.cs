using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Runtime.InteropServices.WindowsRuntime;
using System.Threading.Tasks;
using Windows.ApplicationModel.Appointments;
using Windows.Foundation;
using Windows.Foundation.Collections;
using Windows.Networking.PushNotifications;
using Windows.Security.Cryptography;
using Windows.System.Profile;
using Windows.UI.Popups;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;
using Windows.UI.Xaml.Controls.Primitives;
using Windows.UI.Xaml.Data;
using Windows.UI.Xaml.Input;
using Windows.UI.Xaml.Media;
using Windows.UI.Xaml.Navigation;

// The Blank Page item template is documented at http://go.microsoft.com/fwlink/?LinkId=234238
using demo_flavius_Core;
using demo_flavius_Core.Helpers;
using Microsoft.WindowsAzure.MobileServices;

namespace demoflavius_W8
{
    /// <summary>
    /// An empty page that can be used on its own or navigated to within a Frame.
    /// </summary>
    public sealed partial class LoginPage : Page
    {
        public LoginPage()
        {
            this.InitializeComponent();
        }


        private async Task Authenticate(MobileServiceAuthenticationProvider provider)
        {
            while (App.MobileServicesUser == null)
            {
                string message = null;
                try
                {
                    App.MobileServicesUser = await App.MobileService.LoginAsync(provider);
                    App.RegisterWithMobileServices(provider.ToString());
                    this.Frame.Navigate(typeof(ChatPage));
                }
                catch (InvalidOperationException ex)
                {
                    ex.ToString();
                    message = "You must log in. LoginPage Required";
                }
                if (message != null)
                {
                    var dialog = new MessageDialog(message);
                    dialog.Commands.Add(new UICommand("OK"));
                    await dialog.ShowAsync();
                }
            }
        }

        private async void LoginWithFacebook(object sender, RoutedEventArgs e)
        {
            await Authenticate(MobileServiceAuthenticationProvider.Facebook);
        }

        private async void LoginWithGoogle(object sender, RoutedEventArgs e)
        {
            await Authenticate(MobileServiceAuthenticationProvider.Google);
        }

        private async void LoginWithMicrosoft(object sender, RoutedEventArgs e)
        {
            await Authenticate(MobileServiceAuthenticationProvider.MicrosoftAccount);
        }

        private async void LoginWithTwitter(object sender, RoutedEventArgs e)
        {
            await Authenticate(MobileServiceAuthenticationProvider.Twitter);
        }
    }
}
