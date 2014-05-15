
if (entry.deviceType === "Windows8")  //Windows8
{
    push.wns.sendRaw(entry.channel, JSON.stringify(entry),
   {
       success: function (pushResponse){
           console.log('Success : ', error);
       }, error: function (error) {
           console.log('Error sending push notification w8 : ', error);
       }
   });
}