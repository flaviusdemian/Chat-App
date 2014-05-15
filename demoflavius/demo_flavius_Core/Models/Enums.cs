using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace demo_flavius_Core.Models
{
    public class Enums
    {
        public enum DeviceType
        {
            WindowsPhone8,
            Windows8,
            Android,
            Web
        }

        public enum IdentityProvider
        {
            Facebook,
            Google,
            Twitter,
            MicrosoftAccount
        }

        public enum ApplicationStateType
        {
            Active,
            Suspended
        }

        public enum FriendStateType
        {
            Online,
            Offline
        }
    }
}
