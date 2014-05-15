using System.Windows.Navigation;
using demo_flavius_Core.Helpers.Design;
using demo_flavius_Core.Interfaces;
using GalaSoft.MvvmLight.Ioc;
using Microsoft.Practices.ServiceLocation;

namespace demoflavius_WP8.ViewModel
{
    /// <summary>
    ///     This class contains static references to all the view models in the
    ///     application and provides an entry point for the bindings.
    /// </summary>
    public class ViewModelLocator
    {
        /// <summary>
        ///     Initializes a new instance of the ViewModelLocator class.
        /// </summary>
        public ViewModelLocator()
        {
            ServiceLocator.SetLocatorProvider(() => SimpleIoc.Default);

            //if (ViewModelBase.IsInDesignModeStatic)
            {
                SimpleIoc.Default.Register<IDataService, DesignDataService>();
                //SimpleIoc.Default.Register<INavigationService, Design.DesignNavigationService>();
            }
            //else
            //{
            //    SimpleIoc.Default.Register<IDataService, DataService>();
            //SimpleIoc.Default.Register<INavigationService, NavigationService>;
            //}

            SimpleIoc.Default.Register<ChatPageViewModel>();
        }

        public ChatPageViewModel Chat
        {
            get { return ServiceLocator.Current.GetInstance<ChatPageViewModel>(); }
        }

        public static void Cleanup()
        {
            // TODO Clear the ViewModels
        }
    }
}