using System;
using demo_flavius_Core.Models;
using Newtonsoft.Json.Linq;

namespace demo_flavius_Core.Helpers
{
    public class IdentityProviderParser
    {
        public static string GetShortProvider(string longProviderId)
        {
            try
            {
                string[] result = longProviderId.Split(new[] {":"}, StringSplitOptions.RemoveEmptyEntries);
                if (result != null && result.Length == 2)
                {
                    return result[1];
                }
            }
            catch (Exception ex)
            {
                ex.ToString();
            }
            return null;
        }

        public static void RetrieveTokenObjectSpecificToIdentityProvider(Enums.IdentityProvider identityProvider,
            string accessToken, string accessTokenSecret, out dynamic jToken)
        {
            jToken = new JObject();
            switch (identityProvider)
            {
                case Enums.IdentityProvider.Facebook:
                    jToken.access_token = accessToken;
                    break;
                case Enums.IdentityProvider.Twitter:
                    //jToken.access_token = accessToken;
                    jToken.oauth_token = accessToken;
                    jToken.code = "dddd";
                    //jToken.oauth_token = accessToken;
                    //jToken.accessToken = accessToken;
                    //jToken.accessTokenSecret = accessTokenSecret;
                    //jToken.access_token_secret = accessTokenSecret;
                    //jToken.oauth_token_secret = accessTokenSecret;
                    //jToken.accessTokenSecret = accessTokenSecret;

                    break;
                case Enums.IdentityProvider.Google:
                    jToken.access_token = accessToken;
                    jToken.id_token = accessToken;
                    break;
                case Enums.IdentityProvider.MicrosoftAccount:
                    jToken.authenticationToken = accessToken;
                    break;
            }
        }
    }
}