using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;
using demo_flavius_Core.Interfaces;

namespace demoflavius_W8.Helpers
{
    public class NavigationService : INavigationService
    {
        public void Navigate(Type sourcePageType)
        {
            ((Frame)Window.Current.Content).Navigate(sourcePageType);
        }

        public void Navigate(Type sourcePageType, object parameter)
        {
            ((Frame)Window.Current.Content).Navigate(sourcePageType, parameter);
        }

        //public void NavigateContentFrame(string externalLink)
        //{
        //    MainPage.Current.LoadContentInFrame(externalLink);
        //}

        //public void NavigateContentFrame(Type sourcePageType)
        //{
        //    MainPage.Current.LoadContentInFrame(sourcePageType);
        //}

        //public void NavigateContentFrame(Type sourcePageType, object parameter)
        //{
        //    MainPage.Current.LoadContentInFrame(sourcePageType, parameter);
        //}

        public void GoBack()
        {
            ((Frame)Window.Current.Content).GoBack();
        }
    }
}
