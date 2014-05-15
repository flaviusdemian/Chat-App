using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Threading.Tasks;
using System.Web.Http;
using WebPushNotifications.Helpers;
using WebPushNotifications.Models;

namespace WebPushNotifications.Controllers
{
    public class WebPushNotificationsController : ApiController
    {
        [HttpGet]
        [ActionName("Test")]
        public HttpResponseMessage Test()
        {
            return Request.CreateResponse(HttpStatusCode.OK, "success...");
        }

        [HttpPost]
        [ActionName("Send")]
        public async Task<HttpResponseMessage> Send([FromBody] Message message)
        {
            if (ModelState.IsValid == true)
            {
                await WebPushNotificationWebSocketsServer.WebPushNotificationWebSocketsServer.SendChatMessage(message);
                return Request.CreateResponse(HttpStatusCode.OK, "request sent...");
            }
            return Request.CreateResponse(HttpStatusCode.BadRequest, "invalid data!");
        }
    }
}
