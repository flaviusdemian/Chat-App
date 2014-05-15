using System;
using System.Collections.Generic;
using System.Configuration;
using System.Linq;
using System.Web;

namespace WebPushNotifications.Helpers
{
    public class ApplicationConstants
    {
        public static string WEBSOCKETS_SERVER;
        static ApplicationConstants()
        {
            string value = ConfigurationManager.AppSettings["WEBSOCKETS_SERVER"];
            if (String.IsNullOrWhiteSpace(value) == false)
            {
                WEBSOCKETS_SERVER = value;
            }
            else
            {
                WEBSOCKETS_SERVER = "ws://192.168.16.142:84/WebSocketServer.ashx";
            }
        }

    }
}