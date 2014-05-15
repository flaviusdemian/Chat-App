using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Newtonsoft.Json;

namespace demo_flavius_Core.Models
{
    public class Message
    {
        [JsonProperty(PropertyName = "id")]
        public string Id { get; set; }

        [JsonProperty(PropertyName = "content")]
        public string Content { get; set; }

        [JsonProperty(PropertyName = "fromId")]
        public string FromId { get; set; }

        [JsonProperty(PropertyName = "fromName")]
        public string FromName { get; set; }

        [JsonProperty(PropertyName = "fromPicture")]
        public string FromPicture { get; set; }

        [JsonProperty(PropertyName = "toId")]
        public string ToId { get; set; }

        [JsonProperty(PropertyName = "deviceType")]
        public Enums.DeviceType DeviceType { get; set; }
    }
}
