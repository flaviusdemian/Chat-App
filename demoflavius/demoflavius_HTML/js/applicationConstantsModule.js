
var applicationConstantsModule = function () {

    //var webSocketUrl = "ws://193.226.9.134:8000/WebPushNotificationApi/WebPushNotificationWebSocketsServer.ashx";
    var webSocketUrl = "ws://localhost:82/WebPushNotificationWebSocketsServer.ashx";
    var messageType_Initialization = 0;
    var messageType_Chat = 1;
    var messageType_Broadcast = 2;
    var usersMethodPath = "user";
    return {
        webSocketUrl: webSocketUrl,
        usersMethodPath: usersMethodPath,
        messageType_Initialization: messageType_Initialization,
        messageType_Chat: messageType_Chat,
        messageType_Broadcast: messageType_Broadcast
    };
}();
