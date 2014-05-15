using System;
using Newtonsoft.Json;
using Newtonsoft.Json.Serialization;

namespace WebPushNotifications.Helpers
{
    public class SerializationHelper
    {
        public static String Serialize(Object input)
        {
            var jsonSerializerSettings = new JsonSerializerSettings
            {
                ContractResolver = new CamelCasePropertyNamesContractResolver()
            };
            return JsonConvert.SerializeObject(input, Formatting.Indented, jsonSerializerSettings);
        }
    }
}