if (entry.deviceType === "Windows8")  //Windows8
{
    push.wns.sendToastText04(entry.channel, {
        text1: userOnlineText
    }, {
        success: function (pushResponse) {
            console.log("Sent push to Windows8:", pushResponse);
        }, error: function (error) {
            console.log('Error sending push notification w8 : ', error);
        }
    });
}