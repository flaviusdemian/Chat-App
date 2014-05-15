
var webApiModule = function () {
    function getUsersAndCompleteProfile() {
        var currentUser = azureMobileServicesModule.getCurrentUser();
        var mobileService = azureMobileServicesModule.getMobileService();
        if (typeof (mobileService) != "undefined" && mobileService != null) {
            mobileService.invokeApi("complete_user_profile", {
                body: null,
                method: "GET"
            }).done(function (result) {
                var item = JSON.parse(result.response);
                currentUser.name = item.name;
                currentUser.picture = item.picture;
                currentUser.accessToken = item.accessToken;
                $("#currentUserPicture").attr("src", item.picture);
                $("#currentUserPicture").show();
                currentUser.accessTokenSecret = item.accessTokenSecret;

                azureMobileServicesModule.refreshAuthDisplay();

            }, function (error) {
                alert("errr is : " + error);
            });

            mobileService.invokeApi("users", {
                body: null,
                method: "GET",
                parameters: { "userId": currentUser.providerIdLong }
            }).done(function (results) {
                var items = JSON.parse(results.response);
                if (typeof (items) != "undefined" && items != null) {
                    var convertedResults = convertToUIUsers(items);
                    ko.utils.arrayPushAll(mainViewModel.availableUsers(), convertedResults);
                    mainViewModel.availableUsers(mainViewModel.availableUsers());
                }
                $("#leftChatContainer").show();

            }, function (error) {
                alert("errr is : " + error);
            });
        }
    };

    function convertToUIUsers(results) {
        var finalResults = [];
        if (typeof (results) != "undefined" && results != null) {
            var i, user;
            for (i = 0; i < results.length; i++) {
                user = new Object();
                user.id = results[i].id.replace(":", "_");
                user.name = results[i].name;
                user.picture = results[i].picture;
                user.currentMessage = ko.observable(null);

                finalResults.push(user);
            }
        }
        return finalResults;
    };

    return {
        getUsersAndCompleteProfile: getUsersAndCompleteProfile
    };
}();
