using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Linq;
using System.Windows;
using Coding4Fun.Toolkit.Controls;
using demoflavius;
using demoflavius.Models;
using demoflavius_WP8.Helpers;
using demo_flavius_Core.Helpers;
using demo_flavius_Core.Interfaces;
using demo_flavius_Core.Models;
using GalaSoft.MvvmLight;
using GalaSoft.MvvmLight.Command;
using Microsoft.Phone.Controls;

namespace demoflavius_WP8.ViewModel
{
    /// <summary>
    ///     This class contains properties that the main View can data bind to.
    ///     <para>
    ///         Use the <strong>mvvminpc</strong> snippet to add bindable properties to this ViewModel.
    ///     </para>
    ///     <para>
    ///         You can also use Blend to data bind with the tool's support.
    ///     </para>
    ///     <para>
    ///         See http://www.galasoft.ch/mvvm
    ///     </para>
    /// </summary>
    public class ChatPageViewModel : ViewModelBase
    {
        #region private members

        private readonly IDataService _dataService;

        private readonly IDictionary<string, ObservableCollection<MessageForUI>> _chatConversations =
            new Dictionary<string, ObservableCollection<MessageForUI>>();

        private Friend _selectedFriend;
        private ObservableCollection<MessageForUI> _messagesViewModel = new ObservableCollection<MessageForUI>();
        private ObservableCollection<Friend> _friendsViewModel = new ObservableCollection<Friend>();

        //commands zone
        private RelayCommand _loadApplicationCommand;
        private RelayCommand _sendChatMessageCommand;
        private RelayCommand<object> _loadSelectedFriendCommand;
        private RelayCommand _signOutCommand;

        #endregion

        public Friend SelectedFriend
        {
            get { return _selectedFriend; }
            set
            {
                if (_selectedFriend != value)
                {
                    _selectedFriend = value;
                    RaisePropertyChanged("SelectedFriend");
                    if (_chatConversations.ContainsKey(_selectedFriend.Id) == false)
                    {
                        _chatConversations.Add(_selectedFriend.Id, new ObservableCollection<MessageForUI>());
                    }
                    MessagesViewModel = _chatConversations[_selectedFriend.Id];
                }
            }
        }

        public ObservableCollection<Friend> FriendsViewModel
        {
            get { return _friendsViewModel; }
            set
            {
                _friendsViewModel = value;
                RaisePropertyChanged("FriendsViewModel");
            }
        }

        public ObservableCollection<MessageForUI> MessagesViewModel
        {
            get { return _messagesViewModel; }
            set
            {
                _messagesViewModel = value;
                RaisePropertyChanged("MessagesViewModel");
            }
        }

        public string CurrentMessage { get; set; }

        public void UpdateMessages(string fromId, string content)
        {
            try
            {
                Deployment.Current.Dispatcher.BeginInvoke(() =>
                {
                    if (_chatConversations.ContainsKey(fromId) == false)
                    {
                        _chatConversations.Add(fromId, new ObservableCollection<MessageForUI>());
                    }

                    var receivedMessage = new MessageForUI
                    {
                        FromId = fromId,
                        Content = content,
                        ChatBubbleDirection = ChatBubbleDirection.UpperLeft,
                        Alignment = HorizontalAlignment.Left
                    };
                    _chatConversations[fromId].Add(receivedMessage);
                    if (App.ChatPageReference != null)
                    {
                        App.ChatPageReference.ScrollToLastElement();
                        App.ChatPageReference.PlayChatSound();
                    }
                });
            }
            catch (Exception ex)
            {
                ex.ToString();
            }
        }

        public void SetSelectedUser(string fromId)
        {
            try
            {
                if (String.IsNullOrWhiteSpace(fromId) == false)
                {
                    Friend senderFriend = FriendsViewModel.Where(friend => friend.Id == fromId).FirstOrDefault();
                    if (senderFriend != null)
                    {
                        Deployment.Current.Dispatcher.BeginInvoke(() =>
                        {
                            if (senderFriend != _selectedFriend)
                            {
                                SelectedFriend = senderFriend;
                            }
                        });
                    }
                }
            }
            catch (Exception ex)
            {
                ex.ToString();
            }
        }

        #region commands

        public RelayCommand LoadApplicationCommand
        {
            get
            {
                return _loadApplicationCommand ??
                       (_loadApplicationCommand = new RelayCommand(ExecuteLoadApplicationCommand));
            }
        }

        public RelayCommand<object> LoadSelectedFriendCommand
        {
            get
            {
                return _loadSelectedFriendCommand ??
                       (_loadSelectedFriendCommand =
                           new RelayCommand<object>(param => ExecuteLoadSelectedFriendCommand(param)));
            }
        }

        public RelayCommand SendChatMessageCommand
        {
            get
            {
                return _sendChatMessageCommand ??
                       (_sendChatMessageCommand = new RelayCommand(ExecuteSendChatMessageCommand));
            }
        }

        public RelayCommand LogOutCommand
        {
            get
            {
                return _signOutCommand ??
                       (_signOutCommand = new RelayCommand(ExecuteLogOutCommand));
            }
        }

        #endregion

        /// <summary>
        ///     Initializes a new instance of the ChatpageViewModel class.
        /// </summary>
        public ChatPageViewModel(IDataService dataService)
        {
            _dataService = dataService;

            #region init

            ////if (IsInDesignMode)
            ////{
            ////    // Code runs in Blend --> create design time data.
            ////}
            ////else
            ////{
            ////    // Code runs "for real"
            ////}

            #endregion

#if DEBUG
            CreateDesignTimeData();
#endif
        }

        #region private functions

        private void ExecuteLoadApplicationCommand()
        {
            #region Messages

            MessagesViewModel = new ObservableCollection<MessageForUI>();

            #endregion messages
        }

        private async void ExecuteSendChatMessageCommand()
        {
            string content = CurrentMessage;
            if (String.IsNullOrWhiteSpace(content) == false)
            {
                try
                {
                    ObservableCollection<MessageForUI> messages = _chatConversations[_selectedFriend.Id];
                    if (messages == null)
                    {
                        throw new Exception("null");
                    }

                    messages.Add(new MessageForUI
                    {
                        ChatBubbleDirection = ChatBubbleDirection.LowerRight,
                        Alignment = HorizontalAlignment.Right,
                        FromId = App.CurrentUser.ProviderIdLong,
                        Content = content
                    });

                    if (App.ChatPageReference != null)
                    {
                        App.ChatPageReference.ScrollToLastElement();
                    }

                    MessagesViewModel = messages;

                    var message = new Message
                    {
                        Id = Guid.NewGuid().ToString(),
                        Content = content,
                        DeviceType = Enums.DeviceType.Windows8,
                        FromId = App.CurrentUser.ProviderIdLong,
                        FromName = App.CurrentUser.Name,
                        FromPicture = App.CurrentUser.Picture,
                        ToId = _selectedFriend.Id
                    };

                    CurrentMessage = String.Empty;
                    RaisePropertyChanged("CurrentMessage");

                    try
                    {
                        await App.MessagesTable.InsertAsync(message);
                    }
                    catch (Exception ex)
                    {
                        ex.ToString();
                    }
                }
                catch (Exception ex)
                {
                    ex.ToString();
                }
            }
        }

        private void ExecuteLogOutCommand()
        {
            App.MobileService.Logout();
            ApplicationDataManager.RemoveEntry(ApplicationConstants.UserKey);
            (Application.Current.RootVisual as PhoneApplicationFrame).Navigate(new Uri("/LoginPage.xaml", UriKind.Relative));
        }

        private void ExecuteLoadSelectedFriendCommand(Object selectedItem)
        {
            SelectedFriend = (Friend) selectedItem;
            App.ChatPageReference.OpenClose_Right(null, null);
        }

#if DEBUG
        private void CreateDesignTimeData()
        {
            //if (IsInDesignMode)
            {
                LoadApplicationCommand.Execute(null);
            }
        }
#endif

        #endregion
    }
}