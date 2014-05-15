using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Newtonsoft.Json;

namespace demo_flavius_Core.Models
{
    public class Channel
    {
        [JsonProperty("id")]
        public string Id { get; set; }

        [JsonProperty("deviceType")]
        public Enums.DeviceType DeviceType { get; set; }

        [JsonProperty("channelUri")]
        public String ChannelUri { get; set; }

        [JsonProperty("registrationId")]
        public String RegistrationId { get; set; }

        [JsonProperty("userId")]
        public String UserId { get; set; }

    }
}
