// This define is needed to enable System.Diagnostics.Trace methods. For more information go to:
// http://msdn.microsoft.com/en-us/library/system.diagnostics.trace(v=vs.110).aspx

#define TRACE


using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Net.WebSockets;
using System.Text;
using System.Threading;
using System.Threading.Tasks;
using System.Web;
using System.Web.WebSockets;
using Newtonsoft.Json;
using WebPushNotifications;
using WebPushNotifications.Helpers;
using WebPushNotifications.Models;

namespace WebPushNotificationWebSocketsServer
{
    /// <summary>
    ///     Summary description for WebSocketServer
    /// </summary>
    public class WebPushNotificationWebSocketsServer : IHttpHandler
    {
        private const int MaxBufferSize = 64 * 1024;

        //private static readonly MessageFactory MessageFactory = MessageFactory.GetInstance();

        #region IHttpHandler Members

        public bool IsReusable
        {
            get { return false; }
        }

        public void ProcessRequest(HttpContext context)
        {
            try
            {
                context.AcceptWebSocketRequest(WebSocketLoop);
            }
            catch (Exception ex)
            {
                Trace.WriteLine(ex);
                context.Response.StatusCode = 500;
                context.Response.End();
            }
        }

        #endregion IHttpHandler Members

        private async Task WebSocketLoop(AspNetWebSocketContext context)
        {
            WebSocket socket = null;
            try
            {
                socket = context.WebSocket;
                try
                {
                    while (socket.State == WebSocketState.Open)
                    {
                        string serializedMessage = await ReadSerializedMessage(socket);
                        LogHelper.LogInfo("serializedMessage e: " + serializedMessage);
                        if (String.IsNullOrWhiteSpace(serializedMessage))
                        {
                            LogHelper.LogInfo(SerializationHelper.Serialize("serializedMessage is empty!"));
                            return;
                        }

                        Message message = JsonConvert.DeserializeObject<Message>(serializedMessage);

                        if (message == null || String.IsNullOrWhiteSpace(message.FromId) == true)
                        {
                            LogHelper.LogInfo("invalid messsage!");
                            return;
                        }

                        AddOrUpdateEntry(message.FromId, socket);

                        switch (message.MessageType)
                        {

                            case Enums.MessageType.Initialization:
                                break;
                            case Enums.MessageType.Chat:
                                await SendChatMessage(message);
                                break;
                            case Enums.MessageType.Broadcast:
                                break;
                        }
                    }
                }
                catch (Exception ex)
                {
                    LogHelper.LogInfo(SerializationHelper.Serialize(ex.Message));
                }
            }
            catch (Exception ex)
            {
                LogHelper.LogInfo(SerializationHelper.Serialize(ex.Message));
            }
        }

        #region private methods

        private void AddOrUpdateEntry(String sender, WebSocket socket)
        {
            try
            {
                if (WebApiApplication.UserSocketAssociations.ContainsKey(sender) == false)
                {
                    WebApiApplication.UserSocketAssociations.Add(sender, socket);
                }
                else
                {
                    WebApiApplication.UserSocketAssociations[sender] = socket;
                }

            }
            catch (Exception ex)
            {
                LogHelper.LogInfo(ex.Message);
            }
        }


        private async Task<String> ReadSerializedMessage(WebSocket socket)
        {
            var byteBuffer = new byte[MaxBufferSize];
            var arraySegmentBuffer = new ArraySegment<byte>(byteBuffer);
            WebSocketReceiveResult result = await socket.ReceiveAsync(arraySegmentBuffer, CancellationToken.None);
            string ret = "";

            if (result.MessageType == WebSocketMessageType.Close)
            {
                await
                    socket.CloseAsync(result.CloseStatus.GetValueOrDefault(), result.CloseStatusDescription,
                        CancellationToken.None);
            }
            else
            {
                int messageSize = 0;

                for (messageSize = result.Count; !result.EndOfMessage; messageSize += result.Count)
                {
                    result = await socket.ReceiveAsync(new ArraySegment<byte>(byteBuffer, messageSize, MaxBufferSize - messageSize), CancellationToken.None);
                }
                if (result.MessageType == WebSocketMessageType.Text)
                {
                    ret = Encoding.UTF8.GetString(byteBuffer, 0, messageSize);
                }
            }
            return ret;
        }

        public static async Task SendChatMessage(Message message)
        {
            try
            {
                if (WebApiApplication.UserSocketAssociations.ContainsKey(message.ToId) == true)
                {
                    var socket = WebApiApplication.UserSocketAssociations[message.ToId];
                    var outputBuffer = new ArraySegment<byte>(Encoding.UTF8.GetBytes(SerializationHelper.Serialize(message)));
                    await socket.SendAsync(outputBuffer, WebSocketMessageType.Text, true, CancellationToken.None);
                }
                else
                {
                    LogHelper.LogInfo("This key does not exist!");
                }
            }
            catch (Exception ex)
            {
                LogHelper.LogInfo(ex.Message);
            }
        }

        #endregion
    }
}