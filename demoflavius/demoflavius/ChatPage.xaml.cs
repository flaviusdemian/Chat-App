using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.IO;
using System.Linq;
using System.Runtime.InteropServices.WindowsRuntime;
using System.Windows.Input;
using Windows.Foundation;
using Windows.Foundation.Collections;
using Windows.UI.ViewManagement;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;
using Windows.UI.Xaml.Controls.Primitives;
using Windows.UI.Xaml.Data;
using Windows.UI.Xaml.Input;
using Windows.UI.Xaml.Media;
using Windows.UI.Xaml.Navigation;

// The Split Page item template is documented at http://go.microsoft.com/fwlink/?LinkId=234234
using Coding4Fun.Toolkit.Controls;
using demoflavius_W8.Models;
using demoflavius_W8.ViewModel;
using demo_flavius_Core;
using demo_flavius_Core.Models;
using Microsoft.Practices.ServiceLocation;

namespace demoflavius_W8
{
    /// <summary>
    /// A page that displays a group title, a list of items within the group, and details for
    /// the currently selected item.
    /// </summary>
    public sealed partial class ChatPage : Page
    {
        public ChatPage()
        {
            this.InitializeComponent();

            App.ChatPageReference = this;
        }

        public void ScrollToLastElement()
        {
            try
            {
                MessageForUI message = App.ChatPageViewModel.MessagesViewModel.LastOrDefault();
                if (message != null)
                {
                    chatMessages.ScrollIntoView(message);
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
