using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace demo_flavius_Core.Helpers
{
    public class ToastMessage
    {
        [JsonProperty("fromId")]
        public string FromId { get; set; }

        [JsonProperty("content")]
        public string Content { get; set; }

        [JsonProperty("fromPicture")]
        public string FromPicture { get; set; }
    }
}
