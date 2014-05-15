function insert(item, user, request) {
    request.execute({
        success: function () {
            request.respond();
            sendBroadcastNotifications(item);
        }
    });
}

function sendBroadcastNotifications(item) {
    var userOnlineText = "A new user is online!";
    var sql = "SELECT channel, registrationId, deviceType FROM Channel ";
    //+"WHERE item.channel != channel AND item.registrationId !=registrationId";
    var userOnlineText = "A new user is online!";
    mssql.query(sql, {
        success: function (results) {
            results.forEach(function (entry) {
                console.log("deviceType is :" + entry.deviceType);
                if (entry.deviceType === "WindowsPhone8") //Windows Phone 8
                {
                    push.mpns.sendToast(entry.channel, {
                        text1: userOnlineText
                    }, {
                        success: function (pushResponse) {
                            console.log("Sent push to WP8 sent", pushResponse);
                        },
                        error: function (error) {
                            console.log('Error sending push notification: ', error);
                        }
                    });
                }

                if (entry.deviceType === "Windows8") //Windows8
                {
                    push.wns.sendToastText04(entry.channel, {
                        text1: userOnlineText
                    }, {
                        success: function (pushResponse) {
                            console.log("Sent push to Windows8:", pushResponse);
                        },
                        error: function (error) {
                            console.log('Error sending push notification: ', error);
                        }
                    });
                }

                if (item.DeviceType === "Android") //Android
                {
                    push.gcm.send(entry.registrationId, userOnlineText, {
                        success: function (response) {
                            console.log('Push notification to Android sent: ', response);
                        },
                        error: function (error) {
                            console.log('Error sending push notification: ', error);
                        }
                    });
                }
                if (item.DeviceType === "Web") {
                    //web
                }
            });
        },
        error: function (err) {
            console.log("error is: " + err);
        }
    });
}