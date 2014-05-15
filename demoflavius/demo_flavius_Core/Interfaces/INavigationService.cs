using System;

namespace demo_flavius_Core.Interfaces
{
    public interface INavigationService
    {
        void Navigate(Type sourcePageType);
        void Navigate(Type sourcePageType, object parameter);
        //void NavigateContentFrame(String externalLink);
        //void NavigateContentFrame(Type sourcePageType);
        //void NavigateContentFrame(Type sourcePageType, object parameter);
        void GoBack();
    }
}