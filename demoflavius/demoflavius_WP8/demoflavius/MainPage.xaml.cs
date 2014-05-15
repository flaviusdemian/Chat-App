using System;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using Coding4Fun.Toolkit.Controls;
using demo_flavius_Core;
using demo_flavius_Core.Models;
using Microsoft.Phone.Controls;
using Microsoft.Phone.Notification;
using Microsoft.Phone.Shell;
using Microsoft.WindowsAzure.MobileServices;
using Newtonsoft.Json;
using Microsoft.WindowsAzure.Messaging;

namespace demoflavius
{
    //    public String SessionToken { get; set; }
    //public String Content { get; set; }
    //public Enums.MessageType MessageType { get; set; }
    //public Guid FromId { get; set; }
    //public String FromUseranme { get; set; }
    //public Guid ToId { get; set; }
    //public String DeviceId { get; set; }
    //public Enums.DeviceType DeviceType { get; set; }


    public partial class MainPage : PhoneApplicationPage
    {
        // MobileServiceCollectionView implements ICollectionView (useful for databinding to lists) and 
        // is integrated with your Mobile Service to make it easy to bind your data to the ListView


        // Constructor
        public MainPage()
        {
            InitializeComponent();
            Loaded += MainPage_Loaded;
        }

        private async void InsertTodoItem(Message message)
        {
            try
            {
                // This code inserts a new Message into the database. When the operation completes
                // and Mobile Services has assigned an Id, the item is added to the CollectionView
                //await todoTable.InsertAsync(message);
                //items.Add(message);
            }
            catch (Exception ex)
            {
                ex.ToString();
            }
        }

        private async void RefreshTodoItems()
        {
            // This code refreshes the entries in the list view be querying the TodoItems table.
            // The query excludes completed TodoItems
            //try
            //{
            //    //items = await todoTable
            //    //    .Where(todoItem => todoItem.Complete == false)
            //    //    .ToCollectionAsync();
            //}
            //catch (MobileServiceInvalidOperationException e)
            //{
            //    MessageBox.Show(e.Message, "Error loading items", MessageBoxButton.OK);
            //}

            //ListItems.ItemsSource = items;
        }

        private async void UpdateCheckedTodoItem(Message item)
        {
            // This code takes a freshly completed Message and updates the database. When the MobileService 
            // responds, the item is removed from the list 
            //await todoTable.UpdateAsync(item);
            //items.Remove(item);
        }

        private void ButtonRefresh_Click(object sender, RoutedEventArgs e)
        {
            RefreshTodoItems();
        }

        private void ButtonSave_Click(object sender, RoutedEventArgs e)
        {
            var todoItem = new Message { Content = TodoInput.Text };
            InsertTodoItem(todoItem);
        }

        private void CheckBoxComplete_Checked(object sender, RoutedEventArgs e)
        {
            var cb = (CheckBox)sender;
            var item = cb.DataContext as Message;
            //item.Complete = true;
            //item.
            UpdateCheckedTodoItem(item);
        }

        protected override void OnNavigatedTo(NavigationEventArgs e)
        {
        }

        private async void MainPage_Loaded(object sender, RoutedEventArgs e)
        {
            RefreshTodoItems();
        }
    }
}