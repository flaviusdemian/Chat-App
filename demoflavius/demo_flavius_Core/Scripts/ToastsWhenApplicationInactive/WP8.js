if (entry.deviceType === "WindowsPhone8")  //Windows Phone 8
{
    push.mpns.sendToast(entry.channel, {
        text1: 'Bold text:',
        text2: 'normal text',
        param: '/Page1.xaml?item=5'
    }, {
        success: function (pushResponse) {
            console.log("Sent push to WP8 sent", pushResponse);
        }, error: function (error) {
            console.log('Error sending push notification wp8: ', error);
        }
    });
}