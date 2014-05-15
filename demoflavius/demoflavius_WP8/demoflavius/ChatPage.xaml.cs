using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Linq;
using System.Net;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Input;
using System.Windows.Media.Animation;
using System.Windows.Navigation;
using Coding4Fun.Toolkit.Controls;
using demoflavius.Resources;
using demoflavius.Models;
using demoflavius_WP8.ViewModel;
using demo_flavius_Core;
using demo_flavius_Core.Models;
using Microsoft.Phone.Controls;
using Microsoft.Phone.Shell;
using Microsoft.Practices.ServiceLocation;

namespace demoflavius
{
    public partial class ChatPage : PhoneApplicationPage
    {
        double initialPosition;
        bool _viewMoved = false;

        // Constructor
        public ChatPage()
        {
            InitializeComponent();
            VisualStateManager.GoToState(this, "Normal", false);
            // Sample code to localize the ApplicationBar
            //BuildLocalizedApplicationBar();
            //Touch.FrameReported += new TouchFrameEventHandler(Touch_FrameReported);
            App.ChatPageReference = this;
        }

        protected override void OnNavigatedTo(System.Windows.Navigation.NavigationEventArgs e)
        {
            base.OnNavigatedTo(e);

            try
            {
                string key = "object";
                if (this.NavigationContext.QueryString.ContainsKey(key) == true)
                {
                    string strVal1 = this.NavigationContext.QueryString[key];
                    int x = 0;
                    x++;
                }
            }
            catch (Exception ex)
            {
                ex.ToString();
            }
        }

        public void OpenClose_Right(object sender, RoutedEventArgs e)
        {
            var left = Canvas.GetLeft(LayoutRoot);
            if (left > -520)
            {
                //ApplicationBar.IsVisible = false;
                MoveViewWindow(-840);
            }
            else
            {
                //ApplicationBar.IsVisible = true;
                MoveViewWindow(-420);
            }
        }

        void MoveViewWindow(double left)
        {
            _viewMoved = true;
            //if (left == -420)
            //    ApplicationBar.IsVisible = true;
            //else
            //    ApplicationBar.IsVisible = false;

            ((Storyboard)canvas.Resources["moveAnimation"]).SkipToFill();
            ((DoubleAnimation)((Storyboard)canvas.Resources["moveAnimation"]).Children[0]).To = left;
            ((Storyboard)canvas.Resources["moveAnimation"]).Begin();
        }

        private void canvas_ManipulationDelta(object sender, ManipulationDeltaEventArgs e)
        {
            if (e.DeltaManipulation.Translation.X != 0)
                Canvas.SetLeft(LayoutRoot, Math.Min(Math.Max(-840, Canvas.GetLeft(LayoutRoot) + e.DeltaManipulation.Translation.X), 0));
        }


        private void canvas_ManipulationStarted(object sender, ManipulationStartedEventArgs e)
        {
            _viewMoved = false;
            initialPosition = Canvas.GetLeft(LayoutRoot);
        }

        private void canvas_ManipulationCompleted(object sender, ManipulationCompletedEventArgs e)
        {
            var left = Canvas.GetLeft(LayoutRoot);
            if (_viewMoved)
                return;
            if (Math.Abs(initialPosition - left) < 100)
            {
                //bouncing back
                MoveViewWindow(initialPosition);
                return;
            }
            //change of state
            if (initialPosition - left > 0)
            {
                //slide to the left
                if (initialPosition > -420)
                    MoveViewWindow(-420);
                else
                    MoveViewWindow(-840);
            }
            else
            {
                //slide to the right
                if (initialPosition < -420)
                    MoveViewWindow(-420);
                else
                    MoveViewWindow(0);
            }

        }

        private void LogOutAction(object sender, EventArgs e)
        {
            App.MobileService.Logout();
            NavigationService.Navigate(new Uri("/LoginPage.xaml", UriKind.Relative));
        }

        public void ScrollToLastElement()
        {
            try
            {
                MessageForUI message = App.ChatPageViewModel.MessagesViewModel.LastOrDefault();
                if (message != null)
                {
                    chatMessages.ScrollTo(message);
                }
            }
            catch (Exception ex)
            {
                ex.ToString();
            }
        }

        public void PlayChatSound()
        {
            try
            {
                this.ChatMediaElement.Play();
            }
            catch (Exception ex)
            {
                ex.ToString();
            }
        }
    }
}