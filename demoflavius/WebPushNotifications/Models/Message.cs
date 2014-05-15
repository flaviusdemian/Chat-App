using System;
using System.ComponentModel.DataAnnotations;
using Newtonsoft.Json;

namespace WebPushNotifications.Models
{
    public class Message
    {
        [StringLength(150, MinimumLength = 3)]
        [JsonProperty(PropertyName = "fromId")]
        public String FromId { get; set; }

        [StringLength(150, MinimumLength = 3)]
        [JsonProperty(PropertyName = "fromName")]
        public String FromName { get; set; }

        [StringLength(150, MinimumLength = 3)]
        [JsonProperty(PropertyName = "content")]
        public String Content { get; set; }

        [JsonProperty(PropertyName = "toId")]
        public String ToId { get; set; }

        [JsonProperty(PropertyName = "messageType")]
        public Enums.MessageType MessageType { get; set; }
    }
}