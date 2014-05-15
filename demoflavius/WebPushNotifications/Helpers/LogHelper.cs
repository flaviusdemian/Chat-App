using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Web;

namespace WebPushNotifications.Helpers
{
    public class LogHelper
    {
        public static void LogInfo(string content)
       {
           Trace.WriteLine(String.Format("{0} : {1}", DateTime.UtcNow.ToLongTimeString(), content));
       }
    }
}