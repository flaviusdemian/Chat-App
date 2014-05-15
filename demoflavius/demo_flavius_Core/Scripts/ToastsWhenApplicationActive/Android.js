push.gcm.send(entry.registrationId, userOnlineText, {
    success: function (response) {
        console.log('Android Push notification sent: ', response);
    }, error: function (error) {
        console.log('Android Error sending push notification: ', error);
    }
});