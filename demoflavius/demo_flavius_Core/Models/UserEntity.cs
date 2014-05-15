using System;
using Newtonsoft.Json;

namespace demo_flavius_Core.Models
{
    public class UserEntity
    {
        [JsonProperty("id")]
        public string Id { get; set; }

        [JsonProperty("name")]
        public string Name { get; set; }

        [JsonProperty("picture")]
        public string Picture { get; set; }

        [JsonProperty("token")]
        public string Token { get; set; }

        [JsonProperty("providerIdLong")]
        public string ProviderIdLong { get; set; }

        [JsonProperty("providerIdShort")]
        public string ProviderIdShort { get; set; }

        [JsonProperty("identityProvider")]
        public Enums.IdentityProvider IdentityProvider { get; set; }

        [JsonProperty("accessToken")]
        public string AccessToken { get; set; }

        [JsonProperty("accessTokenSecret")]
        public string AccessTokenSecret { get; set; }
    }
}