using System.Diagnostics.CodeAnalysis;
using demoflavius_W8.Helpers;
using demo_flavius_Core.Helpers.Design;
using demo_flavius_Core.Interfaces;
using GalaSoft.MvvmLight.Ioc;
using Microsoft.Practices.ServiceLocation;

namespace demoflavius_W8.ViewModel
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
            SimpleIoc.Default.Register<INavigationService>(() => new NavigationService());
            //}

            SimpleIoc.Default.Register<ChatPageViewModel>();
        }

        /// <summary>
        ///     Gets the Main property.
        /// </summary>
        [SuppressMessage("Microsoft.Performance",
            "CA1822:MarkMembersAsStatic",
            Justification = "This non-static member is needed for data binding purposes.")]
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