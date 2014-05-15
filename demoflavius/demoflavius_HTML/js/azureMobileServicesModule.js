
var azureMobileServicesModule = function () {
    var currentUser = new Object();
    var currentChannel = new Object();
    var currentProvider = null;

    var mobileService = new WindowsAzure.MobileServiceClient('https://demoflavius.azure-mobile.net/', 'BAnpBgjwvAiseBuqYzJohLfKjwSWbD28');

    var userTable = mobileService.getTable("UserEntity");
    var channelTable = mobileService.getTable("Channel");
    var messageTable = mobileService.getTable("Message");

    //identity zone
    function refreshAuthDisplay() {
        var isLoggedIn = mobileService.currentUser !== null;
        $("#logged-in").toggle(isLoggedIn);
        $("#logged-out").toggle(!isLoggedIn);
        currentUser = azureMobileServicesModule.getCurrentUser();

        if (isLoggedIn && typeof (currentUser) != "undefined") {
            $("#login-name").text(currentUser.name);
        }
    }

    function logIn(provider) {
        currentProvider = provider;
        logOut();

        mobileService.login(provider).then(storeCurrentUserProfile, function (error) {
            alert(error);
        });
    }

    function logOut() {
        mobileService.logout();
        $("#leftChatContainer").hide();
        $("#bottomChatContainer").hide();
        $("#currentUserPicture").hide();
        $("#currentUserPicture").attr("src", "");

        mainViewModel.availableChatEntries([]);
        mainViewModel.availableExpandedChatEntries([]);
        mainViewModel.availableMinimizedChatEntries([]);
        mainViewModel.availableUsers([]);
        
        refreshAuthDisplay();

        $('#summary').html('<strong>You must login to access data.</strong>');
    }
    //end identity zone

    function storeCurrentUserProfile() {
        currentUser.id = guidGeneratorModule.generateGuid();
        currentUser.providerIdLong = mobileService.currentUser.userId;
        currentUser.providerIdShort = identityProviderParserModule.getShrotProvider(mobileService.currentUser.userId);
        currentUser.token = mobileService.currentUser.mobileServiceAuthenticationToken;
        currentUser.identityProvider = currentProvider;
        try {
            userTable.insert(currentUser);

            currentChannel.id = guidGeneratorModule.generateGuid();
            currentChannel.channelUri = "";
            currentChannel.deviceType = "Web";
            currentChannel.userId = mobileService.currentUser.userId;
            currentChannel.registrationId = "";
            try {
                channelTable.insert(currentChannel);
                webSocketsModule.initialize();
                webApiModule.getUsersAndCompleteProfile();
            }
            catch (e) {
                console.log("channelTable " + e.toString());
            }
        }
        catch (e) {
            console.log("userTable " + e.toString());
        }
    }

    function getCurrentUser() {
        return currentUser;
    };

    function getUserTable() {
        return userTable;
    }

    function getChannelTable() {
        return channelTable;
    }

    function getMessageTable() {
        return messageTable;
    }

    function getMobileService() {
        return mobileService;
    }

    function initialize() {
        $('#summary').html('<strong>You must login to access data.</strong>');
        refreshAuthDisplay();
    };

    return {
        initialize: initialize,
        logIn: logIn,
        logOut: logOut,
        getCurrentUser: getCurrentUser,
        refreshAuthDisplay: refreshAuthDisplay,
        getUserTable: getUserTable,
        getMobileService: getMobileService,
        storeCurrentUserProfile: storeCurrentUserProfile,
        getChannelTable: getChannelTable,
        getMessageTable: getMessageTable
    };
}();
