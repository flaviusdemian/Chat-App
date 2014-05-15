using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Windows.UI.Notifications;
using NotificationsExtensions.ToastContent;
using Windows.Foundation;
using demoflavius_W8.ViewModel;
using Microsoft.Practices.ServiceLocation;

namespace demoflavius_W8.Helpers
{
    public class ToastHelper
    {
        public static void DisplayTextToast(ToastTemplateType templateType, string fromId, string content = null, string imageUri = null)
        {
            // Creates a toast using the notification object model, which is another project
            // in this solution.  For an example using Xml manipulation, see the function
            // DisplayToastUsingXmlManipulation below.
            IToastNotificationContent toastContent = null;

            if (templateType == ToastTemplateType.ToastText01)
            {
                IToastText01 templateContent = ToastContentFactory.CreateToastText01();
                templateContent.TextBodyWrap.Text = "Body text that wraps over three lines";
                toastContent = templateContent;
            }
            else if (templateType == ToastTemplateType.ToastText02)
            {
                IToastText02 templateContent = ToastContentFactory.CreateToastText02();
                templateContent.TextHeading.Text = "Heading text";
                templateContent.TextBodyWrap.Text = "Body text that wraps over two lines";
                toastContent = templateContent;
            }
            else if (templateType == ToastTemplateType.ToastText03)
            {
                IToastText03 templateContent = ToastContentFactory.CreateToastText03();
                templateContent.TextHeadingWrap.Text = "Heading text that is very long and wraps over two lines";
                templateContent.TextBody.Text = "Body text";
                toastContent = templateContent;
            }
            else if (templateType == ToastTemplateType.ToastText04)
            {
                IToastText04 templateContent = ToastContentFactory.CreateToastText04();
                templateContent.TextHeading.Text = "Heading text";
                templateContent.TextBody1.Text = "First body text";
                templateContent.TextBody2.Text = "Second body text";
                toastContent = templateContent;
            }
            else if (templateType == ToastTemplateType.ToastImageAndText01)
            {
                IToastImageAndText01 templateContent = ToastContentFactory.CreateToastImageAndText01();
                if (String.IsNullOrWhiteSpace(content) == false)
                {
                    templateContent.TextBodyWrap.Text = content;
                }
                else
                {
                    templateContent.TextBodyWrap.Text = "text here!";
                }

                if (String.IsNullOrWhiteSpace(imageUri) == false)
                {
                    templateContent.Image.Src = imageUri;
                }
                else
                {
                    templateContent.Image.Src = "http://singularlabs.com/wp-content/uploads/2011/11/System-Ninja-2.2.png";
                }

                toastContent = templateContent;
            }

            // Create a toast, then create a ToastNotifier object to show
            // the toast
            ToastNotification toast = toastContent.CreateNotification();
            Dictionary<String, String> args = new Dictionary<String, String>();
            args.Add("fromId", fromId);
            args.Add("content", content);
            //toast.Activated += ToastTapped(toast, args);
            toast.Activated += new TypedEventHandler<ToastNotification, object>((sender, e) => ToastTapped(toast, args));
            // If you have other applications in your package, you can specify the AppId of
            // the app to create a ToastNotifier for that application
            ToastNotificationManager.CreateToastNotifier().Show(toast);

        }

        private static void ToastTapped(ToastNotification sender, object args)
        {
            try
            {
                if (args is Dictionary<String, String>)
                {
                    Dictionary<String, String> values = args as Dictionary<String, String>;
                    ChatPageViewModel chatPageViewModel = ServiceLocator.Current.GetInstance<ChatPageViewModel>();

                    chatPageViewModel.SetSelectedUser(values["fromId"]);
                }
            }
            catch (Exception ex)
            {
                ex.ToString();
            }
        }
    }
}
