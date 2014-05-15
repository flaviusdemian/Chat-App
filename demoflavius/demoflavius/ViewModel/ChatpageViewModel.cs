using System;
using System.Linq;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using Coding4Fun.Toolkit.Controls;
using demoflavius_W8.Models;
using demo_flavius_Core.Interfaces;
using demo_flavius_Core.Models;
using GalaSoft.MvvmLight;
using GalaSoft.MvvmLight.Command;
using System.Threading.Tasks;
using demoflavius.Helpers;
using demo_flavius_Core.Helpers;
using Windows.UI.Core;

namespace demoflavius_W8.ViewModel
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

        private readonly CoreDispatcher dispatcher;
        private readonly IDataService _dataService;
        private readonly INavigationService _navigationService;

        private readonly IDictionary<string, ObservableCollection<MessageForUI>> _chatConversations = new Dictionary<string, ObservableCollection<MessageForUI>>();

        private Friend _selectedFriend;
        private ObservableCollection<MessageForUI> _messagesViewModel = new ObservableCollection<MessageForUI>();
        private ObservableCollection<Friend> _friendsViewModel = new ObservableCollection<Friend>();

        //commands zone
        private RelayCommand _sendChatMessageCommand;
        private RelayCommand _signOutCommand;

        #endregion

        public Friend SelectedFriend
        {
            get
            {
                return _selectedFriend;
            }
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
                else
                {

                }
            }
        }

        public ObservableCollection<Friend> FriendsViewModel
        {
            get
            {
                return _friendsViewModel;
            }
            set
            {
                _friendsViewModel = value;
                RaisePropertyChanged("FriendsViewModel");
            }
        }

        public ObservableCollection<MessageForUI> MessagesViewModel
        {
            get
            {
                return _messagesViewModel;
            }
            set
            {
                _messagesViewModel = value;
                RaisePropertyChanged("MessagesViewModel");
            }
        }

        public string CurrentMessage { get; set; }

        public async void UpdateMessages(string fromId, string content)
        {
            try
            {
                await dispatcher.RunAsync(CoreDispatcherPriority.Normal, () =>
                {
                    if (_chatConversations.ContainsKey(fromId) == false)
                    {
                        _chatConversations.Add(fromId, new ObservableCollection<MessageForUI>());
                    }

                    MessageForUI receivedMessage = new MessageForUI()
                    {
                        FromId = fromId,
                        Content = content,
                        ChatBubbleDirection = Coding4Fun.Toolkit.Controls.ChatBubbleDirection.UpperLeft,
                        Alignment = Windows.UI.Xaml.HorizontalAlignment.Left
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

        public async void SetSelectedUser(string fromId)
        {
            try
            {
                if (String.IsNullOrWhiteSpace(fromId) == false)
                {
                    Friend senderFriend = FriendsViewModel.Where(friend => friend.Id == fromId).FirstOrDefault();
                    if (senderFriend != null)
                    {
                        await dispatcher.RunAsync(CoreDispatcherPriority.Normal, () =>
                        {
                            if (senderFriend != _selectedFriend)
                            {
                                SelectedFriend = senderFriend;
                            }
                            else
                            {

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

        public RelayCommand LogOutCommand
        {
            get
            {
                return _signOutCommand ??
                       (_signOutCommand = new RelayCommand(ExecuteLogOutCommand));
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

        #endregion

        /// <summary>
        ///     Initializes a new instance of the ChatpageViewModel class.
        /// </summary>
        public ChatPageViewModel(IDataService dataService, INavigationService navigationService)
        {
            _dataService = dataService;
            _navigationService = navigationService;

            dispatcher = CoreWindow.GetForCurrentThread().Dispatcher;
        }

        #region private functions

        private void ExecuteLogOutCommand()
        {
            App.MobileService.Logout();
            ApplicationDataManager.RemoveEntry(ApplicationConstants.UserKey);
            _navigationService.Navigate(typeof(LoginPage));
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
                        Alignment = Windows.UI.Xaml.HorizontalAlignment.Right,
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
        #endregion

    }
}