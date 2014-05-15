function insert(item, user, request) {
    var azure = require('azure');
    var hub = azure.createNotificationHubService('test',
    'Endpoint=sb://mobileservicesdemoflavius.servicebus.windows.net/;SharedAccessKeyName=DefaultFullSharedAccessSignature;SharedAccessKey=ti2grwaPTnrRtqhQV/Dj+6x3XYr7e/loqajMk6Mrbvs=');

    // Create the payload for a Windows Store app.
    var wnsPayload = '<toast><visual><binding template="ToastText02"><text id="1">New item added:</text><text id="2">' + item.username + '</text></binding></visual></toast>';

    // Execute the request and send notifications.
    request.execute({
        success: function () {
            // Write the default response and send a notification 
            // to the user on all devices by using the userId tag.
            request.respond();

            // Create a template-based payload.
            var payload = '{ "text_val" : "New item added: ' + item.text + '" }';
            // Send a notification to the current user on all platforms. 
            hub.send(user.userId, payload, function (error, outcome) {
                // Do something here with the outcome or error.
                if (error != null) {
                    console.log("error at hub send is: " + error);
                }
                else {
                    console.log("outcome at outcome is: " + JSON.stringify(outcome));
                }
            });


            // Send to Windows Store apps.
            //             if (item.deviceType === "Windows8")
            //             {
            //                hub.wns.send(user.userId, wnsPayload, 'wns/toast', function(error) {
            //                    if (error) {
            //                        console.log(" error at  hub.wns.send:" + error);
            //                    }
            //                });
            //             }
            //                          
            //             if( item.deviceType === "IOS")
            //             {
            //                  // Send to iOS apps.
            //                hub.apns.send(user.userId, { alert: item.text }, function(error) {
            //                    if (error) {
            //                        console.log(" error at  hub.apns.send:" +error);
            //                    }
            //                });
            //             }
        }
    });
}