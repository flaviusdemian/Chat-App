using System;
using System.Threading.Tasks;
using System.Windows;
using demo_flavius_Core.Helpers;
using Microsoft.Phone.Controls;
using Microsoft.WindowsAzure.MobileServices;

namespace demoflavius
{
    public partial class LoginPage : PhoneApplicationPage
    {
        public LoginPage()
        {
            InitializeComponent();
        }

        public async Task Authenticate(MobileServiceAuthenticationProvider provider)
        {
            while (App.MobileServicesUser == null)
            {
                string message = null;
                try
                {
                    App.MobileServicesUser = await App.MobileService.LoginAsync(provider);

                    App.RegisterWithMobileServices(provider.ToString());
                    NavigationService.Navigate(new Uri("/ChatPage.xaml", UriKind.Relative));
                }
                catch (InvalidOperationException ex)
                {
                    ex.ToString();
                    message = "You must log in. Login Required";
                }
                if (message != null)
                {
                    MessageBox.Show(message);
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