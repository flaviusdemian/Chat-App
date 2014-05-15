using demo_flavius_Core.Helpers;
using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Windows.ApplicationModel.Background;
using Windows.Networking.PushNotifications;
using Windows.Storage;
using Windows.UI.Notifications;

namespace W8Tasks
{
    public sealed class MessagesUpdaterTask : IBackgroundTask
    {
        public void Run(IBackgroundTaskInstance taskInstance)
        {
            BackgroundTaskDeferral _deferral = taskInstance.GetDeferral();

            // Get the background task details
            ApplicationDataContainer settings = ApplicationData.Current.LocalSettings;
            string taskName = taskInstance.Task.Name;


            Debug.WriteLine("Background " + taskName + " starting...");
            taskInstance.Canceled += TaskInstanceCanceled;


            // Store the content received from the notification so it can be retrieved from the UI.
            RawNotification notification = (RawNotification)taskInstance.TriggerDetails;

            ApplicationDataContainer container = ApplicationData.Current.LocalSettings;
            container.Values["RawMessage"] = notification.Content.ToString();

            //SendToastNotification(notification.Content);

            Debug.WriteLine("Background " + taskName + " completed!");

            _deferral.Complete();
        }

        private void TaskInstanceCanceled(IBackgroundTaskInstance sender, BackgroundTaskCancellationReason reason)
        {
            int x = 0;
            x++;
            Debug.WriteLine("Entered TaskInstanceCanceled");
        }


        public void SendToastNotification(string notificationContent)
        {
            try
            {
                ToastMessage message = JsonConvert.DeserializeObject<ToastMessage>(notificationContent);
                ToastHelper.DisplayTextToast(ToastTemplateType.ToastImageAndText01, message.FromId, message.Content, message.FromPicture);
               
            }
            catch (Exception ex)
            {
                ex.ToString();
            }
        }

        private void ToastDismissed(ToastNotification sender, ToastDismissedEventArgs args)
        {
            int x = 0;
            x++;
        }

        private void ToastActivated(ToastNotification sender, object args)
        {
            int x = 0;
            x++;
        }
    }
}
