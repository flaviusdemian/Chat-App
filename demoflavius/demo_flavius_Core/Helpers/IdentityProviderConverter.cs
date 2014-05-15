using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using demo_flavius_Core.Models;

namespace demo_flavius_Core.Helpers
{
    public class IdentityProviderConverter
    {
        public static Enums.IdentityProvider GetProvider(String providerName)
        {
            Enums.IdentityProvider returnedProvider = Enums.IdentityProvider.Facebook;
            switch (providerName)
            {
                case "Facebook":
                    returnedProvider = Enums.IdentityProvider.Facebook;
                    break;
                case "Google":
                    returnedProvider = Enums.IdentityProvider.Google;
                    break;
                case "MicrosoftAccount":
                    returnedProvider = Enums.IdentityProvider.MicrosoftAccount;
                    break;
                case "Twitter":
                    returnedProvider = Enums.IdentityProvider.Twitter;
                    break;
            }
            return returnedProvider;
        }
    }
}
