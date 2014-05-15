using System;
using demo_flavius_Core.Interfaces;

namespace demo_flavius_W8.Interfaces
{
    public class DesignNavigationService : INavigationService
    {
        // This class doesn't perform navigation, in order
        // to avoid issues in the designer at design time.

        public void Navigate(Type sourcePageType)
        {
        }

        public void Navigate(Type sourcePageType, object parameter)
        {
        }

        public void NavigateContentFrame(string externalLink)
        {
        }

        public void NavigateContentFrame(Type sourcePageType)
        {
        }

        public void NavigateContentFrame(Type sourcePageType, object parameter)
        {
        }

        public void GoBack()
        {
        }
    }
}