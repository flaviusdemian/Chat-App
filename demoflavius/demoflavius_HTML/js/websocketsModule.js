
var webSocketsModule = function () {
    var websocketConnectionIsValid = false;
    var browserSupportsWebsockets = true;
    var webSocket;
    function initialize() {
        var currentUser = azureMobileServicesModule.getCurrentUser();
        if (typeof (currentUser) != "undefined" && currentUser != null) {
            if ("WebSocket" in window) {
                // Let us open a web socket
                webSocket = new WebSocket(applicationConstantsModule.webSocketUrl);
                webSocket.onopen = function () {

                    var initMessage = new Object();
                    initMessage.fromId = currentUser.providerIdLong;
                    initMessage.fromName = currentUser.providerIdLong;
                    initMessage.content = "initialize";
                    initMessage.toId = initMessage.fromId;
                    initMessage.messageType = applicationConstantsModule.messageType_Initialization;

                    webSocket.send(JSON.stringify(initMessage));
                    websocketConnectionIsValid = true;
                };
                webSocket.onmessage = function (evt) {
                    var receivedMessage = JSON.parse(evt.data);
                    receivedMessage.fromId = receivedMessage.fromId.replace(":", "_");
                    chatModule.propagateMessageToUI(receivedMessage);
                };
                webSocket.onclose = function (err) {
                    alert(JSON.stringify(err));
                    websocketConnectionIsValid = false;
                };
            } else {
                // The browser doesn't support WebSocket
                alert("WebSocket NOT supported by your Browser!");
                browserSupportsWebsockets = false;
            }
        } else {
            alert("somethign went wrong!");
        }
    };


    function sendMessage(content) {
        if (browserSupportsWebsockets == true && websocketConnectionIsValid == true) {
            webSocket.send(content);
        }
    };

    return {
        initialize: initialize,
        sendMessage: sendMessage
    };

}();
