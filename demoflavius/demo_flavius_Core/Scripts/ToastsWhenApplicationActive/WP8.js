if (entry.deviceType === "WindowsPhone8")  //Windows Phone 8
{

    push.mpns.sendRaw(entry.channel, {
        payload: 'raw string'
    }, {
        success: function (pushResponse) {
            console.log("Sent push to WP8 sent", pushResponse);
        }, error: function (error) {
            console.log('Error sending push notification wp8: ', error);
        }
    });
}